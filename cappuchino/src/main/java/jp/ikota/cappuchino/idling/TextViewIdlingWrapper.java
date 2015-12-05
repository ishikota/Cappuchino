package jp.ikota.cappuchino.idling;


import android.support.test.espresso.Espresso;
import android.widget.TextView;

import jp.ikota.cappuchino.idling.customresource.TextChangeIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class TextViewIdlingWrapper {
    private final TextView mTargetView;

    public TextViewIdlingWrapper(TextView view) {
        mTargetView = view;
    }

    public void waitUntilTextChanges() {
        TextChangeIdlingResource idlingResource = new TextChangeIdlingResource(mTargetView);
        Espresso.registerIdlingResources(idlingResource);
        dummyAssertion();
        Espresso.registerIdlingResources(idlingResource);
    }

    private void dummyAssertion() {
        // Some view assertion is needed to work IdlingResource.
        // So we do meaningless( always true ) assertion in below line.
        onView(withId(android.R.id.content)).check(matches(withId(android.R.id.content)));
    }
}
