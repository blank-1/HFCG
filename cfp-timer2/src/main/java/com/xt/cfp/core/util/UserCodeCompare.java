package com.xt.cfp.core.util;

import java.util.Comparator;

class UserCodeCompare implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		if(StringUtils.isNull(o1)){
			return -1;
		}
		if(StringUtils.isNull(o2)){
			return -1;
		}	
		String[] rs1=o1.split("_");
		String[] rs2=o2.split("_");
		if(rs1[2].compareTo(rs2[2])>0){
			return -1;
		}else{
			return 1;
		}
	}
	
}

	
