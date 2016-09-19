package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/19 下午6:00
 */
public class RefreshChangeCityMessage extends BaseMessage {
    private String cityId;
    private double longitude;
    private double latitude;

    public RefreshChangeCityMessage(String cityId, double longitude, double latitude) {
        this.cityId = cityId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getCityId() {
        return cityId;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
