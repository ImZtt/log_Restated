package com.dinglicom.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;





/**
 * 
 * @author wenxu
 *
 */
public class DLUtils
{
	private static final Log LOG = LogFactory.getLog(DLUtils.class);
	public static DLUtils util = null;
	public DLUtils() 
	{
		super();
	}
	/**
	 * 根据文件名获取resources属性文件
	 * @param configName
	 * @return
	 */
	public static Properties getProperties(String configName)
	{
		InputStream inputStream = getInstance().getClass().getClassLoader().getResourceAsStream(configName);
		Properties p = new Properties();
		try {
			p.load(inputStream);
			inputStream.close();
		} catch (IOException e1) {
			LOG.warn(e1,e1);
		}
		return p;	
	}
	
/**
 * 字节数组反转
 * @param src
 * @return
 */
	public static byte[] arrReverse(byte[] src){
		byte[] obj = new byte[src.length];
		for (int i = 0; i < src.length; i++) {
			obj[i] = src[src.length-1-i];
		}
		return obj;
	}
	/**
	 * 数组截取
	 * @param srcArr
	 * @param start
	 * @param length
	 * @return
	 */
	public static byte[] getArray(byte[] srcArr,int start,int length){
		byte[] arr = new byte[length];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = srcArr[start+i];
		}
		//System.arraycopy(srcArr, start, arr, 0, length);
		return arr;
	}
	/**
	 * 数组截取
	 * @param srcArr
	 * @param start
	 * @param length
	 * @return
	 */
	public static byte[] getArray(byte[] srcArr,int start,int length,boolean reverse){
		byte[] arr = new byte[length];
		if(reverse){
			int start2 = start+arr.length-1;
			for (int i = 0; i < arr.length; i++) {
				arr[i] = srcArr[start2-i];
			}
		}else{
			for (int i = 0; i < arr.length; i++) {
				arr[i] = srcArr[start+i];
			}
		}
		//System.arraycopy(srcArr, start, arr, 0, length);
		return arr;
	}
	/**
	 * 对数组拷贝做了一层封装
	 * @param srcArr
	 * @param start
	 * @param length
	 * @return
	 */
	public static void arraycopy(byte[] srcArr,int srcstart,byte[] destArr,int destStart,int length){
		if(srcArr==null || length+srcstart >srcArr.length
				|| destArr==null||length+destStart>destArr.length)
			return;
		
		for (int i = 0; i < length; i++) {
			destArr[destStart+i] = srcArr[srcstart+i];
		}

	}
	/**
	 * 数组追加
	 * @param a
	 * @param b
	 * @return
	 */
	public static byte[] appendArray(byte[] a,byte[] b){
		byte[] c = new byte[a.length+b.length];
		for (int i = 0; i < a.length; i++) {
			c[i] = a[i];
		}
		for (int j = 0; j < b.length; j++) {
			c[a.length+j] = b[j];
		}
		return c;
	}
	/**
	 * 指定长度转化bigInteger，针对0000XXXX类型的数据
	 * @param b
	 * @param len
	 * @return
	 */
	public static byte[] getArrayByBigInt(BigInteger b,int len){
		byte[] tmp = b.toByteArray();
		byte[] result = new byte[len];
		if(tmp.length<=len){			
			System.arraycopy(tmp, 0, result, len-tmp.length, tmp.length);
			for (int i = 0; i < (result.length-tmp.length); i++) {
				result[i] = 0;
			}
		}else{
			result = getArray(tmp, tmp.length-len, len);
		}
		return result;
	}
	/**
	 * 根据字节数组将long转换高低位
	 * @param old
	 * @return
	 */
	public long toLong(byte[] old) {  
        long ds[] = new long[8];  
          
        ds[7] = (long) (old[7]);  
        ds[6] = (long) (old[6]);  
        ds[5] = (long) (old[5]);  
        ds[4] = (long) (old[4]);  
        ds[3] = (long) (old[3]);  
        ds[2] = (long) (old[2]);  
        ds[1] = (long) (old[1]);  
        ds[0] = (long) (old[0]);  
  
        return (((ds[7]& 0xff) << 56) +((ds[6]& 0xff) << 48) +((ds[5]& 0xff) << 40) +((ds[4]& 0xff) << 32) +((ds[3]& 0xff) << 24) + ((ds[2]& 0xff) << 16) + ((ds[1]& 0xff) << 8) + ((ds[0]& 0xff) << 0));  
    }
	/**
	 * 字节数组转16进制字符串
	 * @param b
	 * @return
	 * @throws Exception
	 */
	public static String getHexString(byte[] src){
//		String result= "";
//		try {
//			for (int i=0; i < b.length; i++) {
//			result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
//			}
//		} catch (Exception e) {
//			LOG.warn(e, e);
//			e.printStackTrace();
//		}
//		return result;
		StringBuilder stringBuilder = new StringBuilder("");  
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }  
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;  
	        String hv = Integer.toHexString(v);  
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }  
	        stringBuilder.append(hv);  
	    }  
	    return stringBuilder.toString();  
	}
	
	/** 
	 * Convert hex string to byte[] 
	 * @param hexString the hex string 
	 * @return byte[] 
	 */  
	public static byte[] hexStringToBytes(String hexString) {  
	    if (hexString == null || hexString.equals("")) {  
	        return null;  
	    }  
//	    hexString = hexString.toUpperCase();  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	    }  
	    return d;  
	}  
	/** 
	 * Convert char to byte 
	 * @param c char 
	 * @return byte 
	 */  
	 private static byte charToByte(char c) {
		//return getMap().get(c).byteValue();
		 if(c>=48 && c<=57)
			 return (byte)(c-48);
		 else if(c>=65 && c<=70)
			 return (byte)(c-55);
		 if(c>=97 && c<=102)
			 return (byte)(c-87);
		 else
			 return 0;
//	    return (byte) "0123456789ABCDEF".indexOf(c);  
	}  
	/**
	 * 返回正则表达式匹配到的字符串
	 * @param src
	 * @param reg
	 * @return
	 */
	public static String getMatcherGroup(String src,String reg){
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(src);
		List<String> list = new ArrayList<String>();
		String rtn = "";
		try {
		  while(matcher.find())//带参数的find会重置之前的匹配结果
		  {
			  String srcStr = matcher.group();    //返回由上一次匹配操作所匹配的输入子序列,如果没有find操作，则没有子序列，会报No match found
			  list.add(srcStr);          //将匹配的结果放入List
		  }
		  if(list.size()!=0){			  
			  rtn = list.get(0);
		  }
		} catch (Exception e) {
			LOG.warn(e, e);
		}
		return rtn;
	}
	/**
	 * utc时间格式化为日期字符串
	 * @param timeStamp
	 * @param formats
	 * @return
	 */
	public static String TimeStamp2Date(long timeStamp, String formats) {
		String date = new java.text.SimpleDateFormat(formats)
				.format(new java.util.Date(timeStamp));
		return date;
	}
/**
 * ip转数值型
 * @param ip
 * @return
 */
	public long IP2number(String ip){
		long i_ip = 0;
		byte[] arr = new byte[8];
		String[] str_arr = ip.split("\\.");
		for (int i = 4; i < arr.length; i++) {
			arr[i]=Byte.parseByte(str_arr[i-4]);
		}
		i_ip = ByteConvert.bytesToLong(arr);
		return i_ip;
	}
	public static String xdridConvert(String xdrid){
		if(xdrid.length()!=32&&xdrid.length()!=16){
			LOG.warn("xdrid must be 32,16 length ");
			return "";
		}
		byte[] tmpArr= hexStringToBytes(xdrid);
		BigInteger bi  = new BigInteger(tmpArr);
		return bi.toString();
	}
	
	/**
	 * 添加WireShark包头
	 * @return
	 */
//	public static byte[] createWireSharkHead(){
//		byte[] wiresharkhead = new byte[24];
//		byte[] magic = DLUtils.arrReverse(ByteConvert.uintToBytes(0xa1b2c3d4));
//		System.arraycopy(magic, 0, wiresharkhead, 0, 4);
//		byte[] version_major = DLUtils.arrReverse(ByteConvert.ushortToBytes(0x2));
//		System.arraycopy(version_major, 0, wiresharkhead, 4, 2);
//		byte[] version_minor = DLUtils.arrReverse(ByteConvert.ushortToBytes(0x4));
//		System.arraycopy(version_minor, 0, wiresharkhead, 6, 2);
//		byte[] thiszone = DLUtils.arrReverse(ByteConvert.intToBytes(0));
//		System.arraycopy(thiszone, 0, wiresharkhead, 8, 4);
//		byte[] sigfigs = DLUtils.arrReverse(ByteConvert.uintToBytes(0));
//		System.arraycopy(sigfigs, 0, wiresharkhead, 12, 4);
//		byte[] snaplen = DLUtils.arrReverse(ByteConvert.uintToBytes(0x0000ffff));
//		System.arraycopy(snaplen, 0, wiresharkhead, 16, 4);
//		byte[] linktype = DLUtils.arrReverse(ByteConvert.intToBytes(0x1));
//		System.arraycopy(linktype, 0, wiresharkhead, 20, 4);
//		return wiresharkhead;
//	}
	/**
	 * 获取本服务类的实例
	 * @return
	 */
	public static DLUtils getInstance()
	{
		if(util!=null)
		{
			return util;
		}
		else
		{
			return new DLUtils();
		}
	}
}
