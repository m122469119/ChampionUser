package com.goodchef.liking.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.android.framework.utils.PhoneUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.activity.LikingHomeActivity;
import com.goodchef.liking.dialog.PhoneDialog;
import com.goodchef.liking.module.data.local.LikingPreference;

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


    public static void showPhoneDialog(final Activity context){
        final PhoneDialog mPhoneDialog = new PhoneDialog(context, R.style.phone_style);
        mPhoneDialog.setGymPhoneText(LikingHomeActivity.gymTel)
                    .setLikingPhoneText(LikingPreference.getCustomerServicePhone())
                    .setOnCancelListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPhoneDialog.dismiss();
                        }
                    })
                    .setOnGymPhoneListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhoneUtils.phoneCall(context, mPhoneDialog.getGymPhoneText());
                            mPhoneDialog.dismiss();
                        }
                    })
                    .setOnLikingPhoneListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhoneUtils.phoneCall(context, mPhoneDialog.getLikingPhoneText());
                            mPhoneDialog.dismiss();
                        }
                    });
        mPhoneDialog.show();
    }

    /**
     * 购买私教课 团体课，预约团体课
     *
     * @param context
     * @param message
     */
    public static void showBuyCoursesErrorDialog(final Context context, String message) {
        HBaseDialog.Builder builder = new HBaseDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.diaog_got_it, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().setCancelable(false);
        builder.create().show();
    }

}
