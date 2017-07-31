package com.goodchef.liking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.MessageResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午5:11
 * version 1.0.0
 */

public class MessageAdapter extends BaseRecycleViewAdapter<MessageAdapter.MessageViewHolder, MessageResult.MessageData.Message> {


    public MessageAdapter(Context context) {
        super(context);
    }

    @Override
    protected MessageViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.viewholder_message, parent, false);
        return new MessageViewHolder(view);
    }

    class MessageViewHolder extends BaseRecycleViewHolder<MessageResult.MessageData.Message> {
        @BindView(R.id.message_read_tag)
        ImageView mMessageReadTag;
        @BindView(R.id.message_name)
        TextView mMessageName;
        @BindView(R.id.message_name_time)
        TextView mMessageNameTime;
        @BindView(R.id.message_content)
        TextView mMessageContent;

        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindViews(MessageResult.MessageData.Message object) {
            int read = object.getIsRead();
            mMessageName.setText(object.getTypeDesc());
            mMessageContent.setText(object.getContent());
            mMessageNameTime.setText(object.getCreateTime());
            messageHasRead(read);
            mMessageContent.setTag(object);
        }

        private void messageHasRead(int read) {
            if (read == 1) { //已读
                mMessageReadTag.setVisibility(View.GONE);
                mMessageName.setTextColor(ResourceUtils.getColor(R.color.c888b99));
                mMessageNameTime.setTextColor(ResourceUtils.getColor(R.color.c888b99));
                mMessageContent.setTextColor(ResourceUtils.getColor(R.color.c888b99));
            } else {// 未读
                mMessageReadTag.setVisibility(View.VISIBLE);
                mMessageName.setTextColor(ResourceUtils.getColor(R.color.liking_lesson_text));
                mMessageNameTime.setTextColor(ResourceUtils.getColor(R.color.liking_lesson_text));
                mMessageContent.setTextColor(ResourceUtils.getColor(R.color.liking_lesson_text));
            }
        }
    }
}
