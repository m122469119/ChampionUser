package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午2:34
 */

public class SelfGroupCoursesListResult extends LikingResult {


    @SerializedName("data")
    private SelfGroupCoursesData mData;

    public SelfGroupCoursesData getData() {
        return mData;
    }

    public void setData(SelfGroupCoursesData data) {
        mData = data;
    }

    public static class SelfGroupCoursesData extends Data{
        /**
         * course_id : 1851033696
         * category : 综合训练
         * name : ABS腰腹燃脂
         * tags : []
         * img : [{"url":"img/2016/07/22/1469158022_57919286d0c4a.jpg"}]
         * equipment : []
         * intensity : 4
         * calorie_per_hour : 300
         * default_price : 0.00
         * desc : ABS腰腹燃脂是一个全面的腹肌训练模式，刺激全面，强度足够高。
         * status : 1
         */

        @SerializedName("list")
        private List<CoursesData> mList;

        public List<CoursesData> getList() {
            return mList;
        }

        public void setList(List<CoursesData> list) {
            mList = list;
        }

        public static class CoursesData extends Data {
            @SerializedName("course_id")
            private String mCourseId;
            @SerializedName("category")
            private String mCategory;
            @SerializedName("name")
            private String mName;
            @SerializedName("intensity")
            private String mIntensity;
            @SerializedName("calorie_per_hour")
            private String mCaloriePerHour;
            @SerializedName("default_price")
            private String mDefaultPrice;
            @SerializedName("desc")
            private String mDesc;
            @SerializedName("status")
            private String mStatus;
            @SerializedName("tags")
            private List<String> mTags;
            /**
             * url : img/2016/07/22/1469158022_57919286d0c4a.jpg
             */

            @SerializedName("img")
            private List<ImgData> mImg;
            @SerializedName("equipment")
            private List<String> mEquipment;
            @SerializedName("video_duration")
            private String mVideoDuration;

            private boolean isSelect ;

            public String getCategory() {
                return mCategory;
            }

            public void setCategory(String category) {
                mCategory = category;
            }

            public String getName() {
                return mName;
            }

            public void setName(String name) {
                mName = name;
            }

            public String getCourseId() {
                return mCourseId;
            }

            public void setCourseId(String courseId) {
                mCourseId = courseId;
            }

            public String getIntensity() {
                return mIntensity;
            }

            public void setIntensity(String intensity) {
                mIntensity = intensity;
            }

            public String getCaloriePerHour() {
                return mCaloriePerHour;
            }

            public void setCaloriePerHour(String caloriePerHour) {
                mCaloriePerHour = caloriePerHour;
            }

            public String getStatus() {
                return mStatus;
            }

            public void setStatus(String status) {
                mStatus = status;
            }

            public List<String> getTags() {
                return mTags;
            }

            public String getDefaultPrice() {
                return mDefaultPrice;
            }

            public void setDefaultPrice(String defaultPrice) {
                mDefaultPrice = defaultPrice;
            }

            public String getDesc() {
                return mDesc;
            }

            public void setDesc(String desc) {
                mDesc = desc;
            }


            public List<ImgData> getImg() {
                return mImg;
            }

            public void setImg(List<ImgData> img) {
                mImg = img;
            }

            public void setTags(List<String> tags) {
                mTags = tags;
            }

            public List<String> getEquipment() {
                return mEquipment;
            }

            public void setEquipment(List<String> equipment) {
                mEquipment = equipment;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public String getVideoDuration() {
                return mVideoDuration;
            }

            public void setVideoDuration(String videoDuration) {
                mVideoDuration = videoDuration;
            }

            public static class ImgData extends Data {
                @SerializedName("url")
                private String mUrl;

                public String getUrl() {
                    return mUrl;
                }

                public void setUrl(String url) {
                    mUrl = url;
                }
            }
        }
    }
}
