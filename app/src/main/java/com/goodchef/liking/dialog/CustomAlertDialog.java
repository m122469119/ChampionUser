package com.goodchef.liking.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goodchef.liking.R;


/**
 * 描述：自定义 AlertDialog(仿IOS)
 *
 * @author Doc.March
 * @TIME 2015-01-27
 */
public class CustomAlertDialog extends Dialog {

    public CustomAlertDialog(Context context) {
        super(context);
    }

    public CustomAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context mContext;
        private String title;
        private String message;
        private boolean cancelable;

        // 对话框中间加载的其他布局界面
        private View contentView;

        // 按钮名称
        private String confirm_btnText;
        private String cancel_btnText;
        private String neutral_btnText;

        // 按钮监听事件
        private OnClickListener confirm_btnClickListener;
        private OnClickListener cancel_btnClickListener;
        private OnClickListener neutral_btnClickListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = title <= 0 ? null : (String) mContext.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = message <= 0 ? null : (String) mContext
                    .getText(message);
            return this;
        }

        /**
         * Set the Dialog message from String
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Click settings outside if you can cancel
         *
         * @param cancelable
         * @return
         */
        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
         * settings dialog view
         *
         * @param v View
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param confirm_btnText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int confirm_btnText,
                                         OnClickListener listener) {
            this.confirm_btnText = confirm_btnText <= 0 ? null
                    : (String) mContext.getText(confirm_btnText);
            this.confirm_btnClickListener = listener;
            return this;
        }

        /**
         * Set the positive button and it's listener
         *
         * @param confirm_btnText
         * @return
         */
        public Builder setPositiveButton(String confirm_btnText,
                                         OnClickListener listener) {
            this.confirm_btnText = confirm_btnText;
            this.confirm_btnClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @return
         */
        public Builder setNegativeButton(int cancel_btnText,
                                         OnClickListener listener) {
            this.cancel_btnText = cancel_btnText <= 0 ? null
                    : (String) mContext.getText(cancel_btnText);
            this.cancel_btnClickListener = listener;
            return this;
        }

        /**
         * Set the negative button and it's listener
         *
         * @return
         */
        public Builder setNegativeButton(String cancel_btnText,
                                         OnClickListener listener) {
            this.cancel_btnText = cancel_btnText;
            this.cancel_btnClickListener = listener;
            return this;
        }

        /**
         * Set the netural button resource and it's listener
         *
         * @return
         */
        public Builder setNeutralButton(int neutral_btnText,
                                        OnClickListener listener) {
            this.neutral_btnText = neutral_btnText <= 0 ? null
                    : (String) mContext.getText(neutral_btnText);
            this.neutral_btnClickListener = listener;
            return this;
        }

        /**
         * Set the netural button and it's listener
         *
         * @return
         */
        public Builder setNeutralButton(String neutral_btnText,
                                        OnClickListener listener) {
            this.neutral_btnText = neutral_btnText;
            this.neutral_btnClickListener = listener;
            return this;
        }

        /**
         * Create dialog
         *
         * @return
         */
        @SuppressLint({"InflateParams", "WrongViewCast"})
        public CustomAlertDialog create() {
            // instantiate the dialog with the custom Theme
            final CustomAlertDialog dialog = new CustomAlertDialog(mContext,
                    R.style.custom_dialog_base_style);
            View layout = LayoutInflater.from(mContext).inflate(
                    R.layout.custom_alert_dialog_layout, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            TextView titleTextView = (TextView) layout.findViewById(R.id.title);
            TextView msgTextView = (TextView) layout.findViewById(R.id.message);
            Button neutralBtn = (Button) layout.findViewById(R.id.neutral_btn);
            Button confirmBtn = (Button) layout.findViewById(R.id.confirm_btn);
            Button cancelBtn = (Button) layout.findViewById(R.id.cancel_btn);

            // set title
            if (title == null || title.trim().length() == 0) {
                titleTextView.setText("");
                titleTextView.setVisibility(View.GONE);
            } else {
                titleTextView.setVisibility(View.VISIBLE);
                titleTextView.setText(title);
                // Set the text in bold type
                titleTextView.getPaint().setFakeBoldText(true);
            }

            // set the contentView to the dialog body
            if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.message))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.message)).addView(
                        contentView, new LayoutParams(
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT));
            } else {
                // set title
                if (message == null || message.trim().length() == 0) {
                    msgTextView.setVisibility(View.GONE);
                } else {
                    msgTextView.setVisibility(View.VISIBLE);
                    msgTextView.setText(message);
                }
            }

            // 如果neutral_btnText、confirm_btnText、cancel_btnText都不为空
            if (neutral_btnText != null && confirm_btnText != null
                    && cancel_btnText != null) {
                neutralBtn.setText(neutral_btnText);
                if (neutral_btnClickListener != null) {
                    neutralBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            neutral_btnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEUTRAL);
                        }
                    });
                } else {
                    neutralBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                // if no confirm button or cancle button or neutral just set the
                // visibility to GONE
                neutralBtn.setVisibility(View.GONE);
                layout.findViewById(R.id.single_line).setVisibility(View.GONE);
            }
            // 设置确定按钮
            if (confirm_btnText != null) {
                confirmBtn.setText(confirm_btnText);
                if (confirm_btnClickListener != null) {
                    confirmBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            confirm_btnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                } else {
                    confirmBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                confirmBtn.setVisibility(View.GONE);
                layout.findViewById(R.id.second_line).setVisibility(View.GONE);
                cancelBtn
                        .setBackgroundResource(R.drawable.shape_bottom_rounded_selector);
            }
            // 设置取消按钮
            if (cancel_btnText != null) {
                cancelBtn.setText(cancel_btnText);
                if (cancel_btnClickListener != null) {
                    cancelBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            cancel_btnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                } else {
                    cancelBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                // if no cancel button just set the visibility to GONE
                cancelBtn.setVisibility(View.GONE);
                layout.findViewById(R.id.second_line).setVisibility(View.GONE);
                confirmBtn
                        .setBackgroundResource(R.drawable.shape_bottom_rounded_selector);
            }
            dialog.setCancelable(cancelable);
            dialog.setContentView(layout);
            return dialog;
        }
    }
}