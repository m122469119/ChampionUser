package com.goodchef.liking.mvp.model;

import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.module.data.local.LikingPreference;

import java.util.List;

/**
 * Created on 2017/3/7
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class ChangeGymModel {

    public List<CityData> cityDataList;//开通服务的城市列表

    public List<CityData> getCityListData() {
        BaseConfigResult baseConfigResult = LikingPreference.getBaseConfig();
        if (baseConfigResult != null) {
            BaseConfigResult.ConfigData baseConfigData = baseConfigResult.getBaseConfigData();
            if (baseConfigData != null) {
                cityDataList = baseConfigData.getCityList();
                return cityDataList;
            }
        }
        return cityDataList;
    }

    /**
     * 处理当前定位城市是否在开通城市范围之内
     */
    public String doLocationCity(String currentCityName, String currentCityId) {
        String cityId = "";
        if (cityDataList != null && cityDataList.size() > 0) {
            for (CityData cityData : cityDataList) {
                if (currentCityName.equals(cityData.getCityName())
                        || currentCityName.contains(cityData.getCityName())) {
                    cityId = cityData.getCityId() + "";
                    return cityId;
                }
            }
            if (StringUtils.isEmpty(cityId)) {
                cityId = currentCityId;
            }
        }
        return cityId;
    }
}
