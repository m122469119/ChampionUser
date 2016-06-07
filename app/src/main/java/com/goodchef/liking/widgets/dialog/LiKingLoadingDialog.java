package com.goodchef.liking.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.goodchef.liking.R;


/**
 * Created on 15/9/30.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class LiKingLoadingDialog extends Dialog {

    private Animation mAnimation;

    public LiKingLoadingDialog(Context context) {
        this(context, R.style.CustomProgressDialog);
    }

    public LiKingLoadingDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.stateview_loading_view);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        mAnimation = AnimationUtils.loadAnimation(context, com.aaron.android.framework.R.anim.stateview_loading_rotate);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            return;
        }
        ImageView imageView = (ImageView) findViewById(R.id.online_refresh_animation);
        imageView.startAnimation(mAnimation);
    }


    /**
     * setMessage 提示内容
     * @param strMessage
     * @return
     */
    public void setMessage(String strMessage) {
        TextView tvMsg = (TextView) findViewById(R.id.textview_loading_dialog_message);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
    }
}
