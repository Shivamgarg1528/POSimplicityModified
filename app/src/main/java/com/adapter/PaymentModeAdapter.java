package com.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.Beans.PaymentMode.PaymentModeBean;
import com.posimplicity.R;

import java.util.List;

public class PaymentModeAdapter extends BaseAdapter {

	private List<PaymentModeBean> paymentModelList;
	private LayoutInflater  layoutInflater;
	private Context mContext;
	private OnPaymentModeOperation listener;


	public static interface OnPaymentModeOperation {
		public  void onEditMode(PaymentModeBean paymentModel);
		public  void onDeleteMode(PaymentModeBean paymentModel);
	}

	public PaymentModeAdapter(List<PaymentModeBean> paymentModelList, Context mContext,OnPaymentModeOperation listener) {
		super();
		this.paymentModelList = paymentModelList;
		this.mContext         = mContext;
		this.layoutInflater   = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listener         = listener;
	}

	@Override
	public int getCount() {
		return paymentModelList.size();
	}

	@Override
	public PaymentModeBean getItem(int position) {
		return paymentModelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		PaymentHolder paymentHolder = null;
		if(convertView == null){
			paymentHolder = new PaymentHolder();
			convertView   = layoutInflater.inflate(R.layout.row_layout_for_payment_mode_list, null);
			paymentHolder.mEditTextName        = (EditText)        convertView.findViewById(R.id.mEditTextPaymentModeName);
			paymentHolder.mPaymentModeEditable = (CheckedTextView) convertView.findViewById(R.id.mCheckedTextViewEditable);
			paymentHolder.mPaymentModeEnable   = (CheckedTextView) convertView.findViewById(R.id.mCheckedTextViewEnable);
			paymentHolder.mSpinnerSortOrder    = (Spinner)         convertView.findViewById(R.id.mSpinnerSortOrder);
			paymentHolder.view                 = convertView;
			convertView.setTag(paymentHolder);
		}
		else{
			paymentHolder = (PaymentHolder) convertView.getTag();
		}

		PaymentModeBean paymentModeBean = getItem(position);
		paymentHolder.mEditTextName        .setEnabled(false);
		paymentHolder.mEditTextName        .setText   (paymentModeBean.getPaymentModeName());
		paymentHolder.mPaymentModeEnable   .setChecked(paymentModeBean.isPaymentModeStatus());
		paymentHolder.mPaymentModeEditable .setChecked(paymentModeBean.isPaymentModeDeletable());
		paymentHolder.mPaymentModeEditable .setEnabled(paymentModeBean.isPaymentModeDeletable());
		paymentHolder.mSpinnerSortOrder    .setSelection(paymentModeBean.getPaymentModeSortId());

		paymentHolder.mSpinnerSortOrder.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				PaymentModeBean paymentModeBean = getItem(position);
				paymentModeBean.setPaymentModeSortId(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		paymentHolder.mPaymentModeEnable.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				PaymentModeBean paymentModeBean = getItem(position);
				paymentModeBean.setPaymentModeStatus(!paymentModeBean.isPaymentModeStatus());
				notifyDataSetChanged();
			}
		});

		paymentHolder.mPaymentModeEditable.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				final PaymentModeBean paymentModeBean = getItem(position);				
				if(!paymentModeBean.isPaymentModeDeletable())
					return ;

				final CharSequence[] items = { "Delete","Edit" };

				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

				builder.setTitle("Action:");
				builder.setItems(items, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int item) {
						if(item == 0){
							new AlertDialog.Builder(mContext)
							.setTitle(mContext.getString(R.string.app_name))
							.setMessage("Are You Sure You Want To Delete This Tender ?")
							.setPositiveButton("Done", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {									
									listener.onDeleteMode(paymentModeBean);				
								}
							})
							.show(); 
						}
						else{
							listener.onEditMode(paymentModeBean);
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
		return convertView;
	}

	static class PaymentHolder{
		CheckedTextView mPaymentModeEnable,mPaymentModeEditable;
		Spinner mSpinnerSortOrder;
		EditText mEditTextName;
		View view;
	}
}
