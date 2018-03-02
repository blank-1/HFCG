package com.xt.cfp.web.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.io.FileUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.constants.RateEnum;
import com.xt.cfp.core.constants.RateEnum.RateProductStatusEnum;
import com.xt.cfp.core.constants.RateEnum.RateUserStatusEnum;
import com.xt.cfp.core.constants.UserType;
import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.RateProduct;
import com.xt.cfp.core.pojo.RateUser;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.ext.RateProductVO;
import com.xt.cfp.core.pojo.ext.RateUsageHistoryVO;
import com.xt.cfp.core.pojo.ext.RateUserVO;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.service.RateProductService;
import com.xt.cfp.core.service.RateUsageHistoryService;
import com.xt.cfp.core.service.RateUserService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.util.ExcelUtil;
import com.xt.cfp.core.util.Pagination;

@Controller
@RequestMapping(value = "/jsp/rate/user")
public class RateUserController extends BaseController {

	@Autowired
    private RateUserService rateUserService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RateProductService rateProductService;
    @Autowired
    private RateUsageHistoryService rateUsageHistoryService;

	/**
	 * 跳转至：加息券发放列表页
	 */
    @RequestMapping(value = "to_list")
    public String to_list(){
        return "jsp/rate/user/list";
    }
    
    /**
     * 执行：加息券发放列表
     * @param rateUser
     * @param userName
     * @param mobileNo
     * @param usageScenario
     * @param status
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public Object list(@ModelAttribute RateUser rateUser,
                       @RequestParam(value = "userName", defaultValue = "", required = false) String userName,
                       @RequestParam(value = "mobileNo" ,defaultValue = "", required = false) String mobileNo,
                       @RequestParam(value = "usageScenario" ,defaultValue = "", required = false) String usageScenario,
                       @RequestParam(value = "status" ,defaultValue = "", required = false) String status,
                       @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                       @RequestParam(value = "page", defaultValue = "1") int pageNo){

        Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("userName", userName);
        customParams.put("mobileNo", mobileNo);
        customParams.put("usageScenario", usageScenario);
        customParams.put("status", status);
        
        Pagination<RateUserVO> pagination = rateUserService.getRateUserPaging(pageNo, pageSize, rateUser, customParams);
        
        //数据处理[开始]
        List<RateUserVO> vos =  pagination.getRows();
        if(null != vos && vos.size() > 0){
        	RateProductVO rateProductVO = new RateProductVO();
        	for (RateUserVO v : vos) {
        		rateProductVO.getRateProductConditionValue(v.getCondition());
        		v.setConditionStr(rateProductVO.getConditionDesc());
        		
        		//如果状态为未使用，并且使用次数大于0，则状态改为使用中
        		if(RateUserStatusEnum.UNUSED.getValue().equals(v.getStatus()) && v.getUsedTimes() > 0){
        			rateUserService.changeRateUserStatus(v.getRateUserId(), RateUserStatusEnum.USING);
        		}
			}
        }
        //数据处理[结束]
        
        return pagination;
    }
    
    /**
     * 加息券发放导出报表
     * @param rateUser
     * @param userName
     * @param mobileNo
     * @param usageScenario
     * @param status
     * @param response
     */
    @RequestMapping(value = "exportExcel")
    public void exportExcel(@ModelAttribute RateUser rateUser,
				            @RequestParam(value = "userName", defaultValue = "", required = false) String userName,
				            @RequestParam(value = "mobileNo" ,defaultValue = "", required = false) String mobileNo,
				            @RequestParam(value = "usageScenario" ,defaultValue = "", required = false) String usageScenario,
				            @RequestParam(value = "status" ,defaultValue = "", required = false) String status,
                            HttpServletResponse response){

    	Map<String, Object> customParams = new HashMap<String, Object>();
        customParams.put("userName", userName);
        customParams.put("mobileNo", mobileNo);
        customParams.put("usageScenario", usageScenario);
        customParams.put("status", status);

        rateUserService.exportExcel(response, rateUser, customParams);
    }
    
	/**
	 * 跳转至：加息券发放添加页
	 */
    @RequestMapping(value = "to_add")
    public String to_add(HttpServletRequest request){
    	List<RateProduct> rateProductList = rateProductService.getEfficientRateProductList();
    	request.setAttribute("rateProductList", rateProductList);
        return "jsp/rate/user/add";
    }
    
    /**
     * 根据加息券产品ID，获取详情
     * @param rateProductId
     * @return
     */
    @RequestMapping(value = "getRateProductDetail")
    @ResponseBody
    public Object getRateProductDetail(Long rateProductId) {
    	RateProduct rateProduct = rateProductService.getRateProductById(rateProductId);
    	
    	RateProductVO rateProductVO = new RateProductVO();
    	rateProductVO.getRateProductConditionValue(rateProduct.getCondition());
        
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("usage_scenario", RateEnum.RateProductScenarioEnum.getRateProductScenarioEnumByValue(rateProduct.getUsageScenario()).getDesc());//使用场景
        resultMap.put("condition", rateProductVO.getConditionDesc());//使用条件
        resultMap.put("usage_times", rateProduct.getUsageTimes()>0?rateProduct.getUsageTimes()+"次":"无限制");//使用次数
        resultMap.put("usage_duration", rateProduct.getUsageDuration()+"天");//有效时长
        resultMap.put("end_date", sdf.format(rateProduct.getEndDate()));//有效截止日
        
        return JSON.toJSON(resultMap);
    }
    
    /**
     * 执行：加息券发放
     * @param rateProductId 加息券ID
     * @param rateUserSource 使用场景
     * @param targetUser 目标用户（0.个人；1.导入；2.全部）
     * @param userId 如果是0个人，用户的ID
     * @param userIdList 如果是1导入，用户ID集合
     * @return
     */
    @RequestMapping(value = "add")
    @ResponseBody
    public String add(String rateProductId, String rateUserSource, String targetUser, String userId, String userIdList){
        AdminInfo currentUser = getCurrentUser();
        if (currentUser==null)
            return "session已过期";
        try {
            switch (targetUser){
                case "0":
                	rateUserService.handOutOne(Long.valueOf(rateProductId), Long.valueOf(userId), rateUserSource, currentUser.getAdminId());
                    break;
                case "1":
                    List<Long> userIds = new ArrayList<Long>();
                    for (String user_id : userIdList.split(",")){
                        userIds.add(Long.valueOf(user_id));
                    }
                    rateUserService.handOutSome(Long.valueOf(rateProductId), userIds, rateUserSource, currentUser.getAdminId());
                    break;
                case "2":
                	rateUserService.handOutAll(Long.valueOf(rateProductId), rateUserSource, currentUser.getAdminId());
                    break;
            }
            rateProductService.changeRateProductStatus(Long.valueOf(rateProductId), RateProductStatusEnum.HAS_USED);
            return "success";
        }catch (SystemException se){
            logger.error(se.getDetailDesc(),se);
            se.printStackTrace();
            return se.getMessage();
        }catch (Exception e) {
        	logger.error(e.getMessage(),e);
            e.printStackTrace();
            return e.getMessage();
		}
    }
    
    /**
     * 执行：导入用户文件
     * @param request
     * @param response
     */
    @RequestMapping(value = "importExcel")
    @ResponseBody
    public String  importExcel(HttpServletRequest request,HttpServletResponse response,MultipartFile importFile){
    	try {
    		List<String> loginName = importExcel(importFile);
            String userIdStr = "";
            String error = "";
            for (String name:loginName){
                UserInfo user = userInfoService.getUserByLoginName(name);
                if (user==null){
                    error = "导入用户中有系统不存在的用户";
                    break;
                }
                if(UserType.COMMON.getValue().equals(user.getType())){
                    userIdStr+=user.getUserId()+",";
                }else{
                    error = "导入用户存在非普通用户，导入失败";
                    break;
                }
            }
            userIdStr = userIdStr.substring(0,userIdStr.length()-1);
            return "<script>parent.inputUserIds('"+userIdStr+"',"+loginName.size()+",'"+error+"');</script>";
		} catch (Exception e) {
			return "<script>parent.inputUserIds('提示',0,'文件格式错误！');</script>";
		}
        
    }
    
    /**
     * 文件解析
     * @param importFile
     * @return
     */
    private List<String> importExcel(MultipartFile importFile) {
        try {
        	String type="1";
        	if(importFile.getName().indexOf(".xlsx")>=0){
        		type="2";
        	}
            File tempFile = FileUtil.createTempFile();
            FileUtil.appendBytes(tempFile,importFile.getBytes());

            List<List<Map<String, Object>>> lists = ExcelUtil.analysisExcel(tempFile,type);
            List<Map<String,Object>> result = lists.get(0);//sheet-0
            List<String> loginNameList = new ArrayList<String>();
            for (Map<String,Object> map:result){
                Collection<Object> values = map.values();
                for (Object o:values){
                    if (o==null)
                        continue;
                    loginNameList.add(o.toString());
                }
            }
            return loginNameList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 跳转至：详情页面
     * @param request
     * @param rateUserId
     * @return
     */
    @RequestMapping(value = "detail")
    public String detail(HttpServletRequest request, Long rateUserId) {
    	RateUser rateUser = rateUserService.getRateUserById(rateUserId);
    	RateProduct rateProduct = rateProductService.getRateProductById(rateUser.getRateProductId());
    	UserInfoVO userInfoVO = userInfoService.getUserExtByUserId(rateUser.getUserId());
    	List<RateUsageHistoryVO> rateUsageHistoryVOList = rateUsageHistoryService.getHistoryByRateUserId(rateUser.getRateUserId());
    	
    	RateProductVO rateProductVO = new RateProductVO();
    	rateProductVO.getRateProductConditionValue(rateProduct.getCondition());
    	
    	request.setAttribute("rateUser", rateUser);
    	request.setAttribute("rateProduct", rateProduct);
    	request.setAttribute("userInfoVO", userInfoVO);
    	request.setAttribute("conditionDesc", rateProductVO.getConditionDesc());
    	request.setAttribute("rateUsageHistoryVOList", rateUsageHistoryVOList);
    	
        return "jsp/rate/user/detail";
    }
	
}
