package com.goodchef.liking.http.result.data;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/29 下午4:21
 */
public class ConfirmCard extends BaseData {

    @SerializedName("card_id")
    private int cardId;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private int type;
    @SerializedName("price")
    private int price;
    @SerializedName("qulification")
    private int qulification;

    private boolean isSelect;

    @SerializedName("time_limit")
    private List<TimeLimitData> timeLimit;

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQulification() {
        return qulification;
    }

    public void setQulification(int qulification) {
        this.qulification = qulification;
    }

    public List<TimeLimitData> getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(List<TimeLimitData> timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}