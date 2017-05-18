package com.goodchef.liking.module.login;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.common.utils.RegularUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;
import com.goodchef.liking.http.verify.LiKingRequestCode;
import com.goodchef.liking.module.data.remote.ApiException;
import com.goodchef.liking.module.data.remote.ResponseThrowable;
import com.goodchef.liking.module.data.remote.rxobserver.ProgressObserver;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

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
         *
         * @param phone 手机号码
         */
        void getVerificationCode(String phone) {
            Observable
                    .just(phone)
                    .filter(new Predicate<String>() {
                        @Override
                        public boolean test(String s) throws Exception {
                            return checkPhone(s);
                        }
                    })
                    .flatMap(new Function<String, ObservableSource<VerificationCodeResult>>() {
                        @Override
                        public ObservableSource<VerificationCodeResult> apply(String s) throws Exception {
                            return mLoginModel.getVerificationCode(s);
                        }
                    })
                    .subscribe(new ProgressObserver<VerificationCodeResult>(mContext, R.string.loading_data) {
                        @Override
                        public void onNext(VerificationCodeResult result) {
                            mView.updateVerificationCodeView(result.getVerificationCodeData());
                            mView.myCountdownTimeStart();
                        }

                        @Override
                        public void onError(ResponseThrowable e) {
                            super.onError(e);
                        }
                    });
        }


        /**
         * 校验手机号码
         */
        private boolean checkPhone(String phone) {
            if (StringUtils.isEmpty(phone)) {
                mView.showToast(mContext.getString(R.string.hint_login_phone));
                return false;
            }
            if (!RegularUtils.isMobileExact(phone)) {
                mView.showToast(mContext.getString(R.string.phone_format_error));
                return false;
            }
            return true;
        }

        /**
         * 检验验证码
         *
         * @param captcha
         * @return
         */
        private boolean checkCaptcha(String captcha) {
            if (StringUtils.isEmpty(captcha)) {
                mView.showToast(mContext.getString(R.string.version_code_not_blank));
                return false;
            }
            return true;
        }


        /**
         * 用户登陆
         *
         * @param phone   手机号码
         * @param captcha 验证码
         */
        void userLogin(final String phone, final String captcha) {
            Observable
                    .just(1)
                    .filter(new Predicate<Object>() {
                        @Override
                        public boolean test(Object o) throws Exception {
                            return checkPhone(phone);
                        }
                    })
                    .filter(new Predicate<Object>() {
                        @Override
                        public boolean test(Object o) throws Exception {
                            return checkCaptcha(captcha);
                        }
                    })
                    .flatMap(new Function<Object, ObservableSource<UserLoginResult>>() {
                        @Override
                        public ObservableSource<UserLoginResult> apply(Object o) throws Exception {
                            return mLoginModel.getLoginResult(phone, captcha);
                        }
                    })


                    .subscribe(new ProgressObserver<UserLoginResult>(mContext, R.string.loading_data) {
                        @Override
                        public void onNext(UserLoginResult value) {
                            UserLoginResult.UserLoginData userLoginData = value.getUserLoginData();
                            if (userLoginData != null) {
                                mView.updateLoginView(value.getUserLoginData());
                            }
                        }

                        @Override
                        public void onError(ResponseThrowable e) {
//                            super.onError();
                        }
                    });
        }
    }

    interface LoginView extends BaseView {
        void updateVerificationCodeView(VerificationCodeResult.VerificationCodeData verificationCodeData);

        void updateLoginView(UserLoginResult.UserLoginData userLoginData);

        void updateLoginOut();

        void myCountdownTimeStart();
    }
}
