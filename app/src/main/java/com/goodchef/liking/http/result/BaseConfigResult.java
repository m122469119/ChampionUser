package com.goodchef.liking.http.result;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.aaron.android.codelibrary.http.result.BaseResult;
import com.goodchef.liking.http.result.data.CityData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/8 上午11:06
 */
public class BaseConfigResult extends BaseResult {

    @SerializedName("data")
    private BaseConfigData mBaseConfigData;

    public BaseConfigData getBaseConfigData() {
        return mBaseConfigData;
    }

    public void setBaseConfigData(BaseConfigData baseConfigData) {
        mBaseConfigData = baseConfigData;
    }

    public static class BaseConfigData extends BaseData {

        @SerializedName("customer_phone")
        private String customerPhone;
        @SerializedName("business_phone")
        private String businessPhone;
        @SerializedName("count_second")
        private String countSecond;
        @SerializedName("api_version")
        private String apiVersion;

        @SerializedName("citys")
        private List<CityData> cityList;

        public String getCustomerPhone() {
            return customerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
        }

        public String getBusinessPhone() {
            return businessPhone;
        }

        public void setBusinessPhone(String businessPhone) {
            this.businessPhone = businessPhone;
        }

        public String getCountSecond() {
            return countSecond;
        }

        public void setCountSecond(String countSecond) {
            this.countSecond = countSecond;
        }

        public String getApiVersion() {
            return apiVersion;
        }

        public void setApiVersion(String apiVersion) {
            this.apiVersion = apiVersion;
        }

        public List<CityData> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityData> cityList) {
            this.cityList = cityList;
        }


    }
}
