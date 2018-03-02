package com.xt.cfp.core.util;

import jodd.io.FileNameUtil;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ThumbnailGenerate {
	private String srcFile;
	private String destFile;
	private int width;
	private int height;
	private Image img;
	private String attachmentName;

	public final static String THUMB_IMAGE_NAME = "_100";
	
	/**
	 * 构造函数
	 * 
	 * @param fileName
	 *            String
	 * @throws java.io.IOException
	 */
	public ThumbnailGenerate(String fileName) throws IOException {
		this(fileName,null);
	}

	public ThumbnailGenerate(String x, String fileName) throws IOException {
		this.attachmentName = fileName;

		File _file = new File(x); // 读入文件
		this.srcFile = _file.getName();
		StringBuffer buff = new StringBuffer(FileNameUtil.getFullPath(x));
		buff.append(StringUtils.isEmpty(attachmentName) ? FileNameUtil.getBaseName(x) : FileNameUtil.getBaseName(attachmentName));
		buff.append(THUMB_IMAGE_NAME);
		buff.append(".");
		buff.append(FileNameUtil.getExtension(x));
		this.destFile = buff.toString();
		// this.srcFile.substring(0,
		// this.srcFile.lastIndexOf(".")) +"_s.jpg";
		img = javax.imageio.ImageIO.read(_file); // 构造Image对象
		width = img.getWidth(null); // 得到源图宽
		height = img.getHeight(null); // 得到源图长
	}

	/**
	 * 强制压缩/放大图片到固定的大小
	 *
	 * @param w
	 *            int 新宽度
	 * @param h
	 *            int 新高度
	 * @throws java.io.IOException
	 */
	public void resize(int w, int h) throws IOException {
		BufferedImage _image = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		_image.getGraphics().drawImage(img, 0, 0, w, h, null);
		ImageIO.write(_image, destFile, new File(destFile));
	}

	/**
	 * 按照固定的比例缩放图片
	 *
	 * @param t
	 *            double 比例
	 * @throws java.io.IOException
	 */
	public void resize(double t) throws IOException {
		int w = (int) (width * t);
		int h = (int) (height * t);
		resize(w, h);
	}

	/**
	 * 以宽度为基准，等比例放缩图片
	 *
	 * @param w
	 *            int 新宽度
	 * @throws java.io.IOException
	 */
	public void resizeByWidth(int w) throws IOException {
		int h = (int) (height * w / width);
		resize(w, h);
	}

	/**
	 * 以高度为基准，等比例缩放图片
	 *
	 * @param h
	 *            int 新高度
	 * @throws java.io.IOException
	 */
	public void resizeByHeight(int h) throws IOException {
		int w = (int) (width * h / height);
		resize(w, h);
	}

	/**
	 * 按照最大高度限制，生成最大的等比例缩略图
	 *
	 * @param w
	 *            int 最大宽度
	 * @param h
	 *            int 最大高度
	 * @throws java.io.IOException
	 */
	public void resizeFix(int w, int h) throws IOException {
		if (width / height > w / h) {
			resizeByWidth(w);
		} else {
			resizeByHeight(h);
		}
	}

	/**
	 * 设置目标文件名 setDestFile
	 * 
	 * @param fileName
	 *            String 文件名字符串
	 */
	public void setDestFile(String fileName) throws Exception {
		if (!fileName.endsWith(".jpg")) {
			throw new Exception("Dest File Must end with \".jpg\".");
		}
		destFile = fileName;
	}

	/**
	 * 获取目标文件名 getDestFile
	 */
	public String getDestFile() {
		return destFile;
	}

	/**
	 * 获取图片原始宽度 getSrcWidth
	 */
	public int getSrcWidth() {
		return width;
	}

	/**
	 * 获取图片原始高度 getSrcHeight
	 */
	public int getSrcHeight() {
		return height;
	}

	/*
	 * 调用测试
	 */
	public static void main(String[] args) throws Exception {
		ThumbnailGenerate generate = new ThumbnailGenerate("C:/Users/Administrator/Desktop/ccc.jpg");
		generate.resizeFix(100,100);
	}
}
