package com.xt.cfp.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SFTPUtil {
	private static Logger log = Logger.getLogger(SFTPUtil.class);
    
    private ChannelSftp sftp;
    
    private Session session;  
    //FTP 服务器地址IP地址
 	private String host;
 	// FTP 端口
 	private int port;
 	// FTP 登录用户名
 	private String username;
 	// FTP 登录密码
 	private String password;
    
    public SFTPUtil(){}
    
    public SFTPUtil(String host, int port, String username, String password) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	/** 
     * 连接sftp服务器 
     *  
     * @throws Exception 
     */  
    public void login(){  
        try {  
            JSch jsch = new JSch();  
            session = jsch.getSession(username, host, port);  
            if (password != null) {  
                session.setPassword(password);  
            }  
            Properties config = new Properties();  
            config.put("StrictHostKeyChecking", "no");  
              
            session.setConfig(config);  
            session.connect();  
              
            Channel channel = session.openChannel("sftp");  
            channel.connect();  
  
            sftp = (ChannelSftp) channel;  
            log.info(String.format("sftp server host:[%s] port:[%s]已连接", host, port));  
        } catch (JSchException e) { 
        	log.error(String.format("无法连接到sftp host:[%s] port:[%s],[%s]", host,port,e.getMessage()));
            e.printStackTrace();
        }  
    }  
  
    /** 
     * 关闭连接 server 
     */  
    public void logout(){  
        if (sftp != null) {  
            if (sftp.isConnected()) {  
                sftp.disconnect();  
                log.info("sftp连接已关闭");  
            }  
        }  
        if (session != null) {  
            if (session.isConnected()) {  
                session.disconnect();  
            }  
        }  
    }  
  
    /** 
     * 将输入流的数据上传到sftp作为文件 
     *  
     * @param directory 
     *            上传到该目录 
     * @param sftpFileName 
     *            sftp端文件名 
     * @param in 
     *            输入流 
     * @throws SftpException  
     */  
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException{  
        try {  
            sftp.cd(directory);  
        } catch (SftpException e) {  
            sftp.mkdir(directory);  
            sftp.cd(directory);  
        }  
        sftp.put(input, sftpFileName);  
    }  
  
    /** 
     * 上传单个文件 
     *  
     * @param directory 
     *            上传到sftp目录 
     * @param file 
     *            要上传的文件
     * @throws FileNotFoundException  
     * @throws SftpException  
     */  
    public void upload(String checkPath,File file){  
        try {
			upload(checkPath, file.getName(), new FileInputStream(file));
		} catch (FileNotFoundException | SftpException e) {
			e.printStackTrace();
		}  
    }  
  
    /** 
     * 将byte[]上传到sftp，作为文件。注意:从String生成byte[]是，要指定字符集。 
     *  
     * @param directory 
     *            上传到sftp目录 
     * @param sftpFileName 
     *            文件在sftp端的命名 
     * @param byteArr 
     *            要上传的字节数组 
     * @throws SftpException  
     * @throws Exception 
     */  
    public void upload(String directory, String sftpFileName, byte[] byteArr) throws SftpException{  
        upload(directory, sftpFileName, new ByteArrayInputStream(byteArr));  
    }  
  
    /** 
     * 将字符串按照指定的字符编码上传到sftp 
     *  
     * @param directory 
     *            上传到sftp目录 
     * @param sftpFileName 
     *            文件在sftp端的命名 
     * @param dataStr 
     *            待上传的数据 
     * @param charsetName 
     *            sftp上的文件，按该字符编码保存 
     * @throws UnsupportedEncodingException  
     * @throws SftpException  
     * @throws Exception 
     */  
    public void upload(String directory, String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException{  
        upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));  
  
    }  
  
    /** 
     * 下载文件 
     *  
     * @param directory 
     *            下载目录 
     * @param downloadFile 
     *            下载的文件 
     * @param saveFile 
     *            存在本地的路径 
     * @throws SftpException  
     * @throws FileNotFoundException  
     */  
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException{  
        if (directory != null && !"".equals(directory)) {  
            sftp.cd(directory);  
        }  
        File file = new File(saveFile);  
        sftp.get(downloadFile, new FileOutputStream(file));
    }  
    
    /** 
     * 下载文件 
     * @param directory 下载目录 
     * @param downloadFile 下载的文件名 
     * @return 字节数组 
     * @throws SftpException  
     * @throws IOException  
     */  
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException{  
        if (directory != null && !"".equals(directory)) {  
            sftp.cd(directory);  
        }  
        InputStream is = sftp.get(downloadFile);  
          
        byte[] fileData = IOUtils.toByteArray(is);  
        return fileData;  
    }  
    
    /**
	 * 指定目录下，是否存在文件或文件夹
	 * 
	 * @param path
	 *            目录
	 * @param name
	 *            名称
	 * @return boolean
	 */
	public boolean exist(String path, String name) {
		return exist(ls(path), name);
	}
	
	/**
	 * 指定目录下文件及文件夹名称列表
	 * 
	 * @return String[]
	 */
	public String[] ls(String pathName) {
		String currentDir = currentDir();
		if (!changeDir(pathName)) {
			return new String[0];
		}
		;
		String[] result = list(Filter.ALL);
		if (!changeDir(currentDir)) {
			return new String[0];
		}
		return result;
	}
	
	/**
	 * 列出当前目录下的文件及文件夹
	 * 
	 * @param filter
	 *            过滤参数
	 * @return String[]
	 */
	@SuppressWarnings("unchecked")
	private String[] list(Filter filter) {
		Vector<LsEntry> list = null;
		try {
			// ls方法会返回两个特殊的目录，当前目录(.)和父目录(..)
			list = sftp.ls(sftp.pwd());
		} catch (SftpException e) {
			log.error("can not list directory", e);
			return new String[0];
		}

		List<String> resultList = new ArrayList<String>();
		for (LsEntry entry : list) {
			if (filter(entry, filter)) {
				resultList.add(entry.getFilename());
			}
		}
		return resultList.toArray(new String[0]);
	}
	
	/**
	 * 判断是否是否过滤条件
	 * 
	 * @param entry
	 *            LsEntry
	 * @param f
	 *            过滤参数
	 * @return boolean
	 */
	private boolean filter(LsEntry entry, Filter f) {
		if (f.equals(Filter.ALL)) {
			return !entry.getFilename().equals(".") && !entry.getFilename().equals("..");
		} else if (f.equals(Filter.FILE)) {
			return !entry.getFilename().equals(".") && !entry.getFilename().equals("..") && !entry.getAttrs().isDir();
		} else if (f.equals(Filter.DIR)) {
			return !entry.getFilename().equals(".") && !entry.getFilename().equals("..") && entry.getAttrs().isDir();
		}
		return false;
	}
	
	/** 枚举，用于过滤文件和文件夹 */
	private enum Filter {
		/** 文件及文件夹 */
		ALL,
		/** 文件 */
		FILE,
		/** 文件夹 */
		DIR
	};
	
	/**
	 * 切换工作目录
	 * @param pathName
	 *            路径
	 * @return boolean
	 */
	public boolean changeDir(String pathName) {
		if (pathName == null || pathName.trim().equals("")) {
			log.debug("invalid pathName");
			return false;
		}

		try {
			sftp.cd(pathName.replaceAll("\\\\", "/"));
			log.debug("directory successfully changed,current dir=" + sftp.pwd());
			return true;
		} catch (SftpException e) {
			log.error("failed to change directory", e);
			return false;
		}
	}
	
	/**
	 * 当前工作目录
	 * 
	 * @return String
	 */
	public String currentDir() {
		try {
			return sftp.pwd();
		} catch (SftpException e) {
			log.error("failed to get current dir", e);
			return homeDir();
		}
	}
	
	/**
	 * 根目录
	 * 
	 * @return String
	 */
	private String homeDir() {
		try {
			return sftp.getHome();
		} catch (SftpException e) {
			return "/";
		}
	}
	
	/**
	 * 判断字符串是否存在于数组中
	 * 
	 * @param strArr
	 *            字符串数组
	 * @param str
	 *            字符串
	 * @return boolean
	 */
	private boolean exist(String[] strArr, String str) {
		if (strArr == null || strArr.length == 0) {
			return false;
		}
		if (str == null || str.trim().equals("")) {
			return false;
		}
		for (String s : strArr) {
			if (s.equalsIgnoreCase(str)) {
				return true;
			}
		}
		return false;
	}
}
