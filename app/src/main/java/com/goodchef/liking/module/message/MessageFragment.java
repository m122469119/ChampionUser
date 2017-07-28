package com.goodchef.liking.module.message;

import android.os.Bundle;

import com.aaron.android.framework.base.widget.refresh.NetworkSwipeRecyclerRefreshPagerLoaderFragment;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.goodchef.liking.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:33
 * version 1.0.0
 */

public class MessageFragment extends NetworkSwipeRecyclerRefreshPagerLoaderFragment<MessageContract.Presenter> implements MessageContract.View {

    private MessageAdapter mMessageAdapter;

    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter() {
        mPresenter = new MessageContract.Presenter();
    }

    @Override
    protected void requestData(int page) {
        getStateView().setState(StateView.State.SUCCESS);
    }

    @Override
    protected void initViews() {
        mMessageAdapter = new MessageAdapter(getContext());
        setRecyclerAdapter(mMessageAdapter);
        List<String> list = new ArrayList<>();
        list.add("系统消息");
        list.add("系统消息");
        list.add("系统消息");
        list.add("系统消息");
        list.add("系统消息");
        updateListView(list);
    }
}
