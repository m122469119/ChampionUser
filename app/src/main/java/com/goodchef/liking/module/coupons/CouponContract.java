package com.goodchef.liking.module.coupons;

import android.content.Context;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.CouponErrorMessage;
import com.goodchef.liking.http.result.CouponsPersonResult;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.base.rxobserver.LikingBaseObserver;
import com.goodchef.liking.module.base.rxobserver.PagerLoadingObserver;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:12
 * version 1.0.0
 */

public interface CouponContract {
    interface CouponView extends BaseView {
        void updateExchangeCode();

        void updateMyCouponData(CouponsPersonResult.DataBean dataBean);

        void updateCouponData(CouponsResult.CouponData couponData);
    }

    class CouponPresenter extends BasePresenter<CouponView> {
        CouponModel mCouponModel = null;

        public CouponPresenter(Context context, CouponView mainView) {
            super(context, mainView);
            mCouponModel = new CouponModel();
        }

        //兑换优惠券
        public void sendExchangeCouponsRequest(String code) {
            mCouponModel.exchangeCoupon(code).subscribe(new LikingBaseObserver<LikingResult>() {
                @Override
                public void onNext(LikingResult result) {
                    super.onNext(result);
                    if (LiKingVerifyUtils.isValid(mContext, result)) {
                        mView.updateExchangeCode();
                    } else {
                        mView.showToast(result.getMessage());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    mView.showToast(mContext.getString(R.string.exchange_fail));
                }
            });
        }

        //获取我的优惠券
        public void getMyCoupons(int page) {
            mCouponModel.getMyCoupons(page).subscribe(new PagerLoadingObserver<CouponsPersonResult>(mView) {
                @Override
                public void onNext(CouponsPersonResult result) {
                    super.onNext(result);
                    if (LiKingVerifyUtils.isValid(mContext, result)) {
                        mView.updateMyCouponData(result.getData());
                    } else {
                        postEvent(new CouponErrorMessage());
                        mView.showToast(result.getMessage());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }
            });
        }

        /**
         * 获取优惠券
         *
         * @param courseId    课程id
         * @param selectTimes 选择的时间
         * @param goodInfo    商品信心
         * @param cardId      购卡id
         * @param type        类型
         * @param scheduleId  日程id
         * @param page        页码
         * @param gymId       场馆id
         */
        public void getCoupons(String courseId, String selectTimes, String goodInfo, String cardId, String type, String scheduleId, int page, String gymId) {
            mCouponModel.getCoupons(courseId, selectTimes, goodInfo, cardId, type, scheduleId, page, gymId).subscribe(new PagerLoadingObserver<CouponsResult>(mView) {
                @Override
                public void onNext(CouponsResult result) {
                    super.onNext(result);
                    if (LiKingVerifyUtils.isValid(mContext, result)) {
                        mView.updateCouponData(result.getData());
                    } else {
                        postEvent(new CouponErrorMessage());
                        mView.showToast(result.getMessage());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }
            });
        }


    }
}
