package com.goodchef.liking.module.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.web.HDefaultWebActivity;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.RegularUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.writeuserinfo.WriteNameActivity;
import com.goodchef.liking.utils.NumberConstantUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/6 上午10:04
 */
public class LoginActivity extends AppBarActivity implements LoginContract.LoginView {
    public static final String KEY_TITLE_SET_USER_INFO = "key_title_set_user_info";
    public static final String KEY_INTENT_TYPE = "key_intent_type";
    @BindView(R.id.et_login_phone)
    EditText mLoginPhoneEditText;//输入手机号
    @BindView(R.id.send_verification_code_btn)
    TextView mSendCodeBtn;//获取验证码按钮
    @BindView(R.id.et_verification_code)
    EditText mCodeEditText;//输入验证码
    @BindView(R.id.register_agree_on)
    TextView mRegisterBtn;//注册协议
    @BindView(R.id.login_btn)
    Button mLoginBtn;//登录按钮

    private String phoneStr;
    private MyCountdownTime mMyCountdownTime;//60s 倒计时类
    private LoginContract.LoginPresenter mLoginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setTitle(R.string.login_btn_text);
        initData();
    }

    private void initData() {
        mMyCountdownTime = new MyCountdownTime(60000, 1000);
        mLoginPresenter = new LoginContract.LoginPresenter(this, this);
        mSendCodeBtn.setText(getString(R.string.get_version_code));
        showHomeUpIcon(R.drawable.app_bar_left_quit);
    }

    @OnClick({R.id.send_verification_code_btn, R.id.register_agree_on, R.id.login_btn})
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                login();
                break;
            case R.id.register_agree_on:
                BaseConfigResult baseConfigResult = LikingPreference.getBaseConfig();
                if (baseConfigResult != null) {
                    BaseConfigResult.ConfigData baseConfigData = baseConfigResult.getBaseConfigData();
                    if (baseConfigData != null) {
                        String agreeUrl = baseConfigData.getAgreeUrl();
                        if (!StringUtils.isEmpty(agreeUrl)) {
                            HDefaultWebActivity.launch(this, agreeUrl, getString(R.string.user_agreement));
                        }
                    }
                }
                break;
            case R.id.send_verification_code_btn:
                getVerificationCode();
                break;
            default:
                break;
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
            showToast(getString(R.string.version_code_not_blank));
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
            showToast(getString(R.string.hint_login_phone));
            return false;
        }
        if (!RegularUtils.isMobileExact(phoneStr)) {
            showToast(getString(R.string.phone_format_error));
            return false;
        }
        return true;
    }

    @Override
    public void updateVerificationCodeView(VerificationCodeResult.VerificationCodeData verificationCodeData) {
        mCodeEditText.setText("");
        showToast(getString(R.string.version_code_sended));
        if (!EnvironmentUtils.Config.isTestMode()) {
            return;
        }
        if (verificationCodeData != null && !TextUtils.isEmpty(verificationCodeData.getCaptcha())) {
            mCodeEditText.setText(verificationCodeData.getCaptcha());
        }
    }

    @Override
    public void updateLoginView(UserLoginResult.UserLoginData userLoginData) {
        if (userLoginData != null) {
            postEvent(new LoginFinishMessage());
            uploadDeviceInfo();
            int newUser = userLoginData.getNewUser();
            LogUtils.i("newUser", newUser + "");
            if (newUser == NumberConstantUtil.ONE) {
                startActivity(WriteNameActivity.class);
            }
            finish();
        }
    }

    /***
     * 上传设备信息
     */
    private void uploadDeviceInfo() {
        String jPushRegisterId = LikingPreference.getJPushRegistrationId();
        if (StringUtils.isEmpty(jPushRegisterId)) {
            return;
        }
        LiKingApi.uploadUserDevice(LikingPreference.getToken(), JPushInterface.getUdid(LoginActivity.this), "", jPushRegisterId, new RequestCallback<LikingResult>() {
            @Override
            public void onSuccess(LikingResult likingResult) {
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
    final class MyCountdownTime extends CountDownTimer {

        MyCountdownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mSendCodeBtn.setClickable(false);
            mSendCodeBtn.setText(getString(R.string.repeate_send_kuohao) + millisUntilFinished / 1000 + "s" + ")");
            mSendCodeBtn.setTextColor(ResourceUtils.getColor(R.color.get_code_gray_dark));
        }

        @Override
        public void onFinish() {
            mSendCodeBtn.setText(getString(R.string.repeate_send));
            mSendCodeBtn.setClickable(true);
            mSendCodeBtn.setTextColor(ResourceUtils.getColor(R.color.bg_login_green_text));
        }
    }

}
