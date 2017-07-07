package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;

/**
 * Created on 2017/7/7
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class WaterDetailsResult extends LikingResult {


    /**
     * err_code : 0
     * err_msg : success
     * data : {"order_id":"4753713867050216627","order_status":1,"order_time":"2017-06-30 16:16:59","start_time":"2017-06-30","end_time":"2017-11-14","pay_type":1,"order_type":1,"order_amount":"1.10","gym_name":"崇明健身房15","gym_addr":"上海市崇明岛","water_time":10,"type":4}
     */
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean extends Data{
        /**
         * order_id : 4753713867050216627
         * order_status : 1
         * order_time : 2017-06-30 16:16:59
         * start_time : 2017-06-30
         * end_time : 2017-11-14
         * pay_type : 1
         * order_type : 1
         * order_amount : 1.10
         * gym_name : 崇明健身房15
         * gym_addr : 上海市崇明岛
         * water_time : 10
         * type : 4
         */

        private String order_id;
        private int order_status;
        private String order_time;
        private String start_time;
        private String end_time;
        private int pay_type;
        private int order_type;
        private String order_amount;
        private String gym_name;
        private String gym_addr;
        private int water_time;
        private int type;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public int getOrder_status() {
            return order_status;
        }

        public void setOrder_status(int order_status) {
            this.order_status = order_status;
        }

        public String getOrder_time() {
            return order_time;
        }

        public void setOrder_time(String order_time) {
            this.order_time = order_time;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }

        public int getOrder_type() {
            return order_type;
        }

        public void setOrder_type(int order_type) {
            this.order_type = order_type;
        }

        public String getOrder_amount() {
            return order_amount;
        }

        public void setOrder_amount(String order_amount) {
            this.order_amount = order_amount;
        }

        public String getGym_name() {
            return gym_name;
        }

        public void setGym_name(String gym_name) {
            this.gym_name = gym_name;
        }

        public String getGym_addr() {
            return gym_addr;
        }

        public void setGym_addr(String gym_addr) {
            this.gym_addr = gym_addr;
        }

        public int getWater_time() {
            return water_time;
        }

        public void setWater_time(int water_time) {
            this.water_time = water_time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
