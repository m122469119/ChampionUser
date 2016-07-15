package com.goodchef.liking.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.mvp.presenter.UserExercisePresenter;
import com.goodchef.liking.mvp.view.UserExerciseView;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * 说明:训练数据
 * Author shaozucheng
 * Time:16/7/2 下午4:45
 */
public class MyTrainDataActivity extends AppBarActivity implements UserExerciseView {
    private TextView mTrainTime;//训练时间
    private TextView mTrainDistance;//训练距离
    private TextView mTrainCal;//消耗卡路里

    private TextView mTrainCountAll;//训练总次数
    private TextView mTrainTimeAll;//训练总时间
    private TextView mTrainDistanceALL;//训练总距离
    private TextView mTrainCalALl;//消耗总卡路里

    private UserExercisePresenter mUserExercisePresenter;
    private LikingStateView mStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_train_data);
        setTitle(getString(R.string.title_my_train_data));
        initView();
        mUserExercisePresenter = new UserExercisePresenter(this, this);
        iniData();
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.my_train_state_view);
        mTrainTime = (TextView) findViewById(R.id.my_train_time);
        mTrainDistance = (TextView) findViewById(R.id.my_train_distance);
        mTrainCal = (TextView) findViewById(R.id.my_train_cal);

        mTrainCountAll = (TextView) findViewById(R.id.my_train_count_all);
        mTrainTimeAll = (TextView) findViewById(R.id.my_train_time_all);
        mTrainDistanceALL = (TextView) findViewById(R.id.my_train_distance_all);
        mTrainCalALl = (TextView) findViewById(R.id.my_train_cal_all);

        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                iniData();
            }
        });
    }

    private void iniData() {
        mUserExercisePresenter.getExerciseData();
    }

    @Override
    public void updateUserExerciseView(UserExerciseResult.ExerciseData exerciseData) {
        if (exerciseData != null) {
            mStateView.setState(StateView.State.SUCCESS);
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

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }
}
