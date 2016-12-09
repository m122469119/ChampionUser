package com.goodchef.liking.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午6:02
 * version 1.0.0
 */

public class ChartColorUtil {
    public static int CHART_WHITE = Color.rgb(255, 255, 255);//白色
    public static int CHART_LIGHT_GRAY = Color.rgb(240, 240, 241);//浅灰色
    public static int CHART_LIGHT_GREEN = Color.rgb(52, 200, 108);//绿色
    public static int CHART_LIGHT_BLACK = Color.rgb(56,60,75);//深灰色
    public static int CHART_NORMAL_GRAY = Color.rgb(75,82,93);//
    public static int CHART_STAND_TEXT = Color.rgb(187,186,188);//


    /**
     * 根据角标获取颜色
     *
     * @param i
     * @return
     */
    public static int getChartColor(int i) {
        Random rand = new Random();
        int r = rand.nextInt(255); //int范围类的随机数
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        switch (i) {
            case 0:
                return CHART_WHITE;
            case 1:
                return CHART_LIGHT_GRAY;
            case 2:
                return CHART_LIGHT_GREEN;
            default:
                return Color.rgb(r, g, b);
        }
    }
}
