package com.dinglicom.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
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
public class FileRead implements FilenameFilter {
	private static Log log = LogFactory.getLog(FileRead.class);
	private String type;

	/**
	 * 文件類型
	 * @param name
	 */
	public FileRead(String name) {
		this.type = name;
	}

	/**
	 * 读取文件和文件夹
	 * 
	 * @param path
	 *            读取文件夹路径
	 * @return map key:文件名 value:文件路径
	 */
	public boolean accept(String path){
		return accept(null, path);
	}

	@Override
	public boolean accept(File fi, String path) {
		File file = new File(path);
		String[] names;
		// 获得所有gz文件
		FileRead filter = new FileRead(type);
		names = file.list(filter);
		// 打印所有java文件名称
		for (int i = 0; i < names.length; i++) {
//			System.out.println("i:" + i + " name:" + names[i]);
			
		}
		return false;
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
		
		String id = root.attributeValue("id");// 获取根节点 info节点 的id属性
		String beginFilename = root.element("beginFilename").getText();
		String endFilename = root.element("endFilename").getText();
		if ((beginFilename == " " || beginFilename == null)
				|| (endFilename == " " || endFilename == null)) {
			log.error("beginFilename or endFilename fail");
			return null;
		}
		
		restatedRequest.setId(id);
		restatedRequest.setBeginFilename(beginFilename);
		restatedRequest.setEndFilename(endFilename);
		log.error(id);
		log.error(beginFilename);
		log.error(endFilename);
		return restatedRequest;
	}

}
