package com.chushi007.android.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.chushi007.android.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/27 下午3:11
 */
public class MyInfoActivity extends AppBarActivity implements View.OnClickListener {
    private RelativeLayout mHeadImageLayout;
    private HImageView mHeadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        setTitle("XXX");
        initData();
    }

    private void initData() {
        initView();
        setViewOnClickListener();
    }

    private void initView() {
        mHeadImageLayout = (RelativeLayout) findViewById(R.id.layout_head_image);
        mHeadImage = (HImageView) findViewById(R.id.head_image);
    }

    private void setViewOnClickListener() {
        mHeadImageLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadImageLayout) {

        }
    }
}
