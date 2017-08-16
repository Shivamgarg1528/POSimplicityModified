package com.CustomControls;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.posimplicity.R;

public class ToastHelper {

	public static void showApprovedToast(Context pContext) {
		Toast imageToast = new Toast(pContext.getApplicationContext());
        LinearLayout toastLayout = new LinearLayout(pContext);
        toastLayout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView image = new ImageView(pContext);
        image.setImageResource(R.drawable.approved);
        toastLayout.addView(image);
        imageToast.setGravity(Gravity.CENTER, 0, 0);
        imageToast.setView(toastLayout);
        imageToast.setDuration(Toast.LENGTH_SHORT);
        imageToast.show();
    }

    public static void showDeclineToast(Context pContext)
    {
        Toast imageToast = new Toast(pContext);
        LinearLayout toastLayout = new LinearLayout(pContext);
        toastLayout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView image = new ImageView(pContext);
        image.setImageResource(R.drawable.carddecline);
        toastLayout.addView(image);
        imageToast.setGravity(Gravity.CENTER, 0, 0);
        imageToast.setView(toastLayout);
        imageToast.setDuration(Toast.LENGTH_SHORT);
        imageToast.show();
    }
	
	public static void showCCSwipeToast(Context context) {
		Toast ImageToast = new Toast(context);
        LinearLayout toastLayout = new LinearLayout(context);
        toastLayout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView image = new ImageView(context);
        image.setImageResource(R.drawable.swipecard_toast_bg);
        toastLayout.addView(image);
        ImageToast.setGravity(Gravity.CENTER, 0, 0);
        ImageToast.setView(toastLayout);
        ImageToast.setDuration(Toast.LENGTH_SHORT);
        ImageToast.show();
    }
}
