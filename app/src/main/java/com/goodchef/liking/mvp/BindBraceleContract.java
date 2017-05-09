package com.goodchef.liking.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.http.result.LikingResult;
import com.aaron.android.framework.base.mvp.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.bluetooth.BleUtils;
import com.goodchef.liking.bluetooth.BlueCommandUtil;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.mvp.model.BindBraceModel;

/**
 * Created on 2017/3/6
 * Created by sanfen
 *
 * @version 1.0.0
 */

public interface BindBraceleContract {
    interface BindBraceleView extends BaseView {
        void stopBlueToothWhewView();

        void setLayoutBlueOpenStateVisibility(int visibility);

        void setLayoutBlueToothBraceletVisibility(int visibility);

        void setLayoutBlueBoothVisibility(int visibility);

        void setOpenBlueToothTextViewVisibility(int visibility);

        void setConnectBluetoothProgressBarVisibility(int visibility);

        void setClickSearchTextViewText(String text);

        void setBluetoothStateTextViewText(String text);

        void setBlueToothNameTextViewText(String text);

        void setConnectBlueToothTextViewText(String text);

        void setConnectBlueToothTextViewEnable(boolean isEnable);

        boolean isBlueToothWhewViewStarting();

        void blueToothWhewViewStop();

        Handler getHandler();

        void updateBindDevicesView();
    }

    class BindBracePresenter extends BasePresenter<BindBraceleView> {
        BindBraceModel mModel;

        private boolean isLoginSuccess = false;
        private boolean isSendRequest = false;//是否发送过请求
        private boolean mConnectionState = false;

        public BindBracePresenter(Context context, BindBraceleView mainView) {
            super(context, mainView);
            mModel = new BindBraceModel(context, new BindBraceModel.Callback() {
                @Override
                public void callback() {
                    mView.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mView.stopBlueToothWhewView();
                        }
                    }, 1500);
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.setLayoutBlueOpenStateVisibility(View.VISIBLE);//展示会员的设备
                            mView.setLayoutBlueToothBraceletVisibility(View.GONE);//隐藏搜索提示
                            mView.setLayoutBlueBoothVisibility(View.VISIBLE);
                            mView.setClickSearchTextViewText(mContext.getString(R.string.click_search));//显示点击搜索
                            mView.setBluetoothStateTextViewText(mContext.getString(R.string.member_bluetooth_devices));
                            mView.setOpenBlueToothTextViewVisibility(View.GONE);
                            mView.setBlueToothNameTextViewText(mModel.mBluetoothDevice.getName());//展示蓝牙名称
                            mView.setConnectBluetoothProgressBarVisibility(View.GONE);//连接的动画关闭
                            mView.setConnectBlueToothTextViewText(mContext.getString(R.string.connect_blue_tooth));//展示连接文案
                            mView.setConnectBlueToothTextViewEnable(true);
                        }
                    });
                }
            });
        }


        /**
         * 打开蓝牙
         */
        public void openBluetooth(Activity c) {
            mModel.mBleManager.getBleUtils().openBlueTooth(c);
            mView.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mModel.mBleManager.getBleUtils().isOpen()) {
                        mView.setLayoutBlueOpenStateVisibility(View.GONE);
                    } else {
                        mView.setLayoutBlueOpenStateVisibility(View.VISIBLE);
                    }
                }
            }, 4000);
        }


        /**
         * 连接蓝牙
         */
        public void connectBlueTooth() {
            if (mView.isBlueToothWhewViewStarting()) {
                mView.blueToothWhewViewStop();
            }
            if (mModel.mBluetoothDevice != null
                    && !StringUtils.isEmpty(mModel.mBluetoothDevice.getAddress())) {
                mModel.mBindDevicesName = mModel.mBluetoothDevice.getName();
                mModel.mBindDevicesAddress = mModel.mBluetoothDevice.getAddress();
                mModel.mBleManager.connect(mModel.mBluetoothDevice.getAddress());
            }
        }


        /**
         * 发送登录
         */
        public void sendLogin() {
            if (mModel.writecharacteristic != null) {
                byte[] uuId = mModel.muuId.getBytes();
                LogUtils.i("BleService", "sendLogin: " + mModel.muuId);
                mModel.mBleManager.wirteCharacteristic(mModel.writecharacteristic, BlueCommandUtil.getLoginBytes(uuId));
            }
        }

        /**
         * 设置蓝牙时间
         */
        public void setBlueToothTime() {
            if (mModel.writecharacteristic != null) {
                mModel.mBleManager.wirteCharacteristic(mModel.writecharacteristic, BlueCommandUtil.getTimeBytes());
            }
        }

        /**
         * 发送绑定请求到后端
         */
        public void sendBindDeviceRequest(String devicesId) {
            if (isLoginSuccess && !isSendRequest) {
                isSendRequest = true;
                sendDevicesRequest(devicesId);
            }
        }

        /**
         * 发送绑定手环信息
         */
        private void sendDevicesRequest(String devicesId) {
            mModel.bindDevices(devicesId,new RequestCallback<LikingResult>() {
                @Override
                public void onSuccess(LikingResult likingResult) {
                    if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                        mView.updateBindDevicesView();
                    } else {
                        mView.updateBindDevicesView();
                        mView.showToast(likingResult.getMessage());
                    }
                }

                @Override
                public void onFailure(RequestError error) {

                }
            });
        }


        public void pauseBle() {
            if (BleUtils.isSupportBleDevice(mContext)
                    && mModel.mBleManager.isOpen()
                    && mConnectionState && mModel.writecharacteristic != null) {
                mModel.mBleManager.wirteCharacteristic(mModel.writecharacteristic,
                        BlueCommandUtil.getDisconnectBlueTooth());
            }
        }

        /**
         * 断开蓝牙连接，并且清理相关资源
         */
        public void releaseBleConnect() {
            mModel.mBleManager.release();
        }

        public boolean isBleManagerOpen() {
            return mModel.mBleManager.isOpen();
        }

        public void isBleManagerDoScan(boolean enable) {
            mModel.mBleManager.doScan(enable);
        }

        public void setBraceletMac(String stringExtra) {
            mModel.myBraceletMac = stringExtra;
        }

        public void getBlueToothServices() {
            mModel.getBlueToothServices();
        }

        public void setMuuid(String stringExtra) {
            mModel.muuId = stringExtra;
        }

        public void bleManagerDiscoverServices() {
            mModel.mBleManager.discoverServices();
        }

        public void setIsSendRequest(boolean b) {
            isSendRequest = b;
        }

        public void setIsLoginSuccess(boolean b) {
            isLoginSuccess = b;
        }

        public String getBindDevicesName() {
            return mModel.mBindDevicesName;
        }

        public String getBindDevicesAddress() {
            return mModel.mBindDevicesAddress;
        }

        public String getFirmwareInfo() {
            return mModel.mFirmwareInfo;
        }

        public void setFirmwareInfo(byte[] data) {
            mModel.setFirmwareInfo(data);
        }

        public boolean isConnectionState() {
            return mConnectionState;
        }

        public void setConnectionState(boolean b) {
            mConnectionState = b;
        }

        public void bluetoothDeviceListClear() {
            mModel.mBluetoothDeviceList.clear();

        }
    }

}
