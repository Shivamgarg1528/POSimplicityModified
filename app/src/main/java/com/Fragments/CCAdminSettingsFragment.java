package com.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.utils.MyPreferences;
import com.posimplicity.R;

public class CCAdminSettingsFragment extends BaseFragment {

	public ListView listView;
	public int   selectedPosition = -1 ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView           = inflater.inflate(R.layout.fragment_cc_admin_settings_fragment, container, false);
		listView           = findViewIdAndCast(R.id.Fragment_CCAdmin_Settings_ListView_Items);
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
		
		arrayAdapter.add(MaintFragmentCCAdmin.PLUG_N_PAY);
		arrayAdapter.add(MaintFragmentCCAdmin.BRIDGE_PAY);
		//arrayAdapter.add(MaintFragmentCCAdmin.NOBLE_);
		arrayAdapter.add(MaintFragmentCCAdmin.TSYS_);
		arrayAdapter.add(MaintFragmentCCAdmin.PROPAY_);
		arrayAdapter.add(MaintFragmentCCAdmin.DEJAVOO);

		listView.setAdapter(arrayAdapter);

		int position = (int) MyPreferences.getLongPreferenceWithDiffDefValue(GATEWAY_USED_POSITION, mContext);

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
}
