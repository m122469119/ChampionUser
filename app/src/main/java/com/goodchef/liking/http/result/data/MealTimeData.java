package com.goodchef.liking.http.result.data;

import com.aaron.android.codelibrary.http.result.BaseData;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/27 下午4:47
 */
public class MealTimeData extends BaseData{
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
