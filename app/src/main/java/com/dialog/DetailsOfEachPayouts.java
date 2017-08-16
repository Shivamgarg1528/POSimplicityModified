package com.dialog;

import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.utils.CalculateWidthAndHeigth;
import com.utils.POSApp;
import com.posimplicity.R;

public class DetailsOfEachPayouts {

	public  void onDetailsOfEachPayouts(final Context _context,List<String> dataList,List<String> descpList,float totalAmount,String payoutNames){

		POSApp globalApp = POSApp.getInstance();
		int width   = CalculateWidthAndHeigth.calculatingWidthAndHeight(globalApp.getDeviceWidth(), 90);

		Typeface typeface = Typeface.createFromAsset(_context.getAssets(), "fonts/HelveticaLTStd-Bold.otf");
		AlertDialog.Builder builder = new AlertDialog.Builder(_context);
		builder.setIcon(R.drawable.app_icon).setMessage(payoutNames +" Details: ").setTitle(R.string.app_name);

		LinearLayout linearLayout = new LinearLayout(_context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		final TextView desc = new TextView(_context);
		desc.setTextColor(Color.BLACK);
		desc.setGravity(Gravity.CENTER);
		desc.setText("No Details Available for "+payoutNames);
		desc.setTypeface(typeface);
		
		if(dataList.size()<1)
			linearLayout.addView(desc);
		
		linearLayout.setPadding(100, 0, 100, 0);


		int count = 1;
		for(int index = 0 ;index < dataList.size(); index++){

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			LinearLayout detailsLL = new LinearLayout(_context);
			detailsLL.setOrientation(LinearLayout.HORIZONTAL);

			TextView tv1 = new TextView(_context);
			tv1.setLayoutParams(lp);
			tv1.setTextColor(Color.BLACK);
			tv1.setGravity(Gravity.CENTER_VERTICAL);
			tv1.setText("Description "+ count +" : ");
			tv1.setTypeface(typeface);
			detailsLL.addView(tv1);

			TextView tv2 = new TextView(_context);
			tv2.setLayoutParams(lp);
			tv2.setTextColor(Color.BLACK);
			tv2.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
			tv2.setText(dataList.get(index));
			tv2.setTypeface(typeface);
			detailsLL.addView(tv2);

			TextView tv3 = new TextView(_context);
			tv3.setLayoutParams(lp);
			tv3.setTextColor(Color.BLACK);
			tv3.setGravity(Gravity.CENTER_VERTICAL);
			tv3.setText(descpList.get(index));
			tv3.setPadding(30, 0, 0, 0);
			tv3.setTypeface(typeface);
			detailsLL.addView(tv3);			

			linearLayout.addView(detailsLL);
			count++;

		}

		ScrollView sl = new ScrollView(_context);
		sl.addView(linearLayout);

		builder.setCancelable(false);
		builder.setView(sl);
		builder.setPositiveButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		AlertDialog al = builder.create();		
		al.setCanceledOnTouchOutside(false);		
		al.getWindow().setLayout(width, -1);
		WindowManager.LayoutParams wmlp = al.getWindow().getAttributes();
		wmlp.gravity = Gravity.CENTER;
		al.show();
	}

	public  String onReformatPayoutName(String textString) {
		StringBuilder strBld = new StringBuilder(textString);
		int stringLegth      = strBld.length();
		int spaceNeed        = Math.abs(25 - stringLegth);
		for(int index = 0 ; index < spaceNeed ; index++)
			strBld.append(" ");
		return strBld.toString();  
	}

	public  String onReformatDesc(String textString) {
		StringBuilder strBld = new StringBuilder(textString);
		for(int index = 0 ; index < 5 ; index++)
			strBld.insert(0, " ");
		return strBld.toString();
	}

	public  String onReformatPriceName(String textString) {
		StringBuilder strBld = new StringBuilder(textString);
		int stringLegth      = strBld.length();
		int spaceNeed        = 10 - stringLegth;
		for(int index = 0 ; index < spaceNeed ; index++)
			strBld.insert(0, " ");
		return strBld.toString();
	}
}
