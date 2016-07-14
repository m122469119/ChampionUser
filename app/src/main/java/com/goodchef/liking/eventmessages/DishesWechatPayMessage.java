package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/27 下午10:01
 */
public class DishesWechatPayMessage extends BaseMessage {
    private boolean isBuySuccess;

    public DishesWechatPayMessage(boolean isBuySuccess) {
        this.isBuySuccess = isBuySuccess;
    }

    public boolean isBuySuccess() {
        return isBuySuccess;
    }
}
