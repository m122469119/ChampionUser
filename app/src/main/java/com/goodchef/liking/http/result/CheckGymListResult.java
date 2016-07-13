package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/12 下午2:11
 */
public class CheckGymListResult extends BaseResult {

    @SerializedName("data")
    private CheckGymData data;

    public CheckGymData getData() {
        return data;
    }

    public void setData(CheckGymData data) {
        this.data = data;
    }

    public class CheckGymData extends BaseData {
        @SerializedName("all_gym")
        private List<CheckGym> allGymList;

        public List<CheckGym> getAllGymList() {
            return allGymList;
        }

        public void setAllGymList(List<CheckGym> allGymList) {
            this.allGymList = allGymList;
        }

        public class CheckGym extends BaseData {

            /**
             * gym_id : 1
             * gym_name : 复兴SOHO广场
             * gym_address : 马当路388号复兴SOHO广场B2-02
             * longitude : 121.474977000
             * latitude : 31.214926000
             * img :
             */

            @SerializedName("gym_id")
            private int gymId;
            @SerializedName("gym_name")
            private String gymName;
            @SerializedName("gym_address")
            private String gymAddress;
            @SerializedName("longitude")
            private double longitude;
            @SerializedName("latitude")
            private double latitude;
            @SerializedName("img")
            private String img;

            private boolean isSelect;

            public int getGymId() {
                return gymId;
            }

            public void setGymId(int gymId) {
                this.gymId = gymId;
            }

            public String getGymName() {
                return gymName;
            }

            public void setGymName(String gymName) {
                this.gymName = gymName;
            }

            public String getGymAddress() {
                return gymAddress;
            }

            public void setGymAddress(String gymAddress) {
                this.gymAddress = gymAddress;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
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