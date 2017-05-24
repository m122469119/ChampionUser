package com.goodchef.liking.module.train;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.data.remote.ApiException;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:53
 */

public interface UserExerciseContract {
    interface UserExerciseView extends BaseStateView {
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
                    .subscribe(new LikingBaseObserver<UserExerciseResult>(mContext, mView){
                        @Override
                        public void onNext(UserExerciseResult result) {
                            if(result == null) return;
                            mView.updateUserExerciseView(result.getExerciseData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            mView.changeStateView(StateView.State.FAILED);
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            mView.changeStateView(StateView.State.FAILED);
                        }

                    });
        }
    }

}
