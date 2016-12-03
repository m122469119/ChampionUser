package com.goodchef.liking.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.framework.base.ui.BaseFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.goodchef.liking.R;
import com.goodchef.liking.http.result.data.BodyChartValueFormatter;
import com.goodchef.liking.utils.ChartColorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:33
 * version 1.0.0
 */

public class BodyAnalyzeChartFragment extends BaseFragment {

    private LineChart mLineChart;
    private List<String> totalList = new ArrayList<>();
    private List<String> dateList = new ArrayList<>();


    public static BodyAnalyzeChartFragment newInstance() {
        Bundle args = new Bundle();
        BodyAnalyzeChartFragment fragment = new BodyAnalyzeChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analyze, container, false);
        mLineChart = (LineChart) view.findViewById(R.id.analyze_LineChart);
        initChartData();
        return view;
    }

    private void initChartData() {
        for (int i = 30; i < 80; i++) {
            totalList.add(i + "");
            dateList.add("16/12" + i);
        }
        setChartView();
    }

    private void setChartView() {
        mLineChart.setDrawGridBackground(false);
        mLineChart.setBackgroundColor(ChartColorUtil.CHART_LIGHT_BLACK);
        mLineChart.animateXY(3000, 3000);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(false);
        mLineChart.setData(generateLineData(totalList));
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.setDescription("");
        mLineChart.setExtraOffsets(0, 0, 20, 20);
        float scaleX = (float) dateList.size() / 4f;
        mLineChart.zoom(scaleX, 1, 0, 0);//设置横向向右扩展,固定横轴为4个坐标,多的会自动向右伸展
        if (dateList.size() > 0 && totalList.size() > 0) {
            int SelectY = totalList.size() - 1;
            int sleectX = dateList.size() - 1;
            mLineChart.highlightValue(new Highlight((float) sleectX, SelectY), true);
            // mLineChart.moveViewToX((float) sleectX);
        }
        //  mLineChart.setOnChartValueSelectedListener(mOnChartValueSelectedListener);
        mLineChart.invalidate();

        Legend l = mLineChart.getLegend();
        l.setEnabled(false);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setAxisMinValue(0f);
        //  leftAxis.setAxisMaxValue(100f);
//        leftAxis.setDrawZeroLine(false);//设置0线
//        leftAxis.setDrawAxisLine(true);
//        leftAxis.setEnabled(false);
//        leftAxis.setDrawGridLines(true);
        leftAxis.setTextColor(ChartColorUtil.CHART_LIGHT_BLACK);
        leftAxis.setTextSize(1f);
        leftAxis.setXOffset(5f);
        leftAxis.setAxisLineColor(ChartColorUtil.CHART_LIGHT_BLACK);//设置y轴的颜色
        leftAxis.setGridColor(ChartColorUtil.CHART_LIGHT_GRAY);//设置网格线的颜色


        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);//设置为1个间距
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisMinValue(0);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(ChartColorUtil.CHART_WHITE);
        AxisValueFormatter formatter = new BodyChartValueFormatter(mLineChart, dateList);
        xAxis.setValueFormatter(formatter);
    }

    /**
     * 设置折线图数据
     *
     * @return
     */
    private LineData generateLineData(List<String> totalList) {
        ArrayList<ILineDataSet> sets = new ArrayList<>();

        ArrayList<Entry> yVals0 = new ArrayList<>();
        for (int i = 0; i < totalList.size(); i++) {
            float val0 = Float.parseFloat(totalList.get(i));
            yVals0.add(new Entry(i, val0));
        }
        LineDataSet ds0 = new LineDataSet(yVals0, "");
        ds0.setLineWidth(6f);//设置折线图的宽度
        ds0.setDrawCircles(true);
        ds0.setColor(ChartColorUtil.CHART_LIGHT_GREEN);//设置折线图的颜色 红色
        ds0.setCircleColor(ChartColorUtil.CHART_LIGHT_GREEN);//设置折线图圆圈的颜色
        ds0.setCircleRadius(8f);//设置圆圈的半径
        ds0.setDrawCircleHole(true);//设置圆圈空心还是实心,false为实心
        ds0.setCircleHoleRadius(4f);
        ds0.setDrawValues(true);
        ds0.setValueTextSize(13f);
        ds0.setValueTextColor(ChartColorUtil.CHART_WHITE);
        ds0.setHighlightEnabled(true);
        ds0.setDrawFilled(true);
        sets.add(ds0);
        LineData d = new LineData(sets);
        return d;
    }


}
