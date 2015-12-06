package jp.ikota.cappuchino.matcher.custommatcher;

import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.hamcrest.core.Is.is;

public class CustomMatcher {

    public interface MatcherRule<T extends View> {
        boolean matches(T view);
    }

    public static Matcher<View> withCustomMatch(final MatcherRule rule) {
        return new TypeSafeMatcher<View>() {
            final Matcher<Boolean> matcher = is(true);

            @Override
            protected boolean matchesSafely(View item) {
                Class expectedClass = getGenericClass(rule);
                Class actualClass = item.getClass();
                if(expectedClass.isAssignableFrom(actualClass)) {
                    return matcher.matches(rule.matches(item));
                } else {
                    String error = "\n\n" +
                            "You specified to use type [" + expectedClass + "] in CustomMatcher.\n" +
                            "But matcher target view class was [" +actualClass+"].\n"+
                            "This exception occurs like below case\n\n"+
                            "expect(R.id.textview).should(new CustomMatcher.MatcherRule<ImageView>() {\n"+
                            ".    @Override\n" +
                            ".    public boolean matches(ImageView view) {\n" +
                            ".        // ...\n" +
                            ".    }});"+
                            "\n";
                    throw new ClassCastException(error);
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Custom matching result ");
                matcher.describeTo(description);
            }
        };
    }

    private static Class getGenericClass(Object object) {
        Class<?> clazz = object.getClass();
        Type[] type = clazz.getGenericInterfaces();
        // ClassCastException is occur when no generic type is specified on MatcherRule like this
        // expect(id(android.R.id.message)).should(new CustomMatcher.MatcherRule() {
        //     @Override
        //     public boolean matches(View view) {
        //        return false;
        //    }});
        try {
            ParameterizedType pt = (ParameterizedType) type[0];
            Type[] actualTypeArguments = pt.getActualTypeArguments();
            return (Class<?>) actualTypeArguments[0];
        } catch (ClassCastException e) {
            return View.class;
        }
    }
}
