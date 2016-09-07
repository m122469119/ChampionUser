package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/7 下午4:23
 */
public class NoCardMessage extends BaseMessage {
    private int target;

    public NoCardMessage(int target) {
        this.target = target;
    }

    public int getTarget() {
        return target;
    }
}
