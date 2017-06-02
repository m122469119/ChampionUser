package com.goodchef.liking.module.body.analyze;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aaron.android.framework.base.mvp.BaseMVPFragment;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.common.utils.ListUtils;
import com.aaron.common.utils.StringUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.BodyAnalyzeHistoryResult;
import com.goodchef.liking.data.remote.retrofit.result.data.BodyChartValueFormatter;
import com.goodchef.liking.data.remote.retrofit.result.data.BodyHistoryData;
import com.goodchef.liking.eventmessages.BodyAnalyzeHistoryMessage;
import com.goodchef.liking.utils.ChartColorUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:33
 * version 1.0.0
 */

public class BodyAnalyzeChartFragment extends BaseMVPFragment<BodyAnalyzeHistoryContract.Presenter>
        implements BodyAnalyzeHistoryContract.View {

    private LineChart mLineChart;
    private List<String> totalList = new ArrayList<>();
    private List<String> dateList = new ArrayList<>();
    private String unit = "";
    private String modules;
    public static String KEY_HISTORY_LIST = "key_history_list";
    public static String KEY_HISTORY_UNIT = "key_history_unit";
    public static String KEY_HISTORY_MODULES = "key_history_modules";

    private LikingStateView mLikingStateView;

    public static BodyAnalyzeChartFragment newInstance(Bundle args) {
        BodyAnalyzeChartFragment fragment = new BodyAnalyzeChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.fragment_analyze_state_view, container, false);
        mLikingStateView = (LikingStateView) view.findViewById(R.id.analyze_chart_stateView);
        mLineChart = (LineChart) view.findViewById(R.id.analyze_LineChart);
        List<BodyHistoryData> historyDataList = getArguments().getParcelableArrayList(KEY_HISTORY_LIST);
        unit = getArguments().getString(KEY_HISTORY_UNIT);
        modules = getArguments().getString(KEY_HISTORY_MODULES);
        initChartData(historyDataList, unit);
        return view;
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(BodyAnalyzeHistoryMessage message) {
        if (message != null) {
            sendRequest(message.getColumn());
            unit = message.getUnit();
            modules = message.getModules();
        }
    }

    /**
     * 发送请求
     *
     * @param column
     */
    private void sendRequest(String column) {
        mLikingStateView.setState(StateView.State.LOADING);
        mPresenter.getBodyAnalyzeHistory(column);
    }

    private void initChartData(List<BodyHistoryData> historyDataList, String unit) {
        dateList.clear();
        totalList.clear();
        float valueMax = 1f;
        float valueMin = 0f;
        if (ListUtils.isEmpty(historyDataList)) {
            mLikingStateView.setState(StateView.State.NO_DATA);
        } else {
            mLikingStateView.setState(StateView.State.SUCCESS);
        }
        for (int i = 0; i < historyDataList.size(); i++) {//遍历数据，将数据重组为图表需要的集合
            dateList.add(historyDataList.get(i).getBodyTime());
            String value = historyDataList.get(i).getValue();
            totalList.add(value);
            float valueFloat = Float.parseFloat(value);
            if (i == 0) {//选出最大值和最小值
                valueMax = valueFloat;
                valueMin = valueFloat;
            }
            if (valueMax < valueFloat) {
                valueMax = valueFloat;
            }
            if (valueMin > valueFloat) {
                valueMin = valueFloat;
            }
        }
        if (valueMax > 0f) {
            valueMax = valueMax * 2;
        } else if (valueMax < 0f) {
            valueMax = valueMax / 2;
        }

        if (valueMin < 0f) {
            valueMin = valueMin - 10;
        } else {
            valueMin = 0f;
        }

        if (valueMax >= 0f && valueMax < 10f) {
            valueMax += 10;
        }

        setChartView(unit, valueMax, valueMin);
    }

    private void setChartView(String unit, float valueMax, float valueMin) {
        mLineChart.setDrawGridBackground(false);
        mLineChart.setBackgroundColor(ChartColorUtil.CHART_LIGHT_BLACK);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(false);//设置这两个方法可以横向滑动
        mLineChart.setData(generateLineData(totalList, unit));
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.setDescription("");
        mLineChart.setHighlightPerTapEnabled(false);//去掉点击高亮效果
        mLineChart.setExtraOffsets(23f, 30f, 25f, 15f);
        mLineChart.setMinOffset(0f);
        if (dateList.size() > 0 && totalList.size() > 0) {
            int sleectX = dateList.size() - 1;
            mLineChart.moveViewToX((float) sleectX);
        }
        mLineChart.setVisibleXRangeMaximum(4f);//设置只显示4个，多余的往后移动
        mLineChart.invalidate();

        Legend l = mLineChart.getLegend();
        l.setEnabled(false);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setTextColor(ChartColorUtil.CHART_LIGHT_BLACK);
        leftAxis.setTextSize(1f);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setXOffset(0f);
        leftAxis.setAxisMaxValue(valueMax);
        leftAxis.setAxisMinValue(valueMin);
        leftAxis.setDrawLabels(false);//不显示y轴
        leftAxis.setAxisLineColor(ChartColorUtil.CHART_LIGHT_BLACK);//设置y轴的颜色
        leftAxis.setGridColor(ChartColorUtil.CHART_NORMAL_GRAY);//设置网格线的颜色

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);//设置为1个间距
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisMinValue(0);
        xAxis.setTextSize(10f);
        xAxis.setAxisLineColor(ChartColorUtil.CHART_NORMAL_GRAY);
        xAxis.setTextColor(ChartColorUtil.CHART_WHITE);
        AxisValueFormatter formatter = new BodyChartValueFormatter(dateList);
        xAxis.setValueFormatter(formatter);
    }

    /**
     * 设置折线图数据
     *
     * @return
     */
    private LineData generateLineData(List<String> totalList, final String unit) {
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        ArrayList<Entry> yVals0 = new ArrayList<>();
        for (int i = 0; i < totalList.size(); i++) {
            float val0 = Float.parseFloat(totalList.get(i));
            yVals0.add(new Entry(i, val0));
        }
        LineDataSet ds0 = new LineDataSet(yVals0, "");
        ds0.setLineWidth(4f);//设置折线图的宽度
        ds0.setDrawCircles(true);
        ds0.setColors(ChartColorUtil.getGradualChangeColor());
        ds0.setCircleColor(ChartColorUtil.CHART_LIGHT_GREEN);//设置折线图圆圈的颜色
        ds0.setCircleRadius(6f);//设置圆圈的半径
        ds0.setDrawCircleHole(true);//设置圆圈空心还是实心,false为实心
        ds0.setCircleHoleRadius(3f);
        ds0.setDrawValues(true);
        ds0.setValueTextSize(10f);
        ds0.setValueTextColor(ChartColorUtil.CHART_WHITE);
        ds0.setHighlightEnabled(true);
        ds0.setDrawFilled(true);
        ds0.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (!StringUtils.isEmpty(modules) && modules.equals("top_data")) {
                    DecimalFormat df = new DecimalFormat("#0");
                    return df.format(value) + unit;
                } else {
                    DecimalFormat df = new DecimalFormat("#0.0");
                    return df.format(value) + unit;
                }
            }
        });
        sets.add(ds0);
        LineData d = new LineData(sets);
        return d;
    }


    @Override
    public void updateBodyAnalyzeHistoryView(BodyAnalyzeHistoryResult.BodyHistory data) {
        mLikingStateView.setState(StateView.State.SUCCESS);
        List<BodyHistoryData> historyDataList = data.getHistoryData();
        initChartData(historyDataList, unit);
    }

    @Override
    public void changeStateView(StateView.State state) {
        mLikingStateView.setState(state);
    }

    @Override
    public void setPresenter() {
        mPresenter = new BodyAnalyzeHistoryContract.Presenter();
    }
}
