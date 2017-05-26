package com.goodchef.liking.data.remote.retrofit.result.data;

import com.aaron.http.code.result.Data;

/**
 * Created by Lennon on 16/3/2.
 */
public class LocationData extends Data {
    private String cityId;
    private String districtId;
    private String longitude;
    private String latitude;
    private String cityName;
    private boolean isPositionSuccess;

    public LocationData() {
    }

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
