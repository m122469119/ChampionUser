package com.goodchef.liking.module.runpush;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.imageloader.code.HImageView;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.utils.HImageLoaderSingleton;
import com.goodchef.liking.widgets.base.LikingStateView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ttdevs
 * 2017-09-01 (AndroidLiking)
 * https://github.com/ttdevs
 */
public class RunFinishActivity extends AppBarMVPSwipeBackActivity<RunFinishContract.Presenter> implements RunFinishContract.View {

    @BindView(R.id.view_state)
    LikingStateView mStateView;
    @BindView(R.id.view_content)
    LinearLayout mViewContent;

    @BindView(R.id.iv_header)
    HImageView ivHeader;
    @BindView(R.id.tv_username)
    TextView tvUserName;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_header_content)
    TextView tvHeaderContent;
    @BindView(R.id.tv_left_title)
    TextView tvLeftTitle;
    @BindView(R.id.tv_left_content)
    TextView tvLeftContent;
    @BindView(R.id.tv_right_title)
    TextView tvRightTitle;
    @BindView(R.id.tv_right_content)
    TextView tvRightContent;
    @BindView(R.id.tv_partner_count)
    TextView tvPartnerCount;

    @OnClick({R.id.bt_finish})
    public void onViewClicked(View view) {
        finish();
    }

    private String mUserId;
    private String mMarahtonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_finish);
        ButterKnife.bind(this);

        hideAppBar();
        initView();
    }

    private void initView() {
        mUserId = getIntent().getStringExtra(KEY_REC_USER_ID);
        mMarahtonId = getIntent().getStringExtra(KEY_MARAHTON_ID);

        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                requestData();
            }
        });

        requestData();
    }

    private void requestData() {
        mPresenter.requestData(mUserId, mMarahtonId);
    }

    @Override
    public void updateData(RunFinishResult.DataBean data) {
        if (null == data) {
            mStateView.setState(StateView.State.NO_DATA);
            return;
        }
        mStateView.setState(StateView.State.SUCCESS);
        RunFinishResult.DataBean.UserBean user = data.getUser();

        HImageLoaderSingleton.loadImage(ivHeader, LikingPreference.getUserIconUrl(), RunFinishActivity.this);
        tvUserName.setText(LikingPreference.getNickName());

        if ("0".equals(mMarahtonId)) { // 普通跑
            tvHeaderTitle.setText(getString(R.string.run_finish_finish_header));
            tvHeaderContent.setText("");
            tvLeftTitle.setText("跑步距离：");
            tvLeftContent.setText(user.getDistance());
            tvRightTitle.setText("跑步时间：");
            tvRightContent.setText(user.getUseTime());
        } else { // 马拉松
            tvHeaderTitle.setText(getString(R.string.run_finish_finish_header_marahton));
            tvHeaderContent.setText("完成" + user.getMarahtonName()); // TODO: 2017/9/3
            tvLeftTitle.setText("耗时：");
            tvLeftContent.setText(user.getUseTime());
            tvRightTitle.setText("名次：");
            tvRightContent.setText(user.getNum());
        }
        tvPartnerCount.setText(String.valueOf(user.getCount()));

        initListItem(data.getList());
    }

    @Override
    public void followUser(String userId) {
        for (int i = 0; i < mViewContent.getChildCount(); i++) {
            View view = mViewContent.getChildAt(i);
            RunFinishResult.DataBean.ListBean item = (RunFinishResult.DataBean.ListBean) view.getTag();
            if (item.getUserId().equals(userId)) {
                TextView tvStatus = (TextView) view.findViewById(R.id.tv_status);
                tvStatus.setText("已关注");
            }
        }
    }

    private void initListItem(List<RunFinishResult.DataBean.ListBean> list) {
        if (null == list || list.size() == 0) {
            return;
        }
        mViewContent.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (final RunFinishResult.DataBean.ListBean item : list) {
            View view = inflater.inflate(R.layout.item_run_finish_partner, null);
            view.setTag(item);
            TextView tvUsername = (TextView) view.findViewById(R.id.tv_username);
            TextView tvNo = (TextView) view.findViewById(R.id.tv_no);
            TextView tvStatus = (TextView) view.findViewById(R.id.tv_status);
            tvUsername.setText(item.getName());
            tvNo.setText(String.valueOf(item.getDesc()));
            tvStatus.setText("+ 关注");
            mViewContent.addView(view);
            tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.follow(item.getUserId());
                }
            });
        }
    }

    @Override
    public void changeStateView(StateView.State state) {
        mStateView.setState(state);
    }

    @Override
    public void setPresenter() {
        mPresenter = new RunFinishContract.Presenter();
    }

    private static final String KEY_REC_USER_ID = "key_rec_user_id";
    private static final String KEY_MARAHTON_ID = "key_marahton_id";

    public static void launch(Context context, String userId, String marahtonId) {
        if (null == context) {
            return;
        }
        Intent intent = new Intent(context, RunFinishActivity.class);
        intent.putExtra(KEY_REC_USER_ID, userId);
        intent.putExtra(KEY_MARAHTON_ID, marahtonId);
        context.startActivity(intent);
    }

    public static void launchWithNew(Context context, String userId, String marahtonId) {
        if (null == context) {
            return;
        }
        Intent intent = new Intent(context, RunFinishActivity.class);
        intent.putExtra(KEY_REC_USER_ID, userId);
        intent.putExtra(KEY_MARAHTON_ID, marahtonId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
