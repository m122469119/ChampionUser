package com.aaron.android.framework.base;

import android.app.Application;

import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.common.utils.LogUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 15/6/2.
 *
 * @author ran.huang
 * @version 3.0.1
 *          <p>
 *          <p>
 *          基类，处理完全退出，系统全局异常等
 */
public abstract class BaseApplication extends Application {

    private static Application sApplication;
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
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                LogUtils.e(TAG, "subscribe thread: " + Thread.currentThread().getName());
                backgroundInitialize();
                e.onNext(new Object());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        initialize();
                        LogUtils.e(TAG, "accept thread: " + Thread.currentThread().getName());
                    }
                });
    }

    /**
     * 获取Application实例
     *
     * @return BaseApplication
     */
    public static Application getInstance() {
        return sApplication;
    }

    /**
     * 退出应用
     */
    public static void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
