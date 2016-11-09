package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.framework.library.imageloader.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.SelfHelpGroupCoursesDateAdapter;
import com.goodchef.liking.http.result.SelfHelpGroupCoursesResult;
import com.goodchef.liking.mvp.presenter.SelfHelpGroupCoursesPresenter;
import com.goodchef.liking.mvp.view.SelfHelpGroupCoursesView;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

/**
 * 说明:
 * Author: shaozucheng
 * Time: 下午4:05
 */

public class SelfHelpGroupActivity extends AppBarActivity implements View.OnClickListener, SelfHelpGroupCoursesView {

    private RecyclerView mCoursesTimeRecyclerView;
    private TextView mUserTimeTextView;
    private RecyclerView mGymRecyclerView;
    private HImageView mSelfGymHImageView;
    private TextView mCoursesTrainTextView;
    private TextView mGroupCoursesTimeTextView;
    private TextView mGroupCoursesStrongTextView;
    private TextView mCoursesIntroduceTextView;
    private TextView mAccommodateNumberTextView;
    private LinearLayout mSelectCoursesLayout;
    private LikingStateView mLikingStateView;

    private SelfHelpGroupCoursesPresenter mSelfHelpGroupCoursesPresenter;
    private SelfHelpGroupCoursesDateAdapter mSelfHelpGroupCoursesDateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_help_group);
        initView();
        setViewOnClickListener();
        sendRequest();
    }

    private void initView() {
        mCoursesTimeRecyclerView = (RecyclerView) findViewById(R.id.self_help_group_courses_time_RecyclerView);
        mUserTimeTextView = (TextView) findViewById(R.id.self_help_user_time);
        mGymRecyclerView = (RecyclerView) findViewById(R.id.self_help_gym_RecyclerView);
        mSelfGymHImageView = (HImageView) findViewById(R.id.self_help_gym_image);
        mCoursesTrainTextView = (TextView) findViewById(R.id.group_courses_train_object);
        mGroupCoursesTimeTextView = (TextView) findViewById(R.id.group_courses_time);
        mGroupCoursesStrongTextView = (TextView) findViewById(R.id.group_courses_strong);
        mCoursesIntroduceTextView = (TextView) findViewById(R.id.self_help_courses_introduce);
        mAccommodateNumberTextView = (TextView) findViewById(R.id.accommodate_user_number);
        mSelectCoursesLayout = (LinearLayout) findViewById(R.id.layout_select_group_courses);
        mLikingStateView = (LikingStateView) findViewById(R.id.self_help_stateView);
    }

    private void setViewOnClickListener() {
        mSelectCoursesLayout.setOnClickListener(this);
    }

    private void sendRequest() {
        if (mSelfHelpGroupCoursesPresenter == null) {
            mSelfHelpGroupCoursesPresenter = new SelfHelpGroupCoursesPresenter(this, this);
        }
        mLikingStateView.setState(StateView.State.LOADING);
        mSelfHelpGroupCoursesPresenter.getSelfHelpCourses(LikingHomeActivity.gymId);
    }

    @Override
    public void onClick(View v) {
        if (v == mSelectCoursesLayout) {

        }
    }

    @Override
    public void updateSelfHelpGroupCoursesView(SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData selfHelpGroupCoursesData) {
        mLikingStateView.setState(StateView.State.SUCCESS);
        List<SelfHelpGroupCoursesResult.SelfHelpGroupCoursesData.TimeData> timeDataList = selfHelpGroupCoursesData.getTime();
        if (timeDataList != null) {
            mCoursesTimeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mSelfHelpGroupCoursesDateAdapter = new SelfHelpGroupCoursesDateAdapter(this);
            mSelfHelpGroupCoursesDateAdapter.setData(timeDataList);
            mCoursesTimeRecyclerView.setAdapter(mSelfHelpGroupCoursesDateAdapter);
        }
    }

    @Override
    public void handleNetworkFailure() {
        mLikingStateView.setState(StateView.State.FAILED);
    }
}
