package com.goodchef.liking.module.user;

import android.content.Context;

import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.BaseView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.module.data.remote.LikingApiService;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 17/3/13.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LoginContract {

    public static class LoginPresenter extends BasePresenter<LoginView> {
        public LoginPresenter(Context context, LoginView mainView) {
            super(context, mainView);
        }

        public void userLogin(String phone, String captcha) {
            LikingApiService.Creator.getInstance().userLogin(UrlList.sHostVersion, phone, captcha)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(userLoginResult -> {
                        if (LiKingVerifyUtils.isValid(mContext, userLoginResult)) {
                            mView.updateLoginView(userLoginResult.getUserLoginData());
                        } else {
                            PopupUtils.showToast(userLoginResult.getMessage());
                        }
                    }, throwable -> {
                        PopupUtils.showToast("userLogin failure!!!");
                    });
        }
    }

    public interface LoginView extends BaseView {
        void updateVerificationCodeView(VerificationCodeResult.VerificationCodeData verificationCodeData);

        void updateLoginView(UserLoginResult.UserLoginData userLoginData);

        void updateLoginOut();
    }
}
