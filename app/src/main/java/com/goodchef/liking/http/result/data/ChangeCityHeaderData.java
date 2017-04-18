package com.goodchef.liking.http.result.data;

import com.goodchef.liking.widgets.indexBar.bean.BaseIndexPinyinBean;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午10:39
 * version 1.0.0
 */

public class ChangeCityHeaderData extends BaseIndexPinyinBean {

    private List<City.RegionsData.CitiesData> cityList;
    //悬停ItemDecoration显示的Tag
    private String suspensionTag;

    public ChangeCityHeaderData() {

    }

    public ChangeCityHeaderData(List<City.RegionsData.CitiesData> cityList, String suspensionTag, String indexBarTag) {
        this.cityList = cityList;
        this.suspensionTag = suspensionTag;
        this.setBaseIndexTag(indexBarTag);
    }

    public List<City.RegionsData.CitiesData> getCityList() {
        return cityList;
    }

    public void setCityList(List<City.RegionsData.CitiesData> cityList) {
        this.cityList = cityList;
    }

    public void setSuspensionTag(String suspensionTag) {
        this.suspensionTag = suspensionTag;
    }

    @Override
    public String getTarget() {
        return null;
    }

    @Override
    public boolean isNeedToPinyin() {
        return false;
    }

    @Override
    public String getSuspensionTag() {
        return suspensionTag;
    }

}
