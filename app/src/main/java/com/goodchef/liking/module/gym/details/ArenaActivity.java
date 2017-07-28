package com.goodchef.liking.module.gym.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.ArenaTagAdapter;
import com.goodchef.liking.adapter.BannerPagerAdapter;
import com.goodchef.liking.data.remote.retrofit.result.BannerResult;
import com.goodchef.liking.data.remote.retrofit.result.GymDetailsResult;
import com.goodchef.liking.dialog.AnnouncementDialog;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.module.map.MapActivity;
import com.goodchef.liking.utils.LikingCallUtil;
import com.goodchef.liking.widgets.autoviewpager.InfiniteViewPager;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:查看门店
 * Author shaozucheng
 * Time:16/6/15 下午1:54
 */
public class ArenaActivity extends AppBarMVPSwipeBackActivity<GymDetailsContract.Presenter> implements GymDetailsContract.View {
    public static final int IMAGE_SLIDER_SWITCH_DURATION = 4000;
    @BindView(R.id.arena_viewpager)
    InfiniteViewPager mImageViewPager;
    @BindView(R.id.arena_indicator)
    IconPageIndicator mIconPageIndicator;
    @BindView(R.id.area_arrow)
    ImageView mAreaArrow;
    @BindView(R.id.public_notice)
    TextView mPublicNotice;
    @BindView(R.id.layout_area_announcement)
    RelativeLayout mAnnouncementLayout;
    @BindView(R.id.arena_address)
    TextView mAddressTextView;
    @BindView(R.id.tag_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.address_map_button)
    ImageView mAddressMapButton;
    private BannerPagerAdapter mBannerPagerAdapter;
    private String announcement;
    private String gymId;//场馆id
    private ArenaTagAdapter mArenaTagAdapter;
    private double longitude;
    private double latitude;
    private String mGymName;//场馆名称
    private String mGymAddress;//场馆地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arena);
        ButterKnife.bind(this);
        initData();
        showHomeUpIcon(R.drawable.app_bar_left_quit);
        setRightMenu();
    }

    private void setRightMenu() {
        setRightIcon(R.drawable.icon_phone, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikingCallUtil.showPhoneDialog(ArenaActivity.this);
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
        mPresenter.getGymDetails(this, gymId);
    }

    @OnClick({R.id.layout_area_announcement, R.id.address_map_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_area_announcement:
                setAnnouncementDialog();
                break;
            case R.id.address_map_button:
                if (longitude != 0d && latitude != 0d) {
                    Intent intent = new Intent(this, MapActivity.class);
                    intent.putExtra(MapActivity.LONGITUDE, longitude);
                    intent.putExtra(MapActivity.LATITUDE, latitude);
                    intent.putExtra(MapActivity.GYM_NAME, mGymName);
                    intent.putExtra(MapActivity.GYM_ADDRESS, mGymAddress);
                    startActivity(intent);
                }
                break;
        }

    }

    private void setAnnouncementDialog() {
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
        longitude = gymDetailsData.getLongitude();
        latitude = gymDetailsData.getLatitude();
        mGymName = gymDetailsData.getName();
        mGymAddress = gymDetailsData.getAddress();

        setTitle(mGymName);
        mAddressTextView.setText(getString(R.string.area_address_left) + mGymAddress);
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
    public void setPresenter() {
        mPresenter = new GymDetailsContract.Presenter();
    }
}
