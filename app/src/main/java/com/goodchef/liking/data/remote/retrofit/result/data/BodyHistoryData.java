package com.goodchef.liking.data.remote.retrofit.result.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:15
 * version 1.0.0
 */

public class BodyHistoryData implements Parcelable {
    @SerializedName("value")
    private String mValue;
    @SerializedName("body_time")
    private String mBodyTime;

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public String getBodyTime() {
        return mBodyTime;
    }

    public void setBodyTime(String bodyTime) {
        mBodyTime = bodyTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mValue);
        dest.writeString(this.mBodyTime);
    }

    public BodyHistoryData() {
    }

    protected BodyHistoryData(Parcel in) {
        this.mValue = in.readString();
        this.mBodyTime = in.readString();
    }

    public static final Parcelable.Creator<BodyHistoryData> CREATOR = new Parcelable.Creator<BodyHistoryData>() {
        @Override
        public BodyHistoryData createFromParcel(Parcel source) {
            return new BodyHistoryData(source);
        }

        @Override
        public BodyHistoryData[] newArray(int size) {
            return new BodyHistoryData[size];
        }
    };
}
