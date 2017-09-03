package com.goodchef.liking.module.paly;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.goodchef.liking.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoPlayActivity extends AppCompatActivity {
    private JCVideoPlayerStandard mJCPlayer;

    private ArrayList<String> mImage;
    private ArrayList<String> mVideo;
    private LinkedHashMap<String, String> mVideoMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        setTitle("SmartSpot训练");

//        ActionBar actionBar = getActionBar();
//        if (null != actionBar) {
//            actionBar.setDisplayHomeAsUpEnabled(true); // 设置返回按钮可见
//            actionBar.setDisplayShowHomeEnabled(true); // 设置是否显示logo图标
//            actionBar.setHomeButtonEnabled(true); // 设置左上角的图标可点击
//        }

        mImage = getIntent().getStringArrayListExtra(KEY_IMG);
        mVideo = getIntent().getStringArrayListExtra(KEY_VIDEO);
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

    public static void launch(Context context, String img, String video) {
        ArrayList<String> imgs = new ArrayList<>();
        ArrayList<String> videos = new ArrayList<>();
        imgs.add(img);
        videos.add(video);
        launch(context, imgs, videos);
    }

    /**
     *
     * @param context
     * @param img 缩略图
     * @param video 视频
     */
    public static void launch(Context context, ArrayList<String> img, ArrayList<String> video) {
        if (null == context) {
            return;
        }
        if (null == video || video.size() == 0) {
            return;
        }
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putStringArrayListExtra(KEY_IMG, img);
        intent.putStringArrayListExtra(KEY_VIDEO, video);

        context.startActivity(intent);
    }

    public static void playTest(Context context) {
        String title = "Test Video";
        // String videoUrl = "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4";
        String videoUrl = "http://video.boohee.cn/chaomo/female/6%E5%8F%A5%E8%AF%9D.mp4";
        String thumbImageUrl = "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640";
        launch(context, thumbImageUrl, videoUrl);
    }
}
