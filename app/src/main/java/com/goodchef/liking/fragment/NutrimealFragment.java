package com.goodchef.liking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.framework.base.BaseFragment;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/20 下午8:39
 */
public class NutrimealFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrimeal,container,false);
        return view;
    }
}
