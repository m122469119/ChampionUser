package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.goodchef.liking.http.result.data.OrderCardData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/1 上午10:08
 */
public class OrderCardListResult extends BaseResult{
    @SerializedName("data")
    private OrderCardListData data;

    public OrderCardListData getData() {
        return data;
    }

    public void setData(OrderCardListData data) {
        this.data = data;
    }

    public static class OrderCardListData extends BaseData{
        @SerializedName("list")
        private List<OrderCardData>  mOrderCardList;

        public List<OrderCardData> getOrderCardList() {
            return mOrderCardList;
        }

        public void setOrderCardList(List<OrderCardData> orderCardList) {
            mOrderCardList = orderCardList;
        }
    }
}
