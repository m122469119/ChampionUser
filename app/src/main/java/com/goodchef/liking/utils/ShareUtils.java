package com.goodchef.liking.utils;

import android.content.Context;
import android.view.View;
import com.aaron.share.weixin.WeixinShare;
import com.aaron.share.weixin.WeixinShareData;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.retrofit.result.data.ShareData;
import com.goodchef.liking.dialog.ShareCustomDialog;

/**
<<<<<<< 1176522f632dfe18b760e1da07754ca924fa5450
 * Created by fensan on 2017/6/1.
 */
public class ShareUtils {

    public static void showShareDialog(final Context context, final ShareData shareData) {
        final ShareCustomDialog shareCustomDialog = new ShareCustomDialog(context);
        shareCustomDialog.setViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeixinShare weixinShare = new WeixinShare(context);
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
