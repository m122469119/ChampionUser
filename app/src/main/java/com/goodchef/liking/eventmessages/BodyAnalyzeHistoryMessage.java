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

    public BodyAnalyzeHistoryMessage(String column) {
        this.column = column;
    }

    public String getColumn() {
        return column;
    }
}
