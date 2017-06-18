package com.dinglicom.js.bean;

public class RestatedResponse {
    /**
     * 发送的消息体
     */
	int id=0;
	String resultCode="";
	String failReason="";
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	
	
	
}
