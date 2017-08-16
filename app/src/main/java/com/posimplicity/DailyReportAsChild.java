package com.posimplicity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.Beans.ReportModel;
import com.Beans.ReportsModel;
import com.Fragments.MaintFragmentTender1;
import com.RecieptPrints.PrintReports;
import com.RecieptPrints.PrintSettings;
import com.SetupPrinter.BasePR;
import com.SetupPrinter.PrinterCallBack;
import com.SetupPrinter.UsbPR;
import com.adapter.ReportAdapter;
import com.posimplicity.database.local.ReportsTable;
import com.posimplicity.database.local.TipTable;
import com.dialog.DetailsOfEachPayouts;
import com.utils.CurrentDate;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class DailyReportAsChild extends BaseActivity implements OnItemClickListener, OnClickListener{

	private ReportsModel dailyReportModel;	
	private ListView daliyReportListView;
	private ReportAdapter reportListAdapter;
	private List<ReportModel> reportListModel;
	private Button printBtn,chartAct;
	private String transTime ;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState,false,this);
		setContentView(R.layout.activity_daily_report_as_child);

		onInitViews();
		onListenerRegister();	

		transTime        = CurrentDate.returnCurrentDate();

		dailyReportModel = new ReportsTable(mContext).getReportModel(transTime,ReportsTable.DAILY_REPORT);
		String tipAmt    = new TipTable(mContext).getSumOfTips(transTime);
		dailyReportModel.setTipAmount(tipAmt);
		dailyReportModel.setNoInternetOrders (new ReportsTable(mContext).getSumOfTotalAmountBasedOnDynamicValues(transTime, ReportsTable.DAILY_REPORT, ReportsTable.FAILED));
		dailyReportModel.setManuallyRecOrders(new ReportsTable(mContext).getSumOfTotalAmountBasedOnDynamicValues(transTime, ReportsTable.DAILY_REPORT, ReportsTable.MANUALLY_ENTRY));


		reportListModel.add(new ReportModel(ReportsTable.TOTAL_AMOUNT,			 dailyReportModel.getTotalAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.CASH_AMOUNT, 			 dailyReportModel.getCashAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.CREDIT_AMOUNT,     	 dailyReportModel.getCreditAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.CHECK_AMOUNT, 			 dailyReportModel.getCheckAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.GIFT_AMOUNT,   		 dailyReportModel.getGiftAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.REWARDS_AMOUNT,         dailyReportModel.getRewardAmount(), false));
		reportListModel.add(new ReportModel(MaintFragmentTender1.getCustom1Name(mContext), 			 dailyReportModel.getCustom1Amount(), false));
		reportListModel.add(new ReportModel(MaintFragmentTender1.getCustom2Name(mContext), 			 dailyReportModel.getCustom2Amount(), false));
		reportListModel.add(new ReportModel(ReportsTable.TAX_AMOUNT, 			 dailyReportModel.getTaxAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.TIP_AMOUNT,             dailyReportModel.getTipAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.LOTTERY_AMOUNT, 		 dailyReportModel.getLotteryAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.EXPENSES_AMOUNT, 		 dailyReportModel.getExpensesAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.SUPPLIES_AMOUNT,		 dailyReportModel.getSuppliesAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.PRODUCT_AMOUNT, 		 dailyReportModel.getProductAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.OTHER_AMOUNT,   		 dailyReportModel.getOtherAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.TIP_Pay_AMOUNT,   		 dailyReportModel.getTipPayAmount(), false));
		reportListModel.add(new ReportModel(ReportsTable.MANUAL_CASH_REFUND,     dailyReportModel.getManualCashRefund(), false));
		reportListModel.add(new ReportModel(ReportsTable.NO_INTERNET_ORDERS,     dailyReportModel.getNoInternetOrders(), false));
		reportListModel.add(new ReportModel(ReportsTable.MANUALLY_RECORDED_ORDERS, dailyReportModel.getManuallyRecOrders(), false));


		daliyReportListView.setOnItemClickListener(this);
		reportListAdapter.notifyDataSetChanged();
	}	

	@Override
	public void onInitViews() {

		daliyReportListView    = findViewByIdAndCast(R.id.Activity_DailyReport_ListView_Items_);
		printBtn               = findViewByIdAndCast(R.id.Activity_DailyReport_Button_Print_);
		chartAct               = findViewByIdAndCast(R.id.Activity_DailyReport_Button_New_ChartAct);

		reportListModel        = new ArrayList<>();
		reportListAdapter      = new ReportAdapter(mContext, reportListModel);

		daliyReportListView.setAdapter(reportListAdapter);
		dailyReportModel      = new ReportsModel();
	}

	@Override
	public void onListenerRegister() {

		printBtn.setOnClickListener(this);
		chartAct.setOnClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

		if(position > 7 && position < 15){

			ReportModel rpModel           = reportListModel.get(position);
			String payoutName                 = rpModel.getName();
			List<String> listOFEachPayOut     = new ReportsTable(mContext).getListOfSomeInfoBasedOnDynamicValue(payoutName,ReportsTable.DAILY_REPORT,transTime,payoutName);
			List<String> listOFEachPayOutDesc = new ReportsTable(mContext).getListOfSomeInfoBasedOnDynamicValue(ReportsTable.DESCRIPTION,ReportsTable.DAILY_REPORT,transTime,payoutName);

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

		case R.id.Activity_DailyReport_Button_Print_:
			
			if(PrintSettings.isAbleToPrintCustomerReceiptThroughUsb(mContext)){
				new UsbPR(mContext, new PrinterCallBack() {
					
					@Override
					public void onDisconnected() {}
					
					@Override
					public void onConnected(BasePR pPrinter) {
						new PrintReports().onPrintReport(DailyReportAsChild.this, pPrinter,dailyReportModel);
					}
					
				}).onConnectionStart();
			}
			
			if(PrintSettings.isAbleToPrintCustomerReceiptThroughBluetooth(mContext)){				
				new PrintReports().onPrintReport(DailyReportAsChild.this,globalApp.getPrinterBT(),dailyReportModel);
			}
			
			break;

		case R.id.Activity_DailyReport_Button_New_ChartAct:		

			Intent intent = new Intent(mContext, ChartViewActivity.class);
			startActivity(intent);

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
