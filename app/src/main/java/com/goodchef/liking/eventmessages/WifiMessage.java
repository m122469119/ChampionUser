package com.goodchef.liking.eventmessages;

/**
 * Created by aaa on 17/9/15.
 */

public class WifiMessage {
   private int wifiState;

    public WifiMessage(int wifiState) {
        this.wifiState = wifiState;
    }

    public int getWifiState() {
        return wifiState;
    }
}
