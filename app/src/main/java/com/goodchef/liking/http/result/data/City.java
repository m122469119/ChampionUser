package com.goodchef.liking.http.result.data;

import com.aaron.android.codelibrary.http.result.BaseData;
import com.github.promeg.pinyinhelper.Pinyin;
import com.goodchef.liking.widgets.indexBar.bean.BaseIndexPinyinBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:38
 * version 1.0.0
 */

public class City extends BaseData{

    @SerializedName("regions")
    private List<RegionsData> regions;

    public List<RegionsData> getRegions() {
        return regions;
    }

    public void setRegions(List<RegionsData> regions) {
        this.regions = regions;
    }

    public static class RegionsData {
        /**
         * province_id : 110000
         * province_name : 北京
         * cities : [{"city_id":"110100","city_code":"010","city_name":"北京","districts":[{"district_id":"110101","district_name":"东城"},{"district_id":"110102","district_name":"西城"},{"district_id":"110105","district_name":"朝阳"},{"district_id":"110106","district_name":"丰台"},{"district_id":"110107","district_name":"石景山"},{"district_id":"110108","district_name":"海淀"},{"district_id":"110109","district_name":"门头沟"},{"district_id":"110111","district_name":"房山"},{"district_id":"110112","district_name":"通州"},{"district_id":"110113","district_name":"顺义"},{"district_id":"110114","district_name":"昌平"},{"district_id":"110115","district_name":"大兴"},{"district_id":"110116","district_name":"怀柔"},{"district_id":"110117","district_name":"平谷"},{"district_id":"110118","district_name":"密云"},{"district_id":"110119","district_name":"延庆"}]}]
         */

        @SerializedName("province_id")
        private String provinceId;
        @SerializedName("province_name")
        private String provinceName;
        @SerializedName("cities")
        private List<CitiesData> cities;

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public List<CitiesData> getCities() {
            return cities;
        }

        public void setCities(List<CitiesData> cities) {
            this.cities = cities;
        }

        public static class CitiesData extends BaseIndexPinyinBean {
            /**
             * city_id : 110100
             * city_code : 010
             * city_name : 北京
             * districts : [{"district_id":"110101","district_name":"东城"},{"district_id":"110102","district_name":"西城"},{"district_id":"110105","district_name":"朝阳"},{"district_id":"110106","district_name":"丰台"},{"district_id":"110107","district_name":"石景山"},{"district_id":"110108","district_name":"海淀"},{"district_id":"110109","district_name":"门头沟"},{"district_id":"110111","district_name":"房山"},{"district_id":"110112","district_name":"通州"},{"district_id":"110113","district_name":"顺义"},{"district_id":"110114","district_name":"昌平"},{"district_id":"110115","district_name":"大兴"},{"district_id":"110116","district_name":"怀柔"},{"district_id":"110117","district_name":"平谷"},{"district_id":"110118","district_name":"密云"},{"district_id":"110119","district_name":"延庆"}]
             */

            @SerializedName("city_id")
            private String cityId;
            @SerializedName("city_code")
            private String cityCode;
            @SerializedName("city_name")
            private String cityName;
            @SerializedName("districts")
            private List<DistrictsData> districts;

            private String pinyin;

            public String getCityId() {
                return cityId;
            }

            public void setCityId(String cityId) {
                this.cityId = cityId;
            }

            public String getCityCode() {
                return cityCode;
            }

            public void setCityCode(String cityCode) {
                this.cityCode = cityCode;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
                pinyin = Pinyin.toPinyin(cityName, ",");
            }

            public String getPinyin(){
                return pinyin;
            }

            public List<DistrictsData> getDistricts() {
                return districts;
            }

            public void setDistricts(List<DistrictsData> districts) {
                this.districts = districts;
            }

            @Override
            public String getTarget() {
                return cityName;
            }

            public static class DistrictsData {
                /**
                 * district_id : 110101
                 * district_name : 东城
                 */

                @SerializedName("district_id")
                private String districtId;
                @SerializedName("district_name")
                private String districtName;

                private String pinyin;

                public String getDistrictId() {
                    return districtId;
                }

                public void setDistrictId(String districtId) {
                    this.districtId = districtId;
                }

                public String getDistrictName() {
                    return districtName;
                }

                public void setDistrictName(String districtName) {
                    this.districtName = districtName;
                    pinyin = Pinyin.toPinyin(districtName, ",");
                }

                public String getPinyin(){
                    return pinyin;
                }
            }
        }
    }
}
