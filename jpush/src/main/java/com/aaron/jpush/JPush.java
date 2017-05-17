package com.aaron.jpush;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;


/**
 * Created on 17/5/12.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class JPush {
    public static void init(Context context, boolean isDebugMode) {
        JPushInterface.setDebugMode(isDebugMode);
        JPushInterface.init(context);
    }
}
