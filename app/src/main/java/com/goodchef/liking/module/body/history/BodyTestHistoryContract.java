package com.goodchef.liking.module.body.history;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.BodyHistoryResult;
import com.goodchef.liking.data.remote.rxobserver.PagerLoadingObserver;
import com.goodchef.liking.module.body.BodyModel;

/**
 * Created on 2017/05/23
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public interface BodyTestHistoryContract {

    interface View extends BaseStateView {
        void updateBodyHistoryView(BodyHistoryResult.BodyHistoryData data);
    }

    class Presenter extends RxBasePresenter<View> {

        private BodyModel mBodyModel;

        public Presenter() {
            mBodyModel = new BodyModel();
        }

        public void getHistoryData(int page) {

            mBodyModel.getHistoryData(page)
            .subscribe(addObserverToCompositeDisposable(new PagerLoadingObserver<BodyHistoryResult>(mView) {

                @Override
                public void onNext(BodyHistoryResult result) {
                    super.onNext(result);
                    if(result == null) return;
                    mView.updateBodyHistoryView(result.getData());
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
