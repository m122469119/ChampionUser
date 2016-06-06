package com.goodchef.liking.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.codelibrary.utils.ValidateUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/6 上午10:04
 */
public class LoginActivity extends AppBarActivity implements View.OnClickListener {
    private EditText mLoginPhoneEditText;//输入手机号
    private EditText mCodeEditText;//输入验证码
    private TextView mSendCodeBtn;//获取验证码按钮
    private Button mLoginBtn;//登录按钮

    private String phoneStr;
    private MyCountdownTime mMyCountdownTime;//60s 倒计时类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.login_btn_text);
        initData();
    }

    private void initData() {
        initView();
        mSendCodeBtn.setText("发送");
        showHomeUpIcon(R.drawable.icon_screen_cancel);
        setViewOnClickListener();
    }

    private void initView() {
        mLoginPhoneEditText = (EditText) findViewById(R.id.et_login_phone);
        mCodeEditText = (EditText) findViewById(R.id.et_verification_code);
        mSendCodeBtn = (TextView) findViewById(R.id.send_verification_code_btn);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
    }

    private void setViewOnClickListener() {
        mSendCodeBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSendCodeBtn) {
            getVerificationCode();
        } else if (v == mLoginBtn) {

        }
    }


    /**
     * 获取验证码
     */
    private void getVerificationCode() {
        if (checkPhone()) {
            //发送请求
            mMyCountdownTime.start();
           // requestVerificationCode(phoneStr);
        }
    }


    /**
     * 校验手机号码
     */
    private boolean checkPhone() {
        phoneStr = mLoginPhoneEditText.getText().toString().trim();
        if (StringUtils.isEmpty(phoneStr)) {
            PopupUtils.showToast("请输入手机号码");
            return false;
        }
        if (!ValidateUtils.isMobile(phoneStr)) {
            PopupUtils.showToast("您手机号码格式不正确");
            return false;
        }
        return true;
    }


    /**
     * 发送验证码按钮 60s倒计时
     */
    class MyCountdownTime extends CountDownTimer {

        public MyCountdownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mSendCodeBtn.setClickable(false);
            mSendCodeBtn.setText("重新发送(" + millisUntilFinished / 1000 + "s" + ")");
            mSendCodeBtn.setTextColor(LoginActivity.this.getResources().getColor(R.color.get_code_gray_dark));
            mSendCodeBtn.setBackgroundResource(R.drawable.shape_gray_drak_radius_background);
        }

        @Override
        public void onFinish() {
            mSendCodeBtn.setText("重新发送");
            mSendCodeBtn.setClickable(true);
            mSendCodeBtn.setTextColor(LoginActivity.this.getResources().getColor(R.color.liking_lesson_group_text));
            mSendCodeBtn.setBackgroundResource(R.drawable.shape_radius_green_btn_background);
        }
    }

}
