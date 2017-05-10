package com.goodchef.liking.app;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.aaron.imageloader.code.HImageLoaderSingleton;
import com.aaron.android.framework.library.storage.DiskStorageManager;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.imageloader.ImageCacheParams;
import com.facebook.common.util.ByteConstants;

/**
 * Created on 17/2/8.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class MainProcessInit implements ProcessInit {
    private static final String TAG = "MainProcessInit";
    private final Context mContext;
    private final EnvironmentUtils.Config.ConfigData mConfitData;
    private static final int MAX_DISK_CACHE_SIZE = 10 * ByteConstants.MB;

    public MainProcessInit(Context context, EnvironmentUtils.Config.ConfigData configData) {
        mContext = context;
        mConfitData = configData;
    }

    @Override
    public void init() {
        /**环境配置信息初始化*/
        EnvironmentUtils.init(mContext, mConfitData);
        DiskStorageManager.getInstance().init(EnvironmentUtils.Config.getAppFlag());
        /**图片加载器初始化*/
        initImageLoader();
        /**设备信息初始化*/
        DisplayUtils.init(mContext);
        /**根据配置信息判断是否打印日志信息*/
        LogUtils.setEnable(EnvironmentUtils.Config.isTestMode());
        printInfo();
    }

    private void initImageLoader() {
        ImageCacheParams imageCacheParams = new ImageCacheParams(mContext);
        imageCacheParams.setDirectoryPath(DiskStorageManager.getInstance().getImagePath());
        imageCacheParams.setDirectoryName(EnvironmentUtils.Config.getAppVersionName());
        imageCacheParams.setMaxDiskCacheSize(MAX_DISK_CACHE_SIZE);
        imageCacheParams.setMaxMemoryCacheSize(getMaxCacheSize());
        HImageLoaderSingleton.getInstance().initialize(imageCacheParams);
    }

    private void printInfo() {
        LogUtils.d(TAG, "\n---ApkInfo---\nappFlag: " + EnvironmentUtils.Config.getAppFlag() + "\nappVersionName: " + EnvironmentUtils.Config.getAppVersionName() +
                "\nappVersionCode: " + EnvironmentUtils.Config.getAppVersionCode() + "\nappHostUrl: " + EnvironmentUtils.Config.getHttpRequestUrlHost());
        LogUtils.d(TAG, "\n---ScreenInfo---\nscreenWidth: " + DisplayUtils.getWidthPixels() + "\nscreenHeight: " + DisplayUtils.getHeightPixels()
                + "\ndensityDpi: " + DisplayUtils.getDensityDpi() + "\ndensity: " + DisplayUtils.getDensity() + "\ndensityStr: " + DisplayUtils.getBitmapDensityStr());
    }

    /**
     *
     * @return 获取可用的最大内存
     */
    private int getMaxCacheSize() {
        final int maxMemory =
                Math.min(((ActivityManager) (mContext.getSystemService(Context.ACTIVITY_SERVICE))).getMemoryClass() * ByteConstants.MB, Integer.MAX_VALUE);
        int memory;
        if (maxMemory < 32 * ByteConstants.MB) {
            memory = 4 * ByteConstants.MB;
        } else if (maxMemory < 64 * ByteConstants.MB) {
            memory = 6 * ByteConstants.MB;
        } else {
            // We don't want to use more ashmem on Gingerbread for now, since it doesn't respond well to
            // native memory pressure (doesn't throw exceptions, crashes app, crashes phone)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
                memory = 8 * ByteConstants.MB;
            } else {
                memory = maxMemory / 8;
            }
        }
        LogUtils.i("aaron", "memory: " + memory / ByteConstants.MB + "M");
        return memory;
    }
}
