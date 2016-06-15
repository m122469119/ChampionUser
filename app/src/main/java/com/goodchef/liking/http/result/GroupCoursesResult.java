package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/12 下午5:45
 */
public class GroupCoursesResult extends BaseResult {

    /**
     * course_name : 美男子教你练腹肌
     * course_date : 2016-06-14 10:00-11:00
     * intensity : 3
     * course_desc :
     * course_imgs : [""]
     * gym_address : 大木桥路107号
     * gym_imgs : []
     * gym_id : 1
     * gym_annonce : 欢迎光临美男子健身房
     * trainer_name : 雷达
     * trainer_imgs : [""]
     * trainer_desc : 我是美男子
     */

    @SerializedName("data")
    private GroupLessonData mGroupLessonData;

    public GroupLessonData getGroupLessonData() {
        return mGroupLessonData;
    }

    public void setGroupLessonData(GroupLessonData groupLessonData) {
        mGroupLessonData = groupLessonData;
    }

    public static class GroupLessonData  extends BaseData{
        @SerializedName("course_name")
        private String courseName;
        @SerializedName("course_date")
        private String courseDate;
        @SerializedName("intensity")
        private String intensity;
        @SerializedName("course_desc")
        private String courseDesc;
        @SerializedName("gym_address")
        private String gymAddress;
        @SerializedName("gym_id")
        private String gymId;
        @SerializedName("gym_annonce")
        private String gymAnnonce;
        @SerializedName("trainer_name")
        private String trainerName;
        @SerializedName("trainer_desc")
        private String trainerDesc;
        @SerializedName("course_imgs")
        private List<String> courseImgs;
        @SerializedName("gym_imgs")
        private List<String> gymImgs;
        @SerializedName("trainer_imgs")
        private List<String> trainerImgs;
        @SerializedName("quota")
        private String quota;
        @SerializedName("gym_name")
        private String gymName;

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getCourseDate() {
            return courseDate;
        }

        public void setCourseDate(String courseDate) {
            this.courseDate = courseDate;
        }

        public String getIntensity() {
            return intensity;
        }

        public void setIntensity(String intensity) {
            this.intensity = intensity;
        }

        public String getCourseDesc() {
            return courseDesc;
        }

        public void setCourseDesc(String courseDesc) {
            this.courseDesc = courseDesc;
        }

        public String getGymAddress() {
            return gymAddress;
        }

        public void setGymAddress(String gymAddress) {
            this.gymAddress = gymAddress;
        }

        public String getGymId() {
            return gymId;
        }

        public void setGymId(String gymId) {
            this.gymId = gymId;
        }

        public String getGymAnnonce() {
            return gymAnnonce;
        }

        public void setGymAnnonce(String gymAnnonce) {
            this.gymAnnonce = gymAnnonce;
        }

        public String getTrainerName() {
            return trainerName;
        }

        public void setTrainerName(String trainerName) {
            this.trainerName = trainerName;
        }

        public String getTrainerDesc() {
            return trainerDesc;
        }

        public void setTrainerDesc(String trainerDesc) {
            this.trainerDesc = trainerDesc;
        }

        public List<String> getCourseImgs() {
            return courseImgs;
        }

        public void setCourseImgs(List<String> courseImgs) {
            this.courseImgs = courseImgs;
        }

        public List<String> getGymImgs() {
            return gymImgs;
        }

        public void setGymImgs(List<String> gymImgs) {
            this.gymImgs = gymImgs;
        }

        public List<String> getTrainerImgs() {
            return trainerImgs;
        }

        public void setTrainerImgs(List<String> trainerImgs) {
            this.trainerImgs = trainerImgs;
        }

        public String getQuota() {
            return quota;
        }

        public void setQuota(String quota) {
            this.quota = quota;
        }

        public String getGymName() {
            return gymName;
        }

        public void setGymName(String gymName) {
            this.gymName = gymName;
        }
    }


}
