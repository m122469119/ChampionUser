package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/8 下午3:11
 */
public class CoursesResult extends BaseResult {
    @SerializedName("data")
    private Courses mCourses;

    public Courses getCourses() {
        return mCourses;
    }

    public void setCourses(Courses courses) {
        mCourses = courses;
    }

    public static class Courses {
        @SerializedName("courses")
        List<CoursesData> mCoursesDataList;

        public List<CoursesData> getCoursesDataList() {
            return mCoursesDataList;
        }

        public void setCoursesDataList(List<CoursesData> coursesDataList) {
            mCoursesDataList = coursesDataList;
        }

        public static class CoursesData extends BaseData {


            /**
             * trainer_id : 10000
             * name : 雷达
             * gender : 0
             * phone : 13222222222
             * description : 我是美男子
             * quota : 100
             * imgs : []
             * tags : []
             * type : 2
             * course_name :
             * course_id :
             * room_id :
             * gym_id :
             * calorie :
             * end_time :
             * start_time :
             */

            @SerializedName("trainer_id")
            private String trainerId;
            @SerializedName("name")
            private String name;
            @SerializedName("gender")
            private String gender;
            @SerializedName("phone")
            private String phone;
            @SerializedName("desc")
            private String description;
            @SerializedName("quota")
            private String quota;
            @SerializedName("type")
            private int type;
            @SerializedName("course_name")
            private String courseName;
            @SerializedName("course_id")
            private String courseId;
            @SerializedName("room_id")
            private String roomId;
            @SerializedName("gym_id")
            private String gymId;
            @SerializedName("calorie")
            private String calorie;
            @SerializedName("end_time")
            private String endTime;
            @SerializedName("start_time")
            private String startTime;
            @SerializedName("imgs")
            private List<String> imgs;
            @SerializedName("tags")
            private List<String> tags;
            @SerializedName("course_date")
            private String courseDate;
            @SerializedName("gym_address")
            private String gymAddress;
            @SerializedName("distance")
            private String distance;
            @SerializedName("schedule_id")
            private String scheduleId;

            public String getTrainerId() {
                return trainerId;
            }

            public void setTrainerId(String trainerId) {
                this.trainerId = trainerId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
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

            public String getCourseName() {
                return courseName;
            }

            public void setCourseName(String courseName) {
                this.courseName = courseName;
            }

            public String getCourseId() {
                return courseId;
            }

            public void setCourseId(String courseId) {
                this.courseId = courseId;
            }

            public String getRoomId() {
                return roomId;
            }

            public void setRoomId(String roomId) {
                this.roomId = roomId;
            }

            public String getGymId() {
                return gymId;
            }

            public void setGymId(String gymId) {
                this.gymId = gymId;
            }

            public String getCalorie() {
                return calorie;
            }

            public void setCalorie(String calorie) {
                this.calorie = calorie;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
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

            public String getCourseDate() {
                return courseDate;
            }

            public void setCourseDate(String courseDate) {
                this.courseDate = courseDate;
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

            public String getScheduleId() {
                return scheduleId;
            }

            public void setScheduleId(String scheduleId) {
                this.scheduleId = scheduleId;
            }
        }
    }
}
