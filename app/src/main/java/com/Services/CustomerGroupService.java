package com.Services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.Beans.CustomerGroup;
import com.posimplicity.database.CustomerGroupTable;
import com.posimplicity.database.CustomerTable;
import com.utils.JSONObJValidator;

import android.content.Context;

public class CustomerGroupService {

	public static void parseCustomerGroupDataWhenAdd(String responseData,Context mContext) {

		List<CustomerGroup> listOfCustomer  = new ArrayList<>();
		try {
			JSONObject custdata       = new JSONObject(responseData);			
			JSONArray custDetails     = custdata.getJSONArray("Group_Details");
			int lengthOfCustomerArray = custDetails.length();

			if(lengthOfCustomerArray > 0){				

				for (int index = 0; index < lengthOfCustomerArray; index++) {

					JSONObject jsonObject = custDetails.getJSONObject(index);

					String customerGroupId          = JSONObJValidator.stringTagValidate(jsonObject, "customer_group_id","-1");
					String customerGroupName        = JSONObJValidator.stringTagValidate(jsonObject, "customer_group_code", "None");

					CustomerGroup customerGroup = new CustomerGroup(customerGroupId, customerGroupName);
					
					if(!customerGroupId.equalsIgnoreCase(CustomerTable.DEFAULT_GROUP_ID))
						listOfCustomer.add(customerGroup);
				}

				new CustomerGroupTable(mContext).addInfoListInTable(listOfCustomer);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
