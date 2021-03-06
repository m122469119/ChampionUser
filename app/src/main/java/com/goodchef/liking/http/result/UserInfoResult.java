package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/4 下午10:52
 */
public class UserInfoResult extends BaseResult {

    @SerializedName("data")
    private UserInfoData data;

    public UserInfoData getData() {
        return data;
    }

    public void setData(UserInfoData data) {
        this.data = data;
    }

    public class UserInfoData extends BaseData {

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
    }
}
