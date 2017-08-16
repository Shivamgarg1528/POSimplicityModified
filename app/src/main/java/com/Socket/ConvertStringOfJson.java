package com.Socket;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.Beans.CheckOutParentModel;
import com.Beans.ProductModel;
import com.Beans.RelationalOptionModel;
import com.Beans.SubOptionModel;
import com.utils.POSApp;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;

public class ConvertStringOfJson {

	public static final String STORE               = "storeName";
	public static final String APP_ID_KEY          = "AppID";
	public static final String APP_ID_VALUE        = "POSimplicity";
	public static final String APP_ID_A_VALUE      = "POSExtension";
	public static final String CHANNEL             = "Channel";
	public static final String PRODUCT_ID          = "ProductId";
	public static final String OPTION_ID           = "OptionId";
	public static final String Sub_OPTION_ID       = "SubOptionId";
	public static final String CHILD_ID            = "ChildId";
	public static final String PRODUCT_Pr          = "ProductPrice";
	public static final String CHILD_Pr            = "OptionPrice";
	public static final String PRODUCT_QTY         = "ProductQty";
	public static final String DISCOUNT            = "DiscountAmt";
	public static final String DISCOUNT_TYPE       = "DiscountType";
	public static final String CUSTOM_TEXT         = "CustomText";
	public static final String OPTION_DET          = "ProductOptionDetails";	
	public static final String TRANS_ID            = "TransId";
	public static final String DATE_TIME           = "DateAndTime";
	public static final String KEY_DISCOUNT_TYPE   = "DiscountType";


	/**
	 *       Here I am Specify all the the channels...
	 */


	public static final String ADD_PRODUCT      = "Add_Product";
	public static final String DELETE_PRODUCT   = "DeleteProduct";
	public static final String INCRE_QTY        = "IncQty";
	public static final String DISCOUNT_ITEM    = "ProductLevelDiscount";
	public static final String TENDER_TAP       = "TenderTap";
	public static final String RESET_DATA       = "ResetData";
	public static final String FULL_PAY         = "FullPayment";
	public static final String TRANSACTION_TIME = "TransactionTime";
	public static final String DISCOUNT_ORDER   = "OrderLevelDiscount";



	private POSApp gApp           = POSApp.getInstance();
	public Context mContext;
	public String storeName = "";

	public static final int SOCKET_ADD_PRODUCT          = 0x001;
	public static final int SOCKET_DEL_PRODUCT          = 0x002;
	public static final int SOCKET_INC_QTY              = 0x003;
	public static final int SOCKET_DISCOUNT             = 0x004;

	public static final int ORDER_DISCOUNT_PER          = 0x005;
	public static final int ORDER_DISCOUNT_DOLL         = 0x006;


	public ConvertStringOfJson(Context mContext) {
		this.mContext  = mContext;
		this.storeName = MyPreferences.getMyPreference(STORE, mContext); 
	}

	public void onProductModification(CheckOutParentModel checkoutParentModel ,int modifyRequest)  // When An ProductList Has No Childs
	{	
		try{
			ProductModel  product                = checkoutParentModel.getProduct();
			List<RelationalOptionModel> childs   = checkoutParentModel.getChilds();
			JSONObject firstChild                = new JSONObject();

			firstChild.put(APP_ID_KEY     , APP_ID_VALUE);
			firstChild.put(STORE          , storeName);

			switch (modifyRequest) {

			case SOCKET_ADD_PRODUCT:
				firstChild.put(CHANNEL        , ADD_PRODUCT);
				break;

			case SOCKET_DEL_PRODUCT:
				firstChild.put(CHANNEL        , DELETE_PRODUCT);
				break;

			case SOCKET_INC_QTY:
				firstChild.put(CHANNEL        , INCRE_QTY);
				break;

			case SOCKET_DISCOUNT:
				firstChild.put(CHANNEL        , DISCOUNT_ITEM);
				break;

			default:
				break;
			}


			if (!checkoutParentModel.getExtraArgument().isSurageApplicable()) {

				String disountByItem          = MyStringFormat.formatWith2DecimalPlaces(Float.parseFloat(product.getProductDisAmount()) / Float.parseFloat(product.getProductQty()));
				firstChild.put(PRODUCT_ID     ,  product.getProductId());
				firstChild.put(PRODUCT_QTY    ,  product.getProductQty());
				firstChild.put(PRODUCT_Pr     ,  product.getProductPrice());
				firstChild.put(DISCOUNT       ,  disountByItem);
				firstChild.put(CHILD_Pr       ,  product.getProductOptionsPrice());


				JSONArray productDetailsArray = new JSONArray();
				for(int value = childs.size()-1; value >= 0 ; value--) {

					JSONObject innerObj                      = new JSONObject();
					RelationalOptionModel productOptionItems = childs.get(value);
					String optionId                          = productOptionItems.getOptionModel().getOptionId();
					String allSuboptionIdBasedOnOptionId     = findAllSubOptionId(productOptionItems.getListOfSubOptionModel());
					innerObj.put(OPTION_ID,     ""+optionId);
					innerObj.put(Sub_OPTION_ID, ""+allSuboptionIdBasedOnOptionId);
					productDetailsArray.put(innerObj);							

				}
				firstChild.put(OPTION_DET, productDetailsArray);
				sendDataOverSocket(firstChild);
			}			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void sendDataOverSocket(JSONObject firstChild){	

		//System.out.println(firstChild.toString());
		if(gApp.getSocketIo() != null && gApp.getSocketIo().nothingIsNull()){
			gApp.getSocketIOClient().emit(firstChild);
		}
	}


	public void onTendertap() {		
		try{
			JSONObject firstChild = new JSONObject();
			firstChild.put(APP_ID_KEY     , APP_ID_VALUE);
			firstChild.put(STORE          , storeName);
			firstChild.put(CHANNEL        , TENDER_TAP);			
			firstChild.put(CUSTOM_TEXT    , "Please Hand \n\nPayment to Cashier");
			sendDataOverSocket(firstChild);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void onFullPayment() {

		try{
			JSONObject firstChild = new JSONObject();
			firstChild.put(APP_ID_KEY     , APP_ID_VALUE);
			firstChild.put(STORE          , storeName);
			firstChild.put(CHANNEL        , FULL_PAY);			
			firstChild.put(CUSTOM_TEXT    , "Thank You \n For Your Purchage \n\n\nCome Again Soon");
			sendDataOverSocket(firstChild);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public String findAllSubOptionId(List<SubOptionModel> childs){
		StringBuilder stringBuilder = new StringBuilder();
		for(int value = childs.size()-1; value >= 0 ; value--) {
			SubOptionModel subOptionModel = childs.get(value);
			stringBuilder.append((stringBuilder.length() == 0) ? subOptionModel.getSubOptionId() :","+subOptionModel.getSubOptionId());
		}
		return stringBuilder.toString();
	}

	public void onClearList() {

		try{
			JSONObject firstChild = new JSONObject();
			firstChild.put(APP_ID_KEY     , APP_ID_VALUE);
			firstChild.put(STORE          , storeName);
			firstChild.put(CHANNEL        ,RESET_DATA );			
			sendDataOverSocket(firstChild);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}


	}

	public void onDateAndTransactionNo(String timeAndDate, String transId) {


		try{
			JSONObject firstChild = new JSONObject();
			firstChild.put(APP_ID_KEY     , APP_ID_VALUE);
			firstChild.put(STORE          , storeName);
			firstChild.put(CHANNEL        , TRANSACTION_TIME);
			firstChild.put(DATE_TIME        ,timeAndDate);
			firstChild.put(TRANS_ID        , transId);
			sendDataOverSocket(firstChild);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void onOrderDiscount(String discountValue, int discountType) {

		try{
			JSONObject firstChild = new JSONObject();
			firstChild.put(APP_ID_KEY                , APP_ID_VALUE);
			firstChild.put(STORE                     , storeName);
			firstChild.put(CHANNEL                   , DISCOUNT_ORDER);
			firstChild.put(KEY_DISCOUNT_TYPE         ,""+discountType);
			firstChild.put(DISCOUNT                  , discountValue);
			sendDataOverSocket(firstChild);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
