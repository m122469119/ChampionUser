package com.aaron.imageloader.code;


import com.aaron.imageloader.ImageLoader;

/**
 * 图片加载器单例
 * Created on 15/6/14.
 *
 * @author HuangRan
 */
public class HImageLoaderSingleton {

    private static ImageLoader sImageLoader;

    public static ImageLoader getInstance() {
        if (sImageLoader == null) {
            sImageLoader = new HImageLoader();
        }
        return sImageLoader;
    }

}
