package com.goodchef.liking.http.result.data;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/29 下午4:21
 */
public class ConfirmCard extends Data {

    @SerializedName("card_id")
    private int cardId;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private int type;
    @SerializedName("price")
    private String price;
    @SerializedName("qulification")
    private int qulification;
    @SerializedName("can_select")
    private int canSelect;

    private boolean isSelect;
    private boolean layoutViewEnable;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQulification() {
        return qulification;
    }

    public int getCanSelect() {
        return canSelect;
    }

    public void setCanSelect(int canSelect) {
        this.canSelect = canSelect;
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

    public boolean isLayoutViewEnable() {
        return layoutViewEnable;
    }

    public void setLayoutViewEnable(boolean layoutViewEnable) {
        this.layoutViewEnable = layoutViewEnable;
    }
}
