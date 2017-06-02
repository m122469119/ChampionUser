package com.goodchef.liking.module.body.analyze;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.BodyModelNavigationResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.body.BodyModel;

/**
 * Created on 2017/05/23
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

interface BodyAnalyzeChartContract {

    interface View extends BaseStateView {
        void  updateBodyModelNavigationView(BodyModelNavigationResult.HistoryTitleData data);
    }

    class Presenter extends RxBasePresenter<View> {

        private BodyModel mBodyModel;
        Presenter() {
            mBodyModel = new BodyModel();
        }

        /**
         * 获取体测历史页面顶部导航
         *
         * @param modules  模块名称
         */
        public void getBodyModeNavigation(String modules) {
            mBodyModel.getBodyHistoryTitleList(modules)
            .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<BodyModelNavigationResult>(mView) {

                @Override
                public void onNext(BodyModelNavigationResult value) {
                    if(value == null) return;
                    mView.updateBodyModelNavigationView(value.getData());
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
