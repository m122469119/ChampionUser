package com.goodchef.liking.data.remote.retrofit.result;

import com.aaron.http.code.result.Data;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 上午11:02
 * version 1.0.0
 */

public class MessageResult extends LikingResult {

    @SerializedName("data")
    public MessageData mData;

    public MessageData getData() {
        return mData;
    }

    public void setData(MessageData data) {
        mData = data;
    }

    public class MessageData extends Data {
        @SerializedName("has_more")
        private String hasMore;

        @SerializedName("list")
        private List<Message> mMessageList;

        public String getHasMore() {
            return hasMore;
        }

        public void setHasMore(String hasMore) {
            this.hasMore = hasMore;
        }

        public List<Message> getMessageList() {
            return mMessageList;
        }

        public void setMessageList(List<Message> messageList) {
            mMessageList = messageList;
        }

        public class Message extends Data {

            /**
             * msg_id : 729
             * content : 请安排好自己的行程，准时开练
             * is_read : 0
             * create_time : 2017-02-05 09:00:01
             * type_desc : 业务消息
             */

            @SerializedName("msg_id")
            private String msgId;
            @SerializedName("content")
            private String content;
            @SerializedName("is_read")
            private int isRead;
            @SerializedName("create_time")
            private String createTime;
            @SerializedName("type_desc")
            private String typeDesc;

            public String getMsgId() {
                return msgId;
            }

            public void setMsgId(String msgId) {
                this.msgId = msgId;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getIsRead() {
                return isRead;
            }

            public void setIsRead(int isRead) {
                this.isRead = isRead;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getTypeDesc() {
                return typeDesc;
            }

            public void setTypeDesc(String typeDesc) {
                this.typeDesc = typeDesc;
            }
        }
    }
}
