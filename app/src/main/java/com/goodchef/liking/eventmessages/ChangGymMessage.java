package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/28 下午5:56
 */
public class ChangGymMessage extends BaseMessage {

    private int index;

    public ChangGymMessage(int index) {
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
}
