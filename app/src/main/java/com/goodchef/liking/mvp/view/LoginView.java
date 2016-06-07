package com.goodchef.liking.mvp.view;

import com.aaron.android.framework.base.mvp.BaseView;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/7 上午10:03
 */
public interface LoginView extends BaseView{
    void updateVerificationCodeView(VerificationCodeResult.VerificationCodeData verificationCodeData);
    void updateLoginView(UserLoginResult.UserLoginData userLoginData);
    void updateLoginOut();

}
