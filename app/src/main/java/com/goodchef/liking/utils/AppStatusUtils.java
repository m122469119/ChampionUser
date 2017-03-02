package com.goodchef.liking.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.aaron.android.codelibrary.utils.LogUtils;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created on 2017/2/28
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class AppStatusUtils {


    public static String getTopActivityClass(Context ctx){
        ActivityManager am = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getClassName();
    }



    /**
     * 判断app是否正在运行
     * @param ctx
     * @param packageName
     * @return
     */
    public static boolean appIsRunning(Context ctx, String packageName)
    {
        ActivityManager am = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        if(runningAppProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if(runningAppProcessInfo.processName.startsWith(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * app 是否在后台运行
     * @param ctx
     * @param packageName
     * @return
     */
    public static boolean appIsBackgroundRunning(Context ctx,String packageName)
    {
        ActivityManager am = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        if(runningAppProcesses!=null)
        {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {

                if(runningAppProcessInfo.processName.startsWith(packageName))
                {
                    return runningAppProcessInfo.importance!= ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                            && runningAppProcessInfo.importance!= ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE; //排除无界面的app
                }
            }
        }

        return false;
    }

    public static String getAppPackageName(Context ctx) {
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }
}
