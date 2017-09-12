package com.goodchef.liking.module.paly;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.widgets.video.SlideListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayActivity extends AppBarMVPSwipeBackActivity<VideoPlayContract.Presenter> implements VideoPlayContract.View, SurfaceHolder.Callback {
    public static final String VIDEO_POSTION = "video_position";
    public static final String KEY_IMG = "key_img";
    public static final String KEY_VIDEO = "key_video";
    public static final String KEY_TITLE = "key_title";

    @BindView(R.id.my_SurfaceView)
    SurfaceView mySurfaceView;

    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog = null;

    private ArrayList<String> mImage;
    private ArrayList<String> mVideoList;
    private String mTitle;
    private LinkedHashMap<String, String> mVideoMap = new LinkedHashMap<>();
    private int postion = 0;

    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    private SlideListener mSlideListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        mImage = getIntent().getStringArrayListExtra(KEY_IMG);
        mVideoList = getIntent().getStringArrayListExtra(KEY_VIDEO);
        mTitle = getIntent().getStringExtra(KEY_TITLE);
        postion = getIntent().getIntExtra(VIDEO_POSTION, 0);
        initPlay();
        setTitle(mTitle);

        setRightIcon(R.mipmap.sport_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getVideoShare(VideoPlayActivity.this, mVideoList.get(0), mTitle);
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mImage = getIntent().getStringArrayListExtra(KEY_IMG);
        mVideoList = getIntent().getStringArrayListExtra(KEY_VIDEO);
        mTitle = getIntent().getStringExtra(KEY_TITLE);
        postion = getIntent().getIntExtra(VIDEO_POSTION, 0);
        initPlay();
    }

    private void initPlay() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); // 设置多媒体流类型
        mySurfaceView.getHolder().addCallback(this);
        prepareVideo();
        surfaceViewTouch();
        playerCompletion();
    }


    private void prepareVideo() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 首先取得video的宽和高
                int vWidth = mediaPlayer.getVideoWidth();
                int vHeight = mediaPlayer.getVideoHeight();
                // 该LinearLayout的父容器 android:orientation="vertical" 必须
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_video);
                int lw = linearLayout.getWidth();
                int lh = linearLayout.getHeight();

                if (vWidth > lw || vHeight > lh) {
                    setVideoWindow(vWidth, vHeight, lw, lh);
                } else if (vWidth < lw || vHeight < lh) {
                    // 如果video的宽或者高低于了当前屏幕的大小，则要进行缩放
                    setVideoWindow(vWidth, vHeight, lw, lh);
                }
                //准备完成后播放
                mediaPlayer.start();
                dialogDismiss();
            }


        });
    }

    private void setVideoWindow(int vWidth, int vHeight, float lw, float lh) {
        // 如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
        float wRatio = (float) vWidth / lw;
        float hRatio = (float) vHeight / lh;
        // 选择大的一个进行缩放
        float ratio = Math.max(wRatio, hRatio);
        vWidth = (int) Math.ceil((float) vWidth / ratio);
        vHeight = (int) Math.ceil((float) vHeight / ratio);
        // 设置surfaceView的布局参数
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mySurfaceView.getLayoutParams();
        lp.width = vWidth;
        lp.height = vHeight;
        //(vWidth, vHeight);
        lp.gravity = Gravity.CENTER;
        mySurfaceView.setLayoutParams(lp);
    }

    /**
     * 播放完成
     */
    private void playerCompletion() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtils.i(TAG, "播放完成");
                if (mVideoList.size() == 1) {
                    showToast("播放完成");
                    finish();
                } else {
                    postion++;
                    playNextVideo();
                }
            }
        });
    }


    /**
     * 滑动
     */
    private void surfaceViewTouch() {
        mySurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    x1 = event.getX();
                    y1 = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //当手指离开的时候
                    x2 = event.getX();
                    y2 = event.getY();
                    if (y1 - y2 > 100) {
                        LogUtils.i(TAG, "向上滑");
                        touchUp();
                    } else if (y2 - y1 > 100) {
                        LogUtils.i(TAG, "向下滑");
                        touchDown();
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mediaPlayer.isPlaying()) {
            // 保存当前播放的位置
            postion = mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // go back
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void changeStateView(StateView.State state) {

    }

    @Override
    public void setPresenter() {
        mPresenter = new VideoPlayContract.Presenter();
    }


    public void touchUp() {
        if (mVideoList.size() == 1) {
            return;
        }
       // releasePlayer();
        postion--;
        if (postion > -1) {
            playNextVideo();
        }
    }

    private void playNextVideo() {
        Intent intent = new Intent(this, VideoPlayActivity.class);
        intent.putExtra(VIDEO_POSTION, postion);
        intent.putStringArrayListExtra(KEY_VIDEO, mVideoList);
        startActivity(intent);
    }

    private void releasePlayer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.release();
        }
    }

    public void touchDown() {
        if (mVideoList.size() == 1) {
            return;
        }
      //  releasePlayer();
        postion++;
        if (postion < mVideoList.size()) {
            playNextVideo();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//       mediaPlayer.reset();
        try {
            //设置视屏文件图像的显示参数
            mediaPlayer.setDisplay(holder);
            //uri 网络视频
            mediaPlayer.setDataSource(VideoPlayActivity.this, Uri.parse(mVideoList.get(0)));
            //异步准备 准备工作在子线程中进行 当播放网络视频时候一般采用此方法
            mediaPlayer.prepareAsync();
            showProgressDialog();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void showProgressDialog() {
        progressDialog = showProgressDialog(this, getString(R.string.please_wait), getString(R.string.play_prepare));
        progressDialog.show();
    }

    private void dialogDismiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public ProgressDialog showProgressDialog(Context context, String title, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(true);
        return progressDialog;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.silde_bottom_in, 0);
    }
}
