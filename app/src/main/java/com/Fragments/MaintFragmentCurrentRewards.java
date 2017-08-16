package com.Fragments;

import com.utils.MyPreferences;
import com.utils.ToastUtils;
import com.posimplicity.R;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MaintFragmentCurrentRewards extends BaseFragment implements OnClickListener{

	public ListView listView;
	public int   selectedPosition = -1 ;
	private  Button saveBtn,resetBtn;
	public static final int TENDER_CARD_REWRDS = 0;
	public static final int POS_CARD_REWRDS    = 1;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView           = inflater.inflate(R.layout.fragment_current_reward, container, false);
		listView           = findViewIdAndCast(R.id.Fragment_Rewards_ListView_Items);
		saveBtn            = findViewIdAndCast(R.id.Fragment_Rewards_Button_Save);
		resetBtn           = findViewIdAndCast(R.id.Fragment_Rewards_Button_Reset);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {		
		super.onActivityCreated(savedInstanceState);

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_checked){

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view     = super.getView(position, convertView, parent);
				TextView text = (TextView) view.findViewById(android.R.id.text1);
				text.setTextColor(Color.WHITE);
				return view;
			}
		};

		arrayAdapter.add("TenderCard");
		arrayAdapter.add("POS Rewards");

		listView.setAdapter(arrayAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		saveBtn.setOnClickListener(this);
		resetBtn.setOnClickListener(this);

		int position = (int) MyPreferences.getLongPreferenceWithDiffDefValue(REWARDS, mContext);

		if(position >= 0)
			listView.setItemChecked(position, true);
	}

	public int  anyItemSelectedFromListView(){
		selectedPosition = -1;
		SparseBooleanArray checked = listView.getCheckedItemPositions();
		for (int i = 0; i < listView.getAdapter().getCount(); i++) {
			if (checked.get(i)) {
				selectedPosition = i;
				break;
			}
		}
		return selectedPosition;    
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.Fragment_Rewards_Button_Save:

			int selectedPos = anyItemSelectedFromListView();
			if(selectedPos < 0 ){
				ToastUtils.showOwnToast(mContext, "Select Any Item From The List");
				return;
			}
			MyPreferences.setLongPreferences(REWARDS, selectedPos, mContext);
			ToastUtils.showOwnToast(mContext, "Saved Successfully");

			break;

		case R.id.Fragment_Rewards_Button_Reset:
			MyPreferences.setLongPreferences(REWARDS, -1, mContext);

			for (int i = 0; i < listView.getAdapter().getCount(); i++) {
				listView.setItemChecked(i, false);
			}

			ToastUtils.showOwnToast(mContext, "Reseted Successfully");

			break;

		default:
			break;
		}

	}
}
