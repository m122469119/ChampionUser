package com.goodchef.liking.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.aaron.android.framework.library.http.volley.VolleyRequestSingleton;
import com.aaron.android.framework.library.thread.TaskScheduler;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.BuildConfig;
import com.goodchef.liking.module.data.local.Preference;
import com.goodchef.liking.tinker.MyLogImp;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import cn.jpush.android.api.JPushInterface;

/**
 * Created on 2017/4/12
 * Created by sanfen
 *
 * @version 1.0.0
 */

@DefaultLifeCycle(application = "com.goodchef.liking.app.LikingApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class LikingApplicationLike extends  DefaultApplicationLike{
    private static final String TAG = "LikingApplicationLike";

    private ProcessInit mMainProcessInit;

    private static Application sApplication;

    public LikingApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
        sApplication = application;
    }

    public static Application getApp() {
        return sApplication;
    }


    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     *
     * @param base
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
        TinkerInstaller.setLogIml(new MyLogImp());
        TinkerInstaller.install(this);
        Tinker tinker = Tinker.with(getApplication());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

    protected EnvironmentUtils.Config.ConfigData buildConfigData(){
        EnvironmentUtils.Config.ConfigData configData = new EnvironmentUtils.Config.ConfigData();
        configData.setAppFlag(BuildConfig.APP_FLAG);
        configData.setAppVersionCode(BuildConfig.VERSION_CODE);
        configData.setAppVersionName(BuildConfig.VERSION_NAME);
        configData.setIsTestMode(BuildConfig.TEST_MODE);
        configData.setUrlHost(BuildConfig.HTTP_HOST);
        return configData;
    }

    private void init() {
        mMainProcessInit = new MainProcessInit(getApplication(), buildConfigData());
        mMainProcessInit.init();
        doBackgroundInit();
        initialize();
    }

    private void doBackgroundInit() {
        TaskScheduler.execute(new Runnable() {
            @Override
            public void run() {
                backgroundInitialize();
            }
        });
    }

    protected void initialize() {
        LogUtils.i(TAG, "initialize---" + this);
        JPushInterface.setDebugMode(EnvironmentUtils.Config.isTestMode());
        JPushInterface.init(getApplication());
        Preference.init(getApplication());
        VolleyRequestSingleton.init(getApplication());
        ResourceUtils.init(getApplication());
    }

    protected void backgroundInitialize() {
        LogUtils.i(TAG, "backgroundInitialize---" + this);
    }

}
