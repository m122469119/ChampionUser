package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/3 下午7:55
 */
public class GymCoursesResult extends BaseResult {

    @SerializedName("data")
    private GymCoursesData data;

    public GymCoursesData getData() {
        return data;
    }

    public void setData(GymCoursesData data) {
        this.data = data;
    }

    public static class GymCoursesData extends BaseData {
        @SerializedName("show_days")
        private List<GymDate> showDayList;
        @SerializedName("courses")
        private List<Courses> coursesList;

        public List<GymDate> getShowDayList() {
            return showDayList;
        }

        public void setShowDayList(List<GymDate> showDayList) {
            this.showDayList = showDayList;
        }

        public List<Courses> getCoursesList() {
            return coursesList;
        }

        public void setCoursesList(List<Courses> coursesList) {
            this.coursesList = coursesList;
        }

        public static class Courses extends BaseData {
            @SerializedName("schedule_id")
            private String scheduleId;
            @SerializedName("course_name")
            private String courseName;
            @SerializedName("trainer_id")
            private String trainerId;
            @SerializedName("course_date")
            private String courseDate;
            @SerializedName("address")
            private String address;
            @SerializedName("calorie")
            private String calorie;
            @SerializedName("quota")
            private String quota;
            @SerializedName("type")
            private int type;
            @SerializedName("gym_address")
            private String gymAddress;
            @SerializedName("distance")
            private String distance;
            @SerializedName("phone")
            private String phone;
            @SerializedName("desc")
            private String desc;
            @SerializedName("gender")
            private String gender;
            @SerializedName("name")
            private String name;
            @SerializedName("imgs")
            private List<String> imgs;
            @SerializedName("tags")
            private List<String> tags;

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

            public String getTrainerId() {
                return trainerId;
            }

            public void setTrainerId(String trainerId) {
                this.trainerId = trainerId;
            }

            public String getCourseDate() {
                return courseDate;
            }

            public void setCourseDate(String courseDate) {
                this.courseDate = courseDate;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getCalorie() {
                return calorie;
            }

            public void setCalorie(String calorie) {
                this.calorie = calorie;
            }

            public String getQuota() {
                return quota;
            }

            public void setQuota(String quota) {
                this.quota = quota;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getGymAddress() {
                return gymAddress;
            }

            public void setGymAddress(String gymAddress) {
                this.gymAddress = gymAddress;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getImgs() {
                return imgs;
            }

            public void setImgs(List<String> imgs) {
                this.imgs = imgs;
            }

            public List<String> getTags() {
                return tags;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
            }
        }

        public static class GymDate extends BaseData {

            @SerializedName("date")
            private String date;
            @SerializedName("show")
            private String show;
            @SerializedName("format")
            private String format;

            private int index;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getShow() {
                return show;
            }

            public void setShow(String show) {
                this.show = show;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public String getFormat() {
                return format;
            }

            public void setFormat(String format) {
                this.format = format;
            }
        }

    }

}
