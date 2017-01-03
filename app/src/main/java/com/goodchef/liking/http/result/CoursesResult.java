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
        @SerializedName("gym")
        private Gym mGym;

        @SerializedName("courses")
        List<CoursesData> mCoursesDataList;

        public List<CoursesData> getCoursesDataList() {
            return mCoursesDataList;
        }

        public void setCoursesDataList(List<CoursesData> coursesDataList) {
            mCoursesDataList = coursesDataList;
        }

        public Gym getGym() {
            return mGym;
        }

        public void setGym(Gym gym) {
            mGym = gym;
        }

        public static class CoursesData extends BaseData {

            @SerializedName("schedule_id")
            private String scheduleId;
            @SerializedName("course_name")
            private String courseName;
            @SerializedName("trainer_id")
            private String trainerId;
            @SerializedName("course_date")
            private String courseDate;
            @SerializedName("quota")
            private String quota;
            @SerializedName("imgs")
            private List<String> imgs;
            @SerializedName("tags")
            private List<String> tags;
            @SerializedName("tag_name")
            private String tagName;
            @SerializedName("type")
            private int type;
            @SerializedName("is_fee")
            private int isFee;
            @SerializedName("price")
            private String price;
            @SerializedName("gender")
            private String gender;
            @SerializedName("phone")
            private String phone;
            @SerializedName("desc")
            private String description;
            @SerializedName("schedule_type")
            private int scheduleType;


            public int getScheduleType() {
                return scheduleType;
            }

            public void setScheduleType(int scheduleType) {
                this.scheduleType = scheduleType;
            }

            public String getTrainerId() {
                return trainerId;
            }

            public void setTrainerId(String trainerId) {
                this.trainerId = trainerId;
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


            public int getIsFee() {
                return isFee;
            }

            public void setIsFee(int isFee) {
                this.isFee = isFee;
            }

            public String getScheduleId() {
                return scheduleId;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public void setScheduleId(String scheduleId) {
                this.scheduleId = scheduleId;
            }

            public String getTagName() {
                return tagName;
            }

            public void setTagName(String tagName) {
                this.tagName = tagName;
            }
        }

        public static class Gym extends BaseData{
            @SerializedName("name")
            private String name;
            @SerializedName("distance")
            private String distance;
            @SerializedName("gym_id")
            private String gymId;
            @SerializedName("announcement_id")
            private String announcementId;
            @SerializedName("announcement_info")
            private String announcementInfo;
            @SerializedName("city_id")
            private String cityId;
            @SerializedName("can_schedule")
            private int canSchedule = -1;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getGymId() {
                return gymId;
            }

            public void setGymId(String gymId) {
                this.gymId = gymId;
            }

            public String getAnnouncementId() {
                return announcementId;
            }

            public void setAnnouncementId(String announcementId) {
                this.announcementId = announcementId;
            }

            public String getAnnouncementInfo() {
                return announcementInfo;
            }

            public void setAnnouncementInfo(String announcementInfo) {
                this.announcementInfo = announcementInfo;
            }

            public String getCityId() {
                return cityId;
            }

            public void setCityId(String cityId) {
                this.cityId = cityId;
            }

            public int getCanSchedule() {
                return canSchedule;
            }

            public void setCanSchedule(int canSchedule) {
                this.canSchedule = canSchedule;
            }
        }
    }


}
