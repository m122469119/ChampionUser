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

    public static class GroupLessonData extends BaseData {


        /**
         * course_name : 俯卧撑
         * course_date : 今天 15:30-16:30
         * intensity : 2
         * course_desc : dafdsgdsgdfasd
         * quota : 还剩4个名额
         * course_imgs : ["http://testimg.likingfit.com/img/2016/07/06/1467790905_577cb63945089.jpg"]
         * gym_name : 凌空SOHO
         * gym_address : 金钟路968号凌空SOHO2号楼一层
         * gym_imgs : [{"object_id":"2","title":"","url":"http://testimg.likingfit.com//img/fuxing.jpg"}]
         * gym_id : 2
         * trainer_name : 雷达
         * trainer_imgs : [""]
         * trainer_desc : 雷达22，这是一个很厉害的教练，有多厉害呢，反正就是厉害到飞起，雷达22，这是一个很厉害的教练，有多厉害呢，反正就是厉害到飞起雷达22，这是一个很厉害的教练，有多厉害呢，反正就是厉害到飞起，雷达22，这是一个很厉害的教练，有多厉害呢，反正就是厉害到飞起
         */

        @SerializedName("course_name")
        private String courseName;
        @SerializedName("course_date")
        private String courseDate;
        @SerializedName("intensity")
        private String intensity;
        @SerializedName("course_desc")
        private String courseDesc;
        @SerializedName("quota")
        private String quota;
        @SerializedName("quota_desc")
        private String quotaDesc;
        @SerializedName("gym_name")
        private String gymName;
        @SerializedName("gym_address")
        private String gymAddress;
        @SerializedName("gym_id")
        private String gymId;
        @SerializedName("trainer_name")
        private String trainerName;
        @SerializedName("trainer_desc")
        private String trainerDesc;
        @SerializedName("course_imgs")
        private List<String> courseImgs;
        @SerializedName("is_fee")
        private int isFree;
        @SerializedName("price")
        private String price;
        @SerializedName("schedule_type")
        private int scheduleType;

        /**
         * object_id : 2
         * title :
         * url : http://testimg.likingfit.com//img/fuxing.jpg
         */

        @SerializedName("gym_imgs")
        private List<GymImgsData> gymImgs;
        @SerializedName("trainer_imgs")
        private List<String> trainerImgs;

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

        public String getQuota() {
            return quota;
        }

        public void setQuota(String quota) {
            this.quota = quota;
        }

        public String getQuotaDesc() {
            return quotaDesc;
        }

        public void setQuotaDesc(String quotaDesc) {
            this.quotaDesc = quotaDesc;
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

        public String getGymId() {
            return gymId;
        }

        public void setGymId(String gymId) {
            this.gymId = gymId;
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

        public List<GymImgsData> getGymImgs() {
            return gymImgs;
        }

        public void setGymImgs(List<GymImgsData> gymImgs) {
            this.gymImgs = gymImgs;
        }

        public List<String> getTrainerImgs() {
            return trainerImgs;
        }

        public void setTrainerImgs(List<String> trainerImgs) {
            this.trainerImgs = trainerImgs;
        }

        public int getIsFree() {
            return isFree;
        }

        public void setIsFree(int isFree) {
            this.isFree = isFree;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getScheduleType() {
            return scheduleType;
        }

        public void setScheduleType(int scheduleType) {
            this.scheduleType = scheduleType;
        }

        public static class GymImgsData {
            @SerializedName("object_id")
            private String objectId;
            @SerializedName("title")
            private String title;
            @SerializedName("url")
            private String url;

            public String getObjectId() {
                return objectId;
            }

            public void setObjectId(String objectId) {
                this.objectId = objectId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }


}
