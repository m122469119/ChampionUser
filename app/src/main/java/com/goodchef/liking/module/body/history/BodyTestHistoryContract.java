package com.goodchef.liking.module.body.history;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseStateView;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.http.result.BodyHistoryResult;
import com.goodchef.liking.module.body.BodyModel;
import com.goodchef.liking.data.remote.ApiException;
import com.goodchef.liking.data.remote.rxobserver.PagerLoadingObserver;

/**
 * Created on 2017/05/23
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public interface BodyTestHistoryContract {

    interface BodyHistoryView extends BaseStateView {
        void updateBodyHistoryView(BodyHistoryResult.BodyHistoryData data);
    }

    class BodyHistoryPresenter extends BasePresenter<BodyHistoryView> {

        private BodyModel mBodyModel;

        public BodyHistoryPresenter(Context context, BodyHistoryView mainView) {
            super(context, mainView);
            mBodyModel = new BodyModel();
        }

        public void getHistoryData(int page, BasePagerLoaderFragment fragment) {

            mBodyModel.getHistoryData(page)
            .subscribe(new PagerLoadingObserver<BodyHistoryResult>(mContext, mView) {
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
            });
        }
    }

}
