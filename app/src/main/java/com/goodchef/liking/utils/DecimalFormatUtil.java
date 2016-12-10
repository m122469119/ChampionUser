package com.goodchef.liking.utils;

import com.aaron.android.codelibrary.utils.StringUtils;

import java.text.DecimalFormat;

/**
 * 说明:格式化小数点工具类
 * Author : shaozucheng
 * Time: 下午4:29
 * version 1.0.0
 */

public class DecimalFormatUtil {

    /**
     * 保留一位小数
     *
     * @param number
     * @return
     */
    public static String getDecimalFormat(String number) {
        String numStr = "0";
        if (!StringUtils.isEmpty(number)) {
            if (Double.parseDouble(number) == 0d) {
                numStr = "0";
                return numStr;
            }
            DecimalFormat df = new DecimalFormat("#0.0");
            numStr = df.format(Double.parseDouble(number));
            return numStr;
        } else {
            return numStr;
        }
    }
}
