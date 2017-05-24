package com.goodchef.liking.module.bodytest;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.BodyTestResult;
import com.goodchef.liking.module.data.remote.rxobserver.ProgressObserver;

/**
 * Created on 2017/5/23
 * Created by sanfen
 *
 * @version 1.0.0
 */

public interface BodyTestDataContract {
    interface BodyTestView extends BaseNetworkLoadView {
        void updateBodyTestView(BodyTestResult.BodyTestData bodyTestData);
    }

    class BodyTestPresenter extends BasePresenter<BodyTestView> {
        public BodyTestModel mBodyTestModel;


        public BodyTestPresenter(Context context, BodyTestView mainView) {
            super(context, mainView);
            mBodyTestModel = new BodyTestModel();
        }

        public void getBodyData(String bodyId) {
            mBodyTestModel.getBodyData(bodyId)
                    .subscribe(new ProgressObserver<BodyTestResult>(mContext, R.string.loading_data, mView) {

                        @Override
                        public void onNext(BodyTestResult value) {
                            mView.updateBodyTestView(value.getData());
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            mView.handleNetworkFailure();
                        }

                    });
        }

    }
}
