package com.goodchef.liking.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.BaseFragment;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.AboutActivity;
import com.goodchef.liking.activity.BecomeTeacherActivity;
import com.goodchef.liking.activity.BingBraceletActivity;
import com.goodchef.liking.activity.BodyTestDataActivity;
import com.goodchef.liking.activity.ContactJonInActivity;
import com.goodchef.liking.activity.CouponsActivity;
import com.goodchef.liking.activity.EveryDaySportActivity;
import com.goodchef.liking.activity.LikingHomeActivity;
import com.goodchef.liking.activity.LoginActivity;
import com.goodchef.liking.activity.MyBraceletActivity;
import com.goodchef.liking.activity.MyCardActivity;
import com.goodchef.liking.activity.MyInfoActivity;
import com.goodchef.liking.activity.MyLessonActivity;
import com.goodchef.liking.activity.MyOrderActivity;
import com.goodchef.liking.activity.MyTrainDataActivity;
import com.goodchef.liking.activity.SelfHelpGroupActivity;
import com.goodchef.liking.eventmessages.GymNoticeMessage;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.eventmessages.LoginOutMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.CoursesResult;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.LoginPresenter;
import com.goodchef.liking.mvp.view.LoginView;
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
public class LikingMyFragment extends BaseFragment implements View.OnClickListener, LoginView {
    public static final String KEY_MY_BRACELET_MAC = "key_my_bracelet_mac";
    private LinearLayout mContactJoinLayout;//联系加盟
    private LinearLayout mBecomeTeacherLayout;//称为教练
    private LinearLayout mAboutUsLayout;//关于我们

    private RelativeLayout mHeadInfoLayout;//头像布局
    private HImageView mHImageViewBackground;//头像背景
    private HImageView mHeadHImageView;//头像
    private TextView mPersonNameTextView;//用户名称
    private TextView mPersonPhoneTextView;//用户手机
    private TextView mLoginOutBtn;//退出登录
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

    private TextView mContactSetviceBtn;//联系客服

    public static final String NULL_STRING = "";
    private LikingStateView mStateView;
    private boolean isRetryRequest = false;
    private Typeface mTypeface;
    private String mBraceletMac;//手环mac地址

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liking_my, container, false);
        mTypeface = TypefaseUtil.getImpactTypeface(getActivity());
        initView(view);
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
            setLogonView();
            getUserExerciseData();
        } else {
            mStateView.setState(StateView.State.FAILED);
        }
    }


    /**
     * 获取我的锻炼数据
     */
    private void getUserExerciseData() {
        if (Preference.isLogin()) {
            if (!EnvironmentUtils.Network.isNetWorkAvailable()) {
                clearExerciseData();
            } else {
                if (isRetryRequest) {
                    mStateView.setState(StateView.State.LOADING);
                }
                LiKingApi.getUserExerciseData(Preference.getToken(), new RequestCallback<UserExerciseResult>() {
                    @Override
                    public void onSuccess(UserExerciseResult result) {
                        if (isRetryRequest) {
                            mStateView.setState(StateView.State.SUCCESS);
                            isRetryRequest = false;
                        }
                        if (LiKingVerifyUtils.isValid(getActivity(), result)) {
                            UserExerciseResult.ExerciseData exerciseData = result.getExerciseData();
                            doExerciseData(exerciseData);
                        }
                    }

                    @Override
                    public void onFailure(RequestError error) {
                        if (isRetryRequest) {
                            mStateView.setState(StateView.State.FAILED);
                            isRetryRequest = false;
                        }
                    }
                });
            }
        } else {
            if (EnvironmentUtils.Network.isNetWorkAvailable()) {
                mStateView.setState(StateView.State.SUCCESS);
            }
            clearExerciseData();
        }
    }


    /**
     * 处理
     *
     * @param exerciseData
     */
    private void doExerciseData(UserExerciseResult.ExerciseData exerciseData) {
        if (exerciseData != null) {
            mTrainTimeData.setText(exerciseData.getTodayMin());
            mBodyScoreData.setText(exerciseData.getScore());
            mEveryDataSportData.setText(exerciseData.getAllDistance());
            Preference.setIsVip(exerciseData.getIsVip());
            Preference.setIsBind(exerciseData.getIsBind());
            mBraceletMac = exerciseData.getBraceletMac();
            if (Preference.isVIP()) {
                mIsVip.setVisibility(View.VISIBLE);
            } else {
                mIsVip.setVisibility(View.GONE);
            }
            if (Preference.isBind()) {
                setMySettingCard(mBindBraceletLinearLayout, R.string.layout_bing_bracelet_my, true);
            } else {
                setMySettingCard(mBindBraceletLinearLayout, R.string.layout_bing_bracelet, true);
            }
        }
    }

    /**
     * 清除训练数据
     */
    private void clearExerciseData() {
        mBodyScoreData.setText("-");
        mTrainTimeData.setText("-");
        mEveryDataSportData.setText("-");
    }


    private void setLogonView() {
        if (Preference.isLogin()) {
            mLoginBtn.setVisibility(View.GONE);
            mLoginOutBtn.setVisibility(View.VISIBLE);
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
            mLoginOutBtn.setVisibility(View.GONE);
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
        mAboutUsLayout = (LinearLayout) view.findViewById(R.id.layout_about_us);

        mHImageViewBackground = (HImageView) view.findViewById(R.id.head_image_background);
        mHeadHImageView = (HImageView) view.findViewById(R.id.head_image);
        mLoginOutBtn = (TextView) view.findViewById(R.id.login_out_btn);
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

        mPersonNameTextView = (TextView) view.findViewById(R.id.person_name);
        mPersonPhoneTextView = (TextView) view.findViewById(R.id.person_phone);
        mContactSetviceBtn = (TextView) view.findViewById(R.id.contact_service);

        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                LiKingVerifyUtils.initApi(getActivity());
                setLogonView();
                isRetryRequest = true;
                getUserExerciseData();
            }
        });
        showSelfHelpGroupLayout(((LikingHomeActivity) getActivity()).mCanSchedule);
    }

    private void setViewOnClickListener() {
        mContactJoinLayout.setOnClickListener(this);
        mBecomeTeacherLayout.setOnClickListener(this);
        mAboutUsLayout.setOnClickListener(this);
        mHeadInfoLayout.setOnClickListener(this);
        mHeadHImageView.setOnClickListener(this);
        mLoginOutBtn.setOnClickListener(this);
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
        setMySettingCard(mBecomeTeacherLayout, R.string.layout_become_teacher, true);
        setMySettingCard(mAboutUsLayout, R.string.layout_about_us, false);
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
        setHeadPersonDataView(mBodyScoreLayout, R.string.body_test_grade, R.string.body_test_grade_unit);
        setHeadPersonDataView(mEverydaySportLayout, R.string.everyday_sport_title, R.string.everyday_sport_unit);
        setHeadPersonDataView(mTrainLayout, R.string.today_train_data, R.string.today_train_data_unit);
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
                intent.putExtra(LoginActivity.KEY_TITLE_SET_USER_INFO, "修改个人信息");
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
        } else if (v == mAboutUsLayout) {//关于
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
        } else if (v == mLoginOutBtn) {//退出登录
            if (Preference.isLogin()) {
                showExitDialog();
            } else {
                PopupUtils.showToast("您还没有登录");
            }
        } else if (v == mContactSetviceBtn) {
            String phone = Preference.getCustomerServicePhone();
            if (!StringUtils.isEmpty(phone)) {
                LikingCallUtil.showCallDialog(getActivity(), "确定联系客服吗？", phone);
            }
        } else if (v == mSelfHelpGroupLayout) {//自助团体课
            startActivity(SelfHelpGroupActivity.class);
        } else if (v == mBodyScoreLayout) {//体测数据
            if (Preference.isLogin()) {
                Intent intent = new Intent(getActivity(), BodyTestDataActivity.class);
                intent.putExtra(BodyTestDataActivity.BODY_ID, "");
                intent.putExtra(BodyTestDataActivity.SOURCE, "other");
                startActivity(intent);
            } else {
                startActivity(LoginActivity.class);
            }
        } else if (v == mBindBraceletLinearLayout) {//绑定手环
            if (Preference.isLogin()) {
                if (StringUtils.isEmpty(mBraceletMac)) {
                    return;
                }
                if (Preference.isBind()) {//绑定过手环
                    Intent intent = new Intent(getActivity(), MyBraceletActivity.class);
                    intent.putExtra(KEY_MY_BRACELET_MAC, mBraceletMac);
                    intent.putExtra(MyBraceletActivity.KEY_BRACELET_SOURCE, "LikingMyFragment");
                    startActivity(intent);
                } else {//没有绑过
                    Intent intent = new Intent(getActivity(), BingBraceletActivity.class);
                    intent.putExtra(KEY_MY_BRACELET_MAC, mBraceletMac);
                    startActivity(intent);
                }
            } else {
                startActivity(LoginActivity.class);
            }
        } else if (v == mEverydaySportLayout) {//每日运动
            startActivity(EveryDaySportActivity.class);
        }
    }

    /**
     * 退出登录dialog
     */
    private void showExitDialog() {
        final HBaseDialog.Builder builder = new HBaseDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.login_exit_message));
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exitLoginRequest();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 退出登录发送请求
     */
    private void exitLoginRequest() {
        LoginPresenter loginPresenter = new LoginPresenter(getActivity(), this);
        loginPresenter.userLoginOut();
    }

    @Override
    public void updateVerificationCodeView(VerificationCodeResult.VerificationCodeData verificationCodeData) {

    }

    @Override
    public void updateLoginView(UserLoginResult.UserLoginData userLoginData) {

    }

    @Override
    public void updateLoginOut() {
        Preference.setToken(NULL_STRING);
        Preference.setNickName(NULL_STRING);
        Preference.setUserPhone(NULL_STRING);
        Preference.setIsNewUser(null);
        Preference.setUserIconUrl(NULL_STRING);
        PopupUtils.showToast("退出成功");
        setLogonView();
        clearExerciseData();
        postEvent(new LoginOutMessage());
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
