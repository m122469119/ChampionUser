package com.goodchef.liking.module.gym.changecity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.eventbus.BaseMessage;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.EnvironmentUtils;
import com.aaron.android.framework.utils.InputMethodManagerUtils;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.ChangeCityAdapter;
import com.goodchef.liking.eventmessages.ChangeCityActivityMessage;
import com.goodchef.liking.eventmessages.ChangeGymActivityMessage;
import com.goodchef.liking.data.remote.retrofit.result.data.City;
import com.goodchef.liking.widgets.CityListWindow;
import com.goodchef.liking.widgets.TimerEditView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 说明:切换城市
 * Author : shaozucheng
 * Time: 下午6:07
 * version 1.0.0
 */
public class ChangeCityActivity extends AppBarActivity implements ChangeCityContract.ChangeCityView {
    private static final String TAG = "ChangeCityActivity";

    public static final String CITY_NAME = "city_name";

    @BindView(R.id.search_city_EditText)
    TimerEditView mSearchCityEditText;
    @BindView(R.id.delete_search_ImageView)
    ImageView mDeleteSearchImageView;
    @BindView(R.id.search_cancel_TextView)
    TextView mSearchCancelTextView;
    @BindView(R.id.location_cityName_TextView)
    TextView mLocationCityNameTextView;

    @BindView(R.id.window_city_layout)
    View mWindowView;

    @BindView(R.id.search_city_layout)
    View mCityLayout;

    ChangeCityContract.ChangeCityPresenter mPresenter;

    String defaultCityName;

    private CityListWindow mCityListWindow;
    private ChangeCityAdapter mChangeCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);
        showHomeUpIcon(R.drawable.app_bar_left_quit);
        Intent intent = getIntent();
        defaultCityName = intent.getStringExtra(CITY_NAME);
        ButterKnife.bind(this);
        setCouponsFragment();
        mPresenter = new ChangeCityContract.ChangeCityPresenter(this, this);
        initView();
        loadData();
    }

    private void loadData() {
        LogUtils.i(TAG,"=-------loadData--------");
        mPresenter.startLocation();
        mPresenter.getCityList();
    }

    private void initView() {
        mSearchCityEditText.setOnTextChangerListener(new TimerEditView.onTextChangerListener() {
            @Override
            public void onTextChanger(String text) {
                if (!StringUtils.isEmpty(text) && text.length() > 0) {
                    mDeleteSearchImageView.setVisibility(View.VISIBLE);
                } else {
                    mDeleteSearchImageView.setVisibility(View.GONE);
                    InputMethodManagerUtils.hideKeyboard(mSearchCityEditText);
                    mSearchCityEditText.clearFocus();
                }
                LogUtils.e(TAG, "text= %s", text);
                mPresenter.getCitySearch(text);
            }
        });

        mSearchCityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mSearchCancelTextView.setVisibility(View.VISIBLE);
                    showCityListWindow(null);
                } else {
                    mSearchCancelTextView.setVisibility(View.GONE);
                    InputMethodManagerUtils.hideKeyboard(mSearchCityEditText);
                }
            }
        });
        mSearchCityEditText.clearFocus();
    }

    private void setCouponsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.change_city_fragment, ChangeCityFragment.newInstance());
        fragmentTransaction.commit();
    }

    @OnClick({R.id.delete_search_ImageView,
            R.id.search_cancel_TextView,
            R.id.location_cityName_TextView})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.delete_search_ImageView:
                String searchText = mSearchCityEditText.getText().toString().trim();
                if (!StringUtils.isEmpty(searchText)) {
                    mSearchCityEditText.setText("");
                }
                break;
            case R.id.search_cancel_TextView:
                mSearchCityEditText.setText("");
                mSearchCityEditText.clearFocus();
                hideInput();
                break;
            case R.id.location_cityName_TextView:
                mPresenter.onLocationTextClick();
                break;
        }
    }


    @Override
    public boolean showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                boolean shouldShow = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
                if (!shouldShow) {
                    showSetLocationPermission();
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
        return true;
    }

    /**
     * 展示没有
     */
    private void showSetLocationPermission() {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(getString(R.string.no_location_permission));
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.go_to_setting), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                jumpSettingLocationActivity();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * 跳转到系统设置界面中的Liking APP中设置定位权限
     */
    private void jumpSettingLocationActivity() {
        String packageName = EnvironmentUtils.Config.isTestMode() ? "com.goodchef.liking.test" : "com.goodchef.liking";
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        startActivity(intent);
       // finish();
    }


    public void onEvent(ChangeCityActivityMessage message) {
        switch (message.what) {
            case ChangeCityActivityMessage.CITY_ITEM_CLICK:
                ChangeGymActivityMessage msg = ChangeGymActivityMessage
                        .obtain(ChangeGymActivityMessage.CHANGE_LEFT_CITY_TEXT);
                msg.msg1 = message.msg1;
                postEvent(msg);
                finish();
                break;
        }
    }


    @Override
    public void showCityListWindow(final List<City.RegionsData.CitiesData> list) {
        if (mCityListWindow != null && mCityListWindow.isShowing()) {
            mChangeCityAdapter.setData(list);
            mChangeCityAdapter.notifyDataSetChanged();
            mCityListWindow.update();
            return;
        } else if (mCityListWindow == null) {
            mCityListWindow = new CityListWindow(this);
            mCityListWindow.setAdapter(mChangeCityAdapter = new ChangeCityAdapter(this));
            mChangeCityAdapter.setOnItemClickListener(new ChangeCityAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, City.RegionsData.CitiesData pos) {

                    ChangeGymActivityMessage msg = ChangeGymActivityMessage
                            .obtain(ChangeGymActivityMessage.CHANGE_LEFT_CITY_TEXT);
                    msg.msg1 = pos.getCityName();
                    postEvent(msg);
                    finish();
                }
            });
            mChangeCityAdapter.setOnDataIsNullListener(new ChangeCityAdapter.OnDataIsNullListener() {
                @Override
                public void onDataIsNull(boolean isNull) {
                    if (isNull) {
                        mCityListWindow.showNoDataView();
                    } else {
                        mCityListWindow.showListView();
                    }
                }
            });
        }
        mChangeCityAdapter.setData(list);
        mChangeCityAdapter.notifyDataSetChanged();
        mCityListWindow.showAsDropDown(mCityLayout);
        mCityListWindow.update();
    }

    @Override
    public void dismissWindow() {
        if (mCityListWindow != null && mCityListWindow.isShowing())
            mCityListWindow.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.stopLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissWindow();
        mPresenter.destroyLocation();
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    @Override
    public void setLocationCityNameTextViewText(String text) {
        mLocationCityNameTextView.setText(text);
    }

    @Override
    public CharSequence getLocationCityNameTextViewText() {
        return mLocationCityNameTextView.getText();
    }

    @Override
    public void setTitle(String text) {
        super.setTitle(text);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void postEvent(BaseMessage object) {
        super.postEvent(object);
    }


    @Override
    public String getDefaultCityName() {
        return defaultCityName;
    }

    @Override
    public void onBackPressed() {
        hideInput();
        if (mSearchCityEditText.isFocused()) {
            mSearchCityEditText.clearFocus();
            return;
        }
        super.onBackPressed();
    }

    public void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
