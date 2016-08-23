package com.aaron.android.codelibrary.imageloader;

import android.widget.ImageView;

/**
 * Created on 15/6/16.
 *
 * @author ran.huang
 * @version 3.0.1
 */
public abstract class ImageConfig {
    private Object mLoadPath;
    private ImageLoaderCallback mImageLoaderCallback;
    private ImageView mImageView;
    private ImageLoader.LoaderType mLoaderType;

    public ImageConfig(ImageConfigBuilder builder) {
        mLoadPath = builder.getLoadPath();
        mImageView = builder.getImageView();
        mImageLoaderCallback = builder.getImageLoaderCallback();
        mLoaderType = builder.getLoaderType();
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public Object getLoadPath() {
        return mLoadPath;
    }

    public ImageLoader.LoaderType getLoaderType() {
        return mLoaderType;
    }

    public ImageLoaderCallback getImageLoaderCallback() {
        return mImageLoaderCallback;
    }

    public void setImageLoaderCallback(ImageLoaderCallback imageLoaderCallback) {
        mImageLoaderCallback = imageLoaderCallback;
    }

}
