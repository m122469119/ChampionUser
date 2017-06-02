package com.goodchef.liking.module.brace.bind;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.common.utils.LogUtils;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.bluetooth.BleUtils;
import com.goodchef.liking.bluetooth.BlueCommandUtil;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;

import cn.jpush.android.api.JPushInterface;

/**
 * Created on 2017/3/6
 * Created by sanfen
 *
 * @version 1.0.0
 */

public interface BindBraceContract {
    interface View extends BaseView {
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

    class Presenter extends RxBasePresenter<View> {
        private static final String TAG = "BindBraceContractPresenter";
        BindBraceModel mModel;

        private boolean isLoginSuccess = false;
        private boolean isSendRequest = false;//是否发送过请求
        private boolean mConnectionState = false;

        public Presenter(final Context context) {
            mModel = new BindBraceModel(context, new BindBraceModel.Callback() {
                @Override
                public void callback() {
                    mView.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mView.stopBlueToothWhewView();
                        }
                    }, 1500);
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.setLayoutBlueOpenStateVisibility(android.view.View.VISIBLE);//展示会员的设备
                            mView.setLayoutBlueToothBraceletVisibility(android.view.View.GONE);//隐藏搜索提示
                            mView.setLayoutBlueBoothVisibility(android.view.View.VISIBLE);
                            mView.setClickSearchTextViewText(context.getString(R.string.click_search));//显示点击搜索
                            mView.setBluetoothStateTextViewText(context.getString(R.string.member_bluetooth_devices));
                            mView.setOpenBlueToothTextViewVisibility(android.view.View.GONE);
                            mView.setBlueToothNameTextViewText(mModel.mBluetoothDevice.getName());//展示蓝牙名称
                            mView.setConnectBluetoothProgressBarVisibility(android.view.View.GONE);//连接的动画关闭
                            mView.setConnectBlueToothTextViewText(context.getString(R.string.connect_blue_tooth));//展示连接文案
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
                    if (mModel.mBleManager == null) {
                        return;
                    }
                    BleUtils bleUtils = mModel.mBleManager.getBleUtils();

                    if (bleUtils == null) {
                        return;
                    }

                    if (bleUtils.isOpen()) {
                        mView.setLayoutBlueOpenStateVisibility(android.view.View.GONE);
                    } else {
                        mView.setLayoutBlueOpenStateVisibility(android.view.View.VISIBLE);
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
            if (mModel.mWriteCharacteristic != null) {
                byte[] uuId = mModel.mUUID.getBytes();
                LogUtils.i("BleService", "sendLogin: " + mModel.mUUID);
                mModel.mBleManager.wirteCharacteristic(mModel.mWriteCharacteristic, BlueCommandUtil.getLoginBytes(uuId));
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

            mModel.bindDevices(devicesId)
            .subscribe(addObserverToCompositeDisposable(new LikingBaseObserver<LikingResult>(mView) {
                @Override
                public void onNext(LikingResult value) {
                    if(value == null) return;
                    mView.updateBindDevicesView();
                }

                @Override
                public void apiError(ApiException apiException) {
                    super.apiError(apiException);
                    mView.updateBindDevicesView();
                }

                @Override
                public void networkError(Throwable throwable) {
                    super.networkError(throwable);
                    mView.updateBindDevicesView();
                }
            }));
        }

        public void pauseBle(Context context) {
            if (BleUtils.isSupportBleDevice(context)
                    && mModel.mBleManager.isOpen()
                    && mConnectionState && mModel.mWriteCharacteristic != null) {
                mModel.mBleManager.wirteCharacteristic(mModel.mWriteCharacteristic,
                        BlueCommandUtil.getDisconnectBlueTooth());
            }
        }

        private void setLoginTimeOut(final Context context) {
            mView.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.setOpenBlueToothTextViewVisibility(android.view.View.GONE);
                            mView.setConnectBluetoothProgressBarVisibility(android.view.View.GONE);
                            mView.setConnectBlueToothTextViewText(context.getString(R.string.loging_out_fail));
                            mView.setConnectBlueToothTextViewEnable(true);
                        }
                    });
                }
            }, 10000);
        }

        /**
         * 处理单包蓝牙数据，在这个界面值涉及到单包的数据
         *
         * @param data
         */
        public void doCharacteristicOnePackageData(final Context context, byte[] data) {
            if (data.length >= 3) {
                if ((data[1] & 0xff) == 0x33) {//绑定
                    if (data[4] == 0x00) {
                        LogUtils.i("BleService", "绑定成功");
                        sendLogin();
                        setLoginTimeOut(context);
                    } else if (data[4] == 0x01) {
                        LogUtils.i("BleService", "绑定失败");
                    }
                } else if ((data[1] & 0xff) == 0x35) {
                    if (data[4] == 0x00) {
                        LogUtils.i("BleService", "登录成功");
                        setIsLoginSuccess(true);
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mView.setOpenBlueToothTextViewVisibility(android.view.View.GONE);
                                mView.setConnectBluetoothProgressBarVisibility(android.view.View.GONE);
                                mView.setConnectBlueToothTextViewText(context.getString(R.string.connect_bluetooth_success));
                            }
                        });
                        mModel.setBlueToothTime();
                        sendBindDeviceRequest(JPushInterface.getUdid(context));
                    } else if (data[4] == 0x01) {
                        LogUtils.i("BleService", "登录失败");
                    }
                } else if ((data[1] & 0xff) == 0x0D) {
                    if (data[4] == 0x00) {
                        LogUtils.i("BleService", "解绑成功");
                    } else if (data[4] == 0x01) {
                        LogUtils.i("BleService", "解绑失败");
                    }
                } else if ((data[1] & 0xff) == 0x09) {//电量
                    LogUtils.i(TAG, "电量 == " + (data[4] & 0xff) + "状态：" + (data[5] & 0xff));
                    mModel.mBraceletPower = (data[4] & 0xff);
                } else if ((data[1] & 0xff) == 0x27) {
                    LogUtils.i(TAG, "心率 == " + (data[4] & 0xff));
                } else if ((data[1] & 0xff) == 0x21) {//运动数据返回
                    LogUtils.i(TAG, "运动数据返回 == " + (data[4] & 0xff));
                } else if ((data[1] & 0xff) == 0x31) {//固件版本信息
                    setFirmwareInfo(data);
                }

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
            mModel.mMyBraceletMac = stringExtra;
        }

        public void getBlueToothServices() {
            mModel.getBlueToothServices();
        }

        public void setMuuid(String stringExtra) {
            mModel.mUUID = stringExtra;
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

        public int getBraceletPower() {
            return mModel.mBraceletPower;
        }

    }

}
