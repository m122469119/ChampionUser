package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/25 下午2:31
 */
public class BuyGroupCoursesWechatMessage extends BaseMessage {

    private boolean paySuccess;

    public BuyGroupCoursesWechatMessage(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }

    public boolean isPaySuccess() {
        return paySuccess;
    }

    public void setPaySuccess(boolean paySuccess) {
        this.paySuccess = paySuccess;
    }
}
