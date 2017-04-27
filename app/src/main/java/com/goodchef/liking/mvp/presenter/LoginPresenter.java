package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.LoginView;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/7 上午10:10
 */
public class LoginPresenter extends BasePresenter<LoginView> {
    public LoginPresenter(Context context, LoginView mainView) {
        super(context, mainView);
    }

    public void getVerificationCode(String phone) {
        LiKingApi.getVerificationCode(phone, new RequestUiLoadingCallback<VerificationCodeResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(VerificationCodeResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateVerificationCodeView(result.getVerificationCodeData());
                }else {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });

    }

    public void userLogin(String phone,String captcha){
        LiKingApi.userLogin(phone, captcha, new RequestUiLoadingCallback<UserLoginResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(UserLoginResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateLoginView(result.getUserLoginData());
                    Preference.clearAnnouncementId();
                    Preference.clearHomeAnnouncement();
                }else {
                    mView.showToast(result.getMessage());
                }
            }
            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }


    public  void userLoginOut(){
        //极光推送的id,有就传，没有可以不传或者传0
        String jPushRegisterId = Preference.getJPushRegistrationId();
        LiKingApi.userLoginOut(Preference.getToken(), jPushRegisterId, new RequestUiLoadingCallback<BaseResult>(mContext,R.string.loading_data) {
            @Override
            public void onSuccess(BaseResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext,result)){
                    mView.updateLoginOut();
                }else {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }


}
