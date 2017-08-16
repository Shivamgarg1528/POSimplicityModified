package com.posimplicity.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.CustomControls.ToastHelper;
import com.craftman.cardform.Card;
import com.easylibs.listener.EventListener;
import com.posimplicity.R;

public class SwipeCardDialog extends BaseAlert implements TextWatcher, Runnable, DialogInterface.OnCancelListener {

    private Context mContext;
    private View mParentView;
    private EditText mEditCardInfo;
    private boolean mDialogDismissed;
    private EventListener mEventListener;

    public SwipeCardDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_card_swipe_input);

        mParentView = findViewById(R.id.dialog_card_swipe_input_ll_);

        mEditCardInfo = (EditText) findViewById(R.id.dialog_card_swipe_input_edt_card_info);
        mEditCardInfo.addTextChangedListener(this);

        setFocusAgain();
        onDimnessDisable();

        mEditCardInfo.setText("1");
        setOnCancelListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0 && s.length() < 100) {
            mParentView.setVisibility(View.VISIBLE);
            mEditCardInfo.removeCallbacks(this);
            mEditCardInfo.postDelayed(this, 200);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Editable text = mEditCardInfo.getText();
                    mEditCardInfo.setText(text.toString().concat("1"));
                }
            }, 100);
        }
    }

    @Override
    public void run() {
        if (!mDialogDismissed) {
            mParentView.setVisibility(View.INVISIBLE);
            String magStripCardData = mEditCardInfo.getText().toString();
            this.mEventListener.onEvent(-1, magStripCardData);
            dismiss();
        }
    }

    private void setFocusAgain() {
        mEditCardInfo.requestFocus(R.id.dialog_card_swipe_input_edt_card_info);
        mEditCardInfo.setText("");
        ToastHelper.showCCSwipeToast(mContext);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mDialogDismissed = true;
        mEditCardInfo.removeTextChangedListener(this);
    }

    public void show(EventListener pEventListener) {
        this.mEventListener = pEventListener;
        show();
    }
}
