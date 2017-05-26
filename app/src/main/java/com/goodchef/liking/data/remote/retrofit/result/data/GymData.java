package com.goodchef.liking.data.remote.retrofit.result.data;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/25 下午3:21
 */
public class GymData extends Data {


    /**
     * gym_id : 1
     * name : Liking Fit（复兴店）
     */

    @SerializedName("gym_id")
    private String gymId;
    @SerializedName("name")
    private String name;
    @SerializedName("distance")
    private String distance;
    @SerializedName("city_id")
    private String cityId;

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
