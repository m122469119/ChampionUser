package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;

import java.util.List;

/**
 * Created on 2017/3/17
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsDetailsResult extends LikingResult {


    /**
     * data : {"title":"liking coupon","amount":"300.00","valid_date":"2016.11.18-2017.05.17","condition_desc":"满10可用","good_desc":"适用于部分会员卡的购买,续卡","detail_desc":"","show_all":1,"gym_list":[{"gym_name":"新尚广场健身房","distance":"42km"},{"gym_name":"佘山健身房","distance":"96.8km"},{"gym_name":"青浦健身房","distance":"98.9km"}]}
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
         * title : liking coupon
         * amount : 300.00
         * valid_date : 2016.11.18-2017.05.17
         * condition_desc : 满10可用
         * good_desc : 适用于部分会员卡的购买,续卡
         * detail_desc :
         * show_all : 1
         * gym_list : [{"gym_name":"新尚广场健身房","distance":"42km"},{"gym_name":"佘山健身房","distance":"96.8km"},{"gym_name":"青浦健身房","distance":"98.9km"}]
         */

        private String title;
        private String amount;
        private String valid_date;
        private String condition_desc;
        private int show_all;
        private List<GymListBean> gym_list;
        private List<DescListBean> desc_list;

        public List<DescListBean> getDesc_list() {
            return desc_list;
        }

        public void setDesc_list(List<DescListBean> desc_list) {
            this.desc_list = desc_list;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getValid_date() {
            return valid_date;
        }

        public void setValid_date(String valid_date) {
            this.valid_date = valid_date;
        }

        public String getCondition_desc() {
            return condition_desc;
        }

        public void setCondition_desc(String condition_desc) {
            this.condition_desc = condition_desc;
        }

        public int getShow_all() {
            return show_all;
        }

        public void setShow_all(int show_all) {
            this.show_all = show_all;
        }

        public List<GymListBean> getGym_list() {
            return gym_list;
        }

        public void setGym_list(List<GymListBean> gym_list) {
            this.gym_list = gym_list;
        }


        public static class DescListBean extends Data {
            /**
             * title : 有效期
             * name : 2016.11.18-2017.05.17
             */

            private String title;
            private String name;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }


        public static class GymListBean extends Data {
            /**
             * gym_name : 新尚广场健身房
             * distance : 42km
             */

            private String gym_name;
            private String distance;

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
