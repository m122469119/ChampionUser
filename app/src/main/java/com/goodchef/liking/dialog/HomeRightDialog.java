package com.goodchef.liking.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goodchef.liking.R;

/**
 * 说明:首页右边的按钮对话框
 * Author shaozucheng
 * Time:16/8/26 下午6:11
 */
public class HomeRightDialog {
    private Context mContext;
    private HomeRightBaseDialog mDialog;
    private View dialogView;
    private RelativeLayout mNoticeLayout;
    private LinearLayout mOpenDoorLayout;
    private TextView mPromptTextView;

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

    public void dismiss(){
        mDialog.dismiss();
    }

    private void initDialogView() {
        dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_home_right, null);
        mNoticeLayout = (RelativeLayout) dialogView.findViewById(R.id.layout_notice);
        mOpenDoorLayout = (LinearLayout) dialogView.findViewById(R.id.layout_open_door);
        mPromptTextView = (TextView) dialogView.findViewById(R.id.notice_prompt);
    }


    public void setViewOnClickListener(View.OnClickListener listener) {
        mNoticeLayout.setOnClickListener(listener);
        mOpenDoorLayout.setOnClickListener(listener);
    }

    /**
     * 设置红色提示
     *
     * @param isShow
     */
    public void setRedPromptShow(boolean isShow) {
        if (isShow) {
            mPromptTextView.setVisibility(View.VISIBLE);
        } else {
            mPromptTextView.setVisibility(View.INVISIBLE);
        }
    }

}
