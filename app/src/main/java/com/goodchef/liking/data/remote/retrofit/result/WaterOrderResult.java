package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;

/**
 * Created on 2017/6/30
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class WaterOrderResult extends LikingResult {


    /**
     * data : {"order_id":4618831726567780000,"pay_type":0,"wx_prepay_id":"wx2016062311233238f195c9b40149344348","ali_pay_token":""}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean extends Data{

        public static final int WECHATPAY = 0;
        public static final int ALIPAY = 1;

        /**
         * order_id : 4618831726567780000
         * pay_type : 0
         * wx_prepay_id : wx2016062311233238f195c9b40149344348
         * ali_pay_token :
         */
        private long order_id;
        private int pay_type;
        private String wx_prepay_id;
        private String ali_pay_token;

        public long getOrder_id() {
            return order_id;
        }

        public void setOrder_id(long order_id) {
            this.order_id = order_id;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }

        public String getWx_prepay_id() {
            return wx_prepay_id;
        }

        public void setWx_prepay_id(String wx_prepay_id) {
            this.wx_prepay_id = wx_prepay_id;
        }

        public String getAli_pay_token() {
            return ali_pay_token;
        }

        public void setAli_pay_token(String ali_pay_token) {
            this.ali_pay_token = ali_pay_token;
        }
    }
}
