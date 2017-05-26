package com.goodchef.liking.module.coupons;

import android.text.TextUtils;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.CouponsPersonResult;
import com.goodchef.liking.data.remote.retrofit.result.CouponsResult;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

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
        return LikingNewApi.getInstance().exchangeCoupon(LikingNewApi.sHostVersion, LikingPreference.getToken(), code)
                .compose(RxUtils.<LikingResult>applyHttpSchedulers());
    }

    /**
     * 获取我的优惠券
     *
     * @param page 页码
     * @return
     */
    public Observable<CouponsPersonResult> getMyCoupons(int page) {
        return LikingNewApi.getInstance().getMyCoupons(LikingNewApi.sHostVersion, LikingPreference.getToken(), page)
                .compose(RxUtils.<CouponsPersonResult>applyHttpSchedulers());
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
        map.put("version", LikingNewApi.sHostVersion);
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
        return LikingNewApi.getInstance().getCoupons(LikingNewApi.sHostVersion, map)
                .compose(RxUtils.<CouponsResult>applyHttpSchedulers());
    }


}
