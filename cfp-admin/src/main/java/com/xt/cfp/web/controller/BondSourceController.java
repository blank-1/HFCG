package com.xt.cfp.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xt.cfp.core.constants.*;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.pojo.ext.BondSourceExt;
import com.xt.cfp.core.pojo.ext.BondSourceUser;
import com.xt.cfp.core.pojo.ext.WithDrawExt;
import com.xt.cfp.core.util.Pagination;

/**
 * Created by luqinglin on 2015/6/19.
 */
@Controller
@RequestMapping(value = "/bondSource")
public class BondSourceController extends BaseController {

    private static Logger logger = Logger.getLogger(BondSourceController.class);

    @Autowired
    private BondSourceService bondSourceService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private RechargeOrderService rechargeOrderService;
    @Autowired
    private ConstantDefineService constantDefineService;
    @Autowired
    private CustomerCardService customerCardService;
    @Autowired
    private UserInfoExtService userInfoExtService;


    /**
     * 跳转至渠道页面
     * @return
     */
    @RequestMapping(value = "to_bondSource_list")
    public String toBondSourceList() {
        return "jsp/bondSource/bondSourceList";
    }

    /**
     * 渠道列表
     * @param request
     * @param bondSource
     * @param pageSize
     * @param pageNo
     * @return
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public Object list(HttpServletRequest request,
                       @ModelAttribute BondSource bondSource,
                       @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                       @RequestParam(value = "page", defaultValue = "1") int pageNo) {

        Pagination<BondSourceExt> bondSourcePaging = bondSourceService.getBondSourcePaging(pageNo, pageSize, bondSource, null);

        return bondSourcePaging;
    }

    /**
     * 跳至添加页面
     * @param request
     * @param bondSourceId
     * @return
     */
    @RequestMapping(value = "add")
    public String add(HttpServletRequest request,Long bondSourceId){
        if (bondSourceId!=null){
            //修改操作
            BondSource bondSource = bondSourceService.getBondSourceByBondSourceId(bondSourceId);
            request.setAttribute("bondSource", bondSource);
        }
        return "jsp/bondSource/bondSourceAdd";
    }

    /**
     * 保存渠道
     * @param bondSource
     * @return
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public String save(@ModelAttribute BondSource bondSource){
        try {
            if (bondSource.getBondSourceId()==null)
                bondSourceService.addBondSource(bondSource);
            else
                bondSourceService.updateBondSource(bondSource);
            return "success";
        }catch (SystemException se){
            logger.error(se.getDetailDesc(),se);
            se.printStackTrace();
            return se.getMessage();
        }

    }

    /**
     * 禁用渠道
     * @param request
     * @param bondSourceId
     * @return
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delBondSource(HttpServletRequest request,Long bondSourceId){
        //修改状态为禁用
        bondSourceService.changeBondSourceStatus(bondSourceId, BondSourceStatus.DISABLED);
        return "success";
    }

    /**
     * 解禁渠道
     * @param request
     * @param bondSourceId
     * @return
     */
    @RequestMapping(value = "startUse")
    @ResponseBody
    public String startUse(HttpServletRequest request,Long bondSourceId){
        //修改状态为正常
        bondSourceService.changeBondSourceStatus(bondSourceId, BondSourceStatus.NORMAL);
        return "success";
    }

    /**
     * 渠道详情
     * @param request
     * @param bondSourceId
     * @return
     */
    @RequestMapping(value = "detail")
    public String detail(HttpServletRequest request,Long bondSourceId){

        BondSource bondSource = bondSourceService.getBondSourceDetailByBondSourceId(bondSourceId);
        request.setAttribute("bondSource", bondSource);
        return "jsp/bondSource/bondSourceDetail";
    }

    /**
     * 原始债券人列表
     * @param request
     * @param bondSourceId
     * @return
     */
    @RequestMapping(value = "showBondUser")
    public String showBondUser(HttpServletRequest request,Long bondSourceId){
        request.setAttribute("bondSourceId", bondSourceId);
        return "jsp/bondSource/bondSourceUserList";
    }

    @RequestMapping(value = "bondSourceUserlist")
    @ResponseBody
    public Object bondSourceUserlist(HttpServletRequest request,
                       @ModelAttribute BondSourceUser bondSourceUser,
                       @RequestParam(required = false) Long bondSourceId,
                       @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                       @RequestParam(value = "page", defaultValue = "1") int pageNo) {

        Pagination<BondSourceUser> bondSourceUserPaging = bondSourceService.getBondSourceUserPaging(pageNo, pageSize, bondSourceUser, null);
        return bondSourceUserPaging;
    }
  /**
   * @param request
   * @param bondSourceUser
   * @param bondSourceId
   * @param pageSize
   * @param pageNo
   * @原始债权人列表
   * @date 2015年10月10日 下午5:38:08
   * @auhthor wangyadong
   */
    @RequestMapping(value = "bondUserList")
    @ResponseBody
    public Object bondUserList(HttpServletRequest request,
                       @ModelAttribute BondSourceUser bondSourceUser,
                       @RequestParam(required = false) Long bondSourceId,
                       @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                       @RequestParam(value = "page", defaultValue = "1") int pageNo) {

    	 Pagination<BondSourceUser> bondSourceUserPaging = bondSourceService.getBondSourceUserListPaging(pageNo, pageSize,bondSourceUser,null);
         return bondSourceUserPaging;
    }


    /**
     * @param request
     * @param bondSourceUser
     * @param bondSourceId
     * @param pageSize
     * @param pageNo
     * @原始债权人列表
     * @date 2015年10月10日 下午5:38:08
     * @auhthor wangyadong
     */
    @RequestMapping(value = "bondUserSelect")
    @ResponseBody
    public Object bondUserSelect(HttpServletRequest request,
                               @ModelAttribute BondSourceUser bondSourceUser,
                               @RequestParam(required = false) Long bondSourceId,
                               @RequestParam(value = "rows", defaultValue = "10") int pageSize,
                               @RequestParam(value = "page", defaultValue = "1") int pageNo) {

        Pagination<BondSourceUser> bondSourceUserPaging = bondSourceService.getBondSourceUserSelectPaging(pageNo, pageSize,bondSourceUser,null);
        return bondSourceUserPaging;
    }


    /**
     * 跳转至dialog页面
     * @param request
     * @param bondSourceId
     * @return
     * @date 2015年10月14日 下午4:12:51
     * @auhthor wangyadong
     *
     */
    @RequestMapping(value = "bondUserByDialog")
    public String bondUserByDialog(HttpServletRequest request,Long bondSourceId){

        BondSource bondSource = bondSourceService.getBondSourceDetailByBondSourceId(bondSourceId);
        request.setAttribute("bondSource", bondSource);
        return "jsp/bondSource/bondSourceDialog";
    }

    /**
     * 跳至原始债权人添加页面
     * @param request
     * @param userSourceId
     * @return
     */
    @RequestMapping(value = "addBondUser")
    public String addBondSourceUser(HttpServletRequest request,Long userSourceId,Long bondSourceId){
        if (userSourceId!=null){
            //修改操作
            BondSourceUser bondSourceUser = bondSourceService.getBondSourceUserByUserSourceId(userSourceId);
            request.setAttribute("bondSourceUser", bondSourceUser);
        }
        request.setAttribute("bondSourceId", bondSourceId);
        return "jsp/bondSource/bondSourceUserAdd";
    }


    /**
     * 跳至原始债权人添加页面
     * @param request
     * @param userSourceId
     * @return
     */
    @RequestMapping(value = "addBondUserformHF")
    public String addBondUserformHF(HttpServletRequest request,Long userSourceId,Long bondSourceId){
        if (userSourceId!=null){
            //修改操作
            BondSourceUser bondSourceUser = bondSourceService.getBondSourceUserByUserSourceId(userSourceId);
            request.setAttribute("bondSourceUser", bondSourceUser);
        }
        request.setAttribute("bondSourceId", bondSourceId);
        return "jsp/bondSource/bondSourceUserAddFromHF";
    }

    /**
     * 保存原始债权人
     * @param bondSourceUser
     * @return
     */
    @RequestMapping(value = "saveUser")
    @ResponseBody
    public String saveUser(@ModelAttribute BondSourceUser bondSourceUser){

        try{
            if (bondSourceUser.getUserId()==null) {
                bondSourceService.addBondSourceUser(bondSourceUser);
            }else
                bondSourceService.updateBondSourceUser(bondSourceUser);
            return "success";
        }catch (SystemException se){
            return se.getErrorCode().getDesc();
        }

    }
    /**
     * 保存原始债权人
     * @param bondSourceUser
     * @return
     */
    @RequestMapping(value = "saveUserToLocal")
    @ResponseBody
    public String saveUserToLocal(@ModelAttribute BondSourceUser bondSourceUser){
        if(null==bondSourceUser.getMobileNo()||"".equals(bondSourceUser.getMobileNo().trim()))
            return "手机号不能为空";


        try{
            if (bondSourceUser.getUserId()==null) {
                bondSourceService.addBondSourceUser(bondSourceUser);
            }else
                bondSourceService.updateBondSourceUser(bondSourceUser);
            return "success";
        }catch (SystemException se){
            return se.getErrorCode().getDesc();
        }

    }


    /**
     * 原始债权人多渠道
     * @param userId
     * @return
     * @author wangyadong
     */
    @RequestMapping(value = "saveUserRelation")
    @ResponseBody
    public String saveUserRelation(String userId,Long bondSourceId){
        try{
        	//userId等于空
        	if(userId==null||bondSourceId==null){
        		return "NULL";
        	}
             String flag = bondSourceService.addBondSourceUserNewRelation(userId,bondSourceId);
             return flag;
        }catch (SystemException se){
            return se.getErrorCode().getDesc();
        }

    }
    /**
     * 禁用原始债券人
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping(value = "deleteUser")
    @ResponseBody
    public String deleteUser(HttpServletRequest request,Long userId){
        //修改状态为禁用
        bondSourceService.changeBondSourceUserStatus(userId, UserStatus.DISABLED);
        return "success";
    }

    /**
     * 原始债券人解禁
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping(value = "free")
    @ResponseBody
    public String free(HttpServletRequest request,Long userId){
        //修改状态为正常
        bondSourceService.changeBondSourceUserStatus(userId, UserStatus.NORMAL);
        return "success";
    }

    /**
     * 跳至充值页面
     * @param request
     * @param bondSourceId
     * @return
     */
    @RequestMapping(value = "toIncome")
    public String toIncome(HttpServletRequest request,@RequestParam(required = false) Long bondSourceId){
        BondSource bondSource = bondSourceService.getBondSourceDetailByBondSourceId(bondSourceId);
        request.setAttribute("bondSource", bondSource);
        return "jsp/bondSource/bondSourceIncome";
    }
    /**
     * 跳至提现页面
     * @param request
     * @param bondSourceId
     * @return
     */
    @RequestMapping(value = "toWithDraw")
    public String toWithDraw(HttpServletRequest request,@RequestParam(required = false) Long bondSourceId){
        BondSource bondSource = bondSourceService.getBondSourceDetailByBondSourceId(bondSourceId);
        request.setAttribute("bondSource", bondSource);
        //准备银行信息
        List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType(ConstantDefineCode.BANK.getValue());
        //查询渠道最近使用的客户卡
        CustomerCard customerCard = customerCardService.getCustomerBindCardByUserId(bondSource.getUserId(), PayConstants.PayChannel.LL);
        request.setAttribute("customerCard", customerCard);
        request.setAttribute("constantDefines", constantDefines);
        return "jsp/bondSource/bondSourceWithDraw";
    }

    /**
     * 充值
     * @param rechargeOrder
     * @return
     */
    @RequestMapping(value = "income")
    @ResponseBody
    public String income(@ModelAttribute RechargeOrder rechargeOrder){
        try {
            bondSourceService.recharge(rechargeOrder);
            return "success";
        }catch (SystemException se){
            se.printStackTrace();
            return se.getMessage();
        }
    }

    /**
     * 提现
     * @param withDraw
     * @return
     */
    @RequestMapping(value = "withDraw")
    @ResponseBody
    public String withDraw(@ModelAttribute WithDrawExt withDraw){
        try {
            withDraw.setHappenType(WithDrawSource.USER_WITHDRAW.getValue());
            bondSourceService.withDraw(withDraw);
            return "success";
        }catch (SystemException se){
            se.printStackTrace();
            return se.getMessage();
        }
    }

    /**
	 * 加载渠道列表。
	 */
	@RequestMapping(value = "/loadBondSource")
	@ResponseBody
	public Object loadBondSource(@RequestParam(value = "selectedDisplay", required = false) String selectedDisplay){
		if("selected".equals(selectedDisplay)){
			selectedDisplay = "选择渠道";
		}else {
			selectedDisplay = "全部";
		}
		List<BondSource> list = bondSourceService.getAllBondSource();
		List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> map;
		map = new HashMap<Object, Object>();
		map.put("SOURCENAME", selectedDisplay);
		map.put("BONDSOURCEID", "");
		map.put("selected", true);
		maps.add(map);
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				BondSource bondSource = list.get(i);
				map = new HashMap<Object, Object>();
				map.put("SOURCENAME", bondSource.getSourceName());
				map.put("BONDSOURCEID", bondSource.getBondSourceId());
				maps.add(map);
			}
		}
		return maps;
	}

    /**
	 * 加载原始债权人列表。
	 */
	@RequestMapping(value = "/loadBondSourceUser")
	@ResponseBody
	public Object loadBondSourceUser(@RequestParam(value = "selectedDisplay", required = false) String selectedDisplay,
			@RequestParam(value = "bondSourceId", required = false) Long bondSourceId){
		if("selected".equals(selectedDisplay)){
			selectedDisplay = "选择原始债权人";
		}else {
			selectedDisplay = "全部";
		}
		List<BondSourceUser> list = bondSourceService.getAllBondSourceUserBySourceId(bondSourceId);
		List<Map<Object, Object>> maps = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> map;
		map = new HashMap<Object, Object>();
		map.put("BONDNAME", selectedDisplay);
		map.put("USERSOURCEID", "");
		map.put("selected", true);
		maps.add(map);
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				BondSourceUser bondSourceUser = list.get(i);
				map = new HashMap<Object, Object>();
				map.put("BONDNAME", bondSourceUser.getBondName());
				map.put("USERSOURCEID", bondSourceUser.getUserSourceId());
				maps.add(map);
			}
		}
		return maps;
	}

    /**
     * 根据渠道查询身份证号和姓名
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping(value = "selectUserByBondUserId")
    @ResponseBody
    public Map selectUserByBondUserId(HttpServletRequest request,Long userSourceId){
        Map<String , Object> map= new HashedMap();
        BondSourceUser bondSourceUserById = bondSourceService.getBondSourceUserById(userSourceId);
        UserInfoExt userInfoExt = userInfoExtService.getUserInfoExtById(bondSourceUserById.getUserId());
        map.put("realname",userInfoExt.getRealName());
        map.put("idcard",userInfoExt.getIdCard());
        return map;
    }
}
