package com.goodchef.liking.module.paly;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.widgets.video.MyGSYPlayView;
import com.goodchef.liking.widgets.video.SlideListener;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayActivity extends AppBarMVPSwipeBackActivity<VideoPlayContract.Presenter> implements VideoPlayContract.View, SlideListener {
    public static final String VIDEO_POSTION = "video_position";
    @BindView(R.id.video_player)
    MyGSYPlayView videoPlayer;


    private ArrayList<String> mImage;
    private ArrayList<String> mVideoList;
    private String mTitle;
    private LinkedHashMap<String, String> mVideoMap = new LinkedHashMap<>();
    private int postion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        hideAppBar();
        ButterKnife.bind(this);
        mImage = getIntent().getStringArrayListExtra(KEY_IMG);
        mVideoList = getIntent().getStringArrayListExtra(KEY_VIDEO);
        mTitle = getIntent().getStringExtra(KEY_TITLE);
        postion = getIntent().getIntExtra(VIDEO_POSTION, 0);

        setTitle(mTitle);

        for (String video : mVideoList) {
            mVideoMap.put(video, video);
        }
        setRightIcon(R.mipmap.sport_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getVideoShare(VideoPlayActivity.this, mVideoList.get(0), mTitle);
            }
        });
        initPlay();
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
        if (mVideoList.size() > 0) {
            videoPlayer.setUp(mVideoList.get(postion), false, "");
            videoPlayer.startPlayLogic();
            videoPlayer.setSlideListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoPlayer != null) {
            videoPlayer.onVideoPause();
        }
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

    private static final String KEY_IMG = "key_img";
    private static final String KEY_VIDEO = "key_video";
    private static final String KEY_TITLE = "key_title";

    public static void launch(Context context, String img, String video, String title, int postion) {
        ArrayList<String> imgs = new ArrayList<>();
        ArrayList<String> videos = new ArrayList<>();
        imgs.add(img);
        videos.add(video);
        launch(context, imgs, videos, title, postion);
    }

    /**
     * @param context
     * @param img     缩略图
     * @param video   视频
     */
    public static void launch(Context context,
                              ArrayList<String> img,
                              ArrayList<String> video,
                              String title,
                              int postion) {
        if (null == context) {
            return;
        }
        if (null == video || video.size() == 0) {
            return;
        }
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putStringArrayListExtra(KEY_IMG, img);
        intent.putStringArrayListExtra(KEY_VIDEO, video);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(VIDEO_POSTION, postion);
        context.startActivity(intent);
    }


    @Override
    public void changeStateView(StateView.State state) {

    }

    @Override
    public void setPresenter() {
        mPresenter = new VideoPlayContract.Presenter();
    }

    @Override
    public void touchUp() {
        if (mVideoList.size() == 1) {
            return;
        }
        releasePlayer();
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
        //释放所有
        if (videoPlayer != null) {
            videoPlayer.setStandardVideoAllCallBack(null);
            videoPlayer.release();
        }
    }

    @Override
    public void touchDown() {
        if (mVideoList.size() == 1) {
            return;
        }
        releasePlayer();
        postion++;
        if (postion < mVideoList.size()) {
            playNextVideo();
        }
    }
}
