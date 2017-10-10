package com.goodchef.liking.module.paly;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.BaseFragment;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.widgets.enviews.ENDownloadView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by aaa on 17/9/16.
 */

public class VideoPlayFragment extends BaseFragment implements SurfaceHolder.Callback {

    // 自动隐藏自定义播放器控制条的时间
    private static final int HIDDEN_TIME = 5000;

//    @BindView(R.id.my_SurfaceView)
//    SurfaceView mySurfaceView;
    @BindView(R.id.layout_video)
    LinearLayout layoutVideo;
    @BindView(R.id.playtime_textView)
    TextView playtimeTextView;
    @BindView(R.id.play_seekbar)
    SeekBar playSeekbar;
    @BindView(R.id.totalTime_textView)
    TextView totalTimeTextView;
    @BindView(R.id.start_pause)
    ImageView startPauseButton;
    @BindView(R.id.layout_controller)
    RelativeLayout layoutController;
    @BindView(R.id.loading)
    ENDownloadView loading;
    @BindView(R.id.layout_loading)
    FrameLayout layoutLoading;


    private MediaPlayer mediaPlayer;
    SurfaceHolder holder = null;
    private int postion = 0;
    private String url;
    private boolean isVisibleToUser;

    private boolean hasShowController = true;
    private Runnable r = new Runnable() {
        @Override
        public void run() {
            // 又回到了主线程
            hasShowController = true;
            showOrHiddenController();
        }
    };

    // 设置定时器
    private Timer timer = null;
    private final static int WHAT = 0;
    private final static int PLAY = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT:
                    if (mediaPlayer != null) {
                        int currentPlayer = mediaPlayer.getCurrentPosition();
                        if (currentPlayer > 0) {
                            mediaPlayer.getCurrentPosition();
                            playtimeTextView.setText(formatTime(currentPlayer));
                            // 让seekBar也跟随改变
                            int progress = (int) ((currentPlayer / (float) mediaPlayer.getDuration()) * 100);
                            playSeekbar.setProgress(progress);
                        } else {
                            playtimeTextView.setText("00:00");
                            playSeekbar.setProgress(0);
                        }
                    }

                    break;
                case PLAY:
                    start();
                    break;

                default:
                    break;
            }
        }
    };

    public static VideoPlayFragment newInstance(String url) {
        Bundle args = new Bundle();
        VideoPlayFragment fragment = new VideoPlayFragment();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_video_play, container, false);
        ButterKnife.bind(this, view);
        url = getArguments().getString("url");
        LogUtils.i(TAG, "onCreateView url =  " + url);
        initPlay();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.i(TAG, isVisibleToUser + "   setUserVisibleHint");
        this.isVisibleToUser = isVisibleToUser;
        if (!isVisibleToUser) {
            pause();
        }
    }

    @OnClick({R.id.start_pause})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_pause:
                if (mediaPlayer.isPlaying()) {
                    pause();
                } else {
                    start();
                }
                break;
        }
    }

    private void start() {
        if (mediaPlayer != null && startPauseButton != null && loading != null) {
            mediaPlayer.start();
            startPauseButton.setBackground(ResourceUtils.getDrawable(R.drawable.video_pause_normal));
            loading.setVisibility(View.GONE);
        }
    }

    private void pause() {
        if (mediaPlayer != null && startPauseButton != null && loading != null) {
            mediaPlayer.pause();
            startPauseButton.setBackground(ResourceUtils.getDrawable(R.drawable.video_play_normal));
            loading.setVisibility(View.GONE);
        }
    }


    private String formatTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    private void initPlay() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); // 设置多媒体流类型
      //  mySurfaceView.getHolder().addCallback(this);
        surfaceViewTouch();
        prepareVideo();
        playerCompletion();
        seekBarListener();
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    LogUtils.i(TAG,"正在缓冲....");
                    layoutLoading.setVisibility(View.VISIBLE);
                    showLoading();
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    layoutLoading.setVisibility(View.GONE);
                    dismissLoading();
                    LogUtils.i(TAG,"缓冲停止....");
                }
                return false;
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initHolder(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void initHolder(SurfaceHolder holder) {
        if (mediaPlayer == null) {
            return;
        }
        this.holder = holder;
        mediaPlayer.reset();
        try {
            //设置视屏文件图像的显示参数
            mediaPlayer.setDisplay(holder);
            //uri 网络视频
            //if (postion < mVideoList.size()) {
            LogUtils.i(TAG, "postion = " + postion + "  url = " + url);
            mediaPlayer.setDataSource(getActivity(), Uri.parse(url));
            //异步准备 准备工作在子线程中进行 当播放网络视频时候一般采用此方法
            mediaPlayer.prepareAsync();
            showLoading();
            layoutLoading.setVisibility(View.VISIBLE);
            //  }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 滑动
     */
    private void surfaceViewTouch() {
//        mySurfaceView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    //当手指按下的时候
//                    showOrHiddenController();
//                }
//                return false;
//            }
//        });
    }


    private void seekBarListener() {
        playSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // 表示手指拖动seekbar完毕，手指离开屏幕会触发以下方法
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 让计时器延时执行
                handler.postDelayed(r, HIDDEN_TIME);
            }

            // 在手指正在拖动seekBar，而手指未离开屏幕触发的方法
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 让计时器取消计时
                handler.removeCallbacks(r);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int playtime = progress * mediaPlayer.getDuration() / 100;
                    mediaPlayer.seekTo(playtime);
                }

            }
        });
    }


    private void prepareVideo() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                layoutLoading.setVisibility(View.GONE);
                // 首先取得video的宽和高
                int vWidth = mediaPlayer.getVideoWidth();
                int vHeight = mediaPlayer.getVideoHeight();
                // 该LinearLayout的父容器 android:orientation="vertical" 必须
                int lw = layoutVideo.getWidth();
                int lh = layoutVideo.getHeight();

                if (vWidth > lw || vHeight > lh) {
                    setVideoWindow(vWidth, vHeight, lw, lh);
                } else if (vWidth < lw || vHeight < lh) {
                    // 如果video的宽或者高低于了当前屏幕的大小，则要进行缩放
                    setVideoWindow(vWidth, vHeight, lw, lh);
                }
                //准备完成后播放
                if (isVisibleToUser) {
                    start();
                }
                // String duration = mediaPlayer.getDuration() ;
                totalTimeTextView.setText(formatTime(mediaPlayer.getDuration()));
                // 初始化定时器
                timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        handler.sendEmptyMessage(WHAT);
                    }
                }, 0, 1000);


                showOrHiddenController();
                dismissLoading();
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
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mySurfaceView.getLayoutParams();
//        lp.width = vWidth;
//        lp.height = vHeight;
//        //(vWidth, vHeight);
//        lp.gravity = Gravity.CENTER;
//        mySurfaceView.setLayoutParams(lp);
    }


    /**
     * 播放完成
     */
    private void playerCompletion() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                postion++;
                LogUtils.i(TAG, "播放完成");
//                if (mVideoList.size() == 1) {
//                    showToast("播放结束");
//                    //finish();
//                } else if (postion < mVideoList.size()) {
//                    playNextVideo();
//                } else {
//                    //finish();
//                    showToast("播放结束");
//                }
            }
        });
    }


    @Override
    public void onPause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
//        mySurfaceView.getHolder().removeCallback(this);
//        mySurfaceView.getHolder().getSurface().release();
        release();
        super.onDestroy();
    }

    private void release() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
//            mySurfaceView.getHolder().removeCallback(this);
            mediaPlayer = null;
        }
    }

    private void showLoading() {
        loading.setVisibility(View.VISIBLE);
        startPauseButton.setVisibility(View.GONE);
        loading.start();
    }

    private void dismissLoading() {
        if (loading != null) {
            loading.reset();
            loading.setVisibility(View.GONE);
        }
    }

    private void showOrHiddenController() {
        if (hasShowController) {
            if (layoutController.getVisibility() == View.VISIBLE) {
                layoutController.setVisibility(View.GONE);
                hasShowController = true;
            } else {
                hasShowController = false;
                layoutController.setVisibility(View.VISIBLE);
                if (loading.getVisibility() == View.GONE) {
                    startPauseButton.setVisibility(View.VISIBLE);
                } else {
                    startPauseButton.setVisibility(View.INVISIBLE);
                }
                // 延时执行
                handler.postDelayed(r, HIDDEN_TIME);
            }
        }
    }
}
