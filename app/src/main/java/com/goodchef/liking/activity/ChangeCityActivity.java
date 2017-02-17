package com.goodchef.liking.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.goodchef.liking.R;
import com.goodchef.liking.fragment.ChangeCityFragment;
import com.goodchef.liking.mvp.presenter.ChangeCityPresenter;
import com.goodchef.liking.mvp.view.ChangeCityView;
import com.goodchef.liking.widgets.TimerEditView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:切换城市
 * Author : shaozucheng
 * Time: 下午6:07
 * version 1.0.0
 */
public class ChangeCityActivity extends AppBarActivity implements ChangeCityView{

    @BindView(R.id.search_city_EditText)
    TimerEditView mSearchCityEditText;
    @BindView(R.id.delete_search_ImageView)
    ImageView mDeleteSearchImageView;
    @BindView(R.id.search_cancel_TextView)
    TextView mSearchCancelTextView;
    @BindView(R.id.location_cityName_TextView)
    TextView mLocationCityNameTextView;

    ChangeCityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);
        ButterKnife.bind(this);
        setCouponsFragment();
        setTitle("当前城市:");//TODO 设置CITY
        mPresenter = new ChangeCityPresenter(this, this);
        initView();
    }

    /**
     * TODO 请求接口
     */
    private void initView() {
        mSearchCityEditText.setOnTextChangerListener(new TimerEditView.onTextChangerListener() {
            @Override
            public void onTextChanger(String text) {
                if (!StringUtils.isEmpty(text) && text.length() > 0) {
                    mDeleteSearchImageView.setVisibility(View.VISIBLE);
                }else {
                    mDeleteSearchImageView.setVisibility(View.GONE);
                }
                LogUtils.e(TAG, "text= %s", text);
                mPresenter.getCitySearch(text);
            }
        });

    }

    private void setCouponsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.change_city_fragment, ChangeCityFragment.newInstance());
        fragmentTransaction.commit();
    }

    @OnClick({R.id.delete_search_ImageView,
            R.id.search_cancel_TextView})
    public void onViewClick(View v){
        switch (v.getId()) {
            case R.id.delete_search_ImageView:
                String searchText = mSearchCityEditText.getText().toString().trim();
                if (!StringUtils.isEmpty(searchText)){
                    mSearchCityEditText.setText("");
                }
                break;
            case R.id.search_cancel_TextView:
                mSearchCityEditText.setText("");
                mSearchCityEditText.clearFocus();
                break;
        }
    }


}
