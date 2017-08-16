package com.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.AsyncTasks.CompleteReportInMagento;
import com.Beans.CustomerModel;
import com.posimplicity.database.CustomerTable;
import com.Dialogs.ShowCommentDailog;
import com.RecieptPrints.KitchenReceipt;
import com.RecieptPrints.PrintReceiptCustomer;
import com.RecieptPrints.PrintSettings;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.UsbPR;
import com.utils.CalculateWidthAndHeigth;
import com.utils.ToastUtils;
import com.utils.Variables;
import com.posimplicity.R;

public class PendingFragment extends BaseFragment implements OnClickListener, TextWatcher {

    private Button saveBtn, printKitchenReceipt, tableAssign, printCustomerReceipt, commentBt, clerkBtn;
    private ImageButton cancelBtn;
    public static final String PAYMENT_MODE = "checkmo";
    public static final String ORDER_STATUS = Variables.orderStatus;
    private EditText tableIdEdtText, clerkIdEdtText;
    private TextView clerkTv, tableTv;
    private CustomerModel tableCustomer, clerkCustomer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_pending_orders, null);
        saveBtn = findViewIdAndCast(R.id.Fragment_Pending_Order_Btn_Save);
        printKitchenReceipt = findViewIdAndCast(R.id.Fragment_Pending_Order_Btn_Save_And_Print_Kitchen_Receipt);
        printCustomerReceipt = findViewIdAndCast(R.id.Fragment_Pending_Order_Btn_Save_And_Print_Customer_Receipt);
        cancelBtn = findViewIdAndCast(R.id.Fragment_Pending_Order_Btn_Cancel);
        tableAssign = findViewIdAndCast(R.id.Fragment_Pending_Order_Btn_Table_Assign);
        commentBt = findViewIdAndCast(R.id.Fragment_Pending_Order_Btn_Comment);
        clerkBtn = findViewIdAndCast(R.id.Fragment_Pending_Order_Btn_Clerk_Assign);
        tableIdEdtText = findViewIdAndCast(R.id.Fragment_Pending_Order_Edt_TableId);
        clerkIdEdtText = findViewIdAndCast(R.id.Fragment_Pending_Order_Edt_ClerkId);
        clerkTv = findViewIdAndCast(R.id.Fragment_Pending_Order_TV_ClerId);
        tableTv = findViewIdAndCast(R.id.Fragment_Pending_Order_TV_TableId);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        saveBtn.setOnClickListener(this);
        printKitchenReceipt.setOnClickListener(this);
        printCustomerReceipt.setOnClickListener(this);
        tableAssign.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        commentBt.setOnClickListener(this);
        clerkBtn.setOnClickListener(this);

        if (PrintSettings.canPrintWifiSlip(mContext)) {
            printKitchenReceipt.setVisibility(View.VISIBLE);
        }

        if (PrintSettings.isAbleToPrintKitchenReceiptThroughBluetooth(mContext)) {
            printKitchenReceipt.setVisibility(View.VISIBLE);
        }
        if (PrintSettings.isAbleToPrintKitchenReceiptThroughUsb(mContext)) {
            printKitchenReceipt.setVisibility(View.VISIBLE);
        }


        if (PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)) {
            printCustomerReceipt.setVisibility(View.VISIBLE);
        }

        if (PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)) {
            printCustomerReceipt.setVisibility(View.VISIBLE);
        }


        clerkIdEdtText.addTextChangedListener(this);
        tableIdEdtText.addTextChangedListener(this);

        if (Variables.tableOrClerkShipToNameModel != null && !Variables.tableOrClerkShipToNameModel.isCustomerNotValid()) {
            tableTv.setText(Variables.tableOrClerkShipToNameModel.getFirstName());
            Variables.shipToName = Variables.tableOrClerkShipToNameModel.getEmailAddress();
            tableIdEdtText.setText(Variables.tableOrClerkShipToNameModel.getTelephoneNo());
        }

        if (Variables.customerOrClerkBillToNameModel != null && !Variables.customerOrClerkBillToNameModel.isCustomerNotValid()) {
            clerkTv.setText(Variables.customerOrClerkBillToNameModel.getFirstName());
            Variables.billToName = Variables.customerOrClerkBillToNameModel.getEmailAddress();
            clerkIdEdtText.setText(Variables.customerOrClerkBillToNameModel.getTelephoneNo());
        }
    }

    @Override
    public void onClick(View v) {

        String text = "";
        switch (v.getId()) {
            case R.id.Fragment_Pending_Order_Btn_Clerk_Assign:

                if (!clerkTv.getText().equals("Invalid Information") && clerkCustomer != null) {
                    Variables.customerId = Integer.parseInt(clerkCustomer.getCustomerId());
                    Variables.billToName = clerkCustomer.getEmailAddress();
                    Variables.customerName = clerkCustomer.getFirstName();
                    ToastUtils.showOwnToast(mContext, "Clerk Assigned Successfully");
                } else
                    clerkIdEdtText.setError("Please Assign Clerk First");
                break;

            case R.id.Fragment_Pending_Order_Btn_Table_Assign:

                if (!tableTv.getText().equals("Invalid Information") && tableCustomer != null) {
                    Variables.shipToName = tableCustomer.getEmailAddress();
                    Variables.tableID = tableTv.getText().toString();
                    ToastUtils.showOwnToast(mContext, "Table Assigned Successfully");
                } else
                    tableIdEdtText.setError("Please Assign Table First");

                break;

            case R.id.Fragment_Pending_Order_Btn_Cancel:
                ((Activity) mContext).finish();
                break;

            case R.id.Fragment_Pending_Order_Btn_Save:
                text = "Submit To " + ((Button) v).getText().toString() + "!";
                okTOGoSimpleUSBWifiBluetoothPrinting(mContext, text, 0);
                break;

            case R.id.Fragment_Pending_Order_Btn_Save_And_Print_Customer_Receipt:
                text = "Submit To " + ((Button) v).getText().toString() + "!";
                okTOGoSimpleUSBWifiBluetoothPrinting(mContext, text, 1);
                break;

            case R.id.Fragment_Pending_Order_Btn_Save_And_Print_Kitchen_Receipt:
                text = "Submit To " + ((Button) v).getText().toString() + "!";
                okTOGoSimpleUSBWifiBluetoothPrinting(mContext, text, 2);
                break;

            case R.id.Fragment_Pending_Order_Btn_Comment:

                int width3 = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceWidth(), 60);
                int height3 = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApp.getDeviceHeight(), 70);
                new ShowCommentDailog(mContext, R.style.myCoolDialog, width3, height3, false, true, R.layout.dialog_comment_for_order).show();

                break;


            default:
                break;
        }
    }

    public void okTOGoSimpleUSBWifiBluetoothPrinting(final Context mContext, String msg, final int status) {

        clerkIdEdtText.getText().toString().trim();
        Variables.customerId = 0;

        if (Variables.shipToName.isEmpty()) {
            tableIdEdtText.setError("Please Assign Table First");
            return;
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.drawable.app_icon);
            builder.setMessage(msg);
            builder.setCancelable(false);

            builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new CompleteReportInMagento(mContext, ORDER_STATUS, PAYMENT_MODE, false).execute();
                    switch (status) {

                        case 0:
                            onCallAfterPrintRequest();
                            break;

                        case 1:
                            if (PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)) {
                                new UsbPR(mContext, new PrinterCallBack() {

                                    @Override
                                    public void onDisconnected() {
                                        onCallAfterPrintRequest();
                                    }

                                    @Override
                                    public void onConnected(BasePR pPrinter) {
                                        PrintReceiptCustomer printReceiptUSB = new PrintReceiptCustomer(mContext);
                                        printReceiptUSB.onPrintRecieptCustomer(pPrinter, true);
                                        onCallAfterPrintRequest();
                                    }

                                }).onConnectionStart();
                                return;
                            }
                            if (PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)) {
                                PrintReceiptCustomer printReceiptUSB = new PrintReceiptCustomer(mContext);
                                printReceiptUSB.onPrintRecieptCustomer(gApp.getPrinterBT(), true);
                            }
                            onCallAfterPrintRequest();
                            break;

                        case 2:

                            if (PrintSettings.isAbleToPrintKitchenReceiptThroughUsb(mContext)) {
                                new UsbPR(mContext, new PrinterCallBack() {

                                    @Override
                                    public void onDisconnected() {
                                        onCallAfterPrintRequest();
                                    }

                                    @Override
                                    public void onConnected(BasePR pPrinter) {
                                        KitchenReceipt.onPrintKitchenReciept(mContext, localInsatnceOfHome, pPrinter);
                                        onCallAfterPrintRequest();
                                    }
                                });
                                return;
                            }

                            if (PrintSettings.isAbleToPrintKitchenReceiptThroughBluetooth(mContext)) {
                                KitchenReceipt.onPrintKitchenReciept(mContext, localInsatnceOfHome, gApp.getPrinterBT());
                            }

                            if (PrintSettings.canPrintWifiSlip(mContext)) {
                                KitchenReceipt.onPrintKitchenReciept(mContext, localInsatnceOfHome, gApp.getPrinterWF());
                            }
                            onCallAfterPrintRequest();

                            break;

                        default:
                            break;
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
            builder.create();
        }
    }

    protected void onCallAfterPrintRequest() {

        localInsatnceOfHome.resetAllData(mContext, 1);
        ((Activity) mContext).finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    private boolean isLong(String integerString) {

        try {
            Long.parseLong(integerString);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private void setCustomerNameCorrespondingToMobileNo(final EditText mobileEditText) {

        String mobileNo = mobileEditText.getText().toString();

        if (isLong(mobileNo)) {
            CustomerTable customerTable = new CustomerTable(mContext);
            switch (mobileEditText.getId()) {

                case R.id.Fragment_Pending_Order_Edt_ClerkId:
                    clerkCustomer = customerTable.getSingleInfoFromTableByPhoneNo(mobileNo);

                    if (clerkCustomer == null || clerkCustomer.getCustomerId().isEmpty()) {
                        clerkTv.setText("Invalid Information");
                        return;
                    } else
                        clerkTv.setText(clerkCustomer.getFirstName());
                    break;

                case R.id.Fragment_Pending_Order_Edt_TableId:

                    tableCustomer = customerTable.getSingleInfoFromTableByPhoneNo(mobileNo);

                    if (tableCustomer == null || tableCustomer.getCustomerId().isEmpty()) {
                        tableTv.setText("Invalid Information");
                        return;
                    } else
                        tableTv.setText(tableCustomer.getFirstName());
                    break;


                default:
                    break;
            }
        }
    }


    @Override
    public void afterTextChanged(Editable s) {
        if (s == clerkIdEdtText.getEditableText()) {
            if (s.length() > 0)
                setCustomerNameCorrespondingToMobileNo(clerkIdEdtText);
            else {
                clerkTv.setText("Invalid Information");
            }

        } else if (s == tableIdEdtText.getEditableText()) {
            if (s.length() > 0)
                setCustomerNameCorrespondingToMobileNo(tableIdEdtText);
            else {
                tableTv.setText("Invalid Information");
            }
        }
    }
}
