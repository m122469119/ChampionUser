package com.goodchef.liking.http.result;

import com.aaron.http.code.result.Data;

import java.util.List;

/**
 * Created on 2017/3/15
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsCities extends LikingResult {


    /**
     * data : {"has_more":1,"gym_list":[{"gym_name":"新尚广场健身房","distance":"42km"},{"gym_name":"佘山健身房","distance":"96.8km"},{"gym_name":"青浦健身房","distance":"98.9km"},{"gym_name":"LikingFit凌空店","distance":"106.6km"}]}
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
         * has_more : 1
         * gym_list : [{"gym_name":"新尚广场健身房","distance":"42km"},{"gym_name":"佘山健身房","distance":"96.8km"},{"gym_name":"青浦健身房","distance":"98.9km"},{"gym_name":"LikingFit凌空店","distance":"106.6km"}]
         */

        private int has_more;
        private int total_gym;
        private List<GymListBean> gym_list;

        public int getTotal_gym() {
            return total_gym;
        }

        public void setTotal_gym(int total_gym) {
            this.total_gym = total_gym;
        }

        public int getHas_more() {
            return has_more;
        }

        public void setHas_more(int has_more) {
            this.has_more = has_more;
        }

        public List<GymListBean> getGym_list() {
            return gym_list;
        }

        public void setGym_list(List<GymListBean> gym_list) {
            this.gym_list = gym_list;
        }

        public static class GymListBean extends Data {
            /**
             * gym_name : 新尚广场健身房
             * distance : 42km
             */

            private String gym_name;
            private String distance;
            private String gym_address;

            public String getGym_address() {
                return gym_address;
            }

            public void setGym_address(String gym_address) {
                this.gym_address = gym_address;
            }

            public String getGym_name() {
                return gym_name;
            }

            public void setGym_name(String gym_name) {
                this.gym_name = gym_name;
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
