package jp.ikota.cappuchino.sampledetail.espresso;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import jp.ikota.cappuchino.R;
import jp.ikota.cappuchino.sampledetail.SampleDetailActivity;
import jp.ikota.cappuchino.samplelist.SampleListFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class SampleDetailActivityTest {

    private Intent mIntent;

    @Rule
    public ActivityTestRule<SampleDetailActivity> activityRule = new ActivityTestRule<>(
            SampleDetailActivity.class,
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    @Before
    public void setUp(){
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        Context context = instrumentation.getTargetContext();
        mIntent = new Intent(context, SampleDetailActivity.class);
    }

    @Test
    public void preConditions() {
        // Setup sample intent
        mIntent.putExtra(SampleListFragment.TITLE, "Cappuchino");
        mIntent.putExtra(SampleListFragment.LIKE, 52);
        activityRule.launchActivity(mIntent);
        // Check view existence
        onView(withId(android.R.id.title)).check(matches(anything()));
        onView(withId(R.id.like_text)).check(matches(anything()));
        onView(withId(R.id.like_button)).check(matches(anything()));
        // Check default button state
        onView(withId(R.id.like_button)).check(matches(withText(R.string.like)));
        // Check if set content which received bia intent
        onView(withId(android.R.id.title)).check(matches(withText("Cappuchino")));
        onView(withId(R.id.like_text)).check(matches(withText("52 likes")));
    }

    @Test
    public void clickLike() {
        // Setup sample intent
        mIntent.putExtra(SampleListFragment.TITLE, "Cappuchino");
        mIntent.putExtra(SampleListFragment.LIKE, 52);
        activityRule.launchActivity(mIntent);
        // Check if like count up and button text changes to UNLIKE
        onView(withId(R.id.like_button)).perform(click());
        onView(withId(R.id.like_text)).check(matches(withText("53 likes")));
        onView(withId(R.id.like_button)).check(matches(withText(R.string.unlike)));
        // Check if like count down and button text changes to LIKE
        onView(withId(R.id.like_button)).perform(click());
        onView(withId(R.id.like_text)).check(matches(withText("52 likes")));
        onView(withId(R.id.like_button)).check(matches(withText(R.string.like)));
    }

    @Test
    public void noLikeCase() {
        // Setup sample intent
        mIntent.putExtra(SampleListFragment.TITLE, "Cappuchino");
        mIntent.putExtra(SampleListFragment.LIKE, 0);
        activityRule.launchActivity(mIntent);
        onView(withId(R.id.like_text)).check(matches(withText(R.string.nolike)));  // not 0 likes
    }

    @Test
    public void oneLikeCase() {
        mIntent.putExtra(SampleListFragment.TITLE, "Cappuchino");
        mIntent.putExtra(SampleListFragment.LIKE, 1);
        activityRule.launchActivity(mIntent);
        onView(withId(R.id.like_text)).check(matches(withText("1 like")));  // not 1 likes
    }

}
