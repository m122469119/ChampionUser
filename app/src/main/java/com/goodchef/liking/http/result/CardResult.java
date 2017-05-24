package com.goodchef.liking.http.result;

import com.aaron.http.code.result.Data;
import com.goodchef.liking.http.result.data.GymData;
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

        @SerializedName("category")
        List<Card> mCardList;

        @SerializedName("gym")
        private GymData mGymData;

        public List<Card> getCardList() {
            return mCardList;
        }

        public void setCardList(List<Card> cardList) {
            mCardList = cardList;
        }

        public GymData getGymData() {
            return mGymData;
        }

        public void setGymData(GymData gymData) {
            mGymData = gymData;
        }

        public static class Card extends Data {
            @SerializedName("category_id")
            private int categoryId;
            @SerializedName("category_name")
            private String categoryName;
            @SerializedName("category_url")
            private String categoryUrl;
            @SerializedName("price")
            private String price;

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getCategoryUrl() {
                return categoryUrl;
            }

            public void setCategoryUrl(String categoryUrl) {
                this.categoryUrl = categoryUrl;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }


}
