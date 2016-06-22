package com.goodchef.liking.activity;

import android.os.Bundle;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BannerPagerAdapter;
import com.goodchef.liking.fragment.LikingNearbyFragment;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.widgets.autoviewpager.InfiniteViewPager;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/5/26 下午3:00
 */
public class DishesDetailsActivity extends AppBarActivity {
    public static final int IMAGE_SLIDER_SWITCH_DURATION = 4000;
    private InfiniteViewPager mImageViewPager;
    private IconPageIndicator mIconPageIndicator;
    private BannerPagerAdapter mBannerPagerAdapter;

    private String mUserCityId;
    private String mGoodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_details);
        setTitle("XXX菜");

        initView();
        initData();
        requestBanner();
    }

    private void initData() {
        mUserCityId = getIntent().getStringExtra(LikingNearbyFragment.INTENT_KEY_USER_CITY_ID);
        mGoodId = getIntent().getStringExtra(LikingNearbyFragment.INTENT_KEY_GOOD_ID);

        mBannerPagerAdapter = new BannerPagerAdapter(this);
        mImageViewPager.setAdapter(mBannerPagerAdapter);
        mImageViewPager.setAutoScrollTime(IMAGE_SLIDER_SWITCH_DURATION);
        mIconPageIndicator.setViewPager(mImageViewPager);
    }


    private void initView() {
        mImageViewPager = (InfiniteViewPager) findViewById(R.id.liking_home_head_viewpager);
        mIconPageIndicator = (IconPageIndicator) findViewById(R.id.liking_home_head_indicator);
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
