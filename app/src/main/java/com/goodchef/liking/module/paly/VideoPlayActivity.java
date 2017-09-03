package com.goodchef.liking.module.paly;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import android.view.View;
import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.ui.actionbar.AppBarSwipeBackActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoPlayActivity extends AppBarMVPSwipeBackActivity<VideoPlayContract.Presenter> implements VideoPlayContract.View {
    private JCVideoPlayerStandard mJCPlayer;

    private ArrayList<String> mImage;
    private ArrayList<String> mVideo;
    private String mTitle;
    private LinkedHashMap<String, String> mVideoMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);


//        ActionBar actionBar = getActionBar();
//        if (null != actionBar) {
//            actionBar.setDisplayHomeAsUpEnabled(true); // 设置返回按钮可见
//            actionBar.setDisplayShowHomeEnabled(true); // 设置是否显示logo图标
//            actionBar.setHomeButtonEnabled(true); // 设置左上角的图标可点击
//        }



        mImage = getIntent().getStringArrayListExtra(KEY_IMG);
        mVideo = getIntent().getStringArrayListExtra(KEY_VIDEO);
        mTitle = getIntent().getStringExtra(KEY_TITLE);

        setTitle(mTitle);

        for (String video: mVideo){
            mVideoMap.put(video, video);
        }



        mJCPlayer = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
        mJCPlayer.setUp(mVideoMap, 0, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
//        mJCPlayer.startWindowFullscreen();
        mJCPlayer.startVideo();
//        mJCPlayer.backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        HImageLoaderSingleton.loadImage(mJCPlayer.thumbImageView, mImage.get(0), this);

        setRightIcon(R.mipmap.sport_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getVideoShare(VideoPlayActivity.this, mVideo.get(0), mTitle);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        JCVideoPlayer.releaseAllVideos();
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
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    private static final String KEY_IMG = "key_img";
    private static final String KEY_VIDEO = "key_video";
    private static final String KEY_TITLE = "key_title";

    public static void launch(Context context, String img, String video, String title) {
        ArrayList<String> imgs = new ArrayList<>();
        ArrayList<String> videos = new ArrayList<>();
        imgs.add(img);
        videos.add(video);
        launch(context, imgs, videos, title);
    }

    /**
     *
     * @param context
     * @param img 缩略图
     * @param video 视频
     */
    public static void launch(Context context,
                              ArrayList<String> img,
                              ArrayList<String> video,
                              String title) {
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

        context.startActivity(intent);
    }

    public static void playTest(Context context) {
        String title = "Test Video";
        // String videoUrl = "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4";
        String videoUrl = "http://video.boohee.cn/chaomo/female/6%E5%8F%A5%E8%AF%9D.mp4";
        String thumbImageUrl = "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640";
      //  launch(context, thumbImageUrl, videoUrl);
    }

    @Override
    public void changeStateView(StateView.State state) {

    }

    @Override
    public void setPresenter() {
        mPresenter = new VideoPlayContract.Presenter();
    }
}
