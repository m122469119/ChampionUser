package com.aaron.android.framework.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created on 2014/12/16 11:18.
 *
 * @author ran.huang
 * @version 1.0.0
 */
public class PopupUtils {

    /**
     * 显示Toast
     *
     * @param msg 内容
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示Toast
     *
     * @param resId 资源ID
     */
    public static void showToast(Context context, int resId) {
        Toast.makeText(context, ResourceUtils.getString(resId), Toast.LENGTH_LONG).show();
    }
}
