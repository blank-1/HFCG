package com.external.deposites.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 1.商户的私钥初始化 2.商户验签的公钥初始化 3.根据商户号加密数据 4.验签富友返回数据
 *
 * @author 2441240397
 */

public class SecurityUtils {
    private static Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
    /**
     * 私钥 ,富友分配给商户的
     */
    public static PrivateKey privateKey;
    /**
     * 公钥，富友的公钥
     */
    public static PublicKey publicKey;
    /**
     * 私钥文件路径 如：D:/rsa/prkey.key
     */
    private static String privateKeyPath = PropertiesUtils.property("hf-config","cg.hf.key.private");

    /**
     * 公钥文件路径 如：D:/rsa/pbkey.key
     */
    private static String publicKeyPath = PropertiesUtils.property("hf-config","cg.hf.key.public");

    static {
        try {
            java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("密钥初始化失败");
        }
    }

    /**
     * init:初始化私钥
     */
    public static void initPrivateKey() {
        try {
            if (privateKey == null) {
                privateKey = getPrivateKey(privateKeyPath);
            }
        } catch (Exception e) {
            System.out.println("SecurityUtils初始化失败" + e.getMessage());
            e.printStackTrace();
            System.out.println("密钥初始化失败");
        }
    }

    /**
     * 初始化公钥
     */
    public static void initPublicKey() {
        try {
            if (publicKey == null) {
                publicKey = getPublicKey(publicKeyPath);
            }
        } catch (Exception e) {
            System.out.println("SecurityUtils初始化失败" + e.getMessage());
            e.printStackTrace();
            System.out.println("密钥初始化失败");
        }
    }

    /**
     * 对传入字符串进行签名
     *
     * @param inputStr
     * @return @
     */
    public static String sign(String inputStr) {
        String result = null;
        try {
            if (privateKey == null) {
                // 初始化
                initPrivateKey();
            }
            byte[] tByte;
            Signature signature = Signature.getInstance("SHA1withRSA", "BC");
            signature.initSign(privateKey);
            signature.update(inputStr.getBytes("UTF-8"));
            tByte = signature.sign();
            result = Base64.encode(tByte);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("密钥初始化失败");
        }
        return result;
    }

    /**
     * 对富友返回的数据进行验签
     *
     * @param src       返回数据明文
     * @param signValue 返回数据签名
     * @return
     */
    public static boolean verifySign(String src, String signValue) {
        boolean bool = false;
        try {
            if (publicKey == null) {
                initPublicKey();
            }
            Signature signature = Signature.getInstance("SHA1withRSA", "BC");
            signature.initVerify(publicKey);
            signature.update(src.getBytes("UTF-8"));
            bool = signature.verify(Base64.decode(signValue));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("密钥初始化失败");
        }
        return bool;
    }

    private static PrivateKey getPrivateKey(String filePath) {
        String base64edKey = readFile(filePath);
        KeyFactory kf;
        PrivateKey privateKey = null;
        try {
            kf = KeyFactory.getInstance("RSA", "BC");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(base64edKey));
            privateKey = kf.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("密钥初始化失败");
        }
        return privateKey;
    }

    private static PublicKey getPublicKey(String filePath) {
        String base64edKey = readFile(filePath);
        KeyFactory kf;
        PublicKey publickey = null;
        try {
            kf = KeyFactory.getInstance("RSA", "BC");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(base64edKey));
            publickey = kf.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("密钥初始化失败");
        }
        return publickey;
    }

    private static String readFile(String fileName) {
        try {
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            InputStreamReader isr = new InputStreamReader(resourceAsStream);
            StringBuilder results = new StringBuilder();
            int len;
            while ((len = isr.read()) >= 0) {
                results.append((char) len);
            }
            return results.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
