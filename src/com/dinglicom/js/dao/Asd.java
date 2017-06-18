package com.dinglicom.js.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Asd {
//	public static Properties readFtp=DLUtils.getProperties("ftp.properties");
	public static void main(String[] args) throws Exception{  
		    
//        	String cmdstring =readFtp.getProperty("qqq");
//        	 Process proc = Runtime.getRuntime().exec(cmdstring);
//        	 System.out.print(proc);
        	 try { 
            String shpath="/dinglicom/csv_split/bin/sftp.sh";  
            Process ps = Runtime.getRuntime().exec(shpath);  
            ps.waitFor();  
  
            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));  
            StringBuffer sb = new StringBuffer();  
            String line;  
            while ((line = br.readLine()) != null) {  
                sb.append(line).append("\n");  
            }  
            String result = sb.toString();  
            System.out.println(result);  
            }   
        catch (Exception e) {  
            e.printStackTrace();  
            }  
    }  
}
