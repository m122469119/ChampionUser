package com.goodchef.liking.widgets.layoutmanager;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created on 2017/7/5
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class FixLinearLayoutManager extends LinearLayoutManager {
    public FixLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        int itemCount = state.getItemCount();
        for (int i = 0; i < itemCount; i ++) {
            heightSpec += RecyclerView.LayoutManager.chooseSize(heightSpec,
                    getPaddingTop() + getPaddingBottom(),
                    ViewCompat.getMinimumHeight(recycler.getViewForPosition(i)));
        }
        setMeasuredDimension(widthSpec, heightSpec);
    }
}
