package com.external.yongyou.util;

/*
 --------------------------------------------**********--------------------------------------------

 该算法于1977年由美国麻省理工学院MIT(Massachusetts Institute of Technology)的Ronal Rivest，Adi Shamir和Len Adleman三位年轻教授提出，并以三人的姓氏Rivest，Shamir和Adlernan命名为RSA算法，是一个支持变长密钥的公共密钥算法，需要加密的文件快的长度也是可变的!

 所谓RSA加密算法，是世界上第一个非对称加密算法，也是数论的第一个实际应用。它的算法如下：

 1.找两个非常大的质数p和q（通常p和q都有155十进制位或都有512十进制位）并计算n=pq，k=(p-1)(q-1)。

 2.将明文编码成整数M，保证M不小于0但是小于n。

 3.任取一个整数e，保证e和k互质，而且e不小于0但是小于k。加密钥匙（称作公钥）是(e, n)。

 4.找到一个整数d，使得ed除以k的余数是1（只要e和n满足上面条件，d肯定存在）。解密钥匙（称作密钥）是(d, n)。

 加密过程： 加密后的编码C等于M的e次方除以n所得的余数。

 解密过程： 解密后的编码N等于C的d次方除以n所得的余数。

 只要e、d和n满足上面给定的条件。M等于N。

 --------------------------------------------**********--------------------------------------------
 */
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RSA {
	/** 指定key的大小 */
	private static int KEYSIZE = 1024;

	/**
	 * 生成密钥对
	 */
	public static Map<String, String> generateKeyPair() throws Exception {
		/** RSA算法要求有一个可信任的随机数源 */
		SecureRandom sr = new SecureRandom();
		/** 为RSA算法创建一个KeyPairGenerator对象 */
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		/** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
		kpg.initialize(KEYSIZE, sr);
		/** 生成密匙对 */
		KeyPair kp = kpg.generateKeyPair();
		/** 得到公钥 */
		Key publicKey = kp.getPublic();
		System.out.println("PublicKey===:" + kp.getPublic().getAlgorithm());
		byte[] publicKeyBytes = publicKey.getEncoded();
		String pub = new String(Base64.encodeBase64(publicKeyBytes),
				ConfigureEncryptAndDecrypt.CHAR_ENCODING);
		System.out.println("PublicKeyStr===:" + pub);
		/** 得到私钥 */
		Key privateKey = kp.getPrivate();
		System.out.println("PrivateKey===" + kp.getPrivate().getAlgorithm());
		byte[] privateKeyBytes = privateKey.getEncoded();
		String pri = new String(Base64.encodeBase64(privateKeyBytes),
				ConfigureEncryptAndDecrypt.CHAR_ENCODING);
		System.out.println("PrivateKey===" + pri);

		Map<String, String> map = new HashMap<String, String>();
		map.put("publicKey", pub);
		map.put("privateKey", pri);
		RSAPublicKey rsp = (RSAPublicKey) kp.getPublic();
		BigInteger bint = rsp.getModulus();
		byte[] b = bint.toByteArray();
		byte[] deBase64Value = Base64.encodeBase64(b);
		String retValue = new String(deBase64Value);
		map.put("modulus", retValue);
		return map;
	}

	/**
	 * 加密方法 source： 源数据
	 */
	public static String encrypt(String source, String publicKey)
			throws Exception {
		Key key = getPublicKey(publicKey);
		/** 得到Cipher对象来实现对源数据的RSA加密 */
		Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] b = source.getBytes();
		/** 执行加密操作 */
		byte[] b1 = cipher.doFinal(b);
		return new String(Base64.encodeBase64(b1),
				ConfigureEncryptAndDecrypt.CHAR_ENCODING);
	}

	/**
	 * 解密算法 cryptograph:密文
	 */
	public static String decrypt(String cryptograph, String privateKey)
			throws Exception {
		Key key = getPrivateKey(privateKey);
		/** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
		Cipher cipher = Cipher.getInstance(ConfigureEncryptAndDecrypt.RSA_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] b1 = Base64.decodeBase64(cryptograph.getBytes());
		/** 执行解密操作 */
		byte[] b = cipher.doFinal(b1);
		return new String(b);
	}

	/**
	 * 得到公钥
	 *
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String key) throws Exception {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
				Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 得到私钥
	 *
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
				Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	public static String sign(String content, String privateKey) {
		String charset = ConfigureEncryptAndDecrypt.CHAR_ENCODING;
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decodeBase64(privateKey.getBytes()));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			Signature signature = Signature.getInstance("SHA1WithRSA");

			signature.initSign(priKey);
			signature.update(content.getBytes(charset));

			byte[] signed = signature.sign();

			return new String(Base64.encodeBase64(signed));
		} catch (Exception e) {

		}

		return null;
	}

	public static boolean checkSign(String content, String sign, String publicKey)
	{
		try
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode2(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


			java.security.Signature signature = java.security.Signature
					.getInstance("SHA1WithRSA");

			signature.initVerify(pubKey);
			signature.update( content.getBytes("utf-8") );

			boolean bverify = signature.verify( Base64.decode2(sign) );
			return bverify;

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	public static void main(String[] args) throws Exception{
		generateKeyPair();
		String str1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCV8XzHkTJum1WDwMO0suQw2sHXQ7Cb2MeNaZUSQaTKI/hWZSJVl7Pj/aRdc9CrOvmEkVGMd0NL6WJLab9UZiaEfiXtMuboWx31dHXrBubby3xiSBQ8UJSM1ROCF3OS9wwtzIh5a7CxrBFsbs0PkIl3QnRm3POqtTxCHDBWIMn8QwIDAQAB";
		String str2 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJXxfMeRMm6bVYPAw7Sy5DDawddDsJvYx41plRJBpMoj+FZlIlWXs+P9pF1z0Ks6+YSRUYx3Q0vpYktpv1RmJoR+Je0y5uhbHfV0desG5tvLfGJIFDxQlIzVE4IXc5L3DC3MiHlrsLGsEWxuzQ+QiXdCdGbc86q1PEIcMFYgyfxDAgMBAAECgYBoZmbKEUe7CX6mKM5a/3+woG/88Q2DxZFT3eLEfl49vIPJq8sXK3Q+VzldNCljP8HB0uij09pQfaRJ+8wS+LjxyW+Zs6KW48gbuJwC0COhkomLPrAvM0O5UYFKo/HgGhJqNgxxxFm4eWlW07TW7uIMLbqdp204I8uxN9HwrVuysQJBAPYIuzwZrnbbSz+AbEMAeRSpaLJKVSLL0F2Hct535SXzaY9/NYoEltlm6jYv/3zSXqTl6HxD71OeB8e3sNSvtesCQQCcBFXj/pwnD7LkoLG+laaJAToRVDZjFOckhJ0nfNCVaB2t+MzZwlbNEW61Yv/AuSvtVcn8hEQynGj54GSJbQUJAkEAp6Trb9hP+M0USQDIxB7eYiXNIS9lM4CNSi2xJ8WqbmGd3Blh8dmOsDRgAuxRukveEZ5W+9WOjftwcbXI2COiYQJAJeTvr7bx2pL3koxtX6sdjf1cS0U3CI3VLot/WE36o69sNgtORj3rs1demnP3RqozPywtKUY/AkP3Q2n7QV3XcQJBAL2oWOulAqUlu4r55UHCcfLXgx9H1TLSisWUt/H100X4WB0gw+na+eHLbPVkjVT3YDQvOwkej++26TITpFhNKaI=";
		String str3="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCufgkUGczFrpXnS/zSmoTgtQjphBUQSpwv9jPjgWVBWou24DKSmwMAlw9MN6Z+N0SRwpxqSea9perh47F3FmZdX8m198UpYe3aMbh+0EMsOx5Ut5Gs1nIuXmYuMJzoQhG82AyodtKxbzXC/Ln0vUCJCN/IiRrI59rvP2g01xxu0QIDAQAB";
		String str4="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK5+CRQZzMWuledL/NKahOC1COmEFRBKnC/2M+OBZUFai7bgMpKbAwCXD0w3pn43RJHCnGpJ5r2l6uHjsXcWZl1fybX3xSlh7doxuH7QQyw7HlS3kazWci5eZi4wnOhCEbzYDKh20rFvNcL8ufS9QIkI38iJGsjn2u8/aDTXHG7RAgMBAAECgYEAhQztdzpE/eclvnweh6fy8jSYWxavyQBbH+cnoa4kuzSDnCpP5aH1JBoLPB6jAVK7Ijm2cwVuv+UnuJ6ZzyhxXDaF4NF2caZWQrjpDbOddpIiXw2pLzip+saq0aKKdAO5Nbzdb/QcseLjZOEvDrD8W9gg7gzp9f325/5OH9VoiVECQQDiDNImstrY/r2IW4xm96fVAgk+VHX8d7/W02N+3dhxWBvNH7+Ji0ZilrKqAHWD6G+l2PcgvV0/SrsDP/Z3LjIfAkEAxZx5zBNLf4dmMHGrwNa+3PTNObbeHSnkk+FvvwY8b0LqGspBLPvQm3C71XXlb20OxQ3LX60t0MiQ6Wbayd6hDwJAL4z24AEbnC3p/LcVT8uk2F/3Rc9KinM+PZh2A4fzMdSQ9JNLHUnY4oBE0RILplXMtpxvT8nFxbyYnlRkKinjjQJAdqXFx08KdI6iP5Hwcr89J8Nf+Tgf0ljV4ihoBMTEwnTlxKINUen7zWh5jufTkvxi8tOeeVqseIAD6e+Ui8I85wJAR3z/oCYXW0gK/7VwqNMUx1aoESkg6cVOZtdpvc1YEcEp/acpWyHLPq6ybdBhXIKkbGO3FQSHIQmjRHqxk0Qfyg==";
		System.out.println(str1.equals(str3));
		System.out.println(str2.equals(str4));
	}
}