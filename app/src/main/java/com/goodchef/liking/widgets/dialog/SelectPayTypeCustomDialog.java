package com.goodchef.liking.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.utils.DialogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.utils.NavigationBarUtil;


/**
 * 说明：选择支付方式自定义dialog
 *
 * @author shaozucheng
 */
public class SelectPayTypeCustomDialog {
    private Context context;
    private Dialog mDialog;
  //  private LinearLayout mBalancePayLayout;
    private LinearLayout mAlipayPayLayput;
    private LinearLayout mWechatPayLayout;

    public SelectPayTypeCustomDialog(Context context, String type) {
        this.context = context;
        mDialog = new android.app.AlertDialog.Builder(context, R.style.pay_dialog_no_screen).create();
        WindowManager wmManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
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
        window.setContentView(R.layout.dialog_select_pay_type);
        window.setWindowAnimations(R.style.my_dialog_enter_exit);  //添加dialog进入和退出的动画

        mAlipayPayLayput = (LinearLayout) window.findViewById(R.id.alipay_pay_layout);
        mWechatPayLayout = (LinearLayout) window.findViewById(R.id.wechat_pay_layout);

        setSelectViewState(type);
    }


    private void setSelectViewState(String type) {
        if (type.equals("")) {
            setPayLayout(mAlipayPayLayput, R.drawable.pay_alipay, R.string.pay_alipay_type, "", false, false);
            setPayLayout(mWechatPayLayout, R.drawable.pay_wechat, R.string.pay_wechat_type, "", false, false);
        } else if (type.equals("0")) {//微信支付
            setPayLayout(mAlipayPayLayput, R.drawable.pay_alipay, R.string.pay_alipay_type, "", false, false);
            setPayLayout(mWechatPayLayout, R.drawable.pay_wechat, R.string.pay_wechat_type, "", false, true);
        } else if (type.equals("1")) {//支付宝
            setPayLayout(mAlipayPayLayput, R.drawable.pay_alipay, R.string.pay_alipay_type, "", false, true);
            setPayLayout(mWechatPayLayout, R.drawable.pay_wechat, R.string.pay_wechat_type, "", false, false);
        }
    }


    private void setPayLayout(View view, int drawableResId, int payTypeText, String blanceText, boolean isShowBalance, boolean isselect) {
        ImageView icon = (ImageView) view.findViewById(R.id.pay_type_icon);
        TextView payType = (TextView) view.findViewById(R.id.tv_pay_type);
        TextView balance = (TextView) view.findViewById(R.id.tv_balance_text);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.pay_Type_checkBox);
        icon.setImageDrawable(context.getResources().getDrawable(drawableResId));
        payType.setText(payTypeText);
        balance.setText(blanceText);
        if (isShowBalance) {
            balance.setVisibility(View.VISIBLE);
        } else {
            balance.setVisibility(View.GONE);
        }
        if (isselect) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    public void setViewOnClickListener(View.OnClickListener onClickListener) {
        mAlipayPayLayput.setOnClickListener(onClickListener);
        mWechatPayLayout.setOnClickListener(onClickListener);
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