package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BannerPagerAdapter;
import com.goodchef.liking.dialog.AnnouncementDialog;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.GymDetailsResult;
import com.goodchef.liking.mvp.presenter.GymDetailsPresenter;
import com.goodchef.liking.mvp.view.GymDetailsView;
import com.goodchef.liking.widgets.autoviewpager.InfiniteViewPager;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:查看门店
 * Author shaozucheng
 * Time:16/6/15 下午1:54
 */
public class ArenaActivity extends AppBarActivity implements GymDetailsView, View.OnClickListener {
    public static final int IMAGE_SLIDER_SWITCH_DURATION = 4000;
    private InfiniteViewPager mImageViewPager;
    private IconPageIndicator mIconPageIndicator;
    private BannerPagerAdapter mBannerPagerAdapter;

    private TextView mAddressTextView;
    private TextView mPublicNoticeTextView;
    private String gymId;
    private GymDetailsPresenter mGymDetailsPresenter;
    private RelativeLayout mAnnouncementLayout;
    private LinearLayout mWifiLayout;
    private LinearLayout mWashLayout;
    private LinearLayout mDayLayout;
    private String announcement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arena);
        initView();
        initData();
        showHomeUpIcon(R.drawable.app_bar_left_quit);
    }


    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);
    }

    private void initData() {
        gymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);
        if (!StringUtils.isEmpty(gymId)) {
            sendRequest();
        }
        mBannerPagerAdapter = new BannerPagerAdapter(this);
        mImageViewPager.setAdapter(mBannerPagerAdapter);
        mImageViewPager.setAutoScrollTime(IMAGE_SLIDER_SWITCH_DURATION);
        mIconPageIndicator.setViewPager(mImageViewPager);
    }

    private void sendRequest() {
        mGymDetailsPresenter = new GymDetailsPresenter(this, this);
        mGymDetailsPresenter.getGymDetails(gymId);
    }

    private void initView() {
        mImageViewPager = (InfiniteViewPager) findViewById(R.id.arena_viewpager);
        mIconPageIndicator = (IconPageIndicator) findViewById(R.id.arena_indicator);
        mAddressTextView = (TextView) findViewById(R.id.arena_address);
        mPublicNoticeTextView = (TextView) findViewById(R.id.public_notice);
        mAnnouncementLayout = (RelativeLayout) findViewById(R.id.layout_area_announcement);
        mWifiLayout = (LinearLayout) findViewById(R.id.layout_wifi);
        mWashLayout = (LinearLayout) findViewById(R.id.layout_wash);
        mDayLayout = (LinearLayout) findViewById(R.id.layout_day);
        mAnnouncementLayout.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mImageViewPager != null && mImageViewPager.getChildCount() != 0) {
            mImageViewPager.startAutoScroll();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mImageViewPager != null && mImageViewPager.getChildCount() != 0) {
            mImageViewPager.stopAutoScroll();
        }
    }

    @Override
    public void updateGymDetailsView(GymDetailsResult.GymDetailsData gymDetailsData) {
        setTitle(gymDetailsData.getName());
        mAddressTextView.setText("地点：" + gymDetailsData.getAddress());
        announcement = gymDetailsData.getAnnouncement();
        if (!StringUtils.isEmpty(announcement)) {
            mPublicNoticeTextView.setVisibility(View.VISIBLE);
            mPublicNoticeTextView.setText("公告：" + gymDetailsData.getAnnouncement());
        } else {
            mPublicNoticeTextView.setVisibility(View.GONE);
        }
        List<GymDetailsResult.GymDetailsData.ImgsData> imgDataList = gymDetailsData.getImgs();
        if (imgDataList != null && imgDataList.size() > 0) {
            setBannerView(imgDataList);
        }
        int isWifi = gymDetailsData.getIsWifi();
        int isWash = gymDetailsData.getIsWash();
        int isDay = gymDetailsData.getIsDay();

        if (isWifi == 0) {
            mWifiLayout.setVisibility(View.GONE);
        } else if (isWifi == 1) {
            mWifiLayout.setVisibility(View.VISIBLE);
        }

        if (isWash == 0) {
            mWashLayout.setVisibility(View.GONE);
        } else if (isWash == 1) {
            mWashLayout.setVisibility(View.VISIBLE);
        }

        if (isDay == 0) {
            mDayLayout.setVisibility(View.GONE);
        } else if (isDay == 1) {
            mDayLayout.setVisibility(View.VISIBLE);
        }

    }

    /***
     * 设置banner
     *
     * @param imgDataList 图片集合
     */
    private void setBannerView(List<GymDetailsResult.GymDetailsData.ImgsData> imgDataList) {
        List<BannerResult.BannerData.Banner> banners = new ArrayList<>();
        for (int i = 0; i < imgDataList.size(); i++) {
            BannerResult.BannerData.Banner banner = new BannerResult.BannerData.Banner();
            banner.setImgUrl(imgDataList.get(i).getUrl());
            banner.setType("2");
            banners.add(banner);
        }
        if (mBannerPagerAdapter != null) {
            mBannerPagerAdapter.setData(banners);
            mBannerPagerAdapter.notifyDataSetChanged();
            mIconPageIndicator.notifyDataSetChanged();
        }
        mImageViewPager.setCurrentItem(0);
        mImageViewPager.startAutoScroll();
    }

    @Override
    public void onClick(View v) {
        if (v == mAnnouncementLayout) {
            final AnnouncementDialog dialog = new AnnouncementDialog(this, announcement);
            dialog.setViewOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.announcement_cancel_image_button:
                            dialog.dismiss();
                            break;
                    }
                }
            });
        }
    }
}
