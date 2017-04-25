package com.goodchef.liking.mvp.model;

import android.content.Context;

import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.map.LocationListener;
import com.aaron.map.amap.AmapGDLocation;
import com.amap.api.location.AMapLocation;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;

/**
 * Created on 2017/3/7
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class AmapGDLocationModel {

    public AmapGDLocation mAmapGDLocation;
    public String longitude;
    public String latitude;

    public String currentCityName;//当前定位城市
    public String currentCityId;//当前定位的城市id


    public interface Callback{
        void receive(AMapLocation object);
        void start();
        void end();
    }

    /***
     * 初始化定位
     */
    public void initTitleLocation(final Context context, final Callback callback) {
        mAmapGDLocation = new AmapGDLocation(context);
        mAmapGDLocation.setLocationListener(new LocationListener<AMapLocation>() {
            @Override
            public void receive(AMapLocation object) {
                LiKingVerifyUtils.initApi(context);

                if (object != null && object.getErrorCode() == 0) {//定位成功
                    currentCityName = StringUtils.isEmpty(object.getCity()) ? null : object.getProvince();
                    currentCityId = object.getCityCode();
                    longitude = object.getLongitude() + "";
                    latitude = object.getLatitude() + "";
                }
                callback.receive(object);
            }

            @Override
            public void start() {
                callback.start();
            }

            @Override
            public void end() {
                LogUtils.i("dust", "定位结束...");
                callback.end();
            }
        });
        mAmapGDLocation.start();
    }
}
