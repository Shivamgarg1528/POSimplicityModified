package com.Beans;

/**
 * Created by shiva on 12/23/2016.
 */

public class DejavooResponse {

    /**
     * response : {"PaymentType":"Credit","RespMSG":"APPROVED 095113","ExtData":"InvNum=1-20150130-082943-21,CardType=VISA,BatchNum=29003,Tip=0.00,CashBack=0.00,Fee=0.00,AcntLast4=0480,Name=MERCURY TEST CARD,SVC=0.00,TotalAmt=55.00,DISC=0.00,Donation=0.00,SHFee=0.00,RwdPoints=0,RwdBalance=0,RwdIssued=,EBTFSLedgerBalance=,EBTFSAvailBalance=,EBTFSBeginBalance=,EBTCashLedgerBalance=,EBTCashAvailBalance=,EBTCashBeginBalance=","PNRef":"00000029","AuthCode":"095113","Message":"Approved","RefId":"1-20150130-082943-21","Sign":"","ResultCode":0,"InvNum":"1-20150130-082943-21","RegisterId":"9645fdbfb0b98355","EMVData":""}
     */

    private XmpBean xmp;

    public XmpBean getXmp() {
        return xmp;
    }

    public void setXmp(XmpBean xmp) {
        this.xmp = xmp;
    }

    public static class XmpBean {
        /**
         * PaymentType : Credit
         * RespMSG : APPROVED 095113
         * ExtData : InvNum=1-20150130-082943-21,CardType=VISA,BatchNum=29003,Tip=0.00,CashBack=0.00,Fee=0.00,AcntLast4=0480,Name=MERCURY TEST CARD,SVC=0.00,TotalAmt=55.00,DISC=0.00,Donation=0.00,SHFee=0.00,RwdPoints=0,RwdBalance=0,RwdIssued=,EBTFSLedgerBalance=,EBTFSAvailBalance=,EBTFSBeginBalance=,EBTCashLedgerBalance=,EBTCashAvailBalance=,EBTCashBeginBalance=
         * PNRef : 00000029
         * AuthCode : 095113
         * Message : Approved
         * RefId : 1-20150130-082943-21
         * Sign :
         * ResultCode : 0
         * InvNum : 1-20150130-082943-21
         * RegisterId : 9645fdbfb0b98355
         * EMVData :
         */

        private ResponseBean response;

        public ResponseBean getResponse() {
            return response;
        }

        public void setResponse(ResponseBean response) {
            this.response = response;
        }

        public static class ResponseBean {
            private String PaymentType;
            private String RespMSG;
            private String ExtData;
            private String PNRef;
            private String AuthCode;
            private String Message;
            private String RefId;
            private String Sign;
            private int ResultCode;
            private String InvNum;
            private String RegisterId;
            private String EMVData;

            public String getPaymentType() {
                return PaymentType;
            }

            public void setPaymentType(String PaymentType) {
                this.PaymentType = PaymentType;
            }

            public String getRespMSG() {
                return RespMSG;
            }

            public void setRespMSG(String RespMSG) {
                this.RespMSG = RespMSG;
            }

            public String getExtData() {
                return ExtData;
            }

            public void setExtData(String ExtData) {
                this.ExtData = ExtData;
            }

            public String getPNRef() {
                return PNRef;
            }

            public void setPNRef(String PNRef) {
                this.PNRef = PNRef;
            }

            public String getAuthCode() {
                return AuthCode;
            }

            public void setAuthCode(String AuthCode) {
                this.AuthCode = AuthCode;
            }

            public String getMessage() {
                return Message;
            }

            public void setMessage(String Message) {
                this.Message = Message;
            }

            public String getRefId() {
                return RefId;
            }

            public void setRefId(String RefId) {
                this.RefId = RefId;
            }

            public String getSign() {
                return Sign;
            }

            public void setSign(String Sign) {
                this.Sign = Sign;
            }

            public int getResultCode() {
                return ResultCode;
            }

            public void setResultCode(int ResultCode) {
                this.ResultCode = ResultCode;
            }

            public String getInvNum() {
                return InvNum;
            }

            public void setInvNum(String InvNum) {
                this.InvNum = InvNum;
            }

            public String getRegisterId() {
                return RegisterId;
            }

            public void setRegisterId(String RegisterId) {
                this.RegisterId = RegisterId;
            }

            public String getEMVData() {
                return EMVData;
            }

            public void setEMVData(String EMVData) {
                this.EMVData = EMVData;
            }
        }
    }
}
