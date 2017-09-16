package com.goodchef.liking.module.paly;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.ViewPagerFragmentAdapter;
import com.goodchef.liking.widgets.verticalviewpage.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aaa on 17/9/16.
 */

public class VideoListActivity extends BaseActivity {

    @BindView(R.id.verticalViewPage)
    VerticalViewPager mVerticalViewPage;

    private ArrayList<String> mVideoList;
    private String mTitle;
    private int postion = 0;

    ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    FragmentManager mFragmentManager;
    List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        getIntentData();
        initFragmentList();
        initViewPager();
    }

    private void getIntentData() {
        mVideoList = getIntent().getStringArrayListExtra(VideoPlayActivity.KEY_VIDEO);
        mTitle = getIntent().getStringExtra(VideoPlayActivity.KEY_TITLE);
        postion = getIntent().getIntExtra(VideoPlayActivity.VIDEO_POSTION, 0);
        LogUtils.i(TAG, "postion = " + postion);
        setTitle(mTitle);
    }

    /**
     * 初始化fragment集合
     */
    public void initFragmentList() {
        for (int i = 0; i < mVideoList.size(); i++) {
            mFragmentList.add(VideoPlayFragment.newInstance(mVideoList.get(i)));
        }
    }

    public void initViewPager() {
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(mFragmentManager, mFragmentList);
        mVerticalViewPage.setAdapter(mViewPagerFragmentAdapter);
        mVerticalViewPage.setCurrentItem(postion);
        mVerticalViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("滑动", position + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
