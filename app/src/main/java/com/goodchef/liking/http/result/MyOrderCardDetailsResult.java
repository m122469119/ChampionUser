package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.goodchef.liking.http.result.data.TimeLimitData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/1 下午2:13
 */
public class MyOrderCardDetailsResult extends BaseResult {
    @SerializedName("data")
    private OrderCardDetailsData data;

    public OrderCardDetailsData getData() {
        return data;
    }

    public void setData(OrderCardDetailsData data) {
        this.data = data;
    }

    public static class OrderCardDetailsData  extends BaseData{

        @SerializedName("order_id")
        private String orderId;
        @SerializedName("order_status")
        private int orderStatus;
        @SerializedName("order_time")
        private String orderTime;
        @SerializedName("buy_type")
        private int buyType;
        @SerializedName("start_time")
        private String startTime;
        @SerializedName("end_time")
        private String endTime;
        @SerializedName("pay_type")
        private int payType;
        @SerializedName("order_amount")
        private String orderAmount;
        @SerializedName("time_limit")
        private List<TimeLimitData> timeLimit;
        @SerializedName("coupon_amount")
        private String couponAmount;
        @SerializedName("gym_name")
        private String gym_name;
        @SerializedName("gym_addr")
        private String gym_address;

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

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public int getBuyType() {
            return buyType;
        }

        public void setBuyType(int buyType) {
            this.buyType = buyType;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public List<TimeLimitData> getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(List<TimeLimitData> timeLimit) {
            this.timeLimit = timeLimit;
        }

        public String getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(String couponAmount) {
            this.couponAmount = couponAmount;
        }

        public String getGym_name() {
            return gym_name;
        }

        public void setGym_name(String gym_name) {
            this.gym_name = gym_name;
        }

        public String getGym_address() {
            return gym_address;
        }

        public void setGym_address(String gym_address) {
            this.gym_address = gym_address;
        }
    }
}
