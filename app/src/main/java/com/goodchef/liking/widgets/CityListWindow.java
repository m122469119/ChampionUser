package com.goodchef.liking.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.goodchef.liking.R;
import com.goodchef.liking.widgets.indexBar.decoration.DividerItemDecoration;

/**
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/20.
 */

public class CityListWindow extends PopupWindow {
    RecyclerView mListView;


    View mNoDataView;

    public CityListWindow(Context context) {
        this(context, null);
    }

    public CityListWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }


    private void initView(Context context, AttributeSet attrs) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.window_city_list, null);
        setContentView(inflate);
        setWidth(ViewPager.LayoutParams.MATCH_PARENT);
        setHeight(ViewPager.LayoutParams.MATCH_PARENT);

        mListView = (RecyclerView) inflate.findViewById(R.id.window_list);
        mListView.setLayoutManager(new LinearLayoutManager(context));
        mListView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));

        mNoDataView = inflate.findViewById(R.id.window_no_data);

        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //setAnimationStyle();
    }


    public void setAdapter(BaseRecycleViewAdapter adapter) {
        mListView.setAdapter(adapter);
    }

    public void showListView(){
        mListView.setVisibility(View.VISIBLE);
        mNoDataView.setVisibility(View.GONE);
    }

    public void showNoDataView(){
        mNoDataView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
    }
}
