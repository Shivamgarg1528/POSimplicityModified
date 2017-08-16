package com.posimplicity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.dialog.AddNewPaygradeClerk;
import com.Beans.StaffModel;
import com.posimplicity.database.local.StaffTable;
import com.utils.ToastUtils;

public class AddClerkPaygradeActivity extends BaseActivity implements OnItemClickListener {

	private ListView listview;
	public List<StaffModel> dataList;
	public List<StaffModel> newUpdatedList;
	public MainAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,false,this);


		newUpdatedList     = new ArrayList<>();

		dataList           = new ArrayList<>();		
		dataList.clear();

		dataList.addAll(new StaffTable(mContext).getAllInfoFromTableDefalut());
		Collections.sort(dataList);

		listview        = new ListView(this);
		mAdapter        = new MainAdapter(dataList);
		listview.setAdapter(mAdapter);
		listview.setFadingEdgeLength(0);		
		setContentView(listview);
		listview.setOnItemClickListener(this);
	}


	private boolean isUpdatedListContainData(){

		newUpdatedList.clear();
		for(int index = dataList.size() - 1 ; index >= 0 ; index --){
			if(dataList.get(index).isRowSelected())
				newUpdatedList.add(dataList.get(index));
		}
		return newUpdatedList.size() > 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_clerk_screen, menu);
		return super.onCreateOptionsMenu(menu);		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.AddNewItem:			
			new AddNewPaygradeClerk(mContext,this).onAddNewEmployee();
			break;

		case R.id.RemoveItem:

			if(dataList.isEmpty()){
				ToastUtils.showOwnToast(mContext, Messages.getString("AddClerkPaygradeActivity.0")); //$NON-NLS-1$
				return false;			
			}

			if(isUpdatedListContainData()){
				
				for(int index = 0 ; index < newUpdatedList.size() ; index ++){
					StaffModel staffModel = newUpdatedList.get(index);
					new StaffTable(mContext).deleteInfoFromTable(staffModel.getStaffId());
				}	
				
				if(newUpdatedList.size()==1)
					ToastUtils.showOwnToast(mContext, Messages.getString("AddClerkPaygradeActivity.1")); //$NON-NLS-1$
				else
					ToastUtils.showOwnToast(mContext, Messages.getString("AddClerkPaygradeActivity.2")); //$NON-NLS-1$
				
				dataList.clear();
				dataList.addAll(new StaffTable(mContext).getAllInfoFromTableDefalut());
				mAdapter.notifyDataSetChanged();
			}
			else
				ToastUtils.showOwnToast(mContext, Messages.getString("AddClerkPaygradeActivity.3")); //$NON-NLS-1$

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public class MainAdapter extends ArrayAdapter<StaffModel> {

		private LayoutInflater mInflater;
		private List<StaffModel> dataList;

		private class Holder {
			public CheckedTextView textview;
		}

		public MainAdapter(List<StaffModel> dataList) {
			super(mContext, 0, dataList);
			this.mInflater  = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.dataList   = dataList;
		}

		@Override
		public View getView(final int position, View convertView,ViewGroup parent) {
			final StaffModel staffModel = this.dataList.get(position);
			final Holder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(android.R.layout.simple_list_item_checked, null);
				convertView.setBackgroundColor(0xFF202020);
				holder = new Holder();
				holder.textview = (CheckedTextView) convertView.findViewById(android.R.id.text1);
				holder.textview.setTextColor(0xFFFFFFFF);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			holder.textview.setText(staffModel.getStaffName());
			holder.textview.setChecked(staffModel.isRowSelected());
			return convertView;
		}
	}
	@Override
	public void onDataRecieved(JSONArray arry) {}

	@Override
	public void onSocketStateChanged(int state) {}

	@Override
	public void onInitViews() {}

	@Override
	public void onListenerRegister() {}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

		dataList.get(position).setRowSelected(!dataList.get(position).isRowSelected());	
		mAdapter.notifyDataSetChanged();
		
	}
}
