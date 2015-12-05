package com.example.cappuchino.samplelist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cappuchino.R;
import com.example.cappuchino.sampledetail.SampleDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SampleListFragment extends Fragment {

    public static final String TITLE = "TITLE";
    public static final String LIKE = "LIKE";

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private View mEmptyView;

    private List<Map<String, Object>> mData = new ArrayList<>();
    private boolean mBusy = false;  // if true, in the middle of item loading

    public static SampleListFragment newInstance(boolean empty_mode) {
        Bundle args = new Bundle();
        args.putBoolean(SampleListActivity.EMPTY_MODE_KEY, empty_mode);
        SampleListFragment fragment = new SampleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sample_list, container, false);

        mRecyclerView = (RecyclerView)root.findViewById(android.R.id.list);
        mProgressBar = (ProgressBar)root.findViewById(android.R.id.progress);
        mEmptyView = root.findViewById(android.R.id.empty);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setAdapter(new ListAdapter(new ListClickListener() {
            @Override
            public void onClick(Map<String, Object> item) {
                Intent intent = new Intent(getActivity(), SampleDetailActivity.class);
                intent.putExtra(TITLE, (String)item.get(TITLE));
                intent.putExtra(LIKE, (Integer)item.get(LIKE));
                startActivity(intent);
            }
        }));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int visibleItemCount = layoutManager.getChildCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (totalItemCount - firstVisibleItem < 30) {
                    loadItems();
                }

                if (mBusy && firstVisibleItem + visibleItemCount == totalItemCount) {
                    showProgress(true);
                }
            }
        });

        showProgress(true);
        loadItems();

        return root;
    }

    private void showProgress(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void loadItems() {
        mBusy = true;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                populateData();
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mBusy = false;
                showProgress(false);
            }
        }, 3000);
    }

    private void populateData() {
        if(isEmptyMode()) {
           mEmptyView.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < 30; i++) {
                mData.add(makeItem(i));
            }
        }
    }

    private boolean isEmptyMode() {
        return getArguments().getBoolean(SampleListActivity.EMPTY_MODE_KEY, false);
    }

    private Map<String, Object> makeItem(int forRow) {
        Map<String, Object> dataRow = new HashMap<>();
        dataRow.put(TITLE, "item: " + forRow);
        dataRow.put(LIKE, forRow);
        return dataRow;
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        private final ListClickListener mClickListener;

        public ListAdapter(ListClickListener listener) {
            mClickListener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.row_sample_list, parent, false);
            return new ViewHolder(noteView, mClickListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Map<String, Object> item = mData.get(position);
            holder.textView.setText((String) item.get(TITLE));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView textView;
            public ListClickListener clickListener;

            public ViewHolder(View itemView, ListClickListener listener) {
                super(itemView);
                clickListener = listener;
                textView = (TextView) itemView.findViewById(android.R.id.text1);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                Map<String, Object> item = mData.get(position);
                clickListener.onClick(item);
            }
        }
    }

    public interface ListClickListener {
        void onClick(Map<String, Object> item);
    }
}
