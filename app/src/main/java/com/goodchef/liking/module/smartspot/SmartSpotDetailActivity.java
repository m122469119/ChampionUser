package com.goodchef.liking.module.smartspot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter;
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ttdevs
 * 2017-09-01 (AndroidLiking)
 * https://github.com/ttdevs
 */
public class SmartSpotDetailActivity extends AppBarMVPSwipeBackActivity<SmartSpotDetailContract.Presenter> implements SmartSpotDetailContract.View {
    @BindView(R.id.view_state)
    LikingStateView mStateView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_datetime)
    TextView mTvDateTime;

    private String mRecordId;

    private SmartDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartspot_history);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mRecordId = getIntent().getStringExtra(KEY_RDID);

        mAdapter = new SmartDetailAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                requestSmartspotDetail();
            }
        });


        setTitle("SmartSpot训练记录");
        requestSmartspotDetail();
    }

    private void requestSmartspotDetail() {
        mPresenter.requestSmartDetail(mRecordId);
    }

    private String calDateString(String start, String end){
        String result = null;
        try {
            Calendar startCal = Calendar.getInstance();
            startCal.setTimeInMillis(Long.parseLong(start));
            Calendar endCal = Calendar.getInstance();
            endCal.setTimeInMillis(Long.parseLong(end));
            int year = startCal.get(Calendar.YEAR);
            int month = startCal.get(Calendar.MONTH);
            int day = startCal.get(Calendar.DAY_OF_MONTH);
            String week = getWeek(startCal.get(Calendar.DAY_OF_WEEK));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String getWeek(int week){
        switch (week){
            case 1:
                return "周一";


            default:
                return "周日";

        }
    }

    @Override
    public void updateData(SmartspotDetailResult.DataBean data) {
        if (null == data) {
            mStateView.setState(StateView.State.NO_DATA);
            return;
        }
        mStateView.setState(StateView.State.SUCCESS);
        SmartspotDetailResult.DataBean.InfoBean info = data.getInfo();
        mTvTitle.setText(info.getTitle());
        mTvDateTime.setText(info.getStartTime() + info.getEndTime());// TODO: 2017/9/2

        mAdapter.addData(data.getList());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }

    @Override
    public void setPresenter() {
        mPresenter = new SmartSpotDetailContract.Presenter();
    }

    class SmartDetailAdapter extends BaseRecycleViewAdapter<SmartDetailAdapter.SmartspotViewHolder, SmartspotDetailResult.DataBean.ListBean> {

        private Context mContext;

        public SmartDetailAdapter(Context context) {
            super(context);

            mContext = context;
        }

        @Override
        protected SmartspotViewHolder createViewHolder(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_smartspot_detail, parent, false);
            return new SmartspotViewHolder(view);
        }

        class SmartspotViewHolder extends BaseRecycleViewHolder<SmartspotDetailResult.DataBean.ListBean> {

            @BindView(R.id.tv_reps)
            TextView tvReps;
            @BindView(R.id.tv_time)
            TextView tvTime;
            @BindView(R.id.tv_rest_time)
            TextView tvRestTime;
            @BindView(R.id.hiv_capture)
            HImageView hivCapture;

            public SmartspotViewHolder(View itemView) {
                super(itemView);

                ButterKnife.bind(this, itemView);
            }

            @Override
            public void bindViews(SmartspotDetailResult.DataBean.ListBean data) {
                tvReps.setText(data.getReps());
                tvTime.setText(String.valueOf(data.getTime()));
                tvRestTime.setText(String.valueOf(data.getRestTime()));
                if (null != data.getMedias() && data.getMedias().size() > 0) {
                    HImageLoaderSingleton.loadImage(hivCapture, data.getMedias().get(0), SmartSpotDetailActivity.this);
                }
            }
        }
    }

    private static final String KEY_RDID = "key_record_id";

    public static void launch(Context context, String recordID) {
        if (null == context) {
            return;
        }
        Intent intent = new Intent(context, SmartSpotDetailActivity.class);
        intent.putExtra(KEY_RDID, recordID);
        context.startActivity(intent);
    }
}
