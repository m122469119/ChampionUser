package com.goodchef.liking.module.coupons.gym;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.http.result.CouponsCities;
import com.goodchef.liking.data.remote.rxobserver.PagerLoadingObserver;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:27
 * version 1.0.0
 */

public class CouponsGymContract {

    interface CouponsGymView extends BaseView {
        void updateCouponData(CouponsCities.DataBean data);
    }

    public static class CouponsGymPresenter extends BasePresenter<CouponsGymView> {
        CouponsGymModel mCouponsGymModel;

        public CouponsGymPresenter(Context context, CouponsGymView mainView) {
            super(context, mainView);
            mCouponsGymModel = new CouponsGymModel();
        }

        public void getCouponsCitys(int page, String couponCode) {
            mCouponsGymModel.getCouponsGym(page, couponCode)
                    .subscribe(new PagerLoadingObserver<CouponsCities>(mContext, mView) {
                        @Override
                        public void onNext(CouponsCities result) {
                            super.onNext(result);
                            if(result == null) return;
                            mView.updateCouponData(result.getData());
                        }

                    });
        }
    }
}
