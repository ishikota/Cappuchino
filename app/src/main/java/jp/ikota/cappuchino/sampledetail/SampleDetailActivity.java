package jp.ikota.cappuchino.sampledetail;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import jp.ikota.cappuchino.samplelist.SampleListFragment;

public class SampleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == savedInstanceState) {
            String title = getIntent().getStringExtra(SampleListFragment.TITLE);
            int like = getIntent().getIntExtra(SampleListFragment.LIKE, -1);
            initFragment(SampleDetailFragment.newInstance(title, like));
        }
    }

    private void initFragment(Fragment sampleFragment) {
        String tag = SampleDetailFragment.class.getSimpleName();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(android.R.id.content, sampleFragment, tag);
        transaction.commit();
    }

}
