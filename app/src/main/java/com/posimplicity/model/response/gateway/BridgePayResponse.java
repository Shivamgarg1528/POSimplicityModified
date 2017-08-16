package com.posimplicity.model.response.gateway;

public class BridgePayResponse {

    private Response Response;

    public Response getResponse() {
        return Response;
    }

    public void setResponse(Response Response) {
        this.Response = Response;
    }

    public static class Response {
        private int Result;
        private String AuthCode;
        private String ExtData;
        private String RespMSG;

        public int getResult() {
            return Result;
        }

        public void setResult(int Result) {
            this.Result = Result;
        }

        public String getAuthCode() {
            return AuthCode;
        }

        public void setAuthCode(String AuthCode) {
            this.AuthCode = AuthCode;
        }

        public String getExtData() {
            return ExtData;
        }

        public void setExtData(String ExtData) {
            this.ExtData = ExtData;
        }

        public String getRespMSG() {
            return RespMSG;
        }

        public void setRespMSG(String RespMSG) {
            this.RespMSG = RespMSG;
        }
    }
}
