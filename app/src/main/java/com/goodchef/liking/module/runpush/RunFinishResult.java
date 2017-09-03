package com.goodchef.liking.module.runpush;

import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RunFinishResult extends LikingResult {

    /**
     * data : {"user":{"avatar":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg","count":"1145","gender":1,"marahton_name":"马拉松","num":"No.32","use_time":526,"distance":"265"},"list":[{"name":"sss","avatar":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg","user_id":"likingfit2131231231","gender":1,"desc":232},{"name":"sss","avatar":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg","user_id":"likingfit2131231231","gender":1,"desc":232},{"name":"sss","avatar":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg","user_id":"likingfit2131231231","gender":1,"desc":232},{"name":"sss","avatar":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg","user_id":"likingfit2131231231","gender":1,"desc":232},{"name":"sss","avatar":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg","user_id":"likingfit2131231231","gender":1,"desc":232}]}
     */

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user : {"avatar":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg","count":"1145","gender":1,"marahton_name":"马拉松","num":"No.32","use_time":526,"distance":"265"}
         * list : [{"name":"sss","avatar":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg","user_id":"likingfit2131231231","gender":1,"desc":232},{"name":"sss","avatar":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg","user_id":"likingfit2131231231","gender":1,"desc":232},{"name":"sss","avatar":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg","user_id":"likingfit2131231231","gender":1,"desc":232},{"name":"sss","avatar":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg","user_id":"likingfit2131231231","gender":1,"desc":232},{"name":"sss","avatar":"http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg","user_id":"likingfit2131231231","gender":1,"desc":232}]
         */

        @SerializedName("user")
        private UserBean user;
        @SerializedName("list")
        private List<ListBean> list;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class UserBean {
            /**
             * avatar : http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg
             * count : 1145
             * gender : 1
             * marahton_name : 马拉松
             * num : No.32
             * use_time : 526
             * distance : 265
             */

            @SerializedName("avatar")
            private String avatar;
            @SerializedName("count")
            private String count;
            @SerializedName("gender")
            private int gender;
            @SerializedName("marahton_name")
            private String marahtonName;
            @SerializedName("num")
            private String num;
            @SerializedName("use_time")
            private String useTime;
            @SerializedName("distance")
            private String distance;

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getMarahtonName() {
                return marahtonName;
            }

            public void setMarahtonName(String marahtonName) {
                this.marahtonName = marahtonName;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getUseTime() {
                return useTime;
            }

            public void setUseTime(String useTime) {
                this.useTime = useTime;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }
        }

        public static class ListBean {
            /**
             * name : sss
             * avatar : http://1029320420942-smartspot-bucket.oss-cn-hongkong.aliyuncs.com/WorkoutSet_36883_0.jpg
             * user_id : likingfit2131231231
             * gender : 1
             * desc : 232
             */

            @SerializedName("name")
            private String name;
            @SerializedName("avatar")
            private String avatar;
            @SerializedName("user_id")
            private String userId;
            @SerializedName("gender")
            private int gender;
            @SerializedName("desc")
            private int desc;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public int getDesc() {
                return desc;
            }

            public void setDesc(int desc) {
                this.desc = desc;
            }
        }
    }
}
