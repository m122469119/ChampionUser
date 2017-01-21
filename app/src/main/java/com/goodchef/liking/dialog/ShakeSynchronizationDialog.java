package com.goodchef.liking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.aaron.android.framework.utils.DialogUtils;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:26
 * version 1.0.0
 */

public class ShakeSynchronizationDialog {

    private Context context;
    private Dialog mDialog;
    private ImageButton mButton;

    public ShakeSynchronizationDialog(Context context) {
        this.context = context;
        mDialog = new android.app.AlertDialog.Builder(context, R.style.share_dialog_no_screen).create();
        //这一行很重要
        DialogUtils.resetDialogScreenPosition(mDialog, Gravity.CENTER, 0, 0, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_synchronization_data);
       // window.setWindowAnimations(R.style.my_dialog_enter_exit);  //添加dialog进入和退出的动画

        mButton = (ImageButton) window.findViewById(R.id.cancel_button);

    }

    public void setButtonOnClickListener(View.OnClickListener listener){
        mButton.setOnClickListener(listener);
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
