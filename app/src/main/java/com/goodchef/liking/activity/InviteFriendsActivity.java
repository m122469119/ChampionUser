package com.goodchef.liking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.aaron.android.codelibrary.http.RequestError;
import com.aaron.android.codelibrary.utils.StringUtils;
import com.aaron.android.framework.base.ui.actionbar.AppBarActivity;
import com.aaron.android.thirdparty.share.weixin.WeixinShare;
import com.aaron.android.thirdparty.share.weixin.WeixinShareData;
import com.goodchef.liking.R;
import com.goodchef.liking.dialog.ShareCustomDialog;
import com.goodchef.liking.http.api.LiKingApi;
import com.goodchef.liking.http.callback.RequestUiLoadingCallback;
import com.goodchef.liking.http.result.InviteFriendResult;
import com.goodchef.liking.http.verify.LiKingVerifyUtils;
import com.goodchef.liking.storage.Preference;

/**
 * 说明:邀请好友
 * Author shaozucheng
 * Time:16/7/6 下午6:37
 */
public class InviteFriendsActivity extends AppBarActivity implements View.OnClickListener {
    public static final String KEY_INTENT_CONFIRM_TEXT = "key_intent_confirm_text";
    private TextView mInviteCodeTextView;
    private TextView mInvitePromptTextView;
    private TextView mInviteFriendsBtn;
    private TextView mEditInviteCodeBtn;
    private TextView mInviteFriendTitleTextView;

    private String mCode;
    private String shareUrl;
    private String shareTitle;
    private String shareContent;
    private String confirmText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        setTitle(getString(R.string.title_activity_invite_friend));
        initView();
        initData();
    }

    private void initView() {
        mInviteCodeTextView = (TextView) findViewById(R.id.invite_code);
        mInvitePromptTextView = (TextView) findViewById(R.id.invite_prompt);
        mInviteFriendsBtn = (TextView) findViewById(R.id.invite_friend_btn);
        mEditInviteCodeBtn = (TextView) findViewById(R.id.edit_invite_code_btn);
        mInviteFriendTitleTextView = (TextView) findViewById(R.id.invite_show_title);

        mInviteFriendsBtn.setOnClickListener(this);
        mEditInviteCodeBtn.setOnClickListener(this);
    }

    private void initData() {
        LiKingApi.getInviteCode(Preference.getToken(), new RequestUiLoadingCallback<InviteFriendResult>(this, R.string.loading_data) {
            @Override
            public void onSuccess(InviteFriendResult result) {
                super.onSuccess(result);
                if (LiKingVerifyUtils.isValid(InviteFriendsActivity.this, result)) {
                    InviteFriendResult.InviteFriendData data = result.getData();
                    if (data != null) {
                        mCode = data.getCode();
                        shareUrl = data.getShareUrl();
                        shareTitle = data.getShareTitle();
                        shareContent = data.getShareContent();
                        mInviteCodeTextView.setText(mCode);
                        mInviteFriendTitleTextView.setText(Html.fromHtml(data.getShowTitle().trim()));
                        mInvitePromptTextView.setText(Html.fromHtml(data.getShowContent().trim()));
                        confirmText = data.getConfirmText();
                    }
                }
            }

            @Override
            public void onFailure(RequestError error) {
                super.onFailure(error);
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == mInviteFriendsBtn) {
            if (!StringUtils.isEmpty(shareUrl)) {
                showShareDialog();
            }
        } else if (v == mEditInviteCodeBtn) {
            Intent intent = new Intent(this, WriteInviteCodeActivity.class);
            intent.putExtra(KEY_INTENT_CONFIRM_TEXT,confirmText);
            startActivity(intent);
        }
    }

    private void showShareDialog() {
        final ShareCustomDialog shareCustomDialog = new ShareCustomDialog(this);
        shareCustomDialog.setViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeixinShare weixinShare = new WeixinShare(InviteFriendsActivity.this);
                switch (v.getId()) {
                    case R.id.layout_wx_friend://微信好友
                        WeixinShareData.WebPageData webPageData = new WeixinShareData.WebPageData();
                        webPageData.setWebUrl(shareUrl);
                        webPageData.setTitle(shareTitle);
                        webPageData.setDescription(shareContent);
                        webPageData.setWeixinSceneType(WeixinShareData.WeixinSceneType.FRIEND);
                        webPageData.setIconResId(R.mipmap.ic_launcher);
                        weixinShare.shareWebPage(webPageData);
                        shareCustomDialog.dismiss();
                        break;
                    case R.id.layout_wx_friend_circle://微信朋友圈
                        WeixinShareData.WebPageData webPageData1 = new WeixinShareData.WebPageData();
                        webPageData1.setWebUrl(shareUrl);
                        webPageData1.setTitle(shareContent);
                        webPageData1.setDescription(shareContent);
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
