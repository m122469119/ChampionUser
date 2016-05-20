package com.aaron.android.framework.base.widget.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.android.codelibrary.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 15/7/30.
 *
 * @param <T>  extends BaseData
 * @param <VH> extends BaseRecycleViewHolder<T>
 * @author ran.huang
 * @version 1.0.0
 */
public abstract class BaseRecycleViewAdapter<VH extends BaseRecycleViewHolder<T>, T>
        extends RecyclerView.Adapter<VH> implements View.OnClickListener, View.OnLongClickListener {

    private List<T> mDataList = new ArrayList<>();

    private Context mContext;

    private OnRecycleViewItemClickListener mOnRecycleViewItemClickListener;

    protected BaseRecycleViewAdapter(Context context) {
        mContext = context;
    }

    /**
     * @return 获取上下文资源
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * @return 获取Adapter Dataliste数据集
     */
    public List<T> getDataList() {
        return mDataList;
    }

    /**
     * 设置RecycleView item事件监听回调
     *
     * @param onRecycleViewItemClickListener OnRecycleViewItemClickListener
     */
    public void setOnRecycleViewItemClickListener(OnRecycleViewItemClickListener onRecycleViewItemClickListener) {
        mOnRecycleViewItemClickListener = onRecycleViewItemClickListener;
    }

    /**
     * 设置数据源
     *
     * @param list List
     */
    public void setData(List<T> list) {
        if (list != null) {
            mDataList = new ArrayList<>(list);
        } else {
            mDataList = new ArrayList<>();
        }
    }

    /**
     * 添加数据源
     *
     * @param list List
     */
    public void addData(List<T> list) {
        if (!ListUtils.isEmpty(list)) {
            mDataList.addAll(list);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        T data = mDataList.get(position);
        if (data != null) {
            /**绑定holder数据*/
            holder.bindViews(data);
            /**ItemView设置监听*/
            View itemView = holder.itemView;
            if (itemView != null) {
                itemView.setTag(position);
                itemView.setClickable(true);
                itemView.setLongClickable(true);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

        }
    }

    @Override
    public void onClick(View v) {
        if (mOnRecycleViewItemClickListener != null) {
            mOnRecycleViewItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return mOnRecycleViewItemClickListener != null
                && mOnRecycleViewItemClickListener.onItemLongClick(v, (Integer) v.getTag());
    }
}
