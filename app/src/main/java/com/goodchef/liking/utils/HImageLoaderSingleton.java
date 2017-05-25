package com.goodchef.liking.utils;


import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;
import com.aaron.android.framework.base.storage.DiskStorageManager;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.imageloader.ImageCacheParams;
import com.aaron.imageloader.ImageConfig;
import com.aaron.imageloader.ImageLoader;
import com.aaron.imageloader.ImageLoaderCallback;
import com.aaron.imageloader.code.HImageLoader;
import com.facebook.common.util.ByteConstants;
import com.goodchef.liking.app.LikingApplication;
import com.goodchef.liking.app.LikingApplicationLike;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.functions.Consumer;

/**
 * 图片加载器单例
 * Created on 15/6/14.
 *
 * @author HuangRan
 */
public class HImageLoaderSingleton {

    private static ImageLoader sImageLoader;

    private static final int MAX_DISK_CACHE_SIZE = 10 * ByteConstants.MB;

    private static ImageLoader getInstance() {
        synchronized (HImageLoaderSingleton.class) {
            if (sImageLoader == null) {
                synchronized (HImageLoaderSingleton.class) {
                    sImageLoader = new HImageLoader();
                    ImageCacheParams imageCacheParams = new ImageCacheParams(LikingApplicationLike.getApp());
                    imageCacheParams.setDirectoryPath(DiskStorageManager.getInstance().getImagePath());
                    imageCacheParams.setDirectoryName(EnvironmentUtils.Config.getAppVersionName());
                    imageCacheParams.setMaxDiskCacheSize(MAX_DISK_CACHE_SIZE);
                    imageCacheParams.setMaxMemoryCacheSize(getMaxCacheSize());
                    sImageLoader.initialize(imageCacheParams);
                }
            }
        }
        return sImageLoader;
    }



    /**
     *
     * @return 获取可用的最大内存
     */
    private static int getMaxCacheSize() {
        final int maxMemory =
                Math.min(((ActivityManager) (LikingApplicationLike.getApp().getSystemService(Context.ACTIVITY_SERVICE))).getMemoryClass() * ByteConstants.MB, Integer.MAX_VALUE);
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
        LogUtils.d("aaron", "memory: " + memory / ByteConstants.MB + "M");
        return memory;
    }


    /**
     * 图片加载
     * @param imageConfig 图片加载配置
     */
    public static void loadImage(final ImageConfig imageConfig, Activity c){
        RxPermissions rxPermissions = new RxPermissions(c);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        getInstance().loadImage(imageConfig);
                    }
                });
    }

    /**
     * 图片加载--网络
     * @param view ImageView
     * @param url 请求url
     * @param imageLoaderCallback 请求回调
     */
    public static void loadImage(final ImageView view, final String url, final ImageLoaderCallback imageLoaderCallback, Activity c){
        RxPermissions rxPermissions = new RxPermissions(c);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        getInstance().loadImage(view, url, imageLoaderCallback);
                    }
                });
    }


    /**
     * 提供一个简单的加载本地resouce资源图片的方法
     * @param view
     * @param res
     */
    public static void loadImage(final ImageView view,final int res, Activity c) {
        RxPermissions rxPermissions = new RxPermissions(c);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        getInstance().loadImage(view, res);
                    }
                });

    }

    /**
     * 提供一个简单的加载网络图片的方法
     * @param view ImageView
     * @param url 请求地址
     */
    public static void loadImage(final ImageView view, final String url, Activity c) {
        RxPermissions rxPermissions = new RxPermissions(c);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        getInstance().loadImage(view, url);
                    }
                });
    }

    /**
     * 根据加载类型和路径加载图片(网络,文件,ContentProvider,Assets)
     * @param view ImageView
     * @param loaderType 请求类型
     * @param path 请求路径
     */
    public static void loadImage(final ImageView view, final ImageLoader.LoaderType loaderType, final String path, Activity c) {
        RxPermissions rxPermissions = new RxPermissions(c);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        getInstance().loadImage(view, loaderType, path);
                    }
                });
    }

    /**
     * 根据加载类型和路径加载图片(网络,文件,ContentProvider,Assets),支持图片回调处理
     * @param view ImageView
     * @param loaderType 请求类型
     * @param path 请求路径
     * @param imageLoaderCallback 请求回调
     */
    public static void loadImage(final ImageView view, final ImageLoader.LoaderType loaderType, final Object path, final ImageLoaderCallback imageLoaderCallback, Activity c){
        RxPermissions rxPermissions = new RxPermissions(c);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        getInstance().loadImage(view, loaderType, path, imageLoaderCallback);
                    }
                });
    }

}
