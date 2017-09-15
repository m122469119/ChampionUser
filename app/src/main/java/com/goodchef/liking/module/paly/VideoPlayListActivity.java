package com.goodchef.liking.module.paly;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.widget.ListView;

import com.aaron.android.framework.base.ui.BaseActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.VideoListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aaa on 17/9/15.
 */

public class VideoPlayListActivity extends BaseActivity {


    @BindView(R.id.video_list_RecyclerView)
    RecyclerView videoListRecyclerView;

    private VideoListAdapter mVideoListAdapter;
    private ArrayList<String> mVideoList = new ArrayList<>();
    private ArrayList<String> mImage;
    private String mTitle;
    private int postion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);
        getIntentData();
        initRecycleView();
    }

    private void getIntentData() {
        mImage = getIntent().getStringArrayListExtra(VideoPlayActivity.KEY_IMG);
        mVideoList = getIntent().getStringArrayListExtra(VideoPlayActivity.KEY_VIDEO);
        mTitle = getIntent().getStringExtra(VideoPlayActivity.KEY_TITLE);
        postion = getIntent().getIntExtra(VideoPlayActivity.VIDEO_POSTION, 0);
    }

    private void initRecycleView() {
        videoListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mVideoListAdapter = new VideoListAdapter(this);
        mVideoListAdapter.setData(mVideoList);
        videoListRecyclerView.setAdapter(mVideoListAdapter);
        videoListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        getLastVisiblePosition();
    }

    public int getLastVisiblePosition() {
        RecyclerView.LayoutManager layoutManager = videoListRecyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            int lastItemPosition = linearManager.findLastVisibleItemPosition();
            return lastItemPosition;

        }
        return -1;
    }


}
