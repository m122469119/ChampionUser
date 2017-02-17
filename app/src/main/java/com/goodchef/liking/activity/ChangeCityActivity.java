package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.StringUtils;
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

public class ChangeCityActivity extends AppBarActivity implements View.OnClickListener{

    @BindView(R.id.search_city_EditText)
    EditText mSearchCityEditText;
    @BindView(R.id.delete_search_ImageView)
    ImageView mDeleteSearchImageView;
    @BindView(R.id.search_cancel_TextView)
    TextView mSearchCancelTextView;
    @BindView(R.id.location_cityName_TextView)
    TextView mLocationCityNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);
        ButterKnife.bind(this);
        setCouponsFragment();
        setSearchCityEditTextOnClickListener();
    }

    private void setCouponsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.change_city_fragment, ChangeCityFragment.newInstance());
        fragmentTransaction.commit();
    }

    private void setSearchCityEditTextOnClickListener(){
        mDeleteSearchImageView.setOnClickListener(this);
        mSearchCityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isEmpty(s.toString()) && s.length()>0) {
                    mDeleteSearchImageView.setVisibility(View.VISIBLE);
                }else {
                    mDeleteSearchImageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mDeleteSearchImageView){
            String searchText = mSearchCityEditText.getText().toString().trim();
            if (!StringUtils.isEmpty(searchText)){
                mSearchCityEditText.setText("");
            }
        }
    }
}
