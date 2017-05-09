package com.goodchef.liking.mvp.model;

import com.aaron.http.code.RequestCallback;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.CityListResult;

/**
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/20.
 */

public class ChangeCityModel implements IChangeCityModel{
    @Override
    public void getCityList(RequestCallback<CityListResult> callback){
        LiKingApi.getCityList(callback);
    }
}
