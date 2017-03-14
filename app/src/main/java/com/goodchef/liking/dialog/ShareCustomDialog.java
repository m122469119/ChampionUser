package com.goodchef.liking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.aaron.android.framework.utils.DialogUtils;
import com.goodchef.liking.R;


/**
 * 说明:
 * Author shaozucheng
 * Time:16/2/26 下午4:54
 */
public class ShareCustomDialog {

    private Context mContext;
    private Dialog mDialog;


    private LinearLayout mWxFriendLayout;
    private LinearLayout mWxCircleLayout;

    private ImageButton mButton;


    public ShareCustomDialog(Context context) {
        this.mContext = context;
        mDialog = new android.app.AlertDialog.Builder(context, R.style.share_dialog_no_screen).create();
        //这一行很重要
        DialogUtils.resetDialogScreenPosition(mDialog, Gravity.CENTER, 0, 0, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_share);
        window.setWindowAnimations(R.style.my_dialog_enter_exit);  //添加dialog进入和退出的动画

        mWxFriendLayout = (LinearLayout) window.findViewById(R.id.layout_wx_friend);
        mWxCircleLayout = (LinearLayout) window.findViewById(R.id.layout_wx_friend_circle);
        mButton = (ImageButton) window.findViewById(R.id.cancel_image_button);
    }

    public void setViewOnClickListener(View.OnClickListener onClickListener) {
        mWxFriendLayout.setOnClickListener(onClickListener);
        mWxCircleLayout.setOnClickListener(onClickListener);
        mButton.setOnClickListener(onClickListener);
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
