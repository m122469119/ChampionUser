package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;

import java.util.List;

/**
 * Created on 2017/3/17
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsPersonResult extends BaseResult {


    /**
     * data : {"coupon_list":[{"coupon_code":"582e64d7bcf2a","title":"liking coupon","amount":"500.00","coupon_status":"0","use_desc":"满10可用,适用于团体课","valid_date":"2016.11.18-2017.05.17"},{"coupon_code":"582e63bd647d0","title":"liking coupon","amount":"100.00","coupon_status":"0","use_desc":"满10可用,适用于私教课","valid_date":"2016.11.18-2017.05.17"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean extends BaseData{
        private List<CouponListBean> coupon_list;

        public List<CouponListBean> getCoupon_list() {
            return coupon_list;
        }

        public void setCoupon_list(List<CouponListBean> coupon_list) {
            this.coupon_list = coupon_list;
        }

        public static class CouponListBean extends BaseData{
            /**
             * coupon_code : 582e64d7bcf2a
             * title : liking coupon
             * amount : 500.00
             * coupon_status : 0
             * use_desc : 满10可用,适用于团体课
             * valid_date : 2016.11.18-2017.05.17
             */

            //优惠券码，用于后面获取详情页参数
            private String coupon_code;
            //优惠券标题
            private String title;
            //面值
            private String amount;
            //优惠券状态: 0 可适用, 1 已使用 , 2 已过期
            private String coupon_status;
            //优惠券使用描述
            private String use_desc;
            //优惠券有效期
            private String valid_date;

            public String getCoupon_code() {
                return coupon_code;
            }

            public void setCoupon_code(String coupon_code) {
                this.coupon_code = coupon_code;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getCoupon_status() {
                return coupon_status;
            }

            public void setCoupon_status(String coupon_status) {
                this.coupon_status = coupon_status;
            }

            public String getUse_desc() {
                return use_desc;
            }

            public void setUse_desc(String use_desc) {
                this.use_desc = use_desc;
            }

            public String getValid_date() {
                return valid_date;
            }

            public void setValid_date(String valid_date) {
                this.valid_date = valid_date;
            }
        }
    }
}
