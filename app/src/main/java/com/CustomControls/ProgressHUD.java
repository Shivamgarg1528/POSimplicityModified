package com.CustomControls;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;

public class ProgressHUD extends ProgressDialog {

    public ProgressHUD(@NonNull Context context) {
        super(context);
    }

    public static ProgressHUD show(Context context, CharSequence message, boolean indeterminate, boolean cancelable, OnCancelListener cancelListener) {
        ProgressHUD dialog = new ProgressHUD(context);
        dialog.setTitle("");
        if (message != null) {
            dialog.setMessage(message);
        }
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(cancelListener);
        dialog.setIndeterminate(indeterminate);
        dialog.show();
        return dialog;
    }
}
