package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.goodchef.liking.data.remote.retrofit.result.data.GymData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/17 上午11:41
 */
public class CardResult extends LikingResult {

    @SerializedName("data")
    private CardData mCardData;

    public CardData getCardData() {
        return mCardData;
    }

    public void setCardData(CardData cardData) {
        mCardData = cardData;
    }


    public static class CardData extends Data {
        public static final int STAGGER_CARD = 1;
        public static final int ALL_CARD = 2;
        public static final int STAGGER_AND_ALL_CARD = 3;


        @SerializedName("category")
        private Category category;

        @SerializedName("gym")
        private GymData gymData;

        @SerializedName("card_only_type")
        private int type;

        @SerializedName("gym_activity")
        private GymActivityBean gymActivityBean;


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }


        public GymActivityBean getGymActivityBean() {
            return gymActivityBean;
        }

        public void setGymActivityBean(GymActivityBean gymActivityBean) {
            this.gymActivityBean = gymActivityBean;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public GymData getGymData() {
            return gymData;
        }

        public void setGymData(GymData gymData) {
            this.gymData = gymData;
        }

        public static class Category extends Data {

            @SerializedName("free_card")
            private FreeCardBean freeCardBean;


            @SerializedName("all_day_card")
            private AllDayCardBean allDayCardBean;

            public FreeCardBean getFreeCardBean() {
                return freeCardBean;
            }

            public void setFreeCardBean(FreeCardBean freeCardBean) {
                this.freeCardBean = freeCardBean;
            }

            public AllDayCardBean getAllDayCardBean() {
                return allDayCardBean;
            }

            public void setAllDayCardBean(AllDayCardBean allDayCardBean) {
                this.allDayCardBean = allDayCardBean;
            }

            public static class FreeCardBean extends Data {
                @SerializedName("time_limit")
                private List<TimeLimitBean> timeLimitBeanList;

                @SerializedName("free_card_array")
                private List<CardBean> cardBeanList;

                public List<TimeLimitBean> getTimeLimitBeanList() {
                    return timeLimitBeanList;
                }

                public void setTimeLimitBeanList(List<TimeLimitBean> timeLimitBeanList) {
                    this.timeLimitBeanList = timeLimitBeanList;
                }

                public List<CardBean> getCardBeanList() {
                    return cardBeanList;
                }

                public void setCardBeanList(List<CardBean> cardBeanList) {
                    this.cardBeanList = cardBeanList;
                }
            }

            public static class AllDayCardBean extends Data {
                @SerializedName("time_limit")
                private List<TimeLimitBean> timeLimitBeanList;

                @SerializedName("all_day_array")
                private List<CardBean> cardBeanList;

                public List<TimeLimitBean> getTimeLimitBeanList() {
                    return timeLimitBeanList;
                }

                public void setTimeLimitBeanList(List<TimeLimitBean> timeLimitBeanList) {
                    this.timeLimitBeanList = timeLimitBeanList;
                }

                public List<CardBean> getCardBeanList() {
                    return cardBeanList;
                }

                public void setCardBeanList(List<CardBean> cardBeanList) {
                    this.cardBeanList = cardBeanList;
                }
            }

            public static class TimeLimitBean extends Data {

                /**
                 * title : 全天
                 * desc : 不限时段
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

            public static class CardBean extends Data {
                public static final int STAGGER_CARD = 1;
                public static final int ALL_CARD = 2;


                /**
                 * category_id : 2000
                 * card_id : 20000
                 * category_name : 错峰季卡
                 * category_url : xxxxx
                 * type : 1
                 * price : ¥0.00
                 * old_price : ¥500.00
                 * category_type : 2
                 * use_status : 1
                 * use_desc :
                 */

                private String category_id;
                private String card_id;
                private String category_name;
                private String category_url;
                private String type;
                private String price;
                private String old_price;
                private int category_type;
                private int use_status;
                private String use_desc;

                public String getCategory_id() {
                    return category_id;
                }

                public void setCategory_id(String category_id) {
                    this.category_id = category_id;
                }

                public String getCard_id() {
                    return card_id;
                }

                public void setCard_id(String card_id) {
                    this.card_id = card_id;
                }

                public String getCategory_name() {
                    return category_name;
                }

                public void setCategory_name(String category_name) {
                    this.category_name = category_name;
                }

                public String getCategory_url() {
                    return category_url;
                }

                public void setCategory_url(String category_url) {
                    this.category_url = category_url;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
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

                public int getCategory_type() {
                    return category_type;
                }

                public void setCategory_type(int category_type) {
                    this.category_type = category_type;
                }

                public int getUse_status() {
                    return use_status;
                }

                public void setUse_status(int use_status) {
                    this.use_status = use_status;
                }

                public String getUse_desc() {
                    return use_desc;
                }

                public void setUse_desc(String use_desc) {
                    this.use_desc = use_desc;
                }
            }
        }

        public static class GymActivityBean extends Data {

            public static final int NO_ACTIVITY = 0;
            public static final int HAVE_ACTIVITY = 1;

            /**
             * is_activity : 0
             * name :
             * start_time :
             * end_time :
             */

            private int is_activity;
            private String name;
            private String start_time;
            private String end_time;

            public int getIs_activity() {
                return is_activity;
            }

            public void setIs_activity(int is_activity) {
                this.is_activity = is_activity;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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
        }

    }


}
