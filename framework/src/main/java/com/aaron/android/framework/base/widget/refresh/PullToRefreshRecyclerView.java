package com.aaron.android.framework.base.widget.refresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.aaron.android.framework.base.widget.pullrefresh.PullToRefreshBase;


/**
 * 支持上拉下拉的RecyclerView
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

    /**
     * 设置RecyclerView的Padding值
     * @param left 左
     * @param top 上
     * @param right 右
     * @param bottom 下
     */
    public void setRefreshViewPadding(int left, int top, int right, int bottom) {
        mRefreshableView.setPadding(left, top, right, bottom);
        mRefreshableView.setClipToPadding(false);
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
        mRefreshableView.setAdapter(adapter);
    }

}
