package com.goodchef.liking.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created on 17/1/22.
 *
 * @author aaron.huang
 * @version 1.0.0
 */

public class BleUtils {

    public BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    /**
     * 检查当前手机是否支持ble蓝牙
     */

    public static boolean isSupportBleDevice(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }
    /**
     * 打开蓝牙，跳转到蓝牙允许界面
     *
     * @param mActivity
     */
    public void openBlueTooth(Activity mActivity) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        mActivity.startActivityForResult(enableBtIntent, 1);
    }


    /**
     * 关闭蓝牙
     */
    public void closeBluetooth() {
        mBluetoothAdapter.disable();
    }

    /**
     * 判断蓝牙是否打开
     *
     * @return boolean
     */
    public boolean isOpen() {
        if (mBluetoothAdapter == null) {
            return false;
        } else {
            return mBluetoothAdapter.isEnabled();
        }
    }
}
