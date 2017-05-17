package com.goodchef.liking.http.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2017/3/15
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsCities extends LikingResult {


    /**
     * data : {"hasMore":1,"gymList":[{"gymName":"新尚广场健身房","distance":"42km"},{"gymName":"佘山健身房","distance":"96.8km"},{"gymName":"青浦健身房","distance":"98.9km"},{"gymName":"LikingFit凌空店","distance":"106.6km"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean extends Data {
        /**
         * hasMore : 1
         * gymList : [{"gymName":"新尚广场健身房","distance":"42km"},{"gymName":"佘山健身房","distance":"96.8km"},{"gymName":"青浦健身房","distance":"98.9km"},{"gymName":"LikingFit凌空店","distance":"106.6km"}]
         */
        @SerializedName("has_more")
        private int hasMore;
        @SerializedName("total_gym")
        private int totalGym;
        @SerializedName("gym_list")
        private List<GymListBean> gymList;

        public int getTotalGym() {
            return totalGym;
        }

        public void setTotalGym(int totalGym) {
            this.totalGym = totalGym;
        }

        public int getHasMore() {
            return hasMore;
        }

        public void setHasMore(int hasMore) {
            this.hasMore = hasMore;
        }

        public List<GymListBean> getGymList() {
            return gymList;
        }

        public void setGymList(List<GymListBean> gymList) {
            this.gymList = gymList;
        }

        public static class GymListBean extends Data {
            /**
             * gymName : 新尚广场健身房
             * distance : 42km
             */
            @SerializedName("gym_name")
            private String gymName;
            @SerializedName("distance")
            private String distance;
            @SerializedName("gym_address")
            private String gymAddress;

            public String getGymAddress() {
                return gymAddress;
            }

            public void setGymAddress(String gymAddress) {
                this.gymAddress = gymAddress;
            }

            public String getGymName() {
                return gymName;
            }

            public void setGymName(String gymName) {
                this.gymName = gymName;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }
        }
    }
}
