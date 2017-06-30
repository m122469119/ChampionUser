package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * Created on 2017/6/30
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class WaterRateActivityMessage extends BaseMessage {

    public static final int WECHAT_PAY = 1;

    public WaterRateActivityMessage(int what) {
        super(what);
    }
}
