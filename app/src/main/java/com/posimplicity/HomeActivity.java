package com.posimplicity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.AlertDialogWithOptions.RefundOptionsDialog;
import com.AlertDialogWithOptions.ReportOptionsDailog;
import com.dialog.ClearAllHomeScreenDataDialog;
import com.dialog.LogoutClerkFromOrders;
import com.dialog.LogoutUser;
import com.posimplicity.service.ServiceUtils;
import com.Beans.CategoryModel;
import com.Beans.CheckOutParentModel;
import com.Beans.POSAuthority;
import com.Beans.ProductModel;
import com.Beans.RelationalOptionModel;
import com.CalculationsTask.AmountCalculation;
import com.adapter.AdapterForSearch;
import com.adapter.CheckOutListAdapter;
import com.CustomControls.HorizontalPager;
import com.CustomControls.HorizontalPager.OnScreenSwitchListener;
import com.posimplicity.database.CategoryTable;
import com.posimplicity.database.CustomerTable;
import com.posimplicity.database.ProductTable;
import com.posimplicity.database.local.SecurityTable;
import com.Dialogs.ClerkSalesLoginLogoutDialog;
import com.Dialogs.DiscountDialog;
import com.Dialogs.ProductOptionDialog;
import com.Dialogs.ProductQtyDialog;
import com.Dialogs.RefundItemsWithAmt;
import com.PosBroadcast.AutologoutConnectionClass;
import com.PosBroadcast.SocketConnectionClass;
import com.RecieptPrints.GoForPrint;
import com.Socket.ConvertStringOfJson;
import com.Socket.SocketIO;
import com.Socket.UpdateUserInterface;
import com.utils.CalculateWidthAndHeigth;
import com.utils.CreateControls;
import com.utils.GenerateNewTransactionNo;
import com.utils.POSApp;
import com.utils.HideSoftKeyBoardFromScreen;
import com.utils.LocationOfPosApp;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.ProductClick;
import com.utils.SecurityVerification;
import com.utils.SelectedProduct;
import com.utils.StartAndroidActivity;
import com.utils.ToastUtils;
import com.utils.Variables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeActivity extends BaseActivity implements OnCheckedChangeListener, OnClickListener, OnScreenSwitchListener, OnGroupClickListener, OnItemClickListener, TextWatcher {

    private RadioGroup mRadioGroup;
    private RadioButton[] radioButtons;
    private RelativeLayout parentOfRadioGroup;
    private ExpandableListView itemListControl;
    public TextView subtotalBtn, scoketConnectionTv;
    private MediaPlayer mp;
    private CreateControls controls;
    public HorizontalPager mPager;
    public CategoryTable categoryTable;
    public EditText barcodeScanner;
    public TextView subTotalTv, taxTv, totalTv, amountToPaidTv, changeAmtTv, trasIdTv, dateTimeTv, discountTv, cashTv;
    public LinearLayout productLayout;
    public AutoCompleteTextView searchProductByName;
    public String[] systemKeyArr, sytemKeyTags;
    public ArrayList<CheckOutParentModel> dataList;
    public List<LinearLayout> deptKeysRowList;
    public List<LinearLayout> sysKeysRowList;
    public List<LinearLayout> keysLayoutList;
    public List<ProductModel> productList, allProductList;
    public List<CategoryModel> deptList;
    public List<RelationalOptionModel> optionList;
    public int numberOfPages, numberOfDepart, numberOfSysBtn;
    public ProductModel selectedProduct, oldProduct;
    public static HomeActivity localInstance;
    public CheckOutListAdapter myAdapter;
    public JSONObject jsonObject;
    private boolean doubleBackToExitPressedOnce;
    public ArrayList<Float> surchrgeAmountList;
    private String productSkuFromScanner;
    public ScrollView productLinearLayoutContaier;

    /**
     * Discount Dialogs instance
     */
    private DiscountDialog discountDialog;
    private int discountDialogWidth;
    private int discountDialogHeight;

    public ProductQtyDialog productIncDialog;
    public ProductOptionDialog productOptionDialog;

    public Handler handler;
    public static final String LOGOUT = "Logout";
    private static final int LOGIN_AS_ID = 0;

    private SocketConnectionClass socketConnectionService;
    public AutologoutConnectionClass autologoutConnectionClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, true, this);
        setContentView(R.layout.activity_sales_management);
        localInstance = this;
        mp = MediaPlayer.create(this, R.raw.button_sound);
        onInitViews();
        onListenerRegister();
        onDefaultCreation();
        onResources();
        onDbWork();
        numberOfSysBtn = systemKeyArr.length;
        categoryTable = new CategoryTable(mContext);
        deptList = categoryTable.getAllInfoFromTable(); // gettting Info from DB
        numberOfDepart = deptList.size();
        onNumberOfPages();
        onRadioGroupCreated();
        controls = new CreateControls(mContext);
        controls.onCreateBottomPanel();

        if (!deptList.isEmpty())
            controls.onCreateProductPanel(Integer.parseInt(deptList.get(0).getDeptId()));

        itemListControl.setAdapter(myAdapter);
        itemListControl.setOnGroupClickListener(this);
        new AdapterForSearch(mContext).onSearchMethod(); // to search an item by Name also add it when user tap on it.

        ServiceUtils.operateWFService(mContext, true);
        ServiceUtils.operateBTService(mContext, true);
        ServiceUtils.operateBTDejavooService(mContext, true);

        socketConnectionService = new SocketConnectionClass(mContext);
        socketConnectionService.startService();

        autologoutConnectionClass = new AutologoutConnectionClass(mContext);
        autologoutConnectionClass.startService();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, LOGIN_AS_ID, 0, LOGOUT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case LOGIN_AS_ID:
                if (item.getTitle().toString().equalsIgnoreCase(LOGOUT)) {
                    new LogoutUser(mContext).onUserLogout();
                }
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onDefaultCreation() {
        deptKeysRowList = new ArrayList<LinearLayout>();
        sysKeysRowList = new ArrayList<LinearLayout>();
        keysLayoutList = new ArrayList<LinearLayout>();
        dataList = new ArrayList<CheckOutParentModel>();
        myAdapter = new CheckOutListAdapter(mContext, dataList, itemListControl);
        surchrgeAmountList = new ArrayList<Float>();
    }

    private void onResources() {

        systemKeyArr = getResources().getStringArray(R.array.systemKeysArr);
        sytemKeyTags = getResources().getStringArray(R.array.systemKeyButtonsIds);
    }

    private void onDbWork() {
        allProductList = new ProductTable(mContext).getAllInfoFromTable();
    }

    private void onNumberOfPages() {

        int largestNo = Math.max(systemKeyArr.length, numberOfDepart);

        if (largestNo <= 5 && largestNo > 0)
            numberOfPages = 1;
        else if (largestNo % 5 == 0)                     // Calculation for numbers of pages
            numberOfPages = largestNo / 5;
        else
            numberOfPages = largestNo / 5 + 1;
    }

    private void onRadioGroupCreated() {
        if (numberOfPages > 0) {
            radioButtons = new RadioButton[numberOfPages];
            ((ViewGroup) mRadioGroup.getParent()).removeView(mRadioGroup);
            for (int index = 0; index < numberOfPages; index++) {
                radioButtons[index] = new RadioButton(this);
                radioButtons[index].setId(index);
                mRadioGroup.addView(radioButtons[index]);
            }
            radioButtons[0].setChecked(true);
            parentOfRadioGroup.addView(mRadioGroup);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T findViewByIdAndCast(int id) {
        return (T) findViewById(id);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        for (int index = 0; index < numberOfPages; index++) {
            if (checkedId == index) {
                mPager.setCurrentScreen(index, true);
                break;
            }
        }
    }

    private void playSound() {
        mp.start();
    }

    @Override
    public void onClick(View v) {
        playSound();
        String tag = (String) v.getTag();

        if (tag.equalsIgnoreCase("deptartmentClick")) { //when an departemt button clicked

            if (!Variables.isReturnActive)
                controls.onCreateProductPanel(v.getId());
            else
                ToastUtils.showOwnToast(mContext, "You Can Not Add New Items During Refund");
        } else if (tag.equalsIgnoreCase("productClick")) {  // when an product item is clicked

            if (!Variables.isReturnActive && !Variables.isReprintActive) {
                if (Variables.startNewTrans) {
                    initializedTrans();
                }
                new ProductClick(mContext).onClick(v.getId());
            } else
                ToastUtils.showOwnToast(mContext, "You Can Not Add New Items During Refund");
        } else if (tag.equalsIgnoreCase("btn_Void_All")) {   // Delete All records From List
            ClearAllHomeScreenDataDialog.onClearHomeScreenDataShown(mContext, this);
        } else if (tag.equals("btn_subtotal")) {
            if (!isShopingCartEmpty()) {
                if (Variables.isReturnActive)
                    new RefundOptionsDialog(mContext).showRefundOptionsDialog();
                else if (Variables.isReprintActive) {

                    GoForPrint goForPrint = new GoForPrint(mContext, GoForPrint.CASH_FRAGMENT, false);
                    goForPrint.onExectue();
                } else {
                    boolean isClerkOrderAssignEnable = MyPreferences.getBooleanPrefrences(CLERK_ORDER_ASSIGN, mContext);
                    boolean isClerkAssignedForOrder = new CustomerTable(mContext).getInfoFromTableBasedOnLoginStatus(true).size() > 0;

                    new ConvertStringOfJson(mContext).onTendertap();

                    if (!isClerkOrderAssignEnable)
                        StartAndroidActivity.onActivityStart(false, mContext, TenderActivity.class);
                    else if (isClerkAssignedForOrder && isClerkOrderAssignEnable)
                        StartAndroidActivity.onActivityStart(false, mContext, TenderActivity.class);
                    else {
                        int width = CalculateWidthAndHeigth.calculatingWidthAndHeight(POSApp.getInstance().getDeviceWidth(), 60);
                        int height = CalculateWidthAndHeigth.calculatingWidthAndHeight(POSApp.getInstance().getDeviceHeight(), 70);

                        ClerkSalesLoginLogoutDialog dailog = new ClerkSalesLoginLogoutDialog(mContext, R.style.myCoolDialog, width, height, true, true, R.layout.dialog_show_clerk_login_logout);
                        dailog.show(0);
                    }
                }
            }
        } else if (tag.equals("btn_Discount")) {
            showDiscountDialog();
        } else if (tag.equalsIgnoreCase("btn_function")) {
            StartAndroidActivity.onActivityStart(false, mContext, FunctionDrawerActivity.class);
        } else if (tag.equals("btn_maintenence")) {
            new SecurityVerification(mContext, SecurityTable.Settings_Admin).adminFunctionChecking();
        } else if (tag.equals("btn_reports")) {
            new ReportOptionsDailog(mContext).showReportOptionsDialog();
        } else if (tag.equalsIgnoreCase("btn_reprint")) {
            StartAndroidActivity.onActivityStart(false, mContext, ReprintActivity.class);
        }

		/*else if(tag.equalsIgnoreCase("btn_reprint")){
            int width   = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceWidth(), 50);
			int height  = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceHeight(),50);

			RefundItemsWithAmt dialog = new RefundItemsWithAmt(mContext, R.style.myCoolDialog, width, height, false, true, R.layout.refund_activity);
			new SecurityVerification(mContext, SecurityTable.Settings_RePrint).refundFunctionChecking(dialog,RefundItemsWithAmt.REPRINT_);
		}*/

        else if (tag.equals("btn_pull")) {
            int width = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceWidth(), 50);
            int height = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceHeight(), 50);

            RefundItemsWithAmt dialog = new RefundItemsWithAmt(mContext, R.style.myCoolDialog, width, height, false, true, R.layout.refund_activity);
            new SecurityVerification(mContext, SecurityTable.Settings_Refund).refundFunctionChecking(dialog, RefundItemsWithAmt.RETURN_);
        } else if (tag.equalsIgnoreCase("btn_pendings_order")) {

            if (!LocationOfPosApp.isRetailsIsActive(mContext) && !LocationOfPosApp.isQuickIsActive(mContext)) {
                StartAndroidActivity.onActivityStart(false, mContext, PendingActivity.class);
            } else
                ToastUtils.showOwnToast(mContext, "Please Enable Pending Functionality First");
        }
    }

    @Override
    public void onScreenSwitched(int screen) {
        for (int index = 0; index < numberOfPages; index++) {
            if (screen == index) {
                mPager.setCurrentScreen(index, true);
                radioButtons[index].setChecked(true);
                break;
            }
        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {

        PopupMenu popup = new PopupMenu(mContext, v);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                CheckOutParentModel parentModel = myAdapter.getGroup(groupPosition);
                if (!parentModel.getExtraArgument().isSurageApplicable()) {

                    switch (item.getItemId()) {

                        case R.id.popup_checkout_menu_apply_discount:
                            showDialogBasedOnValues(DiscountDialog.DIALOG_DISCOUNT_ITEMS_PERCENTAGE, parentModel);
                            break;

                        case R.id.popup_checkout_menu_apply_dollar_discount:
                            showDialogBasedOnValues(DiscountDialog.DIALOG_DISCOUNT_ITEMS_DOLLAR, parentModel);
                            break;

                        case R.id.popup_checkout_menu_change_qty:

                            int width = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceWidth(), 50);
                            int height = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceHeight(), 80);
                            productIncDialog = new ProductQtyDialog(mContext, R.style.myCoolDialog, width, height, true, true, R.layout.dialog_product_quantity);
                            productIncDialog.onSendData(parentModel);
                            productIncDialog.show();

                            break;

                        default:
                            break;
                    }
                }
                return true;
            }
        });
        popup.show();//showing popup menu


        return true;

    }

    public void initializedTrans() {
        try {
            resetAllData(mContext, 1);
            trasIdTv.setText(GenerateNewTransactionNo.onNewTransactionNumber(mContext));
            String timeAndDate = (String) DateFormat.format("yyyy/MM/dd hh:mm:ss", new Date().getTime());
            dateTimeTv.setText(timeAndDate);
            Variables.startNewTrans = false;
            new ConvertStringOfJson(mContext).onDateAndTransactionNo(timeAndDate, trasIdTv.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void resetAllData(Context _context, int statusBit)  // reset ALL The variable At on Initial State
    {
        Variables.isPendingOrderItems = false;
        Variables.startNewTrans = true;
        Variables.discountDollar = false;
        Variables.discountApplied = false;
        Variables.discountPercentage = false;
        Variables.paymentByCC = false;
        Variables.isRetailWithTipAuthDone = true;
        Variables.isRestaurantWithTipSaleDone = true;
        Variables.isDejavooSuccess = false;
        Variables.isReturnActive = false;
        Variables.isReprintActive = false;
        Variables.refundByCC = false;
        Variables.discountInPercentage = 0.0f;
        Variables.discountInDollar = 0.0f;
        Variables.totalBillAmount = 0.0f;
        Variables.totalDiscount = 0.0f;
        Variables.itemsAmount = 0.0f;
        Variables.taxAmount = 0.0f;
        Variables.cashAmount = 0.0f;
        Variables.giftAmount = 0.0f;
        Variables.changeAmt = 0.0f;
        Variables.ccAmount = 0.0f;
        Variables.checkAmount = 0.0f;
        Variables.custom1Amount = 0.0f;
        Variables.custom2Amount = 0.0f;
        Variables.cashAfterChange = 0.0f;
        Variables.giftAmtAfterChange = 0.0f;
        Variables.rewardsAfterChange = 0.0f;
        Variables.rewardsAmount = 0.0f;
        Variables.dueCCAmount = 0.0f;
        Variables.subChargeAmount = 0.0f;
        Variables.orderLevelDiscount = 0.0f;
        Variables.fees = 0.0f;
        Variables.tableID = "";
        Variables.ccNumber = "";
        Variables.CCHOlderName = "";
        Variables.CcExpiryYear = "";
        Variables.CcExpiryDate = "";
        Variables.orderComment = "";
        Variables.Cardtype = "";
        Variables.billToName = "";
        Variables.customerName = "";

        Variables.shipToName = "";
        Variables.gateWayTrasId = "";
        Variables.orderStatus = "pending";
        Variables.customerId = 0;
        Variables.tableOrClerkShipToNameModel = null;
        Variables.customerOrClerkBillToNameModel = null;
        amountToPaidTv.setText("0.00");
        amountToPaidTv.setTextColor(Color.parseColor("#008000"));
        subTotalTv.setText("");
        taxTv.setText("");
        totalTv.setText("");
        cashTv.setText("");
        changeAmtTv.setText("");
        discountTv.setText("");
        dateTimeTv.setText("");
        trasIdTv.setText("");
        subtotalBtn.setText("Tender");
        dataList.clear();
        myAdapter.notifyDataSetChanged();
        surchrgeAmountList.clear();

        new ConvertStringOfJson(mContext).onClearList();


        List<POSAuthority> dataList = new ArrayList<>();
        dataList.clear();
        dataList.addAll(new SecurityTable(mContext).getAllInfoFromTable());

        for (int index = 0; index < dataList.size(); index++) {
            dataList.get(index).setSettingOverrideByManager(false);
        }
        new SecurityTable(mContext).updateInfoListInTable(dataList);

        if (statusBit == 0 && new CustomerTable(mContext).getInfoFromTableBasedOnLoginStatus(true).size() > 0 && MyPreferences.getBooleanPrefrences(CLERK_ORDER_ASSIGN, mContext))
            LogoutClerkFromOrders.onLogoutClerkFromOrder(mContext);
    }


    public void calculateSubTotalEachTime() {

        AmountCalculation amountCalculation = new AmountCalculation(mContext);

        String disountAmount = amountCalculation.getItemsDiscountAmt();
        String itemsAmount = amountCalculation.getItemsAmt();
        String taxAmount = amountCalculation.getItemsTaxAmt();

        float itemsDisAmt = Float.parseFloat(disountAmount);
        float itemsAmt = Float.parseFloat(itemsAmount);
        float itemsTaxAmt = Float.parseFloat(taxAmount);

        float extraDiscountAmt = 0.0f;
        float extraTaxAmt = 0.0f;
        float duePayAmt = 0.0f;


        if (Variables.discountApplied) {
            if (Variables.discountDollar)
                extraDiscountAmt = Variables.discountInDollar;
            if (Variables.discountPercentage) {
                extraDiscountAmt = (itemsAmt - itemsDisAmt) * Variables.discountInPercentage / 100.0f;
                extraTaxAmt = (itemsTaxAmt) * Variables.discountInPercentage / 100.0f;
                itemsTaxAmt -= extraTaxAmt;
            }
            duePayAmt = itemsAmt - itemsDisAmt - extraDiscountAmt;
        } else
            duePayAmt = itemsAmt - itemsDisAmt;

        duePayAmt += itemsTaxAmt;

        ProductModel surchargeProduct = amountCalculation.isSurchagreProductExist();

        if (surchargeProduct != null)
            itemsAmt += Float.parseFloat(surchargeProduct.getProductPrice());

        taxTv.setText(MyStringFormat.formatWith2DecimalPlaces(itemsTaxAmt));
        discountTv.setText(MyStringFormat.formatWith2DecimalPlaces(itemsDisAmt + extraDiscountAmt));
        subTotalTv.setText(MyStringFormat.formatWith2DecimalPlaces(itemsAmt));

        totalTv.setText(MyStringFormat.formatWith2DecimalPlaces(duePayAmt));
        amountToPaidTv.setText(MyStringFormat.formatWith2DecimalPlaces(duePayAmt));

        Variables.totalBillAmount = duePayAmt;                                // Saving All the Values
        Variables.totalDiscount = itemsDisAmt + extraDiscountAmt;
        Variables.orderLevelDiscount = extraDiscountAmt;
        Variables.itemsAmount = Float.parseFloat(itemsAmount);
        Variables.taxAmount = itemsTaxAmt;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String serachProduct = (String) parent.getItemAtPosition(position);

        int productId = new ProductTable(mContext).getProdctIdFromTableByProductName(serachProduct);
        ToastUtils.showOwnToast(mContext, serachProduct);

        searchProductByName.setText("");

        if (Variables.startNewTrans)
            initializedTrans();

        new ProductClick(mContext).onClick(productId);
        HideSoftKeyBoardFromScreen.onHideSoftKeyBoard(mContext, searchProductByName);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {

        productSkuFromScanner = s.toString();

        if (s.length() > 0) {

            if (delayedAction != null)
                handler.removeCallbacks(delayedAction);

            handler.postDelayed(delayedAction, 1000);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            resetAllData(mContext, 1);
            resetAllPrintingConnection();
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press Again To Exit " + mContext.getString(R.string.app_name), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void resetAllPrintingConnection() {

        ServiceUtils.operateWFService(mContext, false);
        ServiceUtils.operateBTService(mContext, false);
        ServiceUtils.operateBTDejavooService(mContext, false);

        if (socketConnectionService != null) {
            socketConnectionService.stopService();
        }

        if (autologoutConnectionClass != null)
            autologoutConnectionClass.stopService();
    }

    public void showDiscountDialog() {

        final CharSequence[] items = {"Discount % ", "Discount $"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Discount :-");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                playSound();
                switch (item) {

                    case DiscountDialog.DIALOG_DISCOUNT_TRANSACTION_PERCENTAGE:
                        showDialogBasedOnValues(DiscountDialog.DIALOG_DISCOUNT_TRANSACTION_PERCENTAGE, null);
                        break;

                    case DiscountDialog.DIALOG_DISCOUNT_TRANSACTION_DOLLAR:
                        showDialogBasedOnValues(DiscountDialog.DIALOG_DISCOUNT_TRANSACTION_DOLLAR, null);
                        break;

                    default:
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void showDialogBasedOnValues(int dialogDiscountVersion, CheckOutParentModel parentModel) {
        discountDialog = new DiscountDialog(mContext, R.style.myCoolDialog, discountDialogWidth, discountDialogHeight, true, true, R.layout.dialog_discount);
        discountDialog.show(dialogDiscountVersion, parentModel);
    }

    public void dialogDiscountOnClick(View view) {
        discountDialog.onClick(view);
    }

    public void onKeyPadClick(View view) {

        if (productOptionDialog != null)
            productOptionDialog.onClick(view);

        if (productIncDialog != null)
            productIncDialog.onClick(view);

    }

    @Override
    public void onInitViews() {

        barcodeScanner = findViewByIdAndCast(R.id.barcodeFocus);
        mPager = findViewByIdAndCast(R.id.horizontal_pager);
        mRadioGroup = findViewByIdAndCast(R.id.radioGroup);
        subTotalTv = findViewByIdAndCast(R.id.tv_subtotal);
        taxTv = findViewByIdAndCast(R.id.tv_tax);
        totalTv = findViewByIdAndCast(R.id.tv_total);
        amountToPaidTv = findViewByIdAndCast(R.id.totalCash);
        cashTv = findViewByIdAndCast(R.id.tv_cashamount);
        changeAmtTv = findViewByIdAndCast(R.id.tv_change);
        trasIdTv = findViewByIdAndCast(R.id.tv_tranId);
        dateTimeTv = findViewByIdAndCast(R.id.tv_dataTime);
        discountTv = findViewByIdAndCast(R.id.tv_discount);
        parentOfRadioGroup = findViewByIdAndCast(R.id.switchScreen);
        itemListControl = findViewByIdAndCast(R.id.listview1);
        subtotalBtn = findViewByIdAndCast(R.id.subtotalButton);
        productLayout = findViewByIdAndCast(R.id.itemsGrid);
        searchProductByName = findViewByIdAndCast(R.id.searchAutoCompleteTextview);
        productLinearLayoutContaier = findViewByIdAndCast(R.id.Activity_Home_ScrollView_Container);
        discountDialogWidth = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceWidth(), 40);
        discountDialogHeight = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceHeight(), 75);
        scoketConnectionTv = findViewByIdAndCast(R.id.socketIndicator);
        handler = new Handler();

    }

    @Override
    public void onListenerRegister() {

        mRadioGroup.setOnCheckedChangeListener(this);
        mPager.setOnScreenSwitchListener(this);
        subtotalBtn.setOnClickListener(this);
        barcodeScanner.addTextChangedListener(this);
    }


    Runnable delayedAction = new Runnable() {

        @Override
        public void run() {

            if (Variables.startNewTrans) {
                initializedTrans();
            }

            new SelectedProduct(mContext).getProductInformation(productSkuFromScanner);
            findViewById(R.id.barcodeFocus).setFocusable(true);
            findViewById(R.id.barcodeFocus).requestFocus();
            barcodeScanner.setText("");
            barcodeScanner.setNextFocusDownId(R.id.barcodeFocus);
            HideSoftKeyBoardFromScreen.onHideSoftKeyBoard(mContext, barcodeScanner);
        }
    };


    @Override
    public void onSocketStateChanged(final int state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (state == SocketIO.CONNECTED_)
                    scoketConnectionTv.setBackgroundResource(R.drawable.socket_connect);
                else
                    scoketConnectionTv.setBackgroundResource(R.drawable.socket_disconnect);
            }
        });
    }

    @Override
    public void onDataRecieved(final JSONArray jsonArray) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new UpdateUserInterface(mContext).onUiUpdate(jsonArray);
            }
        });
    }

    public boolean isShopingCartEmpty() {
        if (dataList != null && dataList.size() > 0) {
            return false;
        } else {
            ToastUtils.showOwnToast(mContext, "Add Items First Into Cart");
            return true;
        }
    }
}

