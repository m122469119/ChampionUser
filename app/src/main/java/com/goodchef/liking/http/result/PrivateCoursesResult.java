package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
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
         * plan :
         * plan_imgs : []
         */

        @SerializedName("trainer_name")
        private String trainerName;
        @SerializedName("desc")
        private String desc;
        @SerializedName("plan")
        private String plan;
        @SerializedName("tags")
        private List<String> tags;
        @SerializedName("trainer_imgs")
        private List<String> trainerImgs;
        @SerializedName("plan_imgs")
        private List<String> planImgs;

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

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }

        public List<?> getTags() {
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

        public List<String> getPlanImgs() {
            return planImgs;
        }

        public void setPlanImgs(List<String> planImgs) {
            this.planImgs = planImgs;
        }
    }
}
