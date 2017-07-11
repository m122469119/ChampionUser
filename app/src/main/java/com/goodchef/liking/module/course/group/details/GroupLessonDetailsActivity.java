package com.goodchef.liking.module.course.group.details;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.recycleview.RecyclerItemDecoration;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.utils.ResourceUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.GroupLessonDetailsAdapter;
import com.goodchef.liking.adapter.GroupLessonNumbersAdapter;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.GroupCoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.data.ShareData;
import com.goodchef.liking.eventmessages.BuyGroupCoursesAliPayMessage;
import com.goodchef.liking.eventmessages.BuyGroupCoursesWechatMessage;
import com.goodchef.liking.eventmessages.CancelGroupCoursesMessage;
import com.goodchef.liking.eventmessages.CoursesErrorMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.eventmessages.NoCardMessage;
import com.goodchef.liking.eventmessages.OrderGroupMessageSuccess;
import com.goodchef.liking.module.course.MyLessonActivity;
import com.goodchef.liking.module.course.group.MyGroupLessonFragment;
import com.goodchef.liking.module.course.group.details.charge.GroupCoursesChargeConfirmActivity;
import com.goodchef.liking.module.gym.details.ArenaActivity;
import com.goodchef.liking.module.home.LikingHomeActivity;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.module.login.LoginActivity;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.goodchef.liking.utils.LikingCallUtil;
import com.goodchef.liking.utils.ShareUtils;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:团体课可详情
 * Author shaozucheng
 * Time:16/5/24 下午3:21
 */

public class GroupLessonDetailsActivity extends AppBarMVPSwipeBackActivity<TeamCourseDetailsContract.Presenter> implements TeamCourseDetailsContract.View {

    private static final int COURSES_STATE_NOT_START = 0;// 未开始
    private static final int COURSES_STATE_PROCESS = 1;//进行中
    private static final int COURSES_STATE_OVER = 2;//已结束
    private static final int COURSES_STATE_CANCEL = 3;//已取消
    private static final int COURSES_IS_FREE = 0;//免费团体课
    private static final int COURSES_NOT_FREE = 1;//收费费团体课
    private static final int COURSES_SELF = 2;//自助团体课

    @BindView(R.id.group_courses_details_state_view)
    LikingStateView mStateView;
    @BindView(R.id.group_lesson_details_shop_image)
    HImageView mShopImageView;
    @BindView(R.id.group_courses_tag)
    TextView mGroupCoursesTagTextView;//课程Tag 付费和免费
    @BindView(R.id.layout_group_courses_share)
    LinearLayout mShareLayout;
    @BindView(R.id.schedule_result)
    TextView mScheduleResultTextView;//排期
    @BindView(R.id.courses_time)
    TextView mCoursesTimeTextView;//时间
    @BindView(R.id.shop_place)
    TextView mShopPlaceTextView;//地点-场馆
    @BindView(R.id.shop_address)
    TextView mShopAddressTextView;//地址
    @BindView(R.id.layout_group_teacher_name)
    RelativeLayout mTeacherNamelayout;
    @BindView(R.id.rating_courses)
    RatingBar mRatingBar;//强度
    @BindView(R.id.courses_introduce)
    TextView mCoursesIntroduceTextView;//课程介绍
    @BindView(R.id.group_user_numbers)
    TextView mJoinUserNumbers;//报名人数
    @BindView(R.id.group_user_list_recyclerView)
    RecyclerView mUserListRecyclerView;//报名人数展示
    @BindView(R.id.layout_gym_introduce)
    RelativeLayout mGymIntroduceLayout;
    @BindView(R.id.group_lesson_details_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.layout_group_details)
    LinearLayout mGymRootLayout;
    @BindView(R.id.group_immediately_submit_btn)
    TextView mImmediatelySubmitBtn;//立即购买
    @BindView(R.id.courses_state_prompt)
    TextView mStatePromptTextView;
    @BindView(R.id.cancel_order_btn)
    TextView mCancelOrderBtn;//取消预订
    @BindView(R.id.layout_group_state)
    LinearLayout mCoursesStateLayout;
    @BindView(R.id.group_teacher_name)
    TextView mTeacherNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_lesson_details);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_gruop_detials));
        initData();
        setViewOnClickListener();
    }

    private void initData() {
        mPresenter.setScheduleId(getIntent().getStringExtra(LikingLessonFragment.KEY_SCHEDULE_ID));
        mPresenter.setCoursesState(getIntent().getIntExtra(MyGroupLessonFragment.INTENT_KEY_STATE, -1));
        mPresenter.setOrderId(getIntent().getStringExtra(MyGroupLessonFragment.INTENT_KEY_ORDER_ID));
        requestData();
        setRightMenu();
    }

    /**
     *
     */
    private void setRightMenu() {
        setRightIcon(R.drawable.icon_phone, new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                LikingCallUtil.showPhoneDialog(GroupLessonDetailsActivity.this);
            }
        });
    }

    private void setViewOnClickListener() {
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
        if (mPresenter.mCoursesState == -1) {
            setBottomCoursesStateFreeView();
        } else if (mPresenter.mCoursesState == COURSES_STATE_NOT_START) {//未开始
            setBottomCoursesStateNoStartView();
        } else if (mPresenter.mCoursesState == COURSES_STATE_PROCESS) {//进行中
            setBottomCoursesStateSameView(R.string.start_process);
        } else if (mPresenter.mCoursesState == COURSES_STATE_OVER) {//已结束
            setBottomCoursesStateSameView(R.string.courses_complete);
        } else if (mPresenter.mCoursesState == COURSES_STATE_CANCEL) {//已取消
            setBottomCoursesStateSameView(R.string.courses_cancel);
        }
    }

    /**
     * 设置底部相同情况的view
     *
     * @param start_process
     */
    private void setBottomCoursesStateSameView(int start_process) {
        mCoursesStateLayout.setVisibility(android.view.View.VISIBLE);
        mImmediatelySubmitBtn.setVisibility(android.view.View.GONE);
        mStatePromptTextView.setText(start_process);
        mStatePromptTextView.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
        mStatePromptTextView.setBackgroundColor(0);
        mCancelOrderBtn.setVisibility(android.view.View.GONE);
        mCancelOrderBtn.setEnabled(false);
        mStatePromptTextView.setGravity(Gravity.CENTER);
    }

    /**
     * 设置底部未开始view
     */
    private void setBottomCoursesStateNoStartView() {
        mCoursesStateLayout.setVisibility(android.view.View.VISIBLE);
        mImmediatelySubmitBtn.setVisibility(android.view.View.GONE);
        mStatePromptTextView.setText(R.string.not_start_courses);
        mStatePromptTextView.setTextColor(ResourceUtils.getColor(R.color.white));
        mStatePromptTextView.setBackgroundColor(ResourceUtils.getColor(R.color.state_prompt_none));
        mCancelOrderBtn.setText(R.string.cancel_appointment);
        mCancelOrderBtn.setVisibility(android.view.View.VISIBLE);
        mCancelOrderBtn.setEnabled(true);
        mStatePromptTextView.setGravity(Gravity.CENTER);
    }

    /**
     * 设置底部团体课免费
     */
    private void setBottomCoursesStateFreeView() {
        if (mPresenter.isFree == COURSES_IS_FREE) {//免费
            mCoursesStateLayout.setVisibility(android.view.View.GONE);
            mImmediatelySubmitBtn.setVisibility(android.view.View.VISIBLE);
            if (!StringUtils.isEmpty(mPresenter.quota)) {
                if (Integer.parseInt(mPresenter.quota) == 0) {
                    mImmediatelySubmitBtn.setText(R.string.appointment_fill);
                    mImmediatelySubmitBtn.setBackgroundColor(ResourceUtils.getColor(R.color.split_line_color));
                    mImmediatelySubmitBtn.setTextColor(ResourceUtils.getColor(R.color.lesson_details_gray_back));
                    mImmediatelySubmitBtn.setEnabled(false);
                } else {
                    setImmediatelySubmitBtnView();
                }
            } else {
                setImmediatelySubmitBtnView();
            }
        } else if (mPresenter.isFree == COURSES_NOT_FREE) {//收费
            mImmediatelySubmitBtn.setVisibility(android.view.View.GONE);
            mCoursesStateLayout.setVisibility(android.view.View.VISIBLE);
            mStatePromptTextView.setTextColor(ResourceUtils.getColor(R.color.add_minus_dishes_text));
            mStatePromptTextView.setText(getString(R.string.money_symbol) + mPresenter.price);
            mStatePromptTextView.setGravity(Gravity.CENTER | Gravity.LEFT);
            mStatePromptTextView.setBackgroundColor(0);
            mCancelOrderBtn.setText(R.string.immediately_buy_btn);
            mCancelOrderBtn.setEnabled(true);
        }
    }

    /**
     * 设置提交按钮
     */
    private void setImmediatelySubmitBtnView() {
        mImmediatelySubmitBtn.setText(R.string.immadetails_appointment);
        mImmediatelySubmitBtn.setBackgroundColor(ResourceUtils.getColor(R.color.liking_green_btn_back));
        mImmediatelySubmitBtn.setTextColor(ResourceUtils.getColor(R.color.white));
        mImmediatelySubmitBtn.setEnabled(true);
    }

    private void requestData() {
        mStateView.setState(StateView.State.LOADING);
        mPresenter.getGroupCoursesDetails(mPresenter.scheduleId);
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
        finish();
    }

    @Override
    public void updateErrorNoCard(String errorMessage) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(getString(R.string.no_card_or_no_time));
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
                intent.putExtra(LikingHomeActivity.KEY_INTENT_TAB, 1);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    public void updateCancelOrderView() {
        showToast(getString(R.string.cancel_success));
        postEvent(new CancelGroupCoursesMessage());
        mPresenter.mCoursesState = 3;
        setBottomCoursesState();
        requestData();
    }

    /**
     * 设置详情数据
     *
     * @param groupLessonData
     */
    private void setDetailsData(GroupCoursesResult.GroupLessonData groupLessonData) {
        //  gymId = groupLessonData.getGymId();
        setTitle(groupLessonData.getCourseName());
        List<String> coursesImageList = groupLessonData.getCourseImgs();
        if (coursesImageList != null && coursesImageList.size() > 0) {
            String coursesImageUrl = coursesImageList.get(0);
            if (!StringUtils.isEmpty(coursesImageUrl)) {
                HImageLoaderSingleton.loadImage(mShopImageView, coursesImageUrl, this);
            }
        }

        mScheduleResultTextView.setText(groupLessonData.getQuotaDesc());
        mCoursesTimeTextView.setText(groupLessonData.getCourseDate());
        if (!TextUtils.isEmpty(groupLessonData.getGymAddress())) {
            mShopAddressTextView.setText(groupLessonData.getGymAddress().trim());
        }
        mShopPlaceTextView.setText(groupLessonData.getPlaceInfo());
        if (COURSES_SELF == mPresenter.scheduleType) {//如果是自助课程隐藏教练显示
            mTeacherNamelayout.setVisibility(android.view.View.GONE);
        } else {
            mTeacherNamelayout.setVisibility(android.view.View.VISIBLE);
            mTeacherNameTextView.setText(groupLessonData.getTrainerName());
        }

        String rat = groupLessonData.getIntensity();
        if (!TextUtils.isEmpty(rat)) {
            mRatingBar.setRating(Float.parseFloat(rat));
        }
        mCoursesIntroduceTextView.setText(groupLessonData.getCourseDesc());
        setStadiumImage(groupLessonData.getGymImgs());
        setGroupLessonNumbers(groupLessonData.getGymNumbers());
        String tagName = groupLessonData.getTagName();
        mGroupCoursesTagTextView.setText(tagName);
        setBottomCoursesState();
    }

    /**
     * 设置报名会员
     *
     * @param gymNumbersDatas
     */
    private void setGroupLessonNumbers(List<GroupCoursesResult.GroupLessonData.GymNumbersData> gymNumbersDatas) {
        mJoinUserNumbers.setText(gymNumbersDatas.size() + getString(R.string.people));
        mUserListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GroupLessonNumbersAdapter groupLessonNumbersAdapter = new GroupLessonNumbersAdapter(this);
        mUserListRecyclerView.addItemDecoration(new RecyclerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        groupLessonNumbersAdapter.setData(gymNumbersDatas);
        mUserListRecyclerView.setAdapter(groupLessonNumbersAdapter);
    }

    private void setStadiumImage(List<GroupCoursesResult.GroupLessonData.GymImgsData> stadiumImageList) {
        if (stadiumImageList != null) {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            GroupLessonDetailsAdapter groupLessonDetailsAdapter = new GroupLessonDetailsAdapter(this);
            groupLessonDetailsAdapter.setData(stadiumImageList);
            mRecyclerView.setAdapter(groupLessonDetailsAdapter);
            groupLessonDetailsAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
                @Override
                public void onItemClick(android.view.View view, int position) {
                    UMengCountUtil.UmengCount(GroupLessonDetailsActivity.this, UmengEventId.ARENAACTIVITY);
                    Intent intent = new Intent(GroupLessonDetailsActivity.this, ArenaActivity.class);
                    intent.putExtra(LikingLessonFragment.KEY_GYM_ID, LikingHomeActivity.gymId);
                    startActivity(intent);
                    overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);
                }

                @Override
                public boolean onItemLongClick(android.view.View view, int position) {
                    return false;
                }
            });
        }
    }

    @OnClick({R.id.group_immediately_submit_btn,
            R.id.cancel_order_btn,
            R.id.layout_gym_introduce,
            R.id.layout_group_details,
            R.id.layout_group_courses_share})
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.group_immediately_submit_btn://立即购买
                UMengCountUtil.UmengBtnCount(this, UmengEventId.GROUP_IMMEDIATELY_SUBMIT_BUTTON);
                if (LikingPreference.isLogin()) {
                    mPresenter.orderGroupCourses(this, LikingHomeActivity.gymId);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.cancel_order_btn://取消预定
                if (mPresenter.isFree == COURSES_IS_FREE) {//免费
                    showCancelCoursesDialog();
                } else if (mPresenter.isFree == COURSES_NOT_FREE) {//收费
                    if (LikingPreference.isLogin()) {
                        Intent intent = new Intent(this, GroupCoursesChargeConfirmActivity.class);
                        intent.putExtra(LikingLessonFragment.KEY_SCHEDULE_ID, mPresenter.scheduleId);
                        // intent.putExtra(LikingLessonFragment.KEY_GYM_ID, gymId);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    }

                }

                break;
            case R.id.layout_gym_introduce:
            case R.id.layout_group_details://进入门店详情
                UMengCountUtil.UmengCount(GroupLessonDetailsActivity.this, UmengEventId.ARENAACTIVITY);
                Intent intent = new Intent(this, ArenaActivity.class);
                intent.putExtra(LikingLessonFragment.KEY_GYM_ID, LikingHomeActivity.gymId);
                this.startActivity(intent);
                this.overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);

                break;
            case R.id.layout_group_courses_share://分享
                mPresenter.getGroupShareData();
                mShareLayout.setEnabled(false);

                break;
        }
    }

    /**
     * 取消预约团体课
     */
    private void showCancelCoursesDialog() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(getString(R.string.sure_cancel_order));
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.sendCancelCoursesRequest(GroupLessonDetailsActivity.this);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }


    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(BuyGroupCoursesWechatMessage wechatMessage) {
        if (wechatMessage.isPaySuccess()) {
            finish();
        }
    }

    public void onEvent(NoCardMessage message) {
        finish();
    }

    public void onEvent(BuyGroupCoursesAliPayMessage message) {
        finish();
    }

    public void onEvent(CoursesErrorMessage message) {
        finish();
    }

    public void onEvent(LoginOutFialureMessage message) {
        finish();
    }

    @Override
    public void updateShareView(ShareData shareData) {
        ShareUtils.showShareDialog(this, shareData);
        mShareLayout.setEnabled(true);
    }

    @Override
    public void setPresenter() {
        mPresenter = new TeamCourseDetailsContract.Presenter();
    }
}
