package com.xt.cfp.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.external.yongyou.service.UserAccountHistoryYongYouService;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;

/**
 * 工具控制
 * */
@Controller
@RequestMapping(value = "/util")
public class UtilsController {

	private Logger logger = Logger.getLogger(UtilsController.class);

	@Autowired
	private UserAccountHistoryYongYouService userAccountHistoryYongYouService;

	/**
	 * 进入控制页
	 * */
	@RequestMapping ( value = "/index")
	public String index(){
		return "exportYongYou";
	}
	
	/**
	 * 用户流水导入用友控制
	 * */
	@RequestMapping(value = "/exeYongYouHis")
	@ResponseBody
	public Object executeYongYouHis(HttpServletRequest request,
			@RequestParam(value = "yongyouFlag", required = false) String flag) {
		String startTime = request.getParameter("yongyouStartTime");
		String endTime = request.getParameter("yongyouEndTime");
		if (StringUtils.isBlank(flag))
			throw new SystemException(SystemErrorCode.PARAM_MISS);

		try {
			if (StringUtils.equals("1", flag)) {// 全部导入
				userAccountHistoryYongYouService.requestAndSaveAccountHis(
						null, null);
			} else if (StringUtils.equals("2", flag)) {// 根据起始时间导入
				userAccountHistoryYongYouService.requestAndSaveAccountHis(
						startTime, endTime);
			} else {
				throw new SystemException(SystemErrorCode.SYSTEM_ERROR_CODE);
			}
		} catch (Exception e) {
			logger.error("导出失败，异常原因：", e);
			return returnResultMap(false, null, null, e.toString());
		}

		return returnResultMap(true, null, null, null);
	}

	/**
	 * 返回json格式结果。
	 * 
	 * @param isSuccess
	 *            是否成功
	 * @param data
	 *            数据
	 * @param errCode
	 *            错误编号
	 * @param errMsg
	 *            错误消息
	 */
	protected Object returnResultMap(boolean isSuccess, Object data,
			String errCode, String errMsg) {
		Map resultMap = new HashMap();
		if (isSuccess) {
			resultMap.put("result", "success");
			resultMap.put("data", data);
		} else {
			resultMap.put("result", "error");
			resultMap.put("errCode", errCode);
			resultMap.put("errMsg", errMsg);
		}
		return JSON.toJSON(resultMap);
	}

}
