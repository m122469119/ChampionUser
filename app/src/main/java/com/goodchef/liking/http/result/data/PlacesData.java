package com.goodchef.liking.http.result.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/8/23 上午10:55
 */
public class PlacesData extends BaseData implements Parcelable {

    /**
     * gym_id : 1
     * address : Liking Fit（复兴店）-黄浦区马当路388号soho复兴广场B2-02
     */

    @SerializedName("gym_id")
    private String gymId;
    @SerializedName("address")
    private String address;
    private boolean isSelect;

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.gymId);
        dest.writeString(this.address);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    public PlacesData() {
    }

    protected PlacesData(Parcel in) {
        this.gymId = in.readString();
        this.address = in.readString();
        this.isSelect = in.readByte() != 0;
    }

    public static final Parcelable.Creator<PlacesData> CREATOR = new Parcelable.Creator<PlacesData>() {
        @Override
        public PlacesData createFromParcel(Parcel source) {
            return new PlacesData(source);
        }

        @Override
        public PlacesData[] newArray(int size) {
            return new PlacesData[size];
        }
    };
}
