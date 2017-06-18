package com.dinglicom.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class ReadShell {
	public static String runSell(String shellPath , String shellName, String parameter){  
//	    JSONObject result = new JSONObject();  
	    System.out.println("接收到参数:shellPath=" + shellPath); 
	    System.out.println("接收到参数:shellName=" + shellName);  
	    System.out.println("接收到参数:parameter=" + parameter);  
	      
	    String osname = System.getProperty("os.name");  
	    if ((osname != null) && (osname.toLowerCase().startsWith("win"))){  
	        System.out.println("当前操作系统是:"+osname);  
//	        result.put("code", "0");  
//	        result.put("msg", "当前服务器操作系统不是linux");  
	        return "当前服务器操作系统不是linux";  
	    }  
	    else if(shellPath == null || shellPath.equals("")){  
	       // result.put("code", "0");  
//	        result.put("msg", "dbCode/targetPath不能为空");  
	        return "shellPath不能为空";  
	    }  
//	    System.out.println("接收到参数:targetPath=" + parameter);  
	    else if(shellName == null || shellName.equals("")){  
	       // result.put("code", "0");  
//	        result.put("msg", "dbCode/targetPath不能为空");  
	        return "shellName不能为空";  
	    }  
//	    System.out.println("接收到参数:targetPath=" + parameter);  
	    else if(parameter == null || parameter.equals("")){  
	       // result.put("code", "0");  
//	        result.put("msg", "dbCode/targetPath不能为空");  
	        return "parameter不能为空";  
	    }  
	      
	    //脚本路径  
//	    String shellPath = request.getServletContext().getRealPath("/")+"WEB-INF/classes";  
//	    String shellPath = "/usr/project/shell";
//	    String cmd = shellPath + "/djun.sh " + parameter;  
	    String cmd = shellPath.trim() +"/"+ shellName.trim()+ " " + parameter.trim();  
	    ProcessBuilder builder = new ProcessBuilder("/bin/sh","-c",cmd);  
	    builder.directory(new File(shellPath));  
	      
	    int runningStatus = 0;  
	    String s = null;  
	    StringBuffer sb = new StringBuffer();  
	    try {  
	        Process p = builder.start();  
	          
	        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));  
	           BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));  
	           while ((s = stdInput.readLine()) != null) {  
	               System.out.println("shell log info ...." + s);  
	               sb.append(s);  
	           }  
	           while ((s = stdError.readLine()) != null) {  
	               System.out.println("shell log error...." + s);  
	               sb.append(s);  
	           }  
	           try {  
	               runningStatus = p.waitFor();  
	           } catch (InterruptedException e) {  
	            runningStatus = 1;  
	            System.out.println("等待shell脚本执行状态时，报错...");  
	            sb.append(e.getMessage());  
	           }  
	             
	           closeStream(stdInput);  
	           closeStream(stdError);  
	             
	    } catch (Exception e) {  
	        System.out.println("执行shell脚本出错...");  
	        sb.append(e.getMessage());  
	        runningStatus =1;  
	    }  
	    System.out.println("runningStatus = " + runningStatus);  
	    if(runningStatus == 0){  
	        //成功  
//	        result.put("code", "1");  
//	        result.put("msg", "success");  
	        return "SUCCESS";  
	    }else{  
//	       /result.put("code", "0");  
//	        result.put("msg", "fail" + sb.toString());  
	        return sb.toString();  
	    }  
	}  
	  
	private static void closeStream(BufferedReader reader){  
	    try {  
	        if(reader != null){  
	            reader.close();  
	        }  
	    } catch (Exception e) {  
	        reader = null;  
	    }  
	} 
}
