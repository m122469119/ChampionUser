package com.goodchef.liking.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.goodchef.liking.activity.LessonActivity;
import com.goodchef.liking.activity.LoginActivity;
import com.goodchef.liking.activity.MyInfoActivity;
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
    private LinearLayout mCouponsLayout;//我的优惠券
    private LinearLayout mContactJoinLayout;//联系加盟
    private LinearLayout mBecomeTeacherLayout;//称为教练
    private LinearLayout mAboutUsLayout;//关于我们

    private RelativeLayout mHeadInfoLayout;//头像布局
    private HImageView mHeadHImageView;//头像
    private TextView mLoginOutBtn;//退出登录

    private LinearLayout mPracticeDataLayout;//训练数据
    private LinearLayout mMyCourseLayout;//我的课程
    private LinearLayout mMyOrderLayout;//我的订单
    private LinearLayout mMyBalanceLayout;//我的余额

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
        mCouponsLayout = (LinearLayout) view.findViewById(R.id.layout_coupons);
        mContactJoinLayout = (LinearLayout) view.findViewById(R.id.layout_contact_join);
        mBecomeTeacherLayout = (LinearLayout) view.findViewById(R.id.layout_become_teacher);
        mAboutUsLayout = (LinearLayout) view.findViewById(R.id.layout_about_us);

        mHeadHImageView = (HImageView) view.findViewById(R.id.head_image);
        mLoginOutBtn = (TextView) view.findViewById(R.id.login_out_btn);

        mPracticeDataLayout = (LinearLayout) view.findViewById(R.id.layout_practice_data);
        mMyCourseLayout = (LinearLayout) view.findViewById(R.id.layout_my_course);
        mMyOrderLayout = (LinearLayout) view.findViewById(R.id.layout_my_order);
        mMyBalanceLayout = (LinearLayout) view.findViewById(R.id.layout_my_balance);

    }

    private void setViewOnClickListener() {
        mInviteFriendsLayout.setOnClickListener(this);
        mCouponsLayout.setOnClickListener(this);
        mContactJoinLayout.setOnClickListener(this);
        mBecomeTeacherLayout.setOnClickListener(this);
        mAboutUsLayout.setOnClickListener(this);
        mHeadInfoLayout.setOnClickListener(this);
        mHeadHImageView.setOnClickListener(this);
        mLoginOutBtn.setOnClickListener(this);
        mPracticeDataLayout.setOnClickListener(this);
        mMyCourseLayout.setOnClickListener(this);
        mMyOrderLayout.setOnClickListener(this);
        mMyBalanceLayout.setOnClickListener(this);
    }


    private void initViewIconAndText() {
        setMySettingCard(mInviteFriendsLayout, 0, R.string.layout_invite_friends, true);
        setMySettingCard(mCouponsLayout, 0, R.string.layout_coupons, true);
        setMySettingCard(mContactJoinLayout, 0, R.string.layout_contact_join, true);
        setMySettingCard(mBecomeTeacherLayout, 0, R.string.layout_become_teacher, true);
        setMySettingCard(mAboutUsLayout, 0, R.string.layout_about_us, false);
    }

    private void setMySettingCard(View view, int drawableResId, int text, boolean isShowLine) {
        ImageView icon = (ImageView) view.findViewById(R.id.standard_my_icon);
        TextView textView = (TextView) view.findViewById(R.id.standard_my_text);
        View line = view.findViewById(R.id.standard_view_line);
        // icon.setImageDrawable(getResources().getDrawable(drawableResId));
        textView.setText(text);
        if (isShowLine) {
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mHeadHImageView) {//头像
            Intent intent = new Intent(getActivity(), MyInfoActivity.class);
            startActivity(intent);
        } else if (v == mHeadInfoLayout) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else if (v == mPracticeDataLayout) {//训练数据

        } else if (v == mMyCourseLayout) {//我的课程
            Intent intent = new Intent(getActivity(), LessonActivity.class);
            startActivity(intent);
        } else if (v == mMyOrderLayout) {//我的订单

        } else if (v == mMyBalanceLayout) {//我的余额

        } else if (v == mInviteFriendsLayout) {//邀请好友

        } else if (v == mCouponsLayout) {//我的优惠券

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