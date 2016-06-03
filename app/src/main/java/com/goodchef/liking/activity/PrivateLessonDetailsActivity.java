package com.goodchef.liking.activity;

import android.os.Bundle;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/24 下午5:55
 */
public class PrivateLessonDetailsActivity extends AppBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_lesson_details);
        setTitle("Hraey教练");
        initData();
    }

    private void initData() {
        initView();
    }

    private void initView() {

    }
}
