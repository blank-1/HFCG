package com.xt.cfp.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import com.xt.cfp.core.pojo.AdminInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;
import com.xt.cfp.core.constants.Constants;
import com.xt.cfp.core.pojo.UserInfo;


public class SecurityUtil {

    final static Logger log = Logger.getLogger(SecurityUtil.class);
    private static final String MD5 = "MD5";
    private static final String AES = "AES";
    private static final String MD5_SALT = "#$%_SFsd_)(#!~*&>?<?sfda|}";

    /**
     * md5 签名
     *
     * @param data 原始数据
     * @return
     */
    private static String md5(String data) {
        try {
            return md5(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            throw new SecurityException("加密运算失败");
        }
    }

    /**
     * md5签名
     *
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
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            throw new SecurityException("加密运算失败");
        }
    }

    /**
     * aes加密
     *
     * @param data     原始数据
     * @param password aes密钥
     * @return
     */
    private static byte[] aesEncrypt(byte[] data, String password) {
        try {
            byte[] tt = Base64.decodeBase64(password);
            SecretKeySpec key = new SecretKeySpec(tt, AES);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化   
            byte[] result = cipher.doFinal(data);
            return result; // 加密  
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SecurityException("加密运算失败");
        }
    }

    /**
     * aes加密
     *
     * @param data     原始数据
     * @param password aes密钥
     * @return
     */
    private static String aesEncrypt(String data, String password) {
        byte[] encryptData;
        try {
            encryptData = aesEncrypt(data.getBytes("UTF-8"), password);
            return (new Base64(true)).encodeAsString(encryptData);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            throw new SecurityException("加密运算失败");
        }

    }

    /**
     * 数据签名  首先aes加密，然后md5签名
     *
     * @param data     原始数据
     * @param passward 密钥
     * @return
     */
    public static String sign(String data, String passward) {
        String encryptData = aesEncrypt(data, passward);
//		log.info("#sign error, data:" + data + ", encrypt:" + encryptData);
        return md5(encryptData + MD5_SALT);
    }


    /**
     * 验证加密字符是否正确
     *
     * @param data     原始数据
     * @param passward 密钥
     * @param signMsg  签名字符串
     * @return
     */
    public static boolean verify(String data, String passward, String signMsg) {
        String encrypt = sign(data, passward);
        boolean rt = encrypt.equals(signMsg);
        if (!rt) {
            log.info("#verify error, data:" + data + ", encrypt:" + encrypt + ", signMsg:" + signMsg);
        }
        return rt;
    }

    /**
     * 检查request中是否存在session
     * @param request
     */
    public static void checkSession(HttpServletRequest request) {
        if (request.getSession(false) == null)
            throw new SystemException(SystemErrorCode.SESSION_NOT_EXISIT);
    }

    /**
     * 获取当前登录用户信息
     * @param assertExisit 断言用户是否存在
     * @return
     */
    public static UserInfo getCurrentUser(boolean assertExisit) {
        Object attribute = WebUtil.getHttpServletRequest().getSession().getAttribute(Constants.USER_ID_IN_SESSION);

        if (assertExisit && attribute == null)
            throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);

        return attribute != null ? (UserInfo) attribute : null;
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    public static Object getCurrentUser() {
        Object attribute = WebUtil.getHttpServletRequest().getSession().getAttribute(Constants.USER_ID_IN_SESSION);

        if ( attribute == null)
            throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);

        return attribute != null ? attribute : null;
    }

	/**
	 * 判断用户登录状态是否失效
	 *
	 * @return
	 */
	public static boolean getLoginStatus() {
		Object attribute = WebUtil.getHttpServletRequest().getSession().getAttribute(Constants.USER_ID_IN_SESSION);
		return attribute != null ? true : false;
	}

    /**
     * 获取请求的ip地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.contains(",")) {
            String[] split = ip.split(",");
            ip = split[0];
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }


    /**
     * 获取当前登录的后台用户
     * @param assertExisit
     * @return
     */
    public static AdminInfo getCurrentAdminUser(boolean assertExisit) {
        AdminInfo adminInfo = (AdminInfo) WebUtil.getHttpServletRequest().getSession().getAttribute(AdminInfo.LOGINUSER);
        if (assertExisit && adminInfo == null)
            throw new SystemException(SystemErrorCode.ILLEGAL_REQUEST);

        return adminInfo;
    }
}
