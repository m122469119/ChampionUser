package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;

/**
 * Created on 2017/7/4
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class DelBodyRecordResult extends LikingResult {
    /**
     * data : {}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean extends Data{
    }
}
