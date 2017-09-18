package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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

        public static final int CAN_RENEW = 1;
        public static final int CAN_NOT_RENEW = 0;

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
        @SerializedName("can_renew")
        private int can_renew;
        @SerializedName("show_code")
        private int showCode;

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

        public int getCan_renew() {
            return can_renew;
        }

        public void setCan_renew(int can_renew) {
            this.can_renew = can_renew;
        }

        public int getShowCode() {
            return showCode;
        }

        public void setShowCode(int showCode) {
            this.showCode = showCode;
        }

        public static class CardData extends Data {

            /**
             * card_no : 4704402099455160135
             * buy_time : 2017-02-14
             * start_time : 2017-04-25
             * end_time : 2017-11-11
             * time_limit : [{"title":"周一至周五","desc":"上午11:00前，下午14:00-17:00，晚上21:00后"},{"title":"周六、周日","desc":"全天"}]
             * gym_id : 1
             * gym_name : LikingFit复兴店
             * gym_addr : 马当路388号复兴SOHO广场B2-02
             */

            private String card_no;
            private String buy_time;
            private String start_time;
            private String end_time;
            private String gym_id;
            private String gym_name;
            private String gym_addr;
            private List<TimeLimitBean> time_limit;

            public String getCard_no() {
                return card_no;
            }

            public void setCard_no(String card_no) {
                this.card_no = card_no;
            }

            public String getBuy_time() {
                return buy_time;
            }

            public void setBuy_time(String buy_time) {
                this.buy_time = buy_time;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getGym_id() {
                return gym_id;
            }

            public void setGym_id(String gym_id) {
                this.gym_id = gym_id;
            }

            public String getGym_name() {
                return gym_name;
            }

            public void setGym_name(String gym_name) {
                this.gym_name = gym_name;
            }

            public String getGym_addr() {
                return gym_addr;
            }

            public void setGym_addr(String gym_addr) {
                this.gym_addr = gym_addr;
            }

            public List<TimeLimitBean> getTime_limit() {
                return time_limit;
            }

            public void setTime_limit(List<TimeLimitBean> time_limit) {
                this.time_limit = time_limit;
            }

            public static class TimeLimitBean extends Data {
                /**
                 * title : 周一至周五
                 * desc : 上午11:00前，下午14:00-17:00，晚上21:00后
                 */

                private String title;
                private String desc;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }
            }
        }

        public static class WaterData extends Data {


            public static final int FREE_WATER = 0;
            public static final int CHARGE_WATER = 1;


            /**
             * water_status : 0
             * water_time :
             */

            private int water_status;
            private String water_time;

            public int getWater_status() {
                return water_status;
            }

            public void setWater_status(int water_status) {
                this.water_status = water_status;
            }

            public String getWater_time() {
                return water_time;
            }

            public void setWater_time(String water_time) {
                this.water_time = water_time;
            }
        }

    }


}
