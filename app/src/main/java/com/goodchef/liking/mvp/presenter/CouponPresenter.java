package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PagerRequestCallback;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.eventmessages.CouponErrorMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.CouponsPersonResult;
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



    public void getCoupons(String courseId, String selectTimes, String goodInfo, String cardId, String type, String scheduleId, int page, String gymId, BasePagerLoaderFragment fragment) {
        LiKingApi.getCoupons(courseId, selectTimes, goodInfo, cardId, type, scheduleId, Preference.getToken(), page, gymId, new PagerRequestCallback<CouponsResult>(fragment) {
            @Override
            public void onSuccess(CouponsResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateCouponData(result.getData());
                } else {
                    postEvent(new CouponErrorMessage());
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }


    public void getMyConpons(int page, BasePagerLoaderFragment fragment) {
        LiKingApi.getMyConpons( page, new PagerRequestCallback<CouponsPersonResult>(fragment) {
            @Override
            public void onSuccess(CouponsPersonResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateMyCouponData(result.getData());
                } else {
                    postEvent(new CouponErrorMessage());
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
