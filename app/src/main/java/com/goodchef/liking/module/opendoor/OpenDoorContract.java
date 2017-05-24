package com.goodchef.liking.module.opendoor;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.UserAuthCodeResult;
import com.goodchef.liking.data.remote.ApiException;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:26
 * version 1.0.0
 */

public class OpenDoorContract {
    interface OpenDoorView extends BaseView {
        void updateUserAuthView(UserAuthCodeResult.UserAuthCodeData userAuthCodeData);

        void updateFailCodeView(String message);
    }

    public static class OpenDoorPresenter extends BasePresenter<OpenDoorView> {
        OpenDoorModel mOpenDoorModel;

        public OpenDoorPresenter(Context context, OpenDoorView mainView) {
            super(context, mainView);
            mOpenDoorModel = new OpenDoorModel();
        }

        public void getOpenPwd(int inout) {
            mOpenDoorModel.getOpenPwd(inout)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new ProgressObserver<UserAuthCodeResult>(mContext, R.string.loading_data, mView) {
                        @Override
                        public void onNext(UserAuthCodeResult result) {
                            if(result == null) return;
                            mView.updateUserAuthView(result.getAuthCodeData());
                        }

                        @Override
                        public void apiError(ApiException apiException) {
                            mView.updateFailCodeView(apiException.getMessage());
                        }

                        @Override
                        public void networkError(Throwable throwable) {
                            mView.updateFailCodeView(throwable.getMessage());
                        }
                    });
        }
    }
}
