package com.goodchef.liking.module.home;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.retrofit.result.BaseConfigResult;
import com.goodchef.liking.data.remote.retrofit.result.SyncTimestampResult;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:22
 * version 1.0.0
 */

public class LikingBaseContract {

    interface LikingBaseView extends BaseView {

    }

    public static class LikingBasePresenter extends BasePresenter<LikingBaseView> {
        LikingBaseModel mLikingBaseModel;

        public LikingBasePresenter(Context context, LikingBaseView mainView) {
            super(context, mainView);
            mLikingBaseModel = new LikingBaseModel();
        }

        public void syncServerTimestamp() {
            mLikingBaseModel.syncServerTimestamp()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new LikingBaseObserver<SyncTimestampResult>(mContext, mView) {
                        @Override
                        public void onNext(SyncTimestampResult value) {
                            if (value == null) return;
                            if (value.getData() != null) {

                            }
                        }
                    });
        }

        public void baseConfig() {
            mLikingBaseModel.baseConfig().observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe(new LikingBaseObserver<BaseConfigResult>(mContext, mView) {
                @Override
                public void onNext(BaseConfigResult value) {
                    if (value == null) return;
                    if (value.getBaseConfigData() != null) {

                    }
                }
            });
        }
    }

}
