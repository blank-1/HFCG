package com.external.wxtl;

import java.net.URLDecoder;
import java.net.URLEncoder;
import com.sun.xml.internal.messaging.saaj.util.Base64;


public class CodingUtils {
	
	private static final Base64 base64 = new Base64();
	
	public static String encodeBase64URL(String arg) throws Exception{		
		String content = new String(Base64. encode(URLEncoder.encode(arg,"UTF-8").getBytes()));
		return content;
	}
	
	public static void main(String[] args)
	{
		String str="ydgx123";
		try {
			System.out.println(encodeBase64URL(str));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String decodeBase64URL(String arg)throws Exception{
		String content = URLDecoder.decode(new String(base64.decode(arg.getBytes())),"UTF-8");
		return content;
	}
	
	public static String encodeURL(String arg) throws Exception{		
		String content = URLEncoder.encode(arg,"UTF-8");
		return content;
	}
	
	public static String decodeURL(String arg)throws Exception{
		String content = URLDecoder.decode(arg,"UTF-8");
		return content;
	}
	
}
