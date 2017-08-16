package com.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.Beans.ProductModel;
import com.posimplicity.database.ProductTable;
import com.posimplicity.HomeActivity;
import com.posimplicity.R;
import com.squareup.picasso.Picasso;

public class CreateControls {

	private Context mContext;	
	private HomeActivity instance;
	private POSApp gApplication;
	private final int PRODUCT_COUNT    = 40;

	public CreateControls(Context mContext) {
		super();
		this.mContext = mContext;	
		this.instance = (HomeActivity) mContext;
		gApplication = POSApp.getInstance();
	}

	public void onCreateBottomPanel()
	{		
		int counter   = 0;
		int loopValue = 0;

		int maxButtons     = (Math.max(instance.numberOfDepart, instance.numberOfSysBtn));
		int widthOfButton  = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApplication.getDeviceWidth(), 12.07);
		int heightOfButton = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApplication.getDeviceHeight(), 9);

		for (int index=0; index<instance.numberOfPages; index++) {

			LinearLayout parentLinear = new LinearLayout(mContext); parentLinear.setId(index); instance.keysLayoutList.add(parentLinear); parentLinear.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
			parentLinear.setOrientation(LinearLayout.VERTICAL);
			LinearLayout    secondChilds   = new LinearLayout(mContext);     secondChilds.setId(index);  instance.sysKeysRowList.add(secondChilds);   secondChilds.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			secondChilds.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout    firstChilds  = new LinearLayout(mContext);    firstChilds.setId(index);  instance.deptKeysRowList.add(firstChilds); firstChilds.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			firstChilds.setOrientation(LinearLayout.HORIZONTAL);
			for (int value = loopValue; value < maxButtons; value++) {

				if(value<instance.numberOfDepart){
					TextView button = new TextView(mContext);
					button.setLayoutParams(new LayoutParams(widthOfButton,heightOfButton)); //linearLayoutWidth/5,Integer.parseInt(getResources().getString(R.string.Image_height_for_departmental))
					button.setId(Integer.parseInt(instance.deptList.get(value).getDeptId()));
					button.setTag("deptartmentClick");	
					button.setText(instance.deptList.get(value).getDeptName());
					button.setSingleLine();
					button.setGravity(Gravity.CENTER);

					/*button.setSelected(true);					
					button.setFocusable(true);
					button.setFocusableInTouchMode(true);
					button.setEllipsize(TruncateAt.MARQUEE);
					button.setMarqueeRepeatLimit(-1);
					 */		

					button.setTextColor(Color.parseColor("#484848"));
					button.setBackgroundResource(R.drawable.btn_blue);
					button.setTextSize(TypedValue.COMPLEX_UNIT_SP, mContext.getResources().getDimension(R.dimen.textAppearance_mdpi_07_sp));
					button.setOnClickListener(instance);
					Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/HelveticaLTStd-Bold.otf");
					button.setTypeface(typeface);
					firstChilds.addView(button);					
				}

				if(value<instance.numberOfSysBtn){						
					Button button = new Button(mContext);
					button.setLayoutParams(new LayoutParams(widthOfButton,heightOfButton)); //linearLayoutWidth/5,Integer.parseInt(getResources().getString(R.string.Image_height_for_departmental))
					if (counter < 2) {
						button.setBackgroundResource(R.drawable.btn_green);
					} else if (counter == 2) {
						button.setBackgroundResource(R.drawable.btn_yellow);
					} else {
						button.setBackgroundResource(R.drawable.btn_orange);
					}
					button.setId(value);
					button.setTag(instance.sytemKeyTags[value]);
					button.setText(instance.systemKeyArr[value]);
					button.setTextColor(Color.parseColor("#484848"));
					button.setSingleLine();
					button.setOnClickListener(instance);
					button.setTextSize(TypedValue.COMPLEX_UNIT_SP, mContext.getResources().getDimension(R.dimen.textAppearance_mdpi_07_sp));
					secondChilds.addView(button);		
					Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/HelveticaLTStd-Bold.otf");
					button.setTypeface(typeface);
				}

				if(instance.numberOfPages == 2 && index == 1){
					if(instance.numberOfDepart <= 5) {
						Button button = new Button(mContext);
						button.setLayoutParams(new LayoutParams(widthOfButton*5,heightOfButton));						
						firstChilds.addView(button);
						button.setVisibility(View.INVISIBLE);
					}
				}
				counter++;
				if (counter == 5) {
					break;
				}
			}

			counter = 0;
			loopValue = loopValue + 5;
			parentLinear.addView(firstChilds);
			parentLinear.addView(secondChilds);
			instance.mPager.addView(parentLinear);
		}
	}

	public void onCreateProductPanel(int deptId) {	

		instance.productLayout.removeAllViews();		
		int noOfProducts,numberOfRows,count = 0,loopvariable = 0,totalProductShown=0;
		instance.productList   = new ProductTable(mContext).getAllInfoFromTableBasedOnCategoryID(""+deptId);
		noOfProducts = instance.productList.size();

		if(noOfProducts >= PRODUCT_COUNT){
			numberOfRows = onNumberOfRows(noOfProducts); totalProductShown +=noOfProducts;                 }
		else{
			numberOfRows = 5;                            totalProductShown +=PRODUCT_COUNT;                 }		

		int parentWidth    = CalculateWidthAndHeigth.calculatingWidthAndHeight(gApplication.getDeviceWidth(),60.4);
		int parentHeight   = parentWidth/8; 
		int withAndHeight  = CalculateWidthAndHeigth.calculatingWidthAndHeight(parentHeight,97);

		if(noOfProducts != 0) {

			for (int value = 0; value < numberOfRows; value ++) {				
				LinearLayout parentLinear = new LinearLayout(mContext);
				parentLinear.setId(value);
				parentLinear.setLayoutParams(new LayoutParams(parentWidth,parentHeight));				
				parentLinear.setOrientation(LinearLayout.HORIZONTAL);
				parentLinear.setGravity(Gravity.CENTER);

				for (int index = count; index < totalProductShown; index++) {

					TextView productButton       = new TextView (mContext);
					ImageView img                = new ImageView(mContext);
					LinearLayout linearLayout  = new LinearLayout(mContext);
					linearLayout.setLayoutParams(new LayoutParams(parentHeight,parentHeight));
					linearLayout.setGravity(Gravity.CENTER);
					productButton.setLayoutParams(new LayoutParams(withAndHeight,withAndHeight));
					img.setLayoutParams(new LayoutParams(withAndHeight,withAndHeight));
					
					if (index<noOfProducts) {

						ProductModel localproduct = instance.productList.get(index);
						String imageLabel         = localproduct.getProductImageText();
						
						if(localproduct.isProductImageShown()){

							img.setId(Integer.parseInt(localproduct.getProductId()));
							img.setTag("productClick");						
							img.setOnClickListener(instance);
							img.setBackgroundColor(Color.WHITE);
							linearLayout.addView(img);
							Picasso.with(mContext).load(localproduct.getProductImageUrl()).into(img);
						}
						else{
							productButton.setId(Integer.parseInt(localproduct.getProductId()));
							productButton.setTag("productClick");						
							productButton.setOnClickListener(instance);
							if(imageLabel.isEmpty())
								productButton.setText(localproduct.getProductName());
							else
								productButton.setText(imageLabel);

							productButton.setTextColor(Color.parseColor("#484848"));
							Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/HelveticaLTStd-Bold.otf");
							productButton.setTypeface(typeface);
							productButton.setBackgroundColor(Color.WHITE);
							productButton.setGravity(Gravity.CENTER_HORIZONTAL);
							linearLayout.addView(productButton);
						}
					}
					else{
						productButton.setBackgroundColor(Color.WHITE);
						linearLayout.addView(productButton);
					}
					
					parentLinear.addView(linearLayout);
					loopvariable++;					
					if(loopvariable == 8) {						
						break;
					}					
				}
				loopvariable=0;
				count += 8;
				instance.productLayout.addView(parentLinear);
			}			
		}
		else
			Toast.makeText(mContext, "No ProductList's Under Department", Toast.LENGTH_SHORT).show();
		instance.productLinearLayoutContaier.fullScroll(ScrollView.FOCUS_UP);
	}

	private int onNumberOfRows(int noOfProducts) {		
		int numberOfRows;
		if(noOfProducts %8 == 0)                     // Calculation for numbers of Rows 
			numberOfRows = noOfProducts/8;
		else
			numberOfRows = noOfProducts/8 + 1;
		return numberOfRows;
	}
}
