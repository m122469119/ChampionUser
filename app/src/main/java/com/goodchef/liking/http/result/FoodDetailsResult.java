package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/22 上午11:21
 */
public class FoodDetailsResult extends BaseResult {
    @SerializedName("data")
    private FoodDetailsData data;

    public FoodDetailsData getData() {
        return data;
    }

    public void setData(FoodDetailsData data) {
        this.data = data;
    }

    public static class FoodDetailsData extends BaseData {
        @SerializedName("goods_id")
        private String goodsId;
        @SerializedName("goods_name")
        private String goodsName;
        @SerializedName("price")
        private String price;
        @SerializedName("goods_desc")
        private String goodsDesc;
        @SerializedName("calorie")
        private int calorie;
        @SerializedName("proteide")
        private int proteide;
        @SerializedName("carbohydrate")
        private int carbohydrate;
        @SerializedName("axunge")
        private int axunge;
        @SerializedName("left_num")
        private int leftNum;
        @SerializedName("all_eat")
        private String allEat;
        @SerializedName("imgs")
        private List<String> imgs;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getGoodsDesc() {
            return goodsDesc;
        }

        public void setGoodsDesc(String goodsDesc) {
            this.goodsDesc = goodsDesc;
        }

        public int getCalorie() {
            return calorie;
        }

        public void setCalorie(int calorie) {
            this.calorie = calorie;
        }

        public int getProteide() {
            return proteide;
        }

        public void setProteide(int proteide) {
            this.proteide = proteide;
        }

        public int getCarbohydrate() {
            return carbohydrate;
        }

        public void setCarbohydrate(int carbohydrate) {
            this.carbohydrate = carbohydrate;
        }

        public int getAxunge() {
            return axunge;
        }

        public void setAxunge(int axunge) {
            this.axunge = axunge;
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

        public List<String> getImgs() {
            return imgs;
        }

        public void setImgs(List<String> imgs) {
            this.imgs = imgs;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }
}
