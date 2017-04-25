package com.goodchef.liking.widgets.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aaron.android.framework.utils.DisplayUtils;

/**
 * Created on 2017/3/17
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsDecoration extends RecyclerView.ItemDecoration {
    Context mContext;
    private int mOrientation;
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;


    private Drawable mHeaderDivider, mContentDivider, mFlootDivider;

    public CouponsDecoration(Context mContext, int mOrientation,
                             @DrawableRes int mHeaderDivider,
                             @DrawableRes int mContentDivider,
                             @DrawableRes int mFlootDivider) {
        this.mContext = mContext;
        this.mOrientation = mOrientation;
        this.mHeaderDivider = ContextCompat.getDrawable(mContext, mHeaderDivider);
        this.mContentDivider = ContextCompat.getDrawable(mContext, mContentDivider);
        this.mFlootDivider = ContextCompat.getDrawable(mContext, mFlootDivider);
        setOrientation(mOrientation);
    }

    //设置屏幕的方向
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == HORIZONTAL_LIST) {
            drawVerticalLine(c, parent, state);
        } else {
            drawHorizontalLine(c, parent, state);
        }
    }

    //画横线, 这里的parent其实是显示在屏幕显示的这部分
    public void drawHorizontalLine(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        int itemCount = state.getItemCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            int pos = parent.getChildAdapterPosition(child);
            if (pos == 0) {
                final int bottom = top + mHeaderDivider.getIntrinsicHeight();
                mHeaderDivider.setBounds(left, top, right, bottom);
                mHeaderDivider.draw(c);
            } else if (pos == itemCount - 1) {
                final int bottom = top + mFlootDivider.getIntrinsicHeight();
                mFlootDivider.setBounds(left, top, right, bottom);
                mFlootDivider.draw(c);
            } else {
                final int bottom = top + mContentDivider.getIntrinsicHeight();
                mContentDivider.setBounds(left + DisplayUtils.dp2px(15), top, right, bottom);
                mContentDivider.draw(c);
            }
        }
    }

    //画竖线
    public void drawVerticalLine(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        int itemCount = state.getItemCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(child);

            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;

            if (pos == 0) {
                final int right = left + mHeaderDivider.getIntrinsicWidth();
                mHeaderDivider.setBounds(left, top, right, bottom);
                mHeaderDivider.draw(c);
            } else if (i == itemCount - 1) {
                final int right = left + mContentDivider.getIntrinsicWidth();
                mContentDivider.setBounds(left, top, right, bottom);
                mContentDivider.draw(c);
            } else {
                final int right = left + mFlootDivider.getIntrinsicWidth();
                mFlootDivider.setBounds(left + DisplayUtils.dp2px(15), top, right, bottom);
                mFlootDivider.draw(c);
            }
        }
    }

    //由于Divider也有长宽高，每一个Item需要向下或者向右偏移
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == HORIZONTAL_LIST) {
            //画横线，就是往下偏移一个分割线的高度
            final int childCount = parent.getChildCount();
            int itemCount = state.getItemCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                int pos = parent.getChildAdapterPosition(child);
                if (pos == 0) {
                    outRect.set(0, 0, 0, mHeaderDivider.getIntrinsicHeight());
                } else if (i == itemCount - 1) {
                    outRect.set(0, 0, 0, mFlootDivider.getIntrinsicHeight());
                } else {
                    outRect.set(0, 0, 0, mContentDivider.getIntrinsicHeight());
                }
            }


        } else {
            //画竖线，就是往右偏移一个分割线的宽度
            final int childCount = parent.getChildCount();
            int itemCount = state.getItemCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                int pos = parent.getChildAdapterPosition(child);
                if (pos == 0) {
                    outRect.set(0, 0, mHeaderDivider.getIntrinsicWidth(), 0);
                } else if (i == itemCount - 1) {
                    outRect.set(0, 0, mFlootDivider.getIntrinsicWidth(), 0);
                } else {
                    outRect.set(0, 0, mContentDivider.getIntrinsicWidth(), 0);
                }
            }

        }
    }

}
