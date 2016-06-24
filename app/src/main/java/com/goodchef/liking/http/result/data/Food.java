package com.goodchef.liking.http.result.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/6/24 上午10:04
 */
public class Food extends BaseData implements Parcelable {

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

    private int selectedOrderNum;
    private int restStock;

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


    public int getSelectedOrderNum() {
        return selectedOrderNum;
    }

    public void setSelectedOrderNum(int selectedOrderNum) {
        this.selectedOrderNum = selectedOrderNum;
    }

    public int getRestStock() {
        return restStock;
    }

    public void setRestStock(int restStock) {
        this.restStock = restStock;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.goodsId);
        dest.writeString(this.goodsName);
        dest.writeString(this.coverImg);
        dest.writeString(this.price);
        dest.writeInt(this.leftNum);
        dest.writeString(this.allEat);
        dest.writeStringList(this.tags);
        dest.writeInt(this.selectedOrderNum);
        dest.writeInt(this.restStock);
    }

    public Food() {
    }

    protected Food(Parcel in) {
        this.goodsId = in.readString();
        this.goodsName = in.readString();
        this.coverImg = in.readString();
        this.price = in.readString();
        this.leftNum = in.readInt();
        this.allEat = in.readString();
        this.tags = in.createStringArrayList();
        this.selectedOrderNum = in.readInt();
        this.restStock = in.readInt();
    }

    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel source) {
            return new Food(source);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };
}
