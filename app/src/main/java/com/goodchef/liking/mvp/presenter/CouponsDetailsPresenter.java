package com.goodchef.liking.mvp.presenter;

import android.content.Context;

import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.goodchef.liking.eventmessages.CouponErrorMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.CouponsDetailsResult;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.view.CouponsDetailsView;
import com.goodchef.liking.module.data.local.Preference;

/**
 * Created on 2017/3/10
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsDetailsPresenter extends BasePresenter<CouponsDetailsView> {
    public CouponsDetailsPresenter(Context context, CouponsDetailsView mainView) {
        super(context, mainView);
    }



    public void getConponsDetail(String coupons_id) {
        LocationData locationData = Preference.getLocationData();
        if (locationData == null) {
            locationData = new LocationData();
        }

        LiKingApi.getConponsDetail(coupons_id, locationData.getLongitude(), locationData.getLatitude(), new RequestCallback<CouponsDetailsResult>() {
            @Override
            public void onSuccess(CouponsDetailsResult result) {
                if (LiKingVerifyUtils.isValid(mContext, result)) {
                    mView.updateCouponData(result.getData());
                } else {
                    postEvent(new CouponErrorMessage());
                    mView.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
            }
        });
    }

}
