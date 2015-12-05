package com.example.cappuchino.samplelist.espresso;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.cappuchino.sampledetail.SampleDetailActivity;
import com.example.cappuchino.samplelist.SampleListActivity;
import com.example.cappuchino.samplelist.SampleListFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class SampleListActivityTest {

    private Intent mIntent;

    @Rule
    public ActivityTestRule<SampleListActivity> activityRule = new ActivityTestRule<>(
            SampleListActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    @Before
    public void setUp(){
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Context context = instrumentation.getTargetContext();
        mIntent = new Intent(context, SampleListActivity.class);
    }

    @Test
    public void preConditions() {
        activityRule.launchActivity(mIntent);
        // Check view existence
        onView(withId(android.R.id.list)).check(matches(anything()));
        onView(withId(android.R.id.empty)).check(matches(anything()));
        onView(withId(android.R.id.progress)).check(matches(anything()));
        // empty view and should be gone at first
        onView(withId(android.R.id.empty)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void showProgressAtFirst() {
        SampleListActivity activity = activityRule.launchActivity(mIntent);
        RecyclerView recyclerView = getList(getFragment(activity));

        // Check if progress is visible
        onView(withId(android.R.id.progress)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Check if progress is gone after item load
        IdlingResource idlingResource = new ListCountIdlingResource(recyclerView, 1);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(android.R.id.progress)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void loadItems() {
        SampleListActivity activity = activityRule.launchActivity(mIntent);
        RecyclerView recyclerView = getList(getFragment(activity));

        // Check if properly load first 30 items
        IdlingResource idlingResource = new ListCountIdlingResource(recyclerView, 1);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(android.R.id.list)).perform(scrollToPosition(25));
        Espresso.unregisterIdlingResources(idlingResource);
        onView(withId(android.R.id.list)).check(matches(withListItemCount(30)));

        // Check if properly load next 30 items
        IdlingResource idlingResource2 = new ListCountIdlingResource(recyclerView, 31);
        Espresso.registerIdlingResources(idlingResource2);
        onView(withId(android.R.id.list)).perform(scrollToPosition(40));
        Espresso.unregisterIdlingResources(idlingResource2);
        onView(withId(android.R.id.list)).check(matches(withListItemCount(60)));
    }

    @Test
    public void showEmptyView() {
        mIntent.putExtra(SampleListActivity.EMPTY_MODE_KEY, true);
        SampleListActivity activity = activityRule.launchActivity(mIntent);
        ProgressBar progress = getProgress(getFragment(activity));

        // Check if emptyView's visibility is Gone
        onView(withId(android.R.id.empty)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        // Wait until item loading is done and check if emptyView's visibility is VISIBLE
        VisibilityIdlingResource idlingResource = new VisibilityIdlingResource(progress, View.GONE);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(android.R.id.empty)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        Espresso.unregisterIdlingResources(idlingResource);
    }

    @Test
    public void openDetailScreen() {
        SampleListActivity activity = activityRule.launchActivity(mIntent);
        RecyclerView recyclerView = getList(getFragment(activity));

        // Set up an ActivityMonitor
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                instrumentation.addMonitor(SampleDetailActivity.class.getName(), null, false);

        // Wait first item loading
        IdlingResource idlingResource = new ListCountIdlingResource(recyclerView, 1);
        Espresso.registerIdlingResources(idlingResource);
        onView(withId(android.R.id.list)).check(matches(anything()));
        Espresso.unregisterIdlingResources(idlingResource);

        // Click first list item and check if correct activity is launched
        onView(withId(android.R.id.list)).perform(actionOnItemAtPosition(0, click()));
        Activity detail_activity = receiverActivityMonitor.waitForActivityWithTimeout(1000);
        instrumentation.removeMonitor(receiverActivityMonitor);
        assertNotNull("SampleDetailActivity is null", detail_activity);
        assertEquals("Launched Activity is not SampleDetailActivity",
                SampleDetailActivity.class, detail_activity.getClass());

        // Check if correct Intent is passed to launched activity
        String expected_title = "item: "+0;
        int expected_like = 0;
        Intent intent = detail_activity.getIntent();
        assertEquals(expected_title, intent.getStringExtra(SampleListFragment.TITLE));
        assertEquals(expected_like, intent.getIntExtra(SampleListFragment.LIKE, -1));
    }

    private SampleListFragment getFragment(AppCompatActivity activity) {
        return (SampleListFragment)activity.getSupportFragmentManager()
                .findFragmentByTag(SampleListFragment.class.getSimpleName());
    }

    @SuppressWarnings("ConstantConditions")
    private RecyclerView getList(Fragment fragment) {
        return (RecyclerView)fragment.getView().findViewById(android.R.id.list);
    }

    @SuppressWarnings("ConstantConditions")
    private ProgressBar getProgress(Fragment fragment) {
        return (ProgressBar)fragment.getView().findViewById(android.R.id.progress);
    }


    /**
     * Check if child count of target RecyclerView matches to expected one
     * @param expected_count expected item count in target RecyclerView
     */
    public static Matcher<View> withListItemCount(final int expected_count) {
        final Matcher<Integer> matcher = is(expected_count);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            @Override
            protected boolean matchesSafely(RecyclerView recyclerView) {
                Log.i("withChildCount", "item num is " + (recyclerView.getAdapter().getItemCount()));
                return matcher.matches(recyclerView.getAdapter().getItemCount());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with childCount: ");
                matcher.describeTo(description);
            }
        };
    }

}
