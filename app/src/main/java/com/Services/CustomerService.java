package com.Services;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.Beans.CustomerModel;
import com.posimplicity.database.CustomerTable;
import com.utils.JSONObJValidator;

import android.content.Context;

public class CustomerService {

	public static void parseCustomerDataWhenAdd (String responseData, Context mContext) {

		List<CustomerModel> listOfCustomer  = new ArrayList<>();
		try {
			JSONObject custdata       = new JSONObject(responseData);			
			JSONArray custDetails     = custdata.getJSONArray("customer_list");
			int lengthOfCustomerArray = custDetails.length();
			if(lengthOfCustomerArray > 0){				

				for (int index = 0; index < lengthOfCustomerArray; index++) {

					JSONObject jsonObject = custDetails.getJSONObject(index);

					String customerId          = JSONObJValidator.stringTagValidate(jsonObject, "customer_id","-1");
					String firstName           = JSONObJValidator.stringTagValidate(jsonObject, "firstname", "");
					String lastName            = JSONObJValidator.stringTagValidate(jsonObject, "lastname", "");
					String emailAddress        = JSONObJValidator.stringTagValidate(jsonObject, "email", "");
					String telephoneNo         = JSONObJValidator.stringTagValidate(jsonObject, "telephone", "111111");
					String permanantAddress    = JSONObJValidator.stringTagValidate(jsonObject, "address", "Not Provided"); 
					String groupId             = JSONObJValidator.stringTagValidate(jsonObject, "group_id", CustomerTable.DEFAULT_GROUP_ID);

					CustomerModel customer = new CustomerModel(customerId, firstName, lastName, emailAddress, telephoneNo, permanantAddress,groupId,false,false);
					listOfCustomer.add(customer);
				}
				new CustomerTable(mContext).addInfoListInTable(listOfCustomer);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean parseCustomerDataWhenAddSales(String responseData, Context mContext) {
		boolean recordInsert = false ;

		List<CustomerModel> listOfCustomer  = new ArrayList<>();
		try {
			JSONObject custdata       = new JSONObject(responseData);			
			JSONArray custDetails     = custdata.getJSONArray("customer_list");
			int lengthOfCustomerArray = custDetails.length();
			if(lengthOfCustomerArray > 0){				

				for (int index = 0; index < lengthOfCustomerArray; index++) {

					JSONObject jsonObject = custDetails.getJSONObject(index);

					String customerId          = JSONObJValidator.stringTagValidate(jsonObject, "customer_id","-1");
					String firstName           = JSONObJValidator.stringTagValidate(jsonObject, "firstname", "");
					String lastName            = JSONObJValidator.stringTagValidate(jsonObject, "lastname", "");
					String emailAddress        = JSONObJValidator.stringTagValidate(jsonObject, "email", "");
					String telephoneNo         = JSONObJValidator.stringTagValidate(jsonObject, "telephone", "111111");
					String permanantAddress    = JSONObJValidator.stringTagValidate(jsonObject, "address", "Not Provided"); 
					String groupId             = JSONObJValidator.stringTagValidate(jsonObject, "group_id",CustomerTable.DEFAULT_GROUP_ID);

					CustomerModel customer = new CustomerModel(customerId, firstName, lastName, emailAddress, telephoneNo, permanantAddress,groupId,false,false);
					listOfCustomer.add(customer);
					recordInsert = true;
				}
				new CustomerTable(mContext).addInfoListInTable(listOfCustomer);
			}

		} catch (Exception e) {
			e.printStackTrace();
			recordInsert = false;
		}
		
		return recordInsert;
	}
}