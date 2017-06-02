package com.goodchef.liking.module.bodytest;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.BodyTestResult;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;

/**
 * Created on 2017/5/23
 * Created by sanfen
 *
 * @version 1.0.0
 */

interface BodyTestDataContract {
    interface View extends BaseNetworkLoadView {
        void updateBodyTestView(BodyTestResult.BodyTestData bodyTestData);
    }

    class Presenter extends RxBasePresenter<View> {
        BodyTestModel mBodyTestModel;


        Presenter() {
            mBodyTestModel = new BodyTestModel();
        }

        void getBodyData(Context context, String bodyId) {
            mBodyTestModel.getBodyData(bodyId)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<BodyTestResult>(context, R.string.loading_data, mView) {

                        @Override
                        public void onNext(BodyTestResult value) {
                            mView.updateBodyTestView(value.getData());
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            mView.handleNetworkFailure();
                        }

                    }));
        }

    }
}
