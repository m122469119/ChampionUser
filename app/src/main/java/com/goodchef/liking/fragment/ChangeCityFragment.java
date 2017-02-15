package com.goodchef.liking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.framework.base.ui.BaseFragment;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午6:12
 * version 1.0.0
 */

public class ChangeCityFragment extends BaseFragment {

    public static ChangeCityFragment newInstance() {
        Bundle args = new Bundle();
        ChangeCityFragment fragment = new ChangeCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_city, container, false);
        return view;
    }
}
