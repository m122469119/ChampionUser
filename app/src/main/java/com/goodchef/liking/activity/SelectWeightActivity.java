package com.goodchef.liking.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * 说明:选择体重
 * Author shaozucheng
 * Time:16/8/16 下午4:17
 */
public class SelectWeightActivity extends AppBarActivity {
    private LikingStateView mStateView;
    private TextView mUserNameTextView;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_weight);
        setTitle("选择体重");
        initView();
        initData();
    }

    private void initView() {
        mStateView = (LikingStateView)findViewById(R.id.select_weight_state_view);
        mUserNameTextView = (TextView) findViewById(R.id.user_name_text);
    }

    private void initData(){
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
}
