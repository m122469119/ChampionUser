package com.goodchef.liking.module.gym;

import android.text.TextUtils;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.CheckGymListResult;
import com.goodchef.liking.http.result.data.LocationData;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.data.remote.LikingNewApi;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public LocationData getCurrLocation() {
        return LikingPreference.getLocationData();
    }
}
