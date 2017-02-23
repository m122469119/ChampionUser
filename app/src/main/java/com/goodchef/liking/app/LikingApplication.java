package com.goodchef.liking.app;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.goodchef.liking.BuildConfig;

import cn.jiajixin.nuwa.Nuwa;
import cn.jpush.android.api.JPushInterface;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingApplication extends BaseApplication {

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
        /**初始化Hotfix修复框架*/
        Nuwa.init(base);
    }

    @Override
    protected void initialize() {
        LogUtils.i(TAG, "initialize---" + this);
        JPushInterface.setDebugMode(EnvironmentUtils.Config.isTestMode());
        JPushInterface.init(this);
    }

    @Override
    protected void backgroundInitialize() {
        LogUtils.i(TAG, "backgroundInitialize---" + this);
    }

    @Override
    protected EnvironmentUtils.Config.ConfigData buildConfigData() {
        EnvironmentUtils.Config.ConfigData configData = new EnvironmentUtils.Config.ConfigData();
        configData.setAppFlag(BuildConfig.APP_FLAG);
        configData.setAppVersionCode(BuildConfig.VERSION_CODE);
        configData.setAppVersionName(BuildConfig.VERSION_NAME);
        configData.setIsTestMode(BuildConfig.TEST_MODE);
        configData.setUrlHost(BuildConfig.HTTP_HOST);
        return configData;
    }


    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        LogUtils.d(TAG, "onTerminate--" + this);
        super.onTerminate();
    }
    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        LogUtils.d(TAG, "onLowMemory--" + this);
        super.onLowMemory();
    }
    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        LogUtils.d(TAG, "onTrimMemory--" + this);
        super.onTrimMemory(level);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtils.d(TAG, "onConfigurationChanged--" + this);
        super.onConfigurationChanged(newConfig);
    }

}
