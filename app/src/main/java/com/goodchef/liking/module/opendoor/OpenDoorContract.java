package com.goodchef.liking.module.opendoor;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.UserAuthCodeResult;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:26
 * version 1.0.0
 */

interface OpenDoorContract {
    interface View extends BaseView {
        void updateUserAuthView(UserAuthCodeResult.UserAuthCodeData userAuthCodeData);

        void updateFailCodeView(String message);
    }

    class Presenter extends RxBasePresenter<View> {
        OpenDoorModel mOpenDoorModel;

        public Presenter() {
            mOpenDoorModel = new OpenDoorModel();
        }

        public void getOpenPwd(Context context, int inout) {
            mOpenDoorModel.getOpenPwd(inout)
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<UserAuthCodeResult>(context, R.string.loading_data, mView) {

                        @Override
                        public void onNext(UserAuthCodeResult result) {
                            if(result == null) return;
                            mView.updateUserAuthView(result.getAuthCodeData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            mView.updateFailCodeView(apiException.getErrorMessage());
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            mView.updateFailCodeView(throwable.getMessage());
                        }
                    }));
        }
    }
}
