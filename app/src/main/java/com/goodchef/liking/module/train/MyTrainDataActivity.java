package com.goodchef.liking.module.train;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.UserExerciseResult;
import com.goodchef.liking.data.remote.retrofit.result.data.ShareData;
import com.goodchef.liking.utils.ShareUtils;
import com.goodchef.liking.utils.TypefaseUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:训练数据
 * Author shaozucheng
 * Time:16/7/2 下午4:45
 */

public class MyTrainDataActivity extends AppBarMVPSwipeBackActivity<UserExerciseContract.Presenter> implements UserExerciseContract.View {

    @BindView(R.id.my_train_time) TextView mTrainTime;//训练时间
    @BindView(R.id.my_train_distance) TextView mTrainDistance;//训练距离
    @BindView(R.id.my_train_cal) TextView mTrainCal;//消耗卡路里

    @BindView(R.id.my_train_count_all) TextView mTrainCountAll;//训练总次数
    @BindView(R.id.my_train_time_all) TextView mTrainTimeAll;//训练总时间
    @BindView(R.id.my_train_distance_all) TextView mTrainDistanceALL;//训练总距离
    @BindView(R.id.my_train_cal_all) TextView mTrainCalALl;//消耗总卡路里

    @BindView(R.id.my_train_state_view)
    LikingStateView mStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_train_data);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_my_train_data));
        initView();
        iniData();
        showRightMenu(getString(R.string.share), shareListener);
    }

    private void initView() {
        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                iniData();
            }
        });
    }

    private void iniData() {
        mPresenter.getExerciseData();
    }

    @Override
    public void updateUserExerciseView(UserExerciseResult.ExerciseData exerciseData) {
        if (exerciseData != null) {
            mStateView.setState(StateView.State.SUCCESS);
            Typeface typeFace = TypefaseUtil.getImpactTypeface(this);
            mTrainTime.setTypeface(typeFace);
            mTrainDistance.setTypeface(typeFace);
            mTrainCal.setTypeface(typeFace);
            mTrainCountAll.setTypeface(typeFace);
            mTrainDistanceALL.setTypeface(typeFace);
            mTrainCalALl.setTypeface(typeFace);
            mTrainTimeAll.setTypeface(typeFace);

            mTrainTime.setText(exerciseData.getTodayMin());
            mTrainDistance.setText(exerciseData.getTodayDistance());
            mTrainCal.setText(exerciseData.getTodayCal());
            mTrainCountAll.setText(exerciseData.getTotalTimes());
            mTrainTimeAll.setText(exerciseData.getTotalMin());
            mTrainDistanceALL.setText(exerciseData.getTotalDistance());
            mTrainCalALl.setText(exerciseData.getTotalCal());
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }

    }

    private android.view.View.OnClickListener shareListener = new android.view.View.OnClickListener() {
        @Override
        public void onClick(android.view.View v) {
            mPresenter.getUserShareData();
            showRightMenu(getString(R.string.share), null);
        }
    };

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }

    @Override
    public void updateShareView(ShareData shareData) {
        ShareUtils.showShareDialog(this,shareData);
        showRightMenu(getString(R.string.share), shareListener);
    }

    @Override
    public void setPresenter() {
        mPresenter = new UserExerciseContract.Presenter();
    }
}
