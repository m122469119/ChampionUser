package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.BuyPrivateCoursesMessage;
import com.goodchef.liking.fragment.LikingLessonFragment;
import com.goodchef.liking.http.result.PrivateCoursesResult;
import com.goodchef.liking.mvp.presenter.PrivateCoursesDetailsPresenter;
import com.goodchef.liking.mvp.view.PrivateCoursesDetailsView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.LikingCallUtil;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;
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
    private RecyclerView mGymRecyclerView;
    private RecyclerView mTrainItemRecyclerView;
    private TextView mImmediatelySubmitBtn;

    private PrivateCoursesDetailsPresenter mCoursesDetailsPresenter;
    private String trainerId;
    private String teacherName;
    private LikingStateView mLikingStateView;

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
                    LikingCallUtil.showCallDialog(PrivateLessonDetailsActivity.this, "确定联系客服吗？", phone);
                }
            }
        });
    }

    private void initView() {
        mLikingStateView = (LikingStateView) findViewById(R.id.private_courses_details_state_view);
        mTeacherHImageView = (HImageView) findViewById(R.id.private_lesson_details_teach_image);
        mTeacherNameTextView = (TextView) findViewById(R.id.private_courses_teacher_name);
        mTeacherSexTextView = (TextView) findViewById(R.id.private_teacher_sex);
        mTeacherHeightTextView = (TextView) findViewById(R.id.private_teacher_height);
        mTeacherWeightTextView = (TextView) findViewById(R.id.private_teacher_weight);
        mTeacherTagsTextView = (TextView) findViewById(R.id.teacher_tags);
        mTeacherIntroduceTextView = (TextView) findViewById(R.id.teacher_introduce);
        mGymRecyclerView = (RecyclerView) findViewById(R.id.gym_recyclerView);
        mTrainItemRecyclerView = (RecyclerView) findViewById(R.id.train_recyclerView);
        mImmediatelySubmitBtn = (TextView) findViewById(R.id.private_lesson_immediately_submit);
        mLikingStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                sendDetailsRequest();
            }
        });

    }

    private void setViewOnClickListener() {
        mImmediatelySubmitBtn.setOnClickListener(this);
    }

    private void sendDetailsRequest() {
        mLikingStateView.setState(StateView.State.LOADING);
        mCoursesDetailsPresenter = new PrivateCoursesDetailsPresenter(this, this);
        mCoursesDetailsPresenter.getPrivateCoursesDetails(trainerId);
    }


    @Override
    public void updatePrivateCoursesDetailsView(PrivateCoursesResult.PrivateCoursesData privateCoursesData) {
        if (privateCoursesData != null) {
            mLikingStateView.setState(StateView.State.SUCCESS);
            setTitle(privateCoursesData.getTrainerName());
            List<String> imageList = privateCoursesData.getTrainerImgs();
            if (imageList != null && imageList.size() > 0) {
                String imageUrl = imageList.get(0);
                if (!TextUtils.isEmpty(imageUrl)) {
                    HImageLoaderSingleton.getInstance().loadImage(mTeacherHImageView, imageUrl);
                }
            }

            StringBuffer stringBuffer = new StringBuffer();
            List<String> tags = privateCoursesData.getTags();
            if (tags != null && tags.size() > 0) {
                for (int i = 0; i < tags.size(); i++) {
                    stringBuffer.append("#" + tags.get(i) + "  ");
                }
                mTeacherTagsTextView.setVisibility(View.VISIBLE);
                mTeacherTagsTextView.setText(stringBuffer.toString());
            } else {
                mTeacherTagsTextView.setVisibility(View.GONE);
            }
            mTeacherIntroduceTextView.setText(privateCoursesData.getDesc());
            mTeacherNameTextView.setText(privateCoursesData.getTrainerName());

            int gender = privateCoursesData.getGender();
            if (gender == 0) {
                mTeacherSexTextView.setText(R.string.sex_men);
            } else if (gender == 1) {
                mTeacherSexTextView.setText(R.string.sex_man);
            }
            mTeacherHeightTextView.setText(privateCoursesData.getHeight());
            mTeacherWeightTextView.setText(privateCoursesData.getWeight());
        } else {
            mLikingStateView.setState(StateView.State.NO_DATA);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == mImmediatelySubmitBtn) {
            UMengCountUtil.UmengBtnCount(PrivateLessonDetailsActivity.this, UmengEventId.PRIVATE_IMMEDIATELY_SUBMIT_BUTTON);
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
        if (wechatMessage.isPaySuccess()) {
            this.finish();
        }
    }

    public void onEvent(BuyPrivateCoursesMessage message) {
        this.finish();
    }

    @Override
    public void handleNetworkFailure() {
        mLikingStateView.setState(StateView.State.FAILED);
    }
}
