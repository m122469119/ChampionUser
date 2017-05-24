package com.goodchef.liking.utils;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.aaron.common.utils.ListUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.http.result.BaseConfigResult;
import com.goodchef.liking.http.result.data.City;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
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

    /**
     * 解析本地城市地区 得到List集合
     * @param context
     * @return
     */
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

    /**
     *  解析本地城市地区 得到Map集合
     * @param context
     * @return
     */
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


    /**
     * 定位成功，判断当前城市是否在我们开通的城市范围内
     * @param cityCode
     * @return
     */
    public static boolean isDredge(String cityCode){
        boolean cityIsDredge = false;
        BaseConfigResult baseConfigResult = LiKingVerifyUtils.sBaseConfigResult;
        if (baseConfigResult ==null){
            return false;
        }
        BaseConfigResult.ConfigData configData = LiKingVerifyUtils.sBaseConfigResult.getBaseConfigData();
        if (configData ==null){
            return false;
        }
        List<String> openCityCodes = configData.getOpenCity();
        if (ListUtils.isEmpty(openCityCodes)){
            return false;
        }

        for (int i=0 ;i<openCityCodes.size();i++){
            if (openCityCodes.get(i).equals(cityCode)){
                cityIsDredge = true;
                break;
            }
        }
        return cityIsDredge;
    }

}
