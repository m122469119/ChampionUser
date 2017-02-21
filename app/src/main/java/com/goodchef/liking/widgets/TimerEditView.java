package com.goodchef.liking.widgets;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.goodchef.liking.R;

/**
 *
 * 延时触发的EditText
 *
 * @author MZ
 * @email sanfenruxi1@163.com
 * @date 2017/2/17.
 */
public class TimerEditView extends EditText {

    private static final String TAG = "TimerEditView";

    private static final int MSG_CODE = 0x00000001;

    public static final int DELAYED_TIME = 2;

    private long mTimer;

    private String mText;

    private int mMsgCount = 0;

    public TimerEditView(Context context) {
        this(context, null);
    }

    public TimerEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimerEditView);
        float integer = typedArray.getFloat(R.styleable.TimerEditView_delayedTime, DELAYED_TIME);
        mTimer = (long) (integer * 1000);
        typedArray.recycle();
        addTextChangedListener(mTextWatcher);
    }


    private onTextChangerListener mListener = null;

    public void setOnTextChangerListener(onTextChangerListener listener) {
        this.mListener = listener;
    }

    public interface onTextChangerListener {
        void onTextChanger(String text);
    }

    private TimerTextWatcher mTextWatcher = new TimerTextWatcher();


    private class TimerTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            mText = s.toString();
            mMsgCount ++;
            Message msg = Message.obtain();
            msg.what = MSG_CODE;
            mHandler.sendMessageDelayed(msg, mTimer);
        }
    }

    public void noChangeSetText(String text) {
        removeTextChangedListener(mTextWatcher);
        setText(text);
        addTextChangedListener(mTextWatcher);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_CODE) {
                if (mMsgCount == 1) {
                    if (mListener != null) {
                        mListener.onTextChanger(mText);
                    }
                    mMsgCount = 0;
                } else {
                    mMsgCount--;
                }
            }
        }
    };






}
