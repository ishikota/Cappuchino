package jp.ikota.cappuchino.idling.customresource;

import android.support.test.espresso.IdlingResource;
import android.support.v7.widget.RecyclerView;


public class ListCountIdlingResource implements IdlingResource {

    private ResourceCallback resourceCallback;
    private RecyclerView recyclerView;
    private final int target;

    public ListCountIdlingResource(RecyclerView recyclerView, int target) {
        this.recyclerView = recyclerView;
        this.target = target;
    }

    @Override
    public String getName() {
        return ListCountIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = isItemLoaded(recyclerView);
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    private boolean isItemLoaded(RecyclerView list) {
        return list.getAdapter()!=null && list.getAdapter().getItemCount() >= target;
    }

}
