package com.goodchef.liking.data.remote.retrofit.result.data;

import com.aaron.http.code.result.Data;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/27 下午4:47
 */
public class MealTimeData extends Data {
    private String mealTime;
    private boolean isSelect;

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
