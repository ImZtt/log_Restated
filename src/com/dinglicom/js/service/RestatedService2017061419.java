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
//import org.jdom2.input.SAXBuilder;
import com.dinglicom.util.FileRead20170614;
import com.dinglicom.util.SFTPFileUtil;

@Service
public class RestatedService2017061419{
	private static Log log = LogFactory.getLog(RestatedService2017061419.class);
	public static Properties readFilePath = DLUtils.getProperties("fileQueryConfig.properties");
	public static String pathFile = readFilePath.getProperty("fgfile.path.output");//文件夹路径
	
//	public static Properties readFtp = DLUtils.getProperties("ftp.properties");
	public static List<String> upFileList = new ArrayList<String>();
    
	public String readThreadMsg = "";
	
	@SuppressWarnings("deprecation")
	public synchronized String ResquestService(final ServletInputStream inputStream) throws DocumentException, IOException, ParseException, InterruptedException {
		
		//开启一个线程读取文件
		final Thread t = new Thread(new Runnable(){  
            public void run(){  
				try {
					readThreadMsg = read(inputStream);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
            }});  
         t.start();
         
         long b = System.currentTimeMillis();
         Thread.sleep(10000);//休眠10秒
		 long c = System.currentTimeMillis();
         System.out.println("-------->>");
         System.out.println("-------->>"+(c-b));
         
         //如果10s之后readThreadMsg为空  则说明未运行完成    则结束该线程
         if(StringUtils.isEmpty(readThreadMsg))	 t.stop();
         
		return readThreadMsg;
	}
	
	public synchronized  String read(final ServletInputStream inputStream) throws DocumentException, IOException, ParseException{
		RestatedRequest restatedRequest = loadXml(inputStream);
		List<String> list = readFileCHK(restatedRequest);
		comparisonFile(list, restatedRequest);
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
		return FileRead20170614.readXml(inputStream);
	}

	/**
	 * 2.读取gz CHK文件
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public static List<String> readFileCHK(RestatedRequest restatedRequest) throws FileNotFoundException {
		
		
		List<String> listFileNameFileter = new FileRead20170614(pathFile).ListFileNameFileter(".gz",restatedRequest);
		
		log.error("ztt print log : RestatedService 85 Lin : 读出.gz文件的集合为："+ listFileNameFileter.size());
		return listFileNameFileter;
	}

	/**
	 * 3.解析csv txt文件内容 并依据上层系统的XML文件进行业务处理
	 * 
	 * @param map
	 *            所有的csv txt文件类型 key:文件名 value:文件路径
	 * @param restatedRequest
	 *            数据重报请求消息实体bean
	 * @return 
	 * @throws ParseException
	 */
	public  void comparisonFile(List<String> list,RestatedRequest restatedRequest) throws ParseException {
		// beginFilename及endFilename的名字相同（如均为LTE_SSS_HW_XXXX_YYYYMMDDHHMISS），则需要重传该上报周期的所有文件
		if (restatedRequest.getBeginFilename().equals(restatedRequest.getEndFilename())) {
			upFileList.addAll(list);
			log.error("ztt print log : RestatedService 102 Lin : beginFilename及endFilename的名字相同,重传该上报周期的所有文件");
		} else {
			String[] begin = restatedRequest.getBeginFilename().split("_");
			String[] end = restatedRequest.getEndFilename().split("_");
			DateFormat df = new SimpleDateFormat("YYYYMMDDHHmmSS");
			
			for (String l : list) {
//				Pattern pattern = Pattern.compile("LTE_JS.*.(gz)");
//				Matcher matcher = pattern.matcher(key);
//				if(!matcher.find()) continue;//如果匹配不上跳出循环 进行下次循环
//				String filename=matcher.group();
				String[] k = l.split("_");
				java.util.Date d1 = df.parse(k[4]);
				java.util.Date d2 = df.parse(begin[4]);
				java.util.Date d3 = df.parse(end[4]);
				/**
				 * 开始文件和结束文件的hw设备号一致
				 */
				if (d1.getTime() >= d2.getTime() && d1.getTime() <= d3.getTime()) {
					upFileList.add(l);
				}
				/**
				 * 开始文件和结束文件的hw设备号不一致时
				 * 1.开始的文件中的hw和map中的key
				 * 2.结束中的文件的hw和map中的key比较
				 */
				 else if ((k[2].equals(begin[2])) || (k[2].equals(end[2]))) {
					 if (d1.getTime() >= d2.getTime() || d1.getTime() <= d3.getTime()) {
						 upFileList.add(l);
					 }
				 }
			}
		}
	}

	/**
	 * 4.上传FTP服务器
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
	 * 5.上报处理结果
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
