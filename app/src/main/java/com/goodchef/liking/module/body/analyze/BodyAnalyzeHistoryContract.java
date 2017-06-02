package com.goodchef.liking.module.body.analyze;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.BodyAnalyzeHistoryResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.body.BodyModel;

/**
 * Created on 2017/05/23
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

interface BodyAnalyzeHistoryContract {

    interface View extends BaseStateView {
        void updateBodyAnalyzeHistoryView(BodyAnalyzeHistoryResult.BodyHistory data);
    }

    class Presenter extends RxBasePresenter<View> {

        private BodyModel mBodyModel;
        public Presenter() {
            mBodyModel = new BodyModel();
        }

        void getBodyAnalyzeHistory(String column) {
            mBodyModel.getBodyHistoryList(column)
            .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<BodyAnalyzeHistoryResult>(mView) {

                @Override
                public void onNext(BodyAnalyzeHistoryResult value) {
                    if(value == null) return;
                    mView.updateBodyAnalyzeHistoryView(value.getData());
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
