package com.goodchef.liking.module.login;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.RegularUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.retrofit.result.UserLoginResult;
import com.goodchef.liking.data.remote.retrofit.result.VerificationCodeResult;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import com.goodchef.liking.data.remote.LiKingRequestCode;

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
                    .subscribe(new ProgressObserver<VerificationCodeResult>(mContext, R.string.loading_data, mView) {
                        @Override
                        public void onNext(VerificationCodeResult result) {
                            mView.updateVerificationCodeView(result.getVerificationCodeData());
                            mView.myCountdownTimeStart();
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            super.networkError(throwable);
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            switch (apiException.getErrorCode()) {
                                case LiKingRequestCode.GET_VERIFICATION_CODE_FAILURE:/**获取验证码失败*/
                                    break;
                                default:
                            }
                            super.apiError(apiException);
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
                    .subscribe(new ProgressObserver<UserLoginResult>(mContext, R.string.loading_data, mView) {
                        @Override
                        public void onNext(UserLoginResult value) {
                            if(value == null) return;
                            UserLoginResult.UserLoginData userLoginData = value.getUserLoginData();
                            if (userLoginData != null) {
                                mLoginModel.saveLoginUserInfo(userLoginData);
                                mView.updateLoginView(value.getUserLoginData());
                            }
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            switch (apiException.getErrorCode()) {
                                case LiKingRequestCode.INVALID_MOBOLE_NUMBER:/**无效手机号*/
                                    break;
                                case LiKingRequestCode.ILLEGAL_VERIFICATION_CODE:/**非法验证码*/
                                    break;
                                case LiKingRequestCode.VERIFICATION_INVALID:/**验证码已过期*/
                                    break;
                                case LiKingRequestCode.VERIFICATION_INCORRECT:/**验证码错误，请重试*/
                                    break;
                                case LiKingRequestCode.LOGIN_FAILURE:/**登录失败*/
                                    break;
                                default:
                            }
                            super.apiError(apiException);
                        }
                    });
        }

        /***
         * 上传设备信息
         *
         * @param device_id       设备id
         * @param device_token    设备token
         * @param registration_id 极光推送id
         */
        public void uploadUserDevice(String device_id, String device_token, String registration_id) {
            mLoginModel.uploadUserDevice(device_id, device_token, registration_id)
            .subscribe(new LikingBaseObserver<LikingResult>(mContext, mView) {
                @Override
                public void onNext(LikingResult value) {
                    LogUtils.i(TAG, "uploadDeviceInfo success!");
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    LogUtils.i(TAG, "uploadDeviceInfo fail!");
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
