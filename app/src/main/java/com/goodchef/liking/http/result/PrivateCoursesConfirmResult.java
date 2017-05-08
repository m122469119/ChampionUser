package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.Data;
import com.goodchef.liking.http.result.data.PlacesData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 上午10:17
 */
public class PrivateCoursesConfirmResult extends LikingResult {
    @SerializedName("data")
    private PrivateCoursesConfirmData data;

    public PrivateCoursesConfirmData getData() {
        return data;
    }

    public void setData(PrivateCoursesConfirmData data) {
        this.data = data;
    }

    public class PrivateCoursesConfirmData extends Data {
        @SerializedName("people_num")
        private String peopleNum;
        @SerializedName("duration")
        private String duration;
        @SerializedName("amount")
        private String amount;
        @SerializedName("course")
        private List<Courses> mCourses;
        @SerializedName("end_time")
        private String endTime;

        @SerializedName("places")
        private List<PlacesData> placesList;

        public String getPeopleNum() {
            return peopleNum;
        }

        public void setPeopleNum(String peopleNum) {
            this.peopleNum = peopleNum;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }


        public List<Courses> getCourses() {
            return mCourses;
        }

        public void setCourses(List<Courses> courses) {
            mCourses = courses;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public List<PlacesData> getPlacesList() {
            return placesList;
        }

        public void setPlacesList(List<PlacesData> placesList) {
            this.placesList = placesList;
        }

        public class Courses extends Data {
            @SerializedName("course_id")
            private String courseId;
            @SerializedName("name")
            private String name;
            @SerializedName("min_times")
            private String minTimes;
            @SerializedName("max_time")
            private String maxTimes;
            @SerializedName("prompt")
            private String prompt;

            private boolean isSelect;

            public String getCourseId() {
                return courseId;
            }

            public void setCourseId(String courseId) {
                this.courseId = courseId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public String getMinTimes() {
                return minTimes;
            }

            public void setMinTimes(String minTimes) {
                this.minTimes = minTimes;
            }

            public String getMaxTimes() {
                return maxTimes;
            }

            public void setMaxTimes(String maxTimes) {
                this.maxTimes = maxTimes;
            }

            public String getPrompt() {
                return prompt;
            }

            public void setPrompt(String prompt) {
                this.prompt = prompt;
            }
        }
    }


}
