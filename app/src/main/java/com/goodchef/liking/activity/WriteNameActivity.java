package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.UpDateUserInfoMessage;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * 说明:首次登陆填写姓名
 * Author shaozucheng
 * Time:16/8/15 上午10:17
 */
public class WriteNameActivity extends AppBarActivity implements View.OnClickListener {

    public static final String KEY_USER_NAME = "key_user_name";
    private LikingStateView mStateView;
    private EditText mWriteNameEditText;
    private TextView mNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_name);
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
        mStateView = (LikingStateView) findViewById(R.id.write_name_state_view);
        mWriteNameEditText = (EditText) findViewById(R.id.write_name_editText);
        mNextBtn = (TextView) findViewById(R.id.write_name_next_btn);
        mNextBtn.setOnClickListener(this);
    }

    private void initData() {
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            mStateView.setState(StateView.State.SUCCESS);
        } else {
            mStateView.setState(StateView.State.FAILED);
        }
    }

    private void setViewOnRetryRequestListener(){
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
            String nameStr = mWriteNameEditText.getText().toString().trim();
            if (StringUtils.isEmpty(nameStr)) {
                PopupUtils.showToast("请输入名称");
                return;
            }
            if (nameStr.length() > 15) {
                PopupUtils.showToast("昵称不能大于15个字");
                return;
            }

            Intent intent = new Intent(this, UserHeadImageActivity.class);
            intent.putExtra(KEY_USER_NAME, nameStr);
            startActivity(intent);
        }
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(UpDateUserInfoMessage message) {
        this.finish();
    }
}
