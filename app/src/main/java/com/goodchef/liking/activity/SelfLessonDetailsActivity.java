package com.goodchef.liking.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.SelfGroupCoursesListResult;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.utils.LikingCallUtil;

import java.util.List;

/**
 * 说明:
 * Author: chenl
 * Time: 下午5:05
 */
public class SelfLessonDetailsActivity extends AppBarActivity implements View.OnClickListener {

    public static final String KEY_SELF_LESSON_DETAILS = "selflessondetails";

    private HImageView mShopImageView;
    private TextView mCoursesTimeTextView;//时间
//    private TextView mCoursesApplianceTextView;//器械
    private RatingBar mRatingBar;//强度
    private TextView mCoursesTags;//标签
    private TextView mCoursesIntroduce;//介绍

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_selectcourse_details);
        initView();
        initData();
    }


    private void initView() {
        mShopImageView = (HImageView) findViewById(R.id.self_lesson_details_shop_image);
        mCoursesTimeTextView = (TextView) findViewById(R.id.courses_time);
//        mCoursesApplianceTextView = (TextView) findViewById(R.id.courses_appliance);
        mRatingBar = (RatingBar) findViewById(R.id.rating_courses);
        mCoursesTags = (TextView) findViewById(R.id.courses_tags);
        mCoursesIntroduce = (TextView) findViewById(R.id.courses_introduce);
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
        if (getIntent() != null){
            SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData coursesData = (SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData)getIntent().getSerializableExtra(KEY_SELF_LESSON_DETAILS);
            if (coursesData != null){
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
        setTitle(coursesData.getName());
        List<SelfGroupCoursesListResult.SelfGroupCoursesData.CoursesData.ImgData> coursesImageList = coursesData.getImg();
        if (coursesImageList != null && coursesImageList.size() > 0) {
            String coursesImageUrl = coursesImageList.get(0).getUrl();
            if (!StringUtils.isEmpty(coursesImageUrl)) {
                HImageLoaderSingleton.getInstance().loadImage(mShopImageView, coursesImageUrl);
            }
        }
        String duration = null;
        try {
            duration = Integer.parseInt(coursesData.getVideoDuration()) / 60 + "min";
        }catch (Exception e){
            duration = "";
        }
        mCoursesTimeTextView.setText(duration);
//        mCoursesApplianceTextView.setText(getAppliances(coursesData.getEquipment()));
        String tags = getTags(coursesData.getTags());
        if(TextUtils.isEmpty(tags)){
            mCoursesTags.setVisibility(View.GONE);
        }else {
            mCoursesTags.setText(tags);
        }
        mCoursesIntroduce.setText(coursesData.getDesc());
        String rat = coursesData.getIntensity();
        if (!TextUtils.isEmpty(rat)) {
            mRatingBar.setRating(Float.parseFloat(rat));
        }
    }

    private  String getAppliances(List<String> appliances){
        StringBuilder sb = new StringBuilder();
        for (String equipment:appliances){
            sb.append(equipment);
            sb.append(" ");
        }
        return sb.toString();
    }

    private String getTags(List<String> tags){
        StringBuilder sb = new StringBuilder();
        for (String equipment:tags){
            sb.append("#");
            sb.append(equipment);
            sb.append(" ");
        }
        return sb.toString();
    }

}
