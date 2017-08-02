package com.goodchef.liking.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aaron.android.framework.utils.DialogUtils;
import com.goodchef.liking.R;

/**
 * 说明:查看门店
 * Author shaozucheng
 * Time:16/7/18 下午5:21
 */
public class AnnouncementDialog {

    private Context context;
    private AppCompatDialog mDialog;
    private TextView mTextView;
    private ImageButton mButton;

    public AnnouncementDialog(Context context, String announcementStr) {
        this.context = context;
        mDialog = new AppCompatDialog(context, R.style.announcement_dialog_no_screen);
        //这一行很重要
        DialogUtils.resetDialogScreenPosition(mDialog, Gravity.CENTER, 0, 0, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_announcement);
        window.setWindowAnimations(R.style.my_dialog_enter_exit);  //添加dialog进入和退出的动画
        mTextView = (TextView) window.findViewById(R.id.announcement);
        mButton = (ImageButton) window.findViewById(R.id.announcement_cancel_image_button);
        if (announcementStr.length() > 22) {
            mTextView.setGravity(Gravity.LEFT);
        } else {
            mTextView.setGravity(Gravity.CENTER);
        }
        mTextView.setText(announcementStr);
    }

    public void setViewOnClickListener(View.OnClickListener onClickListener) {
        mButton.setOnClickListener(onClickListener);
    }

    public void setButtonDismiss() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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
