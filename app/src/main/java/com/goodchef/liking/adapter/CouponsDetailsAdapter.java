package com.goodchef.liking.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.goodchef.liking.R;
import com.goodchef.liking.http.result.CouponsDetailsResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created on 2017/3/10
 * Created by sanfen
 *
 * @version 1.0.0
 */

public class CouponsDetailsAdapter extends RecyclerView.Adapter {

    public static final int DETAILS = 0x00000001; //优惠券
    public static final int TITLE = 0x00000002; //优惠券头
    public static final int VENUE = 0x00000003; //健身房列表
    public static final int MORE = 0x00000004; //附加信息列表

    public Context mContext;


    private CouponsDetailsResult.DataBean mCoupon;
    private List<CouponsDetailsResult.DataBean.GymListBean> mVenueList;
    private List<CouponsDetailsResult.DataBean.DescListBean> mCouponMore;

    View.OnClickListener mOnReadAllClickListener;

    public void setOnReadAllClickListenter(View.OnClickListener listenter) {
        this.mOnReadAllClickListener = listenter;
    }

    @Override
    public int getItemCount() {
        return mCoupon== null ? 0 : 1 + 1 + mVenueList.size() + 1 + mCouponMore.size();
    }

    public CouponsDetailsAdapter(Context mContext) {
        this.mContext = mContext;
        mVenueList = new ArrayList<>();
        mCouponMore = new ArrayList<>();
    }

    public void setVenueList(List<CouponsDetailsResult.DataBean.GymListBean> mVenueList) {
        this.mVenueList.clear();
        if (mVenueList != null) {
            this.mVenueList.addAll(mVenueList);
        }
    }

    public void setCouponMore(List<CouponsDetailsResult.DataBean.DescListBean> mCouponMore) {
        this.mCouponMore.clear();
        if (mCouponMore != null) {
            this.mCouponMore.addAll(mCouponMore);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View v = null;
        switch (viewType) {
            case DETAILS:
                v = LayoutInflater.from(mContext).inflate(R.layout.item_coupons_details, null);
                holder = new DetailsViewHolder(v);
                break;
            case TITLE:
                v = LayoutInflater.from(mContext).inflate(R.layout.item_coupons_title, null);
                holder = new TitleViewHolder(v);
                break;
            case VENUE:
                v = LayoutInflater.from(mContext).inflate(R.layout.item_coupons_venue, null);
                holder = new VenueViewHolder(v);
                break;
            case MORE:
                v = LayoutInflater.from(mContext).inflate(R.layout.item_coupons_more, null);
                holder = new MoreViewHolder(v);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            DetailsViewHolder viewHolde = (DetailsViewHolder) holder;
            setDetailViewHolder(viewHolde, position);
        } else if (position == 1 || position == mVenueList.size() + 2) {
            TitleViewHolder viewHolder = (TitleViewHolder) holder;
            setTitleViewHolder(viewHolder, position - 1);
        } else if (position <= mVenueList.size() + 1) {
            VenueViewHolder viewHolder = (VenueViewHolder) holder;
            setVenueViewHolder(viewHolder, position - 2);
        } else {
            MoreViewHolder viewHolder = (MoreViewHolder) holder;
            setMoreViewHolder(viewHolder, position - 3 - mVenueList.size());
        }
    }

    /**
     * 设置优惠券详情
     * @param viewHolde
     * @param position
     */
    private void setDetailViewHolder(DetailsViewHolder viewHolde, int position) {
        viewHolde.mTitle.setText(mCoupon.getTitle());
        viewHolde.mDraw.setVisibility(View.GONE);
        viewHolde.mNext.setVisibility(View.GONE);
        viewHolde.mContentTop.setText(mCoupon.getCondition_desc());
        viewHolde.mContentBottom.setText(mCoupon.getValid_date());
        viewHolde.mHideView.setVisibility(View.GONE);
        viewHolde.mPrice.setText(mCoupon.getAmount());
    }

    /**
     * 设置附加信息
     *
     * @param viewHolder
     * @param pos
     */
    private void setMoreViewHolder(MoreViewHolder viewHolder, int pos) {
        CouponsDetailsResult.DataBean.DescListBean descListBean = mCouponMore.get(pos);
        viewHolder.mTitle.setText(descListBean.getTitle());
        viewHolder.mContent.setText(descListBean.getName());
    }

    /**
     * 设置场馆的列表
     *
     * @param viewHolder
     * @param pos
     */
    private void setVenueViewHolder(VenueViewHolder viewHolder, int pos) {
        CouponsDetailsResult.DataBean.GymListBean gymListBean = mVenueList.get(pos);
        viewHolder.mName.setText(gymListBean.getGym_name());
        viewHolder.mDistance.setText(gymListBean.getDistance());
    }


    /**
     * 设置标题
     *  0 不展示, 1, 展示
     * @param viewHolder
     * @param position
     */
    private void setTitleViewHolder(TitleViewHolder viewHolder, int position) {
        viewHolder.mReadMoreLayout.setVisibility(View.GONE);

        if (position == 0) {
            viewHolder.mTitle.setText(mContext.getString(R.string.can_used_venue));
            if (mCoupon.getShow_all() == 1)
                viewHolder.mReadMoreLayout.setVisibility(View.VISIBLE);
            if (mOnReadAllClickListener != null) {
                viewHolder.mReadMoreLayout.setOnClickListener(mOnReadAllClickListener);
            }
        } else {
            viewHolder.mTitle.setText(mContext.getString(R.string.used_need_know));

        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return DETAILS;
        } else if (position == 1 || position == mVenueList.size() + 2) {
            return TITLE;
        } else if (position <= mVenueList.size() + 1) {
            return VENUE;
        } else {
            return MORE;
        }
    }

    public void setCouponData(CouponsDetailsResult.DataBean couponData) {
        this.mCoupon = couponData;
        setVenueList(mCoupon.getGym_list());
        setCouponMore(mCoupon.getDesc_list());
    }


    class DetailsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_item_coupons)
        View mItemView;
        @BindView(R.id.txt_item_coupons_title)
        TextView mTitle;
        @BindView(R.id.txt_item_coupons_price)
        TextView mPrice;
        @BindView(R.id.txt_item_coupons_content_top)
        TextView mContentTop;
        @BindView(R.id.txt_item_coupons_content_bottom)
        TextView mContentBottom;
        @BindView(R.id.img_item_coupons_draw)
        ImageView mDraw;
        @BindView(R.id.img_item_coupons_next)
        ImageView mNext;
        @BindView(R.id.rl_item_coupons_hide)
        View mHideView;
        @BindView(R.id.txt_item_coupons_hide)
        TextView mHideText;


        public DetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    class TitleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_coupons_used_venue)
        TextView mTitle;
        @BindView(R.id.ll_coupons_read_more)
        View mReadMoreLayout;

        public TitleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class VenueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_item_coupons_gym_name)
        TextView mName;
        @BindView(R.id.txt_item_coupons_gym_distance)
        TextView mDistance;
        public VenueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class MoreViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_item_coupons_more_title)
        TextView mTitle;
        @BindView(R.id.txt_item_coupons_more_content)
        TextView mContent;

        public MoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
