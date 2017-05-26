package com.goodchef.liking.module.body.analyze;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.data.remote.retrofit.result.BodyAnalyzeHistoryResult;
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

public interface BodyAnalyzeHistoryContract {

    interface BodyAnalyzeHistoryView extends BaseStateView {
        void updateBodyAnalyzeHistoryView(BodyAnalyzeHistoryResult.BodyHistory data);
    }

    class BodyAnalyzeHistoryPresenter extends BasePresenter<BodyAnalyzeHistoryView> {

        private BodyModel mBodyModel;
        public BodyAnalyzeHistoryPresenter(Context context, BodyAnalyzeHistoryView mainView) {
            super(context, mainView);
            mBodyModel = new BodyModel();
        }

        public void getBodyAnalyzeHistory(String column) {
            mBodyModel.getBodyHistoryList(column)
            .subscribe(new LikingBaseObserver<BodyAnalyzeHistoryResult>(mContext, mView) {
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
            });
        }
    }

}
