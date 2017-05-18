package com.goodchef.liking.module.coupons.details;

import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.CouponsDetailsResult;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.data.remote.LikingNewApi;
import com.goodchef.liking.module.data.remote.RxUtils;

import io.reactivex.Observable;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:14
 * version 1.0.0
 */

public class CouponDetailsModel {

    /**
     * 获取优惠券详情
     *
     * @param couponCode 优惠券码
     * @param longitude  经度
     * @param latitude   维度
     * @return Observable<CouponsDetailsResult>
     */
    public Observable<CouponsDetailsResult> getCouponDetails(String couponCode, String longitude, String latitude) {
        return LikingNewApi.getInstance()
                .getCouponDetails(UrlList.sHostVersion, LikingPreference.getToken(),
                couponCode, longitude, latitude)
                .compose(RxUtils.<CouponsDetailsResult>applyHttpSchedulers());
    }
}
