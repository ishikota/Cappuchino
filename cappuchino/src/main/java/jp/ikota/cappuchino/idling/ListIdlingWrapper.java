package jp.ikota.cappuchino.idling;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.v7.widget.RecyclerView;

import jp.ikota.cappuchino.idling.customresource.ListCountIdlingResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


public class ListIdlingWrapper {
    private final RecyclerView mTargetView;

    public ListIdlingWrapper(RecyclerView recyclerView) {
        mTargetView = recyclerView;
    }

    public void waitUntilItemCountGraterThan(int count) {
        IdlingResource idlingResource = new ListCountIdlingResource(mTargetView, count+1);
        Espresso.registerIdlingResources(idlingResource);
        dummyAssertion();
        Espresso.unregisterIdlingResources(idlingResource);
    }

    public void waitFirstItemLoad() {
        waitUntilItemCountGraterThan(0);
    }

    private void dummyAssertion() {
        // Some view assertion is needed to work IdlingResource.
        // So we do meaningless( always true ) assertion in below line.
        onView(withId(android.R.id.content)).check(matches(withId(android.R.id.content)));
    }
}
