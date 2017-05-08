package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/22 下午3:49
 */
public class MyChargeGroupCoursesDetailsResult extends LikingResult {
    @SerializedName("data")
    private MyChargeGroupCoursesDetails data;

    public MyChargeGroupCoursesDetails getData() {
        return data;
    }

    public void setData(MyChargeGroupCoursesDetails data) {
        this.data = data;
    }

    public static class MyChargeGroupCoursesDetails extends Data {
        /**
         * order_id : 4639552308779182689
         * course_name : 跑步机HIIT变速跑
         * trainer_name : 倪晴
         * duration : 90min
         * intensity : 3
         * course_time : 2016-08-21 (周日)19:00-20:30
         * course_address : Liking Fit（复兴店）-黄浦区马当路388号soho复兴广场B2-02
         * order_time : 2016-08-19 15:39:43
         * total_amount : 30.00
         * coupon_amount : 0.00
         * actual_amount : 30.00
         * pay_type : 1
         * status : 0
         */

        @SerializedName("order_id")
        private String orderId;
        @SerializedName("course_name")
        private String courseName;
        @SerializedName("trainer_name")
        private String trainerName;
        @SerializedName("duration")
        private String duration;
        @SerializedName("intensity")
        private int intensity;
        @SerializedName("course_time")
        private String courseTime;
        @SerializedName("course_address")
        private String courseAddress;
        @SerializedName("order_time")
        private String orderTime;
        @SerializedName("total_amount")
        private String totalAmount;
        @SerializedName("coupon_amount")
        private String couponAmount;
        @SerializedName("actual_amount")
        private String actualAmount;
        @SerializedName("pay_type")
        private int payType;
        @SerializedName("status")
        private int status;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getTrainerName() {
            return trainerName;
        }

        public void setTrainerName(String trainerName) {
            this.trainerName = trainerName;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public int getIntensity() {
            return intensity;
        }

        public void setIntensity(int intensity) {
            this.intensity = intensity;
        }

        public String getCourseTime() {
            return courseTime;
        }

        public void setCourseTime(String courseTime) {
            this.courseTime = courseTime;
        }

        public String getCourseAddress() {
            return courseAddress;
        }

        public void setCourseAddress(String courseAddress) {
            this.courseAddress = courseAddress;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
