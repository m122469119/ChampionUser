package com.aaron.android.framework.utils;

import android.content.Context;
import android.widget.Toast;

import com.aaron.common.utils.StringUtils;

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
        if (!StringUtils.isEmpty(msg)) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 显示Toast
     *
     * @param resId 资源ID
     */
    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }
}
