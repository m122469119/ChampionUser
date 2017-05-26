package com.goodchef.liking.data.remote;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.aaron.android.framework.base.thread.TaskScheduler;
import com.aaron.common.utils.DateUtils;
import com.aaron.common.utils.ListUtils;
import com.aaron.http.rxobserver.BaseRequestObserver;
import com.goodchef.liking.app.LikingApplicationLike;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.LikingNewApi;
import com.goodchef.liking.data.remote.retrofit.result.BaseConfigResult;
import com.goodchef.liking.data.remote.retrofit.result.SyncTimestampResult;
import com.goodchef.liking.data.remote.retrofit.result.data.City;
import com.goodchef.liking.data.remote.retrofit.result.data.CityData;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.utils.CityUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.reactivex.Observable;

/**
 * Created on 2017/05/25
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class LikingBaseRequestHelper {

    public static long sTimestampOffset = 0;
    public static long sRequestTimestamp = 0;
    public static long sRequestSyncTimestamp = 0;

    public static boolean isTimestampInit = false; //是否同步时间戳
    public static boolean isBaseConfigInit = false; //BaseConfig未与服务器同步成功

    public static BaseConfigResult sBaseConfigResult = null;

    public static Observable initTimestamp() {
        if(isTimestampInit) return null;
        sRequestSyncTimestamp = DateUtils.currentDataSeconds();
        Observable observable = LikingNewApi.getInstance().syncServerTimestamp();
        observable.subscribe(new BaseRequestObserver<SyncTimestampResult>() {
            @Override
            public void onNext(SyncTimestampResult value) {
                if (value != null && value.isSuccess()) {
                    isTimestampInit = true;
                    long currentSystemSeconds = DateUtils.currentDataSeconds();
                    sTimestampOffset = Long.parseLong(value.getData().getTimestamp())
                            + (currentSystemSeconds - sRequestSyncTimestamp) / 2
                            - currentSystemSeconds;
                }
            }

            @Override
            public void onError(Throwable e) {
                if (sBaseConfigResult == null) {
                    sBaseConfigResult = getLocalBaseConfig();
                }
            }
        });
        return observable;
    }

    public static Observable initBaseConfig() {
        if(isBaseConfigInit) return null;
        Observable observable = LikingNewApi.getInstance().baseConfig();
        observable.subscribe(new BaseRequestObserver<BaseConfigResult>() {
            @Override
            public void onNext(BaseConfigResult value) {
                if (value == null) return;
                isBaseConfigInit = true;
                sBaseConfigResult = value;
                //解析已开通城市
                loadOpenCitysInfo(LikingApplicationLike.getApp().getApplicationContext());
                LikingPreference.setBaseConfig(value);
                EventBus.getDefault().post(new InitApiFinishedMessage(true));
            }

            @Override
            public void onError(Throwable e) {
                if (sBaseConfigResult == null) {
                    sBaseConfigResult = getLocalBaseConfig();
                }
                EventBus.getDefault().post(new InitApiFinishedMessage(false));
            }
        });
        return observable;
    }

    public static List<City.RegionsData.CitiesData> getCitiesDataList() {
        List<City.RegionsData.CitiesData> citiesDatas = new ArrayList<>();
        BaseConfigResult baseResult = LikingPreference.getBaseConfig();
        if (baseResult == null) {
            return citiesDatas;
        }
        BaseConfigResult.ConfigData baseConfig = baseResult.getBaseConfigData();
        if (baseConfig == null) {
            return citiesDatas;
        }
        List<CityData> cityList = baseConfig.getCityList();
        if (ListUtils.isEmpty(cityList)) {
            return citiesDatas;
        }
        for (int i = 0; i < cityList.size(); i++) {
            City.RegionsData.CitiesData cityBean = new City.RegionsData.CitiesData();
            cityBean.setCityName(cityList.get(i).getCityName());
            cityBean.setCityId(cityList.get(i).getCityId() + "");
            citiesDatas.add(cityBean);
        }
        return citiesDatas;
    }


    /**
     * 加载以开放城市信息
     */
    public static void loadOpenCitysInfo(final Context context) {
        loadOpenCitysInfo(context, null);
    }

    public static void loadOpenCitysInfo(final Context context, final List<String> openCities) {
        TaskScheduler.execute(new Runnable() {
            @Override
            public void run() {
                ArrayMap<String, City.RegionsData.CitiesData> citiesMap = CityUtils.getLocalCityMap(context);
                List<CityData> cityList = new ArrayList<>();
                List<String> openCityCodes;
                BaseConfigResult.ConfigData baseConfigData = sBaseConfigResult.getBaseConfigData();
                if (baseConfigData == null) {
                    return;
                }
                if (openCities == null) {
                    openCityCodes = baseConfigData.getOpenCity();
                } else {
                    openCityCodes = openCities;
                }

                for (String cityCode : openCityCodes) {
                    CityData cityData = null;
                    City.RegionsData.CitiesData crc = citiesMap.get(cityCode);
                    try {
                        if (crc != null) {
                            //城市
                            cityData = new CityData();
                            cityData.setCityId(Integer.valueOf(crc.getCityId()));
                            cityData.setCityName(crc.getCityName());
                            List<CityData.DistrictData> districtAll = new ArrayList<>();
                            cityData.setDistrict(districtAll);

                            //地方
                            List<City.RegionsData.CitiesData.DistrictsData> districts = crc.getDistricts();
                            if (districts != null) {
                                for (City.RegionsData.CitiesData.DistrictsData district : districts) {
                                    CityData.DistrictData districtData = new CityData.DistrictData();
                                    districtData.setDistrictId(Integer.parseInt(district.getDistrictId()));
                                    districtData.setDistrictName(district.getDistrictName());
                                    districtAll.add(districtData);
                                }
                            }
                        }
                    } catch (Exception e) {
                    }

                    if (cityData != null) {
                        cityList.add(cityData);
                    }
                }
                sBaseConfigResult.getBaseConfigData().setCityList(cityList);
                LikingPreference.setBaseConfig(sBaseConfigResult);
            }
        });
    }


    private static BaseConfigResult getLocalBaseConfig() {
        BaseConfigResult baseConfigResult = LikingPreference.getBaseConfig();
        return baseConfigResult;
    }
}
