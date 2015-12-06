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

    public void swipeRight() {
        mViewInteraction.perform(android.support.test.espresso.action.ViewActions.swipeRight());
    }

    public void swipeLeft() {
        mViewInteraction.perform(android.support.test.espresso.action.ViewActions.swipeLeft());
    }

    public void swipeUp() {
        mViewInteraction.perform(android.support.test.espresso.action.ViewActions.swipeUp());
    }

    public void swipeDown() {
        mViewInteraction.perform(android.support.test.espresso.action.ViewActions.swipeDown());
    }

    public void clickItemAtPosition(int position) {
        mViewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));
    }

    public void scrollToPosition(int position) {
        mViewInteraction.perform(RecyclerViewActions.scrollToPosition(position));
    }

}
