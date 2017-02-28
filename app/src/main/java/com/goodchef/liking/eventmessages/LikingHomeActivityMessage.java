package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * Created on 2017/2/28
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class LikingHomeActivityMessage extends BaseMessage {

    public static final int SHOW_PUSH_DIALOG = 0x00000001;


    public LikingHomeActivityMessage(int what) {
        super(what);
    }

    public static LikingHomeActivityMessage obtain(int what){
        return new LikingHomeActivityMessage(what);
    }
}
