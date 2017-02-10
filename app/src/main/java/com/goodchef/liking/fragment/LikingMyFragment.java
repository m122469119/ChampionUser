package com.goodchef.liking.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.BaseFragment;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.BecomeTeacherActivity;
import com.goodchef.liking.activity.BingBraceletActivity;
import com.goodchef.liking.activity.BodyTestDataActivity;
import com.goodchef.liking.activity.ContactJonInActivity;
import com.goodchef.liking.activity.CouponsActivity;
import com.goodchef.liking.activity.EveryDaySportActivity;
import com.goodchef.liking.activity.LikingHomeActivity;
import com.goodchef.liking.activity.LoginActivity;
import com.goodchef.liking.activity.MoreActivity;
import com.goodchef.liking.activity.MyBraceletActivity;
import com.goodchef.liking.activity.MyCardActivity;
import com.goodchef.liking.activity.MyInfoActivity;
import com.goodchef.liking.activity.MyLessonActivity;
import com.goodchef.liking.activity.MyOrderActivity;
import com.goodchef.liking.activity.MyTrainDataActivity;
import com.goodchef.liking.activity.SelfHelpGroupActivity;
import com.goodchef.liking.bluetooth.BleUtils;
import com.goodchef.liking.eventmessages.GymNoticeMessage;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.eventmessages.LoginOutMessage;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.http.result.MyUserOtherInfoResult;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.MyUserInfoOtherPresenter;
import com.goodchef.liking.mvp.presenter.UserExercisePresenter;
import com.goodchef.liking.mvp.view.MyUserInfoOtherView;
import com.goodchef.liking.mvp.view.UserExerciseView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.LikingCallUtil;
import com.goodchef.liking.utils.TypefaseUtil;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * Created on 16/5/20.
 * 我的界面
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingMyFragment extends BaseFragment implements View.OnClickListener, MyUserInfoOtherView, UserExerciseView {
    public static final String KEY_MY_BRACELET_MAC = "key_my_bracelet_mac";
    public static final String KEY_UUID = "key_UUID";
    private LinearLayout mContactJoinLayout;//联系加盟
    private LinearLayout mBecomeTeacherLayout;//称为教练
    private RelativeLayout mMoreLayout;//更多
    private ImageView mUpdateAppImageView;

    private RelativeLayout mHeadInfoLayout;//头像布局
    private HImageView mHImageViewBackground;//头像背景
    private HImageView mHeadHImageView;//头像
    private TextView mPersonNameTextView;//用户名称
    private TextView mPersonPhoneTextView;//用户手机

    private TextView mLoginBtn;//登录按钮
    private TextView mIsVip;//是否是VIP

    private LinearLayout mSelfHelpGroupLayout;//自助团体课
    private LinearLayout mBindBraceletLinearLayout;//绑定手环
    private LinearLayout mMyCourseLayout;//我的课程
    private LinearLayout mMyOrderLayout;//我的订单
    private LinearLayout mMemberCardLayout;//会员卡
    private LinearLayout mCouponsLayout;//我的优惠券

    private LinearLayout mBodyScoreLayout;
    private LinearLayout mEverydaySportLayout;
    private LinearLayout mTrainLayout;
    private TextView mBodyScoreData;//个人训练数据
    private TextView mEveryDataSportData;//每日运动
    private TextView mTrainTimeData;//训练时间
    private TextView mHeadPersonDataTextView;

    private TextView mContactSetviceBtn;//联系客服


    private LikingStateView mStateView;
    private boolean isRetryRequest = true;
    private Typeface mTypeface;//字体
    private String mBraceletMac;//手环mac地址
    private String UUID;//蓝牙UUID
    private BleUtils mBleUtils;//蓝牙Util
    private MyUserInfoOtherPresenter mMyUserInfoOtherPresenter;
    private UserExercisePresenter mUserExercisePresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liking_my, container, false);
        mTypeface = TypefaseUtil.getImpactTypeface(getActivity());
        initView(view);
        mBleUtils = new BleUtils();
        initViewIconAndText();
        setHeadPersonData();
        setViewOnClickListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (EnvironmentUtils.Network.isNetWorkAvailable()) {
            mStateView.setState(StateView.State.SUCCESS);
            showUpdate();
            setLogonView();
            getMyUserInfoOther();
            getUserExerciseData();
        } else {
            mStateView.setState(StateView.State.FAILED);
        }
    }

    private void showUpdate(){
        int update = Preference.getUpdateApp();
        if (update ==0){//不更新
            mUpdateAppImageView.setVisibility(View.GONE);
        }else if (update == 1 || update ==2){//有更新
            mUpdateAppImageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获个人信息
     */
    private void getMyUserInfoOther() {
        if (Preference.isLogin()) {
            if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                clearExerciseData();
            } else {
                if (mMyUserInfoOtherPresenter == null) {
                    mMyUserInfoOtherPresenter = new MyUserInfoOtherPresenter(getActivity(), this);
                }
                if (isRetryRequest) {
                    mStateView.setState(StateView.State.LOADING);
                    isRetryRequest = false;
                }
                mMyUserInfoOtherPresenter.getMyserInfoOther();
            }
        } else {
            if (EnvironmentUtils.Network.isNetWorkAvailable()) {
                mStateView.setState(StateView.State.SUCCESS);
            }
            clearExerciseData();
        }
    }


    /**
     * 获取我的锻炼数据
     */
    private void getUserExerciseData() {
        if (Preference.isLogin()) {
            if (mUserExercisePresenter == null) {
                mUserExercisePresenter = new UserExercisePresenter(getActivity(), this);
            }
            mUserExercisePresenter.getExerciseData();
        } else {
            clearExerciseData();
        }
    }

    @Override
    public void updateMyUserInfoOtherView(MyUserOtherInfoResult.UserOtherInfoData userOtherInfoData) {
        mStateView.setState(StateView.State.SUCCESS);
        Preference.setIsBind(userOtherInfoData.getIsBind());
        Preference.setIsVip(Integer.parseInt(userOtherInfoData.getIsVip()));
        mBraceletMac = userOtherInfoData.getBraceletMac();
        UUID = userOtherInfoData.getUuid();
        if (Preference.isVIP()) {
            mIsVip.setVisibility(View.VISIBLE);
        } else {
            mIsVip.setVisibility(View.GONE);
        }
        if (Preference.isBind()) {
            setMySettingCard(mBindBraceletLinearLayout, R.string.layout_bing_bracelet_my, true);
            mEveryDataSportData.setText(userOtherInfoData.getAllDistance());
        } else {
            setMySettingCard(mBindBraceletLinearLayout, R.string.layout_bing_bracelet, true);
        }
        setHeadPersonData();
    }

    @Override
    public void updateUserExerciseView(UserExerciseResult.ExerciseData exerciseData) {
        mStateView.setState(StateView.State.SUCCESS);
        doExerciseData(exerciseData);
    }

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
        isRetryRequest = true;
        clearExerciseData();
    }


    /**
     * 处理
     *
     * @param exerciseData
     */
    private void doExerciseData(UserExerciseResult.ExerciseData exerciseData) {
        if (exerciseData != null) {
            mTrainTimeData.setText(exerciseData.getTodayMin());
            if (Preference.isBind()) {
                mBodyScoreData.setText(exerciseData.getScore());
            }else {
                mEveryDataSportData.setText(exerciseData.getScore());
            }
        }
    }

    /**
     * 清除训练数据
     */
    private void clearExerciseData() {
        if (mBodyScoreData != null) {
            mBodyScoreData.setText("--");
        }
        mTrainTimeData.setText("--");
        mEveryDataSportData.setText("--");
    }


    private void setLogonView() {
        if (Preference.isLogin()) {
            mLoginBtn.setVisibility(View.GONE);
            mPersonNameTextView.setVisibility(View.VISIBLE);
            mPersonPhoneTextView.setVisibility(View.VISIBLE);
            mPersonNameTextView.setText(Preference.getNickName());
            mPersonPhoneTextView.setText(Preference.getUserPhone());
            if (!StringUtils.isEmpty(Preference.getUserIconUrl())) {
                HImageLoaderSingleton.getInstance().loadImage(mHeadHImageView, Preference.getUserIconUrl());
                HImageLoaderSingleton.getInstance().loadImage(mHImageViewBackground, Preference.getUserIconUrl());
            }
            if (Preference.isVIP()) {
                mIsVip.setVisibility(View.VISIBLE);
            } else {
                mIsVip.setVisibility(View.GONE);
            }
        } else {
            mLoginBtn.setVisibility(View.VISIBLE);
            mPersonNameTextView.setVisibility(View.GONE);
            mPersonPhoneTextView.setVisibility(View.GONE);
            mIsVip.setVisibility(View.GONE);
            mHeadHImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_head_default_image));
            HImageLoaderSingleton.getInstance().loadImage(mHImageViewBackground, "");
        }
    }

    private void initView(View view) {
        mStateView = (LikingStateView) view.findViewById(R.id.my_state_view);
        mHeadInfoLayout = (RelativeLayout) view.findViewById(R.id.layout_head_info);
        mContactJoinLayout = (LinearLayout) view.findViewById(R.id.layout_contact_join);
        mBecomeTeacherLayout = (LinearLayout) view.findViewById(R.id.layout_become_teacher);
        mMoreLayout = (RelativeLayout) view.findViewById(R.id.layout_more);
        mUpdateAppImageView = (ImageView) view.findViewById(R.id.more_ImageView);

        mHImageViewBackground = (HImageView) view.findViewById(R.id.head_image_background);
        mHeadHImageView = (HImageView) view.findViewById(R.id.head_image);
        mLoginBtn = (TextView) view.findViewById(R.id.login_text);
        mIsVip = (TextView) view.findViewById(R.id.is_vip);

        mSelfHelpGroupLayout = (LinearLayout) view.findViewById(R.id.layout_self_help_group_gym);
        mBindBraceletLinearLayout = (LinearLayout) view.findViewById(R.id.layout_bind_bracelet);
        mMyCourseLayout = (LinearLayout) view.findViewById(R.id.layout_my_course);
        mMyOrderLayout = (LinearLayout) view.findViewById(R.id.layout_my_order);
        mMemberCardLayout = (LinearLayout) view.findViewById(R.id.layout_member_card);
        mCouponsLayout = (LinearLayout) view.findViewById(R.id.layout_coupons);

        mBodyScoreLayout = (LinearLayout) view.findViewById(R.id.layout_body_score);
        mEverydaySportLayout = (LinearLayout) view.findViewById(R.id.layout_everyday_sport);
        mTrainLayout = (LinearLayout) view.findViewById(R.id.layout_today_data);
        mHeadPersonDataTextView = (TextView) view.findViewById(R.id.my_head_person_data_TextView);

        mPersonNameTextView = (TextView) view.findViewById(R.id.person_name);
        mPersonPhoneTextView = (TextView) view.findViewById(R.id.person_phone);
        mContactSetviceBtn = (TextView) view.findViewById(R.id.contact_service);

        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                LiKingVerifyUtils.initApi(getActivity());
                setLogonView();
                isRetryRequest = true;
                getMyUserInfoOther();
                getUserExerciseData();
            }
        });
        showSelfHelpGroupLayout(((LikingHomeActivity) getActivity()).mCanSchedule);
    }

    private void setViewOnClickListener() {
        mContactJoinLayout.setOnClickListener(this);
        mBecomeTeacherLayout.setOnClickListener(this);
        mMoreLayout.setOnClickListener(this);
        mHeadInfoLayout.setOnClickListener(this);
        mHeadHImageView.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);

        mSelfHelpGroupLayout.setOnClickListener(this);
        mBindBraceletLinearLayout.setOnClickListener(this);
        mMyCourseLayout.setOnClickListener(this);
        mMyOrderLayout.setOnClickListener(this);
        mMemberCardLayout.setOnClickListener(this);
        mCouponsLayout.setOnClickListener(this);

        mBodyScoreLayout.setOnClickListener(this);
        mTrainLayout.setOnClickListener(this);
        mEverydaySportLayout.setOnClickListener(this);

        mContactSetviceBtn.setOnClickListener(this);
    }


    private void initViewIconAndText() {
        setMySettingCard(mSelfHelpGroupLayout, R.string.layout_self_help_group, true);
        if (Preference.isBind()) {
            setMySettingCard(mBindBraceletLinearLayout, R.string.layout_bing_bracelet_my, true);
        } else {
            setMySettingCard(mBindBraceletLinearLayout, R.string.layout_bing_bracelet, true);
        }
        setMySettingCard(mContactJoinLayout, R.string.layout_contact_join, true);
        setMySettingCard(mBecomeTeacherLayout, R.string.layout_become_teacher, false);
    }

    private void setMySettingCard(View view, int text, boolean isShowLine) {
        TextView textView = (TextView) view.findViewById(R.id.standard_my_text);
        View line = view.findViewById(R.id.standard_view_line);
        textView.setText(text);
        if (isShowLine) {
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
    }

    private void setHeadPersonData() {
        if (Preference.isBind()) {//已绑定
            mBodyScoreLayout.setVisibility(View.VISIBLE);
            mHeadPersonDataTextView.setVisibility(View.GONE);
            setHeadPersonDataView(mBodyScoreLayout, R.string.body_test_grade, R.string.body_test_grade_unit);
            setHeadPersonDataView(mEverydaySportLayout, R.string.everyday_sport_title, R.string.everyday_sport_unit);
            setHeadPersonDataView(mTrainLayout, R.string.today_train_data, R.string.today_train_data_unit);
        } else {//没有绑定
            mBodyScoreLayout.setVisibility(View.GONE);
            mHeadPersonDataTextView.setVisibility(View.VISIBLE);
            setHeadPersonDataView(mEverydaySportLayout, R.string.body_test_grade, R.string.body_test_grade_unit);
            setHeadPersonDataView(mTrainLayout, R.string.today_train_data, R.string.today_train_data_unit);
        }
    }

    /**
     * 设置头部个人数据
     *
     * @param view
     * @param title
     * @param unitText
     */
    private void setHeadPersonDataView(View view, int title, int unitText) {
        TextView titleTextView = (TextView) view.findViewById(R.id.person_body_title);
        TextView contentTextView = (TextView) view.findViewById(R.id.person_content_data);
        TextView unitTextView = (TextView) view.findViewById(R.id.person_content_data_unit);
        titleTextView.setText(title);
        unitTextView.setText(unitText);
        unitTextView.setTypeface(mTypeface);
        contentTextView.setTypeface(mTypeface);
        switch (view.getId()) {
            case R.id.layout_body_score:
                mBodyScoreData = contentTextView;
                break;
            case R.id.layout_everyday_sport:
                mEveryDataSportData = contentTextView;
                break;
            case R.id.layout_today_data:
                mTrainTimeData = contentTextView;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mTrainLayout) {//我的训练数据
            if (Preference.isLogin()) {
                UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYTRAINDATAACTIVITY);
                Intent intent = new Intent(getActivity(), MyTrainDataActivity.class);
                startActivity(intent);
            } else {
                startActivity(LoginActivity.class);
            }
        } else if (v == mLoginBtn) {//登录
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else if (v == mHeadInfoLayout || v == mHeadHImageView) {
            if (!Preference.isLogin()) {
                startActivity(LoginActivity.class);
            } else {
                UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYINFOACTIVITY);
                Intent intent = new Intent(getActivity(), MyInfoActivity.class);
                intent.putExtra(LoginActivity.KEY_TITLE_SET_USER_INFO, getString(R.string.change_person_info));
                intent.putExtra(LoginActivity.KEY_INTENT_TYPE, 2);
                startActivity(intent);
            }
        } else if (v == mMyCourseLayout) {//我的课程
            if (Preference.isLogin()) {
                UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYLESSONACTIVITY);
                Intent intent = new Intent(getActivity(), MyLessonActivity.class);
                startActivity(intent);
            } else {
                startActivity(LoginActivity.class);
            }
        } else if (v == mMyOrderLayout) {//我的订单
            if (Preference.isLogin()) {
                Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(intent);
            } else {
                startActivity(LoginActivity.class);
            }
        } else if (v == mMemberCardLayout) {//会员卡
            if (Preference.isLogin()) {
                UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYCARDACTIVITY);
                Intent intent = new Intent(getActivity(), MyCardActivity.class);
                startActivity(intent);
            } else {
                startActivity(LoginActivity.class);
            }
        } else if (v == mCouponsLayout) {//我的优惠券
            if (Preference.isLogin()) {
                UMengCountUtil.UmengCount(getActivity(), UmengEventId.COUPONSACTIVITY);
                Intent intent = new Intent(getActivity(), CouponsActivity.class);
                intent.putExtra(CouponsActivity.TYPE_MY_COUPONS, CouponsActivity.TYPE_MY_COUPONS);
                startActivity(intent);
            } else {
                startActivity(LoginActivity.class);
            }
        } else if (v == mContactJoinLayout) {//联系加盟
            Intent intent = new Intent(getActivity(), ContactJonInActivity.class);
            startActivity(intent);
        } else if (v == mBecomeTeacherLayout) {//成为教练
            Intent intent = new Intent(getActivity(), BecomeTeacherActivity.class);
            startActivity(intent);
        } else if (v == mMoreLayout) {//更多
            startActivity(MoreActivity.class);
        } else if (v == mContactSetviceBtn) {
            String phone = Preference.getCustomerServicePhone();
            if (!StringUtils.isEmpty(phone)) {
                LikingCallUtil.showCallDialog(getActivity(), getString(R.string.confrim_contact_customer_service), phone);
            }
        } else if (v == mSelfHelpGroupLayout) {//自助团体课
            startActivity(SelfHelpGroupActivity.class);
        } else if (v == mBodyScoreLayout) {//体测数据
            if (Preference.isLogin()) {
                jumpBodyTestActivity();
            } else {
                startActivity(LoginActivity.class);
            }
        } else if (v == mBindBraceletLinearLayout) {//绑定手环
            jumpBraceletActivity();
        } else if (v == mEverydaySportLayout) {//每日运动
            if (Preference.isLogin()) {
                if (Preference.isBind()) {
                    Intent intent = new Intent(getActivity(), EveryDaySportActivity.class);
                    if (!StringUtils.isEmpty(mBraceletMac)) {
                        intent.putExtra(KEY_MY_BRACELET_MAC, mBraceletMac.toUpperCase());
                        LogUtils.i(TAG, "用户手环的 mac: " + mBraceletMac.toUpperCase() + " UUID = " + UUID);
                    }
                    intent.putExtra(KEY_UUID, UUID);
                    startActivity(intent);
                } else {
                    jumpBodyTestActivity();
                }
            } else {
                startActivity(LoginActivity.class);
            }
        }
    }

    /**
     * 跳转到我的手环
     */
    private void jumpBraceletActivity() {
        if (Preference.isLogin()) {
            if (!StringUtils.isEmpty(mBraceletMac)) {
                LogUtils.i(TAG, "用户手环的 mac: " + mBraceletMac.toUpperCase() + " UUID = " + UUID);
            }
            if (Preference.isBind()) {//绑定过手环
                if (initBlueTooth()) {
                    Intent intent = new Intent(getActivity(), MyBraceletActivity.class);
                    if (!StringUtils.isEmpty(mBraceletMac)) {
                        intent.putExtra(KEY_MY_BRACELET_MAC, mBraceletMac.toUpperCase());
                    }
                    intent.putExtra(KEY_UUID, UUID);
                    intent.putExtra(MyBraceletActivity.KEY_BRACELET_NAME, "");
                    intent.putExtra(MyBraceletActivity.KEY_BRACELET_ADDRESS, "");
                    intent.putExtra(MyBraceletActivity.KEY_BRACELET_SOURCE, "LikingMyFragment");
                    startActivity(intent);
                }
            } else {//没有绑过
                Intent intent = new Intent(getActivity(), BingBraceletActivity.class);
                if (!StringUtils.isEmpty(mBraceletMac)) {
                    intent.putExtra(KEY_MY_BRACELET_MAC, mBraceletMac.toUpperCase());
                }
                intent.putExtra(KEY_UUID, UUID);
                startActivity(intent);
            }
        } else {
            startActivity(LoginActivity.class);
        }
    }

    /**
     * 初始化蓝牙
     */
    public boolean initBlueTooth() {
        if (!mBleUtils.isOpen()) {
            openBluetooth();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth() {
        mBleUtils.openBlueTooth(getActivity());
    }

    /**
     * 跳转到体测评分界面
     */
    private void jumpBodyTestActivity() {
        Intent intent = new Intent(getActivity(), BodyTestDataActivity.class);
        intent.putExtra(BodyTestDataActivity.BODY_ID, "");
        intent.putExtra(BodyTestDataActivity.SOURCE, "other");
        startActivity(intent);
    }


    @Override
    protected boolean isEventTarget() {
        return true;
    }


    public void onEvent(LoginOutFialureMessage message) {
        setLogonView();
        clearExerciseData();
    }

    public void onEvent(InitApiFinishedMessage message) {
        if (message.isSuccess()) {
            mStateView.setState(StateView.State.SUCCESS);
            setLogonView();
            getUserExerciseData();
        }
    }

    public void onEvent(GymNoticeMessage message) {
        CoursesResult.Courses.Gym mNoticeGym = message.getGym();
        showSelfHelpGroupLayout(mNoticeGym.getCanSchedule());
    }

    public void onEvent(LoginOutMessage loginOutMessage) {
        setMySettingCard(mBindBraceletLinearLayout, R.string.layout_bing_bracelet, true);
        setHeadPersonData();
        setLogonView();
        clearExerciseData();
    }

    /**
     * 是否显示自助团体课
     *
     * @param canschedule
     */
    private void showSelfHelpGroupLayout(int canschedule) {
        if (1 == canschedule) {
            mSelfHelpGroupLayout.setVisibility(View.VISIBLE);
        } else {
            mSelfHelpGroupLayout.setVisibility(View.GONE);
        }
    }


}
