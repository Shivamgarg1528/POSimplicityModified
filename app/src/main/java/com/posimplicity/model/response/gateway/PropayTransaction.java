package com.posimplicity.model.response.gateway;

public class PropayTransaction {

    private Transaction Transaction;
    private RequestResult RequestResult;

    public Transaction getTransaction() {
        return Transaction;
    }

    public void setTransaction(Transaction Transaction) {
        this.Transaction = Transaction;
    }

    public RequestResult getRequestResult() {
        return RequestResult;
    }

    public void setRequestResult(RequestResult RequestResult) {
        this.RequestResult = RequestResult;
    }

    public static class Transaction {
        private String TransactionResult;

        public String getTransactionResult() {
            return TransactionResult;
        }

        public void setTransactionResult(String TransactionResult) {
            this.TransactionResult = TransactionResult;
        }
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
