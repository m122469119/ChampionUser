package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.http.result.LikingResult;
import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.UserInfoResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.UserInfoView;
import com.goodchef.liking.module.data.local.LikingPreference;

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
        LiKingApi.getUserInfo(LikingPreference.getToken(), new RequestCallback<UserInfoResult>() {
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
        LiKingApi.updateUserInfo(LikingPreference.getToken(), name, avatar, gender, birthday, weight, height, new RequestUiLoadingCallback<LikingResult>(mContext, R.string.loading) {
            @Override
            public void onSuccess(LikingResult likingResult) {
                super.onSuccess(likingResult);
                if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                    mView.updateUserInfo();
                } else {
                    mView.showToast(likingResult.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
