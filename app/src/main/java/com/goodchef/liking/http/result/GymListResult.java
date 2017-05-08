package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/26 下午9:20
 */
public class GymListResult extends LikingResult {
    @SerializedName("data")
    private GymData mGymData;

    public GymData getGymData() {
        return mGymData;
    }

    public void setGymData(GymData gymData) {
        mGymData = gymData;
    }

    public static class GymData extends Data {
        @SerializedName("gym_list")
        private List<Shop> gymList;

        public List<Shop> getGymList() {
            return gymList;
        }

        public void setGymList(List<Shop> gymList) {
            this.gymList = gymList;
        }

        public static class Shop extends Data {
            @SerializedName("gym_id")
            private String gymId;
            @SerializedName("name")
            private String name;
            @SerializedName("address")
            private String address;
            @SerializedName("img")
            private String img;
            @SerializedName("stock_limit")
            private int stockLimit;

            private boolean isSelect ;

            public String getGymId() {
                return gymId;
            }

            public void setGymId(String gymId) {
                this.gymId = gymId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getStockLimit() {
                return stockLimit;
            }

            public void setStockLimit(int stockLimit) {
                this.stockLimit = stockLimit;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }
        }
    }
}
