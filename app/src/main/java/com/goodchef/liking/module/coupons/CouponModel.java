package com.goodchef.liking.module.coupons;

import android.text.TextUtils;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.CouponsPersonResult;
import com.goodchef.liking.http.result.CouponsResult;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.data.remote.LikingNewApi;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:37
 * version 1.0.0
 */

public class CouponModel extends BaseModel {

    /**
     * 兑换优惠券
     *
     * @param code 优惠券码
     * @return
     */
    public Observable<LikingResult> exchangeCoupon(String code) {
        return LikingNewApi.getInstance().exchangeCoupon(UrlList.sHostVersion, LikingPreference.getToken(), code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取我的优惠券
     *
     * @param page 页码
     * @return
     */
    public Observable<CouponsPersonResult> getMyCoupons(int page) {
        return LikingNewApi.getInstance().getMyCoupons(UrlList.sHostVersion, LikingPreference.getToken(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取优惠券
     *
     * @param courseId
     * @param selectTimes
     * @param goodInfo
     * @param cardId
     * @param type
     * @param scheduleId
     * @param page
     * @param gymId
     * @return
     */
    public Observable<CouponsResult> getCoupons(String courseId, String selectTimes, String goodInfo, String cardId, String type, String scheduleId, int page, String gymId) {
        Map<String, String> map = new HashMap<>();
        map.put("version", UrlList.sHostVersion);
        map.put("page", page + "");
        map.put("token", LikingPreference.getToken());
        if (!TextUtils.isEmpty(courseId)) {
            map.put("course_id", courseId);
        }
        if (!TextUtils.isEmpty(selectTimes)) {
            map.put("select_times", selectTimes);
        }
        if (!TextUtils.isEmpty(goodInfo)) {
            map.put("good_info", goodInfo);
        }
        if (!TextUtils.isEmpty(cardId)) {
            map.put("card_id", cardId);
        }
        if (!TextUtils.isEmpty(type)) {
            map.put("type", type);
        }
        if (!TextUtils.isEmpty(scheduleId)) {
            map.put("schedule_id", scheduleId);
        }
        if (!TextUtils.isEmpty(gymId)) {
            map.put("gym_id", gymId);
        }
        return LikingNewApi.getInstance().getCoupons(UrlList.sHostVersion,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
