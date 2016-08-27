package com.goodchef.liking.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/26 下午6:11
 */
public class HomeRightDialog {
    private Context mContext;
    private HomeRightBaseDialog mDialog;
    private View dialogView;
    private LinearLayout mNoticeLayout;
    private LinearLayout mOpenDoorLayout;

    public HomeRightDialog(Context context) {
        this.mContext = context;
        mDialog = new HomeRightBaseDialog(context, R.style.camera_dialog_no_screen);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        initDialogView();
        mDialog.setContentView(dialogView);
    }

    public void setAnchor(View view) {
        mDialog.setAnchorView(view);
    }

    public void show() {
        mDialog.show();
    }

    private void initDialogView() {
        dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_home_right, null);
        mNoticeLayout = (LinearLayout) dialogView.findViewById(R.id.layout_notice);
        mOpenDoorLayout = (LinearLayout) dialogView.findViewById(R.id.layout_open_door);
    }

    public void setViewOnClikListener(View.OnClickListener listener) {
        mNoticeLayout.setOnClickListener(listener);
        mOpenDoorLayout.setOnClickListener(listener);
    }

}
