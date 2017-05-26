package com.goodchef.liking.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.common.utils.StringUtils;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.GymDetailsResult;

/**
 * 说明:门店设备适配器
 * Author shaozucheng
 * Time:16/9/1 下午5:05
 */
public class ArenaTagAdapter extends BaseRecycleViewAdapter<ArenaTagAdapter.ArenaTagViewHolder, GymDetailsResult.GymDetailsData.TagData> {

    private Context mContext;

    public ArenaTagAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected ArenaTagViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_arena_tag, parent, false);
        return new ArenaTagViewHolder(view, mContext);
    }

    public class ArenaTagViewHolder extends BaseRecycleViewHolder<GymDetailsResult.GymDetailsData.TagData> {
        HImageView mHImageView;
        TextView mTextView;

        public ArenaTagViewHolder(View itemView, Context context) {
            super(itemView, context);
            mHImageView = (HImageView) itemView.findViewById(R.id.image_tag);
            mTextView = (TextView) itemView.findViewById(R.id.text_tag);
        }

        @Override
        public void bindViews(GymDetailsResult.GymDetailsData.TagData object) {
            String imageUrl = object.getImgUrl();
            if (!StringUtils.isEmpty(imageUrl)) {
                HImageLoaderSingleton.loadImage(mHImageView, imageUrl, (Activity) mContext);
            }
            mTextView.setText(object.getName());
        }
    }
}
