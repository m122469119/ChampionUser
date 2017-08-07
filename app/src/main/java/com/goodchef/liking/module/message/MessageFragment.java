package com.goodchef.liking.module.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.OnRecycleViewItemClickListener;
import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.PullMode;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.adapter.MessageAdapter;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.MessageResult;
import com.goodchef.liking.eventmessages.LoginFinishMessage;
import com.goodchef.liking.module.login.LoginActivity;

import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:33
 * version 1.0.0
 */

public class MessageFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment<MessageContract.Presenter> implements MessageContract.View {

    private MessageAdapter mMessageAdapter;
    private List<MessageResult.MessageData.Message> messageList;
    private String msgId = "";

    public static MessageFragment newInstance(String msgId) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(MessageActivity.MSG_ID, msgId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void setPresenter() {
        mPresenter = new MessageContract.Presenter();
    }

    @Override
    protected void requestData(int page) {
        if (!LikingPreference.isLogin()) {
            setNoDataView();
        } else {
            mPresenter.getMessageList(page);
        }
    }

    @Override
    protected void initViews() {
        setMessageRead();
        setNoDataView();
        setPullMode(PullMode.PULL_BOTH);
        mMessageAdapter = new MessageAdapter(getContext());
        setRecyclerAdapter(mMessageAdapter);
        mMessageAdapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TextView tv = (TextView) view.findViewById(R.id.message_content);
                if (tv != null) {
                    MessageResult.MessageData.Message object = (MessageResult.MessageData.Message) tv.getTag();
                    doReadClick(object, position);
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                return false;
            }
        });
    }

    /**
     * 设置通知过来的消息显示已读
     */
    private void setMessageRead() {
        msgId = getArguments().getString(MessageActivity.MSG_ID);
        if (!StringUtils.isEmpty(msgId) && mPresenter != null) {
            mPresenter.setReadMessage(msgId);
        }
    }

    private void setNoDataView() {
        View noDataView = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noDataView.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noDataView.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noDataView.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.no_message_data);
        noDataText.setText(R.string.no_message_data);
        refreshView.setVisibility(View.INVISIBLE);
        getStateView().setNodataView(noDataView);
    }

    private void setNoLoginView() {
        View noLogin = LayoutInflater.from(getActivity()).inflate(R.layout.view_common_no_data, null, false);
        ImageView noDataImageView = (ImageView) noLogin.findViewById(R.id.imageview_no_data);
        TextView noDataText = (TextView) noLogin.findViewById(R.id.textview_no_data);
        TextView refreshView = (TextView) noLogin.findViewById(R.id.textview_refresh);
        noDataImageView.setImageResource(R.drawable.icon_head_default_image);
        noDataText.setText(R.string.state_view_no_login_text);
        refreshView.setText(R.string.login_btn_text);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.class);
            }
        });
        getStateView().setNoLoginView(noLogin);
    }


    private void doReadClick(MessageResult.MessageData.Message object, int position) {
        if (object != null && !StringUtils.isEmpty(object.getMsgId()) && object.getIsRead() == 0 && mPresenter != null) {
            mPresenter.setReadMessage(object.getMsgId());
            object.setIsRead(1);
            mMessageAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void updateMessageList(MessageResult.MessageData messageData) {
        messageList = messageData.getMessageList();
        if (!StringUtils.isEmpty(msgId)) {
            for (int i = 0; i < messageList.size(); i++) {
                if (msgId.equals(messageList.get(i).getMsgId())) {
                    messageList.get(i).setIsRead(1);
                }
            }
        }
        updateListView(messageList);
    }

    @Override
    protected boolean isEventTarget() {
        return true;
    }

    public void onEvent(LoginFinishMessage message) {
        if (message != null) {
            requestData(1);
        }
    }

    @Override
    public void setReadMessage() {

    }
}
