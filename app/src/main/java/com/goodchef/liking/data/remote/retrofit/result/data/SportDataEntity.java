package com.goodchef.liking.data.remote.retrofit.result.data;

import com.aaron.http.code.result.Data;

/**
 * @Author SanFen
 * @Email sanfenruxi1@163.com
 * @Date 2017/9/2
 * @Version 1.0
 */
public class SportDataEntity extends Data{
    private Long timstamp;
    private String title;
    private String content;
    private String percentage;
    private String percentageText;
    private boolean isChecked;

    public SportDataEntity() {
    }

    public SportDataEntity(Long timstamp, String title, String content, String percentage, String percentageText, boolean isChecked) {
        this.timstamp = timstamp;
        this.title = title;
        this.content = content;
        this.percentage = percentage;
        this.percentageText = percentageText;
        this.isChecked = isChecked;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getPercentageText() {
        return percentageText;
    }

    public void setPercentageText(String percentageText) {
        this.percentageText = percentageText;
    }

    public Long getTimstamp() {
        return timstamp;
    }

    public void setTimstamp(Long timstamp) {
        this.timstamp = timstamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
