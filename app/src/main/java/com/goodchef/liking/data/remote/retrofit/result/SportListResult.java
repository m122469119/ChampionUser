package com.goodchef.liking.data.remote.retrofit.result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @Author SanFen
 * @Email sanfenruxi1@163.com
 * @Date 2017/9/2
 * @Version 1.0
 */
public class SportListResult extends LikingResult{


    /**
     * data : {"list":[{"date":"2017-09-01 11:55:41","name":"快速启动","period":"00'26\"","type":4,"record_id":2929}]}
     */

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        @SerializedName("list")
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Serializable{
            /**
             * date : 2017-09-01 11:55:41
             * name : 快速启动
             * period : 00'26"
             * type : 4
             * record_id : 2929
             */
            public static final int TYPE_LEAGUE_LESSON = 1;
            public static final int TYPE_PRIVATE_LESSON = 2;
            public static final int TYPE_TREADMILL = 3;
            public static final int TYPE_SMARTSPOT = 4;


            @SerializedName("date")
            private String date;
            @SerializedName("name")
            private String name;
            @SerializedName("period")
            private String period;
            @SerializedName("type")
            private int type;
            @SerializedName("record_id")
            private int recordId;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPeriod() {
                return period;
            }

            public void setPeriod(String period) {
                this.period = period;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getRecordId() {
                return recordId;
            }

            public void setRecordId(int recordId) {
                this.recordId = recordId;
            }
        }
    }
}
