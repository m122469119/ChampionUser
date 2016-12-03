package com.goodchef.liking.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.swipeback.app.SwipeBackActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.goodchef.liking.R;
import com.goodchef.liking.utils.ChartColorUtil;

import java.util.ArrayList;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午10:21
 * version 1.0.0
 */

public class BodyTestDataActivity extends SwipeBackActivity implements View.OnClickListener {

    private RadarChart mBodyIngredientRadarChart;//身体分析雷达图
    private TextView mBodyElementHistoryTextView;//身体成分历史记录
    private TextView mBodyTestHistoryTextView;//体测历史

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_test_data);
        initView();
        setBodyIngredientRadarChart();
        initViewOnClickListener();
    }

    private void initView() {
        mBodyIngredientRadarChart = (RadarChart) findViewById(R.id.body_ingredient_RadarChart);
        mBodyElementHistoryTextView = (TextView) findViewById(R.id.body_element_history_TextView);

        mBodyTestHistoryTextView = (TextView) findViewById(R.id.body_test_history_TextView);
    }

    private void initViewOnClickListener() {
        mBodyElementHistoryTextView.setOnClickListener(this);
        mBodyTestHistoryTextView.setOnClickListener(this);
    }

    private void setBodyIngredientRadarChart() {
        mBodyIngredientRadarChart.setBackgroundColor(Color.rgb(255, 255, 255));
        mBodyIngredientRadarChart.setWebLineWidth(1f);
        mBodyIngredientRadarChart.setWebColor(ChartColorUtil.CHART_LIGHT_GRAY);
        mBodyIngredientRadarChart.setWebLineWidthInner(1f);
        mBodyIngredientRadarChart.setWebColorInner(ChartColorUtil.CHART_LIGHT_GRAY);
        mBodyIngredientRadarChart.setWebAlpha(255);
        mBodyIngredientRadarChart.setDescription("");//去掉说明
        mBodyIngredientRadarChart.setRotationEnabled(false);//设置旋转
        setData();

        mBodyIngredientRadarChart.animateXY(1400, 1400, Easing.EasingOption.EaseInOutQuad, Easing.EasingOption.EaseInOutQuad);
        XAxis xAxis = mBodyIngredientRadarChart.getXAxis();

        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setAxisMaxValue(80f);

        xAxis.setValueFormatter(new AxisValueFormatter() {

            private String[] mActivities = new String[]{"Burger", "Steak", "Salad", "Pasta", "Pizza"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = mBodyIngredientRadarChart.getYAxis();
        yAxis.setLabelCount(2, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinValue(0f);
        yAxis.setAxisMaxValue(80f);
        yAxis.setDrawLabels(false); // no axis labels
        yAxis.setDrawAxisLine(false); // no axis line
        yAxis.setDrawGridLines(false); // no grid lines
        yAxis.setDrawZeroLine(false); // draw a zero line
        yAxis.setEnabled(false);

        mBodyIngredientRadarChart.setDrawWeb(true);

    }

    public void setData() {

        float mult = 80;
        float min = 0;
        int cnt = 5;

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            float val1 = 80 + (float) i;
            entries1.add(new RadarEntry(val1));

            float val2 = 50 + (float) i;
            entries2.add(new RadarEntry(val2));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "");
        set1.setColor(ChartColorUtil.CHART_LIGHT_GRAY);
        set1.setFillColor(ChartColorUtil.CHART_LIGHT_GRAY);
        set1.setDrawFilled(true);
        set1.setFillAlpha(100);
        set1.setLineWidth(1f);
        set1.setDrawHighlightCircleEnabled(false);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "");
        set2.setColor(ChartColorUtil.CHART_LIGHT_GREEN);
        set2.setFillColor(ChartColorUtil.CHART_LIGHT_GREEN);
        set2.setDrawFilled(true);
        set2.setFillAlpha(50);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(false);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);

        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.BLACK);

        mBodyIngredientRadarChart.setData(data);
        mBodyIngredientRadarChart.invalidate();
    }


    @Override
    public void onClick(View v) {
        if (v == mBodyElementHistoryTextView) {//身体成分历史记录
            startActivity(BodyAnalyzeChartActivity.class);
        } else if (v == mBodyTestHistoryTextView) {
            startActivity(BodyTestHistoryActivity.class);
        }
    }
}
