package com.goodchef.liking.module.course.selfhelp.details;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.SelfGroupCoursesListResult;
import com.goodchef.liking.utils.LikingCallUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:
 * Author: chenl
 * Time: 下午5:05
 */
public class SelfLessonDetailsActivity extends AppBarActivity implements View.OnClickListener {

    public static final String KEY_SELF_LESSON_DETAILS = "selflessondetails";

    @BindView(R.id.self_lesson_details_shop_image)
    HImageView mShopImageView;
    @BindView(R.id.courses_time)
    TextView mCoursesTimeTextView;//时间
    @BindView(R.id.rating_courses)
    RatingBar mRatingBar;//强度
    @BindView(R.id.courses_tags)
    TextView mCoursesTags;//标签
    @BindView(R.id.courses_introduce)
    TextView mCoursesIntroduce;//介绍

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_selectcourse_details);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        setRightMenu();
    }

    private void setRightMenu() {
        setRightIcon(R.drawable.icon_phone, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikingCallUtil.showPhoneDialog(SelfLessonDetailsActivity.this);
            }
        });
    }

    private void initData() {
        if (getIntent() != null) {
            SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData coursesData = (SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData) getIntent().getSerializableExtra(KEY_SELF_LESSON_DETAILS);
            if (coursesData != null) {
                setDetailsData(coursesData);
            }
        }
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * 设置详情数据
     *
     * @param coursesData
     */
    private void setDetailsData(SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData coursesData) {
        if (coursesData == null) {
            return;
        }
        setTitle(coursesData.getName());
        List<SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData.ImgData> coursesImageList = coursesData.getImg();
        if (coursesImageList != null && coursesImageList.size() > 0) {
            String coursesImageUrl = coursesImageList.get(0).getUrl();
            if (!StringUtils.isEmpty(coursesImageUrl)) {
                HImageLoaderSingleton.loadImage(mShopImageView, coursesImageUrl, this);
            }
        }
        String duration = null;
        try {
            duration = Integer.parseInt(coursesData.getVideoDuration()) / 60 + getString(R.string.min);
        } catch (Exception e) {
            duration = "";
        }
        mCoursesTimeTextView.setText(duration);
//        mCoursesApplianceTextView.setText(getAppliances(coursesData.getEquipment()));
        String tags = getTags(coursesData.getTags());
        if (TextUtils.isEmpty(tags)) {
            mCoursesTags.setVisibility(View.GONE);
        } else {
            mCoursesTags.setText(tags);
        }
        mCoursesIntroduce.setText(coursesData.getDesc());
        String rat = coursesData.getIntensity();
        if (!TextUtils.isEmpty(rat)) {
            mRatingBar.setRating(Float.parseFloat(rat));
        }
    }

    private String getAppliances(List<String> appliances) {
        StringBuilder sb = new StringBuilder();
        for (String equipment : appliances) {
            sb.append(equipment);
            sb.append(" ");
        }
        return sb.toString();
    }

    private String getTags(List<String> tags) {
        StringBuilder sb = new StringBuilder();
        for (String equipment : tags) {
            sb.append("#");
            sb.append(equipment);
            sb.append(" ");
        }
        return sb.toString();
    }

}
