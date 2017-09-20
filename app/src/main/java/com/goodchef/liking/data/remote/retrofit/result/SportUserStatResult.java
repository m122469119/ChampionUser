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
        private int totalTime;
        @SerializedName("total_day")
        private int totalDay;
        @SerializedName("total_cal")
        private int totalCal;
        @SerializedName("total_seconds")
        private int totalSeconds;
        @SerializedName("run_distance")
        private int runDistance;
        @SerializedName("run_time")
        private int runTime;
        @SerializedName("smartspot_exercise")
        private int smartspotExercise;
        @SerializedName("smartspot_time")
        private int smartspotTime;
        @SerializedName("course")
        private int course;
        @SerializedName("course_time")
        private int courseTime;
        @SerializedName("personal")
        private int personal;
        @SerializedName("personal_time")
        private int personalTime;

        public int getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(int totalTime) {
            this.totalTime = totalTime;
        }

        public int getTotalDay() {
            return totalDay;
        }

        public void setTotalDay(int totalDay) {
            this.totalDay = totalDay;
        }

        public int getTotalCal() {
            return totalCal;
        }

        public void setTotalCal(int totalCal) {
            this.totalCal = totalCal;
        }

        public int getTotalSeconds() {
            return totalSeconds;
        }

        public void setTotalSeconds(int totalSeconds) {
            this.totalSeconds = totalSeconds;
        }

        public int getRunDistance() {
            return runDistance;
        }

        public void setRunDistance(int runDistance) {
            this.runDistance = runDistance;
        }

        public int getRunTime() {
            return runTime;
        }

        public void setRunTime(int runTime) {
            this.runTime = runTime;
        }

        public int getSmartspotExercise() {
            return smartspotExercise;
        }

        public void setSmartspotExercise(int smartspotExercise) {
            this.smartspotExercise = smartspotExercise;
        }

        public int getSmartspotTime() {
            return smartspotTime;
        }

        public void setSmartspotTime(int smartspotTime) {
            this.smartspotTime = smartspotTime;
        }

        public int getCourse() {
            return course;
        }

        public void setCourse(int course) {
            this.course = course;
        }

        public int getCourseTime() {
            return courseTime;
        }

        public void setCourseTime(int courseTime) {
            this.courseTime = courseTime;
        }

        public int getPersonal() {
            return personal;
        }

        public void setPersonal(int personal) {
            this.personal = personal;
        }

        public int getPersonalTime() {
            return personalTime;
        }

        public void setPersonalTime(int personalTime) {
            this.personalTime = personalTime;
        }
    }
}
