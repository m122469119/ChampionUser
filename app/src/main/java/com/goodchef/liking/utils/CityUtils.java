package com.goodchef.liking.utils;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.aaron.android.codelibrary.utils.ListUtils;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.data.City;
import com.goodchef.liking.http.result.data.CityData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.ReadProvincesCitiesJson;
import com.google.gson.Gson;

import java.util.ArrayList;
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
     * @param cityCode 城市编码
     * @return 城市Id
     */
    public static String getCityId(Context context, String cityCode) {
        String cityId = "310100";
        if (StringUtils.isEmpty(cityCode)) {
            return cityId;
        }
//        if (StringUtils.isEmpty(provinceName) && StringUtils.isEmpty(cityName)) {
//            return cityId;
//        }
//        BaseConfigResult baseConfigResult = LiKingVerifyUtils.sBaseConfigResult;
//        if (baseConfigResult == null) {
//            return cityId;
//        }
//        BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
//        if (baseConfigData == null) {
//            return cityId;
//        }
//        List<CityData> cityDataList = baseConfigData.getCityList();
//        if (ListUtils.isEmpty(cityDataList)) {
//            return cityId;
//        }
//        for (CityData cityData : cityDataList) {
//            if (cityData == null) {
//                continue;
//            }
//            if (!StringUtils.isEmpty(cityName) &&
//                    (cityName.contains(cityData.getCityName()) || cityData.getCityName().contains(cityName))) {
//                cityId = String.valueOf(cityData.getCityId());
//                break;
//            }
//            if (!StringUtils.isEmpty(provinceName) &&
//                    (provinceName.contains(cityData.getCityName()) || cityData.getCityName().contains(provinceName))) {
//                cityId = String.valueOf(cityData.getCityId());
//                break;
//            }
//        }
        ArrayMap<String, City.RegionsData.CitiesData> citiesMap = CityUtils.getLocalCityMap(context);
        City.RegionsData.CitiesData city = citiesMap.get(cityCode);
        if (city != null && !StringUtils.isEmpty(city.getCityName())) {
            cityId = city.getCityId();
        }
        LogUtils.i(TAG, "城市ID: " + cityId);
        return cityId;
    }

    /**
     * 根据区域名称获取城市Id
     *
     * @return 区域id
     */
    public static String getDistrictId(Context context, String cityCode, String districtName) {
        String districtId = "310104";
        if (StringUtils.isEmpty(cityCode)) {
            return districtId;
        }
        if (StringUtils.isEmpty(districtName)) {
            return districtId;
        }
//        BaseConfigResult baseConfigResult = LiKingVerifyUtils.sBaseConfigResult;
//        if (baseConfigResult == null) {
//            return districtId;
//        }
//        BaseConfigResult.BaseConfigData baseConfigData = baseConfigResult.getBaseConfigData();
//        if (baseConfigData == null) {
//            return districtId;
//        }
//        List<CityData> cityDataList = baseConfigData.getCityList();
//        if (ListUtils.isEmpty(cityDataList)) {
//            return districtId;
//        }
//        for (CityData cityData : cityDataList) {
//            if (cityData == null) {
//                continue;
//            }
//            List<CityData.DistrictData> districtDataList = cityData.getDistrict();
//            if (ListUtils.isEmpty(districtDataList)) {
//                continue;
//            }
//            for (CityData.DistrictData districtData : districtDataList) {
//                if (districtData == null) {
//                    continue;
//                }
//                if (!StringUtils.isEmpty(districtName) &&
//                        (districtName.contains(districtData.getDistrictName()) || districtData.getDistrictName().contains(districtName))) {
//                    districtId = String.valueOf(districtData.getDistrictId());
//                    break;
//                }
//            }
//        }
        ArrayMap<String, City.RegionsData.CitiesData> citiesMap = CityUtils.getLocalCityMap(context);
        City.RegionsData.CitiesData city = citiesMap.get(cityCode);
        if (city != null && !StringUtils.isEmpty(city.getCityName())) {
            List<City.RegionsData.CitiesData.DistrictsData> districtsList = city.getDistricts();
            if (!ListUtils.isEmpty(districtsList)) {
                for (City.RegionsData.CitiesData.DistrictsData districtsData : districtsList) {
                    if (districtName.contains(districtsData.getDistrictName()) || districtName.equals(districtsData.getDistrictName())) {
                        districtId = districtsData.getDistrictId();
                        break;
                    }
                }
            }
        }
        LogUtils.i(TAG, "区域ID: " + districtId);
        return districtId;
    }

    /**
     * 根据城市获取经度
     *
     * @param longitude 经度
     * @return longitudeStr
     */
    public static String getLongitude(Context context, String cityCode, double longitude) {
        String longitudeStr = "0";
        ArrayMap<String, City.RegionsData.CitiesData> citiesMap = CityUtils.getLocalCityMap(context);
        City.RegionsData.CitiesData city = citiesMap.get(cityCode);
        if (city != null && !StringUtils.isEmpty(city.getCityName())) {
            longitudeStr = longitude + "";
        }
        return longitudeStr;
    }

    /**
     * 获取纬度
     *
     * @param latitude 纬度
     * @return latitudeStr
     */
    public static String getLatitude(Context context, String cityCode, double latitude) {
        String latitudeStr = "0";
        ArrayMap<String, City.RegionsData.CitiesData> citiesMap = CityUtils.getLocalCityMap(context);
        City.RegionsData.CitiesData city = citiesMap.get(cityCode);
        if (city != null && !StringUtils.isEmpty(city.getCityName())) {
            latitudeStr = latitude + "";
        }
        return latitudeStr;
    }


    public static List<City.RegionsData> getLocalCityList(Context context) {
        List<City.RegionsData> cityList = new ArrayList<>();
        String jsonStr = ReadProvincesCitiesJson.getJson(context);//获取省市json串
        if (!StringUtils.isEmpty(jsonStr)) {
            Gson gson = new Gson();
            City data = gson.fromJson(jsonStr, City.class);
            cityList = data.getRegions();
        }
        return cityList;
    }

    public static ArrayMap<String, City.RegionsData.CitiesData> getLocalCityMap(Context context) {
        //城市code 以及下面地方放在map中
        ArrayMap<String, City.RegionsData.CitiesData> citiesMap = new ArrayMap<>();
        for (City.RegionsData crd : getLocalCityList(context)) {
            List<City.RegionsData.CitiesData> cities = crd.getCities();
            if (cities != null) {
                for (City.RegionsData.CitiesData citi : cities) {
                    citiesMap.put(citi.getCityCode(), citi);
                }
            }
        }
        return citiesMap;
    }

}
