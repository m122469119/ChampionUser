package com.goodchef.liking.module.scanqrcode;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.aaron.android.framework.base.mvp.presenter.RxBasePresenter;
import com.aaron.android.framework.base.mvp.view.BaseView;
import com.aaron.android.framework.base.widget.dialog.HBaseDialog;
import com.goodchef.liking.R;
import com.goodchef.liking.data.remote.LiKingRequestCode;
import com.goodchef.liking.data.remote.retrofit.ApiException;
import com.goodchef.liking.data.remote.retrofit.result.QRCodeResult;
import com.goodchef.liking.data.remote.rxobserver.ProgressObserver;

/**
 * Created by aaa on 17/9/2.
 */

public class ScanQrCodeContract {


    interface View extends BaseView {
        void updateScanQrView(QRCodeResult.DataBean dataBean);

        void scanErrorView();
    }


    static class Presenter extends RxBasePresenter<View> {
        ScanQrCodeModel scanQrCodeModel;

        public Presenter() {
            scanQrCodeModel = new ScanQrCodeModel();
        }

        public void sendScanQrCode(Context context, String code) {
            scanQrCodeModel.scanQrCode(code).subscribe(addObserverToCompositeDisposable(new ProgressObserver<QRCodeResult>(context, R.string.loading_data, mView) {

                @Override
                public void onNext(QRCodeResult value) {
                    if (value == null) return;
                    mView.updateScanQrView(value.getData());
                }

                @Override
                public void apiError(ApiException apiException) {
//                    if (apiException.getErrorCode() == LiKingRequestCode.SPARTSPOT_OFFLINE_ERRCODE){
//
//                    }else if (apiException.getErrorCode() == LiKingRequestCode.USER_NO_CARD_ERRCODE){
//
//                    }else if (apiException.getErrorCode() == LiKingRequestCode.USER_CARD_OVERDUE_ERRCODE){
//
//                    }else if (apiException.getErrorCode() == LiKingRequestCode.NO_HAVE_BRACELET_ERRCODE){
//
//                    }
                    super.apiError(apiException);
                    mView.scanErrorView();
                }

                @Override
                public void networkError(Throwable throwable) {
                    super.networkError(throwable);
                }
            }));
        }

        public void showDialog(Context context, String string) {
            HBaseDialog.Builder builder = new HBaseDialog.Builder(context);
            android.view.View view = LayoutInflater.from(context).inflate(R.layout.dialog_one_content, null, false);
            TextView mTitleTextView = (TextView) view.findViewById(R.id.one_dialog_title);
            TextView mTextView = (TextView) view.findViewById(R.id.one_dialog_content);
            mTextView.setText(string);
            mTitleTextView.setText("提示");
            builder.setCustomView(view);
            builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mView.scanErrorView();
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }

    }


}
