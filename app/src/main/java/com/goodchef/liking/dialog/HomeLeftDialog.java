package com.goodchef.liking.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.Window;

import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/26 下午6:11
 */
public class HomeLeftDialog  {
    private Context mContext;
    private AppCompatDialog mDialog;

    public HomeLeftDialog(Context context) {
        this.mContext = context;
        mDialog = new AppCompatDialog(context, R.style.camera_dialog_no_screen);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_home_left);
        window.setWindowAnimations(R.style.my_dialog_enter_exit);  //添加dialog进入和退出的动画
    }

    public void setAnchor(View view) {
      //  mDialog.setAnchorView(view);
    }
}
