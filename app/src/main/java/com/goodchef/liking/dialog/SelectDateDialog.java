package com.goodchef.liking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import com.aaron.android.framework.utils.DialogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.utils.NavigationBarUtil;

import java.util.Calendar;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/5 上午10:47
 */
public class SelectDateDialog {

    private Context mContext;
    private Dialog mDialog;
    private TextView mCancelBtn;
    private TextView mConfirmBtn;
    private DatePicker mDatePicker;

    private String initDateTime;

    public SelectDateDialog(Context context) {
        this.mContext = context;
        mDialog = new android.app.AlertDialog.Builder(context, R.style.camera_dialog_no_screen).create();
        WindowManager wmManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        boolean hasSoft = NavigationBarUtil.hasSoftKeys(wmManager);
        if (hasSoft) {
            int navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(context);
            //这一行很重要
            DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, navigationBarHeight, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        } else {
            DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, 0, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_select_date);
        window.setWindowAnimations(R.style.my_dialog_enter_exit);  //添加dialog进入和退出的动画

        mCancelBtn = (TextView) window.findViewById(R.id.dialog_date_cancel);
        mConfirmBtn = (TextView) window.findViewById(R.id.dialog_date_confirm);
        mDatePicker = (DatePicker) window.findViewById(R.id.dialog_date_picker);
        initDate();
    }


    private void initDate(){
        

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
