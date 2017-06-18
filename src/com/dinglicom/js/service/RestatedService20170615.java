package com.dinglicom.js.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletInputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.dinglicom.js.bean.RestatedRequest;
import com.dinglicom.util.DLUtils;
import com.dinglicom.util.FileRead2017061516;
import com.dinglicom.util.SFTPFileUtil;
//import org.jdom2.input.SAXBuilder;

@Service
public class RestatedService20170615 {
	private static Log log = LogFactory.getLog(RestatedService20170615.class);
	public static Properties readFilePath = DLUtils.getProperties("fileQueryConfig.properties");
	public static String pathFile = readFilePath.getProperty("fgfile.path.output");//文件夹路径
	
//	public static Properties readFtp = DLUtils.getProperties("ftp.properties");
	public static List<String> upFileList = new ArrayList<String>();
    
	public String read(final ServletInputStream inputStream) throws DocumentException, IOException, ParseException{
		//1.解析XML
		RestatedRequest restatedRequest = loadXml(inputStream);
		//2.读取.gc文件
		readFileCHK(restatedRequest);
		
		//3.通知
		
		
		//4.上传
		String str = upFileToFtp();
		String state = str.equals(" ") ? "1" : "2";
		String error = str.equals(" ") ? "SUCCESS" : str;
		String resUpMsg = Up(restatedRequest.getId(), state, error);
		return resUpMsg;
	}

	/**
	 * 1.读取并解析上层系统的XML
	 * 
	 * @param inputStream
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	private static RestatedRequest loadXml(ServletInputStream inputStream)
			throws DocumentException, IOException {
//		log.error("解析文件开始");
		return FileRead2017061516.readXml(inputStream);
	}

	/**
	 * 2.读取gz CHK文件
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws ParseException 
	 */
	public static void readFileCHK(RestatedRequest restatedRequest) throws FileNotFoundException, ParseException {
		
		DateFormat df = new SimpleDateFormat("YYYYMMDDHHmmSS");
		Long beginDate = df.parse(restatedRequest.getBeginFilename().split("_")[4]).getTime();//开始时间
		Long endDate = df.parse(restatedRequest.getEndFilename().split("_")[4]).getTime();//结束时间
		
		//1.全部文件上传
		//beginFilename及endFilename的名字相同（如均为LTE_SSS_HW_XXXX_YYYYMMDDHHMISS）并且fileName(单一文件)名为空，则需要重传该上报周期的所有文件
		if (restatedRequest.getBeginFilename().equals(restatedRequest.getEndFilename())) {
			List<String> listFileNameFileter = new FileRead2017061516(pathFile).ListFileNameFileter(".gz",-1L,-1L);
			upFileList.addAll(listFileNameFileter);
			log.debug("ztt print log : RestatedService 102 Lin : beginFilename及endFilename的名字相同,重传该上报周期的所有文件");
		//2.上传部分文件(批量上传)  beginFilename及endFilename的名字不为空   并且不相同     并且  fileName为空
		}else if(StringUtils.isEmpty(restatedRequest.getFileName()) && StringUtils.isNotEmpty(restatedRequest.getBeginFilename()) && StringUtils.isNotEmpty(restatedRequest.getEndFilename()) && !restatedRequest.getBeginFilename().equals(restatedRequest.getEndFilename())){
				List<String> listFileNameFileter = new FileRead2017061516(pathFile).ListFileNameFileter(".gz",beginDate,endDate);
				upFileList.addAll(listFileNameFileter);
				log.debug("ztt print log : RestatedService 85 Lin : 读出.gz文件的集合为："+ listFileNameFileter.size());
		//3.单一文件上传	fileName不为空  并且  beginFilename及endFilename都为空
		}else if(StringUtils.isNotEmpty(restatedRequest.getFileName()) && StringUtils.isEmpty(restatedRequest.getBeginFilename()) && StringUtils.isEmpty(restatedRequest.getEndFilename())){
			List<String> listFileNameFileter = new FileRead2017061516(pathFile).ListFileNameFileter(restatedRequest.getFileName(),-2L,-2L);
			upFileList.addAll(listFileNameFileter);
			log.debug("ztt print log : RestatedService 102 Lin : fileName不为空,开始与结束文件名为空.则只上传一个文件.上传文件名为:"+restatedRequest.getFileName());
		}
		/*if (restatedRequest.getBeginFilename().equals(restatedRequest.getEndFilename())) {
			List<String> listFileNameFileter = new FileRead20170615(".gz").ListFileNameFileter(pathFile,restatedRequest);
			upFileList.addAll(listFileNameFileter);
			log.error("ztt print log : RestatedService 102 Lin : beginFilename及endFilename的名字相同,重传该上报周期的所有文件");
		} else {
			List<String> listFileNameFileter = new FileRead20170615(".gz").ListFileNameFileter(pathFile,restatedRequest);
			upFileList.addAll(listFileNameFileter);
			log.error("ztt print log : RestatedService 85 Lin : 读出.gz文件的集合为："+ listFileNameFileter.size());
		}*/
	}


	/**
	 * 3.上传FTP服务器
	 * 
	 * @return 成功返回SUCCESS 不成功返回错误信息
	 */
	
	public static String upFileToFtp() {
		String stateMsg = " ";
		SFTPFileUtil sftp = new SFTPFileUtil("117.136.188.124",22,60000,"JSGPRS000","JTnj#386");
		
		String loginMsg = sftp.login();
		if(!loginMsg.equals("SUCCESS")) return loginMsg;//如果登陆失败 返回登陆错误信息 
		
		Iterator<String> it = upFileList.iterator();
		
		while(it.hasNext()){
			String entry = it.next();
			String filePath = pathFile+"\\"+entry;/////////////////////////////////可能存在问题     路径需要答应出来看看
			log.debug("ztt print log : RestatedService 167 Lin : upFileToFtp() filePath "+filePath);
			try {
				sftp.uploadFile("/data/IF_UPLOAD/PS_Log_Server/JS/YDJSG00001", entry, new FileInputStream(filePath));
			} catch (Exception e) {
				e.getStackTrace();
				log.error(e.getMessage());
				return e.getMessage();
			}
		}
		sftp.logout();
		return stateMsg;
	}
    
	/**
	 * 4.上报处理结果
	 * 
	 * @param id
	 *            数字，和数据重报请求消息的id相等
	 * @param flag
	 *            结果标识，必填 1：成功 2：失败
	 * @param err
	 *            失败原因，如果失败则必填
	 * @return
	 */
	public static String Up(String id, String flag, String err) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<info id=\"" + id + "\" type=\"query_response\">\n");
		sb.append("<resultCode>" + flag + "</resultCode>");
		sb.append("<failReason>" + err + "</failReason>");
		sb.append("</info>");
		return sb.toString();
	}

}
