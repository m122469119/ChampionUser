package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/16 下午3:25
 */
public class SelectBirthdayActivity extends AppBarActivity implements View.OnClickListener {
    private LikingStateView mStateView;
    private TextView mUserNameTextView;
    private TextView mNextBtn;

    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_birthday);
        setTitle("选择出生日期");
        iniView();
        initData();
    }

    private void iniView() {
        mStateView = (LikingStateView) findViewById(R.id.select_birthady_state_view);
        mUserNameTextView = (TextView) findViewById(R.id.user_name_text);
        mNextBtn = (TextView) findViewById(R.id.select_birthday_next_btn);

        mNextBtn.setOnClickListener(this);
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
        if (v == mNextBtn) {
            Intent intent = new Intent(this, SelectWeightActivity.class);
            intent.putExtra(WriteNameActivity.KEY_USER_NAME,userName);
            startActivity(intent);
        }
    }
}
