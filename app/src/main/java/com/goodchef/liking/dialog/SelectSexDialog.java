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
import com.goodchef.liking.utils.NavigationBarUtil;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/5 上午10:15
 */
public class SelectSexDialog  {
    private Context mContext;
    private AppCompatDialog mDialog;
    private TextView mCancelButton;
    private TextView mTextViewOne;
    private TextView mTextViewTwo;

    public SelectSexDialog(Context context) {
        this.mContext = context;
      //  mDialog = new android.app.AlertDialog.Builder(context, R.style.camera_dialog_no_screen).create();
       mDialog = new AppCompatDialog(context,R.style.camera_dialog_no_screen);
        WindowManager wmManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        boolean hasSoft = NavigationBarUtil.hasSoftKeys(wmManager);
        if (hasSoft) {
            int navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(context);
            //这一行很重要
            DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, 0, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        } else {
            DialogUtils.resetDialogScreenPosition(mDialog, Gravity.BOTTOM, 0, 0, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }

        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_select_sex_or_photo);
        window.setWindowAnimations(R.style.my_dialog_enter_exit);  //添加dialog进入和退出的动画

        mCancelButton = (TextView) window.findViewById(R.id.dialog_cancel_btn);
        mTextViewOne = (TextView) window.findViewById(R.id.dialog_text_one);
        mTextViewTwo = (TextView) window.findViewById(R.id.dialog_text_second);
        setTextViewText();
    }

    public void setTextViewOnClickListener(View.OnClickListener listener) {
        mTextViewOne.setOnClickListener(listener);
        mTextViewTwo.setOnClickListener(listener);
    }


    private void setTextViewText() {
            mTextViewOne.setText(R.string.sex_man);
            mTextViewTwo.setText(R.string.sex_men);

    }

    public void setNegativeClickListener(View.OnClickListener onClickListener) {
        if (mCancelButton != null) {
            mCancelButton.setOnClickListener(onClickListener);
        }
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
