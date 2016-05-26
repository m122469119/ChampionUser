package com.chushi007.android.liking.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.BaseFragment;
import com.chushi007.android.liking.R;
import com.chushi007.android.liking.activity.AboutActivity;
import com.chushi007.android.liking.activity.BecomeTeacherActivity;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingMyFragment extends BaseFragment implements View.OnClickListener{
    private LinearLayout mInviteFriendsLayout;//邀请好友
    private LinearLayout mCouponsLayout;//我的优惠券
    private LinearLayout mContactJoinLayout;//联系加盟
    private LinearLayout mBecomeTeacherLayout;//称为教练
    private LinearLayout mAboutUsLayout;//关于我们


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liking_my, container, false);
        initView(view);
        initViewIconAndText();
        return view;
    }

    private void initView(View view) {
        mInviteFriendsLayout = (LinearLayout) view.findViewById(R.id.layout_invite_friends);
        mCouponsLayout = (LinearLayout) view.findViewById(R.id.layout_coupons);
        mContactJoinLayout = (LinearLayout) view.findViewById(R.id.layout_contact_join);
        mBecomeTeacherLayout = (LinearLayout) view.findViewById(R.id.layout_become_teacher);
        mAboutUsLayout = (LinearLayout) view.findViewById(R.id.layout_about_us);

        mInviteFriendsLayout.setOnClickListener(this);
        mCouponsLayout.setOnClickListener(this);
        mContactJoinLayout.setOnClickListener(this);
        mBecomeTeacherLayout.setOnClickListener(this);
        mAboutUsLayout.setOnClickListener(this);
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
        if (v == mInviteFriendsLayout){

        }else if (v == mCouponsLayout){

        }else if (v == mContactJoinLayout){

        }else if (v == mBecomeTeacherLayout){
            Intent intent = new Intent(getActivity(), BecomeTeacherActivity.class);
            startActivity(intent);
        }else if (v == mAboutUsLayout){
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
        }
    }
}
