package com.posimplicity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import com.dialog.AddNewPayoutDesc;
import com.Beans.CommentModel;
import com.posimplicity.database.local.PayoutDescTable;
import com.utils.ToastUtils;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AddDescriptionActivity extends BaseActivity implements OnItemClickListener {

	private ListView listview;
	public List<CommentModel> dataList;
	public List<CommentModel> newUpdatedList;
	public MainAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState,false,this);

		dataList           = new ArrayList<>();
		newUpdatedList     = new ArrayList<>();
		dataList.clear();
		dataList.addAll(new PayoutDescTable(mContext).getAllInfoFromTableDefalut());
		Collections.sort(dataList);
		listview        = new ListView(this);

		mAdapter = new MainAdapter(dataList);
		listview.setAdapter(mAdapter);
		listview.setFadingEdgeLength(0);		
		setContentView(listview);
		listview.setOnItemClickListener(this);

	}


	private boolean checkUpdatedList(){

		newUpdatedList.clear();
		for(int index = dataList.size() - 1 ; index >= 0 ; index --){
			if(dataList.get(index).isCommentSelected())
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
			new AddNewPayoutDesc(mContext,this).onAddNewPayoutDesc();
			break;

		case R.id.RemoveItem:

			if(dataList.size() == 0){
				ToastUtils.showOwnToast(mContext, "No Payout Description Record Exists");
				return false;			
			}

			if(checkUpdatedList()){
				for(int index = newUpdatedList.size() - 1 ; index >= 0 ; index --){
					new PayoutDescTable(mContext).deleteInfoFromTable(""+newUpdatedList.get(index).getCommentId());
				}				
				if(dataList.size()==1)
					ToastUtils.showOwnToast(mContext, "Records Deleted Successfully");
				else
					ToastUtils.showOwnToast(mContext, "Record Deleted Successfully");
				dataList.clear();
				dataList.addAll(new PayoutDescTable(mContext).getAllInfoFromTableDefalut());
				mAdapter.notifyDataSetChanged();
			}
			else
				ToastUtils.showOwnToast(mContext, "Select Any Description Before Delete");

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public class MainAdapter extends ArrayAdapter<CommentModel> {

		private LayoutInflater mInflater;
		private List<CommentModel> dataList;

		private class Holder {
			public CheckedTextView textview;
		}

		public MainAdapter(List<CommentModel> dataList) {
			super(mContext, 0, dataList);
			this.mInflater  = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.dataList   = dataList;
		}

		@Override
		public View getView(final int position, View convertView,ViewGroup parent) {
			final CommentModel commentModel = this.dataList.get(position);
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

			holder.textview.setText(commentModel.getCommentString());
			holder.textview.setChecked(commentModel.isCommentSelected());

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
		dataList.get(position).setCommentSelected(!dataList.get(position).isCommentSelected());	
		mAdapter.notifyDataSetChanged();
	}
}
