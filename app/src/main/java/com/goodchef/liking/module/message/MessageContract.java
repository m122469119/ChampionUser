package com.goodchef.liking.module.message;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.goodchef.liking.data.remote.retrofit.result.LikingResult;
import com.goodchef.liking.data.remote.retrofit.result.MessageResult;
import com.goodchef.liking.data.remote.rxobserver.LikingBaseObserver;
import com.goodchef.liking.data.remote.rxobserver.PagerLoadingObserver;

/**
 * 说明:
 * Author : shaozucheng
 * Time: 下午4:36
 * version 1.0.0
 */

interface MessageContract {


    interface View extends BaseView {
        void updateMessageList(MessageResult.MessageData messageData);
        void setReadMessage();
    }

    class Presenter extends RxBasePresenter<View> {
        private MessageModel mMessageModel;

        public Presenter() {
            mMessageModel = new MessageModel();
        }

        public void getMessageList(int page) {
            mMessageModel.getMessageList(page).subscribe(new PagerLoadingObserver<MessageResult>(mView) {
                @Override
                public void onNext(MessageResult result) {
                    super.onNext(result);
                    if (result == null) return;
                    mView.updateMessageList(result.getData());
                }
            });
        }

        public void setReadMessage(String messageId) {
            mMessageModel.setMessageRead(messageId).subscribe(new LikingBaseObserver<LikingResult>(mView) {
                @Override
                public void onNext(LikingResult value) {
                    if (value == null) return;
                    mView.setReadMessage();
                }
            });
        }
    }
}
