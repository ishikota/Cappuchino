package jp.ikota.cappuchino;


import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Rule;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import jp.ikota.cappuchino.action.ViewActionInteraction;
import jp.ikota.cappuchino.idling.ListIdlingWrapper;
import jp.ikota.cappuchino.idling.TextViewIdlingWrapper;
import jp.ikota.cappuchino.idling.ViewIdlingWrapper;
import jp.ikota.cappuchino.matcher.ViewMatcherInteraction;

import static android.support.test.espresso.Espresso.onView;
import static junit.framework.Assert.fail;


public class Cappuchino<T extends Activity> {

    @SuppressWarnings("unchecked")
    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(
            getGenericClass(),
            true,     // initialTouchMode
            false);   // launchActivity. False so we can customize the intent per test method

    private Class getGenericClass() {
        Class<?> clazz = this.getClass();
        Type type = clazz.getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType)type;
        Type[] actualTypeArguments = pt.getActualTypeArguments();
        return (Class<?>)actualTypeArguments[0];
    }

    public Context getTargetContext() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        return instrumentation.getTargetContext();
    }

    public Application getTargetApplication() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        return (Application)instrumentation.getTargetContext().getApplicationContext();
    }

    public T getTargetActivity() {
        return (T)activityRule.getActivity();
    }

    public T launchActivity() {
        return (T)activityRule.launchActivity(null);
    }

    public T launchActivity(Intent startIntent) {
        return (T)activityRule.launchActivity(startIntent);
    }

    public ViewMatcherInteraction expect(final Matcher<View> viewMatcher) {
        return new ViewMatcherInteraction(onView(viewMatcher));
    }

    public ViewActionInteraction perform(final Matcher<View> viewMatcher) {
        return new ViewActionInteraction(onView(viewMatcher));
    }

    public ViewIdlingWrapper viewIdlingTarget(int target_view_id) {
        View view = findViewFromActivity(target_view_id);
        return new ViewIdlingWrapper(view);
    }

    public TextViewIdlingWrapper textViewIdlingTarget(int target_view_id) {
        View view = findViewFromActivity(target_view_id);
        if(!(view instanceof TextView)) {
            fail("Target view should be TextView");
        }
        return new TextViewIdlingWrapper((TextView)view);
    }

    public ListIdlingWrapper listIdlingTarget(int target_view_id) {
        View view = findViewFromActivity(target_view_id);
        if(!(view instanceof RecyclerView)) {
            fail("Target view should be RecyclerView");
        }
        return new ListIdlingWrapper((RecyclerView)view);
    }

    private View findViewFromActivity(int target_view_id) {
        Activity activity = activityRule.getActivity();
        if(null == activity) {
            fail("Failed to get Activity. You need to launch some activity before calling this method.");
        }
        View view = activity.findViewById(target_view_id);

        // Target view is not found so fail this test
        if(null == view) {
            fail("Target view is not found from your Activity");
        }

        return view;
    }

}
