package com.goodchef.liking.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.BaseFragment;

/**
 * Created on 16/5/20.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LikingRechargeFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("LikingRechargeFragment");
        return textView;
    }
}
