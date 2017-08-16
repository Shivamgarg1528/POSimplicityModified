package com.posimplicity.model.local;

import java.util.List;

public class Setting {

    private AppSetting appSetting;

    public AppSetting getAppSetting() {
        return appSetting;
    }

    public void setAppSetting(AppSetting appSetting) {
        this.appSetting = appSetting;
    }

    public static class AppSetting {

        private Printer printer;
        private Gateway gateway;
        private Rewards rewards;
        private OtherSetting otherSetting;
        private AppLocation appLocation;
        private Drawer drawer;

        public Printer getPrinter() {
            return printer;
        }

        public void setPrinter(Printer printer) {
            this.printer = printer;
        }

        public Gateway getGateway() {
            return gateway;
        }

        public void setGateway(Gateway gateway) {
            this.gateway = gateway;
        }

        public Rewards getRewards() {
            return rewards;
        }

        public void setRewards(Rewards rewards) {
            this.rewards = rewards;
        }

        public OtherSetting getOtherSetting() {
            return otherSetting;
        }

        public void setOtherSetting(OtherSetting otherSetting) {
            this.otherSetting = otherSetting;
        }

        public AppLocation getAppLocation() {
            return appLocation;
        }

        public void setAppLocation(AppLocation appLocation) {
            this.appLocation = appLocation;
        }

        public Drawer getDrawer() {
            return drawer;
        }

        public void setDrawer(Drawer drawer) {
            this.drawer = drawer;
        }

        public static class Printer {

            private String text1;
            private String text2;
            private String text3;
            private String text4;
            private int space1;
            private int space2;
            private int space3;
            private int space4;
            private int usbPrinting;
            private int bluetoothPrinting;
            private int wifiPrinting;
            private int tip;
            private int duplicateReceipt;
            private int printerSound;
            private int receiptPrompt;
            private int qrCodePrint;
            private int barCodePrint;
            private int customerReceiptUsb;
            private int customerReceiptBT;
            private int kitchenReceiptUsb;
            private int kitchenReceiptBT;

            //
            private String wifiAddress;
            private String bluetoothAddress;

            private List<Detail> detail;
            private List<Detail> customerReceiptArray;
            private List<Detail> kitchenReceiptArray;

            public String getText1() {
                return text1;
            }

            public String getWifiAddress() {
                return wifiAddress;
            }

            public int getQrCodePrint() {
                return qrCodePrint;
            }

            public void setQrCodePrint(int qrCodePrint) {
                this.qrCodePrint = qrCodePrint;
            }

            public int getBarCodePrint() {
                return barCodePrint;
            }

            public void setBarCodePrint(int barCodePrint) {
                this.barCodePrint = barCodePrint;
            }

            public void setWifiAddress(String wifiAddress) {
                this.wifiAddress = wifiAddress;
            }

            public String getBluetoothAddress() {
                return bluetoothAddress;
            }

            public void setBluetoothAddress(String bluetoothAddress) {
                this.bluetoothAddress = bluetoothAddress;
            }

            public void setText1(String text1) {
                this.text1 = text1;
            }

            public String getText2() {
                return text2;
            }

            public void setText2(String text2) {
                this.text2 = text2;
            }

            public String getText3() {
                return text3;
            }

            public void setText3(String text3) {
                this.text3 = text3;
            }

            public String getText4() {
                return text4;
            }

            public void setText4(String text4) {
                this.text4 = text4;
            }

            public int getSpace1() {
                return space1;
            }

            public void setSpace1(int space1) {
                this.space1 = space1;
            }

            public int getSpace2() {
                return space2;
            }

            public void setSpace2(int space2) {
                this.space2 = space2;
            }

            public int getSpace3() {
                return space3;
            }

            public void setSpace3(int space3) {
                this.space3 = space3;
            }

            public int getSpace4() {
                return space4;
            }

            public void setSpace4(int space4) {
                this.space4 = space4;
            }

            public int getUsbPrinting() {
                return usbPrinting;
            }

            public void setUsbPrinting(int usbPrinting) {
                this.usbPrinting = usbPrinting;
            }

            public int getBluetoothPrinting() {
                return bluetoothPrinting;
            }

            public void setBluetoothPrinting(int bluetoothPrinting) {
                this.bluetoothPrinting = bluetoothPrinting;
            }

            public int getWifiPrinting() {
                return wifiPrinting;
            }

            public void setWifiPrinting(int wifiPrinting) {
                this.wifiPrinting = wifiPrinting;
            }

            public int getTip() {
                return tip;
            }

            public void setTip(int tip) {
                this.tip = tip;
            }

            public int getDuplicateReceipt() {
                return duplicateReceipt;
            }

            public void setDuplicateReceipt(int duplicateReceipt) {
                this.duplicateReceipt = duplicateReceipt;
            }

            public int getPrinterSound() {
                return printerSound;
            }

            public void setPrinterSound(int printerSound) {
                this.printerSound = printerSound;
            }

            public int getReceiptPrompt() {
                return receiptPrompt;
            }

            public void setReceiptPrompt(int receiptPrompt) {
                this.receiptPrompt = receiptPrompt;
            }

            public List<Detail> getDetail() {
                return detail;
            }

            public void setDetail(List<Detail> detail) {
                this.detail = detail;
            }

            public List<Detail> getCustomerReceiptArray() {
                return customerReceiptArray;
            }

            public void setCustomerReceiptArray(List<Detail> customerReceipt) {
                this.customerReceiptArray = customerReceipt;
            }

            public List<Detail> getKitchenReceiptArray() {
                return kitchenReceiptArray;
            }

            public void setKitchenReceiptArray(List<Detail> kitchenReceipt) {
                this.kitchenReceiptArray = kitchenReceipt;
            }

            public int getCustomerReceiptUsb() {
                return customerReceiptUsb;
            }

            public void setCustomerReceiptUsb(int customerReceiptUsb) {
                this.customerReceiptUsb = customerReceiptUsb;
            }

            public int getCustomerReceiptBT() {
                return customerReceiptBT;
            }

            public void setCustomerReceiptBT(int customerReceiptBT) {
                this.customerReceiptBT = customerReceiptBT;
            }

            public int getKitchenReceiptUsb() {
                return kitchenReceiptUsb;
            }

            public void setKitchenReceiptUsb(int kitchenReceiptUsb) {
                this.kitchenReceiptUsb = kitchenReceiptUsb;
            }

            public int getKitchenReceiptBT() {
                return kitchenReceiptBT;
            }

            public void setKitchenReceiptBT(int kitchenReceiptBT) {
                this.kitchenReceiptBT = kitchenReceiptBT;
            }
        }

        public static class Gateway {

            private boolean singleChoiceMode;
            private List<Detail> detail;
            private ProPay proPay;
            private BridgePay bridgePay;
            private PlugNPay plugNPay;
            private TsysPay tsysPay;


            public List<Detail> getDetail() {
                return detail;
            }

            public void setDetail(List<Detail> detail) {
                this.detail = detail;
            }

            public boolean isSingleChoiceMode() {
                return singleChoiceMode;
            }

            public void setSingleChoiceMode(boolean singleChoiceMode) {
                this.singleChoiceMode = singleChoiceMode;
            }

            public ProPay getProPay() {
                return proPay;
            }

            public void setProPay(ProPay proPay) {
                this.proPay = proPay;
            }

            public BridgePay getBridgePay() {
                return bridgePay;
            }

            public void setBridgePay(BridgePay bridgePay) {
                this.bridgePay = bridgePay;
            }

            public PlugNPay getPlugNPay() {
                return plugNPay;
            }

            public void setPlugNPay(PlugNPay plugNPay) {
                this.plugNPay = plugNPay;
            }

            public TsysPay getTsysPay() {
                return tsysPay;
            }

            public void setTsysPay(TsysPay tsysPay) {
                this.tsysPay = tsysPay;
            }

            public static class ProPay {
                private String payerId;

                public String getPayerId() {
                    return payerId;
                }

                public void setPayerId(String payerId) {
                    this.payerId = payerId;
                }
            }

            public static class BridgePay {
                private String userName;
                private String userPassword;

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }

                public String getUserPassword() {
                    return userPassword;
                }

                public void setUserPassword(String userPassword) {
                    this.userPassword = userPassword;
                }
            }

            public static class PlugNPay {
                private String plugNPayId;

                public String getPlugNPayId() {
                    return plugNPayId;
                }

                public void setPlugNPayId(String plugNPayId) {
                    this.plugNPayId = plugNPayId;
                }
            }

            public static class TsysPay {
                private String userName;
                private String password;
                private String deviceId;
                private String merchantId;
                private String key;

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }

                public String getPassword() {
                    return password;
                }

                public void setPassword(String password) {
                    this.password = password;
                }

                public String getDeviceId() {
                    return deviceId;
                }

                public void setDeviceId(String deviceId) {
                    this.deviceId = deviceId;
                }

                public String getMerchantId() {
                    return merchantId;
                }

                public void setMerchantId(String merchantId) {
                    this.merchantId = merchantId;
                }

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }
        }

        public static class Rewards {
            private int tenderCard;
            private int posCard;
            private boolean singleChoiceMode;
            private List<Detail> detail;

            public int getTenderCard() {
                return tenderCard;
            }

            public void setTenderCard(int tenderCard) {
                this.tenderCard = tenderCard;
            }

            public int getPosCard() {
                return posCard;
            }

            public void setPosCard(int posCard) {
                this.posCard = posCard;
            }

            public boolean isSingleChoiceMode() {
                return singleChoiceMode;
            }

            public void setSingleChoiceMode(boolean singleChoiceMode) {
                this.singleChoiceMode = singleChoiceMode;
            }

            public List<Detail> getDetail() {
                return detail;
            }

            public void setDetail(List<Detail> detail) {
                this.detail = detail;
            }
        }

        public static class OtherSetting {
            private int collapseItems;
            private int consumerFacingTablet;
            private int encryptionForCC;
            private int resetCurrentStore;
            private int loginLogoutUser;
            private int assignTransactionToClerk;
            private int timeClock;
            private int customOptionPrint;
            private int syncApplication;
            private boolean singleChoiceMode;
            private List<Detail> detail;

            public int getCollapseItems() {
                return collapseItems;
            }

            public void setCollapseItems(int collapseItems) {
                this.collapseItems = collapseItems;
            }

            public int getConsumerFacingTablet() {
                return consumerFacingTablet;
            }

            public void setConsumerFacingTablet(int consumerFacingTablet) {
                this.consumerFacingTablet = consumerFacingTablet;
            }

            public int getEncryptionForCC() {
                return encryptionForCC;
            }

            public void setEncryptionForCC(int encryptionForCC) {
                this.encryptionForCC = encryptionForCC;
            }

            public int getResetCurrentStore() {
                return resetCurrentStore;
            }

            public void setResetCurrentStore(int resetCurrentStore) {
                this.resetCurrentStore = resetCurrentStore;
            }

            public int getLoginLogoutUser() {
                return loginLogoutUser;
            }

            public void setLoginLogoutUser(int loginLogoutUser) {
                this.loginLogoutUser = loginLogoutUser;
            }

            public int getAssignTransactionToClerk() {
                return assignTransactionToClerk;
            }

            public void setAssignTransactionToClerk(int assignTransactionToClerk) {
                this.assignTransactionToClerk = assignTransactionToClerk;
            }

            public int getTimeClock() {
                return timeClock;
            }

            public void setTimeClock(int timeClock) {
                this.timeClock = timeClock;
            }

            public int getCustomOptionPrint() {
                return customOptionPrint;
            }

            public void setCustomOptionPrint(int customOptionPrint) {
                this.customOptionPrint = customOptionPrint;
            }

            public int getSyncApplication() {
                return syncApplication;
            }

            public void setSyncApplication(int syncApplication) {
                this.syncApplication = syncApplication;
            }

            public boolean isSingleChoiceMode() {
                return singleChoiceMode;
            }

            public void setSingleChoiceMode(boolean singleChoiceMode) {
                this.singleChoiceMode = singleChoiceMode;
            }

            public List<Detail> getDetail() {
                return detail;
            }

            public void setDetail(List<Detail> detail) {
                this.detail = detail;
            }
        }

        public static class AppLocation {
            private int retail;
            private int restaurant;
            private int bar;
            private int quick;
            private boolean singleChoiceMode;
            private List<Detail> detail;

            public int getRetail() {
                return retail;
            }

            public void setRetail(int retail) {
                this.retail = retail;
            }

            public int getRestaurant() {
                return restaurant;
            }

            public void setRestaurant(int restaurant) {
                this.restaurant = restaurant;
            }

            public int getBar() {
                return bar;
            }

            public void setBar(int bar) {
                this.bar = bar;
            }

            public int getQuick() {
                return quick;
            }

            public void setQuick(int quick) {
                this.quick = quick;
            }

            public boolean isSingleChoiceMode() {
                return singleChoiceMode;
            }

            public void setSingleChoiceMode(boolean singleChoiceMode) {
                this.singleChoiceMode = singleChoiceMode;
            }

            public List<Detail> getDetail() {
                return detail;
            }

            public void setDetail(List<Detail> detail) {
                this.detail = detail;
            }
        }

        public static class Drawer {
            private int credit;
            private int cash;
            private int check;
            private int reward;
            private int custom1;
            private int custom2;
            private List<Detail> detail;

            public List<Detail> getDetail() {
                return detail;
            }

            public void setDetail(List<Detail> detail) {
                this.detail = detail;
            }

            public int getCredit() {
                return credit;
            }

            public void setCredit(int credit) {
                this.credit = credit;
            }

            public int getCash() {
                return cash;
            }

            public void setCash(int cash) {
                this.cash = cash;
            }

            public int getCheck() {
                return check;
            }

            public void setCheck(int check) {
                this.check = check;
            }

            public int getReward() {
                return reward;
            }

            public void setReward(int reward) {
                this.reward = reward;
            }

            public int getCustom1() {
                return custom1;
            }

            public void setCustom1(int custom1) {
                this.custom1 = custom1;
            }

            public int getCustom2() {
                return custom2;
            }

            public void setCustom2(int custom2) {
                this.custom2 = custom2;
            }
        }
    }
}
