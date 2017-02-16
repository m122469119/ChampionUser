package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.ChangeCityFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:切换城市
 * Author : shaozucheng
 * Time: 下午6:07
 * version 1.0.0
 */

public class ChangeCityActivity extends AppBarActivity {

    @BindView(R.id.search_city_EditText)
    EditText mSearchCityEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);
        ButterKnife.bind(this);
        setCouponsFragment();
    }

    private void setCouponsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.change_city_fragment, ChangeCityFragment.newInstance());
        fragmentTransaction.commit();
    }
}
