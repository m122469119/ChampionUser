package com.goodchef.liking.module.brace.mybracelet;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.aaron.http.code.RequestCallback;
import com.aaron.http.code.RequestError;
import com.goodchef.liking.R;
import com.goodchef.liking.bluetooth.BlueCommandUtil;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.result.LikingResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.module.brace.bind.BindBraceModel;
import com.goodchef.liking.data.local.LikingPreference;

/**
 * Created on 2017/5/16
 * Created by sanfen
 *
 * @version 1.0.0
 */

public interface MyBraceContract {

    interface MyBraceView extends BaseView {
        void updateUnBindDevicesView();

        void beforeConnectBlueToothView(String s);

        void setConnectView(String connectStr);

        void showLoginFail();

        void showFirstCheckPromptDialog();

        void setSynchronizationPowerView(int time);

        Handler getHandler();

        void setLayoutBluetoothConnectFailVisibility(int visibility);

        void  setDevicesVersionTextViewText(String text);

        void  setCurrentDevicesNameTextViewText(String text);

        void setDevicesAddressTextViewText(String text);

        void setUnbindTextViewVisibility(int visibility);

        void setMyBraceletTextViewText(String text);
    }


    class MyBracePresenter extends BasePresenter<MyBraceView> {
        BindBraceModel mModel;
        MyBraceModel mMyBraceModel;

        private boolean isGetAllData = false;
        private boolean isLoginFail = false;//是否连接失败
        private boolean isConnect = false;//是否连接
        private int connectState;
        private boolean connectFail = false;//是否连接失败
        private boolean mConnectionState = false;
        private boolean isSendRequest = false;//是否发送过请求
        private boolean isPause = false;
        private boolean isScanDevices = false;

        public MyBracePresenter(Context context, MyBraceView mainView) {
            super(context, mainView);
            mModel = new BindBraceModel(context, new BindBraceModel.Callback() {
                @Override
                public void callback() {
                    if (!isScanDevices) {
                        isScanDevices = true;
                        connect((Activity) mContext);
                    }
                }
            });
            mMyBraceModel = new MyBraceModel();
        }

        /**
         * 连接蓝牙
         */
        public void connect(Activity c) {
            if (!initBlueTooth(c)) {
                mView.beforeConnectBlueToothView(mContext.getString(R.string.bluetooth_connect_fail));
                return;
            }
            if (mMyBraceModel.mSource.equals("BingBraceletActivity")) {
                return;
            }
            if (!isConnect) {
                isConnect = true;
                mModel.mBleManager.stopScan();
                mView.setLayoutBluetoothConnectFailVisibility(View.GONE);
                mView.setConnectView(mContext.getString(R.string.connect_bluetooth_ing));
                mModel.connect();
                connectState = 1;
                connectTimeOutView();
            }
        }


        private void connectTimeOutView() {
            mView.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (connectState == 1) {
                        mView.beforeConnectBlueToothView(mContext.getString(R.string.connect_time_out));
                    }
                }
            }, 20000);
        }


        /**
         * 第二次连接
         */
        public void sendConnect(Activity c) {
            if (!initBlueTooth(c)) {//连接之前先判断蓝牙的状态
                mView.beforeConnectBlueToothView(mContext.getString(R.string.bluetooth_connect_fail));
                return;
            }
            if (mMyBraceModel.mSource.equals("BingBraceletActivity")) {
                return;
            }
            if (!connectFail) {
                mModel.stopScan();
                mView.setLayoutBluetoothConnectFailVisibility(View.GONE);
                connectFail = true;
                mModel.connect();
                connectState = 1;
                connectTimeOutView();
                mView.setConnectView(mContext.getString(R.string.connect_bluetooth_ing));
            } else {
                mView.beforeConnectBlueToothView(mContext.getString(R.string.bluetooth_connect_fail));
            }
        }


        /**
         * 搜索蓝牙
         */
        public void searchBlueTooth(Activity c) {
            if (!initBlueTooth(c)) {
                return;
            }
            scanLeDevice(true);
        }

        public void scanLeDevice(final boolean enable) {
            mModel.mBleManager.doScan(enable);
        }

        /**
         * 打开蓝牙
         */
        public void openBluetooth(Activity c) {
            mModel.mBleManager.getBleUtils().openBlueTooth(c);
        }


        /**
         * 初始化蓝牙
         */
        public boolean initBlueTooth(Activity c) {
            if (!mModel.mBleManager.isOpen()) {
                mModel.mBleManager.getBleUtils().openBlueTooth(c);
                return false;
            }
            return true;
        }


        /**
         * 获取固件信息
         *
         * @param data
         */
        private void setFirmwareInfo(byte[] data) {
            mModel.setFirmwareInfo(data);
            ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!StringUtils.isEmpty(mModel.mFirmwareInfo)) {
                            mView.setLayoutBluetoothConnectFailVisibility(View.GONE);
                            mView.setDevicesVersionTextViewText(mModel.mFirmwareInfo);
                            if (StringUtils.isEmpty(mModel.mBindDevicesName)) {
                                mView.setCurrentDevicesNameTextViewText(mModel.mBindDevicesAddress);
                            } else {
                                mView.setCurrentDevicesNameTextViewText(mModel.mBindDevicesName);
                            }
                            mView.setDevicesAddressTextViewText(mModel.mBindDevicesAddress);
                        }
                    }
                });

        }

        private void setLoginTimeOut() {
            mView.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isGetAllData) {
                                mView.showLoginFail();
                            }
                        }
                    });
                }
            }, 10000);
        }


        /**
         * 设置电量view
         */
        private void setPowerView() {
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isGetAllData = true;
                    mView.setUnbindTextViewVisibility(View.VISIBLE);
                    mView.setSynchronizationPowerView(500);
                    if (LikingPreference.getFirstBindBracelet()) {
                        mView.showFirstCheckPromptDialog();
                    }
                }
            });
        }


        /**
         * 处理蓝牙返回的数据
         */
        public void doCharacteristicData(byte[] data) {
            if (data != null && data.length >= 3) {
                if ((data[1] & 0xff) == 0x33) {//绑定
                    if (data[4] == 0x00) {
                        LogUtils.i(TAG, "绑定成功");
                    } else if (data[4] == 0x01) {
                        LogUtils.i(TAG, "绑定失败");
                    }
                } else if ((data[1] & 0xff) == 0x35) {
                    if (data[4] == 0x00) {
                        LogUtils.i(TAG, "登录成功");
                        setLoginTimeOut();
                        mModel.setBlueToothTime();
                    } else if (data[4] == 0x01) {
                        LogUtils.i(TAG, "登录失败");
                        if (!isLoginFail) {
                            isLoginFail = true;
                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mView.showLoginFail();
                                }
                            });
                        }
                    }
                } else if ((data[1] & 0xff) == 0x0D) {
                    if (data[4] == 0x00) {
                        LogUtils.i(TAG, "解绑成功");
                    } else if (data[4] == 0x01) {
                        LogUtils.i(TAG, "解绑失败");
                    }
                } else if ((data[1] & 0xff) == 0x09) {//电量
                    LogUtils.i(TAG, "电量 == " + (data[4] & 0xff) + "状态：" + (data[5] & 0xff));
                    mModel.mBraceletPower = (data[4] & 0xff);
                    setPowerView();
                } else if ((data[1] & 0xff) == 0x21) {//运动数据返回
                    LogUtils.i(TAG, "运动数据返回 == " + (data[4] & 0xff));
                } else if ((data[1] & 0xff) == 0x31) {//固件版本信息
                    setFirmwareInfo(data);
                }
            }
        }

        public void getBlueToothServices() {
            mModel.getBlueToothServices();
        }

        public void stopScan() {
            mModel.stopScan();
        }

        /**
         * 发送登录
         */
        public void sendLogin() {
            if (mModel.mWriteCharacteristic != null) {
                byte[] uuId = mModel.mUUID.getBytes();
                mModel.mBleManager.wirteCharacteristic(mModel.mWriteCharacteristic, BlueCommandUtil.getLoginBytes(uuId));
            }
        }

        public void unBindDevices(String devicesId) {
            LiKingApi.unBindDevices(devicesId, new RequestCallback<LikingResult>() {
                @Override
                public void onSuccess(LikingResult likingResult) {
                    if (LiKingVerifyUtils.isValid(mContext, likingResult)) {
                        mView.updateUnBindDevicesView();
                    } else {
                        mView.showToast(likingResult.getMessage());
                    }
                }

                @Override
                public void onFailure(RequestError error) {

                }
            });
        }



        public void pauseBlue(){
            isPause = true;
            isConnect = false;
            if (mModel.mBleManager.isOpen() && mConnectionState && mModel.mWriteCharacteristic != null) {
                mModel.mBleManager.wirteCharacteristic(mModel.mWriteCharacteristic, BlueCommandUtil.getDisconnectBlueTooth());
            }
        }


        public void releaseBlue(){
            mModel.mBleManager.release();
        }


        /**
         * 连接成功
         */
        public void setConnectSuccessView() {
            if (mConnectionState) {//连接成功，关闭扫描
                stopScan();
            }
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!StringUtils.isEmpty(mModel.mBluetoothDevice.getName())) {
                        LikingPreference.setBlueToothName(mModel.mBluetoothDevice.getAddress(), mModel.mBluetoothDevice.getName());
                    }
                    mView.setMyBraceletTextViewText(mContext.getString(R.string.connect_success));
                }
            });
            connectSuccessTimeOut();
        }


        private void connectSuccessTimeOut() {
            mView.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isGetAllData()) {
                        sendLogin();
                    }
                }
            }, 20000);
        }



        public void setConnectionState(boolean b){
            this.mConnectionState = b;
        }


        public boolean isConnect(){
            return isConnect;
        }

        public void setIsConnect(boolean isConnect){
            this.isConnect = isConnect;
        }


        public void setConnectState(int connectState) {
            this.connectState = connectState;
        }


        public boolean isGetAllData(){
            return isGetAllData;
        }

        public void setBindDevicesName(String bindDevicesName) {
            mModel.mBindDevicesName = bindDevicesName;
        }

        public void setBindDevicesAddress(String bindDevicesAddress) {
            mModel.mBindDevicesAddress = bindDevicesAddress;
        }

        public void setFirmwareInfo(String stringExtra) {
            mModel.mFirmwareInfo = stringExtra;
        }

        public void setBraceletPower(int braceletPower) {
            mModel.mBraceletPower = braceletPower;
        }

        public void setMyBraceletMac(String myBraceletMac) {
            mModel.mMyBraceletMac = myBraceletMac;
        }

        public void setUUID(String UUID) {
            mModel.mUUID = UUID;
        }

        public void setSource(String source) {
            mMyBraceModel.mSource = source;
        }

        public String getSource() {
            return mMyBraceModel.mSource;
        }

        public String getMyBraceletMac() {
            return mModel.mMyBraceletMac;
        }

        public int getBraceletPower() {
            return mModel.mBraceletPower;
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


        public void setIsScanDevices(boolean isScanDevices) {
            this.isScanDevices = isScanDevices;
        }

        public void discoverServicesBlue() {
            mModel.mBleManager.discoverServices();
        }

        public void setBluetoothDevice() {
            mModel.setBluetoothDevice();
        }
    }
}
