package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/21 下午5:48
 */
public class FoodListResult extends BaseResult {
    @SerializedName("data")
    private FoodData mFoodData;

    public FoodData getFoodData() {
        return mFoodData;
    }

    public void setFoodData(FoodData foodData) {
        mFoodData = foodData;
    }

    public static class FoodData extends BaseData {
        @SerializedName("list")
        private List<Food> mFoodList;

        public List<Food> getFoodList() {
            return mFoodList;
        }

        public void setFoodList(List<Food> foodList) {
            mFoodList = foodList;
        }

        public static class Food extends BaseData {
            @SerializedName("goods_id")
            private String goodsId;
            @SerializedName("goods_name")
            private String goodsName;
            @SerializedName("cover_img")
            private String coverImg;
            @SerializedName("price")
            private String price;
            @SerializedName("left_num")
            private int leftNum;
            @SerializedName("all_eat")
            private String allEat;
            @SerializedName("tags")
            private List<String> tags;

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getCoverImg() {
                return coverImg;
            }

            public void setCoverImg(String coverImg) {
                this.coverImg = coverImg;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public int getLeftNum() {
                return leftNum;
            }

            public void setLeftNum(int leftNum) {
                this.leftNum = leftNum;
            }

            public String getAllEat() {
                return allEat;
            }

            public void setAllEat(String allEat) {
                this.allEat = allEat;
            }

            public List<String> getTags() {
                return tags;
            }

            public void setTags(List<String> tags) {
                this.tags = tags;
            }
        }
    }


}
