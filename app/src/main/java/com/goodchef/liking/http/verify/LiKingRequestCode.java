package com.goodchef.liking.http.verify;

import com.aaron.android.codelibrary.http.result.ServerRequestCode;

/**
 * Created on 16/1/27.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public interface LiKingRequestCode extends ServerRequestCode {
    /**非法请求*/
    public static final int ILLEGAL_REQUEST = 10000;
    /**非法参数*/
    public static final int ILLEGAL_ARGUMENT = 10001;
    /**参数缺失异常*/
    public static final int ARGUMENT_HIATUS_EXCEPTION = 10002;
    /**DB连接异常*/
    public static final int DB_CONNECTION_EXCEPTION = 10003;
    /**redis连接异常*/
    public static final int REDIS_CONNECTION_EXCEPTION = 10004;
    /**系统异常,请稍后重试*/
    public static final int SERVER_EXCEPTION = 10005;
    /**请求超时*/
    public static final int REQEUST_TIMEOUT = 10006;
    /**无效手机号*/
    public static final int INVALID_MOBOLE_NUMBER = 10007;
    /**获取验证码失败*/
    public static final int GET_VERIFICATION_CODE_FAILURE = 10008;
    /**非法验证码*/
    public static final int ILLEGAL_VERIFICATION_CODE = 10009;
    /**验证码已过期*/
    public static final int VERIFICATION_INVALID = 10010;
    /**验证码错误，请重试*/
    public static final int VERIFICATION_INCORRECT = 10011;
    /**登录失败*/
    public static final int LOGIN_FAILURE = 10012;
    /**登录态失效，请重新登录*/
    public static final int LOGIN_TOKEN_INVALID = 10013;
    /**退出登录失败，请重试*/
    public static final int LOGOUT_FAILURE = 10014;
    /**购买卡*/
    public static final int BUY_CARD_CONFIRM = 23009;
    /**购买私教课团体课*/
    public static final int BUY_COURSES_ERROR =221009;
}
