package com.goodchef.liking.http.result.data;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.util.List;

/**
 * 说明:
 * Author: shaozucheng
 * Time: 下午4:53
 */

public class BodyChartValueFormatter implements AxisValueFormatter {

    private List<String> mAxisList;

    private BarLineChartBase<?> chart;


    public BodyChartValueFormatter(BarLineChartBase<?> chart, List<String> AxisList) {
        this.chart = chart;
        this.mAxisList = AxisList;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int days = (int) value;
        String monthName;
        if (mAxisList.size() >= 0 && value >= 0) {
            if (value < mAxisList.size()) {
                monthName = mAxisList.get(days);
            } else {
                monthName = "";
            }
        } else {
            monthName = "";
        }
        return monthName;
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
