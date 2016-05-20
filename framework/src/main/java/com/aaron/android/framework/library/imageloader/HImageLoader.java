package com.aaron.android.framework.library.imageloader;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.aaron.android.codelibrary.imageloader.ImageCacheParams;
import com.aaron.android.codelibrary.imageloader.ImageConfig;
import com.aaron.android.codelibrary.imageloader.ImageLoader;
import com.aaron.android.codelibrary.imageloader.ImageLoaderCallback;
import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.BaseApplication;
import com.aaron.android.framework.library.imageloader.Supplier.MemorySupplier;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import java.io.File;

/**
 * 图片加载器，封装Fresco开源框架实现
 * Created on 15/6/14.
 *
 * @author HuangRan
 * TODO requestImage需要修改,现在只能从xml读取图片的相关加载属性
 */
public class HImageLoader implements ImageLoader {

    private HImageConfig mImageConfig;
    private ImageRequest mImageRequest;
    private ImageLoaderCallback mImageLoaderCallback;
    private final BasePostprocessor mBasePostprocessor = new BasePostprocessor() {
        @Override
        public void process(Bitmap bitmap) {
            mImageLoaderCallback.finish(bitmap);
        }
    };

    @Override
    public void initialize(ImageCacheParams params) {
        Fresco.initialize(params.getContext(), initImageLoaderConfig(params));
    }

    private ImagePipelineConfig initImageLoaderConfig(ImageCacheParams params) {
        LogUtils.i("aaron", "imageLoader cache directory: " + BaseApplication.getInstance().getExternalCacheDir());
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(BaseApplication.getInstance())
                .setMainDiskCacheConfig(DiskCacheConfig.newBuilder()
                        .setBaseDirectoryPath(new File(params.getDirectoryPath()))
                        .setBaseDirectoryName(params.getDirectoryName())
                        .setMaxCacheSize(params.getMaxDiskCacheSize())
                        .build())
                .setBitmapMemoryCacheParamsSupplier(new MemorySupplier(params.getMaxMemoryCacheSize()))
                .build();
        return imagePipelineConfig;
    }


    /**
     * 加载图片
     *
     * @param view 图片占位图
     * @param url  加载图片地址
     */
    public void requestImage(HImageView view, String url) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url must be not empty!");
        }
        requestImage(view, Uri.parse(url), null, null);
    }

    /**
     * 加载图片
     *
     * @param view                图片占位图
     * @param url                 加载图片地址
     * @param imageLoaderCallback 图片加载完成后回调(后处理器)
     */
    public void requestImage(HImageView view, String url, ImageConfig imageConfig, ImageLoaderCallback imageLoaderCallback) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url must be not empty!");
        }
        requestImage(view, Uri.parse(url), imageConfig, imageLoaderCallback);
    }

    @Override
    public void requestImage(View view, Uri uri, ImageConfig imageConfig, final ImageLoaderCallback imageLoaderCallback) {
        if (uri == null) {
            throw new NullPointerException("request Uri argument must not be null");
        }
        if (view instanceof HImageView) {
            HImageView hImageView = (HImageView) view;
            buildNewImageConfig(imageConfig);
            buildImageRequest(uri, imageLoaderCallback);
            setImageDraweeViewHierarchyBuilder(hImageView);
            setImageDraweeViewController(hImageView);
        } else {
            throw new IllegalArgumentException("request image fail, it isn't a DraweeView, please check!");
        }
    }

    private void setImageDraweeViewController(HImageView hImageView) {
        hImageView.setController(mImageConfig.getDraweeControllerBuilder()
                .setImageRequest(mImageRequest)
                .setOldController(hImageView
                        .getController()).build());
    }

    private void setImageDraweeViewHierarchyBuilder(HImageView view) {
        if (mImageConfig != null && mImageConfig.getDraweeHierarchyBuilder() != null) {
            view.setHierarchy(mImageConfig.getDraweeHierarchyBuilder().build());
        }
    }

    private void buildNewImageConfig(ImageConfig imageConfig) {
        if (imageConfig == null) {
            mImageConfig = new HImageConfig();
        } else {
            mImageConfig = (HImageConfig) imageConfig;
        }
    }

    private ImageRequest buildImageRequest(Uri uri, final ImageLoaderCallback imageLoaderCallback) {
        mImageLoaderCallback = imageLoaderCallback;
        ImageRequestBuilder mImageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
        Postprocessor postprocessor = mImageLoaderCallback == null ? null : mBasePostprocessor;
        if (postprocessor != null) {
            mImageRequestBuilder.setPostprocessor(postprocessor);
        }
        mImageRequest = mImageRequestBuilder.build();
        return mImageRequest;
    }

}
