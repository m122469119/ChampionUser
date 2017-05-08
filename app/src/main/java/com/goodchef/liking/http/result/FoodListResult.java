package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.Data;
import com.goodchef.liking.http.result.data.Food;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/21 下午5:48
 */
public class FoodListResult extends LikingResult {
    @SerializedName("data")
    private FoodData mFoodData;

    public FoodData getFoodData() {
        return mFoodData;
    }

    public void setFoodData(FoodData foodData) {
        mFoodData = foodData;
    }

    public static class FoodData extends Data {
        @SerializedName("list")
        private List<Food> mFoodList;
        @SerializedName("user_city_id")
        private String userCityId;
        @SerializedName("has_more")
        private String hasMore;

        public List<Food> getFoodList() {
            return mFoodList;
        }

        public void setFoodList(List<Food> foodList) {
            mFoodList = foodList;
        }

        public String getUserCityId() {
            return userCityId;
        }

        public void setUserCityId(String userCityId) {
            this.userCityId = userCityId;
        }

        public String getHasMore() {
            return hasMore;
        }

        public void setHasMore(String hasMore) {
            this.hasMore = hasMore;
        }

    }


}
