package com.aaron.android.framework.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Environment utility class
 *
 * @author hu.cao
 * @version 1.0.0
 */
public class EnvironmentUtils {
    private static final String TAG = "EnvironmentUtils";
    private static String mPackageName;

    /**
     * 初始化系统环境
     *
     * @param context 系统环境上下文
     */
    public static void init(Context context, Config.ConfigData configData) {
        if (configData != null) {
            Config.init(configData);
        }
        Network.init(context);
        mPackageName = context.getPackageName();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void resetAsyncTaskDefaultExecutor() {
        try {
            ThreadPoolExecutor.DiscardOldestPolicy discardOldestPolicy = new ThreadPoolExecutor.DiscardOldestPolicy();
            final Field defaultHandler = ThreadPoolExecutor.class.getDeclaredField("defaultHandler");
            defaultHandler.setAccessible(true);
            defaultHandler.set(null, discardOldestPolicy);
            if (SDKVersionUtils.hasHoneycomb()) {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) AsyncTask.THREAD_POOL_EXECUTOR;
                threadPoolExecutor.setRejectedExecutionHandler(discardOldestPolicy);
                Method setDefaultExecutorMethod = AsyncTask.class.getMethod("setDefaultExecutor", Executor.class);
                setDefaultExecutorMethod.invoke(null, threadPoolExecutor);
            } else {
                Field sExecutor = AsyncTask.class.getDeclaredField("sExecutor");
                sExecutor.setAccessible(true);
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) sExecutor.get(null);
                threadPoolExecutor.setRejectedExecutionHandler(discardOldestPolicy);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取包名
     *
     * @return 包名
     */
    public static String getPackageName() {
        return mPackageName;
    }

    /**
     * @param packageName 包名
     */
    public static void setPackageName(String packageName) {
        mPackageName = packageName;
    }

    /**
     * @author ran.huang
     * @version 7.0.0
     *          配置信息，来自与assets/config，obtainBuild，channel文件
     */
    public static class Config {
        private static final String TAG = "Config";
        private static String sAppFlag = "";
        private static int sAppVersionCode;
        private static String sAppVersionName = "";
        private static String sHttpRequestUrlHost = "";
        private static boolean sTestMode = false;

        /**
         * 初始化配置文件
         */
        public static void init(ConfigData configData) {
            sAppFlag = configData.getAppFlag();
            sAppVersionCode = configData.getAppVersionCode();
            sAppVersionName = configData.getAppVersionName();
            sTestMode = configData.isTestMode();
            sHttpRequestUrlHost = configData.getUrlHost();
            LogUtils.i(TAG, "AppKey:" + sAppFlag);
            LogUtils.i(TAG, "AppVersionCode:" + sAppVersionCode);
            LogUtils.i(TAG, "AppVersionName:" + sAppVersionName);
            LogUtils.i(TAG, "TestMode:" + sTestMode);
            LogUtils.i(TAG, "HttpRequestUrlHost:" + sHttpRequestUrlHost);
        }


        /**
         * @return 获取应用请求Host
         */

        public static String getHttpRequestUrlHost() {
            return sHttpRequestUrlHost;
        }

        /**
         * 获取程序版本信息
         *
         * @return 程序版本信息
         */
        public static int getAppVersionCode() {
            return sAppVersionCode;
        }

        /**
         * 获取版本类型名称，alpha,beta,release
         *
         * @return 版本类型名称
         */
        public static String getAppVersionName() {
            return sAppVersionName;
        }


        /**
         * 是否让程序在测试模式下运行
         *
         * @return 是否让程序在测试模式下运行
         */
        public static boolean isTestMode() {
            return sTestMode;
        }

        public static String getAppFlag() {
            return sAppFlag;
        }

        public static class ConfigData {
            private String mAppFlag; //应用标识
            private int mAppVersionCode; //应用版本Code,对应AndroidManifest.xml中的versionCode
            private String mAppVersionName; //应用版本名称,对应AndroidManifest.xml中的versionName
            private boolean isTestMode; //是否为测试模式
            private String mUrlHost; //Http请求host url

            public final String getAppFlag() {
                return mAppFlag;
            }

            public final void setAppFlag(String appFlag) {
                mAppFlag = appFlag;
            }

            public final int getAppVersionCode() {
                return mAppVersionCode;
            }

            public final void setAppVersionCode(int appVersionCode) {
                mAppVersionCode = appVersionCode;
            }

            public final String getAppVersionName() {
                return mAppVersionName;
            }

            public final void setAppVersionName(String appVersionName) {
                mAppVersionName = appVersionName;
            }

            public final boolean isTestMode() {
                return isTestMode;
            }

            public final void setIsTestMode(boolean isTestMode) {
                this.isTestMode = isTestMode;
            }

            public final String getUrlHost() {
                return mUrlHost;
            }

            public final void setUrlHost(String urlHost) {
                mUrlHost = urlHost;
            }
        }

    }


    /**
     * 网络信息
     */
    public static class Network {

        private static ConnectivityManager mConnectManager;

        /**
         * 初始化默认网络参数
         *
         * @param context 上下文环境
         */
        public static void init(Context context) {
            mConnectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        /**
         * 是否存在有效的网络连接.
         *
         * @return 存在有效的网络连接返回true，否则返回false
         */
        public static boolean isNetWorkAvailable() {
            return mConnectManager != null && networkConnected(mConnectManager.getActiveNetworkInfo());
        }


        private static boolean networkConnected(NetworkInfo networkInfo) {
            return networkInfo != null && networkInfo.isConnected();
        }

    }
}
