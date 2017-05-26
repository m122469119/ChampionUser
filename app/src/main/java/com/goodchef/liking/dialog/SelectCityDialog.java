package com.goodchef.liking.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aaron.android.framework.utils.DialogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.data.City;
import com.goodchef.liking.utils.CityUtils;
import com.goodchef.liking.widgets.wheel.OnWheelChangedListener;
import com.goodchef.liking.widgets.wheel.WheelView;
import com.goodchef.liking.widgets.wheel.adapters.AbstractWheelTextAdapter;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:05
 * version 1.0.0
 */

public class SelectCityDialog {

    private Context mContext;
    private AppCompatDialog mDialog;
    private TextView mCancelButton;
    private TextView mConfirmButton;
    private WheelView mProvinceWheelView;
    private WheelView mCityWheelView;

    private List<City.RegionsData> RegionsList;

    public SelectCityDialog(Context context) {
        this.mContext = context;
        mDialog = new AppCompatDialog(context, R.style.camera_dialog_no_screen);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_city_view);
        window.setWindowAnimations(R.style.my_dialog_enter_exit);  //添加dialog进入和退出的动画

        DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, 0, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        mCancelButton = (TextView) window.findViewById(R.id.dialog_cancel_button);
        mConfirmButton = (TextView) window.findViewById(R.id.dialog_confirm_button);
        mProvinceWheelView = (WheelView) window.findViewById(R.id.select_province_WheelView);
        mCityWheelView = (WheelView) window.findViewById(R.id.select_city_WheelView);

        initData();
    }

    private void initData() {
        RegionsList = CityUtils.getLocalCityList(mContext);
        mProvinceWheelView.setVisibleItems(3);
        mProvinceWheelView.setCurrentItem(RegionsList.size() / 2);
        mProvinceWheelView.setCyclic(false);
        mProvinceWheelView.setWheelLine(R.color.split_line_color);
        mCityWheelView.setCyclic(false);
        mCityWheelView.setVisibleItems(3);
        mCityWheelView.setWheelLine(R.color.split_line_color);

        mProvinceWheelView.setViewAdapter(new RegionAdapter(mContext, RegionsList));

        mCityWheelView.setViewAdapter(new CitiesAdapter(mContext, RegionsList.get(0).getCities()));
        mCityWheelView.setCurrentItem(RegionsList.get(0).getCities().size() / 2);

        mProvinceWheelView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                List<City.RegionsData.CitiesData> citiesList = RegionsList.get(newValue).getCities();
                mCityWheelView.setViewAdapter(new CitiesAdapter(mContext, citiesList));
                mCityWheelView.setCurrentItem(citiesList.size() / 2);
            }
        });
    }

    public void setNegativeClickListener(final cancelClickListener onClickListener) {
        if (mCancelButton != null) {
            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onCancelClickListener(mDialog);
                }
            });
        }
    }

    public void setPositiveClickListener(final confirmClickListener confirmClickListener) {
        if (mConfirmButton != null) {
            mConfirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    City.RegionsData regionsData = RegionsList.get(mProvinceWheelView.getCurrentItem());
                    City.RegionsData.CitiesData citiesData = regionsData.getCities().get(mCityWheelView.getCurrentItem());
                    confirmClickListener.OnConfirmClickListener(mDialog, regionsData, citiesData);
                }
            });
        }
    }

    public interface confirmClickListener {
        void OnConfirmClickListener(AppCompatDialog dialog, City.RegionsData regionsData, City.RegionsData.CitiesData citiesData);
    }

    public interface cancelClickListener {
        void onCancelClickListener(AppCompatDialog dialog);
    }


    /**
     * 设置是否可以关闭
     *
     * @param cancelable true或false
     */
    public void setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
    }

    /**
     * 设置是否可以点击dialog外面关闭dialog
     *
     * @param canceledOnTouchOutside true或false
     */
    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        mDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        mDialog.dismiss();
    }


    public class RegionAdapter extends AbstractWheelTextAdapter {

        private List<City.RegionsData> regionsList;

        public RegionAdapter(Context context, List<City.RegionsData> mList) {
            super(context);
            this.regionsList = mList;
        }

        @Override
        protected CharSequence getItemText(int index) {
            if (index >= 0 && index < regionsList.size()) {
                City.RegionsData bank = regionsList.get(index);
                return bank.getProvinceName();
            }
            return null;
        }

        @Override
        public int getItemsCount() {
            return regionsList.size();
        }
    }

    public class CitiesAdapter extends AbstractWheelTextAdapter {

        private List<City.RegionsData.CitiesData> citiesList;

        public CitiesAdapter(Context context, List<City.RegionsData.CitiesData> mList) {
            super(context);
            this.citiesList = mList;
        }

        @Override
        protected CharSequence getItemText(int index) {
            if (index >= 0 && index < citiesList.size()) {
                City.RegionsData.CitiesData bankCard = citiesList.get(index);
                return bankCard.getCityName();
            }
            return null;
        }

        @Override
        public int getItemsCount() {
            return citiesList.size();
        }
    }

}
