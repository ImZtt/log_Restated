package com.dinglicom.util;

import java.io.File;
import java.io.FilenameFilter;
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
public class FileRead2017061516 {
	
	private static Log log = LogFactory.getLog(FileRead20170615.class);
	
	/**
     * 过滤出指定后缀文件  并且升序排序
     * @param fileType 需要过滤的文件类型  
     * 		<p>例如过滤出java文件    则使用:  .java		
     * 		<p>如果过滤test.java文件,则使用test.java
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return 符合过滤条件的文件名结果集合
     */
    public List<String> ListFileNameFileter(final String fileType,final Long beginDate,final Long endDate){
    	
//    	final Long beginDate = 20170431114511L;//开始时间
//    	final Long endDate = 20170531114511L;//结束时间
    	
        File file = new File(filePath);
        String[] files = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
            	//上传所有
            	if (beginDate == -1 && endDate == -1) {
            		return name.endsWith(fileType);
            		
            	//上传单一文件
            	}else if(beginDate == -2 && endDate == -2){
            		return name.endsWith(fileType);
            	//批量上传文件
            	}else{
					long nowNameDate = Long.valueOf(name.split("_")[4]);//当前名中的日期
					//文件以.gz结尾    && 大于开始时间   &&  小于结束时间
					return name.endsWith(fileType) && nowNameDate <= beginDate && nowNameDate <= endDate;
				}
            }
        });
        List<String> list = Arrays.asList(files);
		Collections.sort(list);//升序
		
		
		
		
		
		
        return list;
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
	public static RestatedRequest readXml(ServletInputStream inputStream) throws DocumentException {
		// TODO Auto-generated method stub
		
		SAXReader reader = new SAXReader();
		Document doc = reader.read(inputStream);
		Element root = doc.getRootElement();
		
		String id = root.attributeValue("id");// 获取根节点 info节点 的id属性
		
		String fileName = "" ,beginFilename="",endFilename="";
		
		//如果XML中不包含fileName则说明是多文件上传
		if (root.element("fileName") == null) {
			try {
				beginFilename = root.element("beginFilename").getText();
				endFilename = root.element("endFilename").getText();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				log.error(e.getMessage());
			}
			return new RestatedRequest(id, beginFilename, endFilename);
		}else{
			
			try {
				fileName = root.element("fileName").getText();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				log.error(e.getMessage());
			}
			return new RestatedRequest(id, fileName);
		}
		
	}
	
	//===============   构造器及变量
	private String filePath;
	/**
	 * 无参构造
	 * 	2017年6月14日
	 */
	public FileRead2017061516(){}//无参构造
	
	/**
	 * 一参构造
	 * @param filePath	需要读取文件的路径
	 */
	public FileRead2017061516(String filePath){
		this.filePath = filePath;
	}
	

}
