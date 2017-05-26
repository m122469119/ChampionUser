package com.goodchef.liking.data.remote.retrofit.result.data;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/29 下午4:25
 */
public class TimeLimitData extends Data {
    @SerializedName("title")
    private String title;
    @SerializedName("desc")
    private String desc;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
