package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/9/19 下午2:41
 */
public class DrawerMessage extends BaseMessage {
    boolean isopen;

    public DrawerMessage(boolean isopen) {
        this.isopen = isopen;
    }

    public boolean isopen() {
        return isopen;
    }
}
