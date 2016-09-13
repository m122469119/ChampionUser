package com.goodchef.liking.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.aaron.android.framework.utils.DisplayUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.GroupLessonDetailsActivity;
import com.goodchef.liking.activity.PrivateLessonDetailsActivity;
import com.goodchef.liking.adapter.BannerPagerAdapter;
import com.goodchef.liking.adapter.LikingLessonRecyclerAdapter;
import com.goodchef.liking.eventmessages.ChangGymMessage;
import com.goodchef.liking.eventmessages.CoursesErrorMessage;
import com.goodchef.liking.eventmessages.GymNoticeMessage;
import com.goodchef.liking.eventmessages.LikingHomeNoNetWorkMessage;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.eventmessages.LoginOutMessage;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.mvp.presenter.HomeCoursesPresenter;
import com.goodchef.liking.mvp.view.HomeCourseView;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.UMengCountUtil;
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
public class LikingLessonFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment implements HomeCourseView {
    public static final int IMAGE_SLIDER_SWITCH_DURATION = 4000;
    public static final String KEY_GYM_ID = "key_gym_id";
    public static final String KEY_GYM_NAME = "key_gym_name";
    public static final String KEY_DISTANCE = "key_distance";
    public static final String KEY_INTENT_TYPE = "key_intent_type";
    private View mHeadView;
    private View mBlankView;
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
    private String gymId = "0";
    private CoursesResult.Courses.Gym mGym;

    public static final String KEY_TRAINER_ID = "trainerId";
    public static final String KEY_SCHEDULE_ID = "scheduleId";
    public static final String KEY_TEACHER_NAME = "teacher_name";
    private static final int TYPE_GROUP_LESSON = 1;//团体课
    private static final int TYPE_PRIVATE_LESSON = 2;//私教课
    private boolean isFirstMessage = false;
    private List<BannerResult.BannerData.Banner> bannerDataList = new ArrayList<>();


    @Override
    protected void requestData(int page) {
        LogUtils.d(TAG, "swipeRefresh page: " + page);
        if (isFirstMessage) {
            getCoursesRequest(page);
            requestBanner();
            LogUtils.i("shouye", "shouye");
        } else {
            setLoading(false);
        }
    }

    @Override
    protected void initViews() {
        initData();
    }

    private void initData() {
        mCoursesPresenter = new HomeCoursesPresenter(getActivity(), this);
        initRecycleView();
        initBlankView();
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
        setPullMode(PullMode.PULL_BOTH);
        mLikingLessonRecyclerAdapter = new LikingLessonRecyclerAdapter(getActivity());
        setRecyclerAdapter(mLikingLessonRecyclerAdapter);
        setRecyclerViewPadding(0, 0, 0, DisplayUtils.dp2px(10));
        mLikingLessonRecyclerAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<CoursesResult.Courses.CoursesData> coursesDatas = mLikingLessonRecyclerAdapter.getDataList();
                CoursesResult.Courses.CoursesData coursesData = coursesDatas.get(position);
                int type = coursesData.getType();
                if (type == TYPE_GROUP_LESSON) {
                    UMengCountUtil.UmengCount(getActivity(), UmengEventId.GROUPLESSONDETAILSACTIVITY);
                    Intent intent = new Intent(getActivity(), GroupLessonDetailsActivity.class);
                    intent.putExtra(KEY_SCHEDULE_ID, coursesData.getScheduleId());
                    intent.putExtra(KEY_INTENT_TYPE, "0");
                    intent.putExtra(KEY_GYM_ID, mGym.getGymId());
                    startActivity(intent);
                } else if (type == TYPE_PRIVATE_LESSON) {
                    UMengCountUtil.UmengCount(getActivity(), UmengEventId.PRIVATELESSONDETAILSACTIVITY);
                    Intent intent = new Intent(getActivity(), PrivateLessonDetailsActivity.class);
                    intent.putExtra(KEY_TRAINER_ID, coursesData.getTrainerId());
                    intent.putExtra(KEY_TEACHER_NAME, coursesData.getCourseName());
                    intent.putExtra(KEY_GYM_ID, mGym.getGymId());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
        mLikingLessonRecyclerAdapter.setGroupOnClickListener(null);
    }


    /***
     * chu初始化headView
     */
    private void initRecycleHeadView() {
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_liking_home_head, getRecyclerView(), false);
        mSliderParentLayout = mHeadView.findViewById(R.id.layout_liking_home_head);
        mImageViewPager = (InfiniteViewPager) mHeadView.findViewById(R.id.liking_home_head_viewpager);
        mIconPageIndicator = (IconPageIndicator) mHeadView.findViewById(R.id.liking_home_head_indicator);
        initImageSliderLayout();
    }


    private void initBlankView() {
        mBlankView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_blank_view, getRecyclerView(), false);
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
            mCoursesPresenter.getHomeData(mLongitude + "", mLatitude + "", mCityId, mDistrictId, page, gymId, LikingLessonFragment.this);
        } else {
            mCoursesPresenter.getHomeData("0", "0", mCityId, mDistrictId, page, gymId, LikingLessonFragment.this);
        }
    }


    @Override
    public void updateCourseView(final CoursesResult.Courses courses) {
        if (courses.getGym() != null) {
            mGym = courses.getGym();
            postEvent(new GymNoticeMessage(courses.getGym()));
        }
        List<CoursesResult.Courses.CoursesData> list = courses.getCoursesDataList();
        if (list != null) {
            updateListView(list);
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
            setBlankView();
        }

    }

    private void setBlankView() {
        mLikingLessonRecyclerAdapter.setHeaderView(mBlankView);
        mLikingLessonRecyclerAdapter.notifyDataSetChanged();
    }


    private void removeHeadView() {
        if (mHeadView != null) {
            mLikingLessonRecyclerAdapter.setHeaderView(null);
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
        loadHomePage();
    }

    public void onEvent(CoursesErrorMessage message) {
        gymId = "0";
        loadHomePage();
    }

    public void onEvent(ChangGymMessage message) {
        //切换场馆刷新数据
        gymId = message.getGymId();
        int index = message.getIndex();
        // if (index == 0) {//从首页切换场馆过来不用刷新界面
        loadHomePage();
        // }
    }

    public void onEvent(LoginOutMessage message) {
        //登出刷新首页数据
        gymId = "0";
        loadHomePage();
    }

    public void onEvent(LoginFinishMessage message) {
        //登录完成刷新首页数据
        gymId = "0";
        loadHomePage();
    }

}
