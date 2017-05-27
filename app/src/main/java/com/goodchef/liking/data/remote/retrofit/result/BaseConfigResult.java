package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.goodchef.liking.data.remote.retrofit.result.data.CityData;
import com.goodchef.liking.data.remote.retrofit.result.data.PatchData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author shaozucheng
 * Time:16/7/8 上午11:06
 */
public class BaseConfigResult extends LikingResult {

    @SerializedName("data")
    private ConfigData mBaseConfigData;

    public ConfigData getBaseConfigData() {
        return mBaseConfigData;
    }

    public void setBaseConfigData(ConfigData baseConfigData) {
        mBaseConfigData = baseConfigData;
    }

    public static class ConfigData extends Data {

        @SerializedName("customer_phone")
        private String customerPhone;
        @SerializedName("business_phone")
        private String businessPhone;
        @SerializedName("count_second")
        private String countSecond;
        @SerializedName("api_version")
        private String apiVersion;
        @SerializedName("open_city")
        private List<String> openCity;
        @SerializedName("citys")
        private List<CityData> cityList;
        @SerializedName("patch")
        private PatchData patchData;
        @SerializedName("agree_url")
        private String agreeUrl;
        @SerializedName("service_url")
        private String serviceUrl;
        @SerializedName("trainer_protocol")
        private String trainerProtocol;


        @SerializedName("update")
        private UpdateData mUpdateData;
        @SerializedName("wechat")
        private String wechat;

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

        public PatchData getPatchData() {
            return patchData;
        }

        public void setPatchData(PatchData patchData) {
            this.patchData = patchData;
        }

        public String getAgreeUrl() {
            return agreeUrl;
        }

        public void setAgreeUrl(String agreeUrl) {
            this.agreeUrl = agreeUrl;
        }

        public String getServiceUrl() {
            return serviceUrl;
        }

        public void setServiceUrl(String serviceUrl) {
            this.serviceUrl = serviceUrl;
        }


        public UpdateData getUpdateData() {
            return mUpdateData;
        }

        public void setUpdateData(UpdateData updateData) {
            mUpdateData = updateData;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public List<String> getOpenCity() {
            return openCity;
        }

        public void setOpenCity(List<String> openCity) {
            this.openCity = openCity;
        }

        public String getTrainerProtocol() {
            return trainerProtocol;
        }

        public void setTrainerProtocol(String trainerProtocol) {
            this.trainerProtocol = trainerProtocol;
        }

        public static class UpdateData extends Data {

            /**
             * update : 0
             * title : 更新提示
             * content : app重磅更新，升级抢先体验
             * url : http://sj.qq.com/myapp/detail.htm?apkName=com.vteam.cook
             */

            @SerializedName("update")
            private int mUpdate;
            @SerializedName("title")
            private String mTitle;
            @SerializedName("content")
            private String mContent;
            @SerializedName("url")
            private String mUrl;
             @SerializedName("lastest_ver")
            private String lastestVer;

            public void setUpdate(int update) {
                this.mUpdate = update;
            }

            public void setTitle(String title) {
                this.mTitle = title;
            }

            public void setContent(String content) {
                this.mContent = content;
            }

            public void setUrl(String url) {
                this.mUrl = url;
            }

            public int getUpdate() {
                return mUpdate;
            }

            public String getTitle() {
                return mTitle;
            }

            public String getContent() {
                return mContent;
            }

            public String getUrl() {
                return mUrl;
            }

            public String getLastestVer() {
                return lastestVer;
            }

            public void setLastestVer(String lastestVer) {
                this.lastestVer = lastestVer;
            }
        }

    }
}