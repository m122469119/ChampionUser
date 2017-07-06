package com.goodchef.liking.data.remote.retrofit.result.data;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/1 上午10:06
 */
public class OrderCardData extends Data {

    @SerializedName("order_id")
    private String orderId;
    @SerializedName("order_status")
    private int orderStatus;
    @SerializedName("card_img")
    private String cardImg;
    @SerializedName("buy_type")
    private String buyType;
    @SerializedName("order_amount")
    private String orderAmount;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;

    @SerializedName("type")
    private String type;
    @SerializedName("water_start_time")
    private String waterStartTime;
    @SerializedName("water_end_time")
    private String waterEndTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWaterStartTime() {
        return waterStartTime;
    }

    public void setWaterStartTime(String waterStartTime) {
        this.waterStartTime = waterStartTime;
    }

    public String getWaterEndTime() {
        return waterEndTime;
    }

    public void setWaterEndTime(String waterEndTime) {
        this.waterEndTime = waterEndTime;
    }

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

    public String getCardImg() {
        return cardImg;
    }

    public void setCardImg(String cardImg) {
        this.cardImg = cardImg;
    }

    public String getBuyType() {
        return buyType;
    }

    public void setBuyType(String buyType) {
        this.buyType = buyType;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
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
}
