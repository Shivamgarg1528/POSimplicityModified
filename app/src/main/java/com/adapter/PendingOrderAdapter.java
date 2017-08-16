package com.adapter;

import java.util.List;
import com.Beans.PendingOrderModel;
import com.posimplicity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PendingOrderAdapter extends BaseAdapter{

	private Context mContext;
	private List<PendingOrderModel> dataList;
	private LayoutInflater layoutInf;



	public PendingOrderAdapter(Context mContext, List<PendingOrderModel> dataList) {
		super();
		this.mContext = mContext;
		this.dataList = dataList;
		layoutInf     = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public PendingOrderModel getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		PendingHolder holder = null;
		if(convertView == null)
		{
			holder           = new PendingHolder();
			convertView      = layoutInf.inflate(R.layout.pending_id_list_layout, null);
			holder.tableNo   = (TextView)convertView.findViewById(R.id.Pending_Id_List_Layout_Tv_Table_No);
			holder.clerkName = (TextView)convertView.findViewById(R.id.Pending_Id_List_Layout_Tv_ClerkName);
			convertView.setTag(holder);
		}
		else
			holder  = 	(PendingHolder) convertView.getTag();
		try {			
			PendingOrderModel listItem = getItem(position);
			holder.tableNo.setText(listItem.getTableOrClerkShipToNameModel().getFirstName());
			holder.clerkName.setText(listItem.getCustomerOrClerkBillToNameModel().getFirstName());
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return convertView;
	}

	static class PendingHolder {
		TextView tableNo,clerkName; 
	}
}

