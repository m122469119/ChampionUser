package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/28 下午3:11
 */
public class DishesOrderListResult extends LikingResult {
    @SerializedName("data")
    private DishesOrderData data;

    public DishesOrderData getData() {
        return data;
    }

    public void setData(DishesOrderData data) {
        this.data = data;
    }

    public static class DishesOrderData extends Data {
        @SerializedName("list")
        private List<DishesOrder> mOrderList;
        @SerializedName("has_more")
        private int hasMore;

        public int getHasMore() {
            return hasMore;
        }

        public void setHasMore(int hasMore) {
            this.hasMore = hasMore;
        }

        public List<DishesOrder> getOrderList() {
            return mOrderList;
        }

        public void setOrderList(List<DishesOrder> orderList) {
            mOrderList = orderList;
        }

        public static class DishesOrder extends Data {

            @SerializedName("order_id")
            private String orderId;
            @SerializedName("serial_number")
            private String serialNumber;
            @SerializedName("order_status")
            private int orderStatus;
            @SerializedName("gym_name")
            private String gymName;
            @SerializedName("gym_address")
            private String gymAddress;
            @SerializedName("gym_img")
            private String gymImg;
            @SerializedName("fetch_time")
            private String fetchTime;
            @SerializedName("order_amount")
            private String orderAmount;
            @SerializedName("left_second")
            private String leftSecond;
            @SerializedName("order_time")
            private String orderTime;

            private long orderSurplusTime;//订单过期时间

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getSerialNumber() {
                return serialNumber;
            }

            public void setSerialNumber(String serialNumber) {
                this.serialNumber = serialNumber;
            }

            public int getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(int orderStatus) {
                this.orderStatus = orderStatus;
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

            public String getGymImg() {
                return gymImg;
            }

            public void setGymImg(String gymImg) {
                this.gymImg = gymImg;
            }

            public String getFetchTime() {
                return fetchTime;
            }

            public void setFetchTime(String fetchTime) {
                this.fetchTime = fetchTime;
            }

            public String getOrderAmount() {
                return orderAmount;
            }

            public void setOrderAmount(String orderAmount) {
                this.orderAmount = orderAmount;
            }

            public String getLeftSecond() {
                return leftSecond;
            }

            public void setLeftSecond(String leftSecond) {
                this.leftSecond = leftSecond;
            }

            public String getOrderTime() {
                return orderTime;
            }

            public void setOrderTime(String orderTime) {
                this.orderTime = orderTime;
            }

            public long getOrderSurplusTime() {
                return orderSurplusTime;
            }

            public void setOrderSurplusTime(long orderSurplusTime) {
                this.orderSurplusTime = orderSurplusTime;
            }
        }
    }


}
