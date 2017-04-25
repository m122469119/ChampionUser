package com.goodchef.liking.bluetooth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:34
 * version 1.0.0
 */

public class BlueCommandUtil {

    public static final class Constants {
        public static final UUID SERVER_UUID = UUID.fromString("C3E6FEA0-E966-1000-8000-BE99C223DF6A");
        public static final UUID TX_UUID = UUID.fromString("C3E6FEA1-E966-1000-8000-BE99C223DF6A");
        public static final UUID RX_UUID = UUID.fromString("C3E6FEA2-E966-1000-8000-BE99C223DF6A");
    }

    public static final byte ZERO = 0x00;
    public static final byte ONE = 0x01;

    /**
     * 绑定
     *
     * @return
     */
    public static byte[] getBindBytes(byte[] uuidByte) {
        byte[] bytes = new byte[17];
        bytes[0] = (byte) 0xA9;
        bytes[1] = 0x32;
        bytes[2] = 0x00;
        bytes[3] = 0x0c;
        //11111100000

        for (int i = 0; i < uuidByte.length; i++) {
            bytes[i + 4] = (byte) (uuidByte[i] & 0xff);
        }

//        bytes[4] = 0x01;
//        bytes[5] = 0x01;
//        bytes[6] = 0x01;
//        bytes[7] = 0x01;
//        bytes[8] = 0x01;
//        bytes[9] = 0x01;

        bytes[10] = 0x00;
        bytes[11] = 0x00;
        bytes[12] = 0x00;
        bytes[13] = 0x00;
        bytes[14] = 0x00;

        bytes[15] = (byte) 0xAD;

        byte crc = 0;
        for (int i = 0; i < 16; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[16] = (byte) (crc & 0xFF);
        return bytes;
    }

    /**
     * 登录bytes
     *
     * @return
     */
    public static byte[] getLoginBytes(byte[] uuidByte) {
        byte[] bytes = new byte[17];
        bytes[0] = (byte) 0xA9;
        bytes[1] = 0x34;
        bytes[2] = 0x01;
        bytes[3] = 0x0c;

        for (int i = 0; i < uuidByte.length; i++) {
            bytes[i + 4] = (byte) (uuidByte[i] & 0xff);
        }


//        bytes[4] = 0x01;
//        bytes[5] = 0x01;
//        bytes[6] = 0x01;
//        bytes[7] = 0x01;
//        bytes[8] = 0x01;
//        bytes[9] = 0x01;

//        bytes[10] = (byte) 0xA9;
//        bytes[11] = (byte) 0xBC;
//        bytes[12] = 0x66;
//        bytes[13] = 0x42;
//        bytes[14] = (byte) 0xE4;

        bytes[10] = 0x00;
        bytes[11] = 0x00;
        bytes[12] = 0x00;
        bytes[13] = 0x00;
        bytes[14] = 0x00;

        bytes[15] = (byte) 0xAD;

        byte crc = 0;
        for (int i = 0; i < 16; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[16] = (byte) (crc & 0xFF);
        return bytes;
    }

    /**
     * 解绑
     *
     * @return
     */
    public static byte[] getUnBindBytes() {
        byte[] bytes = new byte[5];
        bytes[0] = (byte) 0xA9;
        bytes[1] = (byte) 0x0C;
        bytes[2] = 0x00;
        bytes[3] = 0x00;

        byte crc = 0;
        for (int i = 0; i < 4; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[4] = (byte) (crc & 0xFF);
        return bytes;
    }

    /**
     * 查找手环
     *
     * @return
     */
    public static byte[] getFindBraceletBytes() {
        byte[] bytes = new byte[5];
        bytes[0] = (byte) 0xA9;
        bytes[1] = (byte) 0x0E;
        bytes[2] = 0x00;
        bytes[3] = 0;
        byte crc = 0;
        for (int i = 0; i < 4; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[4] = (byte) (crc & 0xFF);
        return bytes;
    }

    /**
     * 设置时间
     *
     * @return
     */
    public static byte[] getTimeBytes() {
        byte[] bytes = new byte[11];
        bytes[0] = (byte) 0xA9;
        bytes[1] = (byte) 0x01;
        bytes[2] = 0x00;
        bytes[3] = 0x06;

        byte[] timeBytes = getDateBytes();
        for (int i = 0; i < timeBytes.length; i++) {
            bytes[i + 4] = (byte) (timeBytes[i] & 0xff);
        }

//        bytes[4] = (byte) 0x10;
//        bytes[5] = 0x0c;
//        bytes[6] = 0x18;
//        bytes[7] = 0x0c;
//        bytes[8] = 0x00;
//        bytes[9] = 0x00;


        byte crc = 0;
        for (int i = 0; i < 10; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[10] = (byte) (crc & 0xFF);

        return bytes;
    }


    /**
     * 获取电量
     *
     * @return
     */
    public static byte[] getElectricQuantityBytes() {
        byte[] bytes = new byte[5];
        bytes[0] = (byte) 0xA9;
        bytes[1] = (byte) 0x08;
        bytes[2] = 0x00;
        bytes[3] = 0x00;
        byte crc = 0;
        for (int i = 0; i < 4; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[4] = (byte) (crc & 0xFF);
        return bytes;
    }

    /**
     * 心率测试
     *
     * @return
     */
    public static byte[] getHeartRateTestBytes() {
        byte[] bytes = new byte[5];
        bytes[0] = (byte) 0xA9;
        bytes[1] = (byte) 0xf3;
        bytes[2] = 0x00;
        bytes[3] = 0x01;
        bytes[4] = 0X01;
        byte crc = 0;
        for (int i = 0; i < 4; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[4] = (byte) (crc & 0xFF);
        return bytes;
    }

    /**
     * 获取心率数据
     *
     * @return
     */
    public static byte[] getHeartRateBytes() {
        byte[] bytes = new byte[5];
        bytes[0] = (byte) 0xA9;
        bytes[1] = (byte) 0x26;
        bytes[2] = 0x00;
        bytes[3] = 0x00;
        byte crc = 0;
        for (int i = 0; i < 4; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[4] = (byte) (crc & 0xFF);
        return bytes;
    }

    /**
     * 运动数据请求
     *
     * @return
     */
    public static byte[] getSportBytes() {
        byte[] bytes = new byte[5];
        bytes[0] = (byte) 0xA9;
        bytes[1] = (byte) 0x20;
        bytes[2] = 0x00;
        bytes[3] = 0x00;
        byte crc = 0;
        for (int i = 0; i < 4; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[4] = (byte) (crc & 0xFF);
        return bytes;
    }


    /**
     * 运动数据回应 0 成功 1 失败
     *
     * @return
     */
    public static byte[] getSportRespondBytes(byte response) {
        byte[] bytes = new byte[5];
        bytes[0] = (byte) 0xA9;
        bytes[1] = (byte) 0x21;
        bytes[2] = 0x00;
        bytes[3] = response;
        byte crc = 0;
        for (int i = 0; i < 4; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[4] = (byte) (crc & 0xFF);
        return bytes;
    }

    /**
     * 实时运动数据同步
     *
     * @return
     */
    public static byte[] getSportSynchronizationBytes() {
        byte[] bytes = new byte[6];
        bytes[0] = (byte) 0xA9;
        bytes[1] = (byte) 0x36;
        bytes[2] = 0x00;
        bytes[3] = 0x01;
        bytes[4] = 0x01;
        byte crc = 0;
        for (int i = 0; i < 5; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[5] = (byte) (crc & 0xFF);
        return bytes;
    }

    /**
     * 实时心率数据同步
     *
     * @return
     */
    public static byte[] getHeartRateSynchronizationBytes() {
        byte[] bytes = new byte[6];
        bytes[0] = (byte) 0xA9;
        bytes[1] = (byte) 0x36;
        bytes[2] = 0x00;
        bytes[3] = 0x01;
        bytes[4] = 0x03;
        byte crc = 0;
        for (int i = 0; i < 5; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[5] = (byte) (crc & 0xFF);
        return bytes;
    }

    /**
     * 关闭实时同步数据
     *
     * @return
     */
    public static byte[] getCloseSynchronizationBytes() {
        byte[] bytes = new byte[6];
        bytes[0] = (byte) 0xA9;
        bytes[1] = (byte) 0x36;
        bytes[2] = 0x00;
        bytes[3] = 0x01;
        bytes[4] = 0x00;
        byte crc = 0;
        for (int i = 0; i < 5; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[5] = (byte) (crc & 0xFF);
        return bytes;
    }

    /**
     * 断开蓝牙
     *
     * @return
     */
    public static byte[] getDisconnectBlueTooth() {
        byte[] bytes = new byte[5];
        bytes[0] = (byte) 0xA9;
        bytes[1] = (byte) 0x38;
        bytes[2] = 0x00;
        bytes[3] = 0x00;
        byte crc = 0;
        for (int i = 0; i < 4; i++) {
            crc += (bytes[i] & 0xFF);
        }
        bytes[4] = (byte) (crc & 0xFF);
        return bytes;
    }


    // byte转十六进制字符串
    public static String bytes2HexString(byte[] bytes) {
        String ret = "";
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase(Locale.CHINA);
        }
        return ret;
    }

    /**
     * 将16进制的字符串转换为字节数组 *
     *
     * @param message * @return 字节数组
     */
    public static byte[] getHexBytes(String message) {
        int len = message.length() / 2;
        char[] chars = message.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
    }

    /**
     * 将字符串转成UTF-8编码
     *
     * @param string 字符串
     * @return
     */
    public static String getUTF8XMLString(String string) {
        StringBuffer sb = new StringBuffer();
        sb.append(string);
        String xmString = "";
        String xmlUTF8 = "";
        try {
            xmString = new String(sb.toString().getBytes("UTF-8"));
            xmlUTF8 = URLEncoder.encode(xmString, "UTF-8");
            System.out.println("utf-8 编码：" + xmlUTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return xmlUTF8;
    }

    public static byte[] getDateBytes() {
        byte[] bytes = new byte[6];
     //   Date date = new Date();
       // String year = date.getYear() + "";

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = (calendar.get(Calendar.MONTH) + 1);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);


        String yearStr = year + "";
        String y = yearStr.substring(yearStr.length() - 2, yearStr.length());

        bytes[0] = (byte) Integer.parseInt(y);
        bytes[1] = (byte) month;
        bytes[2] = (byte) day;
        bytes[3] = (byte) hour;
        bytes[4] = (byte) minutes;
        bytes[5] = (byte) second;
        return bytes;
    }

}
