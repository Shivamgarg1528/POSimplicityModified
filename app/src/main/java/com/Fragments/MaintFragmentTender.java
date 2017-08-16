package com.Fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.Beans.PaymentMode;
import com.Beans.PaymentMode.PaymentModeBean;
import com.adapter.PaymentModeAdapter;
import com.adapter.PaymentModeAdapter.OnPaymentModeOperation;
import com.posimplicity.database.local.PaymentModeTable;
import com.utils.ReadFileFromAsset;
import com.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.posimplicity.R;

public class MaintFragmentTender extends BaseFragment implements OnClickListener, OnPaymentModeOperation {

	private Button saveBtn,resetBtn,addBtn;
	private ListView paymentModeListView;
	private List<PaymentModeBean> paymentModeList;
	private PaymentModeAdapter mAdapter;

	public MaintFragmentTender(){}	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		paymentModeList = new ArrayList<>();
		paymentModeList.addAll(new PaymentModeTable(mContext).getAllInfoFromTableDefalut());
		mAdapter        = new PaymentModeAdapter(paymentModeList, mContext,this);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView            = inflater.inflate(R.layout.fragment_tender_setting, container, false);		
		saveBtn             = findViewIdAndCast(R.id.Fragment_Tender_Settings_BUTTON_SAVE);	
		resetBtn            = findViewIdAndCast(R.id.Fragment_Tender_Settings_BUTTON_RESET);
		addBtn              = findViewIdAndCast(R.id.Fragment_Tender_Settings_BUTTON_ADD);
		paymentModeListView = findViewIdAndCast(R.id.Fragment_Tender_List_View);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);
		saveBtn.setOnClickListener(this);
		resetBtn.setOnClickListener(this);
		addBtn.setOnClickListener(this);
		paymentModeListView.setAdapter(mAdapter);
	}

	public void onAddNewTender(final PaymentModeBean paymentModel){

		Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/HelveticaLTStd-Bold.otf");

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(R.drawable.app_icon).setMessage("Enter Tender Name!").setTitle(mContext.getString(R.string.app_name));

		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		final EditText editText = new EditText(mContext);
		editText.setSingleLine(true); 
		editText.setTextColor(Color.BLACK);
		editText.setGravity(Gravity.CENTER);
		editText.setTypeface(tf);
		editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		
		if(paymentModel != null)
			editText.setText(paymentModel.getPaymentModeName());

		linearLayout.addView(editText);

		TextView line = new TextView(mContext);
		line.setHeight(mContext.getResources().getDimensionPixelSize(R.dimen.textAppearance_mdpi_01_sp));
		line.setBackgroundColor(Color.BLUE);

		linearLayout.addView(line);

		builder.setCancelable(false);
		builder.setView(linearLayout);
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(TextUtils.isEmpty(editText.getText())){
					ToastUtils.showOwnToast(mContext, "Please Specify Tender Name First");
					return;
				}
				
				PaymentModeTable paymentModeTable = new PaymentModeTable(mContext);				
				if(paymentModel == null){
					int lastId = paymentModeTable.getLastId();
					PaymentModeBean p = new PaymentModeBean();
					p.setPaymentModeDeletable(true);
					p.setPaymentModeDes("");
					p.setPaymentModeId(++lastId);					
					p.setPaymentModeName(editText.getText().toString());
					p.setPaymentModeSortId(9);
					p.setPaymentModeStatus(true);
					p.setPaymentModeType("tender_custom");
					boolean isAdded = paymentModeTable.addInfoInTable(p);
					if(isAdded){
						freshLoad();
						ToastUtils.showOwnToast(mContext, "Tender Added SuccessFully");		
					}
					else{
						ToastUtils.showOwnToast(mContext, "Failed To Add Tender.");	
					}
				}
				else{
					paymentModel.setPaymentModeName(editText.getText().toString());
					paymentModeTable.updateInfoInTable(paymentModel);
					ToastUtils.showOwnToast(mContext, "Updated SuccessFully");
					freshLoad();
				}				
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {}
			
		});

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.Fragment_Tender_Settings_BUTTON_ADD:
			onAddNewTender(null);
			break;

		case R.id.Fragment_Tender_Settings_BUTTON_SAVE:	
			updateDb();
			freshLoad();
			ToastUtils.showOwnToast(mContext, "Updation Saved SuccessFully");			
			break;

		case R.id.Fragment_Tender_Settings_BUTTON_RESET:

			AlertDialog alertDialog = new AlertDialog.Builder(mContext)
			.setIcon(R.drawable.app_icon)
			.setTitle(R.string.app_name)
			.setMessage("Continue, To Reset All Saved Tender Settings")
			.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {}
			})
			.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					PaymentModeTable paymentModeTable = new PaymentModeTable(mContext);
					paymentModeTable.clearTable();	
					fullFreshLoad();					
				}
			}).create();
			alertDialog.show();
			break;

		default:
			break;
		}
	}

	private void updateDb() {
		new PaymentModeTable(mContext).updateInfoListInTable(paymentModeList);
	}

	@Override
	public void onEditMode(PaymentModeBean paymentModel) {
		onAddNewTender(paymentModel);
	}

	@Override
	public void onDeleteMode(PaymentModeBean paymentModel) {
		PaymentModeTable p = new PaymentModeTable(mContext);
		p.deleteTender(paymentModel.getPaymentModeId());
		freshLoad();
	}
	
	private void freshLoad(){
		paymentModeList.clear();
		paymentModeList.addAll(new PaymentModeTable(mContext).getAllInfoFromTableDefalut());
		mAdapter.notifyDataSetChanged();
	}
	
	private void fullFreshLoad(){
		onPaymentModeLoad();
		freshLoad();
	}
	
	private void onPaymentModeLoad() {
		try{
			String paymentData          = ReadFileFromAsset.readAsString(mContext, "PaymentModeListFile.txt");
			Gson gson                   = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			PaymentMode paymentMode     = gson.fromJson(paymentData, PaymentMode.class);
			new PaymentModeTable(mContext).addInfoListInTable(paymentMode.getPaymentMode());			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}	
	}
}
