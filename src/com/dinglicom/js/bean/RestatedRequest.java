package com.dinglicom.js.bean;

public class RestatedRequest {
     /**
      * 接收的xml格式
      */
	String id="";
	String beginFilename="";//批量文件上传开始文件名
	String endFilename="";//批量文件上传结束文件名
	String fileName="";//单一文件上传  文件名
	
	/**
	 * 单一文件上传  文件名
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 单一文件上传  文件名
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBeginFilename() {
		return beginFilename;
	}
	public void setBeginFilename(String beginFilename) {
		this.beginFilename = beginFilename;
	}
	public String getEndFilename() {
		return endFilename;
	}
	public void setEndFilename(String endFilename) {
		this.endFilename = endFilename;
	}
	
	public RestatedRequest() {
		super();
	}
	public RestatedRequest(String id, String fileName) {
		super();
		this.id = id;
		this.fileName = fileName;
	}
	public RestatedRequest(String id, String beginFilename, String endFilename) {
		super();
		this.id = id;
		this.beginFilename = beginFilename;
		this.endFilename = endFilename;
	}
	public RestatedRequest(String id, String beginFilename, String endFilename,
			String fileName) {
		super();
		this.id = id;
		this.beginFilename = beginFilename;
		this.endFilename = endFilename;
		this.fileName = fileName;
	}
	
}
