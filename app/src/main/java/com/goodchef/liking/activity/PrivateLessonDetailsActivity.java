package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PhoneUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.BuyPrivateCoursesMessage;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.PrivateCoursesResult;
import com.goodchef.liking.mvp.presenter.PrivateCoursesDetailsPresenter;
import com.goodchef.liking.mvp.view.PrivateCoursesDetailsView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

import java.util.List;

/**
 * 说明:私教课详情
 * Author shaozucheng
 * Time:16/5/24 下午5:55
 */
public class PrivateLessonDetailsActivity extends AppBarActivity implements PrivateCoursesDetailsView, View.OnClickListener {
    private HImageView mTeacherHImageView;
    private TextView mTeacherNameTextView;//教练名称
    private TextView mTeacherSexTextView;//教性别练
    private TextView mTeacherHeightTextView;//教练身高
    private TextView mTeacherWeightTextView;//教练体重
    private TextView mTeacherTagsTextView;//tag
    private TextView mTeacherIntroduceTextView;//教练介绍
    private TextView mImmediatelySubmitBtn;

    private PrivateCoursesDetailsPresenter mCoursesDetailsPresenter;
    private String trainerId;
    private String teacherName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_lesson_details);
        initView();
        setViewOnClickListener();
        initData();
    }

    private void initData() {
        trainerId = getIntent().getStringExtra(LikingLessonFragment.KEY_TRAINER_ID);
        teacherName = getIntent().getStringExtra(LikingLessonFragment.KEY_TEACHER_NAME);
        setTitle(teacherName);
        setRightMenu();
        sendDetailsRequest();
    }

    private void setRightMenu() {
        setRightIcon(R.drawable.icon_phone, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = Preference.getCustomerServicePhone();
                if (!StringUtils.isEmpty(phone)) {
                    PhoneUtils.phoneCall(PrivateLessonDetailsActivity.this, phone);
                }
            }
        });
    }

    private void initView() {
        mTeacherHImageView = (HImageView) findViewById(R.id.private_lesson_details_teach_image);
        mTeacherNameTextView = (TextView) findViewById(R.id.private_courses_teacher_name);
        mTeacherSexTextView = (TextView) findViewById(R.id.private_teacher_sex);
        mTeacherHeightTextView = (TextView) findViewById(R.id.private_teacher_height);
        mTeacherWeightTextView = (TextView) findViewById(R.id.private_teacher_weight);
        mTeacherTagsTextView = (TextView) findViewById(R.id.teacher_tags);
        mTeacherIntroduceTextView = (TextView) findViewById(R.id.teacher_introduce);
        mImmediatelySubmitBtn = (TextView) findViewById(R.id.private_lesson_immediately_submit);
    }

    private void setViewOnClickListener() {
        mImmediatelySubmitBtn.setOnClickListener(this);
    }

    private void sendDetailsRequest() {
        mCoursesDetailsPresenter = new PrivateCoursesDetailsPresenter(this, this);
        mCoursesDetailsPresenter.getPrivateCoursesDetails(trainerId);
    }


    @Override
    public void updatePrivateCoursesDetailsView(PrivateCoursesResult.PrivateCoursesData privateCoursesData) {
        setTitle(privateCoursesData.getTrainerName());
        List<String> imageList = privateCoursesData.getTrainerImgs();
        if (imageList != null) {
            String imageUrl = imageList.get(0);
            if (!TextUtils.isEmpty(imageUrl)) {
                HImageLoaderSingleton.getInstance().requestImage(mTeacherHImageView, imageUrl);
            }
        }

        StringBuffer stringBuffer = new StringBuffer();
        List<String> tags = privateCoursesData.getTags();
        if (tags != null && tags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {
                stringBuffer.append("#" + tags.get(i) + "  ");
            }
        }
        mTeacherTagsTextView.setText(stringBuffer.toString());
        mTeacherIntroduceTextView.setText(privateCoursesData.getDesc());
        mTeacherNameTextView.setText(teacherName);
    }


    @Override
    public void onClick(View v) {
        if (v == mImmediatelySubmitBtn) {
            if (Preference.isLogin()) {
                Intent intent = new Intent(this, OrderPrivateCoursesConfirmActivity.class);
                intent.putExtra(LikingLessonFragment.KEY_TRAINER_ID, trainerId);
                intent.putExtra(LikingLessonFragment.KEY_TEACHER_NAME, teacherName);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(WXPayEntryActivity.WechatPayMessage wechatMessage) {
       this.finish();
    }

    public void onEvent(BuyPrivateCoursesMessage message){
        this.finish();
    }

}
