package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.codelibrary.utils.ValidateUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.base.web.HDefaultWebActivity;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;
import com.goodchef.liking.mvp.presenter.LoginPresenter;
import com.goodchef.liking.mvp.view.LoginView;
import com.goodchef.liking.storage.Preference;

import cn.jpush.android.api.JPushInterface;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/6 上午10:04
 */
public class LoginActivity extends AppBarActivity implements View.OnClickListener, LoginView {
    public static final String KEY_TITLE_SET_USER_INFO = "key_title_set_user_info";
    public static final String KEY_INTENT_TYPE = "key_intent_type";
    private EditText mLoginPhoneEditText;//输入手机号
    private EditText mCodeEditText;//输入验证码
    private TextView mSendCodeBtn;//获取验证码按钮
    private TextView mRegisterBtn;//注册协议
    private Button mLoginBtn;//登录按钮

    private String phoneStr;
    private MyCountdownTime mMyCountdownTime;//60s 倒计时类

    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.login_btn_text);
        initData();
    }

    private void initData() {
        initView();
        mMyCountdownTime = new MyCountdownTime(60000, 1000);
        mLoginPresenter = new LoginPresenter(this, this);
        mSendCodeBtn.setText("获取验证码");
        showHomeUpIcon(R.drawable.app_bar_left_quit);
        setViewOnClickListener();
    }

    private void initView() {
        mLoginPhoneEditText = (EditText) findViewById(R.id.et_login_phone);
        mCodeEditText = (EditText) findViewById(R.id.et_verification_code);
        mSendCodeBtn = (TextView) findViewById(R.id.send_verification_code_btn);
        mRegisterBtn = (TextView) findViewById(R.id.register_agree_on);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
    }

    private void setViewOnClickListener() {
        mSendCodeBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mSendCodeBtn) {
            getVerificationCode();
        } else if (v == mLoginBtn) {
            login();
        } else if (v == mRegisterBtn) {
            BaseConfigResult.BaseConfigData baseConfigData = Preference.getBaseConfig().getBaseConfigData();
            if (baseConfigData != null) {
                String agreeUrl = baseConfigData.getAgreeUrl();
                if (!StringUtils.isEmpty(agreeUrl)) {
                    HDefaultWebActivity.launch(this, agreeUrl, "用户协议");
                }
            }
        }
    }

    /**
     * 登录
     */
    private void login() {
        if (!checkPhone()) {
            return;
        }
        String code = mCodeEditText.getText().toString().trim();
        if (StringUtils.isEmpty(code)) {
            PopupUtils.showToast("验证码不能为空");
            return;
        }
        requestLogin(phoneStr, code);
    }


    private void requestLogin(String phoneStr, String code) {
        mLoginPresenter.userLogin(phoneStr, code);
    }


    /**
     * 获取验证码
     */
    private void getVerificationCode() {
        if (checkPhone()) {
            //发送请求
            mMyCountdownTime.start();
            mLoginPresenter.getVerificationCode(phoneStr);
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

    @Override
    public void updateVerificationCodeView(VerificationCodeResult.VerificationCodeData verificationCodeData) {
        mCodeEditText.setText("");
        PopupUtils.showToast("验证码已发送");
        if (verificationCodeData != null && !TextUtils.isEmpty(verificationCodeData.getCaptcha())) {
            mCodeEditText.setText(verificationCodeData.getCaptcha());
        }
    }

    @Override
    public void updateLoginView(UserLoginResult.UserLoginData userLoginData) {
        if (userLoginData != null) {
            Preference.setToken(userLoginData.getToken());
            Preference.setNickName(userLoginData.getName());
            Preference.setUserIconUrl(userLoginData.getAvatar());
            Preference.setUserPhone(userLoginData.getPhone());
            Preference.setIsNewUser(userLoginData.getNewUser());
            postEvent(new LoginFinishMessage());
            uploadDeviceInfo();
            int newUser = userLoginData.getNewUser();
            if (newUser == 1) {
                Intent intent = new Intent(this, MyInfoActivity.class);
                intent.putExtra(KEY_TITLE_SET_USER_INFO, "设置个人信息");
                intent.putExtra(KEY_INTENT_TYPE,1);
                startActivity(intent);
            }
            this.finish();

        }
    }

    /***
     * 上传设备信息
     */
    private void uploadDeviceInfo() {
        String jPushRegisterId = Preference.getJPushRegistrationId();
        if (StringUtils.isEmpty(jPushRegisterId)) {
            return;
        }
        LiKingApi.uploadUserDevice(Preference.getToken(), JPushInterface.getUdid(LoginActivity.this), "", jPushRegisterId, new RequestCallback<BaseResult>() {
            @Override
            public void onSuccess(BaseResult result) {
                LogUtils.i(TAG, "uploadDeviceInfo success!");
            }

            @Override
            public void onFailure(RequestError error) {
                LogUtils.i(TAG, "uploadDeviceInfo fail!");
            }
        });
    }


    @Override
    public void updateLoginOut() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMyCountdownTime != null) {
            mMyCountdownTime.cancel();
        }
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
            mSendCodeBtn.setTextColor(ResourceUtils.getColor(R.color.get_code_gray_dark));
        }

        @Override
        public void onFinish() {
            mSendCodeBtn.setText("重新发送");
            mSendCodeBtn.setClickable(true);
            mSendCodeBtn.setTextColor(ResourceUtils.getColor(R.color.bg_login_green_text));
        }
    }

}
