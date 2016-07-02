package com.goodchef.liking.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.BaseFragment;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.PopupUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.AboutActivity;
import com.goodchef.liking.activity.BecomeTeacherActivity;
import com.goodchef.liking.activity.ContactJonInActivity;
import com.goodchef.liking.activity.CouponsActivity;
import com.goodchef.liking.activity.LessonActivity;
import com.goodchef.liking.activity.LoginActivity;
import com.goodchef.liking.activity.MyCardActivity;
import com.goodchef.liking.activity.MyInfoActivity;
import com.goodchef.liking.activity.MyOrderActivity;
import com.goodchef.liking.activity.MyTrainDataActivity;
import com.goodchef.liking.http.result.UserLoginResult;
import com.goodchef.liking.http.result.VerificationCodeResult;
import com.goodchef.liking.mvp.presenter.LoginPresenter;
import com.goodchef.liking.mvp.view.LoginView;
import com.goodchef.liking.storage.Preference;

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
    private TextView mLoginOutBtn;//退出登录

    private LinearLayout mMyCourseLayout;//我的课程
    private LinearLayout mMyOrderLayout;//我的订单
    private LinearLayout mMemberCardLayout;//会员卡
    private LinearLayout mCouponsLayout;//我的优惠券

    private RelativeLayout mTrainLayout;
    private TextView myTrainTime;
    private TextView myTrainDistance;
    private TextView myTrainCal;


    public static final String NULL_STRING = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liking_my, container, false);
        initView(view);
        initViewIconAndText();
        setViewOnClickListener();
        return view;
    }

    private void initView(View view) {
        mHeadInfoLayout = (RelativeLayout) view.findViewById(R.id.layout_head_info);
        mInviteFriendsLayout = (LinearLayout) view.findViewById(R.id.layout_invite_friends);
        mContactJoinLayout = (LinearLayout) view.findViewById(R.id.layout_contact_join);
        mBecomeTeacherLayout = (LinearLayout) view.findViewById(R.id.layout_become_teacher);
        mAboutUsLayout = (LinearLayout) view.findViewById(R.id.layout_about_us);

        mHeadHImageView = (HImageView) view.findViewById(R.id.head_image);
        mLoginOutBtn = (TextView) view.findViewById(R.id.login_out_btn);

        mMyCourseLayout = (LinearLayout) view.findViewById(R.id.layout_my_course);
        mMyOrderLayout = (LinearLayout) view.findViewById(R.id.layout_my_order);
        mMemberCardLayout = (LinearLayout) view.findViewById(R.id.layout_member_card);
        mCouponsLayout = (LinearLayout) view.findViewById(R.id.layout_coupons);

        mTrainLayout = (RelativeLayout) view.findViewById(R.id.layout_my_train);
        myTrainTime = (TextView) view.findViewById(R.id.my_train_time);
        myTrainDistance = (TextView) view.findViewById(R.id.my_train_distance);
        myTrainCal = (TextView) view.findViewById(R.id.my_train_cal);
    }

    private void setViewOnClickListener() {
        mInviteFriendsLayout.setOnClickListener(this);
        mContactJoinLayout.setOnClickListener(this);
        mBecomeTeacherLayout.setOnClickListener(this);
        mAboutUsLayout.setOnClickListener(this);
        mHeadInfoLayout.setOnClickListener(this);
        mHeadHImageView.setOnClickListener(this);
        mLoginOutBtn.setOnClickListener(this);

        mMyCourseLayout.setOnClickListener(this);
        mMyOrderLayout.setOnClickListener(this);
        mMemberCardLayout.setOnClickListener(this);
        mCouponsLayout.setOnClickListener(this);
        mTrainLayout.setOnClickListener(this);
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
            Intent intent = new Intent(getActivity(), MyTrainDataActivity.class);
            startActivity(intent);
        } else if (v == mHeadHImageView) {//头像
            Intent intent = new Intent(getActivity(), MyInfoActivity.class);
            startActivity(intent);
        } else if (v == mHeadInfoLayout) {
            if (Preference.isLogin()) {
                PopupUtils.showToast("您已登录状态");
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        } else if (v == mMyCourseLayout) {//我的课程
            Intent intent = new Intent(getActivity(), LessonActivity.class);
            startActivity(intent);
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
                Intent intent = new Intent(getActivity(), MyCardActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        } else if (v == mInviteFriendsLayout) {//邀请好友
            PopupUtils.showToast("邀请好友开发中");
        } else if (v == mCouponsLayout) {//我的优惠券
            if (Preference.isLogin()) {
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
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
        } else if (v == mLoginOutBtn) {//退出登录
            if (Preference.isLogin()) {
                showExitDialog();
            } else {
                PopupUtils.showToast("您还没有登录");
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
    }
}
