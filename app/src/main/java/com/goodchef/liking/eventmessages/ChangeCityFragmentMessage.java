package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/17.
 */

public class ChangeCityFragmentMessage extends BaseMessage {
    public static final int REFRESH_LIST_DATA = 0x00000001;


    public ChangeCityFragmentMessage(int what) {
        super(what);
    }

    public static ChangeCityFragmentMessage obtain(int what){
        return new ChangeCityFragmentMessage(what);
    }
}
