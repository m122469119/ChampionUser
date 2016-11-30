package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/18 上午11:21
 */
public class MyGroupCoursesResult extends BaseResult {
    @SerializedName("data")
    private MyGroupCoursesData data;

    public MyGroupCoursesData getData() {
        return data;
    }

    public void setData(MyGroupCoursesData data) {
        this.data = data;
    }

    public static class  MyGroupCoursesData {
        @SerializedName("list")
        private List<MyGroupCourses> mMyGroupCourses;
        @SerializedName("has_more")
        private int has_more;

        public List<MyGroupCourses> getMyGroupCourses() {
            return mMyGroupCourses;
        }

        public void setMyGroupCourses(List<MyGroupCourses> myGroupCourses) {
            mMyGroupCourses = myGroupCourses;
        }

        public int getHas_more() {
            return has_more;
        }

        public void setHas_more(int has_more) {
            this.has_more = has_more;
        }

        public static class MyGroupCourses extends BaseData{
            @SerializedName("order_id")
            private String orderId;
            @SerializedName("schedule_id")
            private String scheduleId;
            @SerializedName("course_name")
            private String courseName;
            @SerializedName("status")
            private int status;
            @SerializedName("start_date")
            private String startDate;
            @SerializedName("start_time")
            private String startTime;
            @SerializedName("end_time")
            private String endTime;
            @SerializedName("week_day")
            private String weekDay;
            @SerializedName("place_info")
            private String placeInfo;
            @SerializedName("gym_name")
            private String gymName;
            @SerializedName("gym_address")
            private String gymAddress;
            @SerializedName("course_url")
            private String courseUrl;
            @SerializedName("is_fee")
            private int isFee;
            @SerializedName("schedule_type")
            private int scheduleType;
            @SerializedName("amount")
            private String amount;
            @SerializedName("cancel_btn_show")
            private int cancelBtnShow;

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getScheduleId() {
                return scheduleId;
            }

            public void setScheduleId(String scheduleId) {
                this.scheduleId = scheduleId;
            }

            public String getCourseName() {
                return courseName;
            }

            public void setCourseName(String courseName) {
                this.courseName = courseName;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
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

            public String getWeekDay() {
                return weekDay;
            }

            public void setWeekDay(String weekDay) {
                this.weekDay = weekDay;
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

            public String getCourseUrl() {
                return courseUrl;
            }

            public void setCourseUrl(String courseUrl) {
                this.courseUrl = courseUrl;
            }

            public int getIsFee() {
                return isFee;
            }

            public void setIsFee(int isFee) {
                this.isFee = isFee;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public int getCancelBtnShow() {
                return cancelBtnShow;
            }

            public void setCancelBtnShow(int cancelBtnShow) {
                this.cancelBtnShow = cancelBtnShow;
            }

            public int getScheduleType() {
                return scheduleType;
            }

            public void setScheduleType(int scheduleType) {
                this.scheduleType = scheduleType;
            }


            public String getPlaceInfo() {
                return placeInfo;
            }

            public void setPlaceInfo(String placeInfo) {
                this.placeInfo = placeInfo;
            }

        }
    }


}
