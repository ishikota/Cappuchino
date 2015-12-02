package jp.ikota.cappuchino.action;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;

import static android.support.test.espresso.action.ViewActions.click;


public class ViewActionInteraction {

    private ViewInteraction mViewInteraction;

    public ViewActionInteraction(ViewInteraction viewInteraction) {
        mViewInteraction = viewInteraction;
    }

    public void clickView() {
        mViewInteraction.perform(click());
    }

    public void clickItemAtPosition(int position) {
        mViewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));
    }

    public void scrollToPosition(int position) {
        mViewInteraction.perform(RecyclerViewActions.scrollToPosition(position));
    }

}
