package com.goodchef.liking.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.aaron.android.framework.utils.DisplayUtils;
import com.goodchef.liking.R;

/**
 * Created on 2017/6/29
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class OutTextView extends View {
    private Paint mPaint;
    private Rect mBounds;
    private String mText;
    private float mTextSize;
    private int mTextColor;


    public OutTextView(Context context) {
        this(context, null);
    }

    public OutTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OutTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OutTextView);

        mText = typedArray.getString(R.styleable.OutTextView_out_text);
        mTextSize = typedArray.getDimensionPixelOffset(R.styleable.OutTextView_out_textSize, DisplayUtils.dp2px(14));
        mTextColor = typedArray.getColor(R.styleable.OutTextView_out_textColor, ContextCompat.getColor(context, R.color.black));

        typedArray.recycle();


        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setStrokeWidth(DisplayUtils.dp2px(1));
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mBounds = new Rect();
    }

    public void setText(String mText) {
        this.mText = mText;
        requestLayout();
        invalidate();
    }

    public void setTextColor(@ColorInt int color) {
        mTextColor = color;
        mPaint.setColor(mTextColor);
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPaint.getTextBounds(mText, 0, mText.length(), mBounds);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            widthMeasureSpec = (int) (mBounds.width() * 1.3f);
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            heightMeasureSpec = (int) mTextSize;
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.getTextBounds(mText, 0, mText.length(), mBounds);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mText, getMeasuredWidth() / 2 - mBounds.width() / 2, baseline, mPaint);
        canvas.drawLine((float) ((getMeasuredWidth() - mBounds.width()) / 2), (float) (getMeasuredHeight() / 1.9),
                (float) ((getMeasuredWidth() - mBounds.width()) / 2 +  mBounds.width() + mBounds.width() * 0.1), (float) (getMeasuredHeight() / 1.9), mPaint);
    }
}
