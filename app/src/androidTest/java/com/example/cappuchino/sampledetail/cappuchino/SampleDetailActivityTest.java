package com.example.cappuchino.sampledetail.cappuchino;


import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;

import com.example.cappuchino.R;
import com.example.cappuchino.sampledetail.SampleDetailActivity;
import com.example.cappuchino.samplelist.SampleListFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import jp.ikota.cappuchino.Cappuchino;

import static jp.ikota.cappuchino.matcher.ViewMatcherWrapper.id;

@RunWith(AndroidJUnit4.class)
public class SampleDetailActivityTest extends Cappuchino<SampleDetailActivity> {

    private Intent mIntent;

    @Before
    public void setUp(){
        mIntent = new Intent(getTargetContext(), SampleDetailActivity.class);
    }

    @Test
    public void preConditions() {
        // Setup sample intent
        mIntent.putExtra(SampleListFragment.TITLE, "Cappuchino");
        mIntent.putExtra(SampleListFragment.LIKE, 52);
        launchActivity(mIntent);
        // Check view existence
        expect(id(android.R.id.title)).exists();
        expect(id(R.id.like_text)).exists();
        expect(id(R.id.like_button)).exists();
        // Check default button state
        expect(id(R.id.like_button)).hasText(R.string.like);
        // Check if set content which received bia intent
        expect(id(android.R.id.title)).hasText("Cappuchino");
        expect(id(R.id.like_text)).hasText("52 likes");
    }

    @Test
    public void clickLike() {
        // Setup sample intent
        mIntent.putExtra(SampleListFragment.TITLE, "Cappuchino");
        mIntent.putExtra(SampleListFragment.LIKE, 52);
        launchActivity(mIntent);
        // Check if like count up and button text changes to UNLIKE
        perform(id(R.id.like_button)).clickView();
        expect(id(R.id.like_text)).hasText("53 likes");
        expect(id(R.id.like_button)).hasText(R.string.unlike);
        // Check if like count down and button text changes to LIKE
        perform(id(R.id.like_button)).clickView();
        expect(id(R.id.like_text)).hasText("52 likes");
        expect(id(R.id.like_button)).hasText(R.string.like);
    }

    @Test
    public void noLikeCase() {
        // Setup sample intent
        mIntent.putExtra(SampleListFragment.TITLE, "Cappuchino");
        mIntent.putExtra(SampleListFragment.LIKE, 0);
        launchActivity(mIntent);
        expect(id(R.id.like_text)).hasText(R.string.nolike); // not 0 likes
    }

    @Test
    public void oneLikeCase() {
        mIntent.putExtra(SampleListFragment.TITLE, "Cappuchino");
        mIntent.putExtra(SampleListFragment.LIKE, 1);
        launchActivity(mIntent);
        expect(id(R.id.like_text)).hasText("1 like"); // not 1 likes
    }

}
