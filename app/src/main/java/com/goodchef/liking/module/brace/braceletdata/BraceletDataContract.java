package com.goodchef.liking.module.brace.braceletdata;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.retrofit.result.SportDataResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:33
 * version 1.0.0
 */

class BraceletDataContract {
    interface BraceletDataView extends BaseView {
        void updateSendSportDataView();

        void updateGetSportDataView(SportDataResult.SportData sportData);
    }


    public static class Presenter extends RxBasePresenter<BraceletDataView> {
        BraceletDataModel mBraceletDataModel;

        public Presenter() {
            mBraceletDataModel = new BraceletDataModel();
        }

        public void sendSportData(String sportData, String deviceId) {
            mBraceletDataModel.sendSportData(sportData, deviceId)
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<LikingResult>(mView) {
                        @Override
                        public void onNext(LikingResult value) {
                            if (value == null) return;
                            mView.updateSendSportDataView();
                        }
                    }));
        }

        public void getSportData() {
            mBraceletDataModel.getSportData()
                    .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<SportDataResult>(mView) {

                        @Override
                        public void onNext(SportDataResult value) {
                            if (value == null) return;
                            if (value.getData() != null) {
                                mView.updateGetSportDataView(value.getData());
                            }
                        }
                    }));
        }
    }

}
