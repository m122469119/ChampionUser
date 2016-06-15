package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BannerPagerAdapter;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.widgets.autoviewpager.InfiniteViewPager;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:查看场馆
 * Author shaozucheng
 * Time:16/6/15 下午1:54
 */
public class ArenaActivity extends AppBarActivity {
    public static final int IMAGE_SLIDER_SWITCH_DURATION = 4000;
    private InfiniteViewPager mImageViewPager;
    private IconPageIndicator mIconPageIndicator;
    private BannerPagerAdapter mBannerPagerAdapter;

    private TextView mAddressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arena);
        initView();
        initData();
        requestBanner();
        showHomeUpIcon(R.drawable.app_bar_left_quit);
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);
    }

    private void initData() {
        setTitle("凌空SOHO店");
        mBannerPagerAdapter = new BannerPagerAdapter(this);
        mImageViewPager.setAdapter(mBannerPagerAdapter);
        mImageViewPager.setAutoScrollTime(IMAGE_SLIDER_SWITCH_DURATION);
        mIconPageIndicator.setViewPager(mImageViewPager);
    }

    private void initView() {
        mImageViewPager = (InfiniteViewPager) findViewById(R.id.arena_viewpager);
        mIconPageIndicator = (IconPageIndicator) findViewById(R.id.arena_indicator);
        mAddressTextView = (TextView) findViewById(R.id.arena_address);
    }

    private void requestBanner() {
        List<BannerResult.BannerData.Banner> banners = new ArrayList<>();
        BannerResult.BannerData.Banner banner = new BannerResult.BannerData.Banner();
        banner.setImgUrl("http://bizhi.33lc.com/uploadfile/2014/0911/20140911092615146.jpg");
        banner.setType("2");
        banners.add(banner);

        BannerResult.BannerData.Banner banner1 = new BannerResult.BannerData.Banner();
        banner1.setImgUrl("http://thumbs.dreamstime.com/z/%BD%A1%C9%ED-34080752.jpg");
        banner1.setType("2");
        banners.add(banner1);

        if (mBannerPagerAdapter != null) {
            mBannerPagerAdapter.setData(banners);
            mBannerPagerAdapter.notifyDataSetChanged();
            mIconPageIndicator.notifyDataSetChanged();
        }
        mImageViewPager.setCurrentItem(0);
        mImageViewPager.startAutoScroll();
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

}
