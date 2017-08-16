package com.Dialogs;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Beans.CheckOutParentModel;
import com.Beans.ProductModel;
import com.Beans.RelationalOptionModel;
import com.Beans.SubOptionModel;
import com.Socket.ConvertStringOfJson;
import com.utils.InputCalculation;
import com.utils.MyStringFormat;
import com.posimplicity.HomeActivity;
import com.posimplicity.R;

public class ProductQtyDialog extends BaseDialog {

	private CheckOutParentModel itemDetail;
	private ProductModel clickedProduct;
	private List<RelationalOptionModel> productItems;
	private TextView qtyOfItemsTv,itemNameTV,itemPriceTv;
	private HomeActivity instance;
	private StringBuilder enteredStringBld;
	@SuppressWarnings("unused")
	private ImageView productImage;
	private final int ADD_CHAR = 0;
	private final int REM_CHAR = 1;
	int qtyOfProdcut = 0;
	float sumOfItemsPrice = 0.0f;

	public ProductQtyDialog(Context context, int theme, int width, int height,boolean isOutSideTouch, boolean isCancelable, int layoutId) {
		super(context, theme, width, height, isOutSideTouch, isCancelable, layoutId);
		enteredStringBld = new StringBuilder("000");
		instance         = HomeActivity.localInstance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		qtyOfItemsTv        = findViewByIdAndCast(R.id.QtyofItems);		
		itemNameTV          = findViewByIdAndCast(R.id.itemNametextview);		
		itemPriceTv         = findViewByIdAndCast(R.id.itemPricetextview);
		productImage        = findViewByIdAndCast(R.id.itemImage);

		//productImage.setBackground(new BitmapDrawable(mContext.getResources(), ImageProcessing.getImageFromSdCard(clickedProduct.getProductId(),ImageProcessing.FOLDER_NAME)));;

		//productImage.setBackgroundResource(R.drawable.beverage_);
		itemNameTV.setText(clickedProduct.getProductName());

		StringBuilder lengthOfQtyString = new StringBuilder(clickedProduct.getProductQty());

		if(lengthOfQtyString.length()     == 1)
			lengthOfQtyString.insert(0, "00");
		else if(lengthOfQtyString.length() == 2)
			lengthOfQtyString.insert(0, "0");

		qtyOfItemsTv.setText(lengthOfQtyString.toString());
		qtyOfProdcut = Integer.parseInt(lengthOfQtyString.toString());

		for(int index = 0; index < productItems.size(); index++){
			List<SubOptionModel> listOfSubOption = productItems.get(index).getListOfSubOptionModel();
			for(int value = listOfSubOption.size() -1 ; value >= 0; value --){
				sumOfItemsPrice += Float.parseFloat(listOfSubOption.get(value).getSubOptionPrice());
			}
		}
		itemPriceTv.setText("$ "+clickedProduct.getProductCalAmount());
	}
	public void onSendData(CheckOutParentModel wholeInfoOfAnyClickedProduct){
		itemDetail     = wholeInfoOfAnyClickedProduct;
		productItems   = itemDetail.getChilds();
		clickedProduct = itemDetail.getProduct();
	}

	private void setTextOnView(String input, int keyPurpose){
		if(keyPurpose == 0){
			qtyOfItemsTv.setText(InputCalculation.onKeyDownWithOutDot(enteredStringBld, input));
		}
		else if(keyPurpose == 1)
			qtyOfItemsTv.setText(InputCalculation.onKeyUpWithOutDot(enteredStringBld));
		qtyOfProdcut = Integer.parseInt(qtyOfItemsTv.getText().toString());

		float calculatedValue = qtyOfProdcut * ( sumOfItemsPrice + Float.parseFloat(clickedProduct.getProductPrice()));
		itemPriceTv.setText("$ "+MyStringFormat.formatWith2DecimalPlaces(calculatedValue));
	}


	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button1:
			setTextOnView("1",ADD_CHAR);
			break;

		case R.id.button2:
			setTextOnView("2",ADD_CHAR);
			break;

		case R.id.button3:
			setTextOnView("3",ADD_CHAR);
			break;

		case R.id.button4:
			setTextOnView("4",ADD_CHAR);
			break;

		case R.id.button5:
			setTextOnView("5",ADD_CHAR);
			break;

		case R.id.button6:
			setTextOnView("6",ADD_CHAR);
			break;

		case R.id.button7:
			setTextOnView("7",ADD_CHAR);
			break;

		case R.id.button8:
			setTextOnView("8",ADD_CHAR);
			break;

		case R.id.button9:
			setTextOnView("9",ADD_CHAR);
			break;

		case R.id.button0:
			setTextOnView("0",ADD_CHAR);
			break;

		case R.id.button00:
			setTextOnView("00",ADD_CHAR);
			break;
		case R.id.backSpace:
			setTextOnView("1",REM_CHAR);
			break;

		case R.id.cancel_dialog:			
			dismiss();
			break;


		case R.id.addItem:

			if(qtyOfProdcut > 0) {		

				clickedProduct.setProductQty(""+qtyOfProdcut);
				clickedProduct.getProductCalAmount();
				instance.myAdapter.notifyDataSetChanged();
				instance.calculateSubTotalEachTime();
				dismiss();
				new ConvertStringOfJson(mContext).onProductModification(itemDetail, ConvertStringOfJson.SOCKET_INC_QTY);
			}
			else
				Toast.makeText(mContext, "Add Qty. of Items", Toast.LENGTH_LONG).show();
			break;
		}
	}
}