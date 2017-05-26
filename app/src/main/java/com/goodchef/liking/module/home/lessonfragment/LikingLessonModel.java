package com.goodchef.liking.module.home.lessonfragment;

import android.text.TextUtils;

import com.goodchef.liking.data.remote.RxUtils;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.data.remote.LikingNewApi;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:01
 * version 1.0.0
 */

public class LikingLessonModel {

    /**
     * 获取banner
     *
     * @return Observable<BannerResult>
     */
    Observable<BannerResult> getBanner() {
        return LikingNewApi.getInstance().getBanner(UrlList.sHostVersion)
                .compose(RxUtils.<BannerResult>applyHttpSchedulers());
    }


    /**
     * 获取首页数据
     *
     * @param token       token
     * @param longitude   经度
     * @param latitude    维度
     * @param cityId      城市id
     * @param districtId  街道id
     * @param currentPage 当前页码
     * @param gymId       场馆id
     * @return CoursesResult
     */
    Observable<CoursesResult> getHomeData(String token, String longitude, String latitude, String cityId, String districtId, int currentPage, String gymId) {
        Map<String, Object> map = new HashMap<>();
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("city_id", cityId);
        map.put("district_id", districtId);
        map.put("current_page", currentPage);
        map.put("gym_id", gymId);
        if (!TextUtils.isEmpty(token)) {
            map.put("token", token);
        }
        return LikingNewApi.getInstance().getHomeData(UrlList.sHostVersion, map)
                .compose(RxUtils.<CoursesResult>applyHttpSchedulers());
    }


}
