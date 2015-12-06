package jp.ikota.cappuchino.matcher;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import org.hamcrest.Matcher;

import jp.ikota.cappuchino.matcher.custommatcher.CustomMatcher;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static jp.ikota.cappuchino.matcher.custommatcher.CappuchinoMatcher.withListItemCount;
import static jp.ikota.cappuchino.matcher.custommatcher.CustomMatcher.withCustomMatch;
import static org.hamcrest.Matchers.anything;

public class ViewMatcherInteraction {

    public final NotViewMatcherInteraction not;

    private final ViewInteraction mViewInteraction;
    protected boolean not_flg = false;

    public ViewMatcherInteraction(ViewInteraction viewInteraction) {
        mViewInteraction = viewInteraction;
        not = new NotViewMatcherInteraction(viewInteraction);
    }

    // This constructor is called from NotViewMatcherInteraction
    // to avoid circular execution of calling constructor.
    public ViewMatcherInteraction(ViewInteraction viewInteraction, Object anything) {
        mViewInteraction = viewInteraction;
        not = null;
    }

    public void isVisible() {
        match((withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    public void isInvisible() {
        match(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE));
    }

    public void isGone() {
        match(withEffectiveVisibility(ViewMatchers.Visibility.GONE));
    }

    public void isDisplayed() {
        match(android.support.test.espresso.matcher.ViewMatchers.isDisplayed());
    }

    public void isDisplayingAtLeast(int areaPercentage) {
        match(android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast(areaPercentage));
    }

    public void hasText(String text) {
        match(withText(text));
    }

    public void hasText(int resourceId) {
        match(withText(resourceId));
    }

    public void exists() {
        mViewInteraction.check(matches(anything()));
    }

    public void listItemCountIs(int expected_item_count) {
        match(withListItemCount(expected_item_count));
    }

    public void should(CustomMatcher.MatcherRule rule) {
        match(withCustomMatch(rule));
    }

    public class NotViewMatcherInteraction extends ViewMatcherInteraction {
        @SuppressWarnings("unused")
        private final NotViewMatcherInteraction not = null;

        public NotViewMatcherInteraction(ViewInteraction viewInteraction) {
            super(viewInteraction, new Object());
            not_flg = true;
        }
    }

    private void match(Matcher<? super View> matcher) {
        if(not_flg) {
            mViewInteraction.check(matches(org.hamcrest.Matchers.not(matcher)));
        } else {
            mViewInteraction.check(matches(matcher));
        }
    }

}
