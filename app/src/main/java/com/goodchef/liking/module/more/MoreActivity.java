package com.goodchef.liking.module.more;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.AppBarMVPSwipeBackActivity;
import com.aaron.android.framework.base.storage.DiskStorageManager;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.aaron.common.utils.StringUtils;
import com.goodchef.liking.R;
import com.goodchef.liking.data.local.LikingPreference;
import com.goodchef.liking.data.remote.retrofit.result.CheckUpdateAppResult;
import com.goodchef.liking.eventmessages.LoginOutMessage;
import com.goodchef.liking.module.about.AboutActivity;
import com.goodchef.liking.module.joinus.becomecoach.BecomeTeacherActivity;
import com.goodchef.liking.module.joinus.contractjoin.ContactJonInActivity;
import com.goodchef.liking.umeng.UmengEventId;
import com.goodchef.liking.utils.FileDownloaderManager;
import com.goodchef.liking.utils.UMengCountUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * 说明:更多
 * Author : shaozucheng
 * Time: 下午3:17
 * version 1.0.0
 */

public class MoreActivity extends AppBarMVPSwipeBackActivity<MoreContract.Presenter> implements MoreContract.View {

    @BindView(R.id.login_out_btn)
    TextView mLoginOutBtn;
    @BindView(R.id.check_update_ImageView)
    ImageView mCheckUpdateImageView;
    @BindView(R.id.layout_check_update)
    RelativeLayout mLayoutCheckUpdate;
    @BindView(R.id.check_update_prompt_TextView)
    TextView mCheckUpdatePromptTextView;

    private LinearLayout mContactJoinLayout;//联系加盟
    private LinearLayout mBecomeTeacherLayout;//称为教练
    private LinearLayout mAboutUsLayout;//关于我们

    private CheckUpdateAppResult.UpdateAppData mUpdateAppData;
    private FileDownloaderManager mFileDownloaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
        setTitle(getString(R.string.title_activity_more));
        initView();
        initViewIconAndText();
        sendUpdateAppRequest();
    }

    private void initView() {
        mAboutUsLayout = (LinearLayout) findViewById(R.id.layout_about_us);
        mContactJoinLayout = (LinearLayout) findViewById(R.id.layout_contact_join);
        mBecomeTeacherLayout = (LinearLayout) findViewById(R.id.layout_become_teacher);
    }

    private void initViewIconAndText() {
        setMySettingCard(mContactJoinLayout, R.string.layout_contact_join, true);
        setMySettingCard(mBecomeTeacherLayout, R.string.layout_become_teacher, true);
        setMySettingCard(mAboutUsLayout, R.string.layout_about_us, true);

        if (LikingPreference.isLogin()) {
            mLoginOutBtn.setVisibility(android.view.View.VISIBLE);
        } else {
            mLoginOutBtn.setVisibility(android.view.View.GONE);
        }
    }


    private void setMySettingCard(android.view.View view, int text, boolean isShowLine) {
        TextView textView = (TextView) view.findViewById(R.id.standard_my_text);
        android.view.View line = view.findViewById(R.id.standard_view_line);
        textView.setText(text);
        if (isShowLine) {
            line.setVisibility(android.view.View.VISIBLE);
        } else {
            line.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * 发送请求
     */
    private void sendUpdateAppRequest() {
        mPresenter.checkAppUpdate();
    }



    @OnClick({R.id.layout_contact_join, R.id.layout_become_teacher, R.id.layout_about_us,
            R.id.login_out_btn, R.id.layout_check_update})
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.layout_contact_join://联系加盟
                UMengCountUtil.UmengCount(this, UmengEventId.CONTACTJONINACTIVITY);
                startActivity(ContactJonInActivity.class);
                break;
            case R.id.layout_become_teacher://成为教练
                UMengCountUtil.UmengCount(this, UmengEventId.BECOMETEACHERACTIVITY);
                startActivity(BecomeTeacherActivity.class);
                break;
            case R.id.layout_about_us://关于我们
                startActivity(AboutActivity.class);
                break;
            case R.id.layout_check_update:
                if (mUpdateAppData != null) {
                    int update = mUpdateAppData.getUpdate();
                    if (update == 1 || update == 2) {
                        showCheckUpdateDialog();
                    }
                }
                break;
            case R.id.login_out_btn:
                if (LikingPreference.isLogin()) {
                    showExitDialog();
                } else {
                    showToast(getString(R.string.not_login));
                }
                break;
        }
    }


    private void showCheckUpdateDialog() {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) return;
                        HBaseDialog.Builder builder = new HBaseDialog.Builder(MoreActivity.this);
                        android.view.View view = LayoutInflater.from(MoreActivity.this).inflate(R.layout.item_textview, null, false);
                        TextView textView = (TextView) view.findViewById(R.id.dialog_custom_title);
                        textView.setText((mUpdateAppData.getTitle()));
                        builder.setCustomTitle(view);
                        builder.setMessage(mUpdateAppData.getContent());
                        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton(R.string.dialog_app_update,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!StringUtils.isEmpty(mUpdateAppData.getUrl())) {
                                            mFileDownloaderManager = new FileDownloaderManager(MoreActivity.this);
                                            mFileDownloaderManager.downloadFile(mUpdateAppData.getUrl(), DiskStorageManager.getInstance().getApkFileStoragePath());
                                        }
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                    }
                });
    }

    /**
     * 退出登录dialog
     * TODO 清除默认的公告剔除列表
     */
    private void showExitDialog() {
        final HBaseDialog.Builder builder = new HBaseDialog.Builder(this);
        builder.setMessage(getString(R.string.login_exit_message));
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exitLoginRequest();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 退出登录发送请求
     */
    private void exitLoginRequest() {
        mPresenter.loginOut(this);
    }

    @Override
    public void updateLoginOut() {
        showToast(getString(R.string.exit_login_success));
        postEvent(new LoginOutMessage());
        mLoginOutBtn.setVisibility(android.view.View.GONE);
        finish();
    }

    @Override
    public void updateCheckUpdateAppView(CheckUpdateAppResult.UpdateAppData updateAppData) {
        mUpdateAppData = updateAppData;
        int update = mUpdateAppData.getUpdate();
        if (update == 0) {//无更新
            mCheckUpdatePromptTextView.setVisibility(android.view.View.VISIBLE);
            mCheckUpdateImageView.setVisibility(android.view.View.GONE);
        } else if (update == 1 || update == 2) {//有更新
            mCheckUpdatePromptTextView.setVisibility(android.view.View.GONE);
            mCheckUpdateImageView.setVisibility(android.view.View.VISIBLE);
        }
    }

    @Override
    public void setPresenter() {
        mPresenter = new MoreContract.Presenter();
    }
}
