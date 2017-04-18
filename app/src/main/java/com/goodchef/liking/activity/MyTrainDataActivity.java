package com.goodchef.liking.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.framework.base.widget.refresh.StateView;
import com.aaron.android.thirdparty.share.weixin.WeixinShare;
import com.aaron.android.thirdparty.share.weixin.WeixinShareData;
import com.goodchef.liking.R;
import com.goodchef.liking.dialog.ShareCustomDialog;
import com.goodchef.liking.http.result.UserExerciseResult;
import com.goodchef.liking.http.result.data.ShareData;
import com.goodchef.liking.mvp.presenter.SharePresenter;
import com.goodchef.liking.mvp.presenter.UserExercisePresenter;
import com.goodchef.liking.mvp.view.ShareView;
import com.goodchef.liking.mvp.view.UserExerciseView;
import com.goodchef.liking.utils.TypefaseUtil;
import com.goodchef.liking.widgets.base.LikingStateView;

/**
 * 说明:训练数据
 * Author shaozucheng
 * Time:16/7/2 下午4:45
 */
public class MyTrainDataActivity extends AppBarActivity implements UserExerciseView, ShareView {
    private TextView mTrainTime;//训练时间
    private TextView mTrainDistance;//训练距离
    private TextView mTrainCal;//消耗卡路里

    private TextView mTrainCountAll;//训练总次数
    private TextView mTrainTimeAll;//训练总时间
    private TextView mTrainDistanceALL;//训练总距离
    private TextView mTrainCalALl;//消耗总卡路里

    private UserExercisePresenter mUserExercisePresenter;
    private LikingStateView mStateView;
    private SharePresenter mSharePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_train_data);
        setTitle(getString(R.string.title_my_train_data));
        initView();
        mUserExercisePresenter = new UserExercisePresenter(this, this);
        if (mSharePresenter == null) {
            mSharePresenter = new SharePresenter(this, this);
        }
        iniData();
        showRightMenu(getString(R.string.share), shareListener);
    }

    private void initView() {
        mStateView = (LikingStateView) findViewById(R.id.my_train_state_view);
        mTrainTime = (TextView) findViewById(R.id.my_train_time);
        mTrainDistance = (TextView) findViewById(R.id.my_train_distance);
        mTrainCal = (TextView) findViewById(R.id.my_train_cal);

        mTrainCountAll = (TextView) findViewById(R.id.my_train_count_all);
        mTrainTimeAll = (TextView) findViewById(R.id.my_train_time_all);
        mTrainDistanceALL = (TextView) findViewById(R.id.my_train_distance_all);
        mTrainCalALl = (TextView) findViewById(R.id.my_train_cal_all);

        mStateView.setState(StateView.State.LOADING);
        mStateView.setOnRetryRequestListener(new StateView.OnRetryRequestListener() {
            @Override
            public void onRetryRequested() {
                iniData();
            }
        });
    }

    private void iniData() {
        mUserExercisePresenter.getExerciseData();
    }

    @Override
    public void updateUserExerciseView(UserExerciseResult.ExerciseData exerciseData) {
        if (exerciseData != null) {
            mStateView.setState(StateView.State.SUCCESS);
            Typeface typeFace = TypefaseUtil.getImpactTypeface(this);
            mTrainTime.setTypeface(typeFace);
            mTrainDistance.setTypeface(typeFace);
            mTrainCal.setTypeface(typeFace);
            mTrainCountAll.setTypeface(typeFace);
            mTrainDistanceALL.setTypeface(typeFace);
            mTrainCalALl.setTypeface(typeFace);
            mTrainTimeAll.setTypeface(typeFace);

            mTrainTime.setText(exerciseData.getTodayMin());
            mTrainDistance.setText(exerciseData.getTodayDistance());
            mTrainCal.setText(exerciseData.getTodayCal());
            mTrainCountAll.setText(exerciseData.getTotalTimes());
            mTrainTimeAll.setText(exerciseData.getTotalMin());
            mTrainDistanceALL.setText(exerciseData.getTotalDistance());
            mTrainCalALl.setText(exerciseData.getTotalCal());
        } else {
            mStateView.setState(StateView.State.NO_DATA);
        }

    }

    private View.OnClickListener shareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSharePresenter.getUserShareData();
            showRightMenu(getString(R.string.share), null);
        }
    };

    @Override
    public void handleNetworkFailure() {
        mStateView.setState(StateView.State.FAILED);
    }

    @Override
    public void updateShareView(ShareData shareData) {
        showShareDialog(shareData);
        showRightMenu(getString(R.string.share), shareListener);
    }

    private void showShareDialog(final ShareData shareData) {
        final ShareCustomDialog shareCustomDialog = new ShareCustomDialog(this);
        shareCustomDialog.setViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeixinShare weixinShare = new WeixinShare(MyTrainDataActivity.this);
                switch (v.getId()) {
                    case R.id.layout_wx_friend://微信好友
                        WeixinShareData.WebPageData webPageData = new WeixinShareData.WebPageData();
                        webPageData.setWebUrl(shareData.getUrl());
                        webPageData.setTitle(shareData.getTitle());
                        webPageData.setDescription(shareData.getContent());
                        webPageData.setWeixinSceneType(WeixinShareData.WeixinSceneType.FRIEND);
                        webPageData.setIconResId(R.mipmap.ic_launcher);
                        weixinShare.shareWebPage(webPageData);
                        shareCustomDialog.dismiss();
                        break;
                    case R.id.layout_wx_friend_circle://微信朋友圈
                        WeixinShareData.WebPageData webPageData1 = new WeixinShareData.WebPageData();
                        webPageData1.setWebUrl(shareData.getUrl());
                        webPageData1.setTitle(shareData.getTitle());
                        webPageData1.setDescription(shareData.getContent());
                        webPageData1.setWeixinSceneType(WeixinShareData.WeixinSceneType.CIRCLE);
                        webPageData1.setIconResId(R.mipmap.ic_launcher);
                        weixinShare.shareWebPage(webPageData1);
                        shareCustomDialog.dismiss();
                        break;
                    case R.id.cancel_image_button:
                        shareCustomDialog.dismiss();
                        break;
                }
            }
        });
    }
}
