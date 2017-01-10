package com.goodchef.liking.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.goodchef.liking.R;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:23
 * version 1.0.0
 */

public class HeartRateDialog {
    private Context mContext;
    private AppCompatDialog mDialog;
    private ImageView mCancelButton;


    public HeartRateDialog(Context context) {
        this.mContext = context;
        mDialog = new AppCompatDialog(context, R.style.dialog_ios_style);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_heart_rate);
        // window.setWindowAnimations(R.style.my_dialog_enter_exit);  //添加dialog进入和退出的动画
        mCancelButton = (ImageView) window.findViewById(R.id.dialog_heart_rate_cancel);
    }


    public void setCancelClickListener(final CancelOnClickListener cancelClickListener) {
        if (mCancelButton != null) {
            mCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelClickListener.onCancelClickListener(mDialog);
                }
            });
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

    public interface CancelOnClickListener {
        void onCancelClickListener(AppCompatDialog dialog);
    }
}
