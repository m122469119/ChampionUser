package com.goodchef.liking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:每日运动
 * Author : shaozucheng
 * Time: 下午6:46
 * version 1.0.0
 */

public class EveryDaySportActivity extends AppBarActivity {

    @BindView(R.id.layout_today_step)
    RelativeLayout mTodayStepLayout;
    @BindView(R.id.layout_today_distance)
    RelativeLayout mTodayDistanceLayout;
    @BindView(R.id.layout_today_kcal)
    RelativeLayout mTodayKcalLayout;
    @BindView(R.id.layout_average_heart_rate)
    RelativeLayout mTodayHeartRateLayout;

    @BindView(R.id.layout_today_total_step)
    RelativeLayout mTotalStelpLayout;
    @BindView(R.id.layout_today_total_distance)
    RelativeLayout mTotalDistanceLayout;
    @BindView(R.id.layout_today_total_kcal)
    RelativeLayout mTotalKcalLayout;
    @BindView(R.id.layout_today_total_average_heart_rate)
    RelativeLayout mTotalHeartRateLayout;

    TextView mTodayStepTextView;
    TextView mTodayDistanceTextView;
    TextView mTodayKcalTextView;
    TextView mTodayAverageHeartRateTextView;

    TextView mTotoalStepTextView;
    TextView mTotalDistanceTextView;
    TextView mTotalKcalTextView;
    TextView mTotalAverageHeartRateTextView;

    @BindView(R.id.synchronization_sate_TextView)
    TextView mSynchronizationSateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_day_sport);
        ButterKnife.bind(this);
        setTitle("日常运动");
        setTodayData();
        setTotalData();
    }

    private void setTodayData() {
        initTodayView(mTodayStepLayout, "今日步数", "Step");
        initTodayView(mTodayDistanceLayout, "距离", "Km");
        initTodayView(mTodayKcalLayout, "卡路里", "Kcal");
        initTodayView(mTodayHeartRateLayout, "平均心率", "Bpm");
    }

    private void setTotalData() {
        initTotalView(mTotalStelpLayout, "总步数", "Step");
        initTotalView(mTotalDistanceLayout, "总距离", "Km");
        initTotalView(mTotalKcalLayout, "总消耗", "Kcal");
        initTotalView(mTotalHeartRateLayout, "平均心率", "Bpm");
    }

    private void initTodayView(View view, String title, String unit) {
        TextView titleTextView = (TextView) view.findViewById(R.id.every_day_data_title);
        TextView unitTextView = (TextView) view.findViewById(R.id.every_day_data_unit);
        TextView contentTextView = (TextView) view.findViewById(R.id.every_day_data_content);
        titleTextView.setText(title);
        unitTextView.setText(unit);
        switch (view.getId()) {
            case R.id.layout_today_step:
                mTodayStepTextView = contentTextView;
                break;
            case R.id.layout_today_distance:
                mTodayDistanceTextView = contentTextView;
                break;
            case R.id.layout_today_kcal:
                mTodayKcalTextView = contentTextView;
                break;
            case R.id.layout_average_heart_rate:
                mTodayAverageHeartRateTextView = contentTextView;
                break;
        }

    }


    private void initTotalView(View view, String title, String unit) {
        TextView titleTextView = (TextView) view.findViewById(R.id.every_day_total_data_title);
        TextView unitTextView = (TextView) view.findViewById(R.id.every_day_total_data_unit);
        TextView contentTextView = (TextView) view.findViewById(R.id.every_day_total_data_content);
        titleTextView.setText(title);
        unitTextView.setText(unit);
        switch (view.getId()) {
            case R.id.layout_today_total_step:
                mTotoalStepTextView = contentTextView;
                break;
            case R.id.layout_today_total_distance:
                mTotalDistanceTextView = contentTextView;
                break;
            case R.id.layout_today_total_kcal:
                mTotalKcalTextView = contentTextView;
                break;
            case R.id.layout_today_total_average_heart_rate:
                mTotalAverageHeartRateTextView = contentTextView;
                break;
        }
    }
}
