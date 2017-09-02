package com.goodchef.liking.data.remote;

/**
 * Created on 16/1/27.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public interface LiKingRequestCode {
    /**请求成功*/
    int SUCCESS = 0;
    /**非法请求*/
    int ILLEGAL_REQUEST = 10000;
    /**非法参数*/
    int ILLEGAL_ARGUMENT = 10001;
    /**参数缺失异常*/
    int ARGUMENT_HIATUS_EXCEPTION = 10002;
    /**DB连接异常*/
    int DB_CONNECTION_EXCEPTION = 10003;
    /**redis连接异常*/
    int REDIS_CONNECTION_EXCEPTION = 10004;
    /**系统异常,请稍后重试*/
    int SERVER_EXCEPTION = 10005;
    /**请求超时*/
    int REQEUST_TIMEOUT = 10006;
    /**无效手机号*/
    int INVALID_MOBOLE_NUMBER = 10007;
    /**获取验证码失败*/
    int GET_VERIFICATION_CODE_FAILURE = 10008;
    /**非法验证码*/
    int ILLEGAL_VERIFICATION_CODE = 10009;
    /**验证码已过期*/
    int VERIFICATION_INVALID = 10010;
    /**验证码错误，请重试*/
    int VERIFICATION_INCORRECT = 10011;
    /**登录失败*/
    int LOGIN_FAILURE = 10012;
    /**登录态失效，请重新登录*/
    int LOGIN_TOKEN_INVALID = 10013;
    /**退出登录失败，请重试*/
    int LOGOUT_FAILURE = 10014;
    /**购买卡*/
    int BUY_CARD_CONFIRM = 23009;
    /**购买私教课团体课*/
    int BUY_COURSES_ERROR =221009;
    /**无可用会员卡,请购卡后重试*/
    int BUY_COURSES_NO_CARD=22013;
    /**您已拥有其他场馆会员卡，无法进行当前操作*/
    int HAS_OTHER_GYM_CARD=240001;
    /**场馆不存在*/
    int NO_GYM=240000;
    /**该教练下无可约的私教课*/
    int PRIVATE_COURSES_CONFIRM = 221004;

    /**已断开网络，请联系场馆管理员*/
    int SPARTSPOT_OFFLINE_ERRCODE= 41001;
    /**你还不是会员，请先成为场馆的会员*/
    int USER_NO_CARD_ERRCODE = 41002;
    /**你的会员卡已过期，请先进行续卡*/
    int USER_CARD_OVERDUE_ERRCODE = 41003;
    /**没有购买手环*/
    int NO_HAVE_BRACELET_ERRCODE = 70005;

}
