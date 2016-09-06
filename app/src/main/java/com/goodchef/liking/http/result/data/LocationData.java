package com.goodchef.liking.http.result.data;

/**
 * Created by Lennon on 16/3/2.
 */
public class LocationData {
    private String cityId;
    private String districtId;
    private double longitude;
    private double latitude;
    private String cityName;
    private boolean isPositionSuccess;

    public LocationData(String cityId, String districtId, double longitude, double latitude,String cityName,boolean isPositionSuccess) {
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

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getCityName() {
        return cityName;
    }

    public boolean isPositionSuccess() {
        return isPositionSuccess;
    }
}
