package com.goodchef.liking.http.result.data;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午10:17
 * version 1.0.0
 */

public class BodyData extends Data {


    /**
     * value : 0.5
     * evaluate : 偏低
     * chinese_name : 左上肢节段脂肪
     * content : 15.5%
     * english_name :
     * unit : kg
     */

    @SerializedName("value")
    private String value;
    @SerializedName("evaluate")
    private String evaluate;
    @SerializedName("chinese_name")
    private String chineseName;
    @SerializedName("content")
    private String content;
    @SerializedName("english_name")
    private String englishName;
    @SerializedName("unit")
    private String unit;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
