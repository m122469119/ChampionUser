package com.goodchef.liking.module.coupons.gym;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.data.remote.retrofit.result.CouponsCities;
import com.goodchef.liking.data.remote.rxobserver.PagerLoadingObserver;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:27
 * version 1.0.0
 */

class CouponsGymContract {

    interface View extends BaseView {
        void updateCouponData(CouponsCities.DataBean data);
    }

    public static class Presenter extends RxBasePresenter<View> {
        CouponsGymModel mCouponsGymModel;

        public Presenter() {
            mCouponsGymModel = new CouponsGymModel();
        }

        void getCouponsCitys(int page, String couponCode) {
            mCouponsGymModel.getCouponsGym(page, couponCode)
                    .subscribe(addObserverToCompositeDisposable(new PagerLoadingObserver<CouponsCities>(mView) {
                        @Override
                        public void onNext(CouponsCities result) {
                            super.onNext(result);
                            if(result == null) return;
                            mView.updateCouponData(result.getData());
                        }

                    }));
        }
    }
}
