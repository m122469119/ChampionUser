package com.chushi007.android.liking.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.aaron.android.framework.base.BaseFragment;
import com.aaron.android.framework.utils.DisplayUtils;
import com.chushi007.android.liking.R;
import com.chushi007.android.liking.activity.GroupLessonDetailsActivity;
import com.chushi007.android.liking.activity.PrivateLessonDetailsActivity;
import com.chushi007.android.liking.adapter.BannerPagerAdapter;
import com.chushi007.android.liking.adapter.LinkingLessonRecyclerAdapter;
import com.chushi007.android.liking.http.result.BannerResult;
import com.chushi007.android.liking.widgets.PullToRefreshRecyclerView;
import com.chushi007.android.liking.widgets.autoviewpager.InfiniteViewPager;
import com.chushi007.android.liking.widgets.autoviewpager.indicator.CirclePageIndicator;
import com.chushi007.android.liking.widgets.autoviewpager.indicator.IconPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingLessonFragment extends BaseFragment {
    public static final int IMAGE_SLIDER_SWITCH_DURATION = 4000;
    private View headView;
    private InfiniteViewPager mImageViewPager;
    private IconPageIndicator mIconPageIndicator;
    private BannerPagerAdapter mBannerPagerAdapter;
    private View mSliderParentLayout;
    private PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    //private LikingLessonAdapter mLikingLessonAdapter;
    private LinkingLessonRecyclerAdapter mLinkingLessonRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liking_lesson, null, false);
        mPullToRefreshRecyclerView = (PullToRefreshRecyclerView) view.findViewById(R.id.listview);
        initData();
        initView();
        return view;
    }

    private void initData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("" + i);
        }
        mLinkingLessonRecyclerAdapter = new LinkingLessonRecyclerAdapter(getActivity());
        mLinkingLessonRecyclerAdapter.setData(list);
        mPullToRefreshRecyclerView.setAdapter(mLinkingLessonRecyclerAdapter);
        mLinkingLessonRecyclerAdapter.setOnItemClickListener(new LinkingLessonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String data) {
                if ((position % 2 == 0)) {
                    Intent intent = new Intent(getActivity(), GroupLessonDetailsActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), PrivateLessonDetailsActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void initView() {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_liking_home_head, mPullToRefreshRecyclerView, false);
        mSliderParentLayout = headView.findViewById(R.id.layout_liking_home_head);
        mImageViewPager = (InfiniteViewPager) headView.findViewById(R.id.liking_home_head_viewpager);
        mIconPageIndicator = (IconPageIndicator) headView.findViewById(R.id.liking_home_head_indicator);
        mLinkingLessonRecyclerAdapter.setHeaderView(headView);
        initImageSliderLayout();
        requestBanner();
        //  setNoDataView();
    }

    private void setNoDataView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_liking_no_data, null, false);
        TextView textView = (TextView) view.findViewById(R.id.no_data_text);
        // getStateView().setNodataView(view);
    }

    private void initImageSliderLayout() {
        //   resizeImageSliderLayout();
        mBannerPagerAdapter = new BannerPagerAdapter(getActivity());
        mImageViewPager.setAdapter(mBannerPagerAdapter);
        mImageViewPager.setAutoScrollTime(IMAGE_SLIDER_SWITCH_DURATION);
        mIconPageIndicator.setViewPager(mImageViewPager);
    }

    private void resizeImageSliderLayout() {
        AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) mSliderParentLayout.getLayoutParams();
        layoutParams.height = (int) (DisplayUtils.getWidthPixels() * 0.4);
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
