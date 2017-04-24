package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.common.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.UpDateUserInfoMessage;
import com.goodchef.liking.widgets.base.LikingStateView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:首次登陆填写姓名
 * Author shaozucheng
 * Time:16/8/15 上午10:17
 */
public class WriteNameActivity extends AppBarActivity {

    public static final String KEY_USER_NAME = "key_user_name";
    @BindView(R.id.write_name_state_view)
    LikingStateView mWriteNameStateView;
    @BindView(R.id.write_name_editText)
    EditText mWriteNameEditText;
    @BindView(R.id.write_name_next_btn)
    TextView mWriteNameNextBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_name);
        ButterKnife.bind(this);
        initView();
        showHomeUpIcon(R.drawable.app_bar_left_quit);
        setTitle(getString(R.string.activity_title_writename));
        showRightMenu(getString(R.string.skip), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
        setViewOnRetryRequestListener();
    }

    private void initView() {
        mWriteNameNextBtn = (TextView) findViewById(R.id.write_name_next_btn);

    }

    private void initData() {
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            mWriteNameStateView.setState(StateView.State.SUCCESS);
        } else {
            mWriteNameStateView.setState(StateView.State.FAILED);
        }
    }

    private void setViewOnRetryRequestListener() {
        mWriteNameStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                initData();
            }
        });
    }

    @OnClick({R.id.write_name_next_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_name_next_btn:
                doWriteName();
                break;
        }
    }


    /**
     * 处理用户先写用户名
     */
    private void doWriteName() {
        String nameStr = mWriteNameEditText.getText().toString().trim();
        if (StringUtils.isEmpty(nameStr)) {
            PopupUtils.showToast(getString(R.string.input_name));
            return;
        }
        if (nameStr.length() > 15) {
            PopupUtils.showToast(getString(R.string.name_limit));
            return;
        }

        Intent intent = new Intent(this, UserHeadImageActivity.class);
        intent.putExtra(KEY_USER_NAME, nameStr);
        startActivity(intent);
    }


    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(UpDateUserInfoMessage message) {
        this.finish();
    }
}
