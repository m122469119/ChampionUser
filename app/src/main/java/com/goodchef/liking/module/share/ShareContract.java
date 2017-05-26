package com.goodchef.liking.module.share;

import android.content.Context;
import android.view.View;

import com.aaron.android.framework.base.mvp.presenter.BasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.share.weixin.WeixinShare;
import com.aaron.share.weixin.WeixinShareData;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.dialog.ShareCustomDialog;
import com.goodchef.liking.data.remote.retrofit.result.ShareResult;
import com.goodchef.liking.data.remote.retrofit.result.data.ShareData;

/**
 * 说明:分享
 * Author : shaozucheng
 * Time: 下午2:40
 * version 1.0.0
 */

public interface ShareContract {
    interface ShareView extends BaseView{
        void updateShareView(ShareData shareData);
    }

     class SharePresenter  extends BasePresenter<ShareView>{

         private ShareModel mShareModel;
         public SharePresenter(Context context, ShareView mainView) {
             super(context, mainView);
             mShareModel = new ShareModel();
         }

         //私教课分享
         public void getPrivateShareData(String trainId) {
             mShareModel.getPrivateCoursesShare(trainId)
             .subscribe(new LikingBaseObserver<ShareResult>(mContext, mView) {
                 @Override
                 public void onNext(ShareResult value) {
                     if(value == null) return;
                     mView.updateShareView(value.getShareData());
                 }
             });
         }

         //团体课分享
         public void getGroupShareData(String scheduleId) {
             mShareModel.getGroupCoursesShare(scheduleId)
             .subscribe(new LikingBaseObserver<ShareResult>(mContext, mView) {
                 @Override
                 public void onNext(ShareResult value) {
                     if(value == null) return;
                     mView.updateShareView(value.getShareData());
                 }
             });
         }

         //我的运动数据分享
         public void getUserShareData() {
             mShareModel.getUserShare()
             .subscribe(new LikingBaseObserver<ShareResult>(mContext, mView) {
                 @Override
                 public void onNext(ShareResult value) {
                     if(value == null) return;
                     mView.updateShareView(value.getShareData());
                 }
             });
         }


         public void showShareDialog(final Context context, final ShareData shareData) {
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
}
