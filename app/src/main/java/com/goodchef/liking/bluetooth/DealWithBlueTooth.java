package com.goodchef.liking.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;

import com.aaron.android.codelibrary.utils.LogUtils;
import com.aaron.android.framework.utils.PopupUtils;

import java.util.List;
import java.util.UUID;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午2:46
 * version 1.0.0
 */

public class DealWithBlueTooth {
    private final String TAG = "DealWithBlueTooth";
    private Context mContext;

    public static final UUID SERVER_UUID = UUID.fromString("C3E6FEA0-E966-1000-8000-BE99C223DF6A");
    public static final UUID TX_UUID = UUID.fromString("C3E6FEA1-E966-1000-8000-BE99C223DF6A");
    public static final UUID RX_UUID = UUID.fromString("C3E6FEA2-E966-1000-8000-BE99C223DF6A");

    public static int STATE_DISCONNECTED = 0; //设备无法连接
    public static int STATE_CONNECTING = 1;  //设备正在连接状态
    public static int STATE_CONNECTED = 2;   //设备连接完毕

    public BluetoothGatt mBluetoothGatt;

    public BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private static final int Nou = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == Nou) {
                // 每隔10s响一次
                handler.sendEmptyMessageDelayed(Nou, 5000);
            }
        }
    };

    public DealWithBlueTooth(Context context) {
        mContext = context;
    }

    /**
     * 检查是否支持蓝牙
     *
     * @return
     */
    public boolean isSupportBlueTooth() {
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            PopupUtils.showToast("设备不支持蓝牙");
            return false;
        } else {
            return true;
        }
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


    /**
     * 发送消息
     */
    public void sendHandleMessage() {
        if (handler != null) {
            handler.sendEmptyMessage(Nou);
        }
    }

    /**
     * 消除消息
     */
    public void removeHandleMessage() {
        if (handler != null) {
            handler.removeMessages(Nou);
        }
    }


    /**
     * 开始搜索
     *
     * @param callback LeScanCallback
     */
    public void startLeScan(BluetoothAdapter.LeScanCallback callback) {
        mBluetoothAdapter.startLeScan(callback); //开始搜索
    }

    /**
     * 开始搜索
     *
     * @param uuid     uuid
     * @param callback LeScanCallback
     */
    public void startLeScan(UUID[] uuid, BluetoothAdapter.LeScanCallback callback) {
        mBluetoothAdapter.startLeScan(uuid, callback); //开始搜索
    }

    /**
     * 停止搜索
     *
     * @param callback LeScanCallback
     */
    public void stopLeScan(BluetoothAdapter.LeScanCallback callback) {
        mBluetoothAdapter.stopLeScan(callback);//停止搜索
    }


    /**
     * 连接蓝牙
     *
     * @param address  蓝牙mac 地址
     * @param callback BluetoothGattCallback
     * @return
     */
    public boolean connect(Context context, String address, BluetoothGattCallback callback) {
        if (mBluetoothAdapter == null || address == null) {
            LogUtils.d(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        // Previously connected device. Try to reconnect. (先前连接的设备。 尝试重新连接)
//        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
//            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
//            if (mBluetoothGatt.connect()) {
//                mConnectionState = STATE_CONNECTING;
//                return true;
//            } else {
//                return false;
//            }
//        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            LogUtils.i(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // 该函数才是真正的去进行连接 一个Context对象，自动连接（boolean值,表示只要BLE设备可用是否自动连接到它）
        mBluetoothGatt = device.connectGatt(context, false, callback);
        LogUtils.i(TAG, "Trying to create a new connection.");
        return true;
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        LogUtils.i(TAG,mBluetoothGatt == null ? "null":mBluetoothGatt.toString());
        if (mBluetoothGatt == null) {
            return;
        }

        mBluetoothGatt.disconnect();
    }

    /**
     * 关闭
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
    }


    /**
     * 获取蓝牙服务
     *
     * @return
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;
        return mBluetoothGatt.getServices();   //此处返回获取到的服务列表
    }

    /**
     * 设置蓝牙通知
     *
     * @param characteristic
     * @param enabled
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            LogUtils.i(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
        for (BluetoothGattDescriptor dp : descriptors) {
            dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(dp);
        }
    }

    /**
     * 写入数据
     *
     * @param characteristic
     * @param bytes
     */
    public void wirteCharacteristic(BluetoothGattCharacteristic characteristic, byte[] bytes) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            LogUtils.i(TAG, "BluetoothAdapter not initialized");
            return;
        }
        characteristic.setValue(bytes);
        mBluetoothGatt.writeCharacteristic(characteristic);
    }


}
