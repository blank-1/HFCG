package com.xt.cfp.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.pojo.ext.UserValidCode;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.Pagination;

@Controller
@RequestMapping(value = "/validCode")
public class ValidCodeController extends BaseController {
	
	private static Logger logger = Logger.getLogger(ValidCodeController.class);
	
	@Autowired
    private UserInfoService userInfoService;
	
	@RequestMapping(value = "showValidCodes")
    public String showValidCodes(HttpServletRequest request){
        return "jsp/validCode/validCode";
    }
	
	@RequestMapping(value = "showCodesList")
    @ResponseBody
    public Object showCodesList(HttpServletRequest request,
                                 @RequestParam(value = "userMobile", defaultValue = "", required = false) String userMobile,
                                 //@RequestParam(value = "startTime" ,defaultValue = "", required = false) String startTime,
                                 @RequestParam(value = "codeType" ,defaultValue = "", required = false) String codeType,
                                 @RequestParam(value = "rows", defaultValue = "20") int pageSize,
                                 @RequestParam(value = "page", defaultValue = "1") int pageNo) {

        
        Pagination<UserValidCode> results = userInfoService.findAllUserValidCodes(pageNo, pageSize, userMobile, codeType);
        return results;
    }
}
