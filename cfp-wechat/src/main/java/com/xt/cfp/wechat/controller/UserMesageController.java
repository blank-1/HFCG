package com.xt.cfp.wechat.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserMessage;
import com.xt.cfp.core.service.UserMessageService;
import com.xt.cfp.core.util.SecurityUtil;



@Controller
@RequestMapping(value = "/message")
public class UserMesageController extends BaseController{
	@Autowired
	UserMessageService userMessageService;
	
    @RequestMapping(value = "/toUserMessage")
    public String toUserMessage(){
    	
        return "/person/userMessage";
    }
    @RequestMapping(value = "/userMessageList")
	@ResponseBody
    public Object userMessageList(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
    		@RequestParam(value = "messageType[]", required=false) String[] messageType,
    		@RequestParam(value = "status[]", required=false) String[] status) {
    	UserInfo currentUser = SecurityUtil.getCurrentUser(true);
    	System.out.println(pageSize+" "+pageNo+" "+messageType.length+" "+status.length);
		return userMessageService.receptionUserMessageList(pageNo, pageSize, currentUser.getUserId(), status, messageType, null); 
    }
    @RequestMapping(value = "/readMessage")
   	@ResponseBody
       public Object readMessage(HttpServletRequest request, HttpSession session,
    		   @RequestParam(value = "reciveId", required=false) Long reciveId,
          		@RequestParam(value = "msgId", required=false) Long msgId) {
       	UserInfo currentUser = SecurityUtil.getCurrentUser(true);
       	UserMessage userMessage = userMessageService.getReadMessage(reciveId, currentUser.getUserId(),msgId);
       	
       	return userMessage;
    }
}
