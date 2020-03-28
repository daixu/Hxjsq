package com.shangame.hxjsq.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.hxjsq.R;

/**
 * Created by 32422 on 2017/10/14.
 */

public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (null != window) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setAttributes(layoutParams);
        }
    }

    public static class Builder {

        private Context context;
        private CharSequence messageText;
        private int positiveResId = 0, negativeResId = 0;

        private OnClickListener positiveButtonListener, negativeButtonListener,
                defaultListener = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.out.println("i:" + i);
                dialogInterface.dismiss();
            }
        };

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(CharSequence sequence) {
            this.messageText = sequence;
            return this;
        }

        public Builder setMessage(int strId) {
            this.messageText = context.getText(strId);
            return this;
        }

        public Builder setPositiveButton(OnClickListener listener) {
            this.positiveButtonListener = listener == null ? defaultListener
                    : listener;
            return this;
        }

        public Builder setPositiveButton(int resId, OnClickListener listener) {
            this.positiveResId = resId;
            this.positiveButtonListener = listener == null ? defaultListener
                    : listener;
            return this;
        }

        public Builder setNegativeButton(int resId, OnClickListener listener) {
            this.negativeResId = resId;
            this.negativeButtonListener = listener == null ? defaultListener
                    : listener;
            return this;
        }

        public Builder setNegativeButton(OnClickListener listener) {
            this.negativeButtonListener = listener == null ? defaultListener
                    : listener;
            return this;
        }

        public CustomDialog create() {
            final CustomDialog dialog = new CustomDialog(context,
                    R.style.CustomDialogStyle);
            LayoutInflater inflater = LayoutInflater.from(context);
            View contentView = inflater.inflate(R.layout.custom_dialog, null);
            dialog.addContentView(contentView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView messageView = contentView.findViewById(R.id.tv_dialogMsg);
            ImageView positiveButton = contentView.findViewById(R.id.btn_positive);
            ImageView negativeButton = contentView.findViewById(R.id.btn_negative);

            if (TextUtils.isEmpty(messageText)) {
                messageView.setVisibility(View.GONE);
            } else {
                messageView.setVisibility(View.VISIBLE);
                messageView.setText(messageText);
            }

            ((View) (positiveButton.getParent().getParent())).setVisibility(View.VISIBLE);
            if (positiveResId == 0) {
                positiveButton.setVisibility(View.GONE);
                // positiveButton.setImageResource(R.drawable.img_confirm_btn);
            } else {
                positiveButton.setVisibility(View.VISIBLE);
                positiveButton.setImageResource(positiveResId);
            }
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    positiveButtonListener.onClick(dialog,
                            DialogInterface.BUTTON_POSITIVE);
                }
            });

            if (negativeResId == 0) {
                negativeButton.setVisibility(View.GONE);
                // negativeButton.setImageResource(R.drawable.img_clear_btn);
            } else {
                negativeButton.setVisibility(View.VISIBLE);
                negativeButton.setImageResource(negativeResId);
            }
            negativeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    negativeButtonListener.onClick(dialog,
                            DialogInterface.BUTTON_NEGATIVE);
                }
            });

            dialog.setContentView(contentView);
            return dialog;
        }
    }
}
