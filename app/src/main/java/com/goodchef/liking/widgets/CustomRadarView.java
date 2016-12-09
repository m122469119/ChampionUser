package com.goodchef.liking.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.aaron.android.framework.utils.DisplayUtils;
import com.goodchef.liking.utils.ChartColorUtil;
import com.goodchef.liking.utils.TypefaseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 芝麻信用分
 * Created by yangle on 2016/9/26.
 */
public class CustomRadarView extends View {
    //数据个数
    private int dataCount = 5;
    //每个角的弧度
    private float radian = (float) (Math.PI * 2 / dataCount);
    //雷达图半径
    private float radius;
    //中心X坐标
    private int centerX;
    //中心Y坐标
    private int centerY;
    //各维度标题
    private List<String> titles;

    private List<Float> radarValueList;
    private List<Float> minValueList;
    private List<Float> maxValueList;
    private List<String> valueUnitList;//单位和具体值
    private List<String> standardList = new ArrayList<>();
    //数据最大值
    private float maxValue = 100f;
    //雷达图与标题的间距
    private int radarMargin = DisplayUtils.dp2px(10);
    //雷达区画笔
    private Paint mainPaint;
    //最小值区画笔
    private Paint minValuePaint;
    //数据值画笔
    private Paint dataValuePaint;
    //最大值画笔
    private Paint maxValuePaint;
    //标题画笔
    private Paint titlePaint;
    private Paint unitPaint;
    private Paint standardPaint;
    private Typeface mTypeface;

    //标题文字大小
    private int titleSize = DisplayUtils.dp2px(13);
    private int standtitleSize = DisplayUtils.dp2px(9);

    public CustomRadarView(Context context) {
        super(context);
        init();
        mTypeface = TypefaseUtil.getImpactTypeface(context);
    }

    public CustomRadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        mTypeface = TypefaseUtil.getImpactTypeface(context);
    }

    public CustomRadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        mTypeface = TypefaseUtil.getImpactTypeface(context);
    }

    private void init() {

        mainPaint = new Paint();
        mainPaint.setAntiAlias(true);
        mainPaint.setStrokeWidth(3f);
        mainPaint.setColor(ChartColorUtil.CHART_LIGHT_GRAY);
        mainPaint.setStyle(Paint.Style.STROKE);

        minValuePaint = new Paint();
        minValuePaint.setAntiAlias(true);
        minValuePaint.setColor(Color.WHITE);
        minValuePaint.setAlpha(140);
        minValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        dataValuePaint = new Paint();
        dataValuePaint.setAntiAlias(true);
        dataValuePaint.setColor(ChartColorUtil.CHART_LIGHT_GREEN);
        dataValuePaint.setAlpha(60);

        maxValuePaint = new Paint();
        maxValuePaint.setAntiAlias(true);
        maxValuePaint.setColor(ChartColorUtil.CHART_LIGHT_GRAY);
        maxValuePaint.setAlpha(120);

        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(titleSize);
        titlePaint.setColor(ChartColorUtil.CHART_LIGHT_BLACK);
        titlePaint.setStyle(Paint.Style.FILL);

        unitPaint = new Paint();
        unitPaint.setAntiAlias(true);
        unitPaint.setTextSize(titleSize);
        unitPaint.setTypeface(mTypeface);
        unitPaint.setColor(ChartColorUtil.CHART_LIGHT_GREEN);
        unitPaint.setStyle(Paint.Style.FILL);

        standardPaint = new Paint();
        standardPaint.setAntiAlias(true);
        standardPaint.setTextSize(standtitleSize);
        standardPaint.setColor(ChartColorUtil.CHART_STAND_TEXT);
        standardPaint.setStyle(Paint.Style.STROKE);
    }

    public void setDataCount(int dataCount) {
        this.dataCount = dataCount;
        radian = (float) (Math.PI * 2 / dataCount);
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
        postInvalidate();
    }

    public void setMinValueList(List<Float> minValueList) {
        this.minValueList = minValueList;
        postInvalidate();
    }

    public void setMaxValueList(List<Float> maxValueList) {
        this.maxValueList = maxValueList;
        postInvalidate();//刷新view
    }

    public void setValueUnitList(List<String> valueUnitList) {
        this.valueUnitList = valueUnitList;
        postInvalidate();//刷新view
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //雷达图半径
        radius = Math.min(h, w) / 2 * 0.5f;
        //中心坐标
        centerX = w / 2;
        centerY = h / 2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPolygon(canvas);
        drawLines(canvas);
        drawRegionMaxView(canvas);//绘制最大区域
        drawMinRegionView(canvas);//绘制最小区域
        drawRegionView(canvas);//绘制数据
        drawTitle(canvas);//绘制标题
        drawUnit(canvas);//绘制值和单位
        drawStandardText(canvas);
    }

    /**
     * 绘制多边形
     *
     * @param canvas 画布
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < dataCount; i++) {
            if (i == 0) {
                path.moveTo(getPoint(i).x, getPoint(i).y);
            } else {
                path.lineTo(getPoint(i).x, getPoint(i).y);
            }
        }
        //闭合路径
        path.close();

        canvas.drawPath(path, mainPaint);
    }

    /**
     * 绘制连接线
     *
     * @param canvas 画布
     */
    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < dataCount; i++) {
            path.reset();
            path.moveTo(centerX, centerY);
            path.lineTo(getPoint(i).x, getPoint(i).y);
            canvas.drawPath(path, mainPaint);
        }
    }

    /**
     * 绘制覆盖区域
     *
     * @param canvas 画布
     */
    private void drawRegionMaxView(Canvas canvas) {
        if (maxValueList == null) {
            return;
        }
        Path path = new Path();
        for (int i = 0; i < dataCount; i++) {
            //计算百分比
            float percent = maxValueList.get(i) / maxValue;
            int x = getPoint(i, 0, percent).x;
            int y = getPoint(i, 0, percent).y;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        //绘制填充区域的边界
        path.close();
        Paint paint = new Paint();
        paint.setStrokeWidth(2f);
        paint.setColor(ChartColorUtil.CHART_LIGHT_GRAY);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
        //绘制填充区域
        maxValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, maxValuePaint);
    }

    private void drawMinRegionView(Canvas canvas) {
        if (minValueList == null) {
            return;
        }
        Path path = new Path();
        for (int i = 0; i < dataCount; i++) {
            //计算百分比
            float percent = minValueList.get(i) / maxValue;
            int x = getPoint(i, 0, percent).x;
            int y = getPoint(i, 0, percent).y;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        //绘制填充区域的边界
        path.close();
        Paint paint = new Paint();
        paint.setStrokeWidth(3f);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
        //绘制填充区域
        minValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, minValuePaint);
    }


    private void drawRegionView(Canvas canvas) {
        if (radarValueList == null) {
            return;
        }
        Path path = new Path();
        for (int i = 0; i < dataCount; i++) {
            //计算百分比
            float percent = radarValueList.get(i) / maxValue;
            int x = getPoint(i, 0, percent).x;
            int y = getPoint(i, 0, percent).y;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }

        //绘制填充区域的边界
        path.close();
        Paint paint = new Paint();
        paint.setColor(ChartColorUtil.CHART_LIGHT_GREEN);
        paint.setStrokeWidth(4f);
        paint.setAntiAlias(true);
        paint.setAlpha(80);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);

        //绘制填充区域
        dataValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, dataValuePaint);
    }

    public void setRadarValueList(List<Float> radarValueList) {
        this.radarValueList = radarValueList;
        dataCount = radarValueList.size();
        radian = (float) (Math.PI * 2 / dataCount);
        standardList.add("偏低");
        standardList.add("标准");
        standardList.add("偏高");
        postInvalidate();
    }

    /**
     * 绘制标题
     *
     * @param canvas 画布
     */
    private void drawTitle(Canvas canvas) {
        if (titles == null) {
            return;
        }
        if (dataCount == 5) {
            for (int i = 0; i < dataCount; i++) {
                int x = getPoint(i, radarMargin, 1).x;
                int y = getPoint(i, radarMargin, 1).y;

                float titleWidth = titlePaint.measureText(titles.get(i));

                //底下两个角的坐标需要向下移动半个图片的位置（1、2）
                if (i == 1) {
                    y += (titleWidth / 2) - 40;
                } else if (i == 2) {
                    x -= titleWidth;
                    y += (titleWidth / 2) - 40;
                } else if (i == 3) {
                    x -= titleWidth;
                } else if (i == 4) {
                    x -= titleWidth / 2;
                    y -= 20;
                }
                canvas.drawText(titles.get(i), x, y, titlePaint);
            }
        } else if (dataCount == 4) {
            for (int i = 0; i < dataCount; i++) {
                int x = getPoint(i, radarMargin, 1).x;
                int y = getPoint(i, radarMargin, 1).y;

                float titleWidth = titlePaint.measureText(titles.get(i));

                //底下两个角的坐标需要向下移动半个图片的位置（1、2）
                if (i == 0) {
                    x = (int) (centerX - (titleWidth / 2));
                    y = (int) (centerY - radius) - 60;
                } else if (i == 1) {
                    x = (int) (centerX + radius + (titleWidth / 2) - 40);
                    y = centerY + 10;
                } else if (i == 2) {
                    x = (int) (centerX - (titleWidth / 2));
                    y = (int) (centerY + radius + (titleWidth / 2)) + 10;
                } else if (i == 3) {
                    x = (int) (centerX - radius - (titleWidth)) - 40;
                    y = centerY + 10;
                }
                canvas.drawText(titles.get(i), x, y, titlePaint);
            }
        }
    }

    private void drawUnit(Canvas canvas) {
        if (valueUnitList == null) {
            return;
        }
        if (dataCount == 5) {
            for (int i = 0; i < dataCount; i++) {
                int x = getPoint(i, radarMargin, 1).x;
                int y = getPoint(i, radarMargin, 1).y;

                float titleWidth = titlePaint.measureText(titles.get(i));
                float unitWidth = unitPaint.measureText(valueUnitList.get(i));
                if (i == 0) {
                    x = (int) (x + titleWidth / 2 - unitWidth / 2);
                    y = y + getTextHeight(titlePaint) + 20;
                } else if (i == 1) {
                    x = (int) (x + titleWidth / 2 - unitWidth / 2);
                    y = y + getTextHeight(titlePaint) + 40;
                } else if (i == 2) {
                    x = (int) (x - titleWidth / 2 - unitWidth / 2);
                    y = y + getTextHeight(titlePaint) + 20;
                } else if (i == 3) {
                    x = (int) (x - titleWidth / 2 - unitWidth / 2);
                    y = y + getTextHeight(titlePaint) + 20;
                } else if (i == 4) {
                    x = (int) (x - unitWidth / 2);
                    y = (int) (y + unitWidth / 2) - 40;
                }
                canvas.drawText(valueUnitList.get(i), x, y, unitPaint);
            }
        } else if (dataCount == 4) {
            for (int i = 0; i < dataCount; i++) {
                int x = getPoint(i, radarMargin, 1).x;
                int y = getPoint(i, radarMargin, 1).y;

                float titleWidth = titlePaint.measureText(titles.get(i));
                float unitWidth = unitPaint.measureText(valueUnitList.get(i));
                //底下两个角的坐标需要向下移动半个图片的位置（1、2）
                if (i == 0) {
                    x = (int) (centerX - unitWidth / 2);
                    y = (int) (centerY - radius) - 20;
                } else if (i == 1) {
                    x = (int) (centerX + radius + (titleWidth / 2));
                    y = centerY + 60;
                } else if (i == 2) {
                    x = (int) (centerX - (titleWidth / 2) + 10);
                    y = (int) (centerY + radius + (titleWidth / 2) + unitWidth / 2) + 20;
                } else if (i == 3) {
                    x = (int) (centerX - radius - (titleWidth) - unitWidth / 2) + 10;
                    y = centerY + 60;
                }
                canvas.drawText(valueUnitList.get(i), x, y, unitPaint);
            }
        }
    }


    private void drawStandardText(Canvas canvas) {
        if (standardList == null) {
            return;
        }
        for (int i = 0; i < standardList.size(); i++) {
            int x = getPoint(i, radarMargin, 1).x;
            int y = getPoint(i, radarMargin, 1).y;
            float titleWidth = standardPaint.measureText(standardList.get(i));
            if (i == 0) {
                x = (int) (centerX - titleWidth / 2);
                y = centerY - 12;
            } else if (i == 1) {
                x = (int) (centerX - titleWidth / 2);
                y = (int) (centerY - radius / 3 - titleWidth / 2) + 15;
            } else if (i == 2) {
                x = (int) (centerX - titleWidth / 2);
                y = (int) (centerY - (radius / 3) * 2 - titleWidth / 2) +15;
            }
            canvas.drawText(standardList.get(i), x, y, standardPaint);
        }
    }


    /**
     * 获取雷达图上各个点的坐标
     *
     * @param position 坐标位置（右上角为0，顺时针递增）
     * @return 坐标
     */
    private Point getPoint(int position) {
        return getPoint(position, 0, 1);
    }

    /**
     * 获取雷达图上各个点的坐标（包括维度标题与图标的坐标）
     *
     * @param position    坐标位置
     * @param radarMargin 雷达图与维度标题的间距
     * @param percent     覆盖区的的百分比
     * @return 坐标
     */
    private Point getPoint(int position, int radarMargin, float percent) {
        int x = 0;
        int y = 0;
        if (dataCount == 5) {
            if (position == 0) {
                x = (int) (centerX + (radius + radarMargin) * Math.sin(radian) * percent);
                y = (int) (centerY - (radius + radarMargin) * Math.cos(radian) * percent);

            } else if (position == 1) {
                x = (int) (centerX + (radius + radarMargin) * Math.sin(radian / 2) * percent);
                y = (int) (centerY + (radius + radarMargin) * Math.cos(radian / 2) * percent);

            } else if (position == 2) {
                x = (int) (centerX - (radius + radarMargin) * Math.sin(radian / 2) * percent);
                y = (int) (centerY + (radius + radarMargin) * Math.cos(radian / 2) * percent);

            } else if (position == 3) {
                x = (int) (centerX - (radius + radarMargin) * Math.sin(radian) * percent);
                y = (int) (centerY - (radius + radarMargin) * Math.cos(radian) * percent);

            } else if (position == 4) {
                x = centerX;
                y = (int) (centerY - (radius + radarMargin) * percent);
            }
        } else if (dataCount == 4) {
            if (position == 0) {
                x = (int) (centerX);
                y = (int) (centerY - (radius * percent));
//                x = (int) (centerX + (radius + radarMargin) * Math.sin(radian) * percent);
//                y = (int) (centerY - (radius + radarMargin) * Math.cos(radian) * percent);

            } else if (position == 1) {
                x = (int) (centerX + (radius * percent));
                y = (int) (centerY);
//                x = (int) (centerX + (radius + radarMargin) * Math.sin(radian /2) * percent);
//                y = (int) (centerY + (radius + radarMargin) * Math.cos(radian/2 ) * percent);

            } else if (position == 2) {
//                x = (int) (centerX - (radius + radarMargin) * Math.sin(radian/2 ) * percent);
//                y = (int) (centerY + (radius + radarMargin) * Math.cos(radian /2) * percent);
                x = (int) (centerX);
                y = (int) (centerY + (radius * percent));

            } else if (position == 3) {
//                x = (int) (centerX - (radius + radarMargin) * Math.sin(radian) * percent);
//                y = (int) (centerY - (radius + radarMargin) * Math.cos(radian) * percent);
                x = (int) (centerX - (radius * percent));
                y = (int) (centerY);

            }
        }

        return new Point(x, y);
    }

    /**
     * 获取文本的高度
     *
     * @param paint 文本绘制的画笔
     * @return 文本高度
     */
    private int getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (int) (fontMetrics.descent - fontMetrics.ascent);
    }
}
