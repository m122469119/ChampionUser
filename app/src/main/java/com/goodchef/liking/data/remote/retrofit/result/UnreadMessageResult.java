package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午2:09
 * version 1.0.0
 */

public class UnreadMessageResult extends LikingResult {

    @SerializedName("data")
    private UnreadMsgData mData;

    public UnreadMsgData getData() {
        return mData;
    }

    public void setData(UnreadMsgData data) {
        mData = data;
    }

    public class UnreadMsgData extends Data{
        /**
         * has_unread_msg : 0
         */

        @SerializedName("has_unread_msg")
        private int hasUnreadMsg;

        public int getHasUnreadMsg() {
            return hasUnreadMsg;
        }

        public void setHasUnreadMsg(int hasUnreadMsg) {
            this.hasUnreadMsg = hasUnreadMsg;
        }
    }
}
