package com.posimplicity.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.recycler.CategoryAdapter;
import com.adapter.recycler.CheckoutAdapter;
import com.adapter.recycler.ProductAdapter;
import com.posimplicity.database.server.CategoryTable;
import com.posimplicity.database.server.ProductOptionTable;
import com.posimplicity.database.server.ProductTable;
import com.posimplicity.interfaces.OnItemUpdate;
import com.posimplicity.dialog.AlertHelper;
import com.posimplicity.dialog.DiscountDialog;
import com.dialog.ProductOptionOrPriceDialog;
import com.posimplicity.interfaces.OnDiscountApply;
import com.posimplicity.interfaces.OnProductItemModify;
import com.posimplicity.model.local.CheckoutParent;
import com.posimplicity.model.response.api.CategoryParent;
import com.posimplicity.model.response.api.ProductParent;
import com.posimplicity.R;
import com.posimplicity.service.BTService;
import com.posimplicity.service.printing.WifiService;
import com.utils.Constants;
import com.utils.Helper;
import com.utils.HideSoftKeyBoardFromScreen;
import com.utils.MyStringFormat;

import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, TextWatcher, Runnable, OnItemUpdate {

    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerViewProduct;

    //AutoComplete
    AutoCompleteTextView mAutoCompleteTextView;
    EditText mEditTextBarCode;

    // TextViews Tenders
    private TextView mTxtViewTenderTotal;
    private TextView mTxtViewTenderPay;

    // TextViews ID and Date
    private TextView mTxtViewTransId;
    private TextView mTxtViewTransDate;

    // TextViews Money
    private TextView mTxtViewSubtotal;
    private TextView mTxtViewTax;
    private TextView mTxtViewDiscount;
    private TextView mTxtViewTotal;

    // DataStructures
    private CheckoutAdapter mCheckoutAdapter;

    private String mProductSku;
    private boolean mAppToastVisible = true;

    // Broadcast Receiver
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_AMOUNT_PAID.equalsIgnoreCase(intent.getAction())) {
                mTxtViewTenderTotal.setText(MyStringFormat.formatWith2DecimalPlaces(mGApp.mOrderModel.orderAmountPaidModel.getAmountDue()));
            } else if (Constants.ACTION_CLEAR.equalsIgnoreCase(intent.getAction())) {
                mGApp.clearOrderModel();
                mCheckoutAdapter.notifyDataSetChanged();
                updateTextViews();

                mTxtViewTransId.setText("");
                mTxtViewTransDate.setText("");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupToolBar("Home", true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_home_drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.activity_home_navigation);
        navigationView.setNavigationItemSelectedListener(this);

        // Setting up recycler view for Checkout
        RecyclerView recyclerViewCheckout = (RecyclerView) findViewById(R.id.activity_home_recycler_view_checkout);
        recyclerViewCheckout.setHasFixedSize(true);
        recyclerViewCheckout.setLayoutManager(new LinearLayoutManager(this));
        mCheckoutAdapter = new CheckoutAdapter(this, mGApp.mOrderModel.orderCheckoutItemsList, this);
        recyclerViewCheckout.setAdapter(mCheckoutAdapter);

        // Setting up recycler view for category
        RecyclerView recyclerViewCategory = (RecyclerView) findViewById(R.id.activity_home_recycler_view_category);
        recyclerViewCategory.setHasFixedSize(true);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Getting all active categories from database and remove any category iff no product under category
        List<CategoryParent.Category> categoryList = new CategoryTable(this).getAllData();
        for (int index = 0; index < categoryList.size(); index++) {
            CategoryParent.Category category = categoryList.get(index);
            boolean isCategoryAssigned = new ProductTable(this).isProductAssignedToGivenCategory(category.getDeptId());
            if (!isCategoryAssigned) {
                categoryList.remove(index);
                --index;
            }
        }
        recyclerViewCategory.setAdapter(new CategoryAdapter(categoryList));
        if (categoryList.size() > 0) {
            refreshProductListBasedOnCategoryId(categoryList.get(0).getDeptId());
        }

        mTxtViewTenderTotal = (TextView) findViewById(R.id.activity_home_tv_tender_total);

        mTxtViewTenderPay = (TextView) findViewById(R.id.activity_home_tv_tender_pay);
        mTxtViewTenderPay.setOnClickListener(this);

        mTxtViewTransId = (TextView) findViewById(R.id.activity_home_tv_transaction_id);
        mTxtViewTransDate = (TextView) findViewById(R.id.activity_home_tv_transaction_date);

        mTxtViewSubtotal = (TextView) findViewById(R.id.activity_home_tv_subtotal);
        mTxtViewTax = (TextView) findViewById(R.id.activity_home_tv_tax);
        mTxtViewDiscount = (TextView) findViewById(R.id.activity_home_tv_discount);
        mTxtViewTotal = (TextView) findViewById(R.id.activity_home_tv_total);

        mEditTextBarCode = (EditText) findViewById(R.id.activity_home_edt_barcode);
        mEditTextBarCode.addTextChangedListener(this);

        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.activity_home_auto_complete_tv_product_name);
        mAutoCompleteTextView.setOnItemClickListener(this);
        mAutoCompleteTextView.setDropDownHorizontalOffset(15);
        mAutoCompleteTextView.setDropDownVerticalOffset(1);
        mAutoCompleteTextView.setDropDownWidth(255);
        mAutoCompleteTextView.setDropDownHeight(150);
        mAutoCompleteTextView.setDropDownBackgroundResource(R.color.colorPrimary);

        List<String> productNameList = new ProductTable(this).getProductNameList();
        ArrayAdapter arrayAdapter = Helper.getStringArrayAdapterInstance(this, R.layout.spinner_layout, android.R.id.text1, productNameList);
        mAutoCompleteTextView.setAdapter(arrayAdapter);

        // setting up default values on TextView's
        updateTextViews();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_AMOUNT_PAID);
        intentFilter.addAction(Constants.ACTION_CLEAR);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);

        //Starting Service
        BTService.startService(this);
        WifiService.startService(this);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_home_tv_tender_pay: {
                if (isCartEmpty()) {
                    return;
                }
                startActivity(new Intent(this, PaymentActivity.class));
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (mAppToastVisible) {
                Toast.makeText(this, getString(R.string.string_press_again_to_exit), Toast.LENGTH_SHORT).show();
                mAppToastVisible = false;
                mDrawerLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAppToastVisible = true;
                    }
                }, 2000);
                return;
            }

            //Starting Service
            BTService.stopService();
            WifiService.stopService();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                onNavigationItemSelect(item.getItemId());
            }
        }, 200);
        return true;
    }

    @Override
    protected void onBackTapped() {
        mDrawerLayout.openDrawer(Gravity.START);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String productName = (String) parent.getAdapter().getItem(position);
        Toast.makeText(this, productName, Toast.LENGTH_SHORT).show();
        ProductParent.Product product = new ProductTable(this).getProduct(productName, false);
        if (product != null) {
            HideSoftKeyBoardFromScreen.onHideSoftKeyBoard(this, mAutoCompleteTextView);
            productButtonTapped(product);
        }
        mAutoCompleteTextView.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mEditTextBarCode.removeCallbacks(this);
        if (s.length() > 0) {
            mProductSku = s.toString();
            mEditTextBarCode.postDelayed(this, 700);
        }
    }

    @Override
    public void run() {
        ProductParent.Product product = new ProductTable(this).getProduct(mProductSku, true);
        if (product != null) {
            HideSoftKeyBoardFromScreen.onHideSoftKeyBoard(this, mEditTextBarCode);
            productButtonTapped(product);
            mEditTextBarCode.setText("");
        }
    }


    /**
     * When an checkout out is modified
     *
     * @param pObject pObject can be null
     */
    @Override
    public void onUpdate(Object pObject) {
        updateTextViews();
    }

    /**
     * When Drawer item selected from list
     *
     * @param pItemId drawer item id
     */
    private void onNavigationItemSelect(int pItemId) {
        switch (pItemId) {

            case R.id.left_nav_pending: {
                Intent intent = new Intent(this, PendingAndReprintActivity.class);
                intent.putExtra(Constants.EXTRA_KEY, true);
                startActivity(intent);
                break;
            }

            case R.id.left_nav_reprint: {
                Intent intent = new Intent(this, PendingAndReprintActivity.class);
                intent.putExtra(Constants.EXTRA_KEY, false);
                startActivity(intent);
                break;
            }

            case R.id.left_nav_discount_clear: {
                if (isCartEmpty()) {
                    return;
                }
                if (mGApp.mOrderModel.orderAmountPaidModel.getAmountPaid() > 0) {
                    Toast.makeText(this, R.string.string_you_can_not_clear_discount_now, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mGApp.mOrderModel.isOrderDiscountApplied()) {
                    Toast.makeText(this, R.string.string_discount_not_applied_yet, Toast.LENGTH_SHORT).show();
                    return;
                }
                mGApp.mOrderModel.setOrderDisPercentage(0.0f);
                mGApp.mOrderModel.setOrderDiscountDollar(0.0f);
                mGApp.mOrderModel.setOrderDiscountApplied(false);
                updateTextViews();
                Toast.makeText(this, R.string.string_discount_cleared_successfully, Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.left_nav_discount_apply: {
                if (isCartEmpty()) {
                    return;
                }
                if (mGApp.mOrderModel.orderAmountPaidModel.getAmountPaid() != 0) {
                    Toast.makeText(this, R.string.string_you_can_not_apply_discount_now, Toast.LENGTH_SHORT).show();
                    return;
                }
                new DiscountDialog(this).show(new OnDiscountApply() {
                    @Override
                    public void onDiscountApply(int pDiscountType, float pValue, android.support.v7.app.AlertDialog pAlertDialog) {
                        switch (pDiscountType) {
                            case DiscountDialog.DISCOUNT_DOLLAR: {
                                if (pValue > mGApp.mOrderModel.orderAmountPaidModel.getAmount()) {
                                    Toast.makeText(HomeActivity.this, R.string.string_value_must_be_less_than_bill_price, Toast.LENGTH_SHORT).show();
                                } else if (pValue == 0) {
                                    Toast.makeText(HomeActivity.this, R.string.string_value_must_be_greater_than_0, Toast.LENGTH_SHORT).show();
                                } else {
                                    mGApp.mOrderModel.setOrderDiscountDollar(pValue);
                                    mGApp.mOrderModel.setOrderDisPercentage(0.0f);
                                    mGApp.mOrderModel.setOrderDiscountApplied(true);
                                    pAlertDialog.dismiss();
                                    updateTextViews();
                                    Toast.makeText(HomeActivity.this, R.string.string_discount_applied_successfully, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                            case DiscountDialog.DISCOUNT_PERCENTAGE: {
                                if (pValue <= 0 || pValue > 100) {
                                    Toast.makeText(HomeActivity.this, R.string.string_value_must_be_greater_than_0_and_less_than_equals_to_100, Toast.LENGTH_SHORT).show();
                                } else {
                                    mGApp.mOrderModel.setOrderDisPercentage(pValue);
                                    mGApp.mOrderModel.setOrderDiscountDollar(0.0f);
                                    mGApp.mOrderModel.setOrderDiscountApplied(true);
                                    pAlertDialog.dismiss();
                                    updateTextViews();
                                    Toast.makeText(HomeActivity.this, R.string.string_discount_applied_successfully, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                        }
                    }
                });
                break;
            }

            case R.id.left_nav_setting: {
                startActivity(new Intent(this, SettingActivity.class));
                break;
            }

            case R.id.left_nav_clear_cart: {
                AlertHelper.showAlertDialog(this, getString(R.string.string_are_you_sure_you_want_to_clear_cart), getString(R.string.string_yes), getString(R.string.string_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(new Intent(Constants.ACTION_CLEAR));
                            Toast.makeText(HomeActivity.this, getString(R.string.string_report_cleared, "Cart"), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            }

            case R.id.left_nav_function: {
                startActivity(new Intent(this, FunctionActivity.class));
                break;
            }

            case R.id.left_nav_report: {
                startActivity(new Intent(this, ReportActivity.class));
                break;
            }

            case R.id.left_nav_logout: {
                finish();
                break;
            }
        }
    }

    /**
     * Check items added for checkout or not...
     *
     * @return return true if not item exist into cart,otherwise false
     */

    private boolean isCartEmpty() {
        if (mGApp.mOrderModel.orderCheckoutItemsList.isEmpty()) {
            Toast.makeText(this, R.string.string_please_add_item_first, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     * refresh and show products based on pCategoryId
     *
     * @param pCategoryId selected categoryId
     */

    public void refreshProductListBasedOnCategoryId(String pCategoryId) {
        List<ProductParent.Product> productList = new ProductTable(this).getProductsAssignedToGivenCategory(pCategoryId);
        if (productList.size() > 0 && productList.size() < 40) { // Adding Extra empty cells
            while (productList.size() != 40) {
                productList.add(null);
            }
        }
        if (mRecyclerViewProduct == null) {
            mRecyclerViewProduct = (RecyclerView) findViewById(R.id.activity_home_recycler_view_product);
            mRecyclerViewProduct.setHasFixedSize(true);
            mRecyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 7));
        }
        mRecyclerViewProduct.setAdapter(new ProductAdapter(productList));
    }

    /**
     * when any product is tapped to add into checkout list
     *
     * @param pProduct checkout item that is going to add into cart
     */
    public void productButtonTapped(ProductParent.Product pProduct) {

        CheckoutParent checkoutParent = new CheckoutParent();
        checkoutParent.setProductId(Integer.parseInt(pProduct.getProductId()));
        checkoutParent.setProductQty(1);
        checkoutParent.setProductPrice(Float.parseFloat(pProduct.getProductPrice()));
        checkoutParent.setProductImageAvailable(pProduct.getProductImageShown() == Constants.IMAGE_SHOWN);
        checkoutParent.setProductImageUrl(pProduct.getProductImage());
        checkoutParent.setProductName(pProduct.getProductName());
        checkoutParent.setProductTaxRate(pProduct.getProductTaxRate());
        checkoutParent.setProductOptions(new ProductOptionTable(this).getProductOptions(pProduct.getProductId()));

        // when product price is 0 or product option is available,then we need to open price popup to set custom price for this transaction only...
        if (Helper.isBlank(pProduct.getProductPrice()) || Float.parseFloat(pProduct.getProductPrice()) <= 0 || !checkoutParent.getProductOptions().isEmpty()) {
            new ProductOptionOrPriceDialog(this).show(checkoutParent, new OnProductItemModify() {
                @Override
                public void onItemModified(CheckoutParent pCheckoutParent) {
                    addItemIntoCart(pCheckoutParent);
                }
            });
        } else {
            addItemIntoCart(checkoutParent);
        }
    }

    /**
     * @param pRHS check if same item exists or not , if exist = true then increase qty otherwise add item
     */
    private void addItemIntoCart(CheckoutParent pRHS) {
        //  Setting product Id as selected id if no product option available or not selected by user...
        if (Helper.isBlank(pRHS.getSelectedIds())) {
            pRHS.setSelectedIds("" + pRHS.getProductId());
        }

        boolean isItemMatched = false;
        int position = -1;
        for (CheckoutParent pLHS : mGApp.mOrderModel.orderCheckoutItemsList) {
            ++position;
            if (pLHS.getSelectedIds().equalsIgnoreCase(pRHS.getSelectedIds())
                    && pLHS.getProductPrice() == pRHS.getProductPrice()) {
                pLHS.setProductQty(pLHS.getProductQty() + pRHS.getProductQty());  // Same Item Found Just going to increase qty
                isItemMatched = true;
                break;
            }
        }
        if (!isItemMatched) {
            mGApp.mOrderModel.orderCheckoutItemsList.add(0, pRHS);
            mCheckoutAdapter.notifyItemInserted(0);
        } else {
            mCheckoutAdapter.notifyItemChanged(position);
        }

        // Setting orderDate and orderId when an item is added into cart
        if (Helper.isBlank(mGApp.mOrderModel.getOrderId())) {
            mGApp.mOrderModel.setOrderId(Helper.getOrderId());
            mTxtViewTransId.setText(mGApp.mOrderModel.getOrderId());
            mTxtViewTransDate.setText(Helper.getCurrentDate(true));
        }
        updateTextViews();
    }

    private void updateTextViews() {
        float subTotalAmt = 0, taxAmt = 0, discountAmt = 0, totalAmt;
        for (CheckoutParent checkoutParent : mGApp.mOrderModel.orderCheckoutItemsList) {
            subTotalAmt += checkoutParent.getProductAndOptionPrice();
            taxAmt += checkoutParent.getProductAndOptionTax();
            discountAmt += checkoutParent.getProductAndOptionDiscount();
        }

        // user applied order discount here....
        if (mGApp.mOrderModel.isOrderDiscountApplied()) {
            taxAmt -= (taxAmt * mGApp.mOrderModel.getOrderDisPercentage() * .01f);
            discountAmt += (subTotalAmt - discountAmt) * mGApp.mOrderModel.getOrderDisPercentage() * .01f;
            discountAmt += mGApp.mOrderModel.getOrderDiscountDollar();
        }
        totalAmt = subTotalAmt - discountAmt + taxAmt;

        mTxtViewSubtotal.setText(MyStringFormat.formatWith2DecimalPlaces(subTotalAmt));
        mTxtViewTax.setText(MyStringFormat.formatWith2DecimalPlaces(taxAmt));
        mTxtViewDiscount.setText(MyStringFormat.formatWith2DecimalPlaces(discountAmt));
        mTxtViewTotal.setText(MyStringFormat.formatWith2DecimalPlaces(totalAmt));

        // setting total amount and due amount
        mGApp.mOrderModel.orderAmountPaidModel.setAmountSubTotal(subTotalAmt);
        mGApp.mOrderModel.orderAmountPaidModel.setAmountTax(taxAmt);
        mGApp.mOrderModel.orderAmountPaidModel.setAmountDiscount(discountAmt);
        mGApp.mOrderModel.orderAmountPaidModel.setAmount(totalAmt);

        mGApp.mOrderModel.orderAmountPaidModel.setAmountDue(totalAmt - mGApp.mOrderModel.orderAmountPaidModel.getAmountPaid());
        mTxtViewTenderTotal.setText(MyStringFormat.formatWith2DecimalPlaces(mGApp.mOrderModel.orderAmountPaidModel.getAmountDue()));
    }
}
