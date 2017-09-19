package com.posimplicity.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.CustomControls.ToastHelper;
import com.easylibs.listener.EventListener;
import com.posimplicity.R;
import com.posimplicity.model.local.CardInfoModel;
import com.utils.CardHelper;

public class SwipeCardDialog extends BaseAlert implements TextWatcher, Runnable, DialogInterface.OnCancelListener {

    private final Context mContext;
    private final boolean mCreditEncryptionEnable;
    private final EventListener mEventListener;

    private View mParentView;
    private EditText mEditCardInfo;
    private boolean mDialogDismissed;

    public SwipeCardDialog(@NonNull Context pContext, boolean pCreditEncryptionEnable, EventListener pEventListener) {
        super(pContext);
        mContext = pContext;
        mCreditEncryptionEnable = pCreditEncryptionEnable;
        mEventListener = pEventListener;
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
        if (s.length() > 0) {
            mParentView.setVisibility(View.VISIBLE);
            mEditCardInfo.removeCallbacks(this);
            mEditCardInfo.postDelayed(this, 200);
        }
    }

    @Override
    public void run() {
        if (!mDialogDismissed) {
            mParentView.setVisibility(View.INVISIBLE);
            String cardInfo = mEditCardInfo.getText().toString();
            CardInfoModel cardInfoModel = new CardInfoModel();
            if (!mCreditEncryptionEnable) {
                CardHelper cardHelper = new CardHelper(cardInfo);
                if (cardHelper.parseNonEncryptedCardInfo()) {
                    cardInfoModel.setCardHolderName(cardHelper.getCardHolderName());
                    cardInfoModel.setCardExpYear(cardHelper.getCardExpiryYear());
                    cardInfoModel.setCardExpMonth(cardHelper.getCardExpiryMonth());
                    cardInfoModel.setCardNumber(cardHelper.getCardNumber());
                    cardInfoModel.setCardTrack1(cardHelper.getTrackData1());
                    cardInfoModel.setCardTrack2(cardHelper.getTrackData2());
                    cardInfoModel.setCardMagData(cardHelper.getMagStripData());
                    cardInfoModel.setCardTypeFullName(cardHelper.getCcTypeFullName());
                    cardInfoModel.setCardTypeShortName(cardHelper.getCcTypeShortName());
                    mEventListener.onEvent(-1, cardInfoModel);
                    dismiss();
                } else {
                    setFocusAgain();
                }
            } else {
                CardHelper cardHelper = new CardHelper(cardInfo);
                if (cardHelper.parseEncryptedCardInfo()) {
                    cardInfoModel.setCardTrack1(cardHelper.getTrackData1());
                    cardInfoModel.setCardTrack2(cardHelper.getTrackData2());
                    cardInfoModel.setKsn(cardHelper.getKsn());
                    mEventListener.onEvent(-1, cardInfoModel);
                    dismiss();
                } else {
                    setFocusAgain();
                }
            }
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
}
