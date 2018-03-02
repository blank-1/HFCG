package com.xt.cfp.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.AppBanner;
import com.xt.cfp.core.pojo.AppStartPage;
import com.xt.cfp.core.service.AdminInfoService;
import com.xt.cfp.core.service.AppStartPageService;
import com.xt.cfp.core.util.PropertiesUtils;

@Controller
@RequestMapping("/jsp/app")
public class AppStartPageController extends BaseController {
	
	@Autowired
	private AppStartPageService appStartPageService;
	
	@Autowired
	private AdminInfoService adminInfoService;
	
    /**
     * 跳转到：APP启动页列表
     */
    @RequestMapping(value = "/to_start_page_list")
    public ModelAndView to_start_page_list() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/app/start_page_list");
        return mv;
    }
    
    /**
     * 执行：分页列表
     * @param pageSize 页数
     * @param pageNo 页码
     */
	@RequestMapping(value = "/start_page_list")
    @ResponseBody
    public Object start_page_list(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo) throws Exception {
		
		// 填充参数
        Map params = new HashMap();
        return appStartPageService.findAllByPage(pageNo, pageSize, params);
    }
	
    /**
     * 跳转到：编辑
     * @param appStartPageId 启动页ID
     */
	@RequestMapping(value="/to_start_page_edit")
	public ModelAndView to_start_page_edit(@RequestParam("appStartPageId") Long appStartPageId) {
		ModelAndView mv = new ModelAndView();
		AppStartPage appStartPage = appStartPageService.getAppStartPageById(appStartPageId);
		mv.addObject("appStartPage", appStartPage);
		mv.setViewName("jsp/app/start_page_edit");
		return mv;
	}
	
	/**
	 * 执行：编辑
	 * @param request
	 * @param session
	 * @param appStartPageId 启动页ID
	 * @param pageTitle 标题
	 * @param picPathFile 图片文件
	 * @return
	 */
    @RequestMapping("/start_page_edit")
    @ResponseBody
    public Object start_page_edit(HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "appStartPageId", required = false) Long appStartPageId,
                           @RequestParam(value = "pageTitle", required = false) String pageTitle,
                           @RequestParam("picPath") MultipartFile picPathFile) {
        try {
            // 合法性验证
            if (null == appStartPageId) {
                return returnResultMap(false, null, "check", "启动页ID不能为空");
            }
            if (null == pageTitle || "".equals(pageTitle)) {
                return returnResultMap(false, null, "check", "标题不能为空");
            }
            
            // 根据用户ID，加载一条数据
            AppStartPage appStartPage = appStartPageService.getAppStartPageById(appStartPageId);
            if(null == appStartPage){
            	return returnResultMap(false, null, "check", "启动页信息不存在");
            }
            
            // 服务根目录
            String rootPath = request.getSession().getServletContext().getRealPath("");
            
            // 中间路径
            String appStartPagePicPath = PropertiesUtils.getInstance().get("APP_START_PAGE_PIC_PATH");
            
            // 物理基础路径
            String path = rootPath + appStartPagePicPath;
            
            // 请求基础路径
            String http_base_url = appStartPagePicPath;
            
 			// 保存图片【开始】
 			InputStream inputStream = picPathFile.getInputStream();
 			
 			String filename = picPathFile.getOriginalFilename();
 			String newFileName = "";
 			
 			if(null != filename && !"".equals(filename)){
 				String suffix = filename.substring(filename.lastIndexOf("."), filename.length());
 				String uuid = UUID.randomUUID().toString();
 				byte[] b = new byte[1024 * 8];
 				int nRead = 0;
 				newFileName = uuid + suffix;
 				// 创建图片
 				OutputStream outputStream = new FileOutputStream(new java.io.File(path + newFileName));
 				while ((nRead = inputStream.read(b)) != -1)
 				{
 					outputStream.write(b, 0, nRead);
 				}
 				outputStream.flush();
 				outputStream.close();
 				inputStream.close();
 			}else {
 				return returnResultMap(false, null, "check", "图片不能为空");
			}
 			http_base_url = http_base_url + newFileName;
 			path = path + newFileName;
 			// 保存图片【结束】
            
            // 获取当前登录用户
            AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
            // 根据用户ID，加载一条数据
            AdminInfo adminInfo = adminInfoService.getAdminById(sessionAdminInfo.getAdminId());
            
            // 赋值
            appStartPage.setPageTitle(pageTitle);//标题
            appStartPage.setPicUrl(http_base_url);//图片请求路径
            appStartPage.setPicPath(path);//图片物理路径
            appStartPage.setUpdateTime(new Date());//最后更改时间
            appStartPage.setAdminId(adminInfo.getAdminId());//操作人ID
            
            // 更改操作
            appStartPageService.updateAppStartPage(appStartPage);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
    /**
     * 跳转到：详情
     * @param appStartPageId 启动页ID
     */
    @RequestMapping(value = "/to_start_page_show")
    public ModelAndView to_start_page_show(@RequestParam("appStartPageId") Long appStartPageId) {
    	ModelAndView mv = new ModelAndView();
    	AppStartPage appStartPage = appStartPageService.getAppStartPageById(appStartPageId);
		mv.addObject("appStartPage", appStartPage);
		mv.setViewName("jsp/app/start_page_show");
        return mv;
    }
    
    /**
     * 执行：启用、禁用
     * @param request
     * @param session
     * @param appStartPageId 启动页ID
     * @param status 状态
     * @return
     */
    @RequestMapping("/doState")
    @ResponseBody
    public Object doState(HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "appStartPageId", required = false) Long appStartPageId,
                           @RequestParam(value = "status", required = false) String status) {
        try {
            // 合法性验证
            if (null == appStartPageId) {
                return returnResultMap(false, null, "check", "启动页ID不能为空");
            }
            if (null == status || "".equals(status)) {
                return returnResultMap(false, null, "check", "状态不能为空");
            }else if(!"0".equals(status) && !"1".equals(status)){
            	return returnResultMap(false, null, "check", "状态值无效");
			}
            
            // 根据用户ID，加载一条数据
            AppStartPage appStartPage = appStartPageService.getAppStartPageById(appStartPageId);
            if(null == appStartPage){
            	return returnResultMap(false, null, "check", "启动页信息不存在");
            }
            
            // 获取当前登录用户
            AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
            // 根据用户ID，加载一条数据
            AdminInfo adminInfo = adminInfoService.getAdminById(sessionAdminInfo.getAdminId());
            
            // 赋值
            appStartPage.setStatus(status);//状态
            appStartPage.setUpdateTime(new Date());//最后更改时间
            appStartPage.setAdminId(adminInfo.getAdminId());//操作人ID
            
            // 更改操作
            appStartPageService.updateAppStartPage(appStartPage);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
    //*****************************************************************************************************************************************
    
    //*****************************************************************************************************************************************
    
    /**
     * 跳转到：APP Banner 列表
     */
    @RequestMapping(value = "/to_app_banner_list")
	public ModelAndView to_app_banner_list(String type) {
		ModelAndView mv = new ModelAndView();
		if (null != type && !"".equals(type) && "1".equals(type))
			mv.setViewName("jsp/wechat/wechat_banner_list");
		else
			mv.setViewName("jsp/app/app_banner_list");
		return mv;
	}
    
    /**
     * 执行：分页列表
     * @param pageSize 页数
     * @param pageNo 页码
     */
	@RequestMapping(value = "/app_banner_list")
    @ResponseBody
    public Object app_banner_list(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,String appType
			,String bannerName,String state,String startDate,String endDate) throws Exception {
		
		// 填充参数
        Map params = new HashMap();
        if(null !=appType && !"".equals(appType)&&"4".equals(appType))
        	params.put("appType", appType);
        if(null ==appType || "".equals(appType)||!"4".equals(appType))
        	params.put("appTypeRevo", 1);
        if(null !=bannerName && !"".equals(bannerName))
        	params.put("bannerName", bannerName);
        if( null!=state && !"".equals(state))
        	params.put("state", state);
        if( null !=startDate &&!"".equals(startDate))
        	params.put("startPublishDate", startDate);
        if( null !=endDate &&!"".equals(endDate))
        	params.put("endPublishDate", endDate);
        return appStartPageService.findAppBannerByPage(pageNo, pageSize, params);
    }
	
    /**
     * 跳转到：添加
     */
	@RequestMapping(value="/to_app_banner_add")
	public ModelAndView to_app_banner_add(String type) {
		ModelAndView mv = new ModelAndView();
		if (null != type && !"".equals(type) && "1".equals(type))
			mv.setViewName("jsp/wechat/wechat_banner_add");
		else
			mv.setViewName("jsp/app/app_banner_add");
		return mv;
	}
    
    /**
     * 跳转到：编辑
     * @param appBannerId bannerId
     */
	@RequestMapping(value="/to_app_banner_edit")
	public ModelAndView to_app_banner_edit(
			@RequestParam("appBannerId") Long appBannerId, String type) {
		ModelAndView mv = new ModelAndView();
		AppBanner appBanner = appStartPageService.getAppBannerById(appBannerId);
		mv.addObject("appBanner", appBanner);
		if (null != type && !"".equals(type) && "1".equals(type))
			mv.setViewName("jsp/wechat/wechat_banner_edit");
		else
			mv.setViewName("jsp/app/app_banner_edit");
		return mv;
	}
	
    /**
     * 跳转到：详情
     * @param appBannerId bannerId
     */
    @RequestMapping(value = "/to_app_banner_show")
	public ModelAndView to_app_banner_show(
			@RequestParam("appBannerId") Long appBannerId, String type) {
		ModelAndView mv = new ModelAndView();
		AppBanner appBanner = appStartPageService.getAppBannerById(appBannerId);
		mv.addObject("appBanner", appBanner);
		if('1'==appBanner.getPublishState()){
			appBanner.setPublishDate(null);
			mv.addObject("publishDateStatus", "立即");
		}else{
			mv.addObject("publishDateStatus", com.xt.cfp.core.util.DateUtil.getDateLong(appBanner.getPublishDate()));
		}
		if (null != type && !"".equals(type) && "1".equals(type))
			mv.setViewName("jsp/wechat/wechat_banner_show");
		else
			mv.setViewName("jsp/app/app_banner_show");
		return mv;
	}
	
	/**
	 * 执行：add
	 * @param request
	 * @param session
	 * @return
	 */
    @RequestMapping("/app_banner_add")
    @ResponseBody
    public Object app_banner_add(HttpServletRequest request, HttpSession session) {
        try {
            
//        	public class AppBanner {
        	AppBanner appBanner = new AppBanner();
//        	    private String appType;//平台
        	appBanner.setAppType("3");
//        	    private Long orderBy;//banner顺序
        	appBanner.setOrderBy(3l);
//        	    private String bannerName;//活动名称
        	appBanner.setBannerName("bannerName活动名称");
//        	    private String httpUrl;//banner跳转地址
        	appBanner.setHttpUrl("banner跳转地址");
//        	    private String httpMethod;//请求方式
        	appBanner.setHttpMethod("post");
//        	    private String httpIsToken;//是否传UserToken
        	appBanner.setHttpIsToken("true");
//        	    private String title;//分享标题
        	appBanner.setTitle("title分享标题");
//        	    private String desc;//分享文案
        	appBanner.setDesc("desc分享文案");
//        	    private String link;//分享链接
        	appBanner.setLink("link分享链接");
//        	    private String shareCloseUrl;//监控分享链接
        	appBanner.setShareCloseUrl("shareCloseUrl监控分享链接");
//        	    private String closeUrl;//监控活动关闭链接
        	appBanner.setCloseUrl("closeUrl监控活动关闭链接");
//        	    private String shareBackUrl;//分享结果回调地址
        	appBanner.setShareBackUrl("shareBackUrl分享结果回调地址");
//        	    private String imageSrc;//banner图片
        	appBanner.setImageSrc("imageSrcbanner图片");
//        	    private String physicsImageSrc;//banner图片物理
        	appBanner.setPhysicsImageSrc("physicsImageSrc banner图片物理");
//        	    private String imgUrl;//分享小图
        	appBanner.setImgUrl("imgUrl分享小图");
//        	    private String physicsImgUrl;//分享小图物理
        	appBanner.setPhysicsImgUrl("physicsImgUrl分享小图物理");
//        	    private String state;//状态
        	appBanner.setState("0");
//        	    private Date updateTime;//最后更改时间
        	appBanner.setUpdateTime(new Date());
//        	    private Long adminId;//最后更改人
        	appBanner.setAdminId(1l);
//        	
        	appStartPageService.addAppBanner(appBanner);
        	
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
    /**
     * 执行：App Banner编辑
     * @param request
     * @param session
     * @param action 操作方式 (edit,add)
     * @param appBannerId bannerId
     * @param appType app类型
     * @param orderBy banner顺序
     * @param bannerName 活动名称
     * @param httpUrl banner跳转地址
     * @param httpMethod 请求方式
     * @param httpIsToken 是否传UserToken
     * @param title 分享标题
     * @param desc 分享文案
     * @param link 分享链接
     * @param shareCloseUrl 监控分享链接
     * @param closeUrl 监控活动关闭链接
     * @param shareBackUrl 分享结果回调地址
     * @param state 状态
     * @param imageSrcFile banner图片
     * @param imgUrlFile 分享小图
     * @return
     */
    @RequestMapping("/app_banner_edit")
    @ResponseBody
    public Object app_banner_edit(HttpServletRequest request, HttpSession session,
    					   @RequestParam(value = "action", required = false) String action,
    					   @RequestParam(value = "appBannerId", required = false) Long appBannerId,
                           @RequestParam(value = "appType", required = false) String appType,
                           @RequestParam(value = "orderBy", required = false) String orderBy,
                           @RequestParam(value = "bannerName", required = false) String bannerName,
                           @RequestParam(value = "httpUrl", required = false) String httpUrl,
                           @RequestParam(value = "httpMethod", required = false) String httpMethod,
                           @RequestParam(value = "httpIsToken", required = false) String httpIsToken,
                           @RequestParam(value = "title", required = false) String title,
                           @RequestParam(value = "desc", required = false) String desc,
                           @RequestParam(value = "link", required = false) String link,
                           @RequestParam(value = "shareCloseUrl", required = false) String shareCloseUrl,
                           @RequestParam(value = "closeUrl", required = false) String closeUrl,
                           @RequestParam(value = "shareBackUrl", required = false) String shareBackUrl,
                           @RequestParam(value = "state", required = false) String state,
                           @RequestParam("imageSrc") MultipartFile imageSrcFile,
                           @RequestParam("imgUrl") MultipartFile imgUrlFile) {
        try {
            // 合法性验证
        	if(null == action || "".equals(action)){
        		return returnResultMap(false, null, "check", "缺少参数");
        	}else if (!"add".equals(action) && !"edit".equals(action)) {
        		return returnResultMap(false, null, "check", "参数异常");
			}
            if (null == appBannerId && "edit".equals(action)) {
                return returnResultMap(false, null, "check", "BannerId,不能为空");
            }
            if (null == appType || "".equals(appType)) {
                return returnResultMap(false, null, "check", "App类型,不能为空");
            }
            if (null == orderBy || "".equals(orderBy)) {
                return returnResultMap(false, null, "check", "banner顺序,不能为空");
            }
            if (null == bannerName || "".equals(bannerName)) {
                return returnResultMap(false, null, "check", "活动名称,不能为空");
            }
            if (null == httpUrl || "".equals(httpUrl)) {
                return returnResultMap(false, null, "check", "banner跳转地址,不能为空");
            }
            if (null == httpMethod || "".equals(httpMethod)) {
                return returnResultMap(false, null, "check", "请求方式,不能为空");
            }
            if (null == httpIsToken || "".equals(httpIsToken)) {
                return returnResultMap(false, null, "check", "是否传UserToken,不能为空");
            }
            if (null == title || "".equals(title)) {
                return returnResultMap(false, null, "check", "分享标题,不能为空");
            }
            if (null == desc || "".equals(desc)) {
                return returnResultMap(false, null, "check", "分享文案,不能为空");
            }
            if (null == link || "".equals(link)) {
                return returnResultMap(false, null, "check", "分享链接,不能为空");
            }
            if (null == state || "".equals(state)) {
                return returnResultMap(false, null, "check", "状态,不能为空");
            }
            
            AppBanner appBanner = new AppBanner();
            // 根据ID，加载一条数据
            if("edit".equals(action)){
            	appBanner = appStartPageService.getAppBannerById(appBannerId);
                if(null == appBanner){
                	return returnResultMap(false, null, "check", "App Banner信息不存在");
                }
            }
            
            // 验证顺序编号是否存在
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("appType", appType);
            params.put("orderBy", orderBy);
            List<AppBanner> appBannerList = appStartPageService.getAppBannerByList(params);
            if(null != appBannerList && appBannerList.size() > 0){
            	if(appBannerId != appBannerList.get(0).getAppBannerId()){
            		return returnResultMap(false, null, "check", "banner顺序编号已经存在，请修改！");
            	}
            }
            
            // 服务根目录
            String rootPath = request.getSession().getServletContext().getRealPath("");
            
            // 中间路径
            String appStartPagePicPath = PropertiesUtils.getInstance().get("APP_START_PAGE_PIC_PATH");
            
            // 物理基础路径
            String path = rootPath + appStartPagePicPath;
            
            // 请求基础路径
            String http_base_url = appStartPagePicPath;
            
 			// imageSrcFile保存图片【开始】
 			InputStream imageSrcFile_inputStream = imageSrcFile.getInputStream();
 			String imageSrcFile_filename = imageSrcFile.getOriginalFilename();
 			String imageSrcFile_newFileName = "";
 			if(null != imageSrcFile_filename && !"".equals(imageSrcFile_filename)){
 				String suffix = imageSrcFile_filename.substring(imageSrcFile_filename.lastIndexOf("."), imageSrcFile_filename.length());
 				String uuid = UUID.randomUUID().toString();
 				byte[] b = new byte[1024 * 8];
 				int nRead = 0;
 				imageSrcFile_newFileName = uuid + suffix;
 				// 创建图片
 				OutputStream outputStream = new FileOutputStream(new java.io.File(path + imageSrcFile_newFileName));
 				while ((nRead = imageSrcFile_inputStream.read(b)) != -1)
 				{
 					outputStream.write(b, 0, nRead);
 				}
 				outputStream.flush();
 				outputStream.close();
 				imageSrcFile_inputStream.close();
 			}else {
 				if("add".equals(action)){
 					return returnResultMap(false, null, "check", "Banner图片不能为空");
 				}
			}
 			String imageSrcFile_http_base_url = http_base_url + imageSrcFile_newFileName;
 			String imageSrcFile_path = path + imageSrcFile_newFileName;
 			// imageSrcFile保存图片【结束】
 			
 		    // imgUrlFile保存图片【开始】
 			InputStream imgUrlFile_inputStream = imgUrlFile.getInputStream();
 			String imgUrlFile_filename = imgUrlFile.getOriginalFilename();
 			String imgUrlFile_newFileName = "";
 			if(null != imgUrlFile_filename && !"".equals(imgUrlFile_filename)){
 				String suffix = imgUrlFile_filename.substring(imgUrlFile_filename.lastIndexOf("."), imgUrlFile_filename.length());
 				String uuid = UUID.randomUUID().toString();
 				byte[] b = new byte[1024 * 8];
 				int nRead = 0;
 				imgUrlFile_newFileName = uuid + suffix;
 				// 创建图片
 				OutputStream outputStream = new FileOutputStream(new java.io.File(path + imgUrlFile_newFileName));
 				while ((nRead = imgUrlFile_inputStream.read(b)) != -1)
 				{
 					outputStream.write(b, 0, nRead);
 				}
 				outputStream.flush();
 				outputStream.close();
 				imgUrlFile_inputStream.close();
 			}else {
 				if("add".equals(action)){
 					return returnResultMap(false, null, "check", "分享小图不能为空");
 				}
			}
 			String imgUrlFile_http_base_url = http_base_url + imgUrlFile_newFileName;
 			String imgUrlFile_path = path + imgUrlFile_newFileName;
 			// imgUrlFile保存图片【结束】
            
            // 获取当前登录用户
            AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
            // 根据用户ID，加载一条数据
            AdminInfo adminInfo = adminInfoService.getAdminById(sessionAdminInfo.getAdminId());
            
            // 赋值
            appBanner.setAppType(appType);//平台
            appBanner.setOrderBy(Long.valueOf(orderBy));//banner顺序
            appBanner.setBannerName(bannerName);//活动名称
            appBanner.setHttpUrl(httpUrl);//banner跳转地址
            appBanner.setHttpMethod(httpMethod);//请求方式
            appBanner.setHttpIsToken(httpIsToken);//是否传UserToken
            appBanner.setTitle(title);//分享标题
            appBanner.setDesc(desc);//分享文案
            appBanner.setLink(link);//分享链接
            appBanner.setShareCloseUrl(shareCloseUrl);//监控分享链接
            appBanner.setCloseUrl(closeUrl);//监控活动关闭链接
            appBanner.setShareBackUrl(shareBackUrl);//分享结果回调地址
            
            if(!"".equals(imageSrcFile_newFileName)){
            	appBanner.setImageSrc(imageSrcFile_http_base_url);//banner图片
                appBanner.setPhysicsImageSrc(imageSrcFile_path);//banner图片物理
            }
            
            if(!"".equals(imgUrlFile_newFileName)){
            	appBanner.setImgUrl(imgUrlFile_http_base_url);//分享小图
                appBanner.setPhysicsImgUrl(imgUrlFile_path);//分享小图物理
            }
            
            appBanner.setState(state);//状态
            appBanner.setUpdateTime(new Date());//最后更改时间
            appBanner.setAdminId(adminInfo.getAdminId());//最后更改人
            
            // 更改操作
            if("edit".equals(action)){
            	appStartPageService.updateAppBanner(appBanner);
            }else {
            	appStartPageService.addAppBanner(appBanner);
			}
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
    /**
     * 执行：启用、禁用
     * @param request
     * @param session
     * @param appBannerId BannerId
     * @param state 状态
     * @return
     */
    @RequestMapping("/doAppBannerState")
    @ResponseBody
    public Object doAppBannerState(HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "appBannerId", required = false) Long appBannerId,
                           @RequestParam(value = "state", required = false) String state) {
        try {
            // 合法性验证
            if (null == appBannerId) {
                return returnResultMap(false, null, "check", "缺少参数");
            }
            if (null == state || "".equals(state)) {
                return returnResultMap(false, null, "check", "状态不能为空");
            }else if(!"0".equals(state) && !"1".equals(state)){
            	return returnResultMap(false, null, "check", "状态值无效");
			}
            
            // 根据ID，加载一条数据
            AppBanner appBanner = appStartPageService.getAppBannerById(appBannerId);
            if(null == appBanner){
            	return returnResultMap(false, null, "check", "信息不存在");
            }
            
            // 获取当前登录用户
            AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
            // 根据用户ID，加载一条数据
            AdminInfo adminInfo = adminInfoService.getAdminById(sessionAdminInfo.getAdminId());
            
            // 赋值
            appBanner.setState(state);//状态
            appBanner.setUpdateTime(new Date());//最后更改时间
            appBanner.setAdminId(adminInfo.getAdminId());//操作人ID
            
            // 更改操作
            appStartPageService.updateAppBanner(appBanner);
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
    /**
     * 执行：删除
     * @param request
     * @param session
     * @param appBannerId BannerId
     * @return
     */
    @RequestMapping("/doAppBannerDelete")
    @ResponseBody
    public Object doAppBannerDelete(HttpServletRequest request, HttpSession session,
                           @RequestParam(value = "appBannerId", required = false) Long appBannerId) {
        try {
            // 合法性验证
            if (null == appBannerId) {
                return returnResultMap(false, null, "check", "缺少参数");
            }
            
            AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
            if(null == sessionAdminInfo){
            	return returnResultMap(false, null, "check", "请先登录");
            }
            
            // 根据ID，加载一条数据
            AppBanner appBanner = appStartPageService.getAppBannerById(appBannerId);
            if(null == appBanner){
            	return returnResultMap(false, null, "check", "信息不存在");
            }
            
            // 删除操作
            appStartPageService.deleteAppBannerById(appBanner.getAppBannerId());
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
    
    
    
    
    
    
    
    
    
    @RequestMapping("/wechat_banner_edit")
    @ResponseBody
    public Object wechat_banner_edit(HttpServletRequest request, HttpSession session,
    					   @RequestParam(value = "action", required = false) String action,
    					   @RequestParam(value = "appBannerId", required = false) Long appBannerId,
                           @RequestParam(value = "appType", required = false) String appType,
                           @RequestParam(value = "orderBy", required = false) String orderBy,
                           @RequestParam(value = "bannerName", required = false) String bannerName,
                           @RequestParam(value = "httpUrl", required = false) String httpUrl,
                           @RequestParam(value = "state", required = false) String state,
                           @RequestParam("imageSrc") MultipartFile imageSrcFile ,
                           @RequestParam(value = "publishState", required = false) char publishState,
                           @RequestParam(value = "publishDate", required = false)  String publishDate
                           ) {
        try {
            // 合法性验证
        	if(null == action || "".equals(action)){
        		return returnResultMap(false, null, "check", "缺少参数");
        	}else if (!"add".equals(action) && !"edit".equals(action)) {
        		return returnResultMap(false, null, "check", "参数异常");
			}
            if (null == appBannerId && "edit".equals(action)) {
                return returnResultMap(false, null, "check", "BannerId,不能为空");
            }
            if (null == appType || "".equals(appType)) {
            	return returnResultMap(false, null, "check", "App类型,不能为空");
            }
            if (null == orderBy || "".equals(orderBy)) {
            	return returnResultMap(false, null, "check", "banner顺序,不能为空");
            }
            if (null == bannerName || "".equals(bannerName)) {
            	return returnResultMap(false, null, "check", "活动名称,不能为空");
            }
            if (null == httpUrl || "".equals(httpUrl) ) {//||!httpUrl.startsWith("https://www.caifupad.com") 
            	return returnResultMap(false, null, "check", "banner跳转地址,不能为空");//并且必须以https://www.caifupad.com 开头
            }
            if (null == state || "".equals(state)) {
            	return returnResultMap(false, null, "check", "状态,不能为空");
            }
            if ("".equals(publishState)) {
            	return returnResultMap(false, null, "check", "发布状态,不能为空");
            }
            if("0".equals(publishState)){
//            	publishDate = new Date();
            	if (null == publishDate || "".equals(publishDate)) {
                	return returnResultMap(false, null, "check", "发布时间,不能为空");
                }
            }
          
            AppBanner appBanner = new AppBanner();
            // 根据ID，加载一条数据
            if("edit".equals(action)){
            	appBanner = appStartPageService.getAppBannerById(appBannerId);
                if(null == appBanner){
                	return returnResultMap(false, null, "check", "App Banner信息不存在");
                }
            }
            
            // 验证顺序编号是否存在
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("appType", appType);
            params.put("orderBy", orderBy);
            List<AppBanner> appBannerList = appStartPageService.getAppBannerByList(params);
            if(null != appBannerList && appBannerList.size() > 0){
            	if(appBannerId != appBannerList.get(0).getAppBannerId()){
            		return returnResultMap(false, null, "check", "banner顺序编号已经存在，请修改！");
            	}
            }
            
            // 服务根目录
            String rootPath = request.getSession().getServletContext().getRealPath("");
            
            // 中间路径
            String appStartPagePicPath = PropertiesUtils.getInstance().get("APP_START_PAGE_PIC_PATH");
            
            // 物理基础路径
            String path = rootPath + appStartPagePicPath;
            File file= new File(path);
            if(!file.exists()){
            	file.mkdirs();
            }
            // 请求基础路径
            String http_base_url = appStartPagePicPath;
            
 			// imageSrcFile保存图片【开始】
 			InputStream imageSrcFile_inputStream = imageSrcFile.getInputStream();
 			String imageSrcFile_filename = imageSrcFile.getOriginalFilename();
 			String imageSrcFile_newFileName = "";
 			if(null != imageSrcFile_filename && !"".equals(imageSrcFile_filename)){
 				String suffix = imageSrcFile_filename.substring(imageSrcFile_filename.lastIndexOf("."), imageSrcFile_filename.length());
 				String uuid = UUID.randomUUID().toString();
 				byte[] b = new byte[1024 * 8];
 				int nRead = 0;
 				imageSrcFile_newFileName = uuid + suffix;
 				// 创建图片
 				OutputStream outputStream = new FileOutputStream(new java.io.File(path + imageSrcFile_newFileName));
 				while ((nRead = imageSrcFile_inputStream.read(b)) != -1)
 				{
 					outputStream.write(b, 0, nRead);
 				}
 				outputStream.flush();
 				outputStream.close();
 				imageSrcFile_inputStream.close();
 			}else {
 				if("add".equals(action)){
 					return returnResultMap(false, null, "check", "Banner图片不能为空");
 				}
			}
 			String imageSrcFile_http_base_url = http_base_url + imageSrcFile_newFileName;
 			String imageSrcFile_path = path + imageSrcFile_newFileName;
 			// imageSrcFile保存图片【结束】
 			// imgUrlFile保存图片【结束】
            
            // 获取当前登录用户
            AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
            // 根据用户ID，加载一条数据
            AdminInfo adminInfo = adminInfoService.getAdminById(sessionAdminInfo.getAdminId());
            
            appBanner.setAppType(appType);//平台
            appBanner.setOrderBy(Long.valueOf(orderBy));//banner顺序
            appBanner.setBannerName(bannerName);//活动名称
            appBanner.setHttpUrl(httpUrl);//banner跳转地址
            appBanner.setPublishStatus(publishState);
           
            Date date = new Date();
           
            if('0'==publishState){//0是定时
            	 Date parseStrToDate = com.xt.cfp.core.util.DateUtil.parseStrToDate(publishDate, "yyyy-MM-dd");
                 int daysBetween = com.xt.cfp.core.util.DateUtil.daysBetween(date,parseStrToDate);
                 if(daysBetween<0){
                 	return returnResultMap(false, null, "check", "定时发布的时间不能小于当前时间");
                 }
            	appBanner.setPublishDate(parseStrToDate);
            }else{//1是立即
            	appBanner.setPublishDate(com.xt.cfp.core.util.DateUtil.getShortDateWithZeroTime(date));
            }
            
            
            if(!"".equals(imageSrcFile_newFileName)){
            	appBanner.setImageSrc(imageSrcFile_http_base_url);//banner图片
                appBanner.setPhysicsImageSrc(imageSrcFile_path);//banner图片物理
            }
            
            
            appBanner.setState(state);//状态
            appBanner.setUpdateTime(new Date());//最后更改时间
            appBanner.setAdminId(adminInfo.getAdminId());//最后更改人
            
            // 更改操作
            if("edit".equals(action)){
            	appStartPageService.updateAppBanner(appBanner);
            }else {
            	appStartPageService.addAppBanner(appBanner);
			}
        } catch (Exception e) {
            e.printStackTrace();
            return returnResultMap(false, null, null, e.getMessage());
        }
        return returnResultMap(true, null, null, null);
    }
    
    
    
    
    /**
     * 跳转到：编辑
     * @param appBannerId bannerId
     * to_app_banner_edit
     */
	@RequestMapping(value="/to_wechat_banner_edit")
	public ModelAndView to_wechat_banner_edit(
			@RequestParam("appBannerId") Long appBannerId, String type) {
		ModelAndView mv = new ModelAndView();
		AppBanner appBanner = appStartPageService.getAppBannerById(appBannerId);
		if('1'==appBanner.getPublishState()){
			appBanner.setPublishDate(null);
			mv.addObject("publishDateStatus", "立即");
		}else{
			mv.addObject("publishDateStatus", com.xt.cfp.core.util.DateUtil.getDateLong(appBanner.getPublishDate()));
		}
		mv.addObject("publishDate", com.xt.cfp.core.util.DateUtil.getDateLong(appBanner.getPublishDate()));
		mv.addObject("appBanner", appBanner);
	
		mv.setViewName("jsp/wechat/wechat_banner_edit");
		return mv;
	}  
    
    
    
    
    
	
}
