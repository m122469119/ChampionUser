package com.goodchef.liking.eventmessages;


import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * Created on 16/2/2.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class InitApiFinishedMessage extends BaseMessage {
    private boolean mIsSuccess = false;

    public InitApiFinishedMessage(boolean isSuccess) {
        mIsSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }
}
