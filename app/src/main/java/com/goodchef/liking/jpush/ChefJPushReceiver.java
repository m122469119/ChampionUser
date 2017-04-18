package com.goodchef.liking.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.web.HDefaultWebActivity;
import com.goodchef.liking.activity.MyCardActivity;
import com.goodchef.liking.activity.MyLessonActivity;
import com.goodchef.liking.activity.MyOrderActivity;
import com.goodchef.liking.storage.Preference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;


/**
 * Created on 16/2/24.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public class ChefJPushReceiver extends BroadcastReceiver {
    private static final String TAG = "ChefJPushReceiver";

    private static final String EXTRA_KEY_DIRECT_TYPE = "directType";
    private static final String EXTRA_KEY_DIRECT = "direct";
    private static final String EXTRA_KEY_DATA = "data";
    public static final String DIRECT_TYPE_NATIVE = "native";
    public static final String DIRECT_TYPE_OUTER = "outer";

    public static final String DIRECT_UPDATE = "update";
    public static final String FOOD = "food";
    public static final String TEAM = "team";
    public static final String CARD = "card";
    public static final String DIRECT_TYPE_HTML5 = "h5";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        LogUtils.d(TAG, "[ChefJPushReceiverr] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Preference.setJPushRegistrationId(regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtils.d(TAG, "[MyReceiver] 用户点击打开了通知");
            handleNotificationMessage(context, bundle);
            //打开自定义的Activity
//            Intent i = new Intent(context, TestActivity.class);
//            i.putExtras(bundle);
//            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            LogUtils.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            LogUtils.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            LogUtils.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void handleNotificationMessage(Context context, Bundle bundle) {
        if (bundle == null) {
            return;
        }
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        LogUtils.d(TAG, "push message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (StringUtils.isEmpty(extras)) {
            return;
        }
        try {
            JSONObject extraJsonObject = new JSONObject(extras);
            String directType = extraJsonObject.getString(EXTRA_KEY_DIRECT_TYPE);
            String direct = extraJsonObject.getString(EXTRA_KEY_DIRECT);
            String data = extraJsonObject.getString(EXTRA_KEY_DATA);
            LogUtils.d(TAG, "directType: " + directType + " direct: " + direct);
            if (StringUtils.isEmpty(directType)) {
                return;
            }
            switch (directType) {
                case DIRECT_TYPE_NATIVE: //跳转
                    if (StringUtils.isEmpty(direct)) {
                        return;
                    }
//                    if (DIRECT_UPDATE.equals(direct)) { //版本更新
//                        if (StringUtils.isEmpty(data)) { //data为空的情况
//                            Toast.makeText(context, "应用有新版本更新", Toast.LENGTH_SHORT).show();
//                        } else { //data不为空的情况
//                            JSONObject jsonObject = new JSONObject(data);
//                            String url = jsonObject.getString("android_url");
//                            if (!StringUtils.isEmpty(url)) {
//                                HDefaultWebActivity.launch(context, url, "", Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            }
//                        }
//                    } else
                    if (FOOD.equals(direct)) { //跳转到营养餐列表
//                        if (StringUtils.isEmpty(data)) {
//                            return;
//                        }
                        LogUtils.d(TAG, "data: " + data);
                        toMyOrderList(context);
                    } else if (TEAM.equals(direct)) { //跳转到课程列表
                        toMyGroupCoursesList(context);
                    } else if(CARD.equals(direct)) { //点击跳转至我的会员卡
                        toMyCardVipInfo(context);
                    }
                    break;
                case DIRECT_TYPE_HTML5:
                    if (StringUtils.isEmpty(direct)) {
                        return;
                    }
                    HDefaultWebActivity.launch(context, direct, "", Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    break;
                case DIRECT_TYPE_OUTER://更新
                    if (StringUtils.isEmpty(direct)) {
                        return;
                    }
                    if (DIRECT_UPDATE.equals(direct)) { //版本更新
                        if (StringUtils.isEmpty(data)) { //data为空的情况
                            Toast.makeText(context, "应用有新版本更新", Toast.LENGTH_SHORT).show();
                        } else { //data不为空的情况
                            JSONObject jsonObject = new JSONObject(data);
                            String url = jsonObject.getString("android_url");
                            if (!StringUtils.isEmpty(url)) {
                                HDefaultWebActivity.launch(context, url, "", Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            LogUtils.e(TAG, "Get message extra JSON error!");
        }

    }


    private void toMyGroupCoursesList(Context context) {
        LogUtils.d(TAG, "toGroupCourses");
        Intent intent = new Intent(context, MyLessonActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void toMyOrderList(Context context) {
        LogUtils.d(TAG, "toGroupCourses");
        Intent intent = new Intent(context, MyOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void toMyCardVipInfo(Context context){
        LogUtils.d(TAG, "MyCardActivity");
        Intent intent = new Intent(context, MyCardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    LogUtils.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    LogUtils.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isForeground) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        LogUtils.d(TAG, "message: " + message + " extras: " + extras);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (null != extraJson && extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            context.sendBroadcast(msgIntent);
//        }
    }
}
