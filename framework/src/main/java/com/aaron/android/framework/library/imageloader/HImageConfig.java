package com.aaron.android.framework.library.imageloader;

import android.graphics.drawable.Drawable;

import com.aaron.android.codelibrary.imageloader.ImageConfig;
import com.aaron.android.framework.utils.ResourceUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeControllerBuilder;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;

import java.util.List;

/**
 * Created on 15/6/14.
 *
 * @author HuangRan
 */
public class HImageConfig implements ImageConfig {
    private static final ScalingUtils.ScaleType DEFAULT_SCALE_TYPE = ScalingUtils.ScaleType.CENTER_CROP;

    private GenericDraweeHierarchyBuilder mDraweeHierarchyBuilder;
    private AbstractDraweeControllerBuilder mDraweeControllerBuilder;

    public HImageConfig() {
        mDraweeControllerBuilder = Fresco.newDraweeControllerBuilder();
    }

    public GenericDraweeHierarchyBuilder getDraweeHierarchyBuilder() {
        return mDraweeHierarchyBuilder;
    }

    public AbstractDraweeControllerBuilder getDraweeControllerBuilder() {
        return mDraweeControllerBuilder;
    }

    private void checkNullBuildDraweeHierarchy() {
        if (mDraweeHierarchyBuilder == null) {
            mDraweeHierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(ResourceUtils.getResources());
        }
    }

    /**
     * @return 默认图片
     */
    @Override
    public Drawable getDefaultImage() {
        return mDraweeHierarchyBuilder == null ? null : mDraweeHierarchyBuilder.getPlaceholderImage();
    }

    /**
     * 设置默认图片
     * @param defaultDrawable 默认图片
     */
    public void setDefaultImage(Drawable defaultDrawable) {
        checkNullBuildDraweeHierarchy();
        mDraweeHierarchyBuilder.setPlaceholderImage(defaultDrawable, DEFAULT_SCALE_TYPE);
    }

    /**
     * 设置默认图片
     * @param defaultDrawable 默认图片
     */
    public void setDefaultImage(Drawable defaultDrawable, ScalingUtils.ScaleType scaleType) {
        checkNullBuildDraweeHierarchy();
        mDraweeHierarchyBuilder.setPlaceholderImage(defaultDrawable, scaleType);
    }

    /**
     *
     * @return 获取失败时显示的图片
     */
    @Override
    public Drawable getFailureImage() {
        return mDraweeHierarchyBuilder == null ? null : mDraweeHierarchyBuilder.getFailureImage();
    }

    /**
     * 设置失败时显示的图片
     * @param failDrawable 失败时显示的图片
     */
    public void setFailImage(Drawable failDrawable) {
        checkNullBuildDraweeHierarchy();
        mDraweeHierarchyBuilder.setFailureImage(failDrawable, DEFAULT_SCALE_TYPE);
    }

    /**
     * 设置失败时显示的图片
     * @param failDrawable 失败时显示的图片
     */
    public void setFailImage(Drawable failDrawable, ScalingUtils.ScaleType scaleType) {
        checkNullBuildDraweeHierarchy();
        mDraweeHierarchyBuilder.setFailureImage(failDrawable, scaleType);
    }

    /**
     * 加载重试的图片
     * @return 获取加载重试的图片
     */
    public Drawable getRetryImage() {
        return mDraweeHierarchyBuilder == null ? null : mDraweeHierarchyBuilder.getRetryImage();
    }

    /**
     * 设置加载重试的图片
     * @param retryDrawable
     */
    public void setRetryImage(Drawable retryDrawable) {
        checkNullBuildDraweeHierarchy();
        mDraweeHierarchyBuilder.setRetryImage(retryDrawable, DEFAULT_SCALE_TYPE);
    }
    /**
     * 设置加载重试的图片
     * @param retryDrawable
     */
    public void setRetryImage(Drawable retryDrawable, ScalingUtils.ScaleType scaleType) {
        checkNullBuildDraweeHierarchy();
        mDraweeHierarchyBuilder.setRetryImage(retryDrawable, scaleType);
    }

    /**
     * @return 图片加载进度图片
     */
    public Drawable getProgressImage() {
        return mDraweeHierarchyBuilder == null ? null : mDraweeHierarchyBuilder.getProgressBarImage();
    }

    /**
     * 设置图片加载进度图片
     * @param progressDrawable 图片加载进度图片
     */
    public void setProgressImage(Drawable progressDrawable) {
        checkNullBuildDraweeHierarchy();
        mDraweeHierarchyBuilder.setProgressBarImage(progressDrawable, DEFAULT_SCALE_TYPE);
    }
    /**
     * 设置图片加载进度图片
     * @param progressDrawable 图片加载进度图片
     */
    public void setProgressImage(Drawable progressDrawable, ScalingUtils.ScaleType scaleType) {
        checkNullBuildDraweeHierarchy();
        mDraweeHierarchyBuilder.setProgressBarImage(progressDrawable, scaleType);
    }

    /**
     * 获取默认显示图片的ScaleType
     * @return 获取默认显示图片的ScaleType
     */
    public ScalingUtils.ScaleType getDefaultDrawableScaleType() {
        return mDraweeHierarchyBuilder == null ? null : mDraweeHierarchyBuilder.getPlaceholderImageScaleType();
    }

    /**
     * @return 图片ScaleType
     */
    public ScalingUtils.ScaleType getScaleType() {
        return mDraweeHierarchyBuilder == null ? null : mDraweeHierarchyBuilder.getActualImageScaleType();
    }

    /**
     * 设置图片ScaleType
     * @param scaleType ScalingUtils.ScaleType
     */
    public void setScaleType(ScalingUtils.ScaleType scaleType) {
        checkNullBuildDraweeHierarchy();
        mDraweeHierarchyBuilder.setActualImageScaleType(scaleType);
    }

    /**
     * @return 失败图片的ScaleType
     */
    public ScalingUtils.ScaleType getFailDrawableScaleType() {
        return mDraweeHierarchyBuilder == null ? null : mDraweeHierarchyBuilder.getFailureImageScaleType();
    }

    /**
     * @return 家在完成后的渐入时间
     */
    public int getFadeDuration() {
        return mDraweeHierarchyBuilder == null ? 0 : mDraweeHierarchyBuilder.getFadeDuration();
    }

    /**
     * @param fadeDuration
     */
    public void setFadeDuration(int fadeDuration) {
        checkNullBuildDraweeHierarchy();
        mDraweeHierarchyBuilder.setFadeDuration(fadeDuration);
    }

    /**
     * @return
     */
    public ScalingUtils.ScaleType getProgressDrawableScaleType() {
        return mDraweeHierarchyBuilder == null ? null : mDraweeHierarchyBuilder.getProgressBarImageScaleType();
    }

    /**
     * @return
     */
    public ScalingUtils.ScaleType getRetryDrawableScaleType() {
        return mDraweeHierarchyBuilder == null ? null : mDraweeHierarchyBuilder.getRetryImageScaleType();
    }


    /**
     * @return
     */
    public Drawable getPressStateOverlayImage() {
        return mDraweeHierarchyBuilder == null ? null : mDraweeHierarchyBuilder.getPressedStateOverlay();
    }

    /**
     * @param pressStateOverlayDrawable
     */
    public void setPressStateOverlayImage(Drawable pressStateOverlayDrawable) {
        checkNullBuildDraweeHierarchy();
        mDraweeHierarchyBuilder.setPressedStateOverlay(pressStateOverlayDrawable);
    }

    /**
     * @return
     */
    public List<Drawable> getOverlaysImage() {
        return mDraweeHierarchyBuilder == null ? null : mDraweeHierarchyBuilder.getOverlays();
    }

    /**
     * @param overlayDrawable
     */
    public void setOverlayImage(Drawable overlayDrawable) {
        checkNullBuildDraweeHierarchy();
        mDraweeHierarchyBuilder.setOverlay(overlayDrawable);
    }

    /**
     *
     * @return 是否支持重试
     */
    public boolean isRetryEnable() {
        return mDraweeControllerBuilder.getTapToRetryEnabled();
    }

    /**
     * 设置是否支持重试
     * @param retryEnable
     */
    public void setRetryEnable(boolean retryEnable) {
        mDraweeControllerBuilder.setTapToRetryEnabled(retryEnable);
    }

    /**
     * 自动执行gif动画
     * @return
     */
    public boolean isAutoPlayAnimations() {
        return mDraweeControllerBuilder.getAutoPlayAnimations();
    }

    /**
     * 自动执行gif动画
     * @param autoPlayAnimations
     */
    public void setAutoPlayAnimations(boolean autoPlayAnimations) {
        mDraweeControllerBuilder.setAutoPlayAnimations(autoPlayAnimations);
    }

}
