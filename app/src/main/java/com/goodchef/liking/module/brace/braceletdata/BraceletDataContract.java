package com.goodchef.liking.module.brace.braceletdata;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.retrofit.result.SportDataResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:33
 * version 1.0.0
 */

public class BraceletDataContract {
    interface BraceletDataView extends BaseView {
        void updateSendSportDataView();

        void updateGetSportDataView(SportDataResult.SportData sportData);
    }


    public static class BraceletDataPresenter extends BasePresenter<BraceletDataView> {
        BraceletDataModel mBraceletDataModel;

        public BraceletDataPresenter(Context context, BraceletDataView mainView) {
            super(context, mainView);
            mBraceletDataModel = new BraceletDataModel();
        }

        public void sendSportData(String sportData, String deviceId) {
            mBraceletDataModel.sendSportData(sportData, deviceId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new LikingBaseObserver<LikingResult>(mContext, mView) {
                        @Override
                        public void onNext(LikingResult value) {
                            if (value == null) return;
                            mView.updateSendSportDataView();
                        }
                    });
        }

        public void getSportData() {
            mBraceletDataModel.getSportData()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new LikingBaseObserver<SportDataResult>(mContext, mView) {
                @Override
                public void onNext(SportDataResult value) {
                    if (value == null) return;
                    if (value.getData() != null) {
                        mView.updateGetSportDataView(value.getData());
                    }
                }
            });
        }


    }

}
