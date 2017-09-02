package com.goodchef.liking.module.smartspot;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;

import java.util.List;

interface SmartSpotDetailContract {
    interface View extends BaseStateView {
        void updateData(SmartspotDetailResult.DataBean data);
    }

    class Presenter extends RxBasePresenter<View> {

        private SmartspotModel mModel;

        public Presenter() {
            mModel = new SmartspotModel();
        }

        public void requestSmartDetail(String recordID) {
            mModel.getSmartspotHistory(recordID)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<SmartspotDetailResult>(mView) {
                        @Override
                        public void onNext(SmartspotDetailResult value) {
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
    }
}
