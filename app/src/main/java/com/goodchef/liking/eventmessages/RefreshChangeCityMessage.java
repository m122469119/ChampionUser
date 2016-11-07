package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/19 下午6:00
 */
public class RefreshChangeCityMessage extends BaseMessage {
    private String cityId;
    private String longitude;
    private String latitude;

    public RefreshChangeCityMessage(String cityId, String longitude, String latitude) {
        this.cityId = cityId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getCityId() {
        return cityId;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }
}
