package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/17.
 */

public class ChangeGymActivityMessage extends BaseMessage {
    public static final int CHANGE_LEFT_CITY_TEXT = 0x00000001;


    public int what;
    public int arg1;
    public int arg2;
    public String msg1;
    public String msg2;

    public Object obj1;
    public Object obj2;

    public ChangeGymActivityMessage(int what) {
        this.what = what;
    }

    public static ChangeGymActivityMessage obtain(int what){
        return new ChangeGymActivityMessage(what);
    }

}
