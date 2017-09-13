package com.goodchef.liking.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import com.aaron.android.framework.utils.DisplayUtils;
import com.goodchef.liking.R;

/**
 * @Author SanFen
 * @Email sanfenruxi1@163.com
 * @Date 2017/9/2
 * @Version 1.0
 */
public class HistogramView extends View {

    Paint mRectPaint, mTextPaint;

    Rect mRect;

    private float mPercentage;
    private String mPercentageText;


    public HistogramView(Context context) {
        this(context, null);
    }

    public HistogramView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRectPaint = new Paint();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(DisplayUtils.dp2px(10));
        mTextPaint.setAntiAlias(true);

        mTextPaint.setColor(ContextCompat.getColor(context, R.color.his_bg));
        mRectPaint.setColor(ContextCompat.getColor(context, R.color.his_bg));
        mRect = new Rect();

    }

    public void setColor(@ColorInt int color) {
        mRectPaint.setColor(color);
        mTextPaint.setColor(color);
    }

    public void setPercentage(float percentage) {
        this.mPercentage = percentage;
        postInvalidate();
    }

    public void setPercentageText(String percentageText) {
        this.mPercentageText = percentageText;
        if (percentageText.equals("NO\nTRAINING")){
            mTextPaint.setTextSize(DisplayUtils.dp2px(4));
        }else {
            mTextPaint.setTextSize(DisplayUtils.dp2px(10));
        }
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(getMeasuredWidth() * 0.1F, getHisHeight() ,
                getMeasuredWidth() * 0.9F, getMeasuredHeight(), mRectPaint);

        if (mPercentageText == null || mPercentageText.length() == 0) {
           mPercentageText = "0mins";
        }
        mTextPaint.getTextBounds(mPercentageText, 0, mPercentageText.length(), mRect);

        canvas.drawText(mPercentageText, (getMeasuredWidth() - mRect.width()) / 2,
                getHisHeight() - mRect.height(), mTextPaint);

    }

    private float getHisHeight() {
        float height = getMeasuredHeight() * 0.8F;
        return getMeasuredHeight() - height * mPercentage;
    }



}
