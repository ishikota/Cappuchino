package jp.ikota.cappuchino.matcher;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static jp.ikota.cappuchino.matcher.custommatcher.CustomMatcher.withListItemCount;
import static org.hamcrest.Matchers.anything;

public class ViewMatcherInteraction {

    private ViewInteraction mViewInteraction;

    public ViewMatcherInteraction(ViewInteraction viewInteraction) {
        mViewInteraction = viewInteraction;
    }

    public void isVisible() {
        mViewInteraction.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    public void isInvisible() {
        mViewInteraction.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    public void isGone() {
        mViewInteraction.check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    public void hasText(String text) {
        mViewInteraction.check(matches(withText(text)));
    }

    public void hasText(int resourceId) {
        mViewInteraction.check(matches(withText(resourceId)));
    }

    public void exists() {
        mViewInteraction.check(matches(anything()));
    }

    public void listItemCountIs(int expected_item_count) {
        mViewInteraction.check(matches(withListItemCount(expected_item_count)));
    }

}
