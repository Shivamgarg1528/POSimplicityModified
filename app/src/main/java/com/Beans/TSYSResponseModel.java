package com.Beans;

public class TSYSResponseModel {
	
	private String responseMsg;
	private boolean isSuccess;
	private String tsysResponse;
	
	public TSYSResponseModel(String responseMsg, boolean isSuccess,
			String tsysResponse) {
		super();
		this.responseMsg  = responseMsg;
		this.isSuccess    = isSuccess;
		this.tsysResponse = tsysResponse;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getTsysResponse() {
		return tsysResponse;
	}

	public void setTsysResponse(String tsysResponse) {
		this.tsysResponse = tsysResponse;
	}

}
