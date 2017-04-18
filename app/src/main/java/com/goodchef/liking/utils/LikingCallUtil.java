package com.goodchef.liking.utils;

import android.app.Activity;
import android.content.DialogInterface;

import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.PhoneUtils;
import com.goodchef.liking.R;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/15 下午7:28
 */
public class LikingCallUtil {

    public static void showCallDialog(final Activity context, String message, final String phone) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(context);
        builder.setMessage(message);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PhoneUtils.phoneCall(context, phone);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
