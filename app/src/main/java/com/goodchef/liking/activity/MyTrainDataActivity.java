package com.goodchef.liking.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.aaron.android.framework.base.actionbar.AppBarActivity;
import com.goodchef.liking.R;

/**
 * 说明:训练数据
 * Author shaozucheng
 * Time:16/7/2 下午4:45
 */
public class MyTrainDataActivity extends AppBarActivity {
    private TextView mTrainTime;//训练时间
    private TextView mTrainDistance;//训练距离
    private TextView mTrainCal;//消耗卡路里

    private TextView mTrainCountAll;//训练总次数
    private TextView mTrainTimeAll;//训练总时间
    private TextView mTrainDistanceALL;//训练总距离
    private TextView mTrainCalALl;//消耗总卡路里


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_train_data);
        setTitle(getString(R.string.title_my_train_data));
        initView();
    }

    private void initView() {
        mTrainTime = (TextView) findViewById(R.id.my_train_time);
        mTrainDistance = (TextView) findViewById(R.id.my_train_distance);
        mTrainCal = (TextView) findViewById(R.id.my_train_cal);

        mTrainCountAll = (TextView) findViewById(R.id.my_train_count_all);
        mTrainTimeAll = (TextView) findViewById(R.id.my_train_time_all);
        mTrainDistanceALL = (TextView) findViewById(R.id.my_train_distance_all);
        mTrainCalALl = (TextView) findViewById(R.id.my_train_cal_all);
    }
}