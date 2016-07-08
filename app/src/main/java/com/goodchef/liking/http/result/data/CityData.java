package com.goodchef.liking.http.result.data;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/8 下午2:23
 */
public class CityData extends BaseData {

    @SerializedName("city_id")
    private int cityId;
    @SerializedName("city_name")
    private String cityName;
    private boolean isSelct;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isSelct() {
        return isSelct;
    }

    public void setSelct(boolean selct) {
        isSelct = selct;
    }
}
