package com.goodchef.liking.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.utils.ListUtils;
import com.aaron.android.framework.base.ui.BaseFragment;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.BodyAnalyzeHistoryMessage;
import com.goodchef.liking.http.result.BodyAnalyzeHistoryResult;
import com.goodchef.liking.http.result.data.BodyChartValueFormatter;
import com.goodchef.liking.http.result.data.BodyHistoryData;
import com.goodchef.liking.mvp.presenter.BodyAnalyzeHistoryPresenter;
import com.goodchef.liking.mvp.view.BodyAnalyzeHistoryView;
import com.goodchef.liking.utils.ChartColorUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:33
 * version 1.0.0
 */

public class BodyAnalyzeChartFragment extends BaseFragment implements BodyAnalyzeHistoryView {

    private LineChart mLineChart;
    private List<String> totalList = new ArrayList<>();
    private List<String> dateList = new ArrayList<>();
    private List<BodyHistoryData> historyDataList;
    private BodyAnalyzeHistoryPresenter mBodyAnalyzeHistoryPresenter;
    public static String KEY_HISTORY_LIST = "key_history_list";
    private LikingStateView mLikingStateView;

    public static BodyAnalyzeChartFragment newInstance(Bundle args) {
        BodyAnalyzeChartFragment fragment = new BodyAnalyzeChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analyze_state_view, container, false);
        mLikingStateView = (LikingStateView) view.findViewById(R.id.analyze_chart_stateView);
        mLineChart = (LineChart) view.findViewById(R.id.analyze_LineChart);
        historyDataList = getArguments().getParcelableArrayList(KEY_HISTORY_LIST);
        initChartData(historyDataList);
        return view;
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(BodyAnalyzeHistoryMessage message) {
        if (message != null) {
            sendRequest(message.getColumn());
        }
    }

    private void sendRequest(String column) {
        if (mBodyAnalyzeHistoryPresenter == null) {
            mBodyAnalyzeHistoryPresenter = new BodyAnalyzeHistoryPresenter(getActivity(), this);
        }
        mLikingStateView.setState(StateView.State.LOADING);
        mBodyAnalyzeHistoryPresenter.getBodyAnalyzeHistory(column);
    }

    private void initChartData(List<BodyHistoryData> historyDataList) {
        dateList.clear();
        totalList.clear();
        if (ListUtils.isEmpty(historyDataList)) {
            mLikingStateView.setState(StateView.State.NO_DATA);
        } else {
            mLikingStateView.setState(StateView.State.SUCCESS);
        }
        for (int i = 0; i < historyDataList.size(); i++) {
            dateList.add(historyDataList.get(i).getBodyTime());
            totalList.add(historyDataList.get(i).getValue());
        }
        setChartView();
    }

    private void setChartView() {
        mLineChart.setDrawGridBackground(false);
        mLineChart.setBackgroundColor(ChartColorUtil.CHART_LIGHT_BLACK);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(false);//设置这两个方法可以横向滑动
        mLineChart.setData(generateLineData(totalList));
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.setDescription("");
        mLineChart.setExtraOffsets(20f, 30f, 20f, 15f);
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
        leftAxis.setAxisMinValue(0f);
        leftAxis.setTextColor(ChartColorUtil.CHART_LIGHT_BLACK);
        leftAxis.setTextSize(1f);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setXOffset(0f);
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
    private LineData generateLineData(List<String> totalList) {
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        ArrayList<Entry> yVals0 = new ArrayList<>();
        for (int i = 0; i < totalList.size(); i++) {
            float val0 = Float.parseFloat(totalList.get(i));
            yVals0.add(new Entry(i, val0));
        }
        LineDataSet ds0 = new LineDataSet(yVals0, "");
        ds0.setLineWidth(4f);//设置折线图的宽度
        ds0.setDrawCircles(true);
        int r;
        int g;
        int b;
        int a[] = new int[11];
        for (int i = 0; i < 10; i++) {
            r = (52 / 10) * i + 52;
            g = (200 - 173) / 10 * i + 173;
            b = (108 - 230) / 10 * i + 230;
            a[i] = Color.rgb(r, g, b);
        }
        a[10] = Color.rgb(52, 200, 108);
        ds0.setColors(a);
        ds0.setCircleColor(ChartColorUtil.CHART_LIGHT_GREEN);//设置折线图圆圈的颜色
        ds0.setCircleRadius(6f);//设置圆圈的半径
        ds0.setDrawCircleHole(true);//设置圆圈空心还是实心,false为实心
        ds0.setCircleHoleRadius(3f);
        ds0.setDrawValues(true);
        ds0.setValueTextSize(11f);
        ds0.setValueTextColor(ChartColorUtil.CHART_WHITE);
        ds0.setHighlightEnabled(true);
        ds0.setDrawFilled(true);
        sets.add(ds0);
        LineData d = new LineData(sets);
        return d;
    }


    @Override
    public void updateBodyAnalyzeHistoryView(BodyAnalyzeHistoryResult.BodyHistory data) {
        mLikingStateView.setState(StateView.State.SUCCESS);
        List<BodyHistoryData> historyDataList = data.getHistoryData();
        initChartData(historyDataList);
    }

    @Override
    public void handleNetworkFailure() {
        mLikingStateView.setState(StateView.State.FAILED);
    }
}
