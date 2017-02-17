package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/17.
 */

public class ChangeCityActivityMessage extends BaseMessage{

    public static final int CITY_ITEM_CLICK = 0x00000001;


    public ChangeCityActivityMessage(int what) {

    }

    public static ChangeCityActivityMessage obtain(int what){
        return new ChangeCityActivityMessage(what);
    }

}
