package jp.ikota.cappuchino.idling;


import android.support.test.espresso.Espresso;
import android.view.View;

import jp.ikota.cappuchino.idling.customresource.VisibilityIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class ViewIdlingWrapper {
    private final View mTargetView;

    public ViewIdlingWrapper(View view) {
        mTargetView = view;
    }

    public void waitUntilViewHasGone() {
        VisibilityIdlingResource idlingResource = new VisibilityIdlingResource(mTargetView, View.GONE);
        Espresso.registerIdlingResources(idlingResource);
        dummyAssertion();
        Espresso.unregisterIdlingResources(idlingResource);
    }

    private void dummyAssertion() {
        // Some view assertion is needed to work IdlingResource.
        // So we do meaningless( always true ) assertion in below line.
        onView(withId(android.R.id.content)).check(matches(withId(android.R.id.content)));
    }
}
