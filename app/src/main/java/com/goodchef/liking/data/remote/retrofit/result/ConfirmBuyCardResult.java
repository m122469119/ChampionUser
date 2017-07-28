package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/29 下午4:28
 */
public class ConfirmBuyCardResult extends LikingResult {


    /**
     * data : {"purchase_activity":"购卡后请前往该健身房免费领取手环","deadline":"2017-09-11 ~ 2017-12-10","gym_name":"Liking Fit美奂店","gym_address":"中山南二路107号","price":"","purchase_type":2,"tips":"续卡只能续同类型会员卡","cards":[{"card_id":20000,"name":"错峰卡","type":1,"price":"455.00","old_price":"499.00","present_water":0,"is_activity":1,"time_limit":[{"title":"周一至周五","desc":"上午11:00前，下午14:00-17:00，晚上21:00后"},{"title":"周六、周日","desc":"全天"}]}],"show_time_limit":0}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean extends Data {
        /**
         * purchase_activity : 购卡后请前往该健身房免费领取手环
         * deadline : 2017-09-11 ~ 2017-12-10
         * gym_name : Liking Fit美奂店
         * gym_address : 中山南二路107号
         * price :
         * purchase_type : 2
         * tips : 续卡只能续同类型会员卡
         * cards : [{"card_id":20000,"name":"错峰卡","type":1,"price":"455.00","old_price":"499.00","present_water":0,"is_activity":1,"time_limit":[{"title":"周一至周五","desc":"上午11:00前，下午14:00-17:00，晚上21:00后"},{"title":"周六、周日","desc":"全天"}]}]
         * show_time_limit : 0
         */

        private String purchase_activity;
        private String deadline;
        private String gym_name;
        private String gym_address;
        private int is_gym_water; //0该场馆水费免费   1为场馆水费收费
        private String price;
        private int purchase_type;
        private String tips;
        private int show_time_limit;
        private List<CardsBean> cards;
        private double longitude;
        private double latitude;

        public String getPurchase_activity() {
            return purchase_activity;
        }

        public void setPurchase_activity(String purchase_activity) {
            this.purchase_activity = purchase_activity;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public String getGym_name() {
            return gym_name;
        }

        public void setGym_name(String gym_name) {
            this.gym_name = gym_name;
        }

        public String getGym_address() {
            return gym_address;
        }

        public void setGym_address(String gym_address) {
            this.gym_address = gym_address;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getPurchase_type() {
            return purchase_type;
        }

        public void setPurchase_type(int purchase_type) {
            this.purchase_type = purchase_type;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public int getShow_time_limit() {
            return show_time_limit;
        }

        public void setShow_time_limit(int show_time_limit) {
            this.show_time_limit = show_time_limit;
        }

        public List<CardsBean> getCards() {
            return cards;
        }

        public void setCards(List<CardsBean> cards) {
            this.cards = cards;
        }

        public int getIs_gym_water() {
            return is_gym_water;
        }

        public void setIs_gym_water(int is_gym_water) {
            this.is_gym_water = is_gym_water;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public static class CardsBean extends Data {


            public static final int NO_ACTIVITY = 0;
            public static final int HAVE_ACTIVITY = 1;

            /**
             * card_id : 20000
             * name : 错峰卡
             * type : 1
             * price : 455.00
             * old_price : 499.00
             * present_water : 0
             * is_activity : 1
             * time_limit : [{"title":"周一至周五","desc":"上午11:00前，下午14:00-17:00，晚上21:00后"},{"title":"周六、周日","desc":"全天"}]
             */

            private int card_id;
            private String name;
            private int type;
            private String price;
            private String old_price;
            private int present_water;
            private int is_activity;
            private List<TimeLimitBean> time_limit;

            public int getCard_id() {
                return card_id;
            }

            public void setCard_id(int card_id) {
                this.card_id = card_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getOld_price() {
                return old_price;
            }

            public void setOld_price(String old_price) {
                this.old_price = old_price;
            }

            public int getPresent_water() {
                return present_water;
            }

            public void setPresent_water(int present_water) {
                this.present_water = present_water;
            }

            public int getIs_activity() {
                return is_activity;
            }

            public void setIs_activity(int is_activity) {
                this.is_activity = is_activity;
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
    }
}
