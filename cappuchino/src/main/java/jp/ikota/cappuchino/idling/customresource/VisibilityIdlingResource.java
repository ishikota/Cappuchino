package jp.ikota.cappuchino.idling.customresource;

import android.support.test.espresso.IdlingResource;
import android.view.View;

// Wait until passed view's visibility changes to specified one.
public class VisibilityIdlingResource implements IdlingResource {

    private ResourceCallback resourceCallback;
    private View view;
    private int expected;

    public VisibilityIdlingResource(View view, int visibility) {
        if(visibility!=View.VISIBLE && visibility!=View.INVISIBLE && visibility!=View.GONE) {
            throw new IllegalArgumentException("Passed visibility is invalid");
        }
        this.view = view;
        this.expected = visibility;
    }

    @Override
    public String getName() {
        return VisibilityIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = view.getVisibility() == expected;
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

}
