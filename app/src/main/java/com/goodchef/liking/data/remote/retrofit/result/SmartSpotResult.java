package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * Created by aaa on 17/9/19.
 */

public class SmartSpotResult extends LikingResult {

    @SerializedName("directType")
    private String directType;
    @SerializedName("data")
    private DataBean data;
    @SerializedName("direct")
    private String direct;
    @SerializedName("title")
    private String title;

    public String getDirectType() {
        return directType;
    }

    public void setDirectType(String directType) {
        this.directType = directType;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getDirect() {
        return direct;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class DataBean extends Data {
        @SerializedName("record_id")
        private String record_id;

        @SerializedName("alert")
        private String alert;

        public String getRecord_id() {
            return record_id;
        }

        public void setRecord_id(String record_id) {
            this.record_id = record_id;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }
    }
}
