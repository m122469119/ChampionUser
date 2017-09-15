package com.goodchef.liking.module.paly;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.WifiMessage;
import com.goodchef.liking.service.NetBroadcastReceiver;
import com.goodchef.liking.utils.NetUtil;
import com.goodchef.liking.widgets.enviews.ENDownloadView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoPlayActivity extends AppBarMVPSwipeBackActivity<VideoPlayContract.Presenter> implements VideoPlayContract.View, SurfaceHolder.Callback {
    public static final String VIDEO_POSTION = "video_position";
    public static final String KEY_IMG = "key_img";
    public static final String KEY_VIDEO = "key_video";
    public static final String KEY_TITLE = "key_title";

    // 自动隐藏自定义播放器控制条的时间
    private static final int HIDDEN_TIME = 5000;

    @BindView(R.id.my_SurfaceView)
    SurfaceView mySurfaceView;
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


    private ArrayList<String> mImage;
    private ArrayList<String> mVideoList;
    private String mTitle;
    private int postion = 0;
    private int currentPosition;

    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;


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

                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);
        mImage = getIntent().getStringArrayListExtra(KEY_IMG);
        mVideoList = getIntent().getStringArrayListExtra(KEY_VIDEO);
        mTitle = getIntent().getStringExtra(KEY_TITLE);
        postion = getIntent().getIntExtra(VIDEO_POSTION, 0);
        registerNet();
        initPlay();
        setTitle(mTitle);

        setRightIcon(R.mipmap.sport_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getVideoShare(VideoPlayActivity.this, mVideoList.get(0), mTitle);
            }
        });

        VideoView videoView = new VideoView(this);
        videoView.setMediaController(new MediaController(this));

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


    @OnClick({R.id.start_pause})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_pause:
                if (mediaPlayer.isPlaying()) {
                    currentPosition = mediaPlayer.getCurrentPosition();
                    pause();
                } else {
                    start();
                }
                break;
        }
    }

    private String formatTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    private void initPlay() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); // 设置多媒体流类型
        mySurfaceView.getHolder().addCallback(this);
        prepareVideo();
        surfaceViewTouch();
        playerCompletion();
        seekBarListener();
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    showLoading();
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    dismissLoading();
                }
                return false;
            }
        });
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
                postion++;
                LogUtils.i(TAG, "播放完成");
                if (mVideoList.size() == 1) {
                    showToast("播放结束");
                    finish();
                } else if (postion < mVideoList.size()) {
                    playNextVideo();
                }else {
                    finish();
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
                    showOrHiddenController();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //当手指离开的时候
                    x2 = event.getX();
                    y2 = event.getY();
                    if (y1 - y2 > 150) {
                        LogUtils.i(TAG, "向上滑");
                        touchUp();
                    } else if (y2 - y1 > 150) {
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
            currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mySurfaceView.getHolder().getSurface().release();
        }
        super.onDestroy();
    }


    @Override
    public void changeStateView(StateView.State state) {

    }

    @Override
    public void setPresenter() {
        mPresenter = new VideoPlayContract.Presenter();
    }


    /**
     * 向下滑动播放上一个视频
     */
    public void touchDown() {
        if (mVideoList.size() == 1 || postion <= 0) {
            return;
        }
        postion--;
        if (postion > -1) {
            playNextVideo();
        }
    }

    /**
     * 向上滑动播放下一个视频
     */
    public void touchUp() {
        if (mVideoList.size() == 1 || postion >= mVideoList.size()) {
            return;
        }
        postion++;
        if (postion < mVideoList.size()) {
            playNextVideo();
        }
    }

    private void playNextVideo() {
        Intent intent = new Intent(this, VideoPlayActivity.class);
        intent.putExtra(VIDEO_POSTION, postion);
        intent.putStringArrayListExtra(KEY_VIDEO, mVideoList);
        intent.putExtra(KEY_TITLE, mTitle);
        startActivity(intent);
        finish();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.reset();
        try {
            //设置视屏文件图像的显示参数
            mediaPlayer.setDisplay(holder);
            //uri 网络视频
            if (postion < mVideoList.size()) {
                mediaPlayer.setDataSource(VideoPlayActivity.this, Uri.parse(mVideoList.get(postion)));
                //异步准备 准备工作在子线程中进行 当播放网络视频时候一般采用此方法
                mediaPlayer.prepareAsync();
                //  showProgressDialog(getString(R.string.play_prepare));
                showLoading();
                layoutLoading.setVisibility(View.VISIBLE);
            }
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.silde_bottom_in, 0);
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

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    private void start() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            startPauseButton.setBackground(ResourceUtils.getDrawable(R.drawable.video_pause_normal));
            loading.setVisibility(View.GONE);
        }
    }

    private void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            startPauseButton.setBackground(ResourceUtils.getDrawable(R.drawable.video_play_normal));
            loading.setVisibility(View.GONE);
        }
    }

    /**
     * 当前处于的网络
     * -1 ：网络不可用
     * 0 ：2G/3G/4G
     * 1：wifi
     */
    public void onEvent(WifiMessage message) {
        if (message != null) {
            int wifiState = message.getWifiState();
            if (wifiState == NetUtil.NETWORK_NONE) {
                pause();
                showWifiDialog(getString(R.string.network_no_work));
                showOrHiddenController();
            } else if (wifiState == NetUtil.NETWORK_MOBILE) {
                pause();
                showWifiDialog(getString(R.string.tips_not_wifi));
            } else if (wifiState == NetUtil.NETWORK_WIFI) {
                dismissWifiDialog();
                start();
            }
        }
    }

    private HBaseDialog dialog;

    public void showWifiDialog(String string) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_one_content, null, false);
        TextView mTitleTextView = (TextView) view.findViewById(R.id.one_dialog_title);
        TextView mTextView = (TextView) view.findViewById(R.id.one_dialog_content);
        mTextView.setText(string);
        mTitleTextView.setText("提示");
        builder.setCustomView(view);
        if (string.equals(getString(R.string.network_no_work))) {
            builder.setNegativeButton(R.string.dialog_know, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pause();
                    dialog.dismiss();
                }
            });
        } else {
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(getString(R.string.continues), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mediaPlayer.start();
                    dialog.dismiss();
                }
            });
        }
        dialog = builder.create();
        dialog.show();
    }


    private void dismissWifiDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    /**
     * 注册网络
     */
    private void registerNet() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetBroadcastReceiver(), filter);
    }


}
