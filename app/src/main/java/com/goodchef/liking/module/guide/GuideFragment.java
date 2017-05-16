package com.goodchef.liking.module.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.BaseFragment;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.LikingHomeActivity;
import com.goodchef.liking.utils.NumberConstantUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/20 下午6:08
 */
public class GuideFragment extends BaseFragment implements View.OnClickListener {

    public static final String NUM = "num";
    @BindView(R.id.layout_guide_intellect_exercise)
    LinearLayout mLayoutGuideIntellect;
    @BindView(R.id.layout_guide_private_teacher)
    LinearLayout mLayoutGuidePrivateTeacher;
    @BindView(R.id.jump_main_btn)
    TextView mJumpBtn;
    @BindView(R.id.layout_guide_data)
    LinearLayout mLayoutGuideData;
    Unbinder unbinder;
    private int mNum; //页号

    public static GuideFragment newInstance(int num) {
        Bundle args = new Bundle();
        args.putInt(NUM, num);
        GuideFragment fragment = new GuideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frgment_guide, container, false);
        unbinder = ButterKnife.bind(this, view);
        mJumpBtn.setOnClickListener(this);
        initData();
        return view;
    }

    private void initData() {
        mNum = getArguments() != null ? getArguments().getInt(NUM) : NumberConstantUtil.ONE;
        if (mNum == NumberConstantUtil.ZERO) {
            mLayoutGuideIntellect.setVisibility(View.VISIBLE);
            mLayoutGuidePrivateTeacher.setVisibility(View.GONE);
            mLayoutGuideData.setVisibility(View.GONE);
        } else if (mNum == NumberConstantUtil.ONE) {
            mLayoutGuideIntellect.setVisibility(View.GONE);
            mLayoutGuidePrivateTeacher.setVisibility(View.VISIBLE);
            mLayoutGuideData.setVisibility(View.GONE);
        } else if (mNum == NumberConstantUtil.TWO) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
