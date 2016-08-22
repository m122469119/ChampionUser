package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderViewFragment;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.CouponView;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 下午2:14
 */
public class CouponPresenter extends BasePresenter<CouponView> {
    public CouponPresenter(Context context, CouponView mainView) {
        super(context, mainView);
    }

    public void getCoupons(String courseId, String goodInfo, Integer cardId, Integer type, Integer scheduleId, int page, BasePagerLoaderViewFragment fragment) {
        LiKingApi.getCoupons(courseId, goodInfo, cardId, type, scheduleId, Preference.getToken(), page, new BasePagerLoaderViewFragment.PagerRequestCallback<CouponsResult>(fragment) {
            @Override
            public void onSuccess(CouponsResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateCouponData(result.getCouponData());
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }
}
