package com.goodchef.liking.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.utils.ResourceUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.data.SportDataEntity;
import com.goodchef.liking.widgets.HistogramView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017/09/14
 * desc:
 *
 * @author: chenlei
 * @version:1.0
 */

public class SportHistogramAdapter extends BaseRecyclerAdapter<SportDataEntity> {

    private int selectCurrPosition = -1;

    public int getSelectCurrPosition() {
        return selectCurrPosition;
    }

    public void setSelectCurrPosition(int selectCurrPosition) {
        this.selectCurrPosition = selectCurrPosition;
        this.notifyDataSetChanged();
    }

    public SportHistogramAdapter(Context mContext) {
        super(mContext);
    }

    public void setDatas(List<SportDataEntity> datas, boolean isLoadMore) {
        setDatas(datas);
        if (getDatas().size() > 0 && !isLoadMore) {
            selectCurrPosition = 0;
        }
        datas.get(selectCurrPosition).setChecked(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        return new SportDataViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sport_tab, null));
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, SportDataEntity data) {
        if (viewHolder instanceof SportDataViewHolder) {
            SportDataViewHolder holder = (SportDataViewHolder) viewHolder;
            holder.setRealPosition(RealPosition);
            holder.bindViews(data);
        }
    }

    public static class SportDataViewHolder extends BaseRecycleViewHolder<SportDataEntity> {

        @BindView(R.id.text_date)
        TextView title;
        @BindView(R.id.text_week)
        TextView content;
        @BindView(R.id.histogramview)
        HistogramView histogramView;
        @BindView(R.id.image_triangle)
        ImageView imageTriangle;

        public SportDataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setRealPosition(int position) {
            this.mPosition = position;
        }

        @Override
        public void bindViews(SportDataEntity date4Index) {
            if (date4Index == null) return;
            histogramView.setPercentageText(date4Index.getPercentageText());
            histogramView.setPercentage(Float.parseFloat(date4Index.getPercentage()));
            title.setText(date4Index.getTitle());
            content.setText(date4Index.getContent());
            if (date4Index.isChecked()) {
                imageTriangle.setVisibility(View.VISIBLE);
                histogramView.setColor(ContextCompat.getColor(itemView.getContext(), R.color.his_bg_green), ContextCompat.getColor(itemView.getContext(), R.color.his_bg_green));
                if (Float.parseFloat(date4Index.getPercentage()) == 0.0) {
                    histogramView.setPercentageText("0mins");
                    histogramView.setPercentage(Float.parseFloat(date4Index.getPercentage()));
                } else {
                    histogramView.setPercentageText(date4Index.getPercentageText());
                    histogramView.setPercentage(Float.parseFloat(date4Index.getPercentage()));
                }
                title.setSelected(true);
                content.setSelected(true);
            } else {
                imageTriangle.setVisibility(View.INVISIBLE);
                histogramView.setColor(ContextCompat.getColor(itemView.getContext(), R.color.his_bg), ContextCompat.getColor(itemView.getContext(), R.color.ce6e6e6));
                if (Float.parseFloat(date4Index.getPercentage()) == 0.0) {
                    histogramView.setPercentageText("NO\nTRAINING");
                    histogramView.setPercentage(Float.parseFloat(date4Index.getPercentage()));
                } else {
                    histogramView.setPercentageText("");
                    histogramView.setPercentage(Float.parseFloat(date4Index.getPercentage()));
                }
                title.setSelected(false);
                content.setSelected(false);
            }
            if (mPosition % 2 == 0) {
                itemView.setBackgroundColor(ResourceUtils.getColor(R.color.cfafafa));
            } else {
                itemView.setBackgroundColor(ResourceUtils.getColor(R.color.cf5f5f5));
            }
        }
    }
}
