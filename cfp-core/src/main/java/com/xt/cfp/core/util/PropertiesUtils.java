package com.xt.cfp.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;

/**
 * 读取properties文件
 * 
 * @author chenbo
 * @创建时间 2015年7月20日 下午1:44:40
 *
 */
public class PropertiesUtils {

	private static Logger logger = Logger.getLogger(PropertiesUtils.class);

	private static Properties pros = new Properties();
	private static PropertiesUtils instance;

	private PropertiesUtils() {
		load("/config.properties");
		load("/email.properties");
		load("/llpayConfig.properties");
	}

	public static PropertiesUtils getInstance() {
		if (null == instance) {
			instance = new PropertiesUtils();
		}
		return instance;
	}

	private static void load(String fileName) {
		InputStream in = PropertiesUtils.class.getResourceAsStream(fileName);
		try {
			pros.load(in);
		} catch (IOException e) {
			logger.error("加载config.properties文件异常", e);
		} finally {
			try {
				if (null != in)
					in.close();
			} catch (IOException e) {
				throw new SystemException(SystemErrorCode.STREAM_CAN_NOT_CLOSE);
			}
		}

	}

	public String get(String key) {
		return pros.getProperty(key);
	}

	public static void main(String[] args) {
		System.out.println(PropertiesUtils.getInstance().get("FRONT_PATH"));
	}
}