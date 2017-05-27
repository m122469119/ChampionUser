package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/1/25 下午7:17
 */
public class UserLoginResult extends LikingResult {

    @SerializedName("data")
    private UserLoginData mUserLoginData;

    public UserLoginData getUserLoginData() {
        return mUserLoginData;
    }

    public void setUserLoginData(UserLoginData userLoginData) {
        mUserLoginData = userLoginData;
    }

    public static class UserLoginData extends Data {

        /**
         * token : 12908fa5a8bce03d35358ca331bb51e2
         * name : 雷达是美男子
         * phone : 13222222222
         * avatar : sssfwf
         * newUser : 0
         */

        @SerializedName("token")
        private String token;
        @SerializedName("name")
        private String name;
        @SerializedName("phone")
        private String phone;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("newUser")
        private int newUser;
        @SerializedName("is_vip")
        private int isVip;
        @SerializedName("gym_id")
        private String gymId;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getNewUser() {
            return newUser;
        }

        public void setNewUser(int newUser) {
            this.newUser = newUser;
        }

        public int getIsVip() {
            return isVip;
        }

        public void setIsVip(int isVip) {
            this.isVip = isVip;
        }

        public String getGymId() {
            return gymId;
        }

        public void setGymId(String gymId) {
            this.gymId = gymId;
        }
    }
}