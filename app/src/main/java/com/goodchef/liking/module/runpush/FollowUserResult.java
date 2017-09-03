package com.goodchef.liking.module.runpush;

import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.google.gson.annotations.SerializedName;

public class FollowUserResult extends LikingResult {

    /**
     * data : {}
     */

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
    }
}
