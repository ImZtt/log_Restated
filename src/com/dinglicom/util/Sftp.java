package com.dinglicom.util;

import java.util.Properties;

public class Sftp {
	@SuppressWarnings("unused")
	public static void login(Properties properties) {
        String ip = properties.getProperty("ip");
        String user = properties.getProperty("user");
        String pwd = properties.getProperty("pwd");
        String port = properties.getProperty("port");
        String privateKeyPath = properties.getProperty("privateKeyPath");
        String passphrase = properties.getProperty("passphrase");
        String sourcePath = properties.getProperty("sourcePath");
        String destinationPath = properties.getProperty("destinationPath");

//        if (ip != null && !ip.equals("") && user != null && !user.equals("")
//                && port != null && !port.equals("") && sourcePath != null
//                && !sourcePath.equals("") && destinationPath != null
//                && !destinationPath.equals("")) {
//
//            if (privateKeyPath != null && !privateKeyPath.equals("")) {
////                sshSftp2(ip, user, Integer.parseInt(port), privateKeyPath,
//                        passphrase, sourcePath, destinationPath);
//            } else if (pwd != null && !pwd.equals("")) {
//                sshSftp(ip, user, pwd, Integer.parseInt(port), sourcePath,
//                        destinationPath);
//            } else {
//                Console console = System.console();
//                System.out.print("Enter password:");
//                char[] readPassword = console.readPassword();
//                sshSftp(ip, user, new String(readPassword),
//                        Integer.parseInt(port), sourcePath, destinationPath);
//            }
//        } else {
//            System.out.println("请先设置配置文件");
//        }
    }
}
