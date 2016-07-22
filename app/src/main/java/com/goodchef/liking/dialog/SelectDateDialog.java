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
import com.goodchef.liking.widgets.wheelpicker.core.AbstractWheelPicker;
import com.goodchef.liking.widgets.wheelpicker.widget.curved.WheelDayPicker;
import com.goodchef.liking.widgets.wheelpicker.widget.curved.WheelMonthPicker;
import com.goodchef.liking.widgets.wheelpicker.widget.curved.WheelYearPicker;

import java.util.Calendar;

/**
 * 说明:选择时间对话框
 * Author shaozucheng
 * Time:16/7/5 上午10:47
 */
public class SelectDateDialog {

    private Context mContext;
    private AppCompatDialog mDialog;
    private TextView mCancelBtn;
    private TextView mConfirmBtn;

    WheelYearPicker mWheelYearPicker;
    WheelMonthPicker mWheelMonthPicker;
    WheelDayPicker mWheelDayPicker;

    private String yearStr;
    private String monthStr;
    private String dayStr;


    public SelectDateDialog(Context context) {
        this.mContext = context;
//        mDialog = new android.app.AlertDialog.Builder(context, R.style.camera_dialog_no_screen).create();
        mDialog = new AppCompatDialog(context, R.style.camera_dialog_no_screen);
//        WindowManager wmManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        boolean hasSoft = NavigationBarUtil.hasSoftKeys(wmManager);
//        if (hasSoft) {
//            int navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(context);
//            //这一行很重要
//            DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, 0, WindowManager.LayoutParams.MATCH_PARENT,
//                    WindowManager.LayoutParams.WRAP_CONTENT);
//        } else {
//
//        }

        DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, 0, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_select_date);
        window.setWindowAnimations(R.style.my_dialog_enter_exit);  //添加dialog进入和退出的动画

        mCancelBtn = (TextView) window.findViewById(R.id.dialog_date_cancel);
        mConfirmBtn = (TextView) window.findViewById(R.id.dialog_date_confirm);
        mWheelYearPicker = (WheelYearPicker) window.findViewById(R.id.wheel_year_picker);
        mWheelMonthPicker = (WheelMonthPicker) window.findViewById(R.id.wheel_month_picker);
        mWheelDayPicker = (WheelDayPicker) window.findViewById(R.id.wheel_day_picker);
        initDate();
    }


    private void initDate() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        mWheelYearPicker.setYearRange(1950, year);
        mWheelYearPicker.setCurrentYear(1990);
        mWheelMonthPicker.setCurrentMonth(6);
        mWheelDayPicker.setCurrentDay(15);
        getYear();
        getMonth();
        getDay();
    }

    public String getYear() {
        mWheelYearPicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(int index, String data) {
                yearStr = data;
            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });
        return yearStr;
    }

    public String getMonth() {
        mWheelMonthPicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(int index, String data) {
                monthStr = data;
            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });
        return monthStr;
    }

    public String getDay() {
        mWheelDayPicker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {

            }

            @Override
            public void onWheelSelected(int index, String data) {
                dayStr = data;
            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });
        return dayStr;
    }


    public void setTextViewOnClickListener(View.OnClickListener listener) {
        mCancelBtn.setOnClickListener(listener);
        mConfirmBtn.setOnClickListener(listener);
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
}
