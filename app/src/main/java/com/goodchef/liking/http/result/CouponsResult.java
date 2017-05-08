package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.Data;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 下午2:00
 */
public class CouponsResult extends LikingResult {

    /**
     * data : {"has_more":0,"coupon_list":[{"coupon_code":"58be571b9239a","title":"liking coupon","amount":"8000.00","can_use":"0","use_desc":"满10可用,适用于私教课","valid_date":"2017.03.07-2017.09.03","not_use_desc":"适用于私教课"}]}
     */

    private CouponData data;

    public CouponData getData() {
        return data;
    }

    public void setData(CouponData data) {
        this.data = data;
    }

    public static class CouponData extends Data {
        /**
         * has_more : 0
         * coupon_list : [{"coupon_code":"58be571b9239a","title":"liking coupon","amount":"8000.00","can_use":"0","use_desc":"满10可用,适用于私教课","valid_date":"2017.03.07-2017.09.03","not_use_desc":"适用于私教课"}]
         */

        private int has_more;
        private List<Coupon> coupon_list;

        public int getHas_more() {
            return has_more;
        }

        public void setHas_more(int has_more) {
            this.has_more = has_more;
        }

        public List<Coupon> getCoupon_list() {
            return coupon_list;
        }

        public void setCoupon_list(List<Coupon> coupon_list) {
            this.coupon_list = coupon_list;
        }

        public static class Coupon extends Data {
            /**
             * coupon_code : 58be571b9239a
             * title : liking coupon
             * amount : 8000.00
             * can_use : 0
             * use_desc : 满10可用,适用于私教课
             * valid_date : 2017.03.07-2017.09.03
             * not_use_desc : 适用于私教课
             */

            private String coupon_code;
            private String title;
            private String amount;
            private String can_use;
            private String use_desc;
            private String valid_date;
            private String not_use_desc;


            private boolean select;

            public boolean isSelect() {
                return select;
            }

            public void setSelect(boolean select) {
                this.select = select;
            }

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

            public String getCan_use() {
                return can_use;
            }

            public void setCan_use(String can_use) {
                this.can_use = can_use;
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

            public String getNot_use_desc() {
                return not_use_desc;
            }

            public void setNot_use_desc(String not_use_desc) {
                this.not_use_desc = not_use_desc;
            }
        }
    }
}
