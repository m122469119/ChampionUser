package com.goodchef.liking.http.result;

import com.aaron.http.code.result.Data;
import com.goodchef.liking.http.result.data.ConfirmCard;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/29 下午4:28
 */
public class ConfirmBuyCardResult extends LikingResult {

    @SerializedName("data")
    private ConfirmBuyCardData data;

    public ConfirmBuyCardData getData() {
        return data;
    }

    public void setData(ConfirmBuyCardData data) {
        this.data = data;
    }

    public static class ConfirmBuyCardData extends Data {
        @SerializedName("ads_url")
        private String adsUrl;
        @SerializedName("deadline")
        private String deadLine;
        @SerializedName("price")
        private String price;
        @SerializedName("cards")
        private List<ConfirmCard> mCardList;
        @SerializedName("purchase_type")
        private int purchaseType;
        @SerializedName("tips")
        private String tips;
        @SerializedName("purchase_activity")
        private String purchaseActivity;
        @SerializedName("gym_name")
        private String gymName;
        @SerializedName("gym_address")
        private String gymAddress;


        public String getAdsUrl() {
            return adsUrl;
        }

        public void setAdsUrl(String adsUrl) {
            this.adsUrl = adsUrl;
        }

        public String getDeadLine() {
            return deadLine;
        }

        public void setDeadLine(String deadLine) {
            this.deadLine = deadLine;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public List<ConfirmCard> getCardList() {
            return mCardList;
        }

        public void setCardList(List<ConfirmCard> cardList) {
            mCardList = cardList;
        }

        public int getPurchaseType() {
            return purchaseType;
        }

        public void setPurchaseType(int purchaseType) {
            this.purchaseType = purchaseType;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public String getPurchaseActivity() {
            return purchaseActivity;
        }

        public void setPurchaseActivity(String purchaseActivity) {
            this.purchaseActivity = purchaseActivity;
        }

        public String getGymName() {
            return gymName;
        }

        public void setGymName(String gymName) {
            this.gymName = gymName;
        }

        public String getGymAddress() {
            return gymAddress;
        }

        public void setGymAddress(String gymAddress) {
            this.gymAddress = gymAddress;
        }
    }
}
