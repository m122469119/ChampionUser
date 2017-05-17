package com.goodchef.liking.module.coupons.details;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.CouponsDetailsResult;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.data.remote.ResponseThrowable;
import com.goodchef.liking.module.data.remote.rxobserver.ProgressObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:14
 * version 1.0.0
 */

public class CouponDetailsContract {

    interface CouponDetailsView extends BaseView {
        void updateCouponData(CouponsDetailsResult.DataBean couponData);
    }

    public static class CouponDetailsPresenter extends BasePresenter<CouponDetailsView> {
        CouponDetailsModel mCouponDetailsModel;

        public CouponDetailsPresenter(Context context, CouponDetailsView mainView) {
            super(context, mainView);
            mCouponDetailsModel = new CouponDetailsModel();
        }

        public void getCouponsDetails(String couponCode) {
            LocationData locationData = LikingPreference.getLocationData();
            if (locationData == null) {
                locationData = new LocationData();
            }

            mCouponDetailsModel.getCouponDetails(couponCode, locationData.getLongitude(), locationData.getLatitude())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new ProgressObserver<CouponsDetailsResult>(mContext, R.string.loading_data) {
                        @Override
                        public void onNext(CouponsDetailsResult result) {
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                                mView.updateCouponData(result.getData());
                            } else {
                                mView.showToast(result.getMessage());
                            }
                        }

                        @Override
                        public void onError(ResponseThrowable responseThrowable) {

                        }
                    });
        }
    }
}
