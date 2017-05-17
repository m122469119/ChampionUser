package com.goodchef.liking.module.login;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;
import com.goodchef.liking.http.verify.LiKingRequestCode;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.data.remote.ApiException;
import com.goodchef.liking.module.data.remote.LikingNewApi;
import com.goodchef.liking.module.data.remote.ResponseThrowable;
import com.goodchef.liking.module.data.remote.rxobserver.ProgressObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 17/3/13.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

class LoginContract {

    static class LoginPresenter extends BasePresenter<LoginView> {
        private LoginModel mLoginModel;

        LoginPresenter(Context context, LoginView mainView) {
            super(context, mainView);
            mLoginModel = new LoginModel();
        }

        /**
         * 获取验证码
         * @param phone 手机号码
         */
        void getVerificationCode(String phone) {
            LikingNewApi.getInstance().getVerificationCode(phone)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ProgressObserver<VerificationCodeResult>(mContext, R.string.loading_data) {
                        @Override
                        public void onNext(VerificationCodeResult result) {
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateVerificationCodeView(result.getVerificationCodeData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(ResponseThrowable e) {
                            if (e.getTrowable() instanceof ApiException) {
                                ApiException apiException = (ApiException) e.getTrowable();
                                switch (apiException.getErrorCode()) {
                                    case LiKingRequestCode.GET_VERIFICATION_CODE_FAILURE:
                                    case LiKingRequestCode.VERIFICATION_INVALID:
                                    case LiKingRequestCode.LOGIN_FAILURE:
                                    case LiKingRequestCode.VERIFICATION_INCORRECT:
                                    case LiKingRequestCode.LOGOUT_FAILURE:
                                    case LiKingRequestCode.ILLEGAL_VERIFICATION_CODE:
                                        mView.showToast(apiException.getMessage());
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    });
        }

        /**
         * 用户登陆
         * @param phone 手机号码
         * @param captcha 验证码
         */
        void userLogin(String phone, String captcha) {
            mLoginModel.getLoginResult(phone, captcha)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new ProgressObserver<UserLoginResult>(mContext, R.string.loading_data) {
                        @Override
                        public void onNext(UserLoginResult value) {
                            if (LiKingVerifyUtils.isValid(mContext, value)) {
                                UserLoginResult.UserLoginData userLoginData = value.getUserLoginData();
                                if (userLoginData != null) {
                                    mLoginModel.saveLoginUserInfo(userLoginData);
                                    mView.updateLoginView(value.getUserLoginData());
                                }
                            } else {
                                mView.showToast(value.getMessage());
                            }
                        }

                        @Override
                        public void onError(ResponseThrowable e) {

                        }
                    });
        }
    }

    interface LoginView extends BaseView {
        void updateVerificationCodeView(VerificationCodeResult.VerificationCodeData verificationCodeData);

        void updateLoginView(UserLoginResult.UserLoginData userLoginData);

        void updateLoginOut();

    }
}
