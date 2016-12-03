package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.BodyAnalyzeChartFragment;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:05
 * version 1.0.0
 */

public class BodyAnalyzeChartActivity extends AppBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_chart);
        setTitle("身体成分分析");
        setAnalyzeFragment();
    }

    private void setAnalyzeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.analyze_FrameLayout, BodyAnalyzeChartFragment.newInstance());
        fragmentTransaction.commit();
    }
}
