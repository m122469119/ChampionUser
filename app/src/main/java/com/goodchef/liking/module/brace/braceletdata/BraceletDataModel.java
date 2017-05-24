package com.goodchef.liking.module.brace.braceletdata;

import com.aaron.android.framework.base.mvp.model.BaseModel;
import com.goodchef.liking.http.api.UrlList;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.result.SportDataResult;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.data.remote.LikingNewApi;

import io.reactivex.Observable;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:33
 * version 1.0.0
 */

public class BraceletDataModel extends BaseModel {


    /**
     * 上报运动数据
     *
     * @param sportData 运动数据
     * @param deviceId  deviceID
     * @return
     */
    Observable<LikingResult> sendSportData(String sportData, String deviceId) {
        return LikingNewApi.getInstance().sendSportData(UrlList.sHostVersion, sportData, deviceId);
    }


    /**
     * 获取运动数据
     *
     * @return
     */
    Observable<SportDataResult> getSportData() {
        return LikingNewApi.getInstance().getSportData(UrlList.sHostVersion, LikingPreference.getToken());
    }

}
