package com.xt.cfp.core.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class ConfigUtil {
	
	private static Properties properties = new Properties();
	
	private final static Logger logger = Logger.getLogger(ConfigUtil.class);
	
	private static String FILENAME = "config.properties";
	
	static {
		InputStream file = null;
		try {
			file = ConfigUtil.class.getResourceAsStream("/" + FILENAME);
			properties.load(file);
		} catch (IOException e) {
			load();
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	private static void load() {
		String curDir = System.getProperty("user.dir");
		File file = new File(curDir, FILENAME);
		if (file.isFile()) {
			InputStream io = null;
			try {
				io = new FileInputStream(file);
				properties.load(io);
			} catch (IOException e) {
				logger.error("#config.properties is null; " + e.getMessage());
			} finally {
				if (file != null) {
					try {
						io.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}
	
	public static String getString(String key) {
		String value = properties.getProperty(key);
		return value;
	}
	
	public static String getString(String key, String defaultValue) {
		String value = properties.getProperty(key);
		if (value == null || value.equals("")) {
			value = defaultValue;
		}
		return value;
	}
	
	public static int getInt(String key, int defaultValue) {
		int v = 0;
		String value = getString(key, defaultValue+"");
		try {
			v = Integer.parseInt(value);
		} catch(Exception e){
			v = defaultValue;
		}
		return v;
	}
	
	public static boolean getBoolean(String key, boolean defaultValue) {
		boolean v = false;
		String value = getString(key, defaultValue+"");
		try {
			v = Boolean.parseBoolean(value);
		} catch(Exception e){
			v = defaultValue;
		}
		return v;
	}
	
	public static void main(String[] args) {
		String node = ConfigUtil.getString("node");
		System.out.println(node);
	}
}
