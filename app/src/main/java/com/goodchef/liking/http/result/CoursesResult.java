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
             */

            @SerializedName("trainer_id")
            private String trainerId;
            @SerializedName("name")
            private String name;
            @SerializedName("gender")
            private String gender;
            @SerializedName("phone")
            private String phone;
            @SerializedName("description")
            private String description;
            @SerializedName("quota")
            private String quota;
            @SerializedName("type")
            private int type;
            @SerializedName("imgs")
            private List<String> imgs;
            @SerializedName("tags")
            private List<String> tags;

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

            public List<?> getImgs() {
                return imgs;
            }

            public void setImgs(List<String> imgs) {
                this.imgs = imgs;
            }

            public List<?> getTags() {
                return tags;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
            }
        }
    }
}
