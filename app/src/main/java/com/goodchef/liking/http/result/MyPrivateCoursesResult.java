package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/18 下午2:49
 */
public class MyPrivateCoursesResult extends BaseResult {

    @SerializedName("data")
    private PrivateCoursesData data;

    public PrivateCoursesData getData() {
        return data;
    }

    public void setData(PrivateCoursesData data) {
        this.data = data;
    }

    public static class PrivateCoursesData extends BaseData {
        @SerializedName("list")
        private List<PrivateCourses> mPrivateCoursesList;
        @SerializedName("has_more")
        private int hasMore;

        public List<PrivateCourses> getPrivateCoursesList() {
            return mPrivateCoursesList;
        }

        public void setPrivateCoursesList(List<PrivateCourses> privateCoursesList) {
            mPrivateCoursesList = privateCoursesList;
        }

        public int getHasMore() {
            return hasMore;
        }

        public void setHasMore(int hasMore) {
            this.hasMore = hasMore;
        }

        public static class PrivateCourses extends BaseResult {

            @SerializedName("order_id")
            private String orderId;
            @SerializedName("status")
            private int status;
            @SerializedName("trainer_name")
            private String trainerName;
            @SerializedName("course_name")
            private String courseName;
            @SerializedName("trainer_avatar")
            private String trainerAvatar;
            @SerializedName("start_time")
            private String startTime;
            @SerializedName("end_time")
            private String endTime;

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTrainerName() {
                return trainerName;
            }

            public void setTrainerName(String trainerName) {
                this.trainerName = trainerName;
            }

            public String getCourseName() {
                return courseName;
            }

            public void setCourseName(String courseName) {
                this.courseName = courseName;
            }

            public String getTrainerAvatar() {
                return trainerAvatar;
            }

            public void setTrainerAvatar(String trainerAvatar) {
                this.trainerAvatar = trainerAvatar;
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
        }
    }


}
