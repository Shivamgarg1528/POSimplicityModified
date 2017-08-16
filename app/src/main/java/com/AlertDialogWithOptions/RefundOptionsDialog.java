package com.AlertDialogWithOptions;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import com.dialog.RefundDialog;
import com.Fragments.MaintFragmentTender1;
import com.utils.TenderEnable;
import com.utils.Variables;
import com.posimplicity.R;

public class RefundOptionsDialog {

	private Context context;	
	private float subtotalAmt         = Variables.totalBillAmount;


	public RefundOptionsDialog(Context context) {
		super();
		this.context = context;
	}

	public void showRefundOptionsDialog() {


		final List<String> paymentModeNames = new ArrayList<>();
		paymentModeNames.add("Cash");
		paymentModeNames.add("Credit");
		paymentModeNames.add("Check");
		paymentModeNames.add("Gift");
		paymentModeNames.add("Rewards");
		
		if(TenderEnable.isCustom1TenderEnable(context))
			paymentModeNames.add(MaintFragmentTender1.getCustom2Name(context));
		
		if(TenderEnable.isCustom2TenderEnable(context))
			paymentModeNames.add(MaintFragmentTender1.getCustom2Name(context));
		
	
		
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice,paymentModeNames);

		AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);    
		builderSingle.setTitle("Select Any Refund Option :- ");
		builderSingle.setIcon(R.drawable.app_icon);

		builderSingle.setAdapter(arrayAdapter,  new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (which) {

				case 0:
					RefundDialog.showRefundDialog(context, subtotalAmt, 0.0f, 0.0f,0.0f,0.0f,0.0f,0.0f,paymentModeNames.get(0),0);
					break;

				case 1:
					RefundDialog.showRefundDialog(context, 0.0f, subtotalAmt, 0.0f,0.0f,0.0f,0.0f,0.0f,paymentModeNames.get(1),1);
					break;

				case 2:
					RefundDialog.showRefundDialog(context, 0.0f, 0.0f, subtotalAmt,0.0f,0.0f,0.0f,0.0f,paymentModeNames.get(2),2);
					break;
					
				case 3:
					RefundDialog.showRefundDialog(context, 0.0f, 0.0f, 0.0f,subtotalAmt,0.0f,0.0f,0.0f,paymentModeNames.get(3),3);
					break;
					
				case 4:
					RefundDialog.showRefundDialog(context, 0.0f, 0.0f, 0.0f,0.0f,subtotalAmt,0.0f,0.0f,paymentModeNames.get(4),4);
					break;
					
				case 5:
					RefundDialog.showRefundDialog(context, 0.0f, 0.0f, 0.0f,0.0f,0.0f,subtotalAmt,0.0f,paymentModeNames.get(5),5);
					break;

				case 6:
					RefundDialog.showRefundDialog(context, 0.0f, 0.0f, 0.0f,0.0f,0.0f,0.0f,subtotalAmt,paymentModeNames.get(6),6);
					break;

				default:
					break;
				}
			}
		});
		builderSingle.show();
	}
}
