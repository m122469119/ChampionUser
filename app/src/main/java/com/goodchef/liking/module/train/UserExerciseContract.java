package com.goodchef.liking.module.train;

import android.content.Context;
import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.base.rxobserver.LikingBaseObserver;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:53
 */

public interface UserExerciseContract {
    interface UserExerciseView extends BaseNetworkLoadView {
        void updateUserExerciseView(UserExerciseResult.ExerciseData exerciseData);
    }

    class UserExercisePresenter extends BasePresenter<UserExerciseView> {

        private UserExerciseModel mUserExerciseModel;

        public UserExercisePresenter(Context context, UserExerciseView mainView) {
            super(context, mainView);
            mUserExerciseModel = new UserExerciseModel();
        }

        public void getExerciseData() {
            mUserExerciseModel.getExerciseData()
                    .subscribe(new LikingBaseObserver<UserExerciseResult>(){
                        @Override
                        public void onNext(UserExerciseResult result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateUserExerciseView(result.getExerciseData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mView.handleNetworkFailure();
                        }
                    });
        }
    }

}
