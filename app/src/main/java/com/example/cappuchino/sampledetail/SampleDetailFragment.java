package com.example.cappuchino.sampledetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cappuchino.R;
import com.example.cappuchino.samplelist.SampleListFragment;

import static java.lang.String.format;


public class SampleDetailFragment extends Fragment {

    public static SampleDetailFragment newInstance(String title, int like) {
        Bundle args = new Bundle();
        args.putString(SampleListFragment.TITLE, title);
        args.putInt(SampleListFragment.LIKE, like);
        SampleDetailFragment fragment = new SampleDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean mIsLiked = false;
    private int mLikeNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sample_detail, container, false);

        TextView titleText = (TextView)root.findViewById(android.R.id.title);
        final TextView likeText = (TextView)root.findViewById(R.id.like_text);
        final Button likeButton = (Button)root.findViewById(R.id.like_button);

        Bundle args = getArguments();
        mLikeNum = args.getInt(SampleListFragment.LIKE);
        titleText.setText(args.getString(SampleListFragment.TITLE));
        setLikeText(likeText, mLikeNum);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLikeNum += mIsLiked ? -1 : +1;
                mIsLiked = !mIsLiked;
                setLikeText(likeText, mLikeNum);
                likeButton.setText(mIsLiked ? R.string.unlike : R.string.like);
            }
        });

        return root;
    }

    private void setLikeText(TextView likeText, int like_num) {
        String like_text = mLikeNum > 1 ? format("%d likes", mLikeNum)
                : mLikeNum == 1 ? "1 like" : getResources().getString(R.string.nolike);
        likeText.setText(like_text);
    }

}
