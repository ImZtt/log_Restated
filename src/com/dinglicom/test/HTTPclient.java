package com.dinglicom.test;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
public class HTTPclient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		SendHttpService ser = new SendHttpService();
//		String url = "http://localhost:8080/queryAndDecodeJS/fileSearcher";
//		String message = "{\"end_time\":\"1493973399\",\"xdr_interface\":\"7\",\"cdr_id\":\"05086601ADCCE031\",\"start_time\":\"1463973399\"}";
//				
//		System.out.println(ser.sendPost(url, message));
		try {
			testQuery();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testQuery() throws IOException, URISyntaxException {
		//澶ф嫭鍙峰唴浜沯son涓�
//		String inputJsonStr = "{\"messageType\":\"1\",\"subscriptionURL\":\"172.16.36.116\",\"keyA\":\"10.25.176.1\"}";
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<info id=\""+111+"\" type=\"query_response\">\n");
		sb.append("<beginFilename>"+"LTE_JS_HW_XXXX_20170331113705"+"</beginFilename>");
		sb.append("<endFilename>"+"LTE_JS_HA_1234_20170331114511"+"</endFilename>");
		sb.append("</info>");
//		String inputJsonStr = "{\"id\":\"11\",\"beginFilename\":\"LTE_JS_HW_XXXX_20170331113705\",\"endFilename\":\"LTE_JS_HA_1234_20170331114511\"}";
//		String inputJsonStr = "{\"id\":\"11\",\"beginFilename\":\"LTE_JS_HW_XXXX_20170331113705\",\"endFilename\":\"LTE_JS_HA_1234_20170331114511\"}";
		HttpClient httpclient = new DefaultHttpClient(); //13800510543
        try {
//        	HttpEntityWrapper en = new HttpEntityWrapper(new StringEntity(sb.toString(), 
        			HttpEntityWrapper en = new HttpEntityWrapper(new StringEntity(sb.toString(),
	        		   ContentType.create("application/xml", "UTF-8")));
	        //HttpPost post = new HttpPost("http://172.16.23.167:8180/queryServer/qrycdr");
        	//鏈湴鐢ㄧ殑灏忕尗绔彛鍙蜂互鍙婂悕绉颁箣绫荤殑涓滆タ
	        HttpPost post = new HttpPost("http://127.0.0.1:8082/log_Restated/queryAll");
	       // HttpPost post = new HttpPost("http://172.16.36.191:8080/queryServer/qrycdr");
	        post.setEntity(en);
	        HttpResponse response = httpclient.execute(post);
	        HttpEntity resEntity = response.getEntity();
	        System.out.println(EntityUtils.toString(resEntity));
        } finally { 
        	httpclient.getConnectionManager().shutdown();
        }
		
	}
}
