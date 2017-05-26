package com.goodchef.liking.module.body.analyze;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.result.BodyModelNavigationResult;
import com.goodchef.liking.module.body.BodyModel;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;

/**
 * Created on 2017/05/23
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public interface BodyAnalyzeChartContract {

    interface BodyModelNavigationView extends BaseStateView {
        void  updateBodyModelNavigationView(BodyModelNavigationResult.HistoryTitleData data);
    }

    class BodyModeNavigationPresenter extends BasePresenter<BodyModelNavigationView> {

        private BodyModel mBodyModel;
        public BodyModeNavigationPresenter(Context context, BodyModelNavigationView mainView) {
            super(context, mainView);
            mBodyModel = new BodyModel();
        }

        /**
         * 获取体测历史页面顶部导航
         *
         * @param modules  模块名称
         */
        public void getBodyModeNavigation(String modules) {
            mBodyModel.getBodyHistoryTitleList(modules)
            .subscribe(new LikingBaseObserver<BodyModelNavigationResult>(mContext, mView) {
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
            });
        }
    }

}
