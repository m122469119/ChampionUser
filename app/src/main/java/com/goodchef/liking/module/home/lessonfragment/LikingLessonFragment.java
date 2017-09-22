package com.goodchef.liking.module.home.lessonfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.AppBarNetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.aaron.android.framework.utils.DisplayUtils;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.ListUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.BannerPagerAdapter;
import com.goodchef.liking.adapter.LikingLessonRecyclerAdapter;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.BannerResult;
import com.goodchef.liking.data.remote.retrofit.result.CoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.UnreadMessageResult;
import com.goodchef.liking.data.remote.retrofit.result.data.NoticeData;
import com.goodchef.liking.dialog.CancelOnClickListener;
import com.goodchef.liking.dialog.ConfirmOnClickListener;
import com.goodchef.liking.dialog.DefaultGymDialog;
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
import com.goodchef.liking.eventmessages.PushHasMessage;
import com.goodchef.liking.eventmessages.RefshReadMessage;
import com.goodchef.liking.eventmessages.getGymDataMessage;
import com.goodchef.liking.module.course.group.details.GroupLessonDetailsActivity;
import com.goodchef.liking.module.course.personal.PrivateLessonDetailsActivity;
import com.goodchef.liking.module.course.selfhelp.SelfHelpGroupActivity;
import com.goodchef.liking.module.gym.list.ChangeGymActivity;
import com.goodchef.liking.module.home.LikingHomeActivity;
import com.goodchef.liking.module.message.MessageActivity;
import com.goodchef.liking.module.writeuserinfo.WriteNameActivity;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.ChangeGymUtil;
import com.goodchef.liking.utils.CityUtils;
import com.goodchef.liking.utils.LikingCallUtil;
import com.goodchef.liking.utils.NumberConstantUtil;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.HomeToolBarController;
import com.goodchef.liking.widgets.autoviewpager.InfiniteViewPager;
import com.goodchef.liking.widgets.autoviewpager.indicator.IconPageIndicator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingLessonFragment extends AppBarNetworkSwipeRecyclerRefreshPagerLoaderFragment<LikingLessonContract.Presenter> implements LikingLessonContract.View {
    public static final int IMAGE_SLIDER_SWITCH_DURATION = 4000;
    public static final String KEY_GYM_ID = "key_gym_id";
    public static final String KEY_GYM_NAME = "key_gym_name";
    public static final String KEY_DISTANCE = "key_distance";
    public static final String KEY_INTENT_TYPE = "key_intent_type";
    private android.view.View mHeadView;
    private RelativeLayout mHeadInfiteLayout;
    private InfiniteViewPager mImageViewPager;
    private IconPageIndicator mIconPageIndicator;
    private BannerPagerAdapter mBannerPagerAdapter;
    private LikingLessonRecyclerAdapter mLikingLessonRecyclerAdapter;

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
    private android.view.View mFooterView;
    private LinearLayout mPreSaleView;//有banner 没有课程数据
    private LinearLayout mNoContentData;
    private ImageView mHeadNoDataImageView;//没有数据图片
    private TextView mHeadNoDataTextViewPrompt;//没有数据提示
    private TextView mHeadTelTextView;//休业中显示电话
    private TextView mBuyCardTextView;
    private List<CoursesResult.Courses.CoursesData> list;
    private String presale;
    private String mNoBuinesses = "";
    private HomeToolBarController homeToolBarController;
    private CoursesResult.Courses.Gym mGym;//场馆信息
    private String currentCityName = "";
    private boolean shoDefaultDialog = true;

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
    public View createToolBarLayout() {
        homeToolBarController = new HomeToolBarController(getActivity());
        return homeToolBarController.createToolbarLayout();
    }

    @Override
    protected void initViews() {
        initData();
    }

    private void initData() {
        initRecycleView();
        initBlankView();
        initRecycleHeadView();
        setNoDataView();
        getStateView().findViewById(R.id.text_view_fail_refresh).setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
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
            public void onItemClick(android.view.View view, int position) {
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
            public boolean onItemLongClick(android.view.View view, int position) {
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
        android.view.View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
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
    private android.view.View.OnClickListener refreshOnClickListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            loadHomePage();
        }
    };

    /**
     * 跳转到自助团体课
     */
    private android.view.View.OnClickListener goToSelfCoursesListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            UMengCountUtil.UmengCount(getActivity(), UmengEventId.SELFHELPGROUPACTIVITY);
            startActivity(SelfHelpGroupActivity.class);
        }
    };

    private android.view.View.OnClickListener buyCardOnClickListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
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
        mPresenter.getBanner();
    }

    //发送首页数据
    private void getCoursesRequest(int page) {
        if (!NumberConstantUtil.STR_ZERO.equals(mLongitude) && !NumberConstantUtil.STR_ZERO.equals(mLatitude)) {
            mPresenter.getHomeData(mLongitude, mLatitude, mCityId, mDistrictId, page, LikingHomeActivity.gymId);
        } else {
            mPresenter.getHomeData(NumberConstantUtil.STR_ZERO, NumberConstantUtil.STR_ZERO, mCityId, mDistrictId, page, LikingHomeActivity.gymId);
        }
    }


    /**
     * @param courses
     */
    @Override
    public void updateCourseView(final CoursesResult.Courses courses) {
        if (courses.getGym() != null) {
            CoursesResult.Courses.UserInfo userInfo = courses.getUserInfo();
            UserIsComplete(userInfo);
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
        mGym = courses.getGym();
        LikingHomeActivity.gymTel = mGym.getTel();
        LikingHomeActivity.gymId = mGym.getGymId();
        LikingHomeActivity.defaultGym = mGym.getDefaultGym();
        presale = mGym.getBizStatus();
        mNoBuinesses = mGym.getBizAlert();
        if (NumberConstantUtil.ONE == mGym.getCanSchedule()) {//支持自助团体课
            mSelfCoursesInView.setVisibility(android.view.View.VISIBLE);
        } else {
            mSelfCoursesInView.setVisibility(android.view.View.GONE);
        }

        setToolbarView();
        showDefaultGymDialog();
        mPresenter.getHasMessage();
        LikingPreference.saveGymData(mGym);
        postEvent(new GymNoticeMessage(courses.getGym()));
    }


    /**
     * 弹出默认场馆的对话框
     */
    private boolean showDefaultGymDialog() {
        //无卡，定位失败，并且是默认场馆 ,没有弹出过 满足以上4各条件弹出
        if (!LikingPreference.getUserHasCard() && NumberConstantUtil.ONE == LikingHomeActivity.defaultGym && shoDefaultDialog) {
            if (!LikingHomeActivity.isWhetherLocation) {//定位失败
                shoDefaultDialog = false;
                setDefaultGymDialog(getString(R.string.current_default_gym) + "\n" + "      " + getString(R.string.please_hand_change_gym), true);
            } else {//定位成功，但是定位所在的城市不再我们开通的城市范围内
                if (!CityUtils.isDredge(LikingHomeActivity.cityCode)) {
                    shoDefaultDialog = false;
                    setDefaultGymDialog(getString(R.string.current_default_gym_no_gym) + "\n" + getString(R.string.current_default_gym_location) + "\n" + getString(R.string.please_hand_change_gym), false);
                }
            }
            return false;
        }
        return true;
    }


    /**
     * 显示默认场馆的对话框
     */
    private void setDefaultGymDialog(String text, boolean isDefaultGym) {
        DefaultGymDialog defaultGymDialog = new DefaultGymDialog(getActivity(), DefaultGymDialog.defaultGymType);
        defaultGymDialog.setCancelable(false);
        defaultGymDialog.setCanceledOnTouchOutside(false);
        if (isDefaultGym) {
            defaultGymDialog.setDefaultPromptView(text);
        } else {
            defaultGymDialog.setCurrentCityNotOpen(text);
        }
        defaultGymDialog.setCancelClickListener(new CancelOnClickListener() {
            @Override
            public void onCancelClickListener(AppCompatDialog dialog) {
                dialog.dismiss();
                if (!shoDefaultDialog) {
                    setHomeMenuReadNotice();
                }
            }
        });

        defaultGymDialog.setConfirmClickListener(new ConfirmOnClickListener() {
            @Override
            public void onConfirmClickListener(AppCompatDialog dialog) {
                if (mGym != null) {
                    ChangeGymUtil.changeGym(getActivity(), mGym, 0);
                }
                dialog.dismiss();
            }
        });

    }


//    /**
//     * 切换场馆
//     */
//    private void changeGym(int index) {
//        if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
//            showToast(getString(R.string.network_no_connection));
//            return;
//        }
//        if (mGym != null && !StringUtils.isEmpty(mGym.getGymId()) && !StringUtils.isEmpty(mGym.getCityId())) {
//            if (StringUtils.isEmpty(currentCityName)) {
//                UMengCountUtil.UmengCount(getActivity(), UmengEventId.CHANGE_GYM_ACTIVITY, "定位失败");
//            } else {
//                UMengCountUtil.UmengCount(getActivity(), UmengEventId.CHANGE_GYM_ACTIVITY, currentCityName);
//            }
//            Intent intent = new Intent(getActivity(), ChangeGymActivity.class);
//            intent.putExtra(LikingHomeActivity.KEY_SELECT_CITY_ID, mGym.getCityId());
//            intent.putExtra(LikingHomeActivity.KEY_WHETHER_LOCATION, LikingHomeActivity.isWhetherLocation);
//            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, mGym.getGymId());
//            intent.putExtra(LikingHomeActivity.KEY_TAB_INDEX, index);
//            startActivity(intent);
//        } else {
//            showToast(getString(R.string.network_home_error));
//        }
//    }


    /**
     * 设置是否读取过公告
     */
    private void setHomeMenuReadNotice() {
        if (mGym != null && !StringUtils.isEmpty(mGym.getAnnouncementId())
                && LikingPreference.isIdenticalAnnouncement(mGym.getAnnouncementId())) {
            showNoticeDialog();
        } else {
            homeToolBarController.setHomeNoticePrompt(false);
        }
    }


    /**
     * 设置Toolbar
     */
    private void setToolbarView() {
        if (homeToolBarController == null) return;

        homeToolBarController.getLikingLeftTitleText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGym != null) {
                    ChangeGymUtil.changeGym(getActivity(), mGym, 0);
                }
            }
        });
        homeToolBarController.getLayoutHomeMiddle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGym != null) {
                    ChangeGymUtil.jumpArenaActivity(getActivity(), mGym);
                }
            }
        });

        homeToolBarController.setLikingRightImageView(R.drawable.icon_home_msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MessageActivity.NOTICE_DATA, mGym);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            if (!StringUtils.isEmpty(mGym.getCityName())) {
                homeToolBarController.setLikingLeftTitleText(mGym.getCityName());
            }
            if (!StringUtils.isEmpty(mGym.getName())) {
                homeToolBarController.setLikingMiddleTitleText(mGym.getName());
                homeToolBarController.setLikingDistanceText(mGym.getDistance());
            } else {//当一个（上海）地区所有的店铺关闭时，而它定位在某个地区（上海），后台返回的场馆数据为空
                homeToolBarController.setLikingMiddleTitleText("");
                homeToolBarController.setLikingDistanceText("");
            }
        } else {
            setNotNetWorkMiddleView();
        }
    }


    /**
     * 设置没有网络是中间view的显示
     */
    private void setNotNetWorkMiddleView() {
        LikingHomeActivity.isWhetherLocation = false;
        homeToolBarController.setLikingMiddleTitleText(getString(R.string.title_network_contact_fail));
        homeToolBarController.setLikingDistanceText("");
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
                        mPreSaleView.setVisibility(android.view.View.VISIBLE);
                    } else {
                        mPreSaleView.setVisibility(android.view.View.GONE);
                    }
                    mNoContentData.setVisibility(android.view.View.GONE);
                }
            } else if (!StringUtils.isEmpty(presale) && NumberConstantUtil.STR_TWO.equals(presale)) {//营业中
                if (mPreSaleView != null && mNoContentData != null) {
                    mPreSaleView.setVisibility(android.view.View.GONE);
                    if (isRequestHomePage()) {
                        mNoContentData.setVisibility(android.view.View.VISIBLE);
                        mHeadNoDataImageView.setImageResource(R.drawable.icon_no_coureses_data);
                        mHeadNoDataTextViewPrompt.setText(R.string.no_data);
                        mHeadTelTextView.setVisibility(android.view.View.GONE);
                    } else {
                        mNoContentData.setVisibility(android.view.View.GONE);
                    }
                }
            } else if (!StringUtils.isEmpty(presale) && NumberConstantUtil.STR_THREE.equals(presale)) {//休业中
                if (mPreSaleView != null && mNoContentData != null) {
                    mPreSaleView.setVisibility(android.view.View.GONE);
                    if (isRequestHomePage()) {
                        mNoContentData.setVisibility(android.view.View.VISIBLE);
                        //这个图片要换掉
                        mHeadNoDataImageView.setImageResource(R.drawable.icon_close);
                        mHeadNoDataTextViewPrompt.setText(mNoBuinesses);
                        mHeadTelTextView.setVisibility(android.view.View.VISIBLE);
                        mHeadTelTextView.setText(LikingHomeActivity.gymTel);
                        mHeadTelTextView.setOnClickListener(callListener);
                    } else {
                        mNoContentData.setVisibility(android.view.View.GONE);
                    }
                }
            }
        } else {
            if (mPreSaleView != null && mNoContentData != null) {
                mPreSaleView.setVisibility(android.view.View.GONE);
                mNoContentData.setVisibility(android.view.View.GONE);
            }
        }

    }


    /**
     * 拨打健身房场馆电话
     */
    private android.view.View.OnClickListener callListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            if (!StringUtils.isEmpty(LikingHomeActivity.gymTel)) {
                LikingCallUtil.showCallDialog(getActivity(), getString(R.string.confirm_call), LikingHomeActivity.gymTel);
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

    @Override
    public void updateHasMessage(UnreadMessageResult.UnreadMsgData data) {
        int hasMsg = data.getHasUnreadMsg();
        boolean hasAnnouncement;
        if (mGym != null && !StringUtils.isEmpty(mGym.getAnnouncementId())
                && LikingPreference.isIdenticalAnnouncement(mGym.getAnnouncementId())) {
            hasAnnouncement = true;
        } else {
            hasAnnouncement = false;
        }
        if (hasMsg == 1 || hasAnnouncement) {
            homeToolBarController.setHomeNoticePrompt(true);
        } else {
            homeToolBarController.setHomeNoticePrompt(false);
        }
    }

    @Override
    public void showNoticesDialog(final Set<NoticeData> noticeData) {
        Iterator<NoticeData> iterator = noticeData.iterator();
        NoticeData next;
        if (iterator.hasNext()) {
            next = iterator.next();
            noticeData.remove(next);
        } else {
            return;
        }
        LogUtils.e(TAG, "------------->mNoticeGym.getAnnouncementId() == " + next.getAid());
        if (!LikingPreference.isIdenticalAnnouncement(next.getGym_id())) {
            showNoticesDialog(noticeData);
        }
        UMengCountUtil.UmengBtnCount(getActivity(), UmengEventId.CHECK_ANNOUNCEMENT, currentCityName);
        DefaultGymDialog defaultGymDialog = new DefaultGymDialog(getActivity(), DefaultGymDialog.noticeType);
        defaultGymDialog.setCancelable(true);
        defaultGymDialog.setCanceledOnTouchOutside(true);

        if (!StringUtils.isEmpty(next.getAid())) {
            if (!StringUtils.isEmpty(next.getGymContent())) {
                defaultGymDialog.setNoticesMessage(next.getGymName(), next.getGymContent());
            } else {
                defaultGymDialog.setNoticesMessage(getString(R.string.no_announcement));
            }
            LikingPreference.setAnnouncementId(next.getAid());
        } else if (!StringUtils.isEmpty(next.getGymContent())) {
            defaultGymDialog.setNoticesMessage(next.getGymName(), next.getGymContent());
            LikingPreference.setAnnouncementId(next.getAid());
        } else {
            defaultGymDialog.setNoticesMessage(getString(R.string.no_announcement));
        }
        defaultGymDialog.setConfirmClickListener(new ConfirmOnClickListener() {
            @Override
            public void onConfirmClickListener(AppCompatDialog dialog) {
                dialog.dismiss();
                showNoticesDialog(noticeData);
            }
        });
    }

    private void setBannerData() {
        if (bannerDataList != null && bannerDataList.size() > 0) {
            mHeadInfiteLayout.setVisibility(android.view.View.VISIBLE);
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
            mHeadInfiteLayout.setVisibility(android.view.View.GONE);
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
        setHomeMenuReadNotice();
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
            currentCityName = mainAddressChanged.getCityName();
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
        if (!isFirstMessage) {
            postEvent(new LikingHomeNoNetWorkMessage());
        } else {
            loadHomePage();
        }
    }

    public void onEvent(PushHasMessage message) {
        if (message != null) {
            homeToolBarController.setHomeNoticePrompt(true);
        } else {
            homeToolBarController.setHomeNoticePrompt(false);
        }
    }

    public void onEvent(RefshReadMessage message) {
        if (message != null && mPresenter != null) {
            mPresenter.getHasMessage();
        }
    }

    public void onEvent(getGymDataMessage message) {
        mGym = message.getGym();
        setToolbarView();
        setHomeMenuReadNotice();//切换场馆有公告就弹出公告
    }

    public void onEvent(LikingHomeActivityMessage message) {
        switch (message.what) {
            case LikingHomeActivityMessage.SHOW_PUSH_DIALOG:
                mPresenter.showPushDialog();
                break;
        }
    }


    /**
     * 查看公告
     */
    private DefaultGymDialog showNoticeDialog() {
        UMengCountUtil.UmengBtnCount(getActivity(), UmengEventId.CHECK_ANNOUNCEMENT, currentCityName);
        DefaultGymDialog defaultGymDialog = new DefaultGymDialog(getActivity(), DefaultGymDialog.noticeType);
        defaultGymDialog.setCancelable(true);
        defaultGymDialog.setCanceledOnTouchOutside(true);
        LogUtils.e(TAG, "------------->mNoticeGym.getAnnouncementId() == " + mGym.getAnnouncementId());
        if (!StringUtils.isEmpty(mGym.getAnnouncementId())) {
            if (!StringUtils.isEmpty(mGym.getAnnouncementInfo())) {
                defaultGymDialog.setNoticesMessage(mGym.getName(), mGym.getAnnouncementInfo());
            } else {
                defaultGymDialog.setNoticesMessage(getString(R.string.no_announcement));
            }
            LikingPreference.setAnnouncementId(mGym.getAnnouncementId());
        } else if (!StringUtils.isEmpty(mGym.getAnnouncementInfo())) {
            defaultGymDialog.setNoticesMessage(mGym.getName(), mGym.getAnnouncementInfo());
            LikingPreference.setAnnouncementId(mGym.getAnnouncementId());
        } else {
            defaultGymDialog.setNoticesMessage(getString(R.string.no_announcement));
        }
        defaultGymDialog.setConfirmClickListener(new ConfirmOnClickListener() {
            @Override
            public void onConfirmClickListener(AppCompatDialog dialog) {
                dialog.dismiss();
            }
        });
        return defaultGymDialog;
    }

    @Override
    public void setPresenter() {
        mPresenter = new LikingLessonContract.Presenter();
    }
}
