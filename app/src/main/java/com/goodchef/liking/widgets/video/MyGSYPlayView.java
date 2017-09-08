package com.goodchef.liking.widgets.video;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.R;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.io.File;
import java.util.Map;

/**
 * Created by aaa on 17/9/8.
 * 自定义视频
 */

public class MyGSYPlayView extends StandardGSYVideoPlayer {

    public static final String TAG = "MyGSYPlayView";
    SlideListener mSlideListener;

    public MyGSYPlayView(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MyGSYPlayView(Context context) {
        super(context);
    }

    public MyGSYPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);//默认比例
        GSYVideoType.enableMediaCodec();//使用硬解
    }

    public void setSlideListener(SlideListener mSlideListener) {
        this.mSlideListener = mSlideListener;
    }

    @Override
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, Map<String, String> mapHeadData, String title) {
        return super.setUp(url, cacheWithPlay, cachePath, mapHeadData, title);
    }

    @Override
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, String title) {
        return super.setUp(url, cacheWithPlay, cachePath, title);
    }

    @Override
    public boolean setUp(String url, boolean cacheWithPlay, String title) {
        return super.setUp(url, cacheWithPlay, title);

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_video_my_custom;
    }


    /**
     * 亮度、进度、音频
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        float x = event.getX();
        float y = event.getY();
        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            onClickUiToggle();
            startDismissControlViewTimer();
            return true;
        }
        if (id == com.shuyu.gsyvideoplayer.R.id.fullscreen) {
            return false;
        }
        if (id == com.shuyu.gsyvideoplayer.R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchSurfaceDown(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - mDownX;
                    float deltaY = y - mDownY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    if ((mIfCurrentIsFullscreen && mIsTouchWigetFull)
                            || (mIsTouchWiget && !mIfCurrentIsFullscreen)) {
                        if (!mChangePosition && !mChangeVolume && !mBrightness) {
                            touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
                        }
                    }
                    touchSurfaceMove(deltaX, deltaY, y);
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    touchSurfaceUp();
                    startProgressTimer();
                    //不要和隐藏虚拟按键后，滑出虚拟按键冲突
                    if (mHideKey && mShowVKey) {
                        return true;
                    }
                    break;
            }
            gestureDetector.onTouchEvent(event);
        } else if (id == com.shuyu.gsyvideoplayer.R.id.progress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelDismissControlViewTimer();
                case MotionEvent.ACTION_MOVE:
                    cancelProgressTimer();
                    ViewParent vpdown = getParent();
                    while (vpdown != null) {
                        vpdown.requestDisallowInterceptTouchEvent(true);
                        vpdown = vpdown.getParent();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    startProgressTimer();
                    ViewParent vpup = getParent();
                    while (vpup != null) {
                        vpup.requestDisallowInterceptTouchEvent(false);
                        vpup = vpup.getParent();
                    }
                    mBrightnessData = -1f;
                    break;
            }
        }
        return false;
    }


    protected void touchSurfaceMove(float deltaX, float deltaY, float y) {
        int curWidth = CommonUtil.getCurrentScreenLand((Activity) getActivityContext()) ? mScreenHeight : mScreenWidth;
        int curHeight = CommonUtil.getCurrentScreenLand((Activity) getActivityContext()) ? mScreenWidth : mScreenHeight;
        if (mChangePosition) {
            int totalTimeDuration = getDuration();
            mSeekTimePosition = (int) (mDownPosition + (deltaX * totalTimeDuration / curWidth) / mSeekRatio);
            if (mSeekTimePosition > totalTimeDuration)
                mSeekTimePosition = totalTimeDuration;
            String seekTime = CommonUtil.stringForTime(mSeekTimePosition);
            String totalTime = CommonUtil.stringForTime(totalTimeDuration);
            showProgressDialog(deltaX, seekTime, mSeekTimePosition, totalTime, totalTimeDuration);
        } else if (deltaY > mScreenHeight / 3) {
            LogUtils.i(TAG, "向上滑动超过40");
            mSlideListener.touchUp();
        } else if (deltaY > -mScreenHeight / 3) {
            LogUtils.i(TAG, "向下滑动超过40");
            mSlideListener.touchDown();
        }
    }

}
