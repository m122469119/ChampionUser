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
        extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {

    protected final String TAG = getClass().getSimpleName();;
    private List<T> mDataList = new ArrayList<>();

    private Context mContext;

    public static final int TYPE_HEADER = RecyclerView.INVALID_TYPE;
    public static final int TYPE_FOOTER = RecyclerView.INVALID_TYPE - 1;
    private View mHeaderView;
    private View mFooterView;

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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new HeaderFooterViewHolder(mHeaderView);
        } else if (mFooterView != null && viewType ==TYPE_FOOTER){
            return new HeaderFooterViewHolder(mFooterView);
        }
        return createViewHolder(parent);
    }

    class HeaderFooterViewHolder extends RecyclerView.ViewHolder {
        public HeaderFooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    protected abstract VH createViewHolder(ViewGroup parent);

    /**
     * 设置HeaderView
     * @param headerView View
     */
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyDataSetChanged();
    }

    public void removeHeaderView() {
        setHeaderView(null);
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyDataSetChanged();
    }

    public void removeFooterView() {
        setFooterView(null);
    }

    /**
     * @return 获取Adapter DataList数据集
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
    public int getItemViewType(int position) {
        int headerCount = mHeaderView == null ? 0 : 1;
        if (position < headerCount) {
            return TYPE_HEADER;
        }
        final int realPosition = position - headerCount;
        if (realPosition < mDataList.size()) {
            return super.getItemViewType(realPosition);
        }
        return TYPE_FOOTER;
    }



    @Override
    public int getItemCount() {
        int count = mDataList.size();
        if (mHeaderView != null) {
            count += 1;
        }
        if (mFooterView != null) {
            count += 1;
        }
        return count;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int headerCount = mHeaderView == null ? 0 : 1;
        if (position < headerCount) {
            return;
        }
        final int realPosition = position - headerCount;
        if (realPosition < mDataList.size()) {
            T data = mDataList.get(realPosition);
            VH viewHolder = (VH) holder;
            if (data != null) {
                /**绑定holder数据*/
                viewHolder.bindViews(data);
                viewHolder.mPosition = realPosition;
                /**ItemView设置监听*/
                View itemView = holder.itemView;
                if (itemView != null) {
                    itemView.setTag(realPosition);
                    itemView.setClickable(true);
                    itemView.setLongClickable(true);
                    itemView.setOnClickListener(this);
                    itemView.setOnLongClickListener(this);
                }

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
