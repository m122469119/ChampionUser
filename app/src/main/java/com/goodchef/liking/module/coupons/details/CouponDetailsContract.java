package com.goodchef.liking.module.coupons.details;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.CouponsDetailsResult;
import com.goodchef.liking.data.remote.retrofit.result.data.LocationData;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:14
 * version 1.0.0
 */

class CouponDetailsContract {

    interface View extends BaseView {
        void updateCouponData(CouponsDetailsResult.DataBean couponData);
    }

    public static class Presenter extends RxBasePresenter<View> {
        CouponDetailsModel mCouponDetailsModel;

        public Presenter() {
            mCouponDetailsModel = new CouponDetailsModel();
        }

        void getCouponsDetails(Context context, String couponCode) {
            LocationData locationData = LikingPreference.getLocationData();
            if (locationData == null) {
                locationData = new LocationData();
            }

            mCouponDetailsModel.getCouponDetails(couponCode, locationData.getLongitude(), locationData.getLatitude())
                    .subscribe(addObserverToCompositeDisposable(new ProgressObserver<CouponsDetailsResult>(context, R.string.loading_data, mView) {
                        @Override
                        public void onNext(CouponsDetailsResult result) {
                            if (result == null) return;
                            mView.updateCouponData(result.getData());
                        }

                    }));
        }
    }
}
