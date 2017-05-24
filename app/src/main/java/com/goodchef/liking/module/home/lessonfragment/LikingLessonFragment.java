package com.goodchef.liking.module.home.lessonfragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.PhoneUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.ListUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BannerPagerAdapter;
import com.goodchef.liking.adapter.LikingLessonRecyclerAdapter;
import com.goodchef.liking.eventmessages.BuyCardMessage;
import com.goodchef.liking.eventmessages.ChangGymMessage;
import com.goodchef.liking.eventmessages.CoursesErrorMessage;
import com.goodchef.liking.eventmessages.GymNoticeMessage;
import com.goodchef.liking.eventmessages.LikingHomeActivityMessage;
import com.goodchef.liking.eventmessages.LikingHomeNoNetWorkMessage;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.eventmessages.LoginOutMessage;
import com.goodchef.liking.eventmessages.MainAddressChanged;
import com.goodchef.liking.eventmessages.OnClickLessonFragmentMessage;
import com.goodchef.liking.http.result.BannerResult;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.module.course.group.details.GroupLessonDetailsActivity;
import com.goodchef.liking.module.course.personal.PrivateLessonDetailsActivity;
import com.goodchef.liking.module.course.selfhelp.SelfHelpGroupActivity;
import com.goodchef.liking.module.data.local.LikingPreference;
import com.goodchef.liking.module.home.LikingHomeActivity;
import com.goodchef.liking.module.writeuserinfo.WriteNameActivity;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.NumberConstantUtil;
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
public class LikingLessonFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment implements LikingLessonContract.LikingLessonView {
    public static final int IMAGE_SLIDER_SWITCH_DURATION = 4000;
    public static final String KEY_GYM_ID = "key_gym_id";
    public static final String KEY_GYM_NAME = "key_gym_name";
    public static final String KEY_DISTANCE = "key_distance";
    public static final String KEY_INTENT_TYPE = "key_intent_type";
    private View mHeadView;
    private RelativeLayout mHeadInfiteLayout;
    private InfiniteViewPager mImageViewPager;
    private IconPageIndicator mIconPageIndicator;
    private BannerPagerAdapter mBannerPagerAdapter;
    private LikingLessonRecyclerAdapter mLikingLessonRecyclerAdapter;
    private LikingLessonContract.LikingLessonPresenter mLikingLessonPresenter;

    private String mLongitude = "0";
    private String mLatitude = "0";
    private String mCityId = "310100";
    private String mDistrictId = "310104";

    public static final String KEY_TRAINER_ID = "trainerId";
    public static final String KEY_SCHEDULE_ID = "scheduleId";
    public static final String KEY_TEACHER_NAME = "teacher_name";
    private static final int TYPE_GROUP_LESSON = 1;//团体课
    private static final int TYPE_PRIVATE_LESSON = 2;//私教课
    private boolean isFirstMessage = false;
    private List<BannerResult.BannerData.Banner> bannerDataList = new ArrayList<>();
    private View mFooterView;
    private LinearLayout mPreSaleView;//有banner 没有课程数据
    private LinearLayout mNoContentData;
    private ImageView mHeadNoDataImageView;//没有数据图片
    private TextView mHeadNoDataTextViewPrompt;//没有数据提示
    private TextView mHeadTelTextView;//休业中显示电话
    private TextView mBuyCardTextView;
    private List<CoursesResult.Courses.CoursesData> list;
    private String presale;
    private String mNoBuinesses = "";

    @Override
    protected void requestData(int page) {
        LogUtils.d(TAG, "swipeRefresh page: " + page);
        loadData(page);
    }

    private void loadData(int page) {
        if (isFirstMessage) {
            getCoursesRequest(page);
            requestBanner();
        } else {
            setLoading(false);
        }
    }

    @Override
    protected void initViews() {
        initData();
    }

    private void initData() {
        mLikingLessonPresenter = new LikingLessonContract.LikingLessonPresenter(getActivity(), this);
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
        getRecyclerView().setBackgroundColor(ResourceUtils.getColor(R.color.app_content_background));
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
                    intent.putExtra(KEY_INTENT_TYPE, NumberConstantUtil.STR_ZERO);
                    startActivity(intent);
                } else if (type == TYPE_PRIVATE_LESSON) {
                    UMengCountUtil.UmengCount(getActivity(), UmengEventId.PRIVATELESSONDETAILSACTIVITY);
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
        mLikingLessonRecyclerAdapter.setGroupOnClickListener(null);
    }

    private LinearLayout mSelfCoursesInView;

    /***
     * chu初始化headView
     */
    private void initRecycleHeadView() {
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_liking_home_head, getRecyclerView(), false);
        mHeadInfiteLayout = (RelativeLayout) mHeadView.findViewById(R.id.layout_InfiniteViewPager);
        mImageViewPager = (InfiniteViewPager) mHeadView.findViewById(R.id.liking_home_head_viewpager);
        mIconPageIndicator = (IconPageIndicator) mHeadView.findViewById(R.id.liking_home_head_indicator);
        mPreSaleView = (LinearLayout) mHeadView.findViewById(R.id.layout_no_content);
        mNoContentData = (LinearLayout) mHeadView.findViewById(R.id.layout_no_data);
        mBuyCardTextView = (TextView) mHeadView.findViewById(R.id.buy_card_TextView);
        mSelfCoursesInView = (LinearLayout) mHeadView.findViewById(R.id.layout_self_courses_view);
        mHeadNoDataImageView = (ImageView) mHeadView.findViewById(R.id.home_no_data_imageView);
        mHeadNoDataTextViewPrompt = (TextView) mHeadView.findViewById(R.id.home_no_data_prompt);
        mHeadTelTextView = (TextView) mHeadView.findViewById(R.id.liking_home_tel);
        mSelfCoursesInView.setOnClickListener(goToSelfCoursesListener);
        mBuyCardTextView.setOnClickListener(buyCardOnClickListener);
        mLikingLessonRecyclerAdapter.addHeaderView(mHeadView);
        initImageSliderLayout();
    }


    private void initBlankView() {
        mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_network_footer, getRecyclerView(), false);
        setNoNextPageFooterView(mFooterView);
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
            loadHomePage();
        }
    };

    /**
     * 跳转到自助团体课
     */
    private View.OnClickListener goToSelfCoursesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UMengCountUtil.UmengCount(getActivity(), UmengEventId.SELFHELPGROUPACTIVITY);
            startActivity(SelfHelpGroupActivity.class);
        }
    };

    private View.OnClickListener buyCardOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            postEvent(new BuyCardMessage());
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
        mLikingLessonPresenter.getBanner();
    }

    //发送首页数据
    private void getCoursesRequest(int page) {
        if (!NumberConstantUtil.STR_ZERO.equals(mLongitude) && !NumberConstantUtil.STR_ZERO.equals(mLatitude)) {
            mLikingLessonPresenter.getHomeData(mLongitude, mLatitude, mCityId, mDistrictId, page, LikingHomeActivity.gymId);
        } else {
            mLikingLessonPresenter.getHomeData(NumberConstantUtil.STR_ZERO, NumberConstantUtil.STR_ZERO, mCityId, mDistrictId, page, LikingHomeActivity.gymId);
        }
    }


    /**
     * @param courses
     */
    @Override
    public void updateCourseView(final CoursesResult.Courses courses) {
        if (courses.getGym() != null) {
            UserIsComplete(courses.getUserInfo());
            doLessonGym(courses);
        }
        list = courses.getCoursesDataList();
        if (list != null && list.size() > 0) {
            setHeadPreSaleView(false, presale);
            updateListView(list);
        } else {
            setTotalPage(getCurrentPage());
            if (isRequestHomePage()) {
                clearContent();
                if (bannerDataList != null && bannerDataList.size() > 0) {
                    setBannerData();
                    setHeadPreSaleView(true, presale);
                    mLikingLessonRecyclerAdapter.removeFooterView(mFooterView);
                } else {
                    setNoDataView();
                }
            } else {
                setHeadPreSaleView(false, presale);
                if (ListUtils.isEmpty(list)) {
                    mLikingLessonRecyclerAdapter.addFooterView(mFooterView);
                } else {
                    mLikingLessonRecyclerAdapter.removeFooterView(mFooterView);
                    mLikingLessonRecyclerAdapter.addData(list);
                }
                mLikingLessonRecyclerAdapter.notifyDataSetChanged();
            }

        }
    }


    /**
     * 处理场馆
     *
     * @param courses
     */
    private void doLessonGym(CoursesResult.Courses courses) {
        CoursesResult.Courses.Gym mGym = courses.getGym();
        LikingHomeActivity.gymTel = mGym.getTel();
        LikingHomeActivity.gymId = mGym.getGymId();
        LikingHomeActivity.defaultGym = mGym.getDefaultGym();
        presale = mGym.getBizStatus();
        mNoBuinesses = mGym.getBizAlert();
        if (NumberConstantUtil.ONE == mGym.getCanSchedule()) {//支持自助团体课
            mSelfCoursesInView.setVisibility(View.VISIBLE);
        } else {
            mSelfCoursesInView.setVisibility(View.GONE);
        }
        postEvent(new GymNoticeMessage(courses.getGym()));
    }

    /**
     * 设置是否完成注册信息
     * 如果没有完成，跳转到填写个人信息界面
     *
     * @param userInfo
     */
    private void UserIsComplete(CoursesResult.Courses.UserInfo userInfo) {
        if (userInfo != null) {
            int userIsComplete = userInfo.getUser_info_complete();
            LogUtils.i(TAG, "userIsComplete ==  " + userIsComplete + "");
            if (userIsComplete == NumberConstantUtil.ONE) {//用户注册信息没有提交完成
                startActivity(WriteNameActivity.class);
            }
        }
    }

    /**
     * 设置预售界面和无数据界面
     *
     * @param hasData
     * @param presale
     */
    private void setHeadPreSaleView(boolean hasData, String presale) {
        if (hasData) {
            if (!StringUtils.isEmpty(presale) && NumberConstantUtil.STR_ONE.equals(presale)) {//预售中
                if (mPreSaleView != null && mNoContentData != null) {
                    if (isRequestHomePage()) {
                        mPreSaleView.setVisibility(View.VISIBLE);
                    } else {
                        mPreSaleView.setVisibility(View.GONE);
                    }
                    mNoContentData.setVisibility(View.GONE);
                }
            } else if (!StringUtils.isEmpty(presale) && NumberConstantUtil.STR_TWO.equals(presale)) {//营业中
                if (mPreSaleView != null && mNoContentData != null) {
                    mPreSaleView.setVisibility(View.GONE);
                    if (isRequestHomePage()) {
                        mNoContentData.setVisibility(View.VISIBLE);
                        mHeadNoDataImageView.setImageResource(R.drawable.icon_no_coureses_data);
                        mHeadNoDataTextViewPrompt.setText(R.string.no_data);
                        mHeadTelTextView.setVisibility(View.GONE);
                    } else {
                        mNoContentData.setVisibility(View.GONE);
                    }
                }
            } else if (!StringUtils.isEmpty(presale) && NumberConstantUtil.STR_THREE.equals(presale)) {//休业中
                if (mPreSaleView != null && mNoContentData != null) {
                    mPreSaleView.setVisibility(View.GONE);
                    if (isRequestHomePage()) {
                        mNoContentData.setVisibility(View.VISIBLE);
                        //这个图片要换掉
                        mHeadNoDataImageView.setImageResource(R.drawable.icon_close);
                        mHeadNoDataTextViewPrompt.setText(mNoBuinesses);
                        mHeadTelTextView.setVisibility(View.VISIBLE);
                        mHeadTelTextView.setText(LikingHomeActivity.gymTel);
                        mHeadTelTextView.setOnClickListener(callListener);
                    } else {
                        mNoContentData.setVisibility(View.GONE);
                    }
                }
            }
        } else {
            if (mPreSaleView != null && mNoContentData != null) {
                mPreSaleView.setVisibility(View.GONE);
                mNoContentData.setVisibility(View.GONE);
            }
        }

    }


    /**
     * 拨打健身房场馆电话
     */
    private View.OnClickListener callListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!StringUtils.isEmpty(LikingHomeActivity.gymTel)) {
                PhoneUtils.phoneCall(getActivity(), LikingHomeActivity.gymTel);
            }
        }
    };


    private void clearContent() {
        if (mLikingLessonRecyclerAdapter != null) {
            mLikingLessonRecyclerAdapter.setData(null);
            mLikingLessonRecyclerAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void updateBanner(BannerResult.BannerData bannerData) {
        bannerDataList = bannerData.getBannerList();
        setBannerData();
    }

    private void setBannerData() {
        if (bannerDataList != null && bannerDataList.size() > 0) {
            mHeadInfiteLayout.setVisibility(View.VISIBLE);
            if (mBannerPagerAdapter != null) {
                mBannerPagerAdapter.setData(bannerDataList);
                mBannerPagerAdapter.notifyDataSetChanged();
                mIconPageIndicator.notifyDataSetChanged();
            }
            mImageViewPager.setCurrentItem(0);
            mImageViewPager.startAutoScroll();
            if (list != null && list.size() > 0) {
                setHeadPreSaleView(false, presale);
            } else {
                if (isRequestHomePage()) {
                    setHeadPreSaleView(false, presale);
                }
            }
        } else {
            mHeadInfiteLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (LikingPreference.isHomeAnnouncement())
            postEvent(LikingHomeActivityMessage.obtain(LikingHomeActivityMessage.SHOW_PUSH_DIALOG));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mImageViewPager != null && mImageViewPager.getChildCount() != 0) {
            mImageViewPager.startAutoScroll();
        }
        LogUtils.i("bbbbb", "onResume()");
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
        try {
            mLatitude = mainAddressChanged.getLatitude();
            mLongitude = mainAddressChanged.getLongitude();
            mCityId = mainAddressChanged.getCityId();
            mDistrictId = mainAddressChanged.getDistrictId();
            LogUtils.i("dust", "消息传送：" + mLatitude + " -- " + mLongitude + "-- " + mCityId + "--" + mDistrictId);
            isFirstMessage = true;
            loadHomePage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onEvent(CoursesErrorMessage message) {
        try {
            loadHomePage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onEvent(ChangGymMessage message) {
        //切换场馆刷新数据
        LogUtils.i(TAG, "---切换场馆啦---");
        loadHomePage();
    }

    public void onEvent(LoginOutMessage message) {
        //登出刷新首页数据
        loadHomePage();
    }

    public void onEvent(LoginFinishMessage message) {
        //登录完成刷新首页数据
        loadHomePage();
    }

    public void onEvent(LoginOutFialureMessage message) {
        loadHomePage();
    }

    public void onEvent(OnClickLessonFragmentMessage message) {
        loadHomePage();
    }

}