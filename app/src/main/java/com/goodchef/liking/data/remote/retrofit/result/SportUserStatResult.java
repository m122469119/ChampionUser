package com.goodchef.liking.data.remote.retrofit.result;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2017/09/20
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class SportUserStatResult extends LikingResult {

    /**
     * data : {"total_time":12,"total_day":11,"total_cal":2300,"total_seconds":1234,"run_distance":469,"run_time":469,"smartspot_exercise":469,"smartspot_time":469,"course":469,"course_time":469,"personal":469,"personal_time":469}
     */

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * total_time : 12
         * total_day : 11
         * total_cal : 2300
         * total_seconds : 1234
         * run_distance : 469
         * run_time : 469
         * smartspot_exercise : 469
         * smartspot_time : 469
         * course : 469
         * course_time : 469
         * personal : 469
         * personal_time : 469
         */

        @SerializedName("total_time")
        private String totalTime;
        @SerializedName("total_day")
        private String totalDay;
        @SerializedName("total_cal")
        private String totalCal;
        @SerializedName("total_seconds")
        private String totalSeconds;
        @SerializedName("run_distance")
        private String runDistance;
        @SerializedName("run_time")
        private String runTime;
        @SerializedName("smartspot_exercise")
        private String smartspotExercise;
        @SerializedName("smartspot_time")
        private String smartspotTime;
        @SerializedName("course")
        private String course;
        @SerializedName("course_time")
        private String courseTime;
        @SerializedName("personal")
        private String personal;
        @SerializedName("personal_time")
        private String personalTime;

        public String getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(String totalTime) {
            this.totalTime = totalTime;
        }

        public String getTotalDay() {
            return totalDay;
        }

        public void setTotalDay(String totalDay) {
            this.totalDay = totalDay;
        }

        public String getTotalCal() {
            return totalCal;
        }

        public void setTotalCal(String totalCal) {
            this.totalCal = totalCal;
        }

        public String getTotalSeconds() {
            return totalSeconds;
        }

        public void setTotalSeconds(String totalSeconds) {
            this.totalSeconds = totalSeconds;
        }

        public String getRunDistance() {
            return runDistance;
        }

        public void setRunDistance(String runDistance) {
            this.runDistance = runDistance;
        }

        public String getRunTime() {
            return runTime;
        }

        public void setRunTime(String runTime) {
            this.runTime = runTime;
        }

        public String getSmartspotExercise() {
            return smartspotExercise;
        }

        public void setSmartspotExercise(String smartspotExercise) {
            this.smartspotExercise = smartspotExercise;
        }

        public String getSmartspotTime() {
            return smartspotTime;
        }

        public void setSmartspotTime(String smartspotTime) {
            this.smartspotTime = smartspotTime;
        }

        public String getCourse() {
            return course;
        }

        public void setCourse(String course) {
            this.course = course;
        }

        public String getCourseTime() {
            return courseTime;
        }

        public void setCourseTime(String courseTime) {
            this.courseTime = courseTime;
        }

        public String getPersonal() {
            return personal;
        }

        public void setPersonal(String personal) {
            this.personal = personal;
        }

        public String getPersonalTime() {
            return personalTime;
        }

        public void setPersonalTime(String personalTime) {
            this.personalTime = personalTime;
        }
    }
}
