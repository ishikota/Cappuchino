package jp.ikota.cappuchino.matcher;

import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class ViewMatcherWrapper {

    public static Matcher<View> id(int id) {
        return withId(id);
    }

    public static Matcher<View> text(String text) {
        return withText(text);
    }

    public static Matcher<View> text(int resourceId) {
        return withText(resourceId);
    }

}
