package com.goodchef.liking.fragment;

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
import com.goodchef.liking.R;
import com.goodchef.liking.activity.GroupLessonDetailsActivity;
import com.goodchef.liking.activity.PrivateLessonDetailsActivity;
import com.goodchef.liking.adapter.BannerPagerAdapter;
import com.goodchef.liking.adapter.LinkingLessonRecyclerAdapter;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.mvp.presenter.HomeCoursesPresenter;
import com.goodchef.liking.mvp.view.HomeCourseView;
import com.goodchef.liking.widgets.PullToRefreshRecyclerView;
import com.goodchef.liking.widgets.autoviewpager.InfiniteViewPager;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingLessonFragment extends BaseFragment implements HomeCourseView {
    public static final int IMAGE_SLIDER_SWITCH_DURATION = 4000;
    private View headView;
    private InfiniteViewPager mImageViewPager;
    private IconPageIndicator mIconPageIndicator;
    private BannerPagerAdapter mBannerPagerAdapter;
    private View mSliderParentLayout;
    private PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    //private LikingLessonAdapter mLikingLessonAdapter;
    private LinkingLessonRecyclerAdapter mLinkingLessonRecyclerAdapter;

    private HomeCoursesPresenter mCoursesPresenter;
    private double mLongitude;
    private double mLatitude;
    private String mCityId = "310100";
    private String mDistrictId = "310104";

    public static final String KEY_SCHEDULE_ID="scheduleId";
    private static final int TYPE_GROUP_LESSON = 1;//团体课
    private static final int TYPE_PRIVATE_LESSON = 2;//私教课

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
        mLinkingLessonRecyclerAdapter = new LinkingLessonRecyclerAdapter(getActivity());
        getCoursesRequest();
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


    private void getCoursesRequest() {
        mCoursesPresenter = new HomeCoursesPresenter(getActivity(), this);
        mCoursesPresenter.getHomeData(mLongitude, mLatitude, mCityId, mDistrictId, 1);
    }


    @Override
    public void updateCourseView(CoursesResult.Courses courses) {
        List<CoursesResult.Courses.CoursesData> list = courses.getCoursesDataList();
        if (list != null) {
            mLinkingLessonRecyclerAdapter.setData(list);
            mPullToRefreshRecyclerView.setAdapter(mLinkingLessonRecyclerAdapter);
            mLinkingLessonRecyclerAdapter.setOnItemClickListener(new LinkingLessonRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, CoursesResult.Courses.CoursesData data) {
                    int type = data.getType();
                    if (type == TYPE_GROUP_LESSON) {
                        Intent intent = new Intent(getActivity(), GroupLessonDetailsActivity.class);
                        intent.putExtra(KEY_SCHEDULE_ID,data.getScheduleId());
                        startActivity(intent);
                    } else if (type == TYPE_PRIVATE_LESSON) {
                        Intent intent = new Intent(getActivity(), PrivateLessonDetailsActivity.class);
                        startActivity(intent);
                    }

                }
            });
        }

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
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(MainAddressChanged mainAddressChanged) {
        mLongitude = mainAddressChanged.getLatitude();
        mLatitude = mainAddressChanged.getLatitude();
        // mCityId = mainAddressChanged.getCityId();
        //  mDistrictId = mainAddressChanged.getDistrictId();
    }
}
