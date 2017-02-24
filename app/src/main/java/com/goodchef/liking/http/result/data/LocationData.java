package com.goodchef.liking.http.result.data;

import com.aaron.android.codelibrary.http.result.BaseData;

/**
 * Created by Lennon on 16/3/2.
 */
public class LocationData extends BaseData{
    private String cityId;
    private String districtId;
    private String longitude;
    private String latitude;
    private String cityName;
    private boolean isPositionSuccess;

    public LocationData(String cityId, String districtId, String longitude, String latitude, String cityName, boolean isPositionSuccess) {
        this.cityId = cityId;
        this.districtId = districtId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.cityName = cityName;
        this.isPositionSuccess = isPositionSuccess;
    }

    public String getCityId() {
        return cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getCityName() {
        return cityName;
    }

    public boolean isPositionSuccess() {
        return isPositionSuccess;
    }
}
