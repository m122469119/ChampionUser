package com.goodchef.liking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.aaron.android.framework.utils.DialogUtils;
import com.aaron.android.framework.utils.DisplayUtils;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/27 下午7:56
 */
public class HomeRightBaseDialog extends Dialog {

    private Context mContext;
    private View arrowView;

    public HomeRightBaseDialog(Context context) {
        super(context);
        init(context);
    }

    public HomeRightBaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    @Override
    public void setContentView(View view) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.home_right_pop_root, null);
        arrowView = rootView.findViewById(R.id.ivArrow);
        FrameLayout frame = (FrameLayout) rootView.findViewById(R.id.flContentRoot);
        frame.addView(view);
        super.setContentView(rootView);
    }

    public void setAnchorView(View view) {
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        loc[1] += view.getHeight();
        int width = view.getWidth();
        arrowView.setTranslationX(loc[0] + width / 2);
        DialogUtils.resetDialogScreenPosition(this, Gravity.TOP|Gravity.RIGHT,
                0, loc[1], DisplayUtils.dp2px(150), WindowManager.LayoutParams.WRAP_CONTENT);
    }


}
