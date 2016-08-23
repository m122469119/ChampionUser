package com.aaron.android.codelibrary.imageloader;

import android.widget.ImageView;

/**
 * 图片加载器
 * Created on 15/6/14.
 *
 * @author HuangRan
 */
public interface ImageLoader {
    /**
     * 初始化操作
     */
    void initialize(ImageCacheParams params);

    /**
     * 图片加载
     * @param imageConfig 图片加载配置
     */
    void loadImage(ImageConfig imageConfig);

    /**
     * 图片加载--网络
     * @param view ImageView
     * @param url 请求url
     * @param imageLoaderCallback 请求回调
     */
    void loadImage(ImageView view, String url, ImageLoaderCallback imageLoaderCallback);

    /**
     * 提供一个简单的加载本地resouce资源图片的方法
     * @param view
     * @param res
     */
    void loadImage(ImageView view, int res);

    /**
     * 提供一个简单的加载网络图片的方法
     * @param view ImageView
     * @param url 请求地址
     */
    void loadImage(ImageView view, String url);

    enum LoaderType {
        NETWORK,
        FILE,
        RESOURCE,
        CONTENT_PROVIDER,
        ASSET,
    }
}
