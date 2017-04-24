package com.goodchef.liking.bluetooth;

import com.aaron.common.utils.LogUtils;
import com.goodchef.liking.utils.DecimalFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:14
 * version 1.0.0
 */

public class BlueToothBytesToStringUtil {
    public static final String TAG = "EveryDaySportActivity";


    /**
     * 获取运动数据
     *
     * @param data
     * @return
     */
    public static byte[] getSportData(byte[] data) {
        byte[] sportByte = new byte[data.length - 9];
        for (int i = 8; i < data.length - 2; i++) {
            sportByte[i - 8] = data[i];
        }
        return sportByte;
    }

    /**
     * 获取运动数据数量
     *
     * @param data
     * @return
     */
    public static int getSprotN(byte[] data) {
        int n = (data[7] & 0xff);
        LogUtils.i(TAG, "数据组数 N= " + n);
        return n;
    }


    /**
     * 将运动数据组 分成长度为15的集合组
     *
     * @param data 运动数据组
     * @return
     */
    public static List<byte[]> getSportDataList(byte[] data) {
        byte[] sportByte = getSportData(data);
        int n = getSprotN(data);
        List<byte[]> bytesList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            byte[] bytes = new byte[15];
            for (int j = 0; j < 15; j++) {
                int count = i * 15 + j;
                bytes[j] = sportByte[count];
            }
            bytesList.add(bytes);
        }
        return bytesList;
    }

    /**
     * 获取心率数据组
     *
     * @param data
     * @return
     */
    public static List<byte[]> getHeartRateList(byte[] data) {
        byte[] heartByte = getSportData(data);
        int n = getSprotN(data);
        List<byte[]> bytesList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            byte[] bytes = new byte[3];
            for (int j = 0; j < 3; j++) {
                int count = i * 3 + j;
                bytes[j] = heartByte[count];
            }
            bytesList.add(bytes);
        }
        return bytesList;
    }

    /**
     * 获取心率值
     * @param bytes
     * @return
     */
    public static int getHeartRate(byte[] bytes) {
        int mHeartRate;
        mHeartRate = (bytes[2] & 0xff);
        return mHeartRate;
    }


    /***
     * 转换运动时间
     *
     * @param data
     * @return
     */
    public static String getSportDate(byte[] data) {
        String sportDate;
        int year = data[4];
        int month = data[5];
        int day = data[6];
        String monthStr;
        String dayStr;
        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = month + "";
        }
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = day + "";
        }
        sportDate = "20" + year + "-" + monthStr + "-" + dayStr;
        LogUtils.i(TAG, "运动时间= " + sportDate);
        return sportDate;
    }

    /**
     * 获取运动步数
     *
     * @param data
     * @return
     */
    public static int getSportStep(byte[] data) {
        int sportStep;
        int thousand = (data[3] & 0xff);
        int hundred = (int) ((data[4] & 0xff) * Math.pow(16, 2));
        int ten = (int) ((data[5] & 0xff) * Math.pow(16, 4));
        int a = (int) ((data[6] & 0xff) * Math.pow(16, 6));
        sportStep = thousand + hundred + ten + a;
        LogUtils.i(TAG, "运动步数= " + sportStep);
        return sportStep;
    }

    /**
     * 获取运动kacl
     *
     * @param data
     * @return
     */
    public static String getSportKcal(byte[] data) {
        String sportKcal;
        int thousand = (data[7] & 0xff);
        int hundred = (int) ((data[8] & 0xff) * Math.pow(16, 2));
        int ten = (int) ((data[9] & 0xff) * Math.pow(16, 4));
        int a = (int) ((data[10] & 0xff) * Math.pow(16, 6));
        double kacl = (double) (thousand + hundred + ten + a) / (double) 10;
        sportKcal = DecimalFormatUtil.getDecimalFormat(kacl + "");
        LogUtils.i(TAG, "运动卡路里= " + kacl + "-----" + sportKcal);
        return sportKcal;
    }

    /**
     * 获取运动距离
     *
     * @param data
     * @return
     */
    public static String getSportDistance(byte[] data) {
        String sportDistance;
        int thousand = (data[11] & 0xff);
        int hundred = (int) ((data[12] & 0xff) * Math.pow(16, 2));
        int ten = (int) ((data[13] & 0xff) * Math.pow(16, 4));
        int a = (int) ((data[14] & 0xff) * Math.pow(16, 6));
        double distance = (double) (thousand + hundred + ten + a) / (double) 100;
        sportDistance = DecimalFormatUtil.getTwoDecimalFormat(distance + "");
        LogUtils.i(TAG, "运动距离= " + distance + "---" + sportDistance);
        return sportDistance;
    }
}
