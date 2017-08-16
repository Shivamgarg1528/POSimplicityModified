package com.RecieptPrints;

import android.content.Context;

import com.Beans.CustomerModel;
import com.PosInterfaces.PrefrenceKeyConst;
import com.SetupPrinter.BasePR;
import com.utils.Helper;
import com.utils.MyPreferences;
import com.utils.MyStringFormat;

public class PrintExtraReceipt {

    private final static String DEFAULT_ADDRESS = "Enter Address ";

    public static void onPrintTipReceipt(CustomerModel clerkCustomer, String tsysTransactionId, BasePR basePR) {

        basePR.playBuzzer();
        basePR.largeText();
        basePR.printData(PrintSettings.onFormatHeaderAndFooter("Tip Receipt") + "\n");
        basePR.smallText();

        basePR.printData(PrintSettings.onReformatAnyText("TSYS ID", 30) + PrintSettings.onReformatAnyText(":" + tsysTransactionId, 18));
        basePR.printData(PrintSettings.onReformatAnyText("Clerk Name:", 30) + PrintSettings.onReformatAnyText(":" + clerkCustomer.getFirstName(), 18));
        basePR.printData(PrintSettings.onReformatAnyText("Clerk ID", 30) + PrintSettings.onReformatAnyText(":" + clerkCustomer.getCustomerId(), 18));
        basePR.printData(PrintSettings.onReformatAnyText("Tip Amount", 30) + PrintSettings.onReformatAnyText(":" + MyStringFormat.onStringFormat(clerkCustomer.getTipAmount()), 18));

        basePR.printData("\n" + PrintSettings.onFormatHeaderAndFooter("-----------------------------"));
        basePR.cutterCmd();

    }


    public static void printSample(BasePR pBasePR) {
        pBasePR.playBuzzer();
        pBasePR.largeText();
        pBasePR.printData(Helper.createSpaceString(pBasePR.getPrinterSetting().getSpace1()) + pBasePR.getPrinterSetting().getText1());
        pBasePR.printData(Helper.createSpaceString(pBasePR.getPrinterSetting().getSpace2()) + pBasePR.getPrinterSetting().getText2());
        pBasePR.printData(Helper.createSpaceString(pBasePR.getPrinterSetting().getSpace3()) + pBasePR.getPrinterSetting().getText3());
        pBasePR.printData(Helper.createSpaceString(pBasePR.getPrinterSetting().getSpace4()) + pBasePR.getPrinterSetting().getText4());
        pBasePR.cutterCmd();
    }

    public static void onPrintPayoutReceipt(Context mContext, final String payoutAmt, final String payoutName, final String payoutDesc, BasePR basePR) {
        basePR.playBuzzer();
        basePR.largeText();
        basePR.printData(PrintSettings.onFormatHeaderAndFooter("---PAYOUT---\n"));
        basePR.smallText();
        basePR.printData(PrintSettings.onFormatHeaderAndFooter(payoutName));
        basePR.printData(PrintSettings.onFormatHeaderAndFooter(payoutAmt));
        basePR.printData(PrintSettings.onFormatHeaderAndFooter(payoutDesc));
        basePR.cutterCmd();
    }

    public static void onOpenCashDrawer(BasePR basePR) {
        basePR.openDrawer();
    }

    public static void onFailedToSave(Context mContext, BasePR basePR) {

        String transId = MyPreferences.getMyPreference(PrefrenceKeyConst.MOST_RECENTLY_TRANSACTION_ID, mContext);
        basePR.playBuzzer();
        basePR.largeText();
        basePR.printData(PrintSettings.onFormatHeaderAndFooter("Please Save Last Order In"));
        basePR.printData(PrintSettings.onFormatHeaderAndFooter("Magento Manually") + "\n");
        basePR.printData("TransID == " + transId);
        basePR.cutterCmd();
    }
}
