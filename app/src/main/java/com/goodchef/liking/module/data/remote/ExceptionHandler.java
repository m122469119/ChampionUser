package com.goodchef.liking.module.data.remote;


import android.content.Context;
import android.content.DialogInterface;
import android.net.ParseException;

import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.R;
import com.goodchef.liking.http.verify.LiKingRequestCode;
import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONException;

import java.net.ConnectException;

/**
 * Created on 17/5/15.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class ExceptionHandler {
//    private static final int UNAUTHORIZED = 401;
//    private static final int FORBIDDEN = 403;
//    private static final int NOT_FOUND = 404;
//    private static final int REQUEST_TIMEOUT = 408;
//    private static final int INTERNAL_SERVER_ERROR = 500;
//    private static final int BAD_GATEWAY = 502;
//    private static final int SERVICE_UNAVAILABLE = 503;
//    private static final int GATEWAY_TIMEOUT = 504;

    public static ResponseThrowable transformResponseThrowable(Throwable throwable) {
        ResponseThrowable responseThrowable;
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            responseThrowable = new ResponseThrowable(RequestError.ErrorType.HTTP_ERROR, httpException);
//            switch (httpException.code()) {
//                case UNAUTHORIZED:
//                case FORBIDDEN:
//                case NOT_FOUND:
//                case REQUEST_TIMEOUT:
//                case GATEWAY_TIMEOUT:
//                case INTERNAL_SERVER_ERROR:
//                case BAD_GATEWAY:
//                case SERVICE_UNAVAILABLE:
//                default:
//                    break;
//            }
            return responseThrowable;
        } else if (throwable instanceof ApiException) {
            ApiException apiException = (ApiException) throwable;
            responseThrowable = new ResponseThrowable(RequestError.ErrorType.SERVER_ERROR, apiException);
            return responseThrowable;
        } else if (throwable instanceof ParseException
                || throwable instanceof JsonParseException
                || throwable instanceof JSONException) {
            responseThrowable = new ResponseThrowable(RequestError.ErrorType.PARSE_ERROR, throwable);
            return responseThrowable;
        } else if (throwable instanceof ConnectException) {
            responseThrowable = new ResponseThrowable(RequestError.ErrorType.NETWORK_ERROR, throwable);
            return responseThrowable;
        } else {
            responseThrowable = new ResponseThrowable(RequestError.ErrorType.UNKNOWN, throwable);
            return responseThrowable;
        }
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
