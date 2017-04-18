package com.goodchef.liking.widgets.indexBar.bean;


import com.goodchef.liking.widgets.indexBar.suspension.ISuspensionInterface;

/**
 * 说明:索引类的标志位的实体基类
 * Author : shaozucheng
 * Time: 上午10:58
 * version 1.0.0
 */

public class BaseIndexBean implements ISuspensionInterface {

    private String baseIndexTag;//所属的分类（城市的汉语拼音首字母）

    public String getBaseIndexTag() {
        return baseIndexTag;
    }

    public BaseIndexBean setBaseIndexTag(String baseIndexTag) {
        this.baseIndexTag = baseIndexTag;
        return this;
    }

    @Override
    public String getSuspensionTag() {
        return baseIndexTag;
    }

    @Override
    public boolean isShowSuspension() {
        return true;
    }
}
