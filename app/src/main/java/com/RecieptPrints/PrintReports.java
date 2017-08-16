package com.RecieptPrints;

import java.util.List;

import android.content.Context;

import com.Beans.ReportModel;
import com.Beans.ReportsModel;
import com.Fragments.MaintFragmentTender1;
import com.SetupPrinter.BasePR;
import com.utils.CurrentDate;

public class PrintReports {

	private final String TOTAL             = "Total:";
	private final String TAX               = "Tax:";
	private final String CASH              = "Cash Total:";
	private final String CREDIT            = "Credit Card:";
	private final String CHECK             = "Check:";		
	private final String GIFT              = "Gift Card:";
	private final String REWARDS           = "Rewards Amount:";
	private final String LOTTERY           = "Lottery Amount:";
	private final String EXPENSES          = "Expenses Amount:";
	private final String SUPPLIES          = "Supplies Amount:";
	private final String PRODUCT           = "ProductList Amount:";
	private final String OTHERPAY          = "OtherPay Amount:";
	private final String TIPPAY            = "TipPay Amount:";
	private final String MANUAL_REF        = "ManualCashRefund Amount:";
	private final String NOT_RECORED       = "Not Recoreded Amount:";
	private final String MANUALR_REC       = "ManualRecorded Amount:";

	private String callFormatMethod(String headerText,String qty,String price) {
		return PrintSettings.onReformatName(headerText) + PrintSettings.onReformatQty(qty) + PrintSettings.onReformatPrice(price);
	}

	public void onPrintAllTip(Context mContext,BasePR basePr, List<ReportModel> reportListModel){

		basePr.playBuzzer();
		basePr.largeText();

		String formattedString   = CurrentDate.returnCurrentDateWithTime();
		basePr.printData("   "+formattedString+"\n\n");

		basePr.printData(PrintSettings.onFormatHeaderAndFooter("Tip Report")+"\n");
		basePr.smallText();

		for(int index = 0 ;index < reportListModel.size(); index++){
			formattedString = callFormatMethod(reportListModel.get(index).getName(),"",reportListModel.get(index).getValue());
			basePr.printData(formattedString);
		}
		
		basePr.printData("\n\n"+PrintSettings.onFormatHeaderAndFooter("-----------------------------------")+"\n");
		basePr.cutterCmd();
	}

	public void onPrintReport(Context mContext,BasePR basePr, ReportsModel shiftReportModel) {

		basePr.playBuzzer();
		basePr.largeText();

		String formattedString = CurrentDate.returnCurrentDateWithTime();

		basePr.printData("   "+formattedString+"\n\n");
		basePr.printData(PrintSettings.onFormatHeaderAndFooter(shiftReportModel.getReportName())+"\n");

		basePr.smallText();
		formattedString = callFormatMethod(TOTAL, "", shiftReportModel.getTotalAmount());
		basePr.printData(formattedString);
		
		formattedString = callFormatMethod(CASH, "", shiftReportModel.getCashAmount());
		basePr.printData(formattedString);

		formattedString = callFormatMethod(CREDIT, "", shiftReportModel.getCreditAmount());
		basePr.printData(formattedString);

		formattedString = callFormatMethod(CHECK, "", shiftReportModel.getCheckAmount());
		basePr.printData(formattedString);
		
		formattedString = callFormatMethod(MaintFragmentTender1.getCustom1Name(mContext)+" :", "", shiftReportModel.getCustom1Amount());
		basePr.printData(formattedString);
		
		formattedString = callFormatMethod(MaintFragmentTender1.getCustom2Name(mContext)+" :", "", shiftReportModel.getCustom2Amount());
		basePr.printData(formattedString);

		formattedString = callFormatMethod(GIFT, "", shiftReportModel.getGiftAmount());
		basePr.printData(formattedString);

		formattedString = callFormatMethod(REWARDS, "", shiftReportModel.getRewardAmount());
		basePr.printData(formattedString);
		
		formattedString = callFormatMethod(TAX, "", shiftReportModel.getTaxAmount());
		basePr.printData(formattedString);

		formattedString = callFormatMethod("Tip Amount:", "", shiftReportModel.getTipAmount());
		basePr.printData(formattedString);

		formattedString = callFormatMethod(LOTTERY, "", shiftReportModel.getLotteryAmount());
		basePr.printData(formattedString);

		formattedString = callFormatMethod(EXPENSES, "", shiftReportModel.getExpensesAmount());
		basePr.printData(formattedString);

		formattedString = callFormatMethod(SUPPLIES, "", shiftReportModel.getSuppliesAmount());
		basePr.printData(formattedString);

		formattedString = callFormatMethod(PRODUCT, "", shiftReportModel.getProductAmount());
		basePr.printData(formattedString);

		formattedString = callFormatMethod(OTHERPAY, "", shiftReportModel.getOtherAmount());
		basePr.printData(formattedString);

		formattedString = callFormatMethod(TIPPAY, "", shiftReportModel.getTipPayAmount());
		basePr.printData(formattedString);

		formattedString = callFormatMethod(MANUAL_REF, "", shiftReportModel.getManualCashRefund());
		basePr.printData(formattedString);

		formattedString = callFormatMethod(NOT_RECORED, "", shiftReportModel.getNoInternetOrders());
		basePr.printData(formattedString);

		formattedString = callFormatMethod(MANUALR_REC, "", shiftReportModel.getManuallyRecOrders());
		basePr.printData(formattedString);

		basePr.printData("\n\n"+PrintSettings.onFormatHeaderAndFooter("-----------------------------------")+"\n");
		basePr.cutterCmd();


	}

}
