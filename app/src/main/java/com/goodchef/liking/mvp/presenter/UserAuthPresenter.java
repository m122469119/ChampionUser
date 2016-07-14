package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.UserAuthCodeResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.UserAuthView;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/14 下午5:09
 */
public class UserAuthPresenter extends BasePresenter<UserAuthView> {
    public UserAuthPresenter(Context context, UserAuthView mainView) {
        super(context, mainView);
    }

    public void getUserAuthCode(int inout) {
        LiKingApi.getUserAuthCode(Preference.getToken(), inout, new RequestUiLoadingCallback<UserAuthCodeResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(UserAuthCodeResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateUserAuthView(result.getAuthCodeData());
                } else {
                    mView.updateFailCodeView(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
