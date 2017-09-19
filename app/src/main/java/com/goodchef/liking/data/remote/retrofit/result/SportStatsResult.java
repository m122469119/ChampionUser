package com.goodchef.liking.data.remote.retrofit.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2017/09/19
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class SportStatsResult extends LikingResult {


    /**
     * data : {"stats":[{"seconds":"0","start_date":"20170907","end_date":"20170907"},{"seconds":"12600","start_date":"20170907","end_date":"20170907"},{"seconds":"0","start_date":"20170907","end_date":"20170907"},{"seconds":"0","start_date":"20170907","end_date":"20170907"},{"seconds":"21643","start_date":"20170907","end_date":"20170907"},{"seconds":"158","start_date":"20170907","end_date":"20170907"},{"seconds":"3600","start_date":"20170907","end_date":"20170907"}]}
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
        @SerializedName("stats")
        private List<StatsBean> stats;

        public List<StatsBean> getStats() {
            return stats;
        }

        public void setStats(List<StatsBean> stats) {
            this.stats = stats;
        }

        public static class StatsBean {
            /**
             * seconds : 0
             * start_date : 20170907
             * end_date : 20170907
             */

            @SerializedName("seconds")
            private String seconds;
            @SerializedName("start_date")
            private String startDate;
            @SerializedName("end_date")
            private String endDate;

            public String getSeconds() {
                return seconds;
            }

            public void setSeconds(String seconds) {
                this.seconds = seconds;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }
        }
    }
}
