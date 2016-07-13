package com.goodchef.liking.eventmessages;


import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * Created on 16/2/25.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class MainAddressChanged extends BaseMessage {
    private double mLongitude;
    private double mLatitude;
    private String mCityId;
    private String mDistrictId;
    private String mCityName;
    private boolean mIsLocationSuccess;

    public MainAddressChanged(double longitude, double latitude, String cityId, String districtId,String cityName,boolean isLocationSuccess) {
        mLongitude = longitude;
        mLatitude = latitude;
        mCityId = cityId;
        mDistrictId = districtId;
        mCityName = cityName;
        mIsLocationSuccess = isLocationSuccess;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public String getCityId() {
        return mCityId;
    }

    public String getDistrictId() {
        return mDistrictId;
    }

    public String getCityName() {
        return mCityName;
    }

    public boolean isLocationSuccess() {
        return mIsLocationSuccess;
    }
}
