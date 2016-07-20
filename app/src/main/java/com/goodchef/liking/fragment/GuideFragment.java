package com.goodchef.liking.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.BaseFragment;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.LikingHomeActivity;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/20 下午6:08
 */
public class GuideFragment extends BaseFragment implements View.OnClickListener {

    private int mNum; //页号
    private LinearLayout mLayoutGuideIntellect;
    private LinearLayout mLayoutGuidePrivateTeacher;
    private LinearLayout mLayoutGuideData;
    private TextView mJumpBtn;


    public static GuideFragment newInstance(int num) {
        Bundle args = new Bundle();
        args.putInt("num", num);
        GuideFragment fragment = new GuideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgment_guide, container, false);
        mLayoutGuideIntellect = (LinearLayout) view.findViewById(R.id.layout_guide_intellect_exercise);
        mLayoutGuidePrivateTeacher = (LinearLayout) view.findViewById(R.id.layout_guide_private_teacher);
        mLayoutGuideData = (LinearLayout) view.findViewById(R.id.layout_guide_data);
        mJumpBtn = (TextView) view.findViewById(R.id.jump_main_btn);
        mJumpBtn.setOnClickListener(this);
        initData();
        return view;
    }

    private void initData() {
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        if (mNum == 0) {
            mLayoutGuideIntellect.setVisibility(View.VISIBLE);
            mLayoutGuidePrivateTeacher.setVisibility(View.GONE);
            mLayoutGuideData.setVisibility(View.GONE);
        } else if (mNum == 1) {
            mLayoutGuideIntellect.setVisibility(View.GONE);
            mLayoutGuidePrivateTeacher.setVisibility(View.VISIBLE);
            mLayoutGuideData.setVisibility(View.GONE);
        } else if (mNum == 2) {
            mLayoutGuideIntellect.setVisibility(View.GONE);
            mLayoutGuidePrivateTeacher.setVisibility(View.GONE);
            mLayoutGuideData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mJumpBtn) {
            startActivity(new Intent(getActivity(), LikingHomeActivity.class));
            getActivity().finish();
        }
    }
}
