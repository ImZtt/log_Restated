package com.dinglicom.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;


/**
 * FTP对文件的操作
 * 1.uploadFile	上传
 * 		input 输入流可以是文件地址("D:/test.txt")	输入流可以是内容(InputStream input = new ByteArrayInputStream("写入文件的内容".getBytes("utf-8")));
 * 2.downFile	下载
 * @author djun
 *
 */
public class FTPFileUtil{
	private static Log log = LogFactory.getLog(FTPFileUtil.class);
    /**  文件上传源代码
    * Description: 向FTP服务器上传文件  
    * @Version1.0  
    * @param url FTP服务器hostname  
    * @param port FTP服务器端口  
    * @param username FTP登录账号  
    * @param password FTP登录密码  
    * @param path FTP服务器保存目录  
    * @param filename 上传到FTP服务器上的文件名  
    * @param input 输入流  
    * @return 成功返回true，否则返回false  
    */   
   public static String uploadFile( 
           String url,//FTP服务器hostname
           int port,//FTP服务器端口 
           String username, // FTP登录账号  
           String password, //FTP登录密码 
           String path, //FTP服务器保存目录 
           String filename, //上传到FTP服务器上的文件名  
           InputStream input // 输入流  
           ) {   
//       boolean success = false;   
       StringBuffer sb = new StringBuffer();  
       FTPClient ftp = new FTPClient();   
       try {   
           int reply;   
           ftp.connect(url, port);//连接FTP服务器    
           //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器    
           ftp.login(username, password);//登录  
           try{
           // 连接后检测返回码来校验连接是否成功
           reply = ftp.getReplyCode();   
           if (!FTPReply.isPositiveCompletion(reply)) {   
               ftp.disconnect(); 
               log.error("连接失败");
               return "连接失败";   
           }   
           }catch (IOException e) {   
               sb.append(e.getMessage());
           }
           //切换路径
           ftp.changeWorkingDirectory(path);  
           //上传
           ftp.storeFile(filename, input);            
           input.close(); 
          //ftp注销
           ftp.logout();  
            log.error("上传成功");
            return  "SUCCESS";   
       } catch (IOException e) {
    	   sb.append(e.getMessage());  
//           e.printStackTrace();   
       } finally {   
           if (ftp.isConnected()) {   
               try {   
                   ftp.disconnect();   
               } catch (IOException ioe) {   
               }   
           }   
       }   
       return sb.toString();   
   }
   /**  
    * Description: 从FTP服务器下载文件  
    * @Version1.0  
    * @param url FTP服务器hostname  
    * @param port FTP服务器端口  
    * @param username FTP登录账号  
    * @param password FTP登录密码  
    * @param remotePath FTP服务器上的相对路径  
    * @param fileName 要下载的文件名  
    * @param localPath 下载后保存到本地的路径  
    * @return  
    */   
   public static boolean downFile( 
           String url, //FTP服务器hostname 
           int port,//FTP服务器端口 
           String username, //FTP登录账号 
           String password, //FTP登录密码 
           String remotePath,//FTP服务器上的相对路径  
           String fileName,//要下载的文件名 
           String localPath//下载后保存到本地的路径 
           ) {   
       boolean success = false;   
       FTPClient ftp = new FTPClient();   
       try {   
           int reply;   
           ftp.connect(url, port);   
           //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器    
           ftp.login(username, password);//登录    
           reply = ftp.getReplyCode();   
           if (!FTPReply.isPositiveCompletion(reply)) {   
               ftp.disconnect();   
               return success;   
           }   
           ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录    
           FTPFile[] fs = ftp.listFiles();   
           for(FTPFile ff:fs){   
               if(ff.getName().equals(fileName)){   
                   File localFile = new File(localPath+"/"+ff.getName());   
                   OutputStream is = new FileOutputStream(localFile);    
                   ftp.retrieveFile(ff.getName(), is);   
                   is.close();   
               }   
           }   
                
           ftp.logout();   
           success = true;   
       } catch (IOException e) {   
           e.printStackTrace();   
       } finally {   
           if (ftp.isConnected()) {   
               try {   
                   ftp.disconnect();   
               } catch (IOException ioe) {   
               }   
           }   
       }   
       return success;   
   }
   
   
   
   
//==============================================FTP测试====================================================================//   
   
   
//   public static void main(String[] args) {
//	   testUpLoadFromString();
//   }
//   
   /** 
    * 将本地文件上传到FTP服务器上 
    * 
    */ 
//   public static void testUpLoadFromDisk(){   
//       try {   
//           FileInputStream in=new FileInputStream(new File("D:/test.txt"));   
//           boolean flag = uploadFile("192.168.1.155", 21, "test", "001234", "/home/test/home.test", "test.txt", in);   
//           System.out.println(flag);   
//       } catch (FileNotFoundException e) {   
//           e.printStackTrace();   
//       }    
//   }
   /** 
    * 在FTP服务器上生成一个文件，并将一个字符串写入到该文件中 
    * 
    */ 
//   public static void testUpLoadFromString(){   
//       try {   
//           String str = "这是要写入的字符串！"; 
//           InputStream input = new ByteArrayInputStream(str.getBytes("utf-8"));   
//           boolean flag = uploadFile("192.168.1.155", 21, "test", "001234", "/home/test", "test.txt", input);   
//           System.out.println(flag);   
//       } catch (UnsupportedEncodingException e) {   
//           e.printStackTrace();   
//       }   
//   }
   
   /** 
    * 将FTP服务器上文件下载到本地 
    * 
    */ 
   public static void testDownFile(){ 
       try {   
           boolean flag = downFile("127.0.0.1", 21, "administrator", "zyuc2011", "test", "test.txt", "D:/");   
           System.out.println(flag);   
       } catch (Exception e) {   
           e.printStackTrace();   
       }        
   }
   
  
   
   
} 