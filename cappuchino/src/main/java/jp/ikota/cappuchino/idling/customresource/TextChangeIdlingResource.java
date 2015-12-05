package jp.ikota.cappuchino.idling.customresource;

import android.support.test.espresso.IdlingResource;
import android.widget.TextView;

// Wait until target text view's text has changed
public class TextChangeIdlingResource implements IdlingResource {

    private ResourceCallback resourceCallback;
    private TextView textView;
    private final String original;

    public TextChangeIdlingResource(TextView textView) {
        this.textView = textView;
        original = textView.getText().toString();
    }

    @Override
    public String getName() {
        return TextChangeIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = hasTextChanged(textView);
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    private boolean hasTextChanged(TextView view) {
        return !view.getText().toString().equals(original);
    }

}
