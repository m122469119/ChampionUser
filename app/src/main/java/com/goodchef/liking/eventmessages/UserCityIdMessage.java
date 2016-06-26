package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/26 下午3:28
 */
public class UserCityIdMessage extends BaseMessage {
    private String userCityId;

    public UserCityIdMessage(String userCityId) {
        this.userCityId = userCityId;
    }

    public String getUserCityId() {
        return userCityId;
    }

    public void setUserCityId(String userCityId) {
        this.userCityId = userCityId;
    }
}
