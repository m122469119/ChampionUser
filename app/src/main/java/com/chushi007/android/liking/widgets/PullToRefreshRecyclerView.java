package com.chushi007.android.liking.widgets;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.aaron.android.thirdparty.widget.pullrefresh.PullToRefreshBase;


/**
 * Created by Lennon on 16/3/24.
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {
    public PullToRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshRecyclerView(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return recyclerView;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        RecyclerView.Adapter adapter = mRefreshableView.getAdapter();
        if (null == adapter) {
            return true;
        } else {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRefreshableView.getLayoutManager();
            int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
            int totalItemCount = layoutManager.getItemCount();
            if (lastVisibleItem == (totalItemCount - 1)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean isReadyForPullStart() {
        RecyclerView.Adapter adapter = mRefreshableView.getAdapter();
        if (null == adapter) {
            return true;
        } else {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRefreshableView.getLayoutManager();
            int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItem == 0) {
                return true;
            }
        }
        return false;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        ((RecyclerView) mRefreshableView).setAdapter(adapter);
    }


}
