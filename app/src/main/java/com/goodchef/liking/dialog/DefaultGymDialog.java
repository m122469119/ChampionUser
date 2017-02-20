package com.goodchef.liking.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.goodchef.liking.R;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午3:34
 * version 1.0.0
 */

public class DefaultGymDialog {

    private Context mContext;
    private AppCompatDialog mDialog;
    private TextView mCancelButton;
    private TextView mConfirmButton;
    private TextView mDefaultPromptTextView;
    private TextView mDefaultGymNameTextView;


    public DefaultGymDialog(Context context) {
        this.mContext = context;
        mDialog = new AppCompatDialog(context, R.style.dialog_ios_style);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_default_gym);
        mConfirmButton = (TextView) window.findViewById(R.id.go_to_change_gym_TextView);
        mCancelButton = (TextView) window.findViewById(R.id.waite_change_gym_TextView);
        mDefaultPromptTextView = (TextView) window.findViewById(R.id.gym_default_prompt);
        mDefaultGymNameTextView = (TextView) window.findViewById(R.id.default_gym_name_TextView);
    }


    /**
     * 设置默认场馆名称
     * @param gymName 场馆名称
     */
    public void setDefaultPromptView(String gymName){
        mDefaultPromptTextView.setVisibility(View.VISIBLE);
        mDefaultPromptTextView.setText(R.string.current_default_gym_prompt);
        mDefaultGymNameTextView.setText(gymName);
    }


    public void  setCurrentCityNotOpen(String openPrompt){
        mDefaultPromptTextView.setVisibility(View.GONE);
        mDefaultGymNameTextView.setText(openPrompt);
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

    public void setConfirmClickListener(final ConfirmOnClickListener confirmClickListener) {
        if (mConfirmButton != null) {
            mConfirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmClickListener.onConfirmClickListener(mDialog);
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


}
