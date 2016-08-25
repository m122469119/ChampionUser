package com.goodchef.liking.http.result.data;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/25 下午3:21
 */
public class GymData extends BaseData {


    /**
     * gym_id : 1
     * name : Liking Fit（复兴店）
     */

    @SerializedName("gym_id")
    private String gymId;
    @SerializedName("name")
    private String name;

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
}
