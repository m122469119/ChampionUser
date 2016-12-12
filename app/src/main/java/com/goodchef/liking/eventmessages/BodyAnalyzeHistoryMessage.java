package com.goodchef.liking.eventmessages;

import com.aaron.android.framework.base.eventbus.BaseMessage;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午7:11
 * version 1.0.0
 */
public class BodyAnalyzeHistoryMessage extends BaseMessage {
    private String column;
    private String unit;
    private String modules;

    public BodyAnalyzeHistoryMessage(String column, String unit, String modules) {
        this.column = column;
        this.unit = unit;
        this.modules = modules;
    }

    public String getColumn() {
        return column;
    }

    public String getUnit() {
        return unit;
    }

    public String getModules() {
        return modules;
    }
}
