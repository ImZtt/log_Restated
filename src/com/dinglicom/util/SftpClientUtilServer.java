package com.dinglicom.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dinglicom.js.service.RestatedService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpClientUtilServer {
	/** 
	* 初始化日志引擎 
	*/ 
	private static Log logger = LogFactory.getLog(RestatedService.class);

	    /** Sftp */ 
	    ChannelSftp sftp = null; 
	    /** 主机 */ 
	    private String host = "117.136.188.124"; 
	    /** 端口 */ 
	    private int port = 22; 
	    /** 用户名 */ 
	    private String username = "JSGPRS000"; 
	    /** 密码 */ 
	    private String password = "JTnj#386"; 
	    
	/** 
	* 构造函数 
	* 
	* @param host 
	*            主机 
	* @param port 
	*            端口 
	* @param username 
	*            用户名 
	* @param password 
	*            密码 
	*            
	*/ 
	    public SftpClientUtilServer(String host, int port, String username, 
	String password){ 
	    
	        this.host = host; 
	        this.port = port; 
	        this.username = username; 
	        this.password = password; 
	    } 

	/** 
	* 连接sftp服务器 
	*            
	* @throws Exception      
	*/ 
	public void connect() throws Exception { 

	JSch jsch = new JSch(); 
	Session sshSession = jsch.getSession(this.username, this.host, this.port); 
	logger.debug(SftpClientUtilServer.class + "Session created."); 

	sshSession.setPassword(password); 
	Properties sshConfig = new Properties(); 
	sshConfig.put("StrictHostKeyChecking", "no"); 
	sshSession.setConfig(sshConfig); 
	sshSession.connect(20000); 
	logger.debug(SftpClientUtilServer.class + " Session connected."); 

	logger.debug(SftpClientUtilServer.class + " Opening Channel."); 
	Channel channel = sshSession.openChannel("sftp"); 
	channel.connect(); 
	this.sftp = (ChannelSftp) channel; 
	logger.debug(SftpClientUtilServer.class + " Connected to " + this.host + "."); 
	} 
	 /** 
     * Disconnect with server 
     *            
     * @throws Exception      
     */ 
    public void disconnect() throws Exception { 
        if(this.sftp != null){ 
            if(this.sftp.isConnected()){ 
                this.sftp.disconnect(); 
            }else if(this.sftp.isClosed()){ 
            logger.debug(SftpClientUtilServer.class + " sftp is closed already"); 
            } 
        } 
    } 
    /** 
    * 上传单个文件 
    * 
    * @param directory 
    *            上传的目录 
    * @param uploadFile 
    *            要上传的文件 
    *            
    * @throws Exception      
    */ 
    public String upload(String directory, String uploadFile) { 
    	StringBuffer sb=new StringBuffer();
    try {
		this.sftp.cd(directory);
		File file = new File(uploadFile); 
	    try {
			this.sftp.put(new FileInputStream(file), file.getName());
			return "SUCCESS";
		} catch (FileNotFoundException e) {
			sb.append(e.getStackTrace());
		} 
	} catch (SftpException e) {
		sb.append(e.getStackTrace());
		e.printStackTrace();
	}
	return sb.toString(); 
    
    } 
    /** 
    * 上传目录下全部文件 
    * 
    * @param directory 
    *            上传的目录 
    *            
    * @throws Exception      
    */ 
//    public void uploadByDirectory(String directory) throws Exception { 
//
//    String uploadFile = ""; 
//    List<String> uploadFileList = this.listFiles(directory); 
//    Iterator<String> it = uploadFileList.iterator(); 
//
//    while(it.hasNext()) 
//    { 
//    uploadFile = it.next().toString(); 
//    this.upload(directory, uploadFile); 
//    } 
//    } 

}
