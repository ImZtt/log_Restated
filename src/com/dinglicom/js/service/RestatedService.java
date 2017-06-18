package com.dinglicom.js.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.dinglicom.js.bean.RestatedRequest;
import com.dinglicom.util.DLUtils;
//import org.jdom2.input.SAXBuilder;
import com.dinglicom.util.FileRead;
import com.dinglicom.util.SFTPFileUtil;

@Service
public class RestatedService {
	private static Log log = LogFactory.getLog(RestatedService.class);
	public static Properties readFilePath = DLUtils.getProperties("fileQueryConfig.properties");
	
//	public static Properties readFtp = DLUtils.getProperties("ftp.properties");
	public static Map<String, String> upFileMap = new HashMap<String, String>();
    
	public String ResquestService(ServletInputStream inputStream)
//	public String ResquestService(String message)
			throws DocumentException, IOException, ParseException {
		
//			JSONObject json = JSONObject.fromObject(message);
//			RestatedRequest restatedRequest = new RestatedRequest();
//			restatedRequest.setId(json.getString("id"));
//			restatedRequest.setBeginFilename(json.getString("beginFilename"));
//			restatedRequest.setEndFilename(json.getString("endFilename"));
//			log.debug(restatedRequest.getBeginFilename());
//			log.debug(restatedRequest.getEndFilename());
//			log.debug(restatedRequest.getId());
	
		Map<String, String> map = readFileCHK();
		RestatedRequest restatedRequest = loadXml(inputStream);
		comparisonFile(map, restatedRequest);
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
		return FileRead.readXml(inputStream);
	}

	/**
	 * 2.读取gz CHK文件
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Map<String, String> readFileCHK()
			throws FileNotFoundException {
		String filterFileType = ".gz";
		String pathFile = readFilePath.getProperty("fgfile.path.output");
		new FileRead(filterFileType).accept(pathFile);
//		String pathFile = readFilePath.getProperty("fgfile.path.output.windows");
//		String[] filterFileType = { ".gz", ".CHK" };
//		log.error("类型" + filterFileType[0] + filterFileType[1]);
//		log.error("类型" + filterFileType[0]);
		log.error("类型" + filterFileType);
		Map<String, String> map = FileRead.readFileOrDirectory(pathFile,filterFileType);
		// log.error("路径" + pathFile);
		// log.error("配置文件" + readFilePath);
//		log.error("=========" + map.keySet());

		// int count = 0;
		// try {
		// String line = System.getProperty("line.separator");
		// StringBuffer str = new StringBuffer();
		// FileWriter fw = new FileWriter("D:\\1.txt", true);
		// Set set = map.entrySet();
		// Iterator iter = set.iterator();
		// while (iter.hasNext()) {
		// count += 1;
		// Map.Entry entry = (Map.Entry) iter.next();
		// str.append(entry.getKey() + " : " + entry.getValue()).append(
		// line).append(count);
		// }
		// fw.write(str.toString());
		// fw.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		log.error("读文件后文件的集合"+ map.size());
		return map;
	}

	/**
	 * 3.解析csv txt文件内容 并依据上层系统的XML文件进行业务处理
	 * 
	 * @param map
	 *            所有的csv txt文件类型 key:文件名 value:文件路径
	 * @param restatedRequest
	 *            数据重报请求消息实体bean
	 * @throws ParseException
	 */
	public void comparisonFile(Map<String, String> map,
			RestatedRequest restatedRequest) throws ParseException {
		// beginFilename及endFilename的名字相同（如均为LTE_SSS_HW_XXXX_YYYYMMDDHHMISS），则需要重传该上报周期的所有文件
		
		if (restatedRequest.getBeginFilename().equals(
				restatedRequest.getEndFilename())) {
			upFileMap.putAll(map);
		} else {
			String[] begin = restatedRequest.getBeginFilename().split("_");
			String[] end = restatedRequest.getEndFilename().split("_");
			DateFormat df = new SimpleDateFormat("YYYYMMDDHHmmSS");

			for (String key : map.keySet()) {
				//LTE_JS_HA_1234_20170331114511_0004 .csv.gz	LTE_JS_HW_XXXX_20170331113705_0001.csv.gz.CHK	
				//这里只查出.gz结尾的文件
//				Pattern pattern = Pattern.compile("LTE_JS.*.(gz|CHK)");
				Pattern pattern = Pattern.compile("LTE_JS.*.(gz)");
//				Pattern pattern = Pattern.compile("LTE_JS_[0-9a-zA-Z]*_[0-9a-zA-Z]*_[0-9]{14}_[0-9a-zA-Z]*.(gz|CHK)");
				Matcher matcher = pattern.matcher(key);
				if(!matcher.find()) continue;//如果匹配不上跳出循环 进行下次循环
				String filename=matcher.group();
				String[] k = filename.split("_");
//				String[] k=key.split("_");
				java.util.Date d1 = df.parse(k[4]);
				java.util.Date d2 = df.parse(begin[4]);
				java.util.Date d3 = df.parse(end[4]);
				/**
				 * 开始文件和结束文件的hw设备号一致
				 */
				// if (begin[2].equals(end[2])) {
				if (d1.getTime() >= d2.getTime()
						&& d1.getTime() <= d3.getTime()) {
					upFileMap.put(key, map.get(key));
//					log.error("開始和結束的key" + key);
//					log.error("開始文件名和結束文件名" + map.get(key));
					
				}
//				log.error("3.解析csv txt文件内容");
				/**
				 * 开始文件和结束文件的hw设备号不一致时 1.开始的文件中的hw和map中的key
				 * 2.结束中的文件的hw和map中的key比较
				 */
				// else if ((k[2].equals(begin[2])) || (k[2].equals(end[2]))) {
				// if (d1.getTime() >= d2.getTime()
				// || d1.getTime() <= d3.getTime()) {
				// upFileMap.put(key, map.get(key));
				// log.error("開始和結束的key不一致時" + key);
				// log.error("開始文件名和結束文件名不一致時" + map.get(key));
				// }
				// }
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
//		SFTPFileUtil sftp = new SFTPFileUtil("182.48.116.94",22,60000,"dujun","dujun1991");
		
		String loginMsg = sftp.login();
		if(!loginMsg.equals("SUCCESS")) return loginMsg;//如果登陆失败 返回登陆错误信息 
		
		Iterator<Map.Entry<String,String>> it = upFileMap.entrySet().iterator();
		
//		for (String key : upFileMap.keySet()) {
		log.debug("remove before"+upFileMap.size());
		while(it.hasNext()){
			Map.Entry<String,String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			
			String filePahtGZ = value;
			String filePathCHK = value+".CHK";
			if(key.substring(key.lastIndexOf(".")).equals(".gz")){
				try {
					//upMsp中已经有错误信息了  gz文件
					//先上传.gz文件  成功后上传.chk文件
//					String upMsgGZ = sftp.uploadFile("/upload", key, new FileInputStream(filePahtGZ));
					String upMsgGZ=sftp.uploadFile("/data/IF_UPLOAD/PS_Log_Server/JS/YDJSG00001", key, new FileInputStream(filePahtGZ));
					if(upMsgGZ.equals("SUCCESS")){//gz上传成功后上传CHK文件
//						String upMsgCHK = sftp.uploadFile("/upload", key+".CHK", new FileInputStream(filePathCHK));
						String upMsgCHK=sftp.uploadFile("/data/IF_UPLOAD/PS_Log_Server/JS/YDJSG00001", key+".CHK", new FileInputStream(filePathCHK));
						if(upMsgCHK.equals("SUCCESS")){
							it.remove();//.gz文件上传成功后从map中清除 防止再次上传
						}else{//gz上传成功 但对应的CHK上传失败
							return key+" up success,"+key+".chk is error";
						}
					}else{
						return upMsgGZ;
					}
//					String upMsg=sftp.uploadFile("/data/IF_UPLOAD/PS_Log_Server/JS/YDJSG00001", key, new FileInputStream(upFileMap.get(key)));
//					System.out.println("===========>"+upFileMap.get(key));
//					log.debug("===========>上传压缩文件"+upMsg);
					//调用FTP脚本失败   文件名+错误信息
				} catch (Exception e) {
					e.getStackTrace();
					log.error(e.getMessage());
					return e.getMessage();
				}
			}
		}
		log.debug("remove"+upFileMap.size());
		sftp.logout();
		return stateMsg;
		
			
			
//			调用FTP脚本
//			String upMsg = ReadShell.runSell("shellPath", "shellName", upFileMap.get(key));
//			if(!upMsg.equals("SUCCESS")) stateMsg += key +":"+upMsg+";";//调用FTP脚本失败   文件名+错误信息
			
//			try {
////				String upMsg=sftp.uploadFile("/upload", "ztt.gz", new FileInputStream("C:/Users/dell/ztt.gz"));
//				String upMsg=sftp.uploadFile("/upload", key, new FileInputStream(upFileMap.get(key)));
//				System.out.println("===========>"+upFileMap.get(key));
//				System.out.println("===========>"+key);
////				if(upMsg.equals("SUCCESS")){
////					System.out.println("上传成功");
////					return stateMsg=" ";
////				}
//				System.out.print(key);
////				stateMsg += key +":"+upMsg+";";//调用FTP脚本失败   文件名+错误信息
//				
//			} catch (Exception e) {
////				sb.append(e.getStackTrace());
//				e.getStackTrace();
//			}
	    	
			
//			String upMsg = SFTPFileUtil.uploadFile();
//			System.out.print("service======="+ upMsg);
//			System.out.println("输出de 结果"+upMsg);
//			if(!upMsg.equals("SUCCESS")) stateMsg +=key +":"+ upMsg+";";//调用FTP脚本失败   文件名+错误信息
//		}
//		System.out.println("输出"+stateMsg);
//		return stateMsg;

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
