package com.goodchef.liking.fragment;

import android.content.DialogInterface;
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
import com.goodchef.liking.activity.BecomeTeacherActivity;
import com.goodchef.liking.activity.ContactJonInActivity;
import com.goodchef.liking.activity.CouponsActivity;
import com.goodchef.liking.activity.InviteFriendsActivity;
import com.goodchef.liking.activity.LoginActivity;
import com.goodchef.liking.activity.MyCardActivity;
import com.goodchef.liking.activity.MyInfoActivity;
import com.goodchef.liking.activity.MyLessonActivity;
import com.goodchef.liking.activity.MyOrderActivity;
import com.goodchef.liking.activity.MyTrainDataActivity;
import com.goodchef.liking.activity.WriteNameActivity;
import com.goodchef.liking.eventmessages.InitApiFinishedMessage;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.presenter.LoginPresenter;
import com.goodchef.liking.mvp.view.LoginView;
import com.goodchef.liking.storage.Preference;
import com.goodchef.liking.storage.UmengEventId;
import com.goodchef.liking.utils.LikingCallUtil;
import com.goodchef.liking.utils.UMengCountUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingMyFragment extends BaseFragment implements View.OnClickListener, LoginView {
    private LinearLayout mInviteFriendsLayout;//邀请好友
    private LinearLayout mContactJoinLayout;//联系加盟
    private LinearLayout mBecomeTeacherLayout;//称为教练
    private LinearLayout mAboutUsLayout;//关于我们

    private RelativeLayout mHeadInfoLayout;//头像布局
    private HImageView mHeadHImageView;//头像
    private TextView mLoginPrompt;//登录提示
    private TextView mPersonNameTextView;
    private TextView mPersonPhoneTextView;
    private TextView mLoginOutBtn;//退出登录
    private TextView mLoginBtn;
    private ImageView mArrowImage;

    private LinearLayout mMyCourseLayout;//我的课程
    private LinearLayout mMyOrderLayout;//我的订单
    private LinearLayout mMemberCardLayout;//会员卡
    private LinearLayout mCouponsLayout;//我的优惠券

    private RelativeLayout mTrainLayout;
    private TextView myTrainTime;
    private TextView myTrainDistance;
    private TextView myTrainCal;
    private TextView myTrainTimePrompt;
    private TextView mContactSetviceBtn;


    public static final String NULL_STRING = "";
    private LikingStateView mStateView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liking_my, container, false);
        initView(view);
        initViewIconAndText();
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
                LiKingApi.getUserExerciseData(Preference.getToken(), new RequestCallback<UserExerciseResult>() {
                    @Override
                    public void onSuccess(UserExerciseResult result) {
                        if (LiKingVerifyUtils.isValid(getActivity(), result)) {
                            UserExerciseResult.ExerciseData exerciseData = result.getExerciseData();
                            if (exerciseData != null) {
                                Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Impact.ttf");
                                myTrainTimePrompt.setVisibility(View.GONE);
                                myTrainTime.setVisibility(View.VISIBLE);
                                myTrainTime.setTypeface(typeFace);
                                myTrainDistance.setTypeface(typeFace);
                                myTrainCal.setTypeface(typeFace);
                                myTrainTime.setText(exerciseData.getTodayMin());
                                myTrainDistance.setText(exerciseData.getTodayDistance());
                                myTrainCal.setText(exerciseData.getTodayCal());
                            }
                        }
                    }

                    @Override
                    public void onFailure(RequestError error) {

                    }
                });
            }
        } else {
            clearExerciseData();
        }
    }

    /**
     * 清除训练数据
     */
    private void clearExerciseData() {
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Impact.ttf");
        myTrainDistance.setTypeface(typeFace);
        myTrainCal.setTypeface(typeFace);
        myTrainTime.setVisibility(View.GONE);
        myTrainDistance.setText("-");
        myTrainCal.setText("-");
        myTrainTimePrompt.setVisibility(View.VISIBLE);
        myTrainTimePrompt.setTypeface(typeFace);
        myTrainTimePrompt.setText("-");
    }

    private void setLogonView() {
        if (Preference.isLogin()) {
            mLoginBtn.setVisibility(View.GONE);
            mArrowImage.setVisibility(View.VISIBLE);
            mLoginOutBtn.setVisibility(View.VISIBLE);
            mLoginPrompt.setVisibility(View.GONE);
            mPersonNameTextView.setVisibility(View.VISIBLE);
            mPersonPhoneTextView.setVisibility(View.VISIBLE);
            mPersonNameTextView.setText(Preference.getNickName());
            mPersonPhoneTextView.setText(Preference.getUserPhone());
            if (!StringUtils.isEmpty(Preference.getUserIconUrl())) {
                HImageLoaderSingleton.getInstance().loadImage(mHeadHImageView, Preference.getUserIconUrl());
            }
        } else {
            mLoginBtn.setVisibility(View.VISIBLE);
            mArrowImage.setVisibility(View.GONE);
            mLoginPrompt.setVisibility(View.VISIBLE);
            mPersonNameTextView.setVisibility(View.GONE);
            mPersonPhoneTextView.setVisibility(View.GONE);
            mLoginOutBtn.setVisibility(View.GONE);
            mHeadHImageView.setImageDrawable(ResourceUtils.getDrawable(R.drawable.icon_head_default_image));
        }
    }

    private void initView(View view) {
        mStateView = (LikingStateView) view.findViewById(R.id.my_state_view);
        mHeadInfoLayout = (RelativeLayout) view.findViewById(R.id.layout_head_info);
        mInviteFriendsLayout = (LinearLayout) view.findViewById(R.id.layout_invite_friends);
        mContactJoinLayout = (LinearLayout) view.findViewById(R.id.layout_contact_join);
        mBecomeTeacherLayout = (LinearLayout) view.findViewById(R.id.layout_become_teacher);
        mAboutUsLayout = (LinearLayout) view.findViewById(R.id.layout_about_us);

        mHeadHImageView = (HImageView) view.findViewById(R.id.head_image);
        mLoginPrompt = (TextView) view.findViewById(R.id.person_login_prompt);
        mLoginOutBtn = (TextView) view.findViewById(R.id.login_out_btn);
        mLoginBtn = (TextView) view.findViewById(R.id.login_text);
        mArrowImage = (ImageView) view.findViewById(R.id.login_arrow);

        mMyCourseLayout = (LinearLayout) view.findViewById(R.id.layout_my_course);
        mMyOrderLayout = (LinearLayout) view.findViewById(R.id.layout_my_order);
        mMemberCardLayout = (LinearLayout) view.findViewById(R.id.layout_member_card);
        mCouponsLayout = (LinearLayout) view.findViewById(R.id.layout_coupons);

        mTrainLayout = (RelativeLayout) view.findViewById(R.id.layout_my_train);
        myTrainTime = (TextView) view.findViewById(R.id.my_train_time);
        myTrainDistance = (TextView) view.findViewById(R.id.my_train_distance);
        myTrainCal = (TextView) view.findViewById(R.id.my_train_cal);
        myTrainTimePrompt = (TextView) view.findViewById(R.id.my_train_time_prompt);

        mPersonNameTextView = (TextView) view.findViewById(R.id.person_name);
        mPersonPhoneTextView = (TextView) view.findViewById(R.id.person_phone);
        mContactSetviceBtn = (TextView) view.findViewById(R.id.contact_service);

        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                LiKingVerifyUtils.initApi(getActivity());
            }
        });
    }

    private void setViewOnClickListener() {
        mInviteFriendsLayout.setOnClickListener(this);
        mContactJoinLayout.setOnClickListener(this);
        mBecomeTeacherLayout.setOnClickListener(this);
        mAboutUsLayout.setOnClickListener(this);
        mHeadInfoLayout.setOnClickListener(this);
        mHeadHImageView.setOnClickListener(this);
        mLoginOutBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);

        mMyCourseLayout.setOnClickListener(this);
        mMyOrderLayout.setOnClickListener(this);
        mMemberCardLayout.setOnClickListener(this);
        mCouponsLayout.setOnClickListener(this);
        mTrainLayout.setOnClickListener(this);
        mContactSetviceBtn.setOnClickListener(this);
    }


    private void initViewIconAndText() {
        setMySettingCard(mInviteFriendsLayout, R.string.layout_invite_friends, true);
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

    @Override
    public void onClick(View v) {
        if (v == mTrainLayout) {
            if (Preference.isLogin()) {
                UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYTRAINDATAACTIVITY);
                Intent intent = new Intent(getActivity(), MyTrainDataActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        } else if (v == mLoginBtn) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else if (v == mHeadInfoLayout || v == mHeadHImageView) {
            if (!Preference.isLogin()) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
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
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        } else if (v == mMyOrderLayout) {//我的订单
            if (Preference.isLogin()) {
                Intent intent = new Intent(getActivity(), MyOrderActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        } else if (v == mMemberCardLayout) {//会员卡
            if (Preference.isLogin()) {
                UMengCountUtil.UmengCount(getActivity(), UmengEventId.MYCARDACTIVITY);
                Intent intent = new Intent(getActivity(), MyCardActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        } else if (v == mInviteFriendsLayout) {//邀请好友
            if (Preference.isLogin()) {
                Intent intent = new Intent(getActivity(), InviteFriendsActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        } else if (v == mCouponsLayout) {//我的优惠券
            if (Preference.isLogin()) {
                UMengCountUtil.UmengCount(getActivity(), UmengEventId.COUPONSACTIVITY);
                Intent intent = new Intent(getActivity(), CouponsActivity.class);
                intent.putExtra(CouponsActivity.TYPE_MY_COUPONS, CouponsActivity.TYPE_MY_COUPONS);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        } else if (v == mContactJoinLayout) {//联系加盟
            Intent intent = new Intent(getActivity(), ContactJonInActivity.class);
            startActivity(intent);
        } else if (v == mBecomeTeacherLayout) {//成为教练
            Intent intent = new Intent(getActivity(), BecomeTeacherActivity.class);
            startActivity(intent);
        } else if (v == mAboutUsLayout) {//关于
            Intent intent = new Intent(getActivity(), WriteNameActivity.class);
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
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(InitApiFinishedMessage message) {
        if (message.isSuccess()) {
            mStateView.setState(StateView.State.SUCCESS);
            setLogonView();
            getUserExerciseData();
        }
    }
}
