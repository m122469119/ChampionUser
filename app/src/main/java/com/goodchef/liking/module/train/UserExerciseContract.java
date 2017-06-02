package com.goodchef.liking.module.train;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.ShareResult;
import com.goodchef.liking.data.remote.retrofit.result.UserExerciseResult;
import com.goodchef.liking.data.remote.retrofit.result.data.ShareData;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.share.ShareModel;

/**
 * 说明:
 * Author: chenlei
 * Time: 下午3:53
 */

interface UserExerciseContract {
    interface View extends BaseStateView {
        void updateUserExerciseView(UserExerciseResult.ExerciseData exerciseData);
        void updateShareView(ShareData shareData);
    }

    class Presenter extends RxBasePresenter<View> {

        private UserExerciseModel mUserExerciseModel;
        private ShareModel mShareModel;

        public Presenter() {
            mUserExerciseModel = new UserExerciseModel();
            mShareModel = new ShareModel();
        }

        void getExerciseData() {
            mUserExerciseModel.getExerciseData()
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<UserExerciseResult>(mView){
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

                    }));
        }

        //我的运动数据分享
        public void getUserShareData() {
            mShareModel.getUserShare()
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<ShareResult>(mView) {

                        @Override
                        public void onNext(ShareResult value) {
                            if (value == null) return;
                            mView.updateShareView(value.getShareData());
                        }
                    }));
        }

    }

}
