package com.goodchef.liking.module.runpush;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.smartspot.SmartspotDetailResult;
import com.goodchef.liking.module.smartspot.SmartspotModel;

interface RunFinishContract {
    interface View extends BaseStateView {
        void updateData(RunFinishResult.DataBean data);
        void followUser(FollowUserResult.DataBean data);
    }

    class Presenter extends RxBasePresenter<View> {

        private RunFinishModel mModel;

        public Presenter() {
            mModel = new RunFinishModel();
        }

        public void requestData(String userId, String mMarahtonId) {
            mModel.getRunFinsh(userId, mMarahtonId)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<RunFinishResult>(mView) {
                        @Override
                        public void onNext(RunFinishResult value) {
                            if (null == value) {
                                mView.updateData(null);
                                return;
                            }
                            mView.updateData(value.getData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            super.apiError(apiException);

                            mView.changeStateView(StateView.State.FAILED);
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            super.networkError(throwable);

                            mView.changeStateView(StateView.State.FAILED);
                        }
                    }));
        }

        public void follow(String userId) {
            mModel.followUser(userId)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<FollowUserResult>(mView) {
                        @Override
                        public void onNext(FollowUserResult value) {
                            if (null == value) {
                                mView.updateData(null);
                                return;
                            }

                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            super.apiError(apiException);

                            mView.changeStateView(StateView.State.FAILED);
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            super.networkError(throwable);

                            mView.changeStateView(StateView.State.FAILED);
                        }
                    }));
        }
    }
}
