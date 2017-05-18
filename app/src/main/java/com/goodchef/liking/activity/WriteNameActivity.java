package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.UpDateUserInfoMessage;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * 说明:首次登陆填写姓名
 * Author shaozucheng
 * Time:16/8/15 上午10:17
 */
public class WriteNameActivity extends BaseActivity implements View.OnClickListener {

    public static final String KEY_USER_NAME = "key_user_name";
    private LikingStateView mStateView;
    private EditText mWriteNameEditText;
    private TextView mNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_name);
        initView();
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

    private void setViewOnRetryRequestListener() {
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
                PopupUtils.showToast(getString(R.string.input_name));
                return;
            }
            if (nameStr.length() > 15) {
                PopupUtils.showToast(getString(R.string.name_limit));
                return;
            }
            String str = stringFilter(nameStr);
            if (!nameStr.equals(str)) {
                PopupUtils.showToast(getString(R.string.not_input_filter_code));
                return;
            }
            Intent intent = new Intent(this, UserHeadImageActivity.class);
            intent.putExtra(KEY_USER_NAME, nameStr);
            startActivity(intent);
        }
    }

    /**
     * 过滤字符，只能输入中文英文或者数字
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[^a-zA-Z0-9\\u4E00-\\u9FA5]"; //要过滤掉的字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
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
            PopupUtils.showToast(getString(R.string.write_name_key_down_prompt));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
