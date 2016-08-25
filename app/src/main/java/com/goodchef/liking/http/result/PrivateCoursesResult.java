package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.goodchef.liking.http.result.data.GymData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/13 上午10:47
 */
public class PrivateCoursesResult extends BaseResult {

    @SerializedName("data")
    private PrivateCoursesData mPrivateCoursesData;

    public PrivateCoursesData getPrivateCoursesData() {
        return mPrivateCoursesData;
    }

    public void setPrivateCoursesData(PrivateCoursesData privateCoursesData) {
        mPrivateCoursesData = privateCoursesData;
    }

    public static class PrivateCoursesData extends BaseData {


        /**
         * trainer_name : 雷达
         * desc : 我是美男子
         * tags : []
         * trainer_imgs : [""]
         * gender : 0
         * height : 165
         * weight : 48
         */

        @SerializedName("trainer_name")
        private String trainerName;
        @SerializedName("desc")
        private String desc;
        @SerializedName("gender")
        private int gender;
        @SerializedName("height")
        private String height;
        @SerializedName("weight")
        private String weight;
        @SerializedName("tags")
        private List<String> tags;
        @SerializedName("trainer_imgs")
        private List<String> trainerImgs;
         @SerializedName("gyms")
        private List<GymData> mGymDataList;
        @SerializedName("courses")
        private List<CoursesData> mCoursesDataList;
        @SerializedName("card_rule")
        private String cardRule;

        public String getTrainerName() {
            return trainerName;
        }

        public void setTrainerName(String trainerName) {
            this.trainerName = trainerName;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public List<String> getTrainerImgs() {
            return trainerImgs;
        }

        public void setTrainerImgs(List<String> trainerImgs) {
            this.trainerImgs = trainerImgs;
        }

        public List<GymData> getGymDataList() {
            return mGymDataList;
        }

        public void setGymDataList(List<GymData> gymDataList) {
            mGymDataList = gymDataList;
        }

        public List<CoursesData> getCoursesDataList() {
            return mCoursesDataList;
        }

        public void setCoursesDataList(List<CoursesData> coursesDataList) {
            mCoursesDataList = coursesDataList;
        }

        public String getCardRule() {
            return cardRule;
        }

        public void setCardRule(String cardRule) {
            this.cardRule = cardRule;
        }

        public static class CoursesData extends BaseData{

            /**
             * name : 综合训练-60min
             * course_id : 10001001
             */

            @SerializedName("name")
            private String name;
            @SerializedName("course_id")
            private String courseId;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCourseId() {
                return courseId;
            }

            public void setCourseId(String courseId) {
                this.courseId = courseId;
            }
        }

    }


}
