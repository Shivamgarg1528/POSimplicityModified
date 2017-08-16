package com.RecieptPrints;

import java.util.List;
import android.content.Context;
import com.Beans.ExtraProductArgument;
import com.Beans.CheckOutParentModel;
import com.Beans.ProductModel;
import com.Beans.RelationalOptionModel;
import com.Beans.SubOptionModel;
import com.PosInterfaces.PrefrenceKeyConst;
import com.SetupPrinter.BasePR;
import com.utils.CurrentDate;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;
import com.utils.Variables;
import com.posimplicity.HomeActivity;

public class KitchenReceipt implements PrefrenceKeyConst {
	
	private static final String DEFAULT_HEADER  = "KITCHEN ORDER";
	private static final String DEFAULT_FOOTER  = "---THANK YOU---";
	private static final String COMMENT         = "Comment:  ";

	public static void onPrintKitchenReciept(Context mContext,HomeActivity instance , BasePR basePR) {
		
		basePR.playBuzzer();
		basePR.largeText();
		basePR.printData(PrintSettings.onFormatHeaderAndFooter(DEFAULT_HEADER)+"\n");
		basePR.smallText();

		String formattedString = CurrentDate.returnCurrentDateWithTime();
		basePR.printData(formattedString);

		formattedString        = MyPreferences.getMyPreference(MOST_RECENTLY_TRANSACTION_ID, mContext);
		basePR.printData("TransID  == "+formattedString);

		if(!Variables.tableID.isEmpty())
			basePR.printData("TableID  == "+Variables.tableID);

		if(!Variables.customerName.isEmpty())
			basePR.printData("Name     == "+Variables.customerName);

		basePR.printData("\n");

		for (int index = 0; index < instance.dataList.size(); index ++) {

			CheckOutParentModel   parent                    = instance.dataList.get(index);		
			ProductModel  product                      		= parent.getProduct();
			ExtraProductArgument extraAg          			= parent.getExtraArgument();
			List<RelationalOptionModel> listOfchilds 	    = parent.getChilds();

			String  itemQty    = "";
			int positiveQty    = (Integer.parseInt(product.getProductQty())-Integer.parseInt(product.getProductQtyOnPendingTime()));
			if(positiveQty <= 0)
				itemQty    = "" + product.getProductQty();
			else
				itemQty    = "" + positiveQty;				

			String productName = product.getProductName();
			float newPrice     = (Integer.parseInt(itemQty)) * (Float.parseFloat(product.getProductPrice())  + Float.parseFloat(product.getProductOptionsPrice())  - Float.parseFloat(product.getProductDisAmount()));

			formattedString    = callFormatMethod(productName,itemQty,newPrice);

			if(Variables.isPendingOrderItems && extraAg.isPendingItems())
				basePR.printData(formattedString);
			else if(Variables.isPendingOrderItems && !extraAg.isPendingItems()){}
			else 
				basePR.printData(formattedString);

			if (!listOfchilds.isEmpty()) {
				for(int count = 0; count < listOfchilds.size(); count ++){
					List<SubOptionModel> subOptionModels = listOfchilds.get(count).getListOfSubOptionModel();
					int sizeOfEachList    = subOptionModels.size();

					for(int index1 = 0 ; index1 < sizeOfEachList ;index1++){
						SubOptionModel subOptionModel = subOptionModels.get(index1);
						productName   = subOptionModel.getSubOptionName();
						if(Variables.isPendingOrderItems && extraAg.isPendingItems())
							basePR.printData(PrintSettings.onReformatName(productName));
						else if(Variables.isPendingOrderItems && !extraAg.isPendingItems()){}
						else 
							basePR.printData(PrintSettings.onReformatName(productName));
					}}
			}		
		}
		
		if (!Variables.orderComment.isEmpty()) {
			basePR.largeText();
			basePR.printData("\n"+COMMENT);
			basePR.smallText();
			basePR.printData(Variables.orderComment);
		}

		basePR.largeText();
		basePR.printData("\n"+PrintSettings.onFormatHeaderAndFooter(DEFAULT_FOOTER)+"\n");
		basePR.cutterCmd();
	}

	private static String callFormatMethod(String headerText,String qty,Object price) {
		return PrintSettings.onReformatName(headerText) + PrintSettings.onReformatQty(qty) + PrintSettings.onReformatPrice(MyStringFormat.formatWith2DecimalPlaces(price));
	}
}
