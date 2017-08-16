package com.posimplicity.model.response.gateway;

public class PropayMethodId {


    private String PaymentMethodId;
    private RequestResult RequestResult;

    public String getPaymentMethodId() {
        return PaymentMethodId;
    }

    public void setPaymentMethodId(String PaymentMethodId) {
        this.PaymentMethodId = PaymentMethodId;
    }

    public RequestResult getRequestResult() {
        return RequestResult;
    }

    public void setRequestResult(RequestResult RequestResult) {
        this.RequestResult = RequestResult;
    }

    public static class RequestResult {
        private String ResultValue;
        private String ResultCode;
        private String ResultMessage;

        public String getResultValue() {
            return ResultValue;
        }

        public void setResultValue(String ResultValue) {
            this.ResultValue = ResultValue;
        }

        public String getResultCode() {
            return ResultCode;
        }

        public void setResultCode(String ResultCode) {
            this.ResultCode = ResultCode;
        }

        public String getResultMessage() {
            return ResultMessage;
        }

        public void setResultMessage(String ResultMessage) {
            this.ResultMessage = ResultMessage;
        }
    }
}
