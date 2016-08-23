package com.aaron.android.framework.base.widget.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

    protected final String TAG = getClass().getSimpleName();;
    private List<T> mDataList = new ArrayList<>();

    private Context mContext;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;

    private OnRecycleViewItemClickListener mOnRecycleViewItemClickListener;
    private int mPosition;

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
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return createHeaderViewHolder();
        }
        return createViewHolder(parent);
    }


    protected abstract VH createHeaderViewHolder();

    protected abstract VH createViewHolder(ViewGroup parent);

    /**
     * 设置HeaderView
     * @param headerView View
     */
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    /**
     * 获取HeaderView
     * @return View
     */
    public View getHeaderView() {
        return mHeaderView;
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

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }



    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDataList.size() : mDataList.size() + 1;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        int realPosition = getRealPosition(holder);
        T data = mDataList.get(realPosition);
        if (data != null) {
            /**绑定holder数据*/
            holder.bindViews(data);
            holder.mPosition = realPosition;
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
