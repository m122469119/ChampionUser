package com.goodchef.liking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aaron.android.framework.library.imageloader.HImageView;
import com.aaron.android.framework.utils.DialogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.utils.NavigationBarUtil;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/8 下午7:40
 */
public class MapStoreDialog {
    private Context mContext;
    private Dialog mDialog;
    private TextView mNameTextView;
    private TextView mAddressTextView;
    private HImageView mHImageView;

    public MapStoreDialog(Context context) {
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
        window.setContentView(R.layout.dialog_store_map_detials);
        window.setWindowAnimations(R.style.my_dialog_enter_exit);  //添加dialog进入和退出的动画

        mNameTextView = (TextView) window.findViewById(R.id.map_store_name);
        mAddressTextView = (TextView) window.findViewById(R.id.store_address);
        mHImageView = (HImageView) window.findViewById(R.id.store_image);

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
