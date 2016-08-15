package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/15 上午11:16
 */
public class UserHeadImageActivity extends AppBarActivity implements View.OnClickListener {
    private LikingStateView mStateView;
    private TextView mUserNameTextView;
    private HImageView mHImageView;
    private TextView mNextBtn;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_head_image);
        initView();
        initData();
    }

    private void initView() {
        mStateView = (LikingStateView)findViewById(R.id.user_head_image_state_view);
        mUserNameTextView = (TextView) findViewById(R.id.user_name_text);
        mHImageView = (HImageView) findViewById(R.id.head_image);
        mNextBtn = (TextView) findViewById(R.id.head_image_next_btn);

        mNextBtn.setOnClickListener(this);
        mHImageView.setOnClickListener(this);
    }

    private void initData() {
        userName = getIntent().getStringExtra(WriteNameActivity.KEY_USER_NAME);
        mUserNameTextView.setText(userName);

        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            mStateView.setState(StateView.State.SUCCESS);
        } else {
            mStateView.setState(StateView.State.FAILED);
        }
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                initData();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mHImageView) {

        } else if (v == mNextBtn) {
            Intent intent = new Intent(this,SexActivity.class);
            startActivity(intent);
        }
    }
}
