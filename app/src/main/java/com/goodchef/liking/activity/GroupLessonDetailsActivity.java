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
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.GroupLessonDetailsAdapter;
import com.goodchef.liking.eventmessages.CancelGroupCoursesMessage;
import com.goodchef.liking.eventmessages.OrderGroupMessageSuccess;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.fragment.MyGroupLessonFragment;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.GroupCoursesDetailsPresenter;
import com.goodchef.liking.mvp.view.GroupCourserDetailsView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.utils.LikingCallUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

/**
 * 说明:团体课可详情
 * Author shaozucheng
 * Time:16/5/24 下午3:21
 */
public class GroupLessonDetailsActivity extends AppBarActivity implements GroupCourserDetailsView, View.OnClickListener {
    private static final int COURSES_STATE_NOT_START = 0;// 未开始
    private static final int COURSES_STATE_PROCESS = 1;//进行中
    private static final int COURSES_STATE_OVER = 2;//已结束
    private static final int COURSES_STATE_CANCEL = 3;//已取消
    private HImageView mShopImageView;
    private TextView mShopNameTextView;
    private TextView mScheduleResultTextView;
    private TextView mCoursesTimeTextView;
    private TextView mShopAddressTextView;
    private TextView mTeacherNameTextView;
    private RatingBar mRatingBar;
    private TextView mCoursesIntroduceTextView;
    private TextView mTrainNameTextView;
    private HImageView mTeacherImageView;
    private TextView mTeacherIntroduceTextView;
    private TextView mImmediatelySubmitBtn;

    private LinearLayout mCoursesStateLayout;
    private TextView mStatePromptTextView;
    private TextView mCancelOrderBtn;

    private RelativeLayout mGymIntroduceLayout;
    private RecyclerView mRecyclerView;
    private GroupLessonDetailsAdapter mGroupLessonDetailsAdapter;
    private GroupCoursesDetailsPresenter mGroupCoursesDetailsPresenter;
    private String scheduleId;
    private int mCoursesState = -1;
    private String orderId;
    private String gymId;
    private LikingStateView mStateView;

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
        initView();
        requestData();
        setRightMenu();
        setBottomCoursesState();
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
        mShopNameTextView = (TextView) findViewById(R.id.shop_name);
        mScheduleResultTextView = (TextView) findViewById(R.id.schedule_result);
        mCoursesTimeTextView = (TextView) findViewById(R.id.courses_time);
        mShopAddressTextView = (TextView) findViewById(R.id.shop_address);
        mTeacherNameTextView = (TextView) findViewById(R.id.group_teacher_name);
        mRatingBar = (RatingBar) findViewById(R.id.rating_courses);
        mCoursesIntroduceTextView = (TextView) findViewById(R.id.courses_introduce);
        mTrainNameTextView = (TextView) findViewById(R.id.train_name);

        mGymIntroduceLayout = (RelativeLayout) findViewById(R.id.layout_gym_introduce);
        mRecyclerView = (RecyclerView) findViewById(R.id.group_lesson_details_recyclerView);

        mTeacherImageView = (HImageView) findViewById(R.id.teacher_imageView);
        mTeacherIntroduceTextView = (TextView) findViewById(R.id.teacher_introduce);
        mImmediatelySubmitBtn = (TextView) findViewById(R.id.group_immediately_submit_btn);
        mCoursesStateLayout = (LinearLayout) findViewById(R.id.layout_group_state);
        mStatePromptTextView = (TextView) findViewById(R.id.courses_state_prompt);
        mCancelOrderBtn = (TextView) findViewById(R.id.cancel_order_btn);

        setViewOnClickListener();
    }

    private void setViewOnClickListener(){
        mImmediatelySubmitBtn.setOnClickListener(this);
        mGymIntroduceLayout.setOnClickListener(this);
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
            mCoursesStateLayout.setVisibility(View.GONE);
            mImmediatelySubmitBtn.setVisibility(View.VISIBLE);
        } else if (mCoursesState == COURSES_STATE_NOT_START) {//未开始
            mCoursesStateLayout.setVisibility(View.VISIBLE);
            mImmediatelySubmitBtn.setVisibility(View.GONE);
            mStatePromptTextView.setText(R.string.not_start);
            mCancelOrderBtn.setText(R.string.cancel_appointment);
            mCancelOrderBtn.setVisibility(View.VISIBLE);
            mCancelOrderBtn.setOnClickListener(this);
            mStatePromptTextView.setGravity(Gravity.CENTER | Gravity.LEFT);
        } else if (mCoursesState == COURSES_STATE_PROCESS) {//进行中
            mCoursesStateLayout.setVisibility(View.VISIBLE);
            mImmediatelySubmitBtn.setVisibility(View.GONE);
            mStatePromptTextView.setText(R.string.start_process);
            mCancelOrderBtn.setVisibility(View.GONE);
            mCancelOrderBtn.setOnClickListener(null);
            mStatePromptTextView.setGravity(Gravity.CENTER);
        } else if (mCoursesState == COURSES_STATE_OVER) {//已结束
            mCoursesStateLayout.setVisibility(View.VISIBLE);
            mImmediatelySubmitBtn.setVisibility(View.GONE);
            mStatePromptTextView.setText(R.string.courses_complete);
            mCancelOrderBtn.setVisibility(View.GONE);
            mCancelOrderBtn.setOnClickListener(null);
            mStatePromptTextView.setGravity(Gravity.CENTER);
        } else if (mCoursesState == COURSES_STATE_CANCEL) {//已取消
            mCoursesStateLayout.setVisibility(View.VISIBLE);
            mImmediatelySubmitBtn.setVisibility(View.GONE);
            mStatePromptTextView.setText(R.string.courses_cancel);
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
                HImageLoaderSingleton.getInstance().requestImage(mShopImageView, coursesImageUrl);
            }
        }
        mScheduleResultTextView.setText(groupLessonData.getQuota());
        mShopNameTextView.setText(groupLessonData.getGymName());
        mCoursesTimeTextView.setText(groupLessonData.getCourseDate());
        mShopAddressTextView.setText(groupLessonData.getGymAddress().trim());
        mTeacherNameTextView.setText(groupLessonData.getTrainerName());
        String rat = groupLessonData.getIntensity();
        if (!TextUtils.isEmpty(rat)) {
            mRatingBar.setRating(Float.parseFloat(rat));
        }
        mTrainNameTextView.setText("教练：" + groupLessonData.getTrainerName());
        mCoursesIntroduceTextView.setText(groupLessonData.getCourseDesc());
        mTeacherIntroduceTextView.setText(groupLessonData.getTrainerDesc());

        List<String> trainsList = groupLessonData.getTrainerImgs();
        if (trainsList != null && trainsList.size() > 0) {
            String trainsUrl = trainsList.get(0);
            if (!StringUtils.isEmpty(trainsUrl)) {
                HImageLoaderSingleton.getInstance().requestImage(mTeacherImageView, trainsUrl);
            }
        }
        setStadiumImage(groupLessonData.getGymImgs());

    }

    private void setStadiumImage(List<GroupCoursesResult.GroupLessonData.GymImgsData> stadiumImageList) {
        if (stadiumImageList != null) {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mGroupLessonDetailsAdapter = new GroupLessonDetailsAdapter(this);
            mGroupLessonDetailsAdapter.setData(stadiumImageList);
            mRecyclerView.setAdapter(mGroupLessonDetailsAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mImmediatelySubmitBtn) {
            if (Preference.isLogin()) {
                mGroupCoursesDetailsPresenter.orderGroupCourses(scheduleId, Preference.getToken());
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } else if (v == mCancelOrderBtn) {//取消预定
            showCancelCoursesDialog();
        } else if (v == mGymIntroduceLayout) {
            Intent intent = new Intent(this, ArenaActivity.class);
            intent.putExtra(LikingLessonFragment.KEY_GYM_ID, gymId);
            this.startActivity(intent);
            this.overridePendingTransition(R.anim.silde_bottom_in, R.anim.silde_bottom_out);
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
}
