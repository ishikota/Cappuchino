package jp.ikota.cappuchino.idling.customresource;


import android.support.test.espresso.IdlingResource;
import android.view.View;

public class CustomIdlingResource<T extends View> implements IdlingResource {

    public interface IdlingRule<T> {
        boolean waitWhileTrue(T view);
    }

    private IdlingResource.ResourceCallback resourceCallback;
    private final T view;
    private final IdlingRule<T> idlingRule;

    public CustomIdlingResource(T targetView, IdlingRule<T> idlingRule) {
        this.view = targetView;
        this.idlingRule = idlingRule;
    }

    @Override
    public String getName() {
        return ListCountIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = !idlingRule.waitWhileTrue(view);
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(IdlingResource.ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

}
