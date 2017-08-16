package com.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dialog.NoInternetDialog;
import com.dialog.ShowDecilineDialog;
import com.dialog.ShowSplitPopUpOnConditions;
import com.posimplicity.service.BTDejavooService;
import com.posimplicity.service.DejavoDebitCreditOption;
import com.posimplicity.service.DejavoDebitCreditOption.YesNoCallBack;
import com.Beans.CheckOutParentModel;
import com.Beans.ExtraProductArgument;
import com.Beans.ProductModel;
import com.Beans.RelationalOptionModel;
import com.CCardPayment.CallSwipeGateWay;
import com.CCardPayment.OfflinePayment;
import com.CustomControls.ToastHelper;
import com.Dialogs.AssignmentPopUp;
import com.Dialogs.CardInfoDialog;
import com.Dialogs.CustomerAssociationPopUp;
import com.Dialogs.ShowCommentDailog;
import com.posimplicity.gateway.DejavoGateway;
import com.posimplicity.gateway.DejavoGatewayBT;
import com.posimplicity.gateway.NobleGatway;
import com.posimplicity.gateway.NobleGatway.NobleResponse;
import com.PosInterfaces.PrefrenceKeyConst;
import com.utils.CalculateWidthAndHeigth;
import com.utils.CheckCardInfo;
import com.utils.HideSoftKeyBoardFromScreen;
import com.utils.InternetConnectionDetector;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.posimplicity.R;
import com.posimplicity.TenderActivity;

import java.util.ArrayList;

public class CreditFragment extends BaseFragment implements TextWatcher, NobleResponse {

    public EditText infoOnSwipeEditText, nobleCCNumberET, tipAmtEditText;
    public TextView totalAmuntTextView, surchargeAmountTV;
    public Button useManualTrans, offlineTransc, dejavoBtn;
    public LinearLayout surchargePanel;
    public TenderActivity mainActivity;
    public float ccSubTotalAmt = 0.0f;
    public String sixDigitCardNumber, cardInformation;
    public CheckCardInfo ccCardInfo;
    public Handler handler;
    public static final String PAYMENT_MODE = "ccsave";
    public static String ORDER_STATUS = "complete";
    public static final int NUMBER_OF_RECEIPT = 2;
    private NobleGatway nobleGateway;
    private Toast pleaseWaitToast;
    private TimerTask timerTask;
    private final int CLERK_ASSIGN_DIALOG = 0;
    public long gatewayUsed = -1L;
    public boolean encryptionEnabled = false;
    public static CreditFragment localInstanceOFCCFragment;
    public float tipAmount = 0.0f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MyPreferences.getLongPreference(POS_STORE_TYPE, mContext) == MaintFragmentOtherSetting.QUICK_)
            ORDER_STATUS = "pending";

        handler = new Handler();
        mainActivity = (TenderActivity) activity;
        gatewayUsed = MyPreferences.getLongPreferenceWithDiffDefValue(GATEWAY_USED_POSITION, mContext);
        encryptionEnabled = MyPreferences.getBooleanPrefrences(ENCRYPTED_PAY_ENABLE, mContext);
        localInstanceOFCCFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_credit_card_payment, null);

        nobleCCNumberET = findViewIdAndCast(R.id.cardNumbera);
        useManualTrans = findViewIdAndCast(R.id.chooseManualTransaction);
        offlineTransc = findViewIdAndCast(R.id.chooseOfflineTransaction);
        dejavoBtn = findViewIdAndCast(R.id.dejavooo);
        totalAmuntTextView = findViewIdAndCast(R.id.subtotaltv);
        surchargeAmountTV = findViewIdAndCast(R.id.payAmount);
        infoOnSwipeEditText = findViewIdAndCast(R.id.cardinfoedittext);
        surchargePanel = findViewIdAndCast(R.id.surchargeLayout);
        tipAmtEditText = findViewIdAndCast(R.id.tipAmtEditText);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customToast();
        setUiVisibility();
        infoOnSwipeEditText.addTextChangedListener(this);
        onUpadte();
    }

    private void setUiVisibility() {

        if (gatewayUsed >= 0) {
            offlineTransc.setVisibility(View.GONE);
            surchargePanel.setVisibility(View.INVISIBLE);
            useManualTrans.setVisibility(View.VISIBLE);
            dejavoBtn.setVisibility(View.GONE);

            if (gatewayUsed == MaintFragmentCCAdmin.DEJAVO_PAY_ID) {
                useManualTrans.setVisibility(View.GONE);
                dejavoBtn.setVisibility(View.VISIBLE);
            }
            /*else if(gatewayUsed == MaintFragmentCCAdmin.NOBLES_PAY_ID){
				surchargePanel.setVisibility(View.VISIBLE);				
			}*/
        }

        if (MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.NOBLE_ON_OFF, mContext))
            surchargePanel.setVisibility(View.VISIBLE);
    }


    public void showCcSwipeMessage() {

        if (gatewayUsed >= 0) {

            if (!(gatewayUsed == MaintFragmentCCAdmin.DEJAVO_PAY_ID)) {
                ToastHelper.showCCSwipeToast(mContext);
            }
        }
    }

    private void customToast() {
        timerTask = new TimerTask(20000, 1000);
        pleaseWaitToast = Toast.makeText(mContext, "Processing Card ! Please wait a while...", Toast.LENGTH_SHORT);

    }

    public void onShowCustomToast() {
        timerTask.start();
        pleaseWaitToast.show();
    }

    public void onHideCustomToast() {
        pleaseWaitToast.cancel();
        timerTask.cancel();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }


    @Override
    public void afterTextChanged(final Editable s) {

        if (s.length() > 0) {

            if (s.length() == 1) {
                onShowCustomToast();
            }

            if (delayedAction != null)
                handler.removeCallbacks(delayedAction);

            handler.postDelayed(delayedAction, 1500);
        }
    }

    Runnable delayedAction = new Runnable() {

        @Override
        public void run() {

            onHideCustomToast();
            Boolean isInternetPresent = InternetConnectionDetector.isInternetAvailable(mContext);
            if (isInternetPresent) {
                cardInformation = infoOnSwipeEditText.getText().toString();
                if (encryptionEnabled) {
                    new CallSwipeGateWay(mContext, cardInformation, CreditFragment.this).execute();
                } else {
                    boolean correct = checkCcCredentials();
                    if (correct) {
                        new CallSwipeGateWay(mContext, CreditFragment.this, ccCardInfo).execute();
                    } else {
                        clearCardInfoFieldAndSetFocusAgain();
                        ShowDecilineDialog.onWrongInput(mContext);
                    }
                }
            } else
                NoInternetDialog.noInternetDialogShown(mContext);
        }
    };


    protected boolean checkCcCredentials() {
        ccCardInfo = new CheckCardInfo(mContext, cardInformation);
        return ccCardInfo.validationOfCard();
    }

    public void onUpadte() {
        findCreditCardPaymentAmount();
        //if(gatewayUsed == MaintFragmentCCAdmin.NOBLES_PAY_ID){

        if (MyPreferences.getBooleanPrefrences(PrefrenceKeyConst.NOBLE_ON_OFF, mContext)) {
            if (mainActivity.leavedThisFragment) {
                surchargePanel.setVisibility(View.INVISIBLE);
                useManualTrans.setVisibility(View.VISIBLE);
                showCcSwipeMessage();
            } else {
                surchargePanel.setVisibility(View.VISIBLE);
            }
        } else {
            surchargePanel.setVisibility(View.INVISIBLE);
            if (!mainActivity.leavedThisFragment) {
                showCcSwipeMessage();
                clearCardInfoFieldAndSetFocusAgain();
            }
        }
        totalAmuntTextView.setText(MyStringFormat.formatWith2DecimalPlaces(ccSubTotalAmt));
        surchargeAmountTV.setText(MyStringFormat.formatWith2DecimalPlaces(Variables.subChargeAmount));
        //HideSoftKeyBoardFromScreen.hideSoftKeyboard(mContext, infoOnSwipeEditText);
    }

    public void clearCardInfoFieldAndSetFocusAgain() {
        infoOnSwipeEditText.requestFocus(R.id.cardinfoedittext);
        infoOnSwipeEditText.setText("");
    }

    public void onSurchargeUpdate() {
        mainActivity.leavedThisFragment = true;
        surchargeAmountTV.setText(MyStringFormat.formatWith2DecimalPlaces(Variables.subChargeAmount));
    }

    public void findCreditCardPaymentAmount() {

        ccSubTotalAmt = Variables.totalBillAmount - Variables.cashAmount - Variables.rewardsAmount - Variables.giftAmount - Variables.ccAmount;
    }

    public void onCreditFragmentClick(View v) {
        try {
            switch (v.getId()) {

                case R.id.submit:

                    HideSoftKeyBoardFromScreen.onHideSoftKeyBoard(mContext, nobleCCNumberET);
                    sixDigitCardNumber = nobleCCNumberET.getText().toString().trim();
                    if (TextUtils.isEmpty(sixDigitCardNumber))
                        nobleCCNumberET.setError("Please Enter CardNumber First");
                    else {
                        if (sixDigitCardNumber.length() != 6) {
                            nobleCCNumberET.setError("Please Enter First Six Digit's Of Card");
                        } else {

                            if (sixDigitCardNumber.startsWith("5") || sixDigitCardNumber.startsWith("4")) {
                                nobleGateway = new NobleGatway(mContext, sixDigitCardNumber, String.valueOf(ccSubTotalAmt));
                                nobleGateway.setInterface(this);
                                nobleGateway.onExecute();
                            } else
                                nobleCCNumberET.setError("Invalid Card Info");
                        }
                    }
                    break;

                case R.id.customerDialog:

                    int width1 = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth(), 70);
                    int height1 = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(), 80);
                    new CustomerAssociationPopUp(mContext, R.style.myCoolDialog, width1, height1, false, true, R.layout.dialog_assign_trans).show();


                    break;

                case R.id.assingClerk:

                    int width2 = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth(), 70);
                    int height2 = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(), 80);
                    new AssignmentPopUp(mContext, R.style.myCoolDialog, width2, height2, false, true, R.layout.dialog_assign_trans).show(CLERK_ASSIGN_DIALOG);

                    break;

                case R.id.chooseManualTransaction:

                    if (gatewayUsed == MaintFragmentCCAdmin.TSYS_PAY_ID) {
                        try {
                            tipAmount = Float.parseFloat(tipAmtEditText.getText().toString());
                        } catch (Exception ex) {
                            tipAmtEditText.setError("Enter Valid Tip Amount");
                            return;
                        }
                        if (tipAmount > ccSubTotalAmt) {
                            tipAmtEditText.setError("Tip Amount Must Be Less Or Equal To Total Amount");
                            return;
                        }
                    }

                    int width = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth(), 60);
                    int height = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(), 60);

                    CardInfoDialog cardInfoDialog = new CardInfoDialog(mContext, R.style.myCoolDialog, width, height, false, false, R.layout.dialog_card_info);
                    cardInfoDialog.show();

                    break;

                case R.id.chooseOfflineTransaction:
                    new OfflinePayment(mContext).onExecute();

                    break;

                case R.id.btnCancelCC:
                    ((Activity) mContext).finish();
                    break;

                case R.id.dejavooo:


                    final String ipAddress = MyPreferences.getMyPreference(DEJAVOO_IP_ADDRESS, mContext);
                    final long selectedOption = MyPreferences.getLongPreference(DEJAVO_OPTION, mContext);
                    if (selectedOption >= 0) {

                        if (DejavoDebitCreditOption.isDejavoPromptEnable(mContext)) {
                            DejavoDebitCreditOption.showDejavoOptionListDialog(new YesNoCallBack() {

                                @Override
                                public void onYes(String string) {

                                    if (MyPreferences.getBooleanPrefrences(DEJAVO_PAYMENT_VIA_BLUETOOTH, mContext)) {
                                        Variables.dejavoPaymentAmt = ccSubTotalAmt;
                                        DejavoGatewayBT dejavoGateway = new DejavoGatewayBT(mContext, Variables.dejavoPaymentAmt, MyPreferences.getLongPreference(DEJAVO_OPTION, mContext));
                                        dejavoGateway.execute();
                                    } else
                                        new DejavoGateway(mContext, ipAddress, ccSubTotalAmt, selectedOption, CreditFragment.this).execute();
                                }

                                @Override
                                public void onNo() {
                                }

                            }, mContext);
                        } else {
                            if (MyPreferences.getBooleanPrefrences(DEJAVO_PAYMENT_VIA_BLUETOOTH, mContext)) {
                                if (BTDejavooService.isBtConnectionAvailable()) {
                                    Variables.dejavoPaymentAmt = ccSubTotalAmt;
                                    DejavoGatewayBT dejavoGateway = new DejavoGatewayBT(mContext, Variables.dejavoPaymentAmt, MyPreferences.getLongPreference(DEJAVO_OPTION, mContext));
                                    dejavoGateway.execute();
                                } else
                                    ToastUtils.showOwnToast(mContext, "Device Not Connected.Trying to Connect...");
                            } else
                                new DejavoGateway(mContext, ipAddress, ccSubTotalAmt, selectedOption, CreditFragment.this).execute();
                        }
                    } else
                        ToastUtils.showOwnToast(mContext, "Select Any Option For Dejavoo");

                    break;

                case R.id.splitDialog:

                    int width3 = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth(), 35);
                    int height3 = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(), 45);
                    new ShowSplitPopUpOnConditions(mContext, width3, height3, ccSubTotalAmt, PAYMENT_MODE, ORDER_STATUS).letShow();
                    break;

                case R.id.commentBtnCreditPayment:

                    int width4 = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth(), 60);
                    int height4 = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(), 70);
                    new ShowCommentDailog(mContext, R.style.myCoolDialog, width4, height4, false, true, R.layout.dialog_comment_for_order).show();


                    break;

                default:
                    break;
            }

        } catch (Exception e) {
            ShowDecilineDialog.onSHowErroeMsg(mContext, e);
        }
    }

    @Override
    public void onNobleRespose(String adjustmentAmt) {
        try {
            Variables.subChargeAmount = Float.parseFloat(adjustmentAmt);
            CheckOutParentModel parent = new CheckOutParentModel(new ProductModel("Credit Card Surcharge", "" + Variables.subChargeAmount), new ArrayList<RelationalOptionModel>(), new ExtraProductArgument(true, false));

            boolean isExist = localInsatnceOfHome.dataList.contains(parent);
            if (isExist) {
                CheckOutParentModel localParent = localInsatnceOfHome.dataList.get(localInsatnceOfHome.dataList.indexOf(parent));
                if (localParent != null) {
                    String productPrice = "" + (Float.parseFloat(localParent.getProduct().getProductPrice()) + Variables.subChargeAmount);
                    localParent.getProduct().setProductPrice(productPrice);
                }
            } else
                localInsatnceOfHome.dataList.add(0, parent);

            localInsatnceOfHome.myAdapter.notifyDataSetChanged();
            localInsatnceOfHome.calculateSubTotalEachTime();

            onSurchargeUpdate();
            clearCardInfoFieldAndSetFocusAgain();
            showCcSwipeMessage();

            surchargePanel.setVisibility(View.INVISIBLE);

            if (gatewayUsed >= 0) {

                if (gatewayUsed != MaintFragmentCCAdmin.DEJAVO_PAY_ID)
                    useManualTrans.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class TimerTask extends CountDownTimer {

        public TimerTask(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            pleaseWaitToast.show();
        }

        @Override
        public void onFinish() {
            pleaseWaitToast.show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (timerTask != null) {
            timerTask.cancel();
        }
    }
}
