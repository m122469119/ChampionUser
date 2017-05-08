package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/28 下午9:21
 */
public class MyDishesOrderDetailsResult extends LikingResult {

    @SerializedName("data")
    private OrderDetailsData mOrderDetailsData;

    public OrderDetailsData getOrderDetailsData() {
        return mOrderDetailsData;
    }

    public void setOrderDetailsData(OrderDetailsData orderDetailsData) {
        mOrderDetailsData = orderDetailsData;
    }

    public static class OrderDetailsData extends Data {


        /**
         * order_id : 4624630579397160537
         * order_status : 3
         * serial_number : 4
         * coupon_amount : 0.00
         * actual_amount : 30.01
         * pay_type : 1
         * left_second : 0
         * name : 健将
         * phone : 15618576553
         * gym_name : 凌空SOHO
         * gym_address : 金钟路968号凌空SOHO2号楼一层
         * fetch_time : 2016-07-09 14:00
         * order_time : 2016-07-09 11:26:05
         * food_list : [{"food_name":"甜萝卜有","food_num":"1","total_amount":"0.01"},{"food_name":"酸萝卜","food_num":"1","total_amount":"30"}]
         */

        @SerializedName("order_id")
        private String orderId;
        @SerializedName("order_status")
        private int orderStatus;
        @SerializedName("serial_number")
        private int serialNumber;
        @SerializedName("coupon_amount")
        private String couponAmount;
        @SerializedName("actual_amount")
        private String actualAmount;
        @SerializedName("pay_type")
        private int payType;
        @SerializedName("left_second")
        private String leftSecond;
        @SerializedName("name")
        private String name;
        @SerializedName("phone")
        private String phone;
        @SerializedName("gym_name")
        private String gymName;
        @SerializedName("gym_address")
        private String gymAddress;
        @SerializedName("fetch_time")
        private String fetchTime;
        @SerializedName("order_time")
        private String orderTime;
        /**
         * food_name : 甜萝卜有
         * food_num : 1
         * total_amount : 0.01
         */

        @SerializedName("food_list")
        private List<FoodListData> foodList;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(int serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(String couponAmount) {
            this.couponAmount = couponAmount;
        }

        public String getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(String actualAmount) {
            this.actualAmount = actualAmount;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public String getLeftSecond() {
            return leftSecond;
        }

        public void setLeftSecond(String leftSecond) {
            this.leftSecond = leftSecond;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getGymName() {
            return gymName;
        }

        public void setGymName(String gymName) {
            this.gymName = gymName;
        }

        public String getGymAddress() {
            return gymAddress;
        }

        public void setGymAddress(String gymAddress) {
            this.gymAddress = gymAddress;
        }

        public String getFetchTime() {
            return fetchTime;
        }

        public void setFetchTime(String fetchTime) {
            this.fetchTime = fetchTime;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public List<FoodListData> getFoodList() {
            return foodList;
        }

        public void setFoodList(List<FoodListData> foodList) {
            this.foodList = foodList;
        }

        public static class FoodListData extends Data {
            @SerializedName("food_name")
            private String foodName;
            @SerializedName("food_num")
            private String foodNum;
            @SerializedName("total_amount")
            private String totalAmount;

            public String getFoodName() {
                return foodName;
            }

            public void setFoodName(String foodName) {
                this.foodName = foodName;
            }

            public String getFoodNum() {
                return foodNum;
            }

            public void setFoodNum(String foodNum) {
                this.foodNum = foodNum;
            }

            public String getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(String totalAmount) {
                this.totalAmount = totalAmount;
            }
        }
    }
}
