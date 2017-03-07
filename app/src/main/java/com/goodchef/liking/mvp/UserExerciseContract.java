package com.goodchef.liking.mvp;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BaseNetworkLoadView;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.BaseView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.model.UserExerciseModel;
import com.goodchef.liking.mvp.view.UserExerciseView;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:53
 */

public interface UserExerciseContract {
    interface UserExerciseView extends BaseNetworkLoadView {
        void updateUserExerciseView(UserExerciseResult.ExerciseData exerciseData);
        void showToast(String message);
    }

    class UserExercisePresenter extends BasePresenter<UserExerciseView> {

        private UserExerciseModel mUserExerciseModel;

        public UserExercisePresenter(Context context, UserExerciseView mainView) {
            super(context, mainView);
            mUserExerciseModel = new UserExerciseModel();
        }

        public void getExerciseData() {
            mUserExerciseModel.getExerciseData(new RequestCallback<UserExerciseResult>() {
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

}
