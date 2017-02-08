package com.aaron.android.framework.base;

import android.app.Application;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.library.thread.TaskScheduler;
import com.aaron.android.framework.utils.EnvironmentUtils;

/**
 * Created on 15/6/2.
 *
 * @author ran.huang
 * @version 3.0.1
 *
 *
 * 基类，处理完全退出，系统全局异常等
 */
public abstract class BaseApplication extends Application {

    private static BaseApplication sApplication;
    protected final String TAG = getClass().getSimpleName();
    private ProcessInit mMainProcessInit;

    /**
     * 子类实现其他相关初始化操作(由主线程处理，可做非耗时初始化操作)
     */
    protected abstract void initialize();

    /**
     * 子类实现其他相关初始化操作(由子线程处理，可做耗时初始化操作)
     */
    protected abstract void backgroundInitialize();

    protected abstract EnvironmentUtils.Config.ConfigData buildConfigData();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG, "onCreate----");
        init();
    }

    private void init() {
        sApplication = this;
        mMainProcessInit = new MainProcessInit(this, buildConfigData());
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

    /**
     * 获取Application实例
     * @return BaseApplication
     */
    public static BaseApplication getInstance() {
        return sApplication;
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
