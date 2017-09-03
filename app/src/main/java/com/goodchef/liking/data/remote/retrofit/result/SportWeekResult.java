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
public class SportWeekResult extends LikingResult{


    /**
     * data : {"total":{"times":"1","seconds":"26","sport_date":1},"stats":[{"seconds":"0","fmt_seconds":"00'00\"","date":"20170827"},{"seconds":"0","fmt_seconds":"00'00\"","date":"20170828"},{"seconds":"0","fmt_seconds":"00'00\"","date":"20170829"},{"seconds":"0","fmt_seconds":"00'00\"","date":"20170830"},{"seconds":"0","fmt_seconds":"00'00\"","date":"20170831"},{"seconds":"26","fmt_seconds":"00'26\"","date":"20170901"},{"seconds":"0","fmt_seconds":"00'00\"","date":"20170902"}],"title":"近七天运动"}
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
        /**
         * total : {"times":"1","seconds":"26","sport_date":1}
         * stats : [{"seconds":"0","fmt_seconds":"00'00\"","date":"20170827"},{"seconds":"0","fmt_seconds":"00'00\"","date":"20170828"},{"seconds":"0","fmt_seconds":"00'00\"","date":"20170829"},{"seconds":"0","fmt_seconds":"00'00\"","date":"20170830"},{"seconds":"0","fmt_seconds":"00'00\"","date":"20170831"},{"seconds":"26","fmt_seconds":"00'26\"","date":"20170901"},{"seconds":"0","fmt_seconds":"00'00\"","date":"20170902"}]
         * title : 近七天运动
         */

        @SerializedName("total")
        private TotalBean total;
        @SerializedName("title")
        private String title;
        @SerializedName("stats")
        private List<StatsBean> stats;

        public TotalBean getTotal() {
            return total;
        }

        public void setTotal(TotalBean total) {
            this.total = total;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<StatsBean> getStats() {
            return stats;
        }

        public void setStats(List<StatsBean> stats) {
            this.stats = stats;
        }

        public static class TotalBean implements Serializable{
            /**
             * times : 1
             * seconds : 26
             * sport_date : 1
             */

            @SerializedName("times")
            private String times;
            @SerializedName("seconds")
            private String seconds;
            @SerializedName("sport_date")
            private int sportDate;

            public String getTimes() {
                return times;
            }

            public void setTimes(String times) {
                this.times = times;
            }

            public String getSeconds() {
                return seconds;
            }

            public void setSeconds(String seconds) {
                this.seconds = seconds;
            }

            public int getSportDate() {
                return sportDate;
            }

            public void setSportDate(int sportDate) {
                this.sportDate = sportDate;
            }
        }

        public static class StatsBean implements Serializable{
            /**
             * seconds : 0
             * fmt_seconds : 00'00"
             * date : 20170827
             */

            @SerializedName("seconds")
            private String seconds;
            @SerializedName("fmt_seconds")
            private String fmtSeconds;
            @SerializedName("date")
            private String date;

            public String getSeconds() {
                return seconds;
            }

            public void setSeconds(String seconds) {
                this.seconds = seconds;
            }

            public String getFmtSeconds() {
                return fmtSeconds;
            }

            public void setFmtSeconds(String fmtSeconds) {
                this.fmtSeconds = fmtSeconds;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }
    }
}
