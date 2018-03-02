package com.xt.cfp.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.constants.WechatNoticeConstants.WechatNoticeStateEnum;
import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.WechatNotice;
import com.xt.cfp.core.service.AdminInfoService;
import com.xt.cfp.core.service.WechatNoticeService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;
import com.xt.cfp.core.util.TimeInterval;
import com.xt.cfp.core.util.Uploader;

@Controller
@RequestMapping(value = "/jsp/notice")
public class WechatNoticeController extends BaseController{

	@Autowired
	private WechatNoticeService wechatNoticeService;
	
	@Autowired
	private AdminInfoService adminInfoService; 
	
	/**
	 * 跳转至：微信公告列表页
	 */
    @RequestMapping(value = "to_list")
    public String to_list(){
        return "jsp/notice/list";
    }
    
    /**
     * 执行：微信公告列表
     * @param wechatNotice
     * @param startDate
     * @param endDate
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public Object list(@ModelAttribute WechatNotice wechatNotice,
                       @RequestParam(value = "startDateStr", defaultValue = "", required = false) String startDate,
                       @RequestParam(value = "endDateStr" ,defaultValue = "", required = false) String endDate,
                       @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                       @RequestParam(value = "page", defaultValue = "1") int pageNo){
        //封装参数
        TimeInterval timeInterval = new TimeInterval(// 日期间隔
                "".equals(startDate) ? null : DateUtil.parseStrToDate(
                        startDate, "yyyy-MM-dd"),
                "".equals(endDate) ? null : DateUtil.parseStrToDate(
                        endDate, "yyyy-MM-dd"));
        
        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("timeInterval", timeInterval);
        
        //验证非法字符
        if(null != wechatNotice){
        	if(!StringUtils.isNull(wechatNotice.getNoticeTitle())){
        		if(StringUtils.checkSQLStr(wechatNotice.getNoticeTitle())){
        			return new Pagination<WechatNotice>();
        		}
        	}
        }

        Pagination<WechatNotice> pagination = wechatNoticeService.getWechatNoticePaging(pageNo, pageSize, wechatNotice, customParams);
        
        //补充字段[开始]
        if(null != pagination && null != pagination.getRows()){
        	if(pagination.getRows().size() > 0){
        		AdminInfo adminInfo = null;
        		for (WechatNotice notice : pagination.getRows()) {
        			adminInfo = adminInfoService.getAdminById(notice.getAdminId());
        			if(null != adminInfo){
        				notice.setAdminName(adminInfo.getLoginName());
        			}
				}
        	}
        }
        //补充字段[结束]
        
        return pagination;
    }
    
    /**
     * 跳转至：详情页面
     * @param request
     * @param noticeId
     * @return
     */
    @RequestMapping(value = "detail")
    public String detail(HttpServletRequest request, Long noticeId) {
    	WechatNotice wechatNotice = wechatNoticeService.getWechatNoticeById(noticeId);
        request.setAttribute("wechatNotice", wechatNotice);
        return "jsp/notice/detail";
    }
    
    /**
     * 执行：停用
     * @param request
     * @param noticeId
     * @return
     */
    @RequestMapping(value = "stopUse")
    @ResponseBody
    public String stopUse(HttpServletRequest request, Long noticeId){
        //修改状态为无效
    	wechatNoticeService.changeWechatNoticeState(noticeId, WechatNoticeStateEnum.UN_VALID);
        return "success";
    }

    /**
     * 执行：启用
     * @param request
     * @param noticeId
     * @return
     */
    @RequestMapping(value = "startUse") 
    @ResponseBody
    public String startUse(HttpServletRequest request, Long noticeId){
    	//修改状态为有效
    	wechatNoticeService.changeWechatNoticeState(noticeId, WechatNoticeStateEnum.VALID);
        return "success";
    }
    
    /**
	 * 跳转至：微信公告添加页
	 */
    @RequestMapping(value = "to_add")
    public String to_add(){
        return "jsp/notice/add";
    }
    
    /**
     * 执行：新增微信公告
     * @param wechatNotice
     * @return
     */
    @RequestMapping(value = "add")
    @ResponseBody
    public String add(WechatNotice wechatNotice){
        AdminInfo currentUser = getCurrentUser();
        if (currentUser==null)
            return "session已过期";
        try {
        	
        	System.out.println("---------------------");
        	System.out.println(wechatNotice.getNoticeTitle().getBytes().length);
        	System.out.println(wechatNotice.getNoticeSynopsis().getBytes().length);
        	System.out.println(wechatNotice.getNoticeContent().getBytes().length);
        	
        	//验证非法字符
            if(null != wechatNotice){
            	if(StringUtils.isNull(wechatNotice.getNoticeTitle())){
            		return "公告标题不能为空！";
            	}else{
            		if(StringUtils.checkSQLStrForSimple(wechatNotice.getNoticeTitle())){
            			return "公告标题包含非法字符！";
            		}else if (StringUtils.checkStrForXSS(wechatNotice.getNoticeTitle())) {
            			return "公告标题包含非法字符！";
					}
            	}
            	if(StringUtils.isNull(wechatNotice.getNoticeSynopsis())){
            		return "公告简介不能为空！";
            	}else{
            		if(StringUtils.checkSQLStrForSimple(wechatNotice.getNoticeSynopsis())){
            			return "公告简介包含非法字符！";
            		}else if (StringUtils.checkStrForXSS(wechatNotice.getNoticeSynopsis())) {
            			return "公告简介包含非法字符！";
					}
            	}
            	if(StringUtils.isNull(wechatNotice.getNoticeContent())){
            		return "公告内容不能为空！";
            	}else{
            		if(StringUtils.checkSQLStrForSimple(wechatNotice.getNoticeContent())){
            			return "公告内容包含非法字符！";
            		}else if (wechatNotice.getNoticeContent().getBytes().length > 2500) {
            			return "公告内容长度过长，请精简！";
					}
            	}
            }else{
            	return "缺少必要参数！";
            }
            
        	wechatNotice.setPublishTime(new Date());
        	wechatNotice.setAdminId(currentUser.getAdminId());
        	wechatNotice.setNoticeState(WechatNoticeStateEnum.VALID.getValue());
        	
        	wechatNoticeService.addWechatNotice(wechatNotice);

            return "success";
        }catch (SystemException se){
            logger.error(se.getDetailDesc(),se);
            se.printStackTrace();
            return se.getMessage();
        }
    }
    
    /**
     * umeditor上传图片辅助
     * @return
     */
    @RequestMapping(value = "imageUp")
    @ResponseBody
    public String imageUp(HttpServletRequest request, HttpServletResponse response){
    	String result = "";
    	try {
    		request.setCharacterEncoding("utf-8");
        	response.setCharacterEncoding("utf-8");
            Uploader up = new Uploader(request);
            up.setSavePath("upload");
            String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"};
            up.setAllowFiles(fileType);
            up.setMaxSize(10000); //单位KB
            up.upload();

            String callback = request.getParameter("callback");

            result = "{\"name\":\""+ up.getFileName() +"\", \"originalName\": \""+ up.getOriginalName() +"\", \"size\": "+ up.getSize() +", \"state\": \""+ up.getState() +"\", \"type\": \""+ up.getType() +"\", \"url\": \""+ up.getHttpBasePath() + up.getUrl() +"\"}";

            result = result.replaceAll( "\\\\", "\\\\" );
            
            if( callback != null ){
                result = "<script>"+ callback +"(" + result + ")</script>"; 
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    }
    
}
