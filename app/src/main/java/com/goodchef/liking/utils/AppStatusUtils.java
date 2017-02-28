package com.goodchef.liking.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created on 2017/2/28
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class AppStatusUtils {
    /**
     * 判断app是否正在运行
     * @param ctx
     * @param packageName
     * @return
     */
    public boolean appIsRunning(Context ctx, String packageName)
    {
        ActivityManager am = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        if(runningAppProcesses!=null)
        {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {

                if(runningAppProcessInfo.processName.startsWith(packageName))
                {
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
    public boolean appIsBackgroundRunning(Context ctx,String packageName)
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
}
