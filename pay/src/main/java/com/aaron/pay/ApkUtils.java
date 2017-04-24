package com.aaron.pay;

import android.content.Context;
import android.content.Intent;

/**
 * 1. 安装apk
 * 2. 获取本机安装的应用程序信息-包名、版本
 *
 * @author guo.chen
 * @version 4.1.0
 */
public final class ApkUtils {

    /**
     * 根据包名获取意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 意图
     */
    private static Intent getIntentByPackageName(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }

    /**
     * 根据包名判断App是否安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return true: 已安装<br>false: 未安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        return getIntentByPackageName(context, packageName) != null;
    }



}
