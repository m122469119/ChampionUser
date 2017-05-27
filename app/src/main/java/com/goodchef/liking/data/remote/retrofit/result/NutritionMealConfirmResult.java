package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/23 下午8:49
 */
public class NutritionMealConfirmResult extends LikingResult {
    @SerializedName("data")
    private NutritionMealConfirmData mConfirmData;

    public NutritionMealConfirmData getConfirmData() {
        return mConfirmData;
    }

    public void setConfirmData(NutritionMealConfirmData confirmData) {
        mConfirmData = confirmData;
    }

    public static class NutritionMealConfirmData extends Data {
        @SerializedName("totalAmount")
        private String totalAmount;
        @SerializedName("selectDate")
        private List<String> selectDate;
        @SerializedName("store")
        private Store mStore;

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public List<String> getSelectDate() {
            return selectDate;
        }

        public void setSelectDate(List<String> selectDate) {
            this.selectDate = selectDate;
        }

        public Store getStore() {
            return mStore;
        }

        public void setStore(Store store) {
            mStore = store;
        }

        public static class Store extends Data {
            @SerializedName("gym_id")
            private String gymId;
            @SerializedName("name")
            private String name;
            @SerializedName("address")
            private String address;

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
        }


    }


}