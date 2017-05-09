package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PagerRequestCallback;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.CouponsCities;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.CouponsCitysView;
import com.goodchef.liking.module.data.local.Preference;

/**
 * Created on 2017/3/15
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsCitysPresenter extends BasePresenter<CouponsCitysView> {

    public CouponsCitysPresenter(Context context, CouponsCitysView mainView) {
        super(context, mainView);
    }



    public void requestCityPage(int page, String coupons_id, BasePagerLoaderFragment clazz){

        LocationData locationData = Preference.getLocationData();
        if (locationData == null) {
            locationData = new LocationData();
        }

        LiKingApi.getCouponsGym(page, coupons_id, locationData.getLongitude(), locationData.getLatitude(), new PagerRequestCallback<CouponsCities>(clazz) {
            @Override
            public void onSuccess(CouponsCities result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateCouponData(result.getData());
                } else {
//                    postEvent(new CouponErrorMessage());
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }

}
