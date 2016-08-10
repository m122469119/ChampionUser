package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 上午10:17
 */
public class PrivateCoursesConfirmResult extends BaseResult {
    @SerializedName("data")
    private PrivateCoursesConfirmData data;

    public PrivateCoursesConfirmData getData() {
        return data;
    }

    public void setData(PrivateCoursesConfirmData data) {
        this.data = data;
    }

    public class PrivateCoursesConfirmData extends BaseData {
        @SerializedName("people_num")
        private String peopleNum;
        @SerializedName("end_time")
        private String endTime;
        @SerializedName("total_amount")
        private String totalAmount;
        @SerializedName("course")
        private List<Courses> mCourses;
        @SerializedName("address_text")
        private String addressText;

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

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public List<Courses> getCourses() {
            return mCourses;
        }

        public void setCourses(List<Courses> courses) {
            mCourses = courses;
        }

        public String getAddressText() {
            return addressText;
        }

        public void setAddressText(String addressText) {
            this.addressText = addressText;
        }

        public class Courses extends BaseData {
            @SerializedName("course_id")
            private String courseId;
            @SerializedName("name")
            private String name;
            @SerializedName("times")
            private String times;
            @SerializedName("price")
            private String price;
            @SerializedName("course_addr")
            private String courseAddress;
            @SerializedName("duration")
            private String duration;

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

            public String getTimes() {
                return times;
            }

            public void setTimes(String times) {
                this.times = times;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getCourseAddress() {
                return courseAddress;
            }

            public void setCourseAddress(String courseAddress) {
                this.courseAddress = courseAddress;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }
        }
    }


}
