package com.xt.cfp.core.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 文件操作类
 * 
 * @author chenbo
 * @创建时间 2015年6月19日 下午4:58:51
 *
 */
public class FileUtil {

	private final static Log log = LogFactory.getLog(FileUtil.class);

	/**
	 * 生成目录
	 * 
	 * @param path
	 *            目录地址
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public static boolean mkDirs(String path) throws IllegalArgumentException {
		if (StringUtils.isNull(path)) {
			log.error("不能生成文件,文件目录为空");
			throw new IllegalArgumentException("生成文件目录名不能为空");
		}
		boolean isMk = false;

		File file = new File(path);
		if (!file.exists()) {
			isMk = file.mkdirs();
			if (isMk) {
				if (log.isDebugEnabled())
					log.debug("生成" + path + "成功!!!");
			} else {
				log.error("生成" + path + "失败!!!");
			}
		}
		return isMk;
	}

	/**
	 * 
	 * @param content
	 *            文件流
	 * @param filePath
	 *            文件路径
	 * @param fileName
	 *            文件名称
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void save(byte[] content, String filePath, String fileName) throws IOException, IllegalArgumentException {
		if (StringUtils.isNull(filePath) || StringUtils.isNull(fileName)) {
			log.error("保存文件路径及文件不能为空; \n fileName === " + fileName + "\n  filePath===" + filePath);
			throw new IllegalArgumentException("保存文件路径及文件不能为空");
		}
		if (content == null) {
			log.error("文件流不能为空");
			throw new IOException("文件流不能为空");
		}
		InputStream is = new ByteArrayInputStream(content);
		this.save(is, filePath, fileName);
	}

	/**
	 * 
	 * @param streamIn
	 *            输入流
	 * @param filePath
	 *            文件路径
	 * @param fileName
	 *            文件名
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void save(InputStream streamIn, String filePath, String fileName) throws IOException, IllegalArgumentException {
		if (StringUtils.isNull(filePath) || StringUtils.isNull(fileName)) {
			log.error("保存文件路径及文件不能为空; \n fileName === " + fileName + "\n  filePath===" + filePath);
			throw new IllegalArgumentException("保存文件路径及文件不能为空");
		}
		if (streamIn == null) {
			log.error("文件流不能为空");
			throw new IOException("文件流不能为空");
		}
		try {
			this.mkDirs(filePath);
			OutputStream streamOut = new FileOutputStream(filePath + fileName);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = streamIn.read(buffer, 0, 8192)) != -1) {
				streamOut.write(buffer, 0, bytesRead);
			}
			streamOut.close();
			streamIn.close();
		} catch (IOException ex) {
			throw new IOException("保存文件失败", ex);
		}
	}

	/**
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws IOException
	 */
	public FileInputStream load(String filePath, String fileName) throws IOException {
		FileInputStream fileStream = null;
		if (StringUtils.isNull(filePath) || StringUtils.isNull(fileName)) {
			log.error("读取文件路径及文件不能为空; \n fileName === " + fileName + "\n  filePath===" + filePath);
			throw new IllegalArgumentException("读取文件路径及文件不能为空");
		}
		try {
			fileStream = new FileInputStream(filePath + fileName);
		} catch (IOException ex) {
			throw new IOException("读取文件失败", ex);
		}
		return fileStream;
	}

	/**
	 * 移动文件到所指定的目录
	 * 
	 * @param srcFile
	 * @param desPath
	 * @throws IOException
	 */
	public void moveFile(String srcFilePath, String srcFileName, String desPath) throws IOException {
		FileInputStream fileStream = null;
		try {
			fileStream = this.load(srcFilePath, srcFileName);
			this.save(fileStream, desPath, srcFileName);
		} catch (IOException e) {
			throw new IOException("读取文件失败", e);
		}

	}

	public static void main(String[] args) {
		FileUtil aa = new FileUtil();
		try {
			aa.moveFile("D:/cfpUploadPic/formal", "/nihao.jpg", "D:/cfpUploadPic/delete");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 文件删除
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileName
	 *            文件名
	 */
	public void deleteFile(String filePath, String fileName) {
		File file = new File(filePath + fileName);
		file.delete();
	}

	/**
	 * 获取路径下所有文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public static File[] getFileAllList(String filePath) {
		File file = new File(filePath);
		return file.listFiles();
	}

	/**
	 * 输入流转换为字节
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] inputStreamToByte(InputStream is) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}

	/**
	 * 文件转换为字节
	 * 
	 * @param filePath
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public byte[] getFileToBytes(String filePath, String fileName) throws IOException {
		FileInputStream fis = load(filePath, fileName);
		return FileUtil.inputStreamToByte(fis);
	}

	/**
	 * 下载文件
	 * @param file
	 * @param response
	 * @throws IOException
	 */
	public static void download(File file, HttpServletResponse response) throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(response.getOutputStream());

			long fileLength = file.length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename=" + new String(file.getName().getBytes("UTF8"), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			int bytesRead;
			byte[] bytes = new byte[2048];
			while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {
				bos.write(bytes, 0, bytesRead);
			}
			bos.flush();
		} catch (Exception e) {
			throw new IOException("读取下载失败", e);
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}

	}

	/**
	 * 
	 * @param bytes
	 *            自定义byte大小
	 * @param path
	 *            文件路径
	 * @param zipName
	 *            zip名称
	 * @param response
	 * @throws IOException
	 */
	public static void download(byte[] bytes, String path, String zipName, HttpServletResponse response) throws IOException {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String _zipName = (zipName.indexOf(".zip") != -1) ? zipName : zipName + ".zip";
		String file = path + "/" + _zipName;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(response.getOutputStream());

			long fileLength = new File(file).length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename=" + new String(_zipName.getBytes("utf-8"), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			int bytesRead;
			while (-1 != (bytesRead = bis.read(bytes, 0, bytes.length))) {
				bos.write(bytes, 0, bytesRead);
			}
			bos.flush();
		} catch (Exception e) {
			throw new IOException("读取下载失败", e);
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}

	}

	/**
	 * zip 压缩方法
	 * 
	 * @param files
	 *            文件
	 * @param zipPath
	 *            zip压缩至zipPath
	 * @param zipName
	 *            zip名称
	 * @throws IOException
	 */
	public static void zipFiles(File[] files, String zipPath, String zipName) throws IOException {
		new FileUtil().mkDirs(zipPath);
		// zip完整路径及文件名称
		String fullName = zipPath + "/" + ((zipName.indexOf(".zip") != -1) ? zipName : zipName + ".zip");
		File zipFile = new File(fullName);
		InputStream input = null;
		ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
		// zip的名称为
		zipOut.setComment(zipName);
		for (int i = 0; i < files.length; ++i) {
			if (files[i].exists()) {
				input = new FileInputStream(files[i]);
				zipOut.putNextEntry(new ZipEntry(files[i].getName()));
				int temp = 0;
				while ((temp = input.read()) != -1) {
					zipOut.write(temp);
				}
				input.close();
			}
		}
		zipOut.flush();
		zipOut.close();
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 *            文件路径（包括文件名及后缀）
	 * @return
	 */
	public static boolean deleteFile(String filePath) {
		boolean flag = false;
		File file = new File(filePath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * @param   sPath 被删除目录的文件路径
	 * @return  目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		boolean flag = true;
		//如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		//如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		//删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			//删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) break;
			} //删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) break;
			}
		}
		if (!flag) return false;
		//删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

}
