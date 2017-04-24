package com.goodchef.liking.mvp.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.aaron.android.codelibrary.http.RequestCallback;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.bluetooth.BleManager;
import com.goodchef.liking.bluetooth.BlueCommandUtil;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.storage.Preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/3/6
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class BindBraceModel {

    private static final String TAG = "BindBraceModel";

    public BleManager mBleManager;
    public Map<String, BluetoothDevice> map = new HashMap<>();
    public List<BluetoothDevice> mBluetoothDeviceList = new ArrayList<>();
    public boolean isSearchSuccess = false;
    public BluetoothDevice mBluetoothDevice;
    public String mBindDevicesName;//绑定的设备名称
    public String mBindDevicesAddress;//绑定的设备地址

    public String myBraceletMac;//我的手环地址
    public String muuId;//UUID
    public String mFirmwareInfo;//固件版本信息

    public BluetoothGattCharacteristic writecharacteristic;
    public BluetoothGattCharacteristic readcharacteristic;




    public interface Callback{
        void callback();
    }

    public BindBraceModel(Context context, final Callback callback) {
       mBleManager = new BleManager(context, new BluetoothAdapter.LeScanCallback() {
           @Override
           public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
               LogUtils.i(TAG, "needName = " + myBraceletMac + "searchName = " + device.getName() + " mac = " + device.getAddress());
               if (Math.abs(rssi) <= 150) {//过滤掉信号强度小于-150的设备
                   if (!TextUtils.isEmpty(device.getName()) && !TextUtils.isEmpty(device.getAddress())) {
                       if (!StringUtils.isEmpty(myBraceletMac) && myBraceletMac.equals(device.getAddress())) {
                           LogUtils.i(TAG, "找到匹配的手环设备: " + myBraceletMac);
                           map.clear();
                           map.put(device.getAddress(), device);
                           setBlueToothDevicesList(callback);
                       }
                   }
               }
           }
       });
        mBleManager.bind();
    }


    private void setBlueToothDevicesList(final Callback callback) {
        mBluetoothDeviceList.clear();
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String key = (String) entry.getKey();
            BluetoothDevice value = (BluetoothDevice) entry.getValue();
            System.out.println("Key = " + key + ", name = " + value.getName() + "address = " + value.getAddress());
            mBluetoothDeviceList.add(value);
        }
        if (!isSearchSuccess) {
            setSearchBlueToothDevices(callback);
        }
    }

    private void setSearchBlueToothDevices(Callback callback) {
        if (mBluetoothDeviceList != null && mBluetoothDeviceList.size() > 0) {//搜索到设备了
            mBluetoothDevice = mBluetoothDeviceList.get(0);//获取第一个设备
            if (mBluetoothDevice != null) {
                isSearchSuccess = true;
            }
            if (!StringUtils.isEmpty(mBluetoothDevice.getName())) {
                Preference.setBlueToothName(mBluetoothDevice.getAddress(), mBluetoothDevice.getName());
            }
            callback.callback();
        }
    }

    /**
     * 获取蓝牙服务
     */
    public void getBlueToothServices() {
        List<BluetoothGattService> bluetoothGattServices = mBleManager.getSupportedGattServices();
        BluetoothGattService bleService = null;
        for (BluetoothGattService service : bluetoothGattServices) {
            if (service.getUuid().equals(BlueCommandUtil.Constants.SERVER_UUID)) {
                bleService = service;
                break;
            }
        }
        if (bleService != null) {
            writecharacteristic = bleService.getCharacteristic(BlueCommandUtil.Constants.TX_UUID);
            readcharacteristic = bleService.getCharacteristic(BlueCommandUtil.Constants.RX_UUID);
            if (writecharacteristic != null) {
                byte[] uuId = muuId.getBytes();
                mBleManager.wirteCharacteristic(writecharacteristic, BlueCommandUtil.getBindBytes(uuId));
                if (readcharacteristic != null) {
                    mBleManager.setCharacteristicNotification(readcharacteristic, true);
                }
            }
        }
    }

    /**
     * 发送绑定手环信息
     */
    public void bindDevices(String devicesId,RequestCallback<BaseResult> requestCallback) {
        String osName = Build.MODEL;
        String osVersion = Build.VERSION.RELEASE;
        LiKingApi.bindDevices(mBindDevicesName, mFirmwareInfo, devicesId, "android" , osName, osVersion, requestCallback);
    }


    /**
     * 获取固件信息
     *
     * @param data
     */
    public void setFirmwareInfo(byte[] data) {
        if (data.length > 10) {
            byte[] bytes = new byte[6];
            for (int i = 4; i < 10; i++) {
                bytes[i - 4] = data[i];
            }
            String str = new String(bytes);
            mFirmwareInfo = BlueCommandUtil.getUTF8XMLString(str);
            LogUtils.i(TAG, "固件信息= " + mFirmwareInfo);
        }
    }
}
