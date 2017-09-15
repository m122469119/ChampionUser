package com.goodchef.liking.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.eventmessages.WifiMessage;
import com.goodchef.liking.utils.NetUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by aaa on 17/9/15.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            LogUtils.i("network", "网络状态 = " + netWorkState);
            EventBus.getDefault().post(new WifiMessage(netWorkState));
        }
    }


}
