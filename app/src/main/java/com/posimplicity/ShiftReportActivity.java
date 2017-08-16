package com.posimplicity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.Beans.ReportModel;
import com.dialog.DetailsOfEachPayouts;
import com.Beans.ReportsModel;
import com.adapter.ReportAdapter;
import com.posimplicity.database.local.ReportsTable;
import com.posimplicity.database.local.TipTable;
import com.Fragments.MaintFragmentTender1;
import com.RecieptPrints.PrintReports;
import com.RecieptPrints.PrintSettings;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.UsbPR;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ShiftReportActivity extends BaseActivity implements OnItemClickListener, OnClickListener{

	private ReportsModel shiftReportModel;	
	private ListView daliyReportListView;
	private ReportAdapter reportListAdapter;
	private List<ReportModel> reportListModel;
	private Button printBtn,newShiftBtn;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState,false,this);
		setContentView(R.layout.activity_shift_report_as_child);

		onInitViews();
		onListenerRegister();

		shiftReportModel = new ReportsTable(mContext).getReportModel(null,ReportsTable.SHIFT_REPORT);
		shiftReportModel.setTipAmount(new TipTable(mContext).getSumOfTipsWithOutDate());

		reportListModel.add(new ReportModel(ReportsTable.TOTAL_AMOUNT,			 shiftReportModel.getTotalAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.CASH_AMOUNT, 			 shiftReportModel.getCashAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.CREDIT_AMOUNT,     	 shiftReportModel.getCreditAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.CHECK_AMOUNT, 			 shiftReportModel.getCheckAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.GIFT_AMOUNT,   		 shiftReportModel.getGiftAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.REWARDS_AMOUNT,         shiftReportModel.getRewardAmount(), false));
		reportListModel.add(new ReportModel(MaintFragmentTender1.getCustom1Name(mContext), 			 shiftReportModel.getCustom1Amount(), false));
		reportListModel.add(new ReportModel(MaintFragmentTender1.getCustom2Name(mContext), 			 shiftReportModel.getCustom2Amount(), false));
		reportListModel.add(new ReportModel(ReportsTable.TAX_AMOUNT, 			 shiftReportModel.getTaxAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.TIP_AMOUNT,             shiftReportModel.getTipAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.LOTTERY_AMOUNT, 		 shiftReportModel.getLotteryAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.EXPENSES_AMOUNT, 		 shiftReportModel.getExpensesAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.SUPPLIES_AMOUNT,		 shiftReportModel.getSuppliesAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.PRODUCT_AMOUNT, 		 shiftReportModel.getProductAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.OTHER_AMOUNT,   		 shiftReportModel.getOtherAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.TIP_Pay_AMOUNT,   		 shiftReportModel.getTipPayAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.MANUAL_CASH_REFUND,     shiftReportModel.getManualCashRefund(), false));

		daliyReportListView.setOnItemClickListener(this);
		reportListAdapter.notifyDataSetChanged();
	}	

	@Override
	public void onInitViews() {

		daliyReportListView    = findViewByIdAndCast(R.id.Activity_ShiftReport_ListView_Items_);
		printBtn               = findViewByIdAndCast(R.id.Activity_ShiftReport_Button_Print_);
		newShiftBtn            = findViewByIdAndCast(R.id.Activity_ShiftReport_Button_New_Shift);

		reportListModel        = new ArrayList<>();
		reportListAdapter      = new ReportAdapter(mContext, reportListModel);

		daliyReportListView.setAdapter(reportListAdapter);
		shiftReportModel      = new ReportsModel();
	}

	@Override
	public void onListenerRegister() {

		printBtn.setOnClickListener(this);
		newShiftBtn.setOnClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

		if(position > 7){

			ReportModel rpModel           = reportListModel.get(position);
			String payoutName                 = rpModel.getName();
			List<String> listOFEachPayOut     = new ReportsTable(mContext).getListOfSomeInfoBasedOnDynamicValue(payoutName,ReportsTable.SHIFT_REPORT,"",payoutName);
			List<String> listOFEachPayOutDesc = new ReportsTable(mContext).getListOfSomeInfoBasedOnDynamicValue(ReportsTable.DESCRIPTION,ReportsTable.SHIFT_REPORT,"",payoutName);

			float totalAmont = sumOfAllValues(listOFEachPayOut);
			new DetailsOfEachPayouts().onDetailsOfEachPayouts(mContext, listOFEachPayOut, listOFEachPayOutDesc,totalAmont, payoutName);

		}
	}

	public float sumOfAllValues(List<String> listOFEachPayOut){
		float sumOfAmount = 0.0f;

		for(int index = listOFEachPayOut.size()-1 ; index >= 0 ; index -- ){
			sumOfAmount += Float.parseFloat(listOFEachPayOut.get(index));
		}
		return sumOfAmount;		
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.Activity_ShiftReport_Button_Print_:
			
			if(PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)){
				new UsbPR(mContext, new PrinterCallBack() {
					
					@Override
					public void onDisconnected() {}
					
					@Override
					public void onConnected(BasePR pPrinter) {
						new PrintReports().onPrintReport(ShiftReportActivity.this, pPrinter,shiftReportModel);
					}
					
				}).onConnectionStart();
			}
			
			if(PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)){				
				new PrintReports().onPrintReport(ShiftReportActivity.this,globalApp.getPrinterBT(),shiftReportModel);
			}

			break;

		case R.id.Activity_ShiftReport_Button_New_Shift:		

			shiftReportModel = new ReportsModel();
			shiftReportModel.setReportName(ReportsTable.SHIFT_REPORT);
			reportListModel.clear();
			reportListModel.add(new ReportModel(ReportsTable.TOTAL_AMOUNT,			 shiftReportModel.getTotalAmount(), false));
			reportListModel.add(new ReportModel(ReportsTable.CASH_AMOUNT, 			 shiftReportModel.getCashAmount(), false));
			reportListModel.add(new ReportModel(ReportsTable.CREDIT_AMOUNT,     	 shiftReportModel.getCreditAmount(), false));
			reportListModel.add(new ReportModel(ReportsTable.CHECK_AMOUNT, 			 shiftReportModel.getCheckAmount(), false));
			reportListModel.add(new ReportModel(ReportsTable.GIFT_AMOUNT,   		 shiftReportModel.getGiftAmount(), false));
			reportListModel.add(new ReportModel(ReportsTable.REWARDS_AMOUNT,         shiftReportModel.getRewardAmount(), false));
			reportListModel.add(new ReportModel(MaintFragmentTender1.getCustom1Name(mContext), 			 shiftReportModel.getCustom1Amount(), false));
			reportListModel.add(new ReportModel(MaintFragmentTender1.getCustom2Name(mContext), 			 shiftReportModel.getCustom2Amount(), false));
			reportListModel.add(new ReportModel(ReportsTable.TAX_AMOUNT, 			 shiftReportModel.getTaxAmount(), false));
			reportListModel.add(new ReportModel(ReportsTable.TIP_Pay_AMOUNT,         shiftReportModel.getTipAmount(), false));
			reportListModel.add(new ReportModel(ReportsTable.LOTTERY_AMOUNT, 		 shiftReportModel.getLotteryAmount(), false));
			reportListModel.add(new ReportModel(ReportsTable.EXPENSES_AMOUNT, 		 shiftReportModel.getExpensesAmount(), false));
			reportListModel.add(new ReportModel(ReportsTable.SUPPLIES_AMOUNT,		 shiftReportModel.getSuppliesAmount(), false));
			reportListModel.add(new ReportModel(ReportsTable.PRODUCT_AMOUNT, 		 shiftReportModel.getProductAmount(), false));
			reportListModel.add(new ReportModel(ReportsTable.OTHER_AMOUNT,   		 shiftReportModel.getOtherAmount(), false));
			reportListModel.add(new ReportModel(ReportsTable.TIP_Pay_AMOUNT,   		 shiftReportModel.getTipPayAmount(), false));
			reportListModel.add(new ReportModel(ReportsTable.MANUAL_CASH_REFUND,     shiftReportModel.getManualCashRefund(), false));
			reportListAdapter.notifyDataSetChanged();

			new ReportsTable(mContext).deleteInfoFromTable(ReportsTable.SHIFT_REPORT);
			new TipTable(mContext).deleteInfoFromTable();

			break;

		default:
			break;
		}
	}

	@Override
	public void onDataRecieved(JSONArray arry) {}

	@Override
	public void onSocketStateChanged(int state) {}
}


