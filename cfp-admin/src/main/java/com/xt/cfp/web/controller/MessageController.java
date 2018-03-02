package com.xt.cfp.web.controller;



import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xt.cfp.core.pojo.UserMessage;
import com.xt.cfp.core.service.UserMessageService;


@Controller
@RequestMapping("/jsp/message")
public class MessageController extends BaseController{
	private static Logger logger = Logger.getLogger(MessageController.class);
	@Autowired
	private UserMessageService userMessageService;
	/**
	 * 跳转公告和站内信列表
	 * @return
	 */
	@RequestMapping(value = "/to_station_notice_list")
    public ModelAndView to_systemmsg_list() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/message/station_notice_msg_list");
        return mv;
    }
	
	/**
	 * 跳转系统消息列表
	 * @return
	 */
	@RequestMapping(value = "/to_system_list")
    public ModelAndView to_station_notice_list() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/message/system_msg_list");
        return mv;
    }
	
	/**
	 *  公告站内信 执行分页列表:
	 * @param request
	 * @param session
	 * @param pageSize
	 * @param pageNo
	 * @param message_name
	 * @param message_type
	 * @param message_start_time
	 * @param message_end_time
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/station_notice_list")
    @ResponseBody
    public Object list(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "message_name", required = false) String message_name,
			@RequestParam(value = "message_type", required = false) String message_type,
			@RequestParam(value = "message_start_time", required = false) String message_start_time,
			@RequestParam(value = "message_end_time", required = false) String message_end_time) throws Exception {
		
		System.out.println("message");
		System.out.println(pageSize +" "+ pageNo  +" "+ message_name +" "+ message_type +" "+ message_start_time +" "+ message_end_time);
		
        
        return userMessageService.getAllMessageList(pageNo, pageSize, message_name, message_type, message_start_time, message_end_time);
    }
	/**
	 * 站内信和公告详情
	 * @param msgId
	 * @return
	 */
	@RequestMapping(value = "/to_notice_station_show")
    public ModelAndView to_notice_station_show(@RequestParam(value = "msgId") Long msgId){
		System.out.println("to_notice_station_show");
		ModelAndView mv = new ModelAndView();
		UserMessage userMessage = userMessageService.getMessageDetail(msgId);
		
		mv.addObject(userMessage);
        mv.setViewName("jsp/message/msg_show");
		return mv;
	}
	/**
	 * 系统消息详情
	 * @param msgId
	 * @return
	 */
	@RequestMapping(value = "/to_system_show")
    public ModelAndView to_system_show(@RequestParam(value = "msgId") Long msgId){
		System.out.println("to_system_show");
		ModelAndView mv = new ModelAndView();
		UserMessage userMessage = userMessageService.getMessageSysDetail(msgId);
		
		mv.addObject(userMessage);
        mv.setViewName("jsp/message/msg_show");
		return mv;
	}
	/**
	 * 撤回
	 * @param msgId
	 * @param reciverId
	 * @return
	 */
	@RequestMapping(value = "/to_msg_del")
	@ResponseBody
    public Object to_msg_del(@RequestParam(value = "msgId") Long msgId,
    		@RequestParam(value = "reciverId",required =false) Long reciverId){
		System.out.println("message_del");
		try {
				userMessageService.cancelMessage(msgId,reciverId);
			return "success";
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return "error";
		}
	}
	
	/**
	 * 系统消息  分页列表
	 * @param request
	 * @param session
	 * @param pageSize
	 * @param pageNo
	 * @param message_name
	 * @param message_start_time
	 * @param message_end_time
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/system_list")//station_notice_list
    @ResponseBody
    public Object station_notice_list(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "rows", defaultValue = "10") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "message_name", required = false) String message_name,
			@RequestParam(value = "message_start_time", required = false) String message_start_time,
			@RequestParam(value = "message_end_time", required = false) String message_end_time) throws Exception {
		
		System.out.println("message");
		System.out.println(pageSize +" "+ pageNo  +" "+ message_name +" "+ message_start_time +" "+ message_end_time);
        return userMessageService.getSystemMessageList(pageNo, pageSize, message_name, message_start_time, message_end_time);
    }
	
	@RequestMapping(value = "/to_station_add")
    public ModelAndView to_station_add() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/message/station_msg_add");
        return mv;
    }
	@RequestMapping(value = "/to_notice_add")
    public ModelAndView to_notice_add() {
    	ModelAndView mv = new ModelAndView();
        mv.setViewName("jsp/message/notice_msg_add");
        return mv;
    }
	@RequestMapping(value = "/station_notice_msg_add")
    @ResponseBody
    public Object station_notice_msg_add(HttpServletRequest request, HttpSession session,
    		@RequestParam(value = "msgName", required = false) String msgName,
			@RequestParam(value = "msgContent", required = false) String msgContent,
			@RequestParam(value = "reciverType", required = false) String reciverType,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "senderName", required = false) String senderName,
			@RequestParam(value = "topSign", required = false) String topSign,
			@RequestParam(value = "imgAddress", required = false) String imgAddress) throws Exception {
		
		System.out.println(msgName +" 1"+ msgContent +" "+ reciverType +" "+ endTime +" "+ senderName +" "+ topSign);
		try {
				if(endTime == null)
				{
					userMessageService.sendStationMessage(null, msgName, msgContent, reciverType, senderName, (long)1, imgAddress);
				}
				else
				{
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					Date date = sdf.parse(endTime);
					userMessageService.sendNoticeMessage(msgName, msgContent, date, (long)1, senderName, topSign);
				}
			return "success";
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return "error";
		}
		
	}
	/**
	 * 设置置顶
	 * @param msgId
	 * @param reciverId
	 * @return
	 */
	@RequestMapping(value = "/topSign")
	@ResponseBody
    public Object topSign(@RequestParam(value = "msgId") Long msgId,
    		@RequestParam(value = "topSign") String topSign){
		try {
			userMessageService.topSign(msgId, topSign);
			return "success";
		} catch (Exception e) {
			return "error";
		}
	}
}
