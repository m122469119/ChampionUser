package com.goodchef.liking.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;

/**
 * 说明: 透明或者设置状态栏完美解决方案
 * Android 4.4 以后版本均可，4.4以前的不行
 * Author : shaozucheng
 * Time: 下午8:53
 * version 1.0.0
 */

public class StatusBarUtils {

    public static void setWindowStatusBarColor(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置顶部状态栏颜色
                window.setStatusBarColor(ResourceUtils.getColor(R.color.transparent));
                //底部导航栏颜色
                window.setNavigationBarColor(ResourceUtils.getColor(R.color.main_app_color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
