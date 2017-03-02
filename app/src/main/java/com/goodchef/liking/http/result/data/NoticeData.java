package com.goodchef.liking.http.result.data;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 2017/2/28
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class NoticeData extends BaseData {


    /**
     * aid : 82
     * gym_id : 1
     * gymName : LikingFit复兴店
     */

    @SerializedName("aid")
    private String aid;
    @SerializedName("gym_id")
    private String gym_id;
    @SerializedName("gym_name")
    private String gym_name;
    @SerializedName("alert")
    private String gym_content;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getGym_id() {
        return gym_id;
    }

    public void setGym_id(String gym_id) {
        this.gym_id = gym_id;
    }

    public String getGymName() {
        return gym_name;
    }

    public void setGymName(String gymName) {
        this.gym_name = gymName;
    }

    public String getGymContent() {
        return gym_content;
    }

    public void setGymContent(String gyn_content) {
        this.gym_content = gyn_content;
    }


    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof NoticeData){
            return aid.equals (((NoticeData) o).aid);
        }
        return false;
    }
}
