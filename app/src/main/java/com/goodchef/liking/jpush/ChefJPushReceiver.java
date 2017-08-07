package com.goodchef.liking.jpush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.aaron.android.framework.base.ui.BaseActivity;
import com.aaron.android.framework.base.widget.web.HDefaultWebActivity;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.data.AnnouncementDirect;
import com.goodchef.liking.eventmessages.PushHasMessage;
import com.goodchef.liking.module.card.my.MyCardActivity;
import com.goodchef.liking.module.card.order.MyOrderActivity;
import com.goodchef.liking.module.course.MyLessonActivity;
import com.goodchef.liking.module.home.LikingHomeActivity;
import com.goodchef.liking.module.message.MessageActivity;
import com.goodchef.liking.utils.AppStatusUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;


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
    public static final String MSG = "msg";
    public static final String DIRECT_TYPE_HTML5 = "h5";
    public static final String DIRECT_ANNOUNCEMENT = "announcement";

    public static final int ANNOUNCEMENT_NOTICE_ID = 0x00000001;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        LogUtils.d(TAG, "[ChefJPushReceiverr] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            LikingPreference.setJPushRegistrationId(regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            LogUtils.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            processCustomMessage(context, bundle);
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
                    } else if (CARD.equals(direct)) { //点击跳转至我的会员卡
                        toMyCardVipInfo(context);
                    } else if (DIRECT_ANNOUNCEMENT.equals(direct)) {
                        //  String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
                        toNoticeInfo(extras, context);
                    } else if (MSG.equals(direct)) {
                        JSONObject jsonObject = new JSONObject(data);
                        String msgId = jsonObject.getString("msg_id");
                        LogUtils.i(TAG, "msgId = " + msgId);
                        if (StringUtils.isEmpty(msgId)) {
                            return;
                        }
                        EventBus.getDefault().post(new PushHasMessage());
                        Intent intent = new Intent(context, MessageActivity.class);
                        intent.putExtra(MessageActivity.CURRENT_TAB, 1);
                        intent.putExtra(MessageActivity.MSG_ID, msgId);
                        intent.putExtra(MessageActivity.ENTER, "push");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
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

    private void toMyCardVipInfo(Context context) {
        LogUtils.d(TAG, "MyCardActivity");
        Intent intent = new Intent(context, MyCardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void toNoticeInfo(String extras, Context context) {
        Gson gson = new Gson();
        AnnouncementDirect announcement = gson.fromJson(extras, AnnouncementDirect.class);
        if (announcement == null || announcement.getData() == null) {
            return;
        }
        LikingPreference.setHomeAnnouncementId(announcement.getData());

        Intent intent = new Intent(context, LikingHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(LikingHomeActivity.ACTION, LikingHomeActivity.SHOW_PUSH_NOTICE);
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

        if (StringUtils.isEmpty(extras)) {
            return;
        }

        JSONObject extraJsonObject = null;
        try {
            extraJsonObject = new JSONObject(extras);
            String directType = extraJsonObject.getString(EXTRA_KEY_DIRECT);
            switch (directType) {
                case DIRECT_ANNOUNCEMENT:
                    String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
                    toAnnouncement(extras, context);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void toAnnouncement(String extras, Context context) {
        Gson gson = new Gson();
        AnnouncementDirect announcement = gson.fromJson(extras, AnnouncementDirect.class);
        if (announcement == null || announcement.getData() == null) {
            return;
        }

        LikingPreference.setHomeAnnouncementId(announcement.getData());

        if (!AppStatusUtils.appIsRunning(context, AppStatusUtils.getAppPackageName(context))
                || BaseActivity.isAppToBackground()) {
            Intent resultIntent = new Intent(context, LikingHomeActivity.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(LikingHomeActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            showNotification(context,
                    announcement.getData().getGymName(),
                    announcement.getData().getGymContent(),
                    resultPendingIntent);
        } else if (AppStatusUtils.getTopActivityClass(context).equals("com.goodchef.liking.module.home.LikingHomeActivity")) {
            Intent intent = new Intent(context, LikingHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(LikingHomeActivity.ACTION, LikingHomeActivity.SHOW_PUSH_NOTICE_RECEIVED);
            context.startActivity(intent);
        }
    }

    private void showNotification(Context ctx, String title, String message, PendingIntent intent) {
        Notification.Builder builder = new Notification.Builder(ctx)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(intent);
        Notification build = builder.build();
        build.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(ANNOUNCEMENT_NOTICE_ID, build);
    }


}
