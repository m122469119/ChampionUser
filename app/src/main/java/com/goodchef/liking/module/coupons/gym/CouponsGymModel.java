package com.goodchef.liking.module.coupons.gym;

import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.CouponsCities;
import com.goodchef.liking.data.remote.retrofit.result.data.LocationData;

import io.reactivex.Observable;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:27
 * version 1.0.0
 */

public class CouponsGymModel {

    /**
     * 获取优惠券可使用的城市列表
     *
     * @param page       页码
     * @param couponCode 优惠券码
     * @return Observable<CouponsCities>
     */
    public Observable<CouponsCities> getCouponsGym(int page, String couponCode) {
        LocationData locationData = LikingPreference.getLocationData();
        if (locationData == null) {
            locationData = new LocationData();
        }
        return LikingNewApi.getInstance().getCouponsGym(LikingNewApi.sHostVersion, page, couponCode, locationData.getLongitude(), locationData.getLatitude())
                .compose(RxUtils.<CouponsCities>applyHttpSchedulers());
    }
}
