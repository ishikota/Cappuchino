package jp.ikota.cappuchino;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;


public class Cappuchino {

    public static ViewMatcherInteraction expect(final Matcher<View> viewMatcher) {
        return new ViewMatcherInteraction(onView(viewMatcher));
    }

    public static Matcher<View> id(int id) {
        return withId(id);
    }

    public static Matcher<View> text(String text) {
        return withText(text);
    }

    public static Matcher<View> text(int resourceId) {
        return withText(resourceId);
    }

    public static class ViewMatcherInteraction {
        private ViewInteraction mViewInteraction;

        private ViewMatcherInteraction(ViewInteraction viewInteraction) {
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

        public void exists() {
            mViewInteraction.check(matches(anything()));
        }

//        public void listItemCountIs(int expected) {
//            mViewInteraction.check(matches(withListItemCount(expected)));
//        }

    }


}
