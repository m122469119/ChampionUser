package com.goodchef.liking.module.gym;

import android.text.TextUtils;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.CheckGymListResult;
import com.goodchef.liking.http.result.CityListResult;
import com.goodchef.liking.http.result.GymDetailsResult;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.LikingNewApi;
import com.goodchef.liking.data.remote.RxUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;


/**
 * Created on 2017/05/15
 * desc: 场馆相关
 *
 * @author: chenlei
 * @version:1.0
 */

public class GymModel extends BaseModel {

    /**
     * 查看场馆
     *
     * @param cityId 城市id
     */
    public Observable<CheckGymListResult> getCheckGymList(int cityId, String longitude, String latitude) {
        Map<String, String> map = new HashMap<>();
        map.put("city_id", String.valueOf(cityId));
        if (!StringUtils.isEmpty(longitude)) {
            map.put("longitude", longitude);
        }
        if (!StringUtils.isEmpty(latitude)) {
            map.put("latitude", latitude);
        }
        String token = LikingPreference.getToken();
        if (!TextUtils.isEmpty(token)) {
            map.put("token", token);
        }
        return LikingNewApi.getInstance()
                .getCheckGymList(UrlList.sHostVersion, map)
                .compose(RxUtils.<CheckGymListResult>applyHttpSchedulers());
    }

    /**
     * 获取城市列表
     * @return
     */
    public Observable<CityListResult> getCityList(){
        return LikingNewApi.getInstance()
                .getCityList(UrlList.sHostVersion)
                .compose(RxUtils.<CityListResult>applyHttpSchedulers());
    }

    /**
     * 场馆详情
     * @param gymId
     * @return
     */
    public Observable<GymDetailsResult> getGymDetails(String gymId) {
        return LikingNewApi.getInstance()
                .getGymDetails(UrlList.sHostVersion, gymId)
                .compose(RxUtils.<GymDetailsResult>applyHttpSchedulers());
    }

    public LocationData getCurrLocation() {
        return LikingPreference.getLocationData();
    }
}
