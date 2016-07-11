package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.UserExerciseView;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/11 下午3:18
 */
public class UserExercisePresenter extends BasePresenter<UserExerciseView> {
    public UserExercisePresenter(Context context, UserExerciseView mainView) {
        super(context, mainView);
    }

    public void getExerciaseData() {
        LiKingApi.getUserExerciseData(Preference.getToken(), new RequestUiLoadingCallback<UserExerciseResult>(mContext, R.string.loading_data) {
            @Override
            public void onSuccess(UserExerciseResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateUserExerciseView(result.getExerciseData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
