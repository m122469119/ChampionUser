package com.goodchef.liking.utils;

import com.aaron.android.codelibrary.utils.ListUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;

import java.util.List;


/**
 * Created on 16/2/22.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class CityUtils {
    private static final String TAG = "CityUtils";

    /**
     * 根据城市名称获取城市Id
     *
     * @param cityName 城市名称
     * @return 城市Id
     */
    public static String getCityId(String provinceName, String cityName) {
        String cityId = "310100";
        if (StringUtils.isEmpty(provinceName) && StringUtils.isEmpty(cityName)) {
            return cityId;
        }
        BaseConfigResult baseConfigResult = LiKingVerifyUtils.sBaseConfigResult;
        if (baseConfigResult == null) {
            return cityId;
        }
        BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
        if (baseConfigData == null) {
            return cityId;
        }
        List<CityData> cityDataList = baseConfigData.getCityList();
        if (ListUtils.isEmpty(cityDataList)) {
            return cityId;
        }
        for (CityData cityData : cityDataList) {
            if (cityData == null) {
                continue;
            }
            if (!StringUtils.isEmpty(cityName) &&
                    (cityName.contains(cityData.getCityName()) || cityData.getCityName().contains(cityName))) {
                cityId = String.valueOf(cityData.getCityId());
                break;
            }
            if (!StringUtils.isEmpty(provinceName) &&
                    (provinceName.contains(cityData.getCityName()) || cityData.getCityName().contains(provinceName))) {
                cityId = String.valueOf(cityData.getCityId());
                break;
            }
        }
        LogUtils.d(TAG, "城市ID: " + cityId);
        return cityId;
    }

    /**
     * 根据城市名称获取城市Id
     *
     * @return 城市Id
     */
    public static String getDistrictId(String districtName) {
        String districtId = "310104";
        if (StringUtils.isEmpty(districtName)) {
            return districtId;
        }
        BaseConfigResult baseConfigResult = LiKingVerifyUtils.sBaseConfigResult;
        if (baseConfigResult == null) {
            return districtId;
        }
        BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
        if (baseConfigData == null) {
            return districtId;
        }
        List<CityData> cityDataList = baseConfigData.getCityList();
        if (ListUtils.isEmpty(cityDataList)) {
            return districtId;
        }
        for (CityData cityData : cityDataList) {
            if (cityData == null) {
                continue;
            }
            List<CityData.DistrictData> districtDataList = cityData.getDistrict();
            if (ListUtils.isEmpty(districtDataList)) {
                continue;
            }
            for (CityData.DistrictData districtData : districtDataList) {
                if (districtData == null) {
                    continue;
                }
                if (!StringUtils.isEmpty(districtName) &&
                        (districtName.contains(districtData.getDistrictName()) || districtData.getDistrictName().contains(districtName))) {
                    districtId = String.valueOf(districtData.getDistrictId());
                    break;
                }
            }
        }
        LogUtils.d(TAG, "区域ID: " + districtId);
        return districtId;
    }

    /**
     * 根据城市获取经度
     *
     * @param longitude    经度
     * @param districtName 地区名称
     * @return longitudeStr
     */
    public static String getLongitude(double longitude, String districtName) {
        String longitudeStr = "0";
        if (StringUtils.isEmpty(districtName)) {
            return longitudeStr;
        }
        BaseConfigResult baseConfigResult = LiKingVerifyUtils.sBaseConfigResult;
        if (baseConfigResult == null) {
            return longitudeStr;
        }
        BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
        if (baseConfigData == null) {
            return longitudeStr;
        }
        List<CityData> cityDataList = baseConfigData.getCityList();
        if (ListUtils.isEmpty(cityDataList)) {
            return longitudeStr;
        }
        for (CityData cityData : cityDataList) {
            if (cityData == null) {
                continue;
            }
            List<CityData.DistrictData> districtDataList = cityData.getDistrict();
            if (ListUtils.isEmpty(districtDataList)) {
                continue;
            }
            for (CityData.DistrictData districtData : districtDataList) {
                if (districtData == null) {
                    continue;
                }
                if (!StringUtils.isEmpty(districtName) && (districtName.contains(districtData.getDistrictName()) || districtData.getDistrictName().contains(districtName))) {
                    longitudeStr = longitude + "";
                    break;
                }
            }
        }
        return longitudeStr;
    }

    /**
     * 获取纬度
     *
     * @param latitude     纬度
     * @param districtName 地区名称
     * @return latitudeStr
     */
    public static String getLatitude(double latitude, String districtName) {
        String latitudeStr = "0";
        if (StringUtils.isEmpty(districtName)) {
            return latitudeStr;
        }
        BaseConfigResult baseConfigResult = LiKingVerifyUtils.sBaseConfigResult;
        if (baseConfigResult == null) {
            return latitudeStr;
        }
        BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
        if (baseConfigData == null) {
            return latitudeStr;
        }
        List<CityData> cityDataList = baseConfigData.getCityList();
        if (ListUtils.isEmpty(cityDataList)) {
            return latitudeStr;
        }
        for (CityData cityData : cityDataList) {
            if (cityData == null) {
                continue;
            }
            List<CityData.DistrictData> districtDataList = cityData.getDistrict();
            if (ListUtils.isEmpty(districtDataList)) {
                continue;
            }
            for (CityData.DistrictData districtData : districtDataList) {
                if (districtData == null) {
                    continue;
                }
                if (!StringUtils.isEmpty(districtName) && (districtName.contains(districtData.getDistrictName()) || districtData.getDistrictName().contains(districtName))) {
                    latitudeStr = latitude + "";
                    break;
                }
            }
        }
        return latitudeStr;
    }
}
