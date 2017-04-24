package com.aaron.pay.alipay;

import com.aaron.pay.PayListener;

/**
 * Created on 15/10/9.
 *
 * @author aaron.huang
 * @version 1.0.0
 */
public interface OnAliPayListener extends PayListener {

    void confirm();//待确认
}
