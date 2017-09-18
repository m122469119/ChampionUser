package com.goodchef.liking.module.message;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * Created by aaa on 17/9/18.
 */

public class ShowCodeMessage extends BaseMessage {
    private int showCode;

    public ShowCodeMessage(int showCode) {
        this.showCode = showCode;
    }

    public int getShowCode() {
        return showCode;
    }
}
