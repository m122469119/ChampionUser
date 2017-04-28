package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.UserExerciseView;
import com.goodchef.liking.module.data.local.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/11 下午3:18
 */
public class UserExercisePresenter extends BasePresenter<UserExerciseView> {
    public UserExercisePresenter(Context context, UserExerciseView mainView) {
        super(context, mainView);
    }

    public void getExerciseData() {
        LiKingApi.getUserExerciseData(Preference.getToken(), new RequestCallback<UserExerciseResult>() {
            @Override
            public void onSuccess(UserExerciseResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateUserExerciseView(result.getExerciseData());
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
}
