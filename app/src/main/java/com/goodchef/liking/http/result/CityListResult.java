package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/20.
 */

public class CityListResult extends LikingResult {

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean extends Data {
        @SerializedName("open_city")
        private List<String> open_city;

        public List<String> getOpen_city() {
            return open_city;
        }

        public void setOpen_city(List<String> open_city) {
            this.open_city = open_city;
        }
    }
}
