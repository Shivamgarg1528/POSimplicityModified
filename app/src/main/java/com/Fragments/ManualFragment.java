package com.Fragments;

import java.util.ArrayList;
import java.util.List;

import com.AsyncTasks.ManualReportInMagento;
import com.utils.HideSoftKeyBoardFromScreen;
import com.utils.TenderEnable;
import com.utils.ToastUtils;
import com.posimplicity.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ManualFragment extends BaseFragment implements OnClickListener {

	private EditText transIdEB;
	private Button   submitBtn;
	private TextView closeBtn;
	private String   transId;
	private ListView paymentModeList;
	private List<String> paymentModeNames = new ArrayList<>();
	private int selectedPosition = -1;
	private ArrayAdapter<String> arrayAdapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		rootView     	 = inflater.inflate(R.layout.fragment_offline, null);
		transIdEB    	 = findViewIdAndCast(R.id.Fragment_Offline_Tender_Edt_transcId);
		submitBtn    	 = findViewIdAndCast(R.id.Fragment_Offline_Tender_Btn_Submit);
		closeBtn     	 = findViewIdAndCast(R.id.Fragment_Offline_Tender_Btn_Close);
		paymentModeList  = findViewIdAndCast(R.id.Fragment_Offline_Tender_ListView_mode);

		paymentModeNames.add("Cash");
		paymentModeNames.add("Credit");
		paymentModeNames.add("Check");
		paymentModeNames.add("Gift");
		paymentModeNames.add("Rewards");
		
		if(TenderEnable.isCustom1TenderEnable(mContext))
			paymentModeNames.add(MaintFragmentTender1.getCustom2Name(mContext));
		
		if(TenderEnable.isCustom2TenderEnable(mContext))
			paymentModeNames.add(MaintFragmentTender1.getCustom2Name(mContext));
		
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		submitBtn.setOnClickListener(this);
		closeBtn.setOnClickListener(this);		
		arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_checked, android.R.id.text1, paymentModeNames);
		paymentModeList.setAdapter(arrayAdapter);
	}

	public int  anyItemSelectedFromListView(){
		int  isSelected = -1;
		SparseBooleanArray checked = paymentModeList.getCheckedItemPositions();

		for (int i = 0; i < arrayAdapter.getCount(); i++) {
			if (checked.get(i)) {
				isSelected = i;
				break;
			}
		}
		return isSelected;    
	}


	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.Fragment_Offline_Tender_Btn_Submit:
			transId = transIdEB.getText().toString().trim();
			if(transId.isEmpty()){
				transIdEB.setError("Please Enter Id First");
			}else
			{
				selectedPosition    = anyItemSelectedFromListView();
				if(selectedPosition < 0){
					ToastUtils.showOwnToast(mContext, "Please Select Any Payment Mode");
					return ;
				}
				HideSoftKeyBoardFromScreen.onHideSoftKeyBoard(mContext, transIdEB);
				okToGo(mContext);
			}

			break;
		case R.id.Fragment_Offline_Tender_Btn_Close:
			((Activity) mContext).finish();
			break;

		default:
			break;
		}
	}

	public void okToGo(final Context mContext) {

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);				
		builder.setTitle(R.string.app_name);
		builder.setIcon(R.drawable.app_icon);
		builder.setMessage("OK to Save Order!");
		builder.setCancelable(false);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new ManualReportInMagento(mContext, transId,selectedPosition).execute();
			}
		});		
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {	
				dialog.dismiss();
			}
		});
		builder.show();
		builder.create();
	}
}
