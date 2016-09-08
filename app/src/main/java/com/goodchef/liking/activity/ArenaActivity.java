package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.ArenaTagAdapter;
import com.goodchef.liking.adapter.BannerPagerAdapter;
import com.goodchef.liking.dialog.AnnouncementDialog;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.GymDetailsResult;
import com.goodchef.liking.mvp.presenter.GymDetailsPresenter;
import com.goodchef.liking.mvp.view.GymDetailsView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.utils.LikingCallUtil;
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
    private TextView mPublicNoticeTextView;//公告
    private RelativeLayout mAnnouncementLayout;
    private RecyclerView mRecyclerView;

    private String announcement;
    private GymDetailsPresenter mGymDetailsPresenter;
    private String gymId;//场馆id

    private ArenaTagAdapter mArenaTagAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arena);
        initView();
        initData();
        showHomeUpIcon(R.drawable.app_bar_left_quit);
        setRightMenu();
    }

    private void setRightMenu() {
        setRightIcon(R.drawable.icon_phone, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = Preference.getCustomerServicePhone();
                if (!StringUtils.isEmpty(phone)) {
                    LikingCallUtil.showCallDialog(ArenaActivity.this, "确定联系客服吗？", phone);
                }
            }
        });
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
        mRecyclerView = (RecyclerView) findViewById(R.id.tag_recyclerView);
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
        announcement = gymDetailsData.getAnnouncement().trim();
        mAnnouncementLayout.setVisibility(View.GONE);

        List<GymDetailsResult.GymDetailsData.ImgsData> imgDataList = gymDetailsData.getImgs();
        if (imgDataList != null && imgDataList.size() > 0) {
            setBannerView(imgDataList);
        }

        List<GymDetailsResult.GymDetailsData.TagData> tagList = gymDetailsData.getTagDataList();
        if (tagList != null) {
            if (tagList.size() == 1) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            } else if (tagList.size() == 2) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            }
            mArenaTagAdapter = new ArenaTagAdapter(this);
            mArenaTagAdapter.setData(gymDetailsData.getTagDataList());
            mRecyclerView.setAdapter(mArenaTagAdapter);
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
