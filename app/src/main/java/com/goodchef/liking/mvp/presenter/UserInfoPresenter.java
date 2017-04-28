package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.UserInfoResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.UserInfoView;
import com.goodchef.liking.module.data.local.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/4 下午11:04
 */
public class UserInfoPresenter extends BasePresenter<UserInfoView> {
    public UserInfoPresenter(Context context, UserInfoView mainView) {
        super(context, mainView);
    }

    public void getUserInfo() {
        LiKingApi.getUserInfo(Preference.getToken(), new RequestCallback<UserInfoResult>() {
            @Override
            public void onSuccess(UserInfoResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateGetUserInfoView(result.getData());
                } else {
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                mView.handleNetworkFailure();
            }
        });
    }

    public void updateUserInfo(String name, String avatar, Integer gender, String birthday, String weight, String height) {
        LiKingApi.updateUserInfo(Preference.getToken(), name, avatar, gender, birthday, weight, height, new RequestUiLoadingCallback<BaseResult>(mContext, R.string.loading) {
            @Override
            public void onSuccess(BaseResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateUserInfo();
                } else {
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
