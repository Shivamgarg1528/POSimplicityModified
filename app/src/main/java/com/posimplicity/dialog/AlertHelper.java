package com.posimplicity.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.easylibs.utils.EasyUtils;
import com.posimplicity.R;


public class AlertHelper {

    public static AlertDialog getAlertDialog(Context pContext, String pMessage, String pPositiveBtnTxt, String pNegativeBtnTxt, DialogInterface.OnClickListener pListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
        builder.setMessage(pMessage);
        builder.setTitle(pContext.getString(R.string.app_name));
        builder.setIcon(R.drawable.app_icon);
        if (!EasyUtils.isBlank(pNegativeBtnTxt)) {
            builder.setNegativeButton(pNegativeBtnTxt, pListener);
        }

        if (!EasyUtils.isBlank(pPositiveBtnTxt)) {
            builder.setPositiveButton(pPositiveBtnTxt, pListener);
        }
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        return alertDialog;
    }

    public static AlertDialog getNoInternetAlert(Context pContext, String pNegativeBtnTxt, DialogInterface.OnClickListener pListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
        builder.setMessage(pContext.getString(R.string.string_please_check_internet_connection));
        builder.setTitle(pContext.getString(R.string.app_name));
        builder.setIcon(R.drawable.app_icon);
        if (!EasyUtils.isBlank(pNegativeBtnTxt)) {
            builder.setNegativeButton(pNegativeBtnTxt, pListener);
        }
        builder.setPositiveButton("Retry", pListener);
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        return alertDialog;
    }

    public static void showProductOptionDialog(Context pContext, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
        builder.setCancelable(false);

        View view = LayoutInflater.from(pContext).inflate(R.layout.dialog_product_option_item, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
