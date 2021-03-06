package jp.ikota.cappuchino.idling;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.view.View;

import jp.ikota.cappuchino.idling.customresource.CustomIdlingResource;
import jp.ikota.cappuchino.idling.customresource.VisibilityIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class ViewIdlingWrapper {
    private final View mTargetView;

    public ViewIdlingWrapper(View view) {
        mTargetView = view;
    }

    private void waitUntilViewIs(int visibility) {
        VisibilityIdlingResource idlingResource = new VisibilityIdlingResource(mTargetView, visibility);
        Espresso.registerIdlingResources(idlingResource);
        dummyAssertion();
        Espresso.unregisterIdlingResources(idlingResource);
    }

    public void waitUntilViewIsVisible() {
        waitUntilViewIs(View.VISIBLE);
    }

    public void waitUntilViewIsInvisible() {
        waitUntilViewIs(View.INVISIBLE);
    }

    public void waitUntilViewIsGone() {
        waitUntilViewIs(View.GONE);
    }

    public void waitFor(CustomIdlingResource.IdlingRule<View> rule) {
        IdlingResource idlingResource = new CustomIdlingResource<>(mTargetView, rule);
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
