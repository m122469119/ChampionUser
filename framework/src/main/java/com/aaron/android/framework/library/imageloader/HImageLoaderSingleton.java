package com.aaron.android.framework.library.imageloader;

/**
 * 图片加载器单例
 * Created on 15/6/14.
 *
 * @author HuangRan
 */
public class HImageLoaderSingleton {

    private static HImageLoader sImageLoader;

    public static HImageLoader getInstance() {
        if (sImageLoader == null) {
            sImageLoader = new HImageLoader();
        }
        return sImageLoader;
    }
}
