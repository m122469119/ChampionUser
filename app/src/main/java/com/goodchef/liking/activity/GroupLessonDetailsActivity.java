package com.goodchef.liking.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.recycleview.RecyclerItemDecoration;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.android.thirdparty.share.weixin.WeixinShare;
import com.aaron.android.thirdparty.share.weixin.WeixinShareData;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.GroupLessonDetailsAdapter;
import com.goodchef.liking.adapter.GroupLessonNumbersAdapter;
import com.goodchef.liking.adapter.SelfHelpCoursesRoomAdapter;
import com.goodchef.liking.dialog.ShareCustomDialog;
import com.goodchef.liking.eventmessages.BuyGroupCoursesAliPayMessage;
import com.goodchef.liking.eventmessages.BuyGroupCoursesWechatMessage;
import com.goodchef.liking.eventmessages.CancelGroupCoursesMessage;
import com.goodchef.liking.eventmessages.CoursesErrorMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.eventmessages.NoCardMessage;
import com.goodchef.liking.eventmessages.OrderGroupMessageSuccess;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.fragment.MyGroupLessonFragment;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.result.data.ShareData;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.GroupCoursesDetailsPresenter;
import com.goodchef.liking.mvp.presenter.SharePresenter;
import com.goodchef.liking.mvp.view.GroupCourserDetailsView;
import com.goodchef.liking.mvp.view.ShareView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.LikingCallUtil;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

/**
 * 说明:团体课可详情
 * Author shaozucheng
 * Time:16/5/24 下午3:21
 */
public class GroupLessonDetailsActivity extends AppBarActivity implements GroupCourserDetailsView, View.OnClickListener, ShareView {
    private static final int COURSES_STATE_NOT_START = 0;// 未开始
    private static final int COURSES_STATE_PROCESS = 1;//进行中
    private static final int COURSES_STATE_OVER = 2;//已结束
    private static final int COURSES_STATE_CANCEL = 3;//已取消
    private static final int COURSES_IS_FREE = 0;//免费团体课
    private static final int COURSES_NOT_FREE = 1;//收费费团体课
    private static final int COURSES_SELF = 2;//自助团体课
    public static final String KEY_NO_CARD = "key_no_card";

    private HImageView mShopImageView;
    //  private TextView mShopNameTextView;//门店名称
    private TextView mScheduleResultTextView;//排期
    private TextView mCoursesTimeTextView;//时间
    private TextView mShopAddressTextView;//地址
    private RelativeLayout mTeacherNamelayout;
    private TextView mTeacherNameTextView;//教练名称
    private RatingBar mRatingBar;//强度
    private TextView mCoursesIntroduceTextView;//课程介绍
    private TextView mJoinUserNumbers;//报名人数
    private RecyclerView  mUserListRecyclerView;//报名人数展示
    private TextView mImmediatelySubmitBtn;//立即购买
    private TextView mGroupCoursesTagTextView;//课程Tag 付费和免费
    private LinearLayout mShareLayout;

    private LinearLayout mCoursesStateLayout;
    private TextView mStatePromptTextView;
    private TextView mCancelOrderBtn;

    private RelativeLayout mGymIntroduceLayout;
    private LinearLayout mGymRootLayout;
    private RecyclerView mRecyclerView;

    private GroupLessonDetailsAdapter mGroupLessonDetailsAdapter;
    private GroupCoursesDetailsPresenter mGroupCoursesDetailsPresenter;
    private String scheduleId;//排期id
    private int mCoursesState = -1;//课程状态
    private String orderId;//订单id
    private String gymId;//场馆id
    private LikingStateView mStateView;
    private String guota;//预约人数
    private int isFree;//是否免费
    private int scheduleType = -1;
    private String price;//价格
    private SharePresenter mSharePresenter;

    private GroupLessonNumbersAdapter mGroupLessonNumbersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_lesson_details);
        setTitle(getString(R.string.title_gruop_detials));
        initData();
    }

    private void initData() {
        scheduleId = getIntent().getStringExtra(LikingLessonFragment.KEY_SCHEDULE_ID);
        mCoursesState = getIntent().getIntExtra(MyGroupLessonFragment.INTENT_KEY_STATE, -1);
        orderId = getIntent().getStringExtra(MyGroupLessonFragment.INTENT_KEY_ORDER_ID);
        gymId = getIntent().getStringExtra(LikingLessonFragment.KEY_GYM_ID);
        initView();
        requestData();
        setRightMenu();
    }

    private void setRightMenu() {
        setRightIcon(R.drawable.icon_phone, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = Preference.getCustomerServicePhone();
                if (!StringUtils.isEmpty(phone)) {
                    LikingCallUtil.showCallDialog(GroupLessonDetailsActivity.this, "确定联系客服吗？", phone);
                }
            }
        });
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.group_courses_details_state_view);
        mShopImageView = (HImageView) findViewById(R.id.group_lesson_details_shop_image);
        //   mShopNameTextView = (TextView) findViewById(R.id.shop_name);
        mScheduleResultTextView = (TextView) findViewById(R.id.schedule_result);
        mCoursesTimeTextView = (TextView) findViewById(R.id.courses_time);
        mShopAddressTextView = (TextView) findViewById(R.id.shop_address);
        mTeacherNamelayout = (RelativeLayout) findViewById(R.id.layout_group_teacher_name);
        mTeacherNameTextView = (TextView) findViewById(R.id.group_teacher_name);
        mRatingBar = (RatingBar) findViewById(R.id.rating_courses);
        mCoursesIntroduceTextView = (TextView) findViewById(R.id.courses_introduce);
        mJoinUserNumbers = (TextView) findViewById(R.id.group_user_numbers);
        mUserListRecyclerView = (RecyclerView) findViewById(R.id.group_user_list_recyclerView);
        mGroupCoursesTagTextView = (TextView) findViewById(R.id.group_courses_tag);
        mShareLayout = (LinearLayout) findViewById(R.id.layout_group_courses_share);

        mGymRootLayout = (LinearLayout) findViewById(R.id.layout_group_details);
        mGymIntroduceLayout = (RelativeLayout) findViewById(R.id.layout_gym_introduce);
        mRecyclerView = (RecyclerView) findViewById(R.id.group_lesson_details_recyclerView);

        mImmediatelySubmitBtn = (TextView) findViewById(R.id.group_immediately_submit_btn);
        mCoursesStateLayout = (LinearLayout) findViewById(R.id.layout_group_state);
        mStatePromptTextView = (TextView) findViewById(R.id.courses_state_prompt);
        mCancelOrderBtn = (TextView) findViewById(R.id.cancel_order_btn);

        setViewOnClickListener();
    }

    private void setViewOnClickListener() {
        mImmediatelySubmitBtn.setOnClickListener(this);
        mGymIntroduceLayout.setOnClickListener(this);
        mGymRootLayout.setOnClickListener(this);
        mShareLayout.setOnClickListener(this);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                requestData();
            }
        });
    }


    /**
     * 设置底部view的状态
     */
    private void setBottomCoursesState() {
        if (mCoursesState == -1) {
            if (isFree == COURSES_IS_FREE) {//免费
                mCoursesStateLayout.setVisibility(View.GONE);
                mImmediatelySubmitBtn.setVisibility(View.VISIBLE);
                mGroupCoursesTagTextView.setText(R.string.free_courses);
                if (!StringUtils.isEmpty(guota)) {
                    if (Integer.parseInt(guota) == 0) {
                        mImmediatelySubmitBtn.setText(R.string.appointment_fill);
                        mImmediatelySubmitBtn.setBackgroundColor(ResourceUtils.getColor(R.color.split_line_color));
                        mImmediatelySubmitBtn.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                        mImmediatelySubmitBtn.setEnabled(false);
                    } else {
                        mImmediatelySubmitBtn.setText(R.string.immadetails_appointment);
                        mImmediatelySubmitBtn.setBackgroundColor(ResourceUtils.getColor(R.color.liking_green_btn_back));
                        mImmediatelySubmitBtn.setTextColor(ResourceUtils.getColor(R.color.white));
                        mImmediatelySubmitBtn.setEnabled(true);
                    }
                } else {
                    mImmediatelySubmitBtn.setText(R.string.immadetails_appointment);
                    mImmediatelySubmitBtn.setBackgroundColor(ResourceUtils.getColor(R.color.liking_green_btn_back));
                    mImmediatelySubmitBtn.setTextColor(ResourceUtils.getColor(R.color.white));
                    mImmediatelySubmitBtn.setEnabled(true);
                }
            } else if (isFree == COURSES_NOT_FREE) {//收费
                mImmediatelySubmitBtn.setVisibility(View.GONE);
                mCoursesStateLayout.setVisibility(View.VISIBLE);
                mGroupCoursesTagTextView.setText(R.string.not_free_group_courses);
                mStatePromptTextView.setTextColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
                mStatePromptTextView.setText("¥ " + price);
                mCancelOrderBtn.setText(R.string.immediately_buy_btn);
                mCancelOrderBtn.setOnClickListener(this);
            }

            if( COURSES_SELF == scheduleType) {
                mGroupCoursesTagTextView.setText(R.string.self_courses);
            }

        } else if (mCoursesState == COURSES_STATE_NOT_START) {//未开始
            mCoursesStateLayout.setVisibility(View.VISIBLE);
            mImmediatelySubmitBtn.setVisibility(View.GONE);
            mStatePromptTextView.setText(R.string.not_start);
            mStatePromptTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
            mCancelOrderBtn.setText(R.string.cancel_appointment);
            mCancelOrderBtn.setVisibility(View.VISIBLE);
            mCancelOrderBtn.setOnClickListener(this);
            mStatePromptTextView.setGravity(Gravity.CENTER | Gravity.LEFT);
        } else if (mCoursesState == COURSES_STATE_PROCESS) {//进行中
            mCoursesStateLayout.setVisibility(View.VISIBLE);
            mImmediatelySubmitBtn.setVisibility(View.GONE);
            mStatePromptTextView.setText(R.string.start_process);
            mStatePromptTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
            mCancelOrderBtn.setVisibility(View.GONE);
            mCancelOrderBtn.setOnClickListener(null);
            mStatePromptTextView.setGravity(Gravity.CENTER);
        } else if (mCoursesState == COURSES_STATE_OVER) {//已结束
            mCoursesStateLayout.setVisibility(View.VISIBLE);
            mImmediatelySubmitBtn.setVisibility(View.GONE);
            mStatePromptTextView.setText(R.string.courses_complete);
            mStatePromptTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
            mCancelOrderBtn.setVisibility(View.GONE);
            mCancelOrderBtn.setOnClickListener(null);
            mStatePromptTextView.setGravity(Gravity.CENTER);
        } else if (mCoursesState == COURSES_STATE_CANCEL) {//已取消
            mCoursesStateLayout.setVisibility(View.VISIBLE);
            mImmediatelySubmitBtn.setVisibility(View.GONE);
            mStatePromptTextView.setText(R.string.courses_cancel);
            mStatePromptTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
            mCancelOrderBtn.setVisibility(View.GONE);
            mCancelOrderBtn.setOnClickListener(null);
            mStatePromptTextView.setGravity(Gravity.CENTER);
        }
    }

    private void requestData() {
        mStateView.setState(StateView.State.LOADING);
        mGroupCoursesDetailsPresenter = new GroupCoursesDetailsPresenter(this, this);
        mGroupCoursesDetailsPresenter.getGroupCoursesDetails(scheduleId);
    }


    @Override
    public void updateGroupLessonDetailsView(GroupCoursesResult.GroupLessonData groupLessonData) {
        if (groupLessonData != null) {
            mStateView.setState(StateView.State.SUCCESS);
            setDetailsData(groupLessonData);
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }
    }

    @Override
    public void updateOrderGroupCourses() {
        postEvent(new OrderGroupMessageSuccess());
        Intent intent = new Intent(this, MyLessonActivity.class);
        intent.putExtra(MyLessonActivity.KEY_CURRENT_ITEM, 0);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void updateErrorNoCard(String errorMessage) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(errorMessage);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.go_buy_card), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(GroupLessonDetailsActivity.this, LikingHomeActivity.class);
                intent.putExtra(LikingHomeActivity.KEY_INTENT_TAB,1);
                startActivity(intent);
                dialog.dismiss();
                GroupLessonDetailsActivity.this.finish();
            }
        });
        builder.create().show();
    }

    /**
     * 设置详情数据
     *
     * @param groupLessonData
     */
    private void setDetailsData(GroupCoursesResult.GroupLessonData groupLessonData) {
        gymId = groupLessonData.getGymId();
        setTitle(groupLessonData.getCourseName());
        List<String> coursesImageList = groupLessonData.getCourseImgs();
        if (coursesImageList != null && coursesImageList.size() > 0) {
            String coursesImageUrl = coursesImageList.get(0);
            if (!StringUtils.isEmpty(coursesImageUrl)) {
                HImageLoaderSingleton.getInstance().loadImage(mShopImageView, coursesImageUrl);
            }
        }
        guota = groupLessonData.getQuota();
        mScheduleResultTextView.setText(groupLessonData.getQuotaDesc());
        //  mShopNameTextView.setText(groupLessonData.getGymName());
        mCoursesTimeTextView.setText(groupLessonData.getCourseDate());
        mShopAddressTextView.setText(groupLessonData.getGymAddress().trim());
        scheduleType = groupLessonData.getScheduleType();
        if(COURSES_SELF == scheduleType) {//如果是自助课程隐藏教练显示
            mTeacherNamelayout.setVisibility(View.GONE);
        } else {
            mTeacherNamelayout.setVisibility(View.VISIBLE);
        }

        String rat = groupLessonData.getIntensity();
        if (!TextUtils.isEmpty(rat)) {
            mRatingBar.setRating(Float.parseFloat(rat));
        }
        mCoursesIntroduceTextView.setText(groupLessonData.getCourseDesc());
        setStadiumImage(groupLessonData.getGymImgs());
        setGroupLessonNumbers(groupLessonData.getGymNumbers());
        isFree = groupLessonData.getIsFree();
        price = groupLessonData.getPrice();
        setBottomCoursesState();
    }

    /**
     * 设置报名会员
     *
     * @param gymNumbersDatas
     */
    private void setGroupLessonNumbers(List<GroupCoursesResult.GroupLessonData.GymNumbersData> gymNumbersDatas){
        mJoinUserNumbers.setText(gymNumbersDatas.size() + " 人");
        mUserListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mGroupLessonNumbersAdapter = new GroupLessonNumbersAdapter(this);
        mUserListRecyclerView.addItemDecoration(new RecyclerItemDecoration(this,LinearLayoutManager.HORIZONTAL));
        mGroupLessonNumbersAdapter.setData(gymNumbersDatas);
        mUserListRecyclerView.setAdapter(mGroupLessonNumbersAdapter);
    }

    private void setStadiumImage(List<GroupCoursesResult.GroupLessonData.GymImgsData> stadiumImageList) {
        if (stadiumImageList != null) {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mGroupLessonDetailsAdapter = new GroupLessonDetailsAdapter(this);
            mGroupLessonDetailsAdapter.setData(stadiumImageList);
            mRecyclerView.setAdapter(mGroupLessonDetailsAdapter);
            mGroupLessonDetailsAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    UMengCountUtil.UmengCount(GroupLessonDetailsActivity.this, UmengEventId.ARENAACTIVITY);
                    Intent intent = new Intent(GroupLessonDetailsActivity.this, ArenaActivity.class);
                    intent.putExtra(LikingLessonFragment.KEY_GYM_ID, gymId);
                    startActivity(intent);
                    overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);
                }

                @Override
                public boolean onItemLongClick(View view, int position) {
                    return false;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mImmediatelySubmitBtn) {
            UMengCountUtil.UmengBtnCount(this, UmengEventId.GROUP_IMMEDIATELY_SUBMIT_BUTTON);
            if (Preference.isLogin()) {
                mGroupCoursesDetailsPresenter.orderGroupCourses(gymId, scheduleId, Preference.getToken());
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } else if (v == mCancelOrderBtn) {//取消预定
            if (isFree == COURSES_IS_FREE) {//免费
                showCancelCoursesDialog();
            } else if (isFree == COURSES_NOT_FREE) {//收费
                if (Preference.isLogin()) {
                    Intent intent = new Intent(this, GroupCoursesChargeConfirmActivity.class);
                    intent.putExtra(LikingLessonFragment.KEY_SCHEDULE_ID, scheduleId);
                    intent.putExtra(LikingLessonFragment.KEY_GYM_ID, gymId);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        } else if (v == mGymIntroduceLayout || v == mGymRootLayout) {//进入门店详情
            UMengCountUtil.UmengCount(GroupLessonDetailsActivity.this, UmengEventId.ARENAACTIVITY);
            Intent intent = new Intent(this, ArenaActivity.class);
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, gymId);
            this.startActivity(intent);
            this.overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);
        } else if (v == mShareLayout) {//分享
            mSharePresenter = new SharePresenter(this, this);
            mSharePresenter.getGroupShareData(scheduleId);
        }
    }


    /**
     * 取消预约团体课
     */
    private void showCancelCoursesDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage("您确定取消预约？");
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendCancelCoursesRequest(orderId);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    //发送取消请求
    private void sendCancelCoursesRequest(String orderId) {
        LiKingApi.cancelGroupCourses(Preference.getToken(), orderId, new RequestUiLoadingCallback<BaseResult>(GroupLessonDetailsActivity.this, R.string.loading_data) {
            @Override
            public void onSuccess(BaseResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(GroupLessonDetailsActivity.this, result)) {
                    PopupUtils.showToast("取消成功");
                    postEvent(new CancelGroupCoursesMessage());
                    mCoursesState = 3;
                    setBottomCoursesState();
                    requestData();
                } else {
                    PopupUtils.showToast(result.getMessage());
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }


    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(BuyGroupCoursesWechatMessage wechatMessage) {
        if (wechatMessage.isPaySuccess()) {
            this.finish();
        }
    }

    public void onEvent(NoCardMessage message){
        this.finish();
    }

    public void onEvent(BuyGroupCoursesAliPayMessage message) {
        this.finish();
    }

    public void onEvent(CoursesErrorMessage message) {
        this.finish();
    }

    public void onEvent(LoginOutFialureMessage message){
        this.finish();
    }

    @Override
    public void updateShareView(ShareData shareData) {
        showShareDialog(shareData);
    }

    private void showShareDialog(final ShareData shareData) {
        final ShareCustomDialog shareCustomDialog = new ShareCustomDialog(this);
        shareCustomDialog.setViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeixinShare weixinShare = new WeixinShare(GroupLessonDetailsActivity.this);
                switch (v.getId()) {
                    case R.id.layout_wx_friend://微信好友
                        WeixinShareData.WebPageData webPageData = new WeixinShareData.WebPageData();
                        webPageData.setWebUrl(shareData.getUrl());
                        webPageData.setTitle(shareData.getTitle());
                        webPageData.setDescription(shareData.getContent());
                        webPageData.setWeixinSceneType(WeixinShareData.WeixinSceneType.FRIEND);
                        webPageData.setIconResId(R.mipmap.ic_launcher);
                        weixinShare.shareWebPage(webPageData);
                        shareCustomDialog.dismiss();
                        break;
                    case R.id.layout_wx_friend_circle://微信朋友圈
                        WeixinShareData.WebPageData webPageData1 = new WeixinShareData.WebPageData();
                        webPageData1.setWebUrl(shareData.getUrl());
                        webPageData1.setTitle(shareData.getTitle());
                        webPageData1.setDescription(shareData.getContent());
                        webPageData1.setWeixinSceneType(WeixinShareData.WeixinSceneType.CIRCLE);
                        webPageData1.setIconResId(R.mipmap.ic_launcher);
                        weixinShare.shareWebPage(webPageData1);
                        shareCustomDialog.dismiss();
                        break;
                    case R.id.cancel_image_button:
                        shareCustomDialog.dismiss();
                        break;
                }
            }
        });
    }
}
