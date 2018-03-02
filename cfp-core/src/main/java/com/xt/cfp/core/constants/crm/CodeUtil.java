package com.xt.cfp.core.constants.crm;

public class CodeUtil {
	
	/**
	 * 
	 * @param pnum 同级节点数量
	 * @return
	 */
	public static String setCode(int pnum){
		pnum++;
		String code="";
		if(pnum<10){
			code+="00"+pnum;
		}else{
			code+="0"+pnum;
		}
		return code;
	}
	
}
