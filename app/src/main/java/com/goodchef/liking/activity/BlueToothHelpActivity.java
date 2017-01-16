package com.goodchef.liking.activity;

import android.os.Bundle;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;

/**
 * 说明:蓝牙帮助界面
 * Author : shaozucheng
 * Time: 下午3:19
 * version 1.0.0
 */

public class BlueToothHelpActivity extends AppBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth_help);
        setTitle(getString(R.string.title_bing_bracelet));
    }
}
