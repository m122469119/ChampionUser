package com.goodchef.liking.http.result.data;

/**
 * Created by Lennon on 16/3/2.
 */
public class LocationData {
    private String cityId;
    private String districtId;
    private double longitude;
    private double latitude;

    public LocationData(String cityId, String districtId, double longitude, double latitude) {
        this.cityId = cityId;
        this.districtId = districtId;
        this.longitude = longitude;
        this.latitude = latitude;
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
}
