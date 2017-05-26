package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/20 下午7:13
 */
public class MyPrivateCoursesDetailsResult extends LikingResult {

    @SerializedName("data")
    private MyPrivateCoursesDetailsData data;

    public MyPrivateCoursesDetailsData getData() {
        return data;
    }

    public void setData(MyPrivateCoursesDetailsData data) {
        this.data = data;
    }

    public static class MyPrivateCoursesDetailsData extends Data {
        @SerializedName("status")
        private int status;
        @SerializedName("trainer_name")
        private String trainerName;
        @SerializedName("trainer_avatar")
        private String trainerAvatar;
        @SerializedName("start_time")
        private String startTime;
        @SerializedName("end_time")
        private String endTime;
        @SerializedName("course_name")
        private String courseName;
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("order_time")
        private String orderTime;
        @SerializedName("people_num")
        private int peopleNum;
        @SerializedName("total_amount")
        private String totalAmount;
        @SerializedName("coupon_amount")
        private String couponAmount;
        @SerializedName("actual_amount")
        private String actualAmount;
        @SerializedName("pay_type")
        private int payType;
        @SerializedName("trainer_phone")
        private String trainerPhone;
        @SerializedName("complete_times")
        private String completeTimes;
        @SerializedName("prompt")
        private String prompt;
        @SerializedName("left_times")
        private String left_times;
        @SerializedName("miss_times")
        private String miss_times;
        @SerializedName("destroy_times")
        private String destroy_times;
        @SerializedName("duration")
        private String duration;
        @SerializedName("buy_times")
        private String buyTimes;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTrainerName() {
            return trainerName;
        }

        public void setTrainerName(String trainerName) {
            this.trainerName = trainerName;
        }

        public String getTrainerAvatar() {
            return trainerAvatar;
        }

        public void setTrainerAvatar(String trainerAvatar) {
            this.trainerAvatar = trainerAvatar;
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

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public int getPeopleNum() {
            return peopleNum;
        }

        public void setPeopleNum(int peopleNum) {
            this.peopleNum = peopleNum;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
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

        public String getTrainerPhone() {
            return trainerPhone;
        }

        public void setTrainerPhone(String trainerPhone) {
            this.trainerPhone = trainerPhone;
        }

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }

        public String getLeft_times() {
            return left_times;
        }

        public void setLeft_times(String left_times) {
            this.left_times = left_times;
        }

        public String getMiss_times() {
            return miss_times;
        }

        public void setMiss_times(String miss_times) {
            this.miss_times = miss_times;
        }

        public String getDestroy_times() {
            return destroy_times;
        }

        public void setDestroy_times(String destroy_times) {
            this.destroy_times = destroy_times;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getCompleteTimes() {
            return completeTimes;
        }

        public void setCompleteTimes(String completeTimes) {
            this.completeTimes = completeTimes;
        }

        public String getBuyTimes() {
            return buyTimes;
        }

        public void setBuyTimes(String buyTimes) {
            this.buyTimes = buyTimes;
        }
    }
}
