package com.xt.cfp.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterUtil {
	
	private static String[] filters_sql = new String[]{
		"select",
		"from",
		"delete",
		"where",
		"or",
		"update",
		"insert",
		"into",
		"and",
   };
	
	public static boolean filterSql(String word){
		if(null != word && !"".equals(word)){
	    	String[] works = word.split(" ");
	        for (String f : filters_sql) {
	    	   for (String w : works) {
	    		  if(f.equalsIgnoreCase(w)){
	    			  return true;
	    		  }
			   }
			}
	    }
		return false;
	}
	
    /**
	 * 过滤特殊字符
	 * @param str 文本字符串
	 * @return
	 */
    public static String StrFilter(String str){
        String regEx="[`~!@#$%^&*()+=|{}';',//[//]<>/~！@#￥%……&*（）+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return  m.replaceAll("").trim();
    }
    
    /**
     * 判断是否有特殊字符
     * @param str
     * @return true 有，false 没有
     */
    public static boolean isSpecialCharacters(String str){
        str = str.trim();
        return !str.equals(StrFilter(str));
    }
    
}
