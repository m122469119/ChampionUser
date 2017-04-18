package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/28 下午5:56
 */
public class ChangGymMessage extends BaseMessage {

    private String gymId;
    private int index;

    public ChangGymMessage(String gymId,int index) {
        this.gymId = gymId;
        this.index = index;
    }

    public String getGymId() {
        return gymId;
    }

    public int getIndex() {
        return index;
    }
}
