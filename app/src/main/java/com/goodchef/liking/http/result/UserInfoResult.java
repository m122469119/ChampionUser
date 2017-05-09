package com.goodchef.liking.http.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/4 下午10:52
 */
public class UserInfoResult extends LikingResult {

    @SerializedName("data")
    private UserInfoData data;

    public UserInfoData getData() {
        return data;
    }

    public void setData(UserInfoData data) {
        this.data = data;
    }

    public class UserInfoData extends Data {

        @SerializedName("avatar")
        private String avatar;
        @SerializedName("name")
        private String name;
        @SerializedName("birthday")
        private String birthday;
        @SerializedName("height")
        private int height;
        @SerializedName("weight")
        private double weight;
        @SerializedName("gender")
        private int gender;
        @SerializedName("is_update_birthday")
        private int isUpdateBirthday;
        @SerializedName("is_update_gender")
        private int isUpdateGender;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getIsUpdateBirthday() {
            return isUpdateBirthday;
        }

        public void setIsUpdateBirthday(int isUpdateBirthday) {
            this.isUpdateBirthday = isUpdateBirthday;
        }

        public int getIsUpdateGender() {
            return isUpdateGender;
        }

        public void setIsUpdateGender(int isUpdateGender) {
            this.isUpdateGender = isUpdateGender;
        }
    }
}
