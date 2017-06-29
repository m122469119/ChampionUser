package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:28
 * version 1.0.0
 */

public class MyUserOtherInfoResult extends LikingResult {


    /**
     * data : {"is_vip":"0","card":{},"is_bind":0,"all_distance":0,"bracelet_mac":"","is_first_bind":1,"uuid":"","today_min":"0"}
     */

    @SerializedName("data")
    private UserOtherInfoData data;

    public UserOtherInfoData getData() {
        return data;
    }

    public void setData(UserOtherInfoData data) {
        this.data = data;
    }

    public static class UserOtherInfoData extends Data {
        /**
         * is_vip : 0
         * card : {}
         * is_bind : 0
         * all_distance : 0
         * bracelet_mac :
         * is_first_bind : 1
         * uuid :
         * today_min : 0
         */

        @SerializedName("is_vip")
        private String isVip;
        @SerializedName("card")
        private CardData card;
        @SerializedName("is_bind")
        private String isBind;
        @SerializedName("all_distance")
        private String allDistance;
        @SerializedName("bracelet_mac")
        private String braceletMac;
        @SerializedName("is_first_bind")
        private int isFirstBind;
        @SerializedName("uuid")
        private String uuid;
        @SerializedName("today_min")
        private String todayMin;
        @SerializedName("water")
        private WaterData waterData;

        public WaterData getWaterData() {
            return waterData;
        }

        public void setWaterData(WaterData waterData) {
            this.waterData = waterData;
        }

        public String getIsVip() {
            return isVip;
        }

        public void setIsVip(String isVip) {
            this.isVip = isVip;
        }

        public CardData getCard() {
            return card;
        }

        public void setCard(CardData card) {
            this.card = card;
        }

        public String getIsBind() {
            return isBind;
        }

        public void setIsBind(String isBind) {
            this.isBind = isBind;
        }

        public String getAllDistance() {
            return allDistance;
        }

        public void setAllDistance(String allDistance) {
            this.allDistance = allDistance;
        }

        public String getBraceletMac() {
            return braceletMac;
        }

        public void setBraceletMac(String braceletMac) {
            this.braceletMac = braceletMac;
        }

        public int getIsFirstBind() {
            return isFirstBind;
        }

        public void setIsFirstBind(int isFirstBind) {
            this.isFirstBind = isFirstBind;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getTodayMin() {
            return todayMin;
        }

        public void setTodayMin(String todayMin) {
            this.todayMin = todayMin;
        }

        public static class CardData extends Data {
        }

        public static class WaterData extends Data {


            public static final int FREE_WATER = 0;
            public static final int CHARGE_WATER = 1;

            /**
             * water_status : 0
             * water_time : 20
             */

            @SerializedName("water_status")
            private int water_status;
            @SerializedName("water_time")
            private int water_time;

            public int getWater_status() {
                return water_status;
            }

            public void setWater_status(int water_status) {
                this.water_status = water_status;
            }

            public int getWater_time() {
                return water_time;
            }

            public void setWater_time(int water_time) {
                this.water_time = water_time;
            }
        }

    }


}
