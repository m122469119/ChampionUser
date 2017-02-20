package com.goodchef.liking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.goodchef.liking.R;

/**
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/20.
 */

public class PhoneDialog extends Dialog {
    TextView mGymPhone;
    TextView mLikingPhone;
    TextView mCancel;


    View mGymView, mLikingView;


    View.OnClickListener mOnCancelListener,
            mOnGymPhoneListener,
            mOnLikingPhoneListener;

    public PhoneDialog(Context context) {
        super(context);
        initView(context);
    }

    public PhoneDialog(Context context, int theme) {
        super(context, theme);
        initView(context);
    }

    private void initView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_phone, null);
        setContentView(inflate, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dipToPx(180)));
        mGymPhone = (TextView) findViewById(R.id.gym_phone);
        mLikingPhone = (TextView) findViewById(R.id.liking_phone);
        mCancel = (TextView) findViewById(R.id.cancel_txt);
        mGymView = findViewById(R.id.gym_phone_layout);
        mLikingView = findViewById(R.id.liking_phone_layout);

        setCanceledOnTouchOutside(true);
        setCancelable(true);

        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.phone_style);
        window.getDecorView().setPadding(0, 0, 0, 0);
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public PhoneDialog setGymPhoneText(String s){
        mGymPhone.setText(s);
        return this;
    }

    public PhoneDialog setLikingPhoneText(String s){
        mLikingPhone.setText(s);
        return this;
    }

    public String getGymPhoneText(){
        return mGymPhone.getText().toString();
    }

    public String getLikingPhoneText(){
        return mLikingPhone.getText().toString();
    }

    public PhoneDialog setOnCancelListener(View.OnClickListener mListener) {
        this.mOnCancelListener = mListener;
        mCancel.setOnClickListener(mListener);
        return this;
    }

    public PhoneDialog setOnGymPhoneListener(View.OnClickListener mOnGymPhoneListener) {
        this.mOnGymPhoneListener = mOnGymPhoneListener;
        mGymView.setOnClickListener(mOnGymPhoneListener);
        return this;
    }

    public PhoneDialog setOnLikingPhoneListener(View.OnClickListener mOnLikingPhoneListener) {
        this.mOnLikingPhoneListener = mOnLikingPhoneListener;
        mLikingView.setOnClickListener(mOnLikingPhoneListener);
        return this;
    }

    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

}
