package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.widget.refresh.BasePagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PagerRequestCallback;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.CouponsCities;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.CouponsCitysView;
import com.goodchef.liking.storage.Preference;

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

        LiKingApi.getCouponsGym(page, coupons_id, locationData.getLongitude(), locationData.getLongitude(), new PagerRequestCallback<CouponsCities>(clazz) {
            @Override
            public void onSuccess(CouponsCities result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateCouponData(result.getData());
                } else {
//                    postEvent(new CouponErrorMessage());
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
