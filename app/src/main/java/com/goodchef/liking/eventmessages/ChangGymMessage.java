package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/28 下午5:56
 */
public class ChangGymMessage extends BaseMessage {

    private String gymId;

    public ChangGymMessage(String gymId) {
        this.gymId = gymId;
    }

    public String getGymId() {
        return gymId;
    }
}
