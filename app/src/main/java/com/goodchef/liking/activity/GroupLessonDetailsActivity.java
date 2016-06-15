package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.GroupLessonDetailsAdapter;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.GroupCoursesResult;
import com.goodchef.liking.http.result.PrivateCoursesResult;
import com.goodchef.liking.mvp.presenter.CoursesDetailsPresenter;
import com.goodchef.liking.mvp.view.CoursesDetailsView;

import java.util.List;

/**
 * 说明:团体课可详情
 * Author shaozucheng
 * Time:16/5/24 下午3:21
 */
public class GroupLessonDetailsActivity extends AppBarActivity implements CoursesDetailsView, View.OnClickListener {
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


    private RecyclerView mRecyclerView;
    private GroupLessonDetailsAdapter mGroupLessonDetailsAdapter;
    private CoursesDetailsPresenter mCoursesDetailsPresenter;
    private String scheduleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_lesson_details);
        setTitle("团体课详情");
        initData();
    }

    private void initData() {
        scheduleId = getIntent().getStringExtra(LikingLessonFragment.KEY_SCHEDULE_ID);
        initView();
        requestData();
        setRightMenu();
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

        mImmediatelySubmitBtn.setOnClickListener(this);
    }

    private void requestData() {
        mCoursesDetailsPresenter = new CoursesDetailsPresenter(this, this);
        mCoursesDetailsPresenter.getGroupCoursesDetails(scheduleId);
    }


    @Override
    public void updateGroupLessonDetailsView(GroupCoursesResult.GroupLessonData groupLessonData) {
        setDetailsData(groupLessonData);
    }

    /**
     * 设置详情数据
     *
     * @param groupLessonData
     */
    private void setDetailsData(GroupCoursesResult.GroupLessonData groupLessonData) {
        setTitle(groupLessonData.getCourseName());

        List<String> coursesImageList = groupLessonData.getCourseImgs();
        if (coursesImageList != null) {
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
        if (trainsList != null) {
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
    public void updatePrivateCoursesDetailsView(PrivateCoursesResult.PrivateCoursesData privateCoursesData) {
    }

    @Override
    public void onClick(View v) {
        if (v == mImmediatelySubmitBtn) {
            PopupUtils.showToast("开发中");
        }
    }
}
