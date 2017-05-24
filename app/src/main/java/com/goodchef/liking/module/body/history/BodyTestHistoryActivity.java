package com.goodchef.liking.module.body.history;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:09
 * version 1.0.0
 */

public class BodyTestHistoryActivity extends AppBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodytest_history);
        setTitle(getString(R.string.body_test_history_title));
        setFragment();
    }

    private void setFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.body_test_history_FrameLayout, BodyTestHistoryFragment.newInstance());
        fragmentTransaction.commit();
    }
}
