package com.goodchef.liking.module.coupons.gym;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.http.result.CouponsCities;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.data.remote.rxobserver.PagerLoadingObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new PagerLoadingObserver<CouponsCities>(mContext, mView) {
                        @Override
                        public void onNext(CouponsCities result) {
                            super.onNext(result);
                            if (LiKingVerifyUtils.isValid(mContext, result)) {
                               mView.updateCouponData(result.getData());
                            }else {
                                mView.showToast(result.getMessage());
                            }
                        }

                    });
        }
    }
}
