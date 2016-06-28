package com.goodchef.liking.fragment;

import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.goodchef.liking.http.result.data.Food;

import java.util.ArrayList;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/28 上午10:00
 */
public class RefreshChangeDataMessage extends BaseMessage {
    private ArrayList<Food> buyList;

    public RefreshChangeDataMessage(ArrayList<Food> buyList) {
        this.buyList = buyList;
    }

    public ArrayList<Food> getBuyList() {
        return buyList;
    }

    public void setBuyList(ArrayList<Food> buyList) {
        this.buyList = buyList;
    }
}
