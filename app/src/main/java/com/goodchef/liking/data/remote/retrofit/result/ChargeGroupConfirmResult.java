package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/25 上午10:01
 */
public class ChargeGroupConfirmResult extends LikingResult {

    @SerializedName("data")
    private ChargeGroupConfirmData data;

    public ChargeGroupConfirmData getData() {
        return data;
    }

    public void setData(ChargeGroupConfirmData data) {
        this.data = data;
    }

    public static class ChargeGroupConfirmData extends Data {

        /**
         * prompt : 课程购买后,课程开始前4小时不能取消,请谨慎购买
         * course_name : 综合热身
         * trainer_name : 张兴会
         * duration : 90min
         * intensity : 1
         * course_time : 2016-08-24 (周三)16:30-18:00
         * course_address : Liking Fit（凌空店）-上海市长宁区金钟路968号2号楼107
         * amount : 10.00
         */

        @SerializedName("prompt")
        private String prompt;
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
        @SerializedName("amount")
        private String amount;

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
