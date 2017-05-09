package com.goodchef.liking.module.base.rxobserver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.aaron.android.framework.base.mvp.view.BaseNetworkLoadView;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.http.helper.VerifyResultUtils;
import com.aaron.http.rxobserver.BaseRequestObserver;
import com.aaron.android.framework.utils.PopupUtils;
import com.aaron.common.utils.ConstantUtils;
import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.eventmessages.LoginOutFialureMessage;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.verify.LiKingRequestCode;
import com.goodchef.liking.module.data.local.Preference;
import com.goodchef.liking.module.login.LoginActivity;
import com.goodchef.liking.mvp.view.BaseLoginView;

import de.greenrobot.event.EventBus;

/**
 * Created on 17/5/8.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class LikingBaseObserver<T extends LikingResult> extends BaseRequestObserver<T> {
    @Override
    public void onNext(T result) {
        super.onNext(result);

    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
    }

    /**
     * 验证Result有效性,处理相关服务器响应的BaseResult错误码
     *
     * @param context 上下文资源
     * @param likingResult  需要校验的BaseResult
     * @return 是否为正确有效的BaseResult
     */
    private boolean isValid(final Context context, final BaseView view, LikingResult likingResult) {
        if (VerifyResultUtils.checkResultSuccess(context, likingResult)) {
            return true;
        }
//        if (context instanceof Activity && ((Activity)context).isFinishing()) {
//            return false;
//        }
        if (likingResult != null) {
            final int errorCode = likingResult.getCode();
            LogUtils.e(TAG, "request server error: " + errorCode);
            switch (errorCode) {
                case LiKingRequestCode.LOGIN_TOKEN_INVALID:
                    if (view != null && view instanceof BaseLoginView) {
                        ((BaseLoginView) view).updateNoLoginView();
                    } else {
                        Preference.setToken(ConstantUtils.BLANK_STRING);
                        Preference.setNickName(ConstantUtils.BLANK_STRING);
                        Preference.setUserPhone(ConstantUtils.BLANK_STRING);
                        Preference.setIsNewUser(null);
                        Preference.setUserIconUrl(ConstantUtils.BLANK_STRING);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        EventBus.getDefault().post(new LoginOutFialureMessage());
                    }
                    break;
                case LiKingRequestCode.REQEUST_TIMEOUT:
//                    initApi(context);
                    break;
                case LiKingRequestCode.ILLEGAL_REQUEST:
                case LiKingRequestCode.ILLEGAL_ARGUMENT:
                case LiKingRequestCode.ARGUMENT_HIATUS_EXCEPTION:
                case LiKingRequestCode.DB_CONNECTION_EXCEPTION:
                case LiKingRequestCode.REDIS_CONNECTION_EXCEPTION:
                case LiKingRequestCode.SERVER_EXCEPTION:
                    if (view != null && view instanceof BaseNetworkLoadView) {
                        ((BaseNetworkLoadView) view).handleNetworkFailure();
                    }
                    break;
                case LiKingRequestCode.BUY_COURSES_ERROR:
                    showBuyCoursesErrorDialog(context, likingResult.getMessage());
                    break;
                case LiKingRequestCode.INVALID_MOBOLE_NUMBER:
                case LiKingRequestCode.GET_VERIFICATION_CODE_FAILURE:
                case LiKingRequestCode.VERIFICATION_INVALID:
                case LiKingRequestCode.LOGIN_FAILURE:
                case LiKingRequestCode.VERIFICATION_INCORRECT:
                case LiKingRequestCode.LOGOUT_FAILURE:
                case LiKingRequestCode.ILLEGAL_VERIFICATION_CODE:
                    PopupUtils.showToast(context, likingResult.getMessage());
                    break;
                default:
                    break;

            }
        }
        return false;
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
//                Intent intent = new Intent(context, LikingHomeActivity.class);
//                context.startActivity(intent);
//                EventBus.getDefault().post(new CoursesErrorMessage());
                dialog.dismiss();
            }
        });
        builder.create().setCancelable(false);
        builder.create().show();
    }
}
