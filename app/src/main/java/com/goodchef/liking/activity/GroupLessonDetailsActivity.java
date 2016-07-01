package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.GroupLessonDetailsAdapter;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.fragment.MyGroupLessonFragment;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.GroupCoursesDetailsPresenter;
import com.goodchef.liking.mvp.view.GroupCourserDetailsView;
import com.goodchef.liking.storage.Preference;

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


    private RecyclerView mRecyclerView;
    private GroupLessonDetailsAdapter mGroupLessonDetailsAdapter;
    private GroupCoursesDetailsPresenter mGroupCoursesDetailsPresenter;
    private String scheduleId;
    private int mCoursesState = -1;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_lesson_details);
        setTitle("团体课详情");
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
                PopupUtils.showToast("开发中");
            }
        });
    }

    private void initView() {
        mShopImageView = (HImageView) findViewById(R.id.group_lesson_details_shop_image);
        mShopNameTextView = (TextView) findViewById(R.id.shop_name);
        mScheduleResultTextView = (TextView) findViewById(R.id.schedule_result);
        mCoursesTimeTextView = (TextView) findViewById(R.id.courses_time);
        mShopAddressTextView = (TextView) findViewById(R.id.shop_address);
        mTeacherNameTextView = (TextView) findViewById(R.id.group_teacher_name);
        mRatingBar = (RatingBar) findViewById(R.id.rating_courses);
        mCoursesIntroduceTextView = (TextView) findViewById(R.id.courses_introduce);
        mTrainNameTextView = (TextView) findViewById(R.id.train_name);
        mRecyclerView = (RecyclerView) findViewById(R.id.group_lesson_details_recyclerView);
        mTeacherImageView = (HImageView) findViewById(R.id.teacher_imageView);
        mTeacherIntroduceTextView = (TextView) findViewById(R.id.teacher_introduce);
        mImmediatelySubmitBtn = (TextView) findViewById(R.id.group_immediately_submit_btn);
        mCoursesStateLayout = (LinearLayout) findViewById(R.id.layout_group_state);
        mStatePromptTextView = (TextView) findViewById(R.id.courses_state_prompt);
        mCancelOrderBtn = (TextView) findViewById(R.id.cancel_order_btn);

        mImmediatelySubmitBtn.setOnClickListener(this);
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
        mGroupCoursesDetailsPresenter = new GroupCoursesDetailsPresenter(this, this);
        mGroupCoursesDetailsPresenter.getGroupCoursesDetails(scheduleId);
    }


    @Override
    public void updateGroupLessonDetailsView(GroupCoursesResult.GroupLessonData groupLessonData) {
        setDetailsData(groupLessonData);
    }

    @Override
    public void updateOrderGroupCourses() {
        Intent intent = new Intent(this, LessonActivity.class);
        startActivity(intent);
        this.finish();
    }

    /**
     * 设置详情数据
     *
     * @param groupLessonData
     */
    private void setDetailsData(GroupCoursesResult.GroupLessonData groupLessonData) {
        setTitle(groupLessonData.getCourseName());

        List<String> coursesImageList = groupLessonData.getCourseImgs();
        if (coursesImageList != null && coursesImageList.size()>0) {
            String coursesImageUrl = coursesImageList.get(0);
            if (!StringUtils.isEmpty(coursesImageUrl)) {
                HImageLoaderSingleton.getInstance().requestImage(mShopImageView, coursesImageUrl);
            }
        }
        mScheduleResultTextView.setText(groupLessonData.getQuota());
        mShopNameTextView.setText(groupLessonData.getGymName());
        mCoursesTimeTextView.setText(groupLessonData.getCourseDate());
        mShopAddressTextView.setText(groupLessonData.getGymAddress());
        mTeacherNameTextView.setText(groupLessonData.getTrainerName());
        String rat = groupLessonData.getIntensity();
        if (!TextUtils.isEmpty(rat)) {
            mRatingBar.setRating(Float.parseFloat(rat));
        }
        mTrainNameTextView.setText("教练：" + groupLessonData.getTrainerName());
        mCoursesIntroduceTextView.setText(groupLessonData.getCourseDesc());
        mTeacherIntroduceTextView.setText(groupLessonData.getTrainerDesc());

        List<String> trainsList = groupLessonData.getTrainerImgs();
        if (trainsList != null && trainsList.size()>0) {
            String trainsUrl = trainsList.get(0);
            if (!StringUtils.isEmpty(trainsUrl)) {
                HImageLoaderSingleton.getInstance().requestImage(mTeacherImageView, trainsUrl);
            }
        }
        setStadiumImage(groupLessonData.getGymImgs());

    }

    private void setStadiumImage(List<String> stadiumImageList) {
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
            sendCancelCoursesRequest(orderId);
        }
    }


    //发送取消请求
    private void sendCancelCoursesRequest(String orderId) {
        LiKingApi.cancelGroupCourses(Preference.getToken(), orderId, new RequestUiLoadingCallback<BaseResult>(GroupLessonDetailsActivity.this, R.string.loading_data) {
            @Override
            public void onSuccess(BaseResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(GroupLessonDetailsActivity.this, result)) {
                    PopupUtils.showToast("取消成功");
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
}
