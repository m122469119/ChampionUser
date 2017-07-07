package com.goodchef.liking.module.writeuserinfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.UpDateUserInfoMessage;
import com.goodchef.liking.utils.CheckUtils;
import com.goodchef.liking.widgets.base.LikingStateView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:首次登陆填写姓名
 * Author shaozucheng
 * Time:16/8/15 上午10:17
 */

public class WriteNameActivity extends BaseActivity {

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
        initData();
        setViewOnRetryRequestListener();
    }

    private void initView() {
        mWriteNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence ss, int start, int before, int count) {
                String editable = mWriteNameEditText.getText().toString();
                String str = CheckUtils.replaceSpecialCharacter(editable);
                if(!editable.equals(str)){
                    mWriteNameEditText.setText(str);
                    //设置新的光标所在位置
                    mWriteNameEditText.setSelection(str.length());
                    showToast("不可输入特殊字符");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

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
                String nameStr = mWriteNameEditText.getText().toString().trim();
                if (StringUtils.isEmpty(nameStr)) {
                    showToast(getString(R.string.input_name));
                    return;
                }
                if (nameStr.length() > 15) {
                    showToast(getString(R.string.name_limit));
                    return;
                }
                Intent intent = new Intent(this, UserHeadImageActivity.class);
                intent.putExtra(KEY_USER_NAME, nameStr);
                startActivity(intent);
                break;
        }
    }





    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(UpDateUserInfoMessage message) {
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showToast(getString(R.string.write_name_key_down_prompt));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
