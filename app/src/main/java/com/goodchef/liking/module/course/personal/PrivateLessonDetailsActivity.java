package com.goodchef.liking.module.course.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.GymAdapter;
import com.goodchef.liking.adapter.TrainItemAdapter;
import com.goodchef.liking.eventmessages.BuyPrivateCoursesMessage;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.module.home.lessonfragment.LikingLessonFragment;
import com.goodchef.liking.data.remote.retrofit.result.PrivateCoursesResult;
import com.goodchef.liking.data.remote.retrofit.result.data.GymData;
import com.goodchef.liking.data.remote.retrofit.result.data.ShareData;
import com.goodchef.liking.module.login.LoginActivity;
import com.goodchef.liking.module.share.ShareContract;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.LikingCallUtil;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;
import com.goodchef.liking.wxapi.WXPayEntryActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:私教课详情
 * Author shaozucheng
 * Time:16/5/24 下午5:55
 */
public class PrivateLessonDetailsActivity extends AppBarActivity implements PrivateCoursesDetailsContract.PrivateCoursesDetailsView, ShareContract.ShareView {
    @BindView(R.id.private_courses_details_state_view)
    LikingStateView mLikingStateView;
    @BindView(R.id.private_lesson_details_teach_image)
    HImageView mTeacherHImageView;
    @BindView(R.id.layout_private_courses_share)
    LinearLayout mShareLayout;//分享布局
    @BindView(R.id.private_courses_teacher_name)
    TextView mTeacherNameTextView;//教练名称
    @BindView(R.id.private_teacher_sex)
    TextView mTeacherSexTextView;//教性别练
    @BindView(R.id.private_teacher_height)
    TextView mTeacherHeightTextView;//教练身高
    @BindView(R.id.private_teacher_weight)
    TextView mTeacherWeightTextView;//教练体重
    @BindView(R.id.teacher_tags)
    TextView mTeacherTagsTextView;
    @BindView(R.id.teacher_introduce)
    TextView mTeacherIntroduceTextView;//教练介绍
    @BindView(R.id.gym_recyclerView)
    RecyclerView mGymRecyclerView;//健身房场馆布局
    @BindView(R.id.train_recyclerView)
    RecyclerView mTrainItemRecyclerView;//训练项目布局
    @BindView(R.id.card_rule)
    TextView mCardRuleTextView;//规则

    private PrivateCoursesDetailsContract.PrivateCoursesDetailsPresenter mCoursesDetailsPresenter;
    private String trainerId;
    private String teacherName;

    private ShareContract.SharePresenter mSharePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_lesson_details);
        ButterKnife.bind(this);
        mSharePresenter = new ShareContract.SharePresenter(this, this);
        initView();
        initData();
    }

    private void initView() {
        mLikingStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                sendDetailsRequest();
            }
        });
    }

    private void initData() {
        trainerId = getIntent().getStringExtra(LikingLessonFragment.KEY_TRAINER_ID);
        teacherName = getIntent().getStringExtra(LikingLessonFragment.KEY_TEACHER_NAME);

        setTitle(teacherName);
        setRightMenu();
        sendDetailsRequest();
    }

    /**
     *
     */
    private void setRightMenu() {
        setRightIcon(R.drawable.icon_phone, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikingCallUtil.showPhoneDialog(PrivateLessonDetailsActivity.this);
            }
        });
    }


    private void sendDetailsRequest() {
        mLikingStateView.setState(StateView.State.LOADING);
        mCoursesDetailsPresenter = new PrivateCoursesDetailsContract.PrivateCoursesDetailsPresenter(this, this);
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
                    HImageLoaderSingleton.loadImage(mTeacherHImageView, imageUrl, this);
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
            mCardRuleTextView.setText(privateCoursesData.getPurchaseRule());
            setGymRecyclerData(privateCoursesData.getGymDataList());
            setTrainItemRecyclerData(privateCoursesData.getCoursesDataList());
        } else {
            mLikingStateView.setState(StateView.State.NO_DATA);
        }
    }


    /**
     * 设置场馆
     */
    private void setGymRecyclerData(List<GymData> mGymDataList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mGymRecyclerView.setLayoutManager(layoutManager);
        GymAdapter gymAdapter = new GymAdapter(this);
        gymAdapter.setData(mGymDataList);
        mGymRecyclerView.setAdapter(gymAdapter);
    }

    /**
     * 设置训练项目
     *
     * @param coursesDataList 训练项目数据
     */
    private void setTrainItemRecyclerData(List<PrivateCoursesResult.PrivateCoursesData.CoursesData> coursesDataList) {
        mTrainItemRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        TrainItemAdapter trainItemAdapter = new TrainItemAdapter(this);
        trainItemAdapter.setData(coursesDataList);
        mTrainItemRecyclerView.setAdapter(trainItemAdapter);
    }


    @OnClick({R.id.private_lesson_immediately_submit, R.id.layout_private_courses_share})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.private_lesson_immediately_submit://立即预约
                UMengCountUtil.UmengBtnCount(PrivateLessonDetailsActivity.this, UmengEventId.PRIVATE_IMMEDIATELY_SUBMIT_BUTTON);
                if (LikingPreference.isLogin()) {
                    Intent intent = new Intent(this, OrderPrivateCoursesConfirmActivity.class);
                    intent.putExtra(LikingLessonFragment.KEY_TRAINER_ID, trainerId);
                    intent.putExtra(LikingLessonFragment.KEY_TEACHER_NAME, teacherName);

                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.layout_private_courses_share://分享
                if (mSharePresenter == null) {
                    mSharePresenter = new ShareContract.SharePresenter(this, this);
                }
                mSharePresenter.getPrivateShareData(trainerId);
                mShareLayout.setEnabled(false);
                break;
        }
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(WXPayEntryActivity.WechatPayMessage wechatMessage) {
        if (wechatMessage.isPaySuccess()) {
            finish();
        }
    }

    public void onEvent(BuyPrivateCoursesMessage message) {
        finish();
    }

    public void onEvent(LoginFinishMessage message) {
        sendDetailsRequest();
    }

    public void onEvent(LoginOutFialureMessage message) {
        finish();
    }

    @Override
    public void changeStateView(StateView.State state) {
        mLikingStateView.setState(state);
    }

    @Override
    public void updateShareView(ShareData shareData) {
        mShareLayout.setEnabled(true);
        mSharePresenter.showShareDialog(this, shareData);
    }
}
