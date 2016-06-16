package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/16 下午2:00
 */
public class CouponsResult extends BaseResult {
    @SerializedName("data")
    private CouponData mCouponData;

    public CouponData getCouponData() {
        return mCouponData;
    }

    public void setCouponData(CouponData couponData) {
        mCouponData = couponData;
    }

    public static class CouponData extends BaseData {
        @SerializedName("has_more")
        private int hasMore;
        @SerializedName("coupon_list")
        private List<Coupon> mCouponList;

        public int getHasMore() {
            return hasMore;
        }

        public void setHasMore(int hasMore) {
            this.hasMore = hasMore;
        }

        public List<Coupon> getCouponList() {
            return mCouponList;
        }

        public void setCouponList(List<Coupon> couponList) {
            mCouponList = couponList;
        }

        public static class Coupon extends BaseData {
            @SerializedName("startTime")
            private String startTime;
            @SerializedName("endTime")
            private String endTime;
            @SerializedName("title")
            private String title;
            @SerializedName("couponStatus")
            private String couponStatus;
            @SerializedName("couponType")
            private String couponType;
            @SerializedName("minAmount")
            private String minAmount;
            @SerializedName("amount")
            private String amount;

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCouponStatus() {
                return couponStatus;
            }

            public void setCouponStatus(String couponStatus) {
                this.couponStatus = couponStatus;
            }

            public String getCouponType() {
                return couponType;
            }

            public void setCouponType(String couponType) {
                this.couponType = couponType;
            }

            public String getMinAmount() {
                return minAmount;
            }

            public void setMinAmount(String minAmount) {
                this.minAmount = minAmount;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }
        }
    }

}
