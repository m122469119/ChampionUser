package com.goodchef.liking.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkPagerLoaderRecyclerViewFragment;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.GroupLessonDetailsActivity;
import com.goodchef.liking.activity.GymCoursesActivity;
import com.goodchef.liking.activity.PrivateLessonDetailsActivity;
import com.goodchef.liking.adapter.BannerPagerAdapter;
import com.goodchef.liking.adapter.LikingLessonRecyclerAdapter;
import com.goodchef.liking.eventmessages.LikingHomeNoNetWorkMessage;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.mvp.presenter.HomeCoursesPresenter;
import com.goodchef.liking.mvp.view.HomeCourseView;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.widgets.autoviewpager.InfiniteViewPager;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPageIndicator;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingLessonFragment extends NetworkPagerLoaderRecyclerViewFragment implements HomeCourseView {
    public static final int IMAGE_SLIDER_SWITCH_DURATION = 4000;
    public static final String KEY_GYM_ID = "key_gym_id";
    public static final String KEY_GYM_NAME = "key_gym_name";
    public static final String KEY_DISTANCE = "key_distance";
    public static final String KEY_INTENT_TYPE = "key_intent_type";
    private View mHeadView;
    private InfiniteViewPager mImageViewPager;
    private IconPageIndicator mIconPageIndicator;
    private BannerPagerAdapter mBannerPagerAdapter;
    private View mSliderParentLayout;
    private LikingLessonRecyclerAdapter mLikingLessonRecyclerAdapter;
    private HomeCoursesPresenter mCoursesPresenter;

    private double mLongitude = 0;
    private double mLatitude = 0;
    private String mCityId = "310100";
    private String mDistrictId = "310104";

    public static final String KEY_TRAINER_ID = "trainerId";
    public static final String KEY_SCHEDULE_ID = "scheduleId";
    public static final String KEY_TEACHER_NAME = "teacher_name";
    private static final int TYPE_GROUP_LESSON = 1;//团体课
    private static final int TYPE_PRIVATE_LESSON = 2;//私教课
    private boolean isFirstMessage = false;
    private List<BannerResult.BannerData.Banner> bannerDataList = new ArrayList<>();

    @Override
    protected void requestData(int page) {
        if (isFirstMessage) {
            getCoursesRequest(page);
            requestBanner();
            LogUtils.i("shouye", "shouye");
        }
    }

    @Override
    protected void initViews() {
        initData();
    }

    private void initData() {
        mCoursesPresenter = new HomeCoursesPresenter(getActivity(), this);
        initRecycleView();
        initRecycleHeadView();
        setNoDataView();
        getStateView().findViewById(R.id.text_view_fail_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postEvent(new LikingHomeNoNetWorkMessage());
            }
        });
    }

    private void initRecycleView() {
        setPullType(PullMode.PULL_BOTH);
        mLikingLessonRecyclerAdapter = new LikingLessonRecyclerAdapter(getActivity());
        setRecyclerAdapter(mLikingLessonRecyclerAdapter);
        mLikingLessonRecyclerAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<CoursesResult.Courses.CoursesData> coursesDatas = mLikingLessonRecyclerAdapter.getDataList();
                CoursesResult.Courses.CoursesData coursesData = coursesDatas.get(position);
                int type = coursesData.getType();
                if (type == TYPE_GROUP_LESSON) {
                    Intent intent = new Intent(getActivity(), GroupLessonDetailsActivity.class);
                    intent.putExtra(KEY_SCHEDULE_ID, coursesData.getScheduleId());
                    intent.putExtra(KEY_INTENT_TYPE,"0");
                    startActivity(intent);
                } else if (type == TYPE_PRIVATE_LESSON) {
                    Intent intent = new Intent(getActivity(), PrivateLessonDetailsActivity.class);
                    intent.putExtra(KEY_TRAINER_ID, coursesData.getTrainerId());
                    intent.putExtra(KEY_TEACHER_NAME, coursesData.getCourseName());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
        mLikingLessonRecyclerAdapter.setGroupOnClickListener(mClickListener);
    }


    /***
     * 查看团体课场馆
     */
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.layout_group_lesson);
            if (layout != null) {
                CoursesResult.Courses.CoursesData data = (CoursesResult.Courses.CoursesData) layout.getTag();
                if (data != null) {
                    MobclickAgent.onEvent(getActivity(), UmengEventId.CHECK_GYM_COURSES);
                    Intent intent = new Intent(getActivity(), GymCoursesActivity.class);
                    intent.putExtra(KEY_GYM_ID, data.getGymId());
                    intent.putExtra(KEY_DISTANCE, data.getDistance());
                    intent.putExtra(KEY_GYM_NAME, data.getGymName());
                    startActivity(intent);
                }
            }
        }
    };

    private void initRecycleHeadView() {
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_liking_home_head, getPullToRefreshRecyclerView(), false);
        mSliderParentLayout = mHeadView.findViewById(R.id.layout_liking_home_head);
        mImageViewPager = (InfiniteViewPager) mHeadView.findViewById(R.id.liking_home_head_viewpager);
        mIconPageIndicator = (IconPageIndicator) mHeadView.findViewById(R.id.liking_home_head_indicator);
        initImageSliderLayout();
    }

    private void setNoDataView() {
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.icon_no_coureses_data);
        noDataText.setText(R.string.no_data);
        refreshView.setText(R.string.refresh_btn_text);
        refreshView.setOnClickListener(refreshOnClickListener);
        getStateView().setNodataView(noDataView);
    }

    /***
     * 刷新事件
     */
    private View.OnClickListener refreshOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestBanner();
            loadHomePage();
        }
    };

    private void initImageSliderLayout() {
        mBannerPagerAdapter = new BannerPagerAdapter(getActivity());
        mImageViewPager.setAdapter(mBannerPagerAdapter);
        mImageViewPager.setAutoScrollTime(IMAGE_SLIDER_SWITCH_DURATION);
        mIconPageIndicator.setViewPager(mImageViewPager);
    }

    //发送banner请求
    private void requestBanner() {
        mCoursesPresenter.getBanner();
    }

    //发送首页数据
    private void getCoursesRequest(int page) {
        if (mLongitude > 0 && mLatitude > 0) {
            mCoursesPresenter.getHomeData(mLongitude + "", mLatitude + "", mCityId, mDistrictId, page, LikingLessonFragment.this);
        } else {
            mCoursesPresenter.getHomeData("0", "0", mCityId, mDistrictId, page, LikingLessonFragment.this);
        }
    }


    @Override
    public void updateCourseView(final CoursesResult.Courses courses) {
        List<CoursesResult.Courses.CoursesData> list = courses.getCoursesDataList();
        if (list != null) {
            updateListView(list);
            if (bannerDataList != null && bannerDataList.size() > 0) {
                mLikingLessonRecyclerAdapter.setHeaderView(mHeadView);
                mLikingLessonRecyclerAdapter.notifyDataSetChanged();
            } else {
                removeHeadView();
            }
        }
    }

    @Override
    public void updateBanner(BannerResult.BannerData bannerData) {
        bannerDataList = bannerData.getBannerList();
        if (bannerDataList != null && bannerDataList.size() > 0) {
            mLikingLessonRecyclerAdapter.setHeaderView(mHeadView);
            if (mBannerPagerAdapter != null) {
                mBannerPagerAdapter.setData(bannerData.getBannerList());
                mBannerPagerAdapter.notifyDataSetChanged();
                mIconPageIndicator.notifyDataSetChanged();
            }
            mImageViewPager.setCurrentItem(0);
            mImageViewPager.startAutoScroll();
            mLikingLessonRecyclerAdapter.notifyDataSetChanged();
        } else {
            removeHeadView();
        }

    }


    private void removeHeadView() {
        if (mHeadView != null) {
            getPullToRefreshRecyclerView().removeView(mHeadView);
            mLikingLessonRecyclerAdapter.notifyDataSetChanged();
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
        mLatitude = mainAddressChanged.getLatitude();
        mLongitude = mainAddressChanged.getLongitude();
        mCityId = mainAddressChanged.getCityId();
        mDistrictId = mainAddressChanged.getDistrictId();
        LogUtils.i("dust", "消息传送：" + mLatitude + " -- " + mLongitude + "-- " + mCityId + "--" + mDistrictId);
        isFirstMessage = true;
        requestBanner();
        getCoursesRequest(1);

    }

}
