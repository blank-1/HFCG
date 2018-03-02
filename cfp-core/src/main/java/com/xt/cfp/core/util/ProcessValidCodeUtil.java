package com.xt.cfp.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.xt.cfp.core.constants.ValidCodeEnum;
import com.xt.cfp.core.pojo.ext.UserValidCode;
import com.xt.cfp.core.service.redis.RedisCacheManger;

public class ProcessValidCodeUtil {
	
	private static final String CACHE_KEY="VC";
	
	
	
	public static void saveValidCode(ValidCodeEnum codeEnum,RedisCacheManger redisCacheManger,String code,String mobile){
		Date d=new Date();
		try {
			//key VC_mobile_type	value  mobile_type_beginTime_endTime_code
			redisCacheManger.setRedisCacheInfo(CACHE_KEY+"_"+mobile+"_"+codeEnum.getValue(), mobile+"_"+codeEnum.getValue()+"_"+DateUtil.getPlusTime(d)+"_"+DateUtil.getPlusTime(DateUtil.addSecond(d, RedisCacheManger.getSmsCodeTime()))+"_"+code,3600);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static List<String> getAllValues(RedisCacheManger redisCacheManger,String phone,String type){
		List<String> results=new ArrayList<String>();
		Set<String> sets=getKeysByCondition(redisCacheManger, phone, type);
		if(sets!=null&&sets.size()>0){
			for (String key : sets) {
				results.add(redisCacheManger.getRedisCacheInfo(key).toString());
			}
		}
		Collections.sort(results,new UserCodeCompare());
		return results;
	}
	
	private static Set<String> getKeysByCondition(RedisCacheManger redisCacheManger,String phone,String type){
		StringBuffer sb=new StringBuffer(CACHE_KEY);
		if(!StringUtils.isNull(phone)&&StringUtils.isNull(type)){
			sb.append("_"+phone+"_*");
		}else if(StringUtils.isNull(phone)&&!StringUtils.isNull(type)){
			sb.append("_"+"*_"+type);
		}else{
			sb.append("_"+phone+"_"+type);
		}
		Set<String> sets=redisCacheManger.getAllRedisCacheInfoBySort(sb.toString());
		return sets;
	}
	
	public static void deleteKeysByCondition(RedisCacheManger redisCacheManger,String phone,String type){
		Set<String> sets=getKeysByCondition(redisCacheManger, phone, type);
		if(sets!=null&&sets.size()>0){
			for (String key : sets) {
				redisCacheManger.destroyRedisCacheInfo(key);
			}
		}
		
	}
	
	public static List<String> getValusByCondition(String phone,String type,RedisCacheManger redisCacheManger){
		if(StringUtils.isNull(phone)&&StringUtils.isNull(type)){
			return null;
		}
		List<String> all=getAllValues(redisCacheManger,phone,type);
		return all;
	}
	
	public static List<String> getValusByConditionAndPaging(String phone,String type,Integer pageNo,Integer pageSize,RedisCacheManger redisCacheManger){
		List<String> results=getValusByCondition(phone, type, redisCacheManger);
		if(results==null||results.size()==0){
			return null;
		}
		if(pageNo==null){
			pageNo=1;
		}
		if(pageSize==null){
			pageSize=20;
		}
		int start = pageSize *( pageNo-1)>results.size()?results.size():pageSize * ( pageNo-1);
        int end = start + pageSize>results.size()?results.size():start + pageSize; 
        List<String> list=results.subList(start, end);
        return list;
	}
	
	public static int getValusCountByConditionAndPaging(String phone,String type,RedisCacheManger redisCacheManger){
		List<String> results=getValusByCondition(phone, type, redisCacheManger);
		return results==null?0:results.size();
	}
	
	public static List<UserValidCode> changeToUserValidCode(List<String> results){
		List<UserValidCode> list=new ArrayList<UserValidCode>();
		if(results!=null&&results.size()>0){
			for (String s : results) {
				UserValidCode uv=new UserValidCode();
				String[] rs=s.split("_");
				uv.setMobile(rs[0]);
				uv.setType(ValidCodeEnum.getValidCodeEnumByType(Integer.valueOf(rs[1])).getDesc());
				uv.setStartTime(rs[2]);
				uv.setEndTime(rs[3]);
				uv.setCode(rs[4]);
				list.add(uv);
			}
		}
		return list;
	}
	
}
