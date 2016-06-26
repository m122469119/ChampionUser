package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.goodchef.liking.http.result.data.Food;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/24 下午10:10
 */
public class JumpToDishesDetailsMessage extends BaseMessage {
    private Food foodData;
    private String mUserCityId;


    public JumpToDishesDetailsMessage(Food foodData, String userCityId) {
        this.foodData = foodData;
        mUserCityId = userCityId;
    }

    public Food getFoodData() {
        return foodData;
    }

    public void setFoodData(Food foodData) {
        this.foodData = foodData;
    }

    public String getUserCityId() {
        return mUserCityId;
    }

    public void setUserCityId(String userCityId) {
        mUserCityId = userCityId;
    }
}
