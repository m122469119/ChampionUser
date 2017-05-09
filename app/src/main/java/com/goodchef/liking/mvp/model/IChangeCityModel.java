package com.goodchef.liking.mvp.model;

import com.aaron.http.code.RequestCallback;
import com.goodchef.liking.http.result.CityListResult;

/**
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/20.
 */

public interface IChangeCityModel {

    void getCityList(RequestCallback<CityListResult> callback);
}
