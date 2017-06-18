package com.dinglicom.util;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dinglicom.js.bean.RestatedRequest;

//import com.dinglicom.js.bean.RestatedRequest;

/**
 * 读取文件 XML
 * 
 * @author ztt
 *
 */
public class FileRead20170615 {

	private static Log log = LogFactory.getLog(FileRead20170615.class);

	/**
	 * 过滤出指定后缀文件 并且升序排序
	 * 
	 * @param fileType
	 *            需要过滤的文件类型 例如过滤出java文件 则使用: .java
	 * @return 符合过滤条件的文件名结果集合
	 * @throws ParseException
	 */
	public static List<String> upFileList = new ArrayList<String>();

	public List<String> ListFileNameFileter(final String fileType,
			RestatedRequest restatedRequest) throws ParseException {
		File file = new File(filePath);
		String[] files = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				// 文件以.gz结尾
				return name.endsWith(".gz");
			}
		});
		List<String> list = Arrays.asList(files);
		Collections.sort(list);// 升序
		String[] begin = restatedRequest.getBeginFilename().split("_");
		String[] end = restatedRequest.getEndFilename().split("_");
		String fileName=restatedRequest.getFileName();
		DateFormat df = new SimpleDateFormat("YYYYMMDDHHmmSS");
		if(fileName.equals(" ") ||fileName.equals(null)){
			if(restatedRequest.getBeginFilename().equals(
					restatedRequest.getEndFilename())){
				upFileList.addAll(list);
			}else {
			for (String l : list) {
				String[] k = l.split("_");
				try {
					java.util.Date d1 = df.parse(k[4]);
					java.util.Date d2 = df.parse(begin[4]);
					java.util.Date d3 = df.parse(end[4]);
					/**
					 * 开始文件和结束文件的hw设备号一致
					 */
					if (d1.getTime() >= d2.getTime()
							&& d1.getTime() <= d3.getTime()) {
						upFileList.add(l);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
		}else{
			upFileList.add(fileName);
		}
		
		
		
		
		return upFileList;
	}

	public static Map<String, String> readFileOrDirectory(String path,
			String filterFileType) {

		// String path = "D:\\waterSoftware\\DataSend\\config";
		Map<String, String> map = new HashMap<String, String>();
		// File file = new File(path);
		// // File[] tempList = file.listFiles();
		// File[] tempList = file.listFiles(new FilenameFilter() {
		//
		// @Override
		// public boolean accept(File dir, String name) {
		// // TODO Auto-generated method stub
		// // File file=new File(path);
		//
		// return false;
		// }
		// });
		// // log.info("该目录下对象个数：" + tempList.length);
		// for (int i = 0; i < tempList.length; i++) {
		// if (tempList[i].isFile()) {
		// String filePath = tempList[i].getPath();//获取当前文件路径
		// //astIndexOf() 方法可返回一个指定的字符串值最后出现的位置，在一个字符串中的指定位置从后向前搜索。
		// String fileType = filePath.substring(filePath.lastIndexOf("."),
		// filePath.length());//获取文件类型
		// // log.error("获取文件的类型"+fileType);
		// // if(fileType.equals(filterFileType[0])||
		// fileType.equals(filterFileType[1])) {map.put(tempList[i].getName(),
		// filePath);}
		// if(fileType.equals(filterFileType)) {map.put(tempList[i].getName(),
		// filePath);}
		// // log.error(tempList[0].getName());
		// // log.error("filterFileType"+filterFileType[0]);
		// // log.error("文     件：" + tempList[i]);
		// // log.error("文件路徑"+filePath);
		// }
		// if (tempList[i].isDirectory()) {
		// // log.error("文件夹：" + tempList[i]);
		// }
		// // log.error("集合"+map.get(tempList[i].getName()));
		//
		// }
		return map;
	}

	/**
	 * 读取XML文件
	 * 
	 * @param ServletInputStream
	 * @return
	 * @throws DocumentException
	 */
	public static RestatedRequest readXml(ServletInputStream inputStream)
			throws DocumentException {
		// TODO Auto-generated method stub
		SAXReader reader = new SAXReader();
		Document doc = reader.read(inputStream);
		// Document doc = reader.read(new File("D:\\parameter.xml"));
		RestatedRequest restatedRequest = new RestatedRequest();
		Element root = doc.getRootElement();
		if (root.attributeCount() > 2) {
			String id = root.attributeValue("id");// 获取根节点 info节点 的id属性
			String beginFilename = root.element("beginFilename").getText();
			String endFilename = root.element("endFilename").getText();
			if ((beginFilename == " " || beginFilename == null) || (endFilename == " " || endFilename == null)) {
				log.error("beginFilename or endFilename fail");
				return null;
			}
			restatedRequest.setId(id);
			restatedRequest.setBeginFilename(beginFilename);
			restatedRequest.setEndFilename(endFilename);
			log.error(id);
			log.error(beginFilename);
			log.error(endFilename);
		} else {
			String id = root.attributeValue("id");// 获取根节点 info节点 的id属性
			String fileName = root.element("fileName").getText();
			if (fileName == " " || fileName == null) {
				return null;
			}
			restatedRequest.setId(id);
			restatedRequest.setFileName(fileName);
		}
		return restatedRequest;
	}

	// =============== 构造器及变量
	private String filePath;

	/**
	 * 无参构造 2017年6月14日
	 */
	public FileRead20170615() {
	}// 无参构造

	/**
	 * 一参构造
	 * 
	 * @param filePath
	 *            需要过滤的文件类型
	 */
	public FileRead20170615(String filePath) {
		this.filePath = filePath;
	}

}
