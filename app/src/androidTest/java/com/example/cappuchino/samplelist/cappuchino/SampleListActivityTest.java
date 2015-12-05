package com.example.cappuchino.samplelist.cappuchino;

import android.app.Activity;
import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;

import com.example.cappuchino.sampledetail.SampleDetailActivity;
import com.example.cappuchino.samplelist.SampleListActivity;
import com.example.cappuchino.samplelist.SampleListFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import jp.ikota.cappuchino.Cappuchino;
import jp.ikota.cappuchino.assertion.LaunchActivityAssertion;

import static jp.ikota.cappuchino.matcher.ViewMatcherWrapper.id;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SampleListActivityTest extends Cappuchino<SampleListActivity> {

    private Intent mIntent;

    @Before
    public void setUp(){
        mIntent = new Intent(getTargetContext(), SampleListActivity.class);
    }

    @Test
    public void preConditions() {
        launchActivity(mIntent);
        // Check view existence
        expect(id(android.R.id.list)).exists();
        expect(id(android.R.id.empty)).exists();
        expect(id(android.R.id.progress)).exists();
        // empty view and should be gone at first
        expect(id(android.R.id.empty)).isGone();
    }

    @Test
    public void showProgressAtFirst() {
        launchActivity(mIntent);
        // Check if progress is visible
        expect(id(android.R.id.progress)).isVisible();
        // Wait item load
        listIdlingTarget(android.R.id.list).waitFirstItemLoad();
        // Check if progress is gone
        expect(id(android.R.id.progress)).isGone();
    }

    @Test
    public void loadItems() {
        activityRule.launchActivity(mIntent);

        // Check if properly load first 30 items
        listIdlingTarget(android.R.id.list).waitFirstItemLoad();
        expect(id(android.R.id.list)).listItemCountIs(30);

        // scroll view to invoke next item load
        perform(id(android.R.id.list)).scrollToPosition(25);

        // Check if properly load next 30 items
        listIdlingTarget(android.R.id.list).waitUntilItemCountGraterThan(30);
        expect(id(android.R.id.list)).listItemCountIs(60);
    }

    @Test
    public void showEmptyView() {
        mIntent.putExtra(SampleListActivity.EMPTY_MODE_KEY, true);
        launchActivity(mIntent);

        // Check if emptyView's visibility is gone
        expect(id(android.R.id.empty)).isGone();

        // Wait until item loading is done
        viewIdlingTarget(android.R.id.progress).waitUntilViewIsGone();

        //Check if emptyView's visibility is visible
        expect(id(android.R.id.empty)).isVisible();
    }

    @Test
    public void openDetailScreen() {
        launchActivity(mIntent);
        // Wait until first item is loaded to click item
        listIdlingTarget(android.R.id.list).waitFirstItemLoad();

        LaunchActivityAssertion
                .setTimeOut(1000)
                .launch(SampleDetailActivity.class, new LaunchActivityAssertion.LaunchMethod() {
                    @Override
                    public void launchActivity() {
                        perform(id(android.R.id.list)).clickItemAtPosition(0);
                    }
                })
                .asserts(new LaunchActivityAssertion.ActivityAssertion() {
                    @Override
                    public void assertActivity(Activity activity, Intent intent) {
                        assertEquals("Launched Activity is not SampleDetailActivity",
                                SampleDetailActivity.class, activity.getClass());

                        // Check if correct Intent is passed to launched activity
                        String expected_title = "item: "+0;
                        int expected_like = 0;
                        assertEquals(expected_title, intent.getStringExtra(SampleListFragment.TITLE));
                        assertEquals(expected_like, intent.getIntExtra(SampleListFragment.LIKE, -1));
                    }
                });
    }

}
