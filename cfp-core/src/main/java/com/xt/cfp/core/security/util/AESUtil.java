package com.xt.cfp.core.security.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AESUtil {

	private static final Log logger = LogFactory.getLog(AESUtil.class);
	
	private static final String MD5 = "MD5";
	private static final String AES = "AES";
	public static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final String CAHRSET = "UTF-8";

	/**
	 * 生成AES秘钥
	 * @return
	 */
	public static String generateAesPasswrod() {
		
        KeyGenerator kgen = null;
		try {
			kgen = KeyGenerator.getInstance(AES);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
			throw new SecurityException("AES加密秘钥生成失败!");
		}  
		if (kgen == null) 
			throw new SecurityException("AES加密秘钥生成失败!");
        // set up password length
        kgen.init(128);  
        SecretKey skey = kgen.generateKey();  
        // encode the password
        return Base64.encodeBase64String(skey.getEncoded()); 
	}
	
	public static void main(String[] args) {
		String keyPwd = generateAesPasswrod();
		System.out.println(keyPwd);
	}
	
	
	/**
	 *  md5 签名
	 * @param data 原始数据
	 * @return
	 */
	private static String md5(String data) {
		try {
			return md5(data.getBytes(CAHRSET));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new SecurityException("加密运算失败");
		}
	}
	
	/**
	 * md5签名
	 * @param bytes 原始数据
	 * @return
	 */
	private static String md5(byte[] bytes) {
		try {
			MessageDigest md = MessageDigest.getInstance(MD5);
			md.update(bytes);
			byte[] result = md.digest();

			StringBuffer sb = new StringBuffer();

			for (byte b : result) {
				int i = b & 0xff;
				if (i < 0xf) {
					sb.append(0);
				}
				sb.append(Integer.toHexString(i));
			}
			return sb.toString();
		} catch(NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
			throw new SecurityException("加密运算失败");
		}
	}
	
	/**
	 * aes加密
	 * @param data 原始数据
	 * @param password  aes密钥
	 * @return
	 */
	private static byte[] aesEncrypt(byte[] data, String password) {
		try {
        	byte[] tt = Base64.decodeBase64(password);
            SecretKeySpec key = new SecretKeySpec(tt, AES);  
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
            byte[] result = cipher.doFinal(data);  
            return result; // 加密  
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
			throw new SecurityException("加密运算失败");
        }  
    }
	/**
	 * aes加密
	 * @param data 原始数据
	 * @param password  aes密钥
	 * @return
	 */
	private static byte[] aesEncrypt(byte[] data, byte[] password) {
		try {
			SecretKeySpec key = new SecretKeySpec(password, AES);  
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
			byte[] result = cipher.doFinal(data);  
			return result; // 加密  
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new SecurityException("加密运算失败");
		}  
	}
	
	/**
	 * aes加密
	 * @param data 原始数据
	 * @param password  aes密钥
	 * @return
	 */
	public static String aesEncrypt(String data, String password) {
		byte[] encryptData;
		try {
			encryptData = aesEncrypt(data.getBytes(CAHRSET), password);
			return Base64.encodeBase64String(encryptData);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new SecurityException("加密运算失败");
		}
		
	}
	
	public static String aesEncryptWithRawKey(String data, String rawKey) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(rawKey.getBytes(CAHRSET), AES);
			byte[] enCodeFormat = secretKey.getEncoded();
			byte[] encryptData = aesEncrypt(data.getBytes(CAHRSET), enCodeFormat);
			return Base64.encodeBase64String(encryptData);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new SecurityException("加密运算失败");
		} 
		
	}
	
	/**
	 * aes解密
	 * @param data 原始数据
	 * @param password  aes密钥
	 * @return
	 */
	private static byte[] aesDecrypt(byte[] data, String password) {
		try {
        	byte[] tt = Base64.decodeBase64(password);
            SecretKeySpec key = new SecretKeySpec(tt, AES);  
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化   
            byte[] result = cipher.doFinal(data);  
            return result; // 加密  
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
			throw new SecurityException("加密运算失败");
        }  
    }
	/**
	 * aes解密
	 * @param data 原始数据
	 * @param password  aes密钥
	 * @return
	 */
	private static byte[] aesDecrypt(byte[] data, byte[] password) {
		try {
			SecretKeySpec key = new SecretKeySpec(password, AES);  
			Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化   
			byte[] result = cipher.doFinal(data);  
			return result; // 加密  
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new SecurityException("加密运算失败");
		}  
	}
	
	/**
	 * aes解密
	 * @param data 原始数据
	 * @param password  aes密钥
	 * @return
	 */
	public static String aesDecrypt(String data, String password) {
		byte[] encryptData;
		byte[] decryptData;
		try {
			encryptData = Base64.decodeBase64(data);
			decryptData = aesDecrypt(encryptData, password);
			return new String(decryptData, CAHRSET);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new SecurityException("解密运算失败");
		}
		
	}
	
	public static String aesDecryptWithRawKey(String data, String rawKey) {
		byte[] encryptData;
		byte[] decryptData;
		try {
			SecretKeySpec secretKey = new SecretKeySpec(rawKey.getBytes(CAHRSET), AES);
			byte[] enCodeFormat = secretKey.getEncoded();
			encryptData = Base64.decodeBase64(data);
			decryptData = aesDecrypt(encryptData, enCodeFormat);
			return new String(decryptData, CAHRSET);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new SecurityException("解密运算失败");
		}
		
	}


	/**
	 * 数据签名  首先aes加密，然后md5签名
	 * @param data  原始数据
	 * @param salt  岩
	 * @return
	 */
	public static String sign(String data, String salt) {
		String signedData = null;
		if (salt == null) {
			signedData = md5(data);
		} else {
			signedData = md5(data + salt);
		}
		return signedData;
	}


    /**
	 * 验证加密字符是否正确
	 * @param data     原始数据
	 * @param salt     岩
	 * @param signMsg  签名字符串
	 * @return
	 */
	public static boolean verify(String data, String salt, String signMsg) {
		String signedData = sign(data, salt);
		if (signedData == null) return false;
		boolean rt = signedData.equals(signMsg);
		if (!rt) {
			logger.info("#verify error, data:" + data + ", encrypt:" + signedData + ", signMsg:" + signMsg);
		}
		return rt;
	}
}
