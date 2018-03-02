package com.xt.cfp.core.service.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.AddressVO;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.pojo.ext.crm.CRMCustomerVO;
import com.xt.cfp.core.pojo.ext.crm.InvestmentVO;
import com.xt.cfp.core.pojo.ext.crm.PerformanceStatisticsVO;
import com.xt.cfp.core.pojo.ext.crm.StaffTop;
import com.xt.cfp.core.pojo.ext.crm.StaffVO;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.ExcelUtil;
import com.xt.cfp.core.util.TimeInterval;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.css.parser.property.PrimitivePropertyBuilders.Page;

import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.service.CrmFranchiseService;
import com.xt.cfp.core.service.LoanProductService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.crm.StaffService;
import com.xt.cfp.core.util.Pagination;

/***
 * 名称 空值 类型 ------------------- -------- ------------- FRANCHISE_ID NOT NULL
 * NUMBER(11) ADDRESS_ID NUMBER(11) FRANCHISE_STATE CHAR(1) FRANCHISE_CODE
 * VARCHAR2(200) FRANCHISE_NAME VARCHAR2(200) FRANCHISE_AGREECODE VARCHAR2(100)
 * STAR_LEVEL VARCHAR2(10) SIGN_DATE DATE OPEN_DATE DATE CREATE_DATE DATE
 * CONCAT_PERSON VARCHAR2(50) CONCAT_INFORMATION VARCHAR2(50) LAST_UPDATE_DATE
 * DATE 消除记忆负担
 * 
 * @author wangyadong 组织结构
 */
@Service
public class CrmFranchiseImpl implements CrmFranchiseService {

	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private MyBatisDao myBatisDaoRead;
	@Autowired
	private StaffService staffService;
	@Autowired
	private UserInfoExtService userInfoExtService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private LoanProductService loanProductService;
	

	/***
	 * @author wangyadong
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            页数
	 * @param params
	 *            查询条件 组织结构列表
	 */
	@Override
	public Pagination<CrmFranchise> getCrmFranchiseList(Object params, int pageNo, int pageSize) {
		Pagination<CrmFranchise> pagination = new Pagination<CrmFranchise>();
		pagination.setCurrentPage(pageNo);
		pagination.setPageSize(pageSize);
		List<CrmFranchise> crmOrgList = myBatisDaoRead.getListForPaging("CRM_FRANCHISE.selectByPrimaryKeyPaging",
				params, pageNo, pageSize);
		pagination.setRows(crmOrgList);
		pagination.setTotal(myBatisDaoRead.count("CrmFranchisePaging", params));
		return pagination;
	}

	/***
	 * @author wangyadong
	 * @param adress
	 * @param CrmFranchise
	 *            加盟商 增加或者修改
	 */
	@Override
	public void saveOrUpdate(CrmFranchise franchis) {
		try {
			if (franchis.getFranchiseId() == null) {// 修改
				myBatisDao.update("CRM_FRANCHISE.insertSelective", franchis);
			} else {// 增加
				myBatisDao.insert("CRM_FRANCHISE.updateByPrimaryKeySelective", franchis);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/***
	 * @author wangyadong
	 * @param CrmFranchiseId
	 *            删除加盟商
	 */
	@Override
	public void delete(Long id) {
		myBatisDao.delete("CRM_FRANCHISE.deleteByPrimaryKey", id);

	}

	/***
	 * @author wangyadong
	 * @param CrmFranchiseId
	 *            查询加盟商
	 */
	@Override
	public CrmFranchise getCrmFranchiseById(Long id) {
		return myBatisDaoRead.get("CRM_FRANCHISE.selectByPrimaryKey", id);
	}

	/***
	 * @author wangyadong
	 * @param getAccountAsset
	 *            获取加盟商总投资额
	 */

	public CrmFranchise getAccountAsset(Long id) {
		// List<CreditorRights> list
		// =creditorRightsService.getCreditorRightsByFranchiseId();
		return myBatisDaoRead.get("CRM_FRANCHISE.selectByPrimaryKey", id);
	}

	/**
	 * 获取json串 加盟商列表数据 wangyadong
	 ***/
	@Override
	public 	Pagination<	CrmFranchise > getCrmFranchiseJsonList(Object params, int pageNo, int pageSize) {
		Pagination<CrmFranchise> page = new Pagination<CrmFranchise>();
		Integer count = myBatisDaoRead.count("CrmFranchisePaging", params);
		Map<String, Object> map = new HashMap<String, Object>();

		List<CrmFranchise> listForPaging = myBatisDaoRead.getListForPaging("CRM_FRANCHISE.selectByPrimaryKeyPaging",
				params, pageNo, pageSize);

		for (CrmFranchise crm : listForPaging) {
			AddressVO address = myBatisDaoRead.get("ADDRESS.getAddressVOByIdNvl", crm.getAddressId());
			if (address != null) {
				if (address.getDetail() != null && !"".equals(address.getDetail())) {
					crm.setDetailAddress(address.getDetail());
				}
				if (address.getProvince() != null) {
					crm.setPronvince(address.getProvince().toString());
				}
				if (address.getCity() != null) {
					crm.setCity(address.getCity().toString());
				}
				if (address.getDistrict() != null) {
					crm.setDistrict(address.getDistrict().toString());
				}
				if (address.getProvinceStr() != null) {
					if (address.getCityStr() != null) {
						if (address.getDistrictStr() != null) {
							crm.setAddress(address.getProvinceStr() + "-" + address.getCityStr() + "-"
									+ address.getDistrictStr());
						} else {
							crm.setAddress(address.getProvinceStr() + address.getCityStr() + "-");
						}
					} else {
						crm.setAddress(address.getProvinceStr() + "-" + "-");
					}
				} else {
					crm.setAddress("-" + "-" + "-");
				}
			} else {
				crm.setAddress("-" + "-" + "-");
			}
		
			// 循环查找员工
			if (crm.getFranchiseId() != null && !"".equals(crm.getFranchiseId())) {
				Map<String, Object> crmFranchiseInfoMap = myBatisDaoRead
						.get("CRM_FRANCHISE.getUserIdByfranchiseForCount", crm.getFranchiseId());
				BigDecimal countPrice = (BigDecimal) crmFranchiseInfoMap.get("AAAA");
				BigDecimal halfPrice = (BigDecimal) crmFranchiseInfoMap.get("BBBB");
				BigDecimal countUser = (BigDecimal) crmFranchiseInfoMap.get("CCCC");
				BigDecimal countCurrentUser = (BigDecimal) crmFranchiseInfoMap.get("EEE");
				BigDecimal countCurrentPrice = (BigDecimal) crmFranchiseInfoMap.get("FFF");
				crm.setOnSaleGoal(halfPrice.toString());
				crm.setAllManagerMoney(countPrice.toString());
				crm.setIncreaseByCurrentManagerMonth(countCurrentPrice.toString());
				crm.setIncreaseByCurrentMonth(Integer.parseInt(countCurrentUser.toString()));
				crm.setAllCustomer(Integer.parseInt(countUser.toString()));

			}
}
		page.setRows(listForPaging);
		page.setTotal(count);
		map.put("total", count);
		map.put("rows", listForPaging);

		return page;
	}

	/**
	 * 根据员工ID 查询所有的客户
	 * 
	 * @param userId
	 * @return
	 * @date 2015年11月30日 下午3:12:51
	 * @auhthor wangyadong
	 */
	public List<UserInfoExt> getPersonOrFranchiseAllManager(Long userId) {
		// 先根据员工id查询客户
		List<UserInfoExt> allUser = new ArrayList<UserInfoExt>();
		if (userId != null) {
			UserInfoExt emp=myBatisDaoRead.get("USER_INFO_EXT.selectByPrimaryKey", userId);
			allUser.add(emp);
		}
		List<CrmFranCustomer> customer=staffService.findStaffCustomers(userId, null);
		List<CrmFranCustomer> customer2=new ArrayList<CrmFranCustomer>();
		if(customer!=null&&customer.size()>0){
			for (CrmFranCustomer c : customer) {// 一级客户
				UserInfoExt ue=myBatisDaoRead.get("USER_INFO_EXT.selectByPrimaryKey", c.getCustomerId());
				if(ue!=null){
					allUser.add(ue);
				}
				customer2.add(c);
			}
		}
		List<CrmFranCustomer> customer3=new ArrayList<CrmFranCustomer>();
		if(customer2.size()>0){
			for (CrmFranCustomer c : customer2) {// 二级客户
				customer=staffService.findStaffCustomers(c.getCustomerId(), null);
				if(customer!=null&&customer.size()>0){
					for (CrmFranCustomer c2 : customer) {
						UserInfoExt ue=myBatisDaoRead.get("USER_INFO_EXT.selectByPrimaryKey", c2.getCustomerId());
						if(ue!=null){
							allUser.add(ue);
						}
						customer3.add(c2);
					}
				}
			}
		}
		if(customer3.size()>0){
			for (CrmFranCustomer c : customer3) {// 三级客户
				customer=staffService.findStaffCustomers(c.getCustomerId(), null);
				if(customer!=null&&customer.size()>0){
					for (CrmFranCustomer c2 : customer) {
						UserInfoExt ue=myBatisDaoRead.get("USER_INFO_EXT.selectByPrimaryKey", c2.getCustomerId());
						if(ue!=null){
							allUser.add(ue);
						}
					}
				}
			}
		}
		return allUser;
	}
	
	/**
	 * @param pageSize
	 * @param pageNo
	 * @param map
	 *            * 王亚东 *
	 ************/

	@Override
	public  Pagination<InvitationCode> getInvitationCode(Object params, int pageNo, int pageSize) {
		List<UserInfoExt> UserInfoList = new ArrayList<UserInfoExt>();
		// 查询员工
		Pagination<InvitationCode> page = new Pagination<InvitationCode>();
		Integer count = myBatisDaoRead.count("selectByPrimaryKeyByListCode", params);
		List<InvitationCode> userInfo = myBatisDaoRead.getListForPaging("selectByPrimaryKeyByListCode", params, pageNo,
				pageSize);
		if (userInfo != null && userInfo.size() > 0) {
			for (InvitationCode invitationCode : userInfo) {
				UserInfoList.addAll(getPersonOrFranchiseAllManager(invitationCode.getUserId()));
				invitationCode.setCountUser(UserInfoList.size());// 客户数
				// invitationCode.setCountUser(getPersonOrFranchiseAllManager(invitationCode.getUserId()。);//当月新增客户数
				InvitationCode moneny = getMoneny(UserInfoList);
				invitationCode.setCountMoneny(moneny.getCountMoneny());// 总金额
				invitationCode.setCount(moneny.getCount());// 查询投标客户数量
				invitationCode.setGetCount(moneny.getGetCount());// 查询投标客户数量
				invitationCode.setOnCount(moneny.getOnCount());// 折标
				UserInfoList.clear();
			}
             page.setRows(userInfo);
             page.setTotal(count);
//			map.put("total", count);
//			map.put("rows", userInfo);
		}else{
			  page.setRows(userInfo);
	          page.setTotal(count);
		}
		// 查询出所有的员工后 查询所有的购买金额、折标金额，标的数量，投标客户数
		
		return page;
	}
	
	/**
	 * //查询出所有的员工后 查询所有的购买金 额、折标金额，标的数量，投标客户数 王亚东
	 */
	public InvitationCode getMoneny(List<UserInfoExt> userinfo) {
		InvitationCode inc = new InvitationCode();
		List<InvitationCode> list = null;
		List<InvitationCode> listCurrent = null;// 当月新增
		BigDecimal bigdecimal = new BigDecimal("0");// 购买金额
		BigDecimal divide = new BigDecimal("0");// 折标总金额
		BigDecimal bigdecimalCurrent = new BigDecimal("0");// 折标总金额
		Integer count = null;
		Integer countOrder = null;
		BigDecimal zhe = new BigDecimal("0");// 折标金额

		if (userinfo != null && userinfo.size() > 0) {
			List<InvitationCode> all = new ArrayList<InvitationCode>();
			if(userinfo.size()>=1000){
				for (int i = 0; i < userinfo.size(); i+=500) {
					list = myBatisDaoRead.getList("selectByPrimaryKeyByCustomNum", userinfo.subList(i, i+500>=userinfo.size()?userinfo.size():i+500));// 查询购买金额
					if(list!=null&&list.size()>0){
						all.addAll(list);
					}
				}
			}else{
				list = myBatisDaoRead.getList("selectByPrimaryKeyByCustomNum", userinfo);// 查询购买金额
				if(list!=null&&list.size()>0){
					all.addAll(list);
				}
			}
			if (all.size() > 0) {
				for (InvitationCode ic : all) {
					if (ic != null) {
						if (ic.getAmount() != null) {
							bigdecimal = bigdecimal.add(ic.getAmount()); // 购买金额
							BigDecimal multiply = ic.getAmount().multiply(ic.getAmountArount());
							if (0 != multiply.compareTo(new BigDecimal("0"))) {
								divide = multiply.divide(new BigDecimal("12"), 2, BigDecimal.ROUND_HALF_EVEN);
								zhe = zhe.add(divide);
							} 
						}
					}
				}
			}
			if(userinfo.size()>=1000){
				countOrder=0;
				for (int i = 0; i < userinfo.size(); i+=500) {
					Integer oNums = myBatisDaoRead.get("selectByPrimaryKeyByOrderCount", userinfo.subList(i, i+500>=userinfo.size()?userinfo.size():i+500));// 查询购买订单数量
					if(oNums!=null){
						countOrder+=oNums;
					}
				}
			}else{
				countOrder = myBatisDaoRead.get("selectByPrimaryKeyByOrderCount", userinfo);// 查询购买订单数量
			}
			if(userinfo.size()>=1000){
				count=0;
				for (int i = 0; i < userinfo.size(); i+=500) {
					Integer nums=count = myBatisDaoRead.get("selectByPrimaryKeyByOrderCountTrue", userinfo.subList(i, i+500>=userinfo.size()?userinfo.size():i+500));// 查询投标客户数量
					if(nums!=null){
						count+=nums;
					}
				}
			}else{
				count = myBatisDaoRead.get("selectByPrimaryKeyByOrderCountTrue", userinfo);// 查询投标客户数量
			}

			
			if (bigdecimal.toString() != null) {
				inc.setCountMoneny(bigdecimal.toString());// 购买总金额
			} else {
				inc.setCountMoneny("0");// 购买总金额
			}
			if (count != null) {
				inc.setCount(count.toString());// 查询投标客户数量
			} else {
				inc.setCount("0");// 查询投标客户数量
			}
			if (countOrder != null) {
				inc.setGetCount(countOrder.toString());// 查询投标客户数量
			} else {
				inc.setGetCount("0");// 查询投标客户数量
			}
			if (zhe != null) {
				inc.setOnCount(zhe.toString());// 折标
			} else {
				inc.setOnCount("0");// 折标
			}
			if (bigdecimalCurrent != null) {// 当月购买总金额
				inc.setCrruentUseramount(bigdecimalCurrent.toString());
			} else {
				inc.setCrruentUseramount("0");
			}
			if (listCurrent != null && listCurrent.size() > 0) {// 当月新增客户数
				inc.setCrruentUser(listCurrent.size());
			} else {
				inc.setCrruentUser(0);
			}
		}
		return inc;

	}

	/**
	 * 查询加盟商列表 王亚东
	 */
	@Override
	public List<CrmFranchise> getCrmFranchiseList() {
		List<CrmFranchise>  list = myBatisDaoRead.getList("CRM_FRANCHISE.selectByPrimaryKeyPaging");
		return list;
	}

	/**
	 * 查询客户列表 王亚东
	 */
	@Override
	public Map<String, Object> getCustomerList(Map<String, String> params, Integer pageSize, Integer pageNo) {
		Integer count = myBatisDaoRead.count("selectByPrimaryKeyByCustomList", params);
		params.put("pageSize", Integer.toString(pageSize * pageNo));
		params.put("pageNo",  Integer.toString(pageSize * (pageNo - 1)));
 		List<String> userId=myBatisDaoRead.getList("selectByPrimaryKeyByCustomList", params);
 		List<UserInfo> all =myBatisDaoRead.getList("selectByPrimaryKeyByCustomListAll", userId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", count);
		map.put("rows", all);
		return map;
	}
	
	@Override
	public Pagination<CRMCustomerVO> getCustomerByPagination(Map<String, String> map, Integer pageSize, Integer pageNo) {
		Pagination<CRMCustomerVO> re = new Pagination<CRMCustomerVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);
        
        Map<String, Object> params = new HashMap<String, Object>();
        for (String key : map.keySet()) {
			if(map.get(key)!=null){
				params.put(key, map.get(key));
			}
		}
        
        int totalCount = this.myBatisDaoRead.count("getCRMCustomers", params);
        List<CRMCustomerVO> uah = this.myBatisDaoRead.getListForPaging("getCRMCustomers", params, pageNo, pageSize);
        Map param=new HashMap();
        for (CRMCustomerVO c : uah) {
        	c.setAccountInvestment(new BigDecimal(myBatisDaoRead.get("countCRMCustomerInvestmentAccount", c.getUserId()).toString()));
        	c.setAccountWithDraw(new BigDecimal(myBatisDaoRead.get("countCRMCustomerWithdrawAccount", c.getUserId()).toString()));
        	param.put("userId", c.getUserId());
        	LendOrder order=myBatisDaoRead.get("LENDORDER.getUserFirstOrLastInvestment",param);
        	c.setFirstInvest(order!=null?order.getBuyTime():null);
        	param.put("order", "order");
        	order=myBatisDaoRead.get("LENDORDER.getUserFirstOrLastInvestment",param);
        	c.setLastInvest(order!=null?order.getBuyTime():null);
		}
        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
	}
	
	@Override
	public void exportCustomerExcel(Map<String, String> params,HttpServletResponse response) {
		String[] title = {"客户用户名", "客户姓名", "客户手机号", "账户余额", "投资总金额", "提现总金额", "注册来源", "注册时间", "首投时间", 
				"最后投资时间", "最后登录时间", "邀请码", "客户状态"};
        List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();
        List<CRMCustomerVO> all = this.myBatisDaoRead.getList("getCRMCustomers", params);
 		DecimalFormat df = new DecimalFormat("0.00"); 
 		 Map param=new HashMap();
 		for (CRMCustomerVO u : all) {
 			Map<String, Object> map = new HashMap<String, Object>();
 			map.put(title[0], u.getLoginName());
 			map.put(title[1], u.getRealName());
 			map.put(title[2], u.getMobile());
 			map.put(title[3], u.getAccount());
 			map.put(title[4], df.format(new BigDecimal(myBatisDaoRead.get("countCRMCustomerInvestmentAccount", u.getUserId()).toString())));
 			map.put(title[5], df.format(new BigDecimal(myBatisDaoRead.get("countCRMCustomerWithdrawAccount", u.getUserId()).toString())));
 			map.put(title[6], u.getSource());
 			map.put(title[7], DateUtil.getDateLong(u.getRegTime()));
 			param.put("userId", u.getUserId());
 			LendOrder order=myBatisDaoRead.get("LENDORDER.getUserFirstOrLastInvestment",param);
 			map.put(title[8], order!=null?DateUtil.getDateLong(order.getBuyTime()):"");
 			param.put("order", "order");
        	order=myBatisDaoRead.get("LENDORDER.getUserFirstOrLastInvestment",param);
 			map.put(title[9], order!=null?DateUtil.getDateLong(order.getBuyTime()):"");
 			map.put(title[10],DateUtil.getDateLong(u.getLastLogin()));
 			map.put(title[11], u.getCode());
 			map.put(title[12], u.getStatus());
 			dataMap.add(map);
		}
 		try {
            OutputStream os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            String fileName = "客户列表";
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".xls");
            response.setContentType("application/msexcel");// 定义输出类型
            HSSFWorkbook wb = ExcelUtil.createExcel(title, dataMap, fileName);
            wb.write(os);
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	@Override
	public List<CrmFranchise> getCrmFranchiseListFromOrgCode(String code) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		return myBatisDaoRead.getList("CRM_FRANCHISE.getCrmFranchiseListFromOrgCode", map);
	}

	public void sum_up(Long fId) {// 伪造数据
		List<CrmFranchise> list = myBatisDaoRead.getList("CRM_FRANCHISE.selectEmployeeByPrimaryKey", 49l);
		if (list != null && list.size() > 0) {
			for (CrmFranchise crmFranchise : list) {
				Map<String, Integer> object = myBatisDaoRead.get("CRM_FRANCHISE.getCrmsumUp", crmFranchise.getUserId());
				object.get("");
			}
		}

	}

	/**** 充值列表 ***/
	public Integer sumlMonth(Map<String, Integer> map) {
		Integer lMonth = 0;// 当月
		if (map == null) {
			return 0;
		}
		lMonth += map.get("LmonthA");
		if (map.get("leijiA") != null && map.get("yueA") != null && map.get("jiduA") != null && map.get("nianA") != null
				&& map.get("LmonthA") != null) {

		}
		return lMonth;
	}

	/**** 充值列表 ***/
	public Integer sumRecharge(Map<String, Integer> map) {
		Integer year = 0;// 年
		if (map == null) {
			return 0;
		}
		year += map.get("nianA");
		if (map.get("leijiA") != null && map.get("yueA") != null && map.get("jiduA") != null && map.get("nianA") != null
				&& map.get("LmonthA") != null) {

		}
		return year;
	}

	/**** 充值列表 ***/

	public Integer sumSum(Map<String, Integer> map) {
		Integer sum = 0;// 累计

		if (map == null) {
			return 0;
		}
		sum += map.get("leijiA");
		if (map.get("leijiA") != null && map.get("yueA") != null && map.get("jiduA") != null && map.get("nianA") != null
				&& map.get("LmonthA") != null) {

		}
		return sum;
	}

	/**** 充值列表 ***/

	public Integer sumWithDraw(Map<String, Integer> map) {
		Integer month = 0;// 当月
		month += map.get("yueA");
		if (map.get("leijiA") != null && map.get("yueA") != null && map.get("jiduA") != null && map.get("nianA") != null
				&& map.get("LmonthA") != null) {

		}
		return month;
	}

	/**** 充值列表 ***/

	public Integer sumReson(Map<String, Integer> map) {
		Integer reson = 0;// 季度
		if (map == null) {
			return 0;
		}
		reson += map.get("jiduA");
		if (map.get("leijiA") != null && map.get("yueA") != null && map.get("jiduA") != null && map.get("nianA") != null
				&& map.get("LmonthA") != null) {

		}
		return reson;
	}
	
	@Override
	public Pagination<PerformanceStatisticsVO> performanceStatistice(int pageNo, int pageSize, String queryMethod,
			Map<String, Object> customParams) {
		Pagination<PerformanceStatisticsVO> re = new Pagination<PerformanceStatisticsVO>();
		re.setCurrentPage(pageNo);
		re.setPageSize(pageSize);

		Map<String, Object> params = new HashMap<String, Object>();
		TimeInterval timeInterval = (TimeInterval) customParams.get("timeInterval");
		
		/*params.put("customParams", customParams);
		int totalCount = this.myBatisDaoRead.count("performanceStatistice", params);
		List<PerformanceStatisticsVO> uah = this.myBatisDaoRead.getListForPaging("performanceStatistice", params, pageNo, pageSize);*/
		if(timeInterval!=null&&!"".equals(timeInterval)){
			params.put("startTime", DateUtil.getDateLong(timeInterval.startTime));
			params.put("endTime", DateUtil.getDateLong(timeInterval.endTime));
		}
	
		params.put("orgCode", customParams.get("code"));
		params.put("franId", customParams.get("franchiseeId"));
		params.put("userId", customParams.get("userId"));
		
		
//		 if(!com.xt.cfp.core.util.StringUtils.isNull(name))
//			 params.put("staffName", name);
//	     if(!com.xt.cfp.core.util.StringUtils.isNull(mobile))
//	         params.put("mobile", mobile);
		
		
		int totalCount = this.myBatisDaoRead.count("selectStaffTop", params);
         List<StaffTop> staffs = this.myBatisDaoRead.getListForPaging("selectStaffTop", params, pageNo, pageSize);
        List<PerformanceStatisticsVO> uah =new ArrayList<PerformanceStatisticsVO>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map map=new HashMap();
        if(timeInterval!=null&&!"".equals(timeInterval)){
        	  map.put("startTime", DateUtil.getDateLong(timeInterval.startTime));
              map.put("endTime", DateUtil.getDateLong(timeInterval.endTime));
		}
       
        //的
//        InterestCalculation.
      
        for (StaffTop staff : staffs) {
//        	staff.get
        	PerformanceStatisticsVO p=new PerformanceStatisticsVO();
        	p.setFname(staff.getFranchinseName());
        	p.setStaffName(staff.getGenName());
        	p.setCustomerName(staff.getName());
        	p.setCustomerMobile(staff.getMobile());
        	p.setAmount(staff.getAccount());
        	p.setDiscountAmount(staff.getDisAccount());
        	p.setIvcode(staff.getGenCode());
        	p.setgDiscount(staff.getgDisAccount());
	    	if(timeInterval!=null&&!"".equals(timeInterval)){
	    		  p.setTimeProid(sdf.format(timeInterval.getStartTime()) + "——" + sdf.format(timeInterval.getEndTime()));
	    	}
        	p.setUserId(staff.getUserId());
        	// 有效投资过万量  计算有效邀请量
        	Integer overAimcount=0;
        	Integer effectiveCount=0;
        	p.setEffectiveCount(effectiveCount);
        	p.setOverAimcount(overAimcount);
        	if(p.getUserId()==null){
        		continue;
        	}
        	List<UserInfo> users = userInfoService.findCRMUserAndCustomers(staff.getOrgCode(), p.getUserId());
        	if(users!=null&&users.size()>0){
        		if(users.size()>999){
        			for (int i = 0; i < users.size(); i+=500) {
        				StringBuffer sb=new StringBuffer();
        				sb.append(users.get(i).getUserId()+",");
        				String ids=sb.toString();
            			ids=ids.substring(0,ids.length()-1);
            			map.put("ids", ids);
            			Integer tempO=myBatisDaoRead.get("LENDORDER.getTenThousandSumByLendOrder", map);
            			overAimcount+=tempO!=null?tempO:0;
            			Integer tempE=myBatisDaoRead.get("LENDORDER.getValidUserNum", map);
            			effectiveCount+=tempE!=null?tempE:0;
					}
        		}else{
        			StringBuffer sb=new StringBuffer();
        			for (UserInfo u : users) {
    					sb.append(u.getUserId()+",");
    				}
        			String ids=sb.toString();
        			ids=ids.substring(0,ids.length()-1);
        			map.put("ids", ids);
        			Integer tempO=myBatisDaoRead.get("LENDORDER.getTenThousandSumByLendOrder", map);
        			overAimcount+=tempO!=null?tempO:0;
        			Integer tempE=myBatisDaoRead.get("LENDORDER.getValidUserNum", map);
        			effectiveCount+=tempE!=null?tempE:0;
        		}
        		p.setOverAimcount(overAimcount);
        		p.setEffectiveCount(effectiveCount);
        	}
        	uah.add(p);
		}
		
       
		re.setTotal(totalCount);
		re.setRows(uah);
		return re;
	}

	@Override
	public Pagination<InvestmentVO> investmentList(int pageNo, int pageSize, Map<String, Object> customParams) {
		Pagination<InvestmentVO> re = new Pagination<InvestmentVO>();
		re.setCurrentPage(pageNo);
		re.setPageSize(pageSize);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customParams", customParams);

		int totalCount = this.myBatisDaoRead.count("investmentList", params);
		List<InvestmentVO> uah = this.myBatisDaoRead.getListForPaging("investmentList", params, pageNo, pageSize);

		for (InvestmentVO vo : uah) {
			LendOrder order = myBatisDaoRead.get("LENDORDER.getFirstLendOrderDate", vo.getUserId());
			if (order != null && vo.getLendOrderId().compareTo(order.getLendOrderId()) == 0) {
				vo.setIsFirst("Y");
			} else {
				vo.setIsFirst("N");
			}

			if (vo.getGenId()!=null){
				UserInfoVO userExtForm = myBatisDaoRead.get("USER_INFO_EXT.getUserExtByUserId", vo.getGenId());
				vo.setGenName(userExtForm.getRealName());
			}
			//查询订单对应一个债券
			LoanProduct loanpoject=loanProductService.findLoanProductByOrerId(vo.getLendOrderId());
			if(null ==loanpoject || "".equals(loanpoject.getDueTimeType())){
				vo.setDueTimeType('3');
			}else{
				vo.setDueTimeType(loanpoject.getDueTimeType());
			}
		}

		re.setTotal(totalCount);
		re.setRows(uah);
		return re;
	}
	
	@Override
	public void exportInvestmentExcel(Map<String, Object> customParams, HttpServletResponse response) {
		String[] title = {"订单号", "首投", "购买产品", "购买金额", "客户用户名", "客户姓名", "客户手机号", "购买时间", "来源", 
						"订单状态", "邀请人", "所在部门", "加盟商", "隶属员工"};
        List<Map<String, Object>> dataMap = new ArrayList<Map<String, Object>>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("customParams", customParams);
		List<InvestmentVO> uah = this.myBatisDaoRead.getList("investmentList", params);
		DecimalFormat df = new DecimalFormat("0.00"); 
		for (InvestmentVO i : uah) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(title[0], i.getOrderNo());
			LendOrder order = myBatisDaoRead.get("LENDORDER.getFirstLendOrderDate", i.getUserId());
			if (order != null && i.getLendOrderId().compareTo(order.getLendOrderId()) == 0) {
				map.put(title[1], "Y");
			} else {
				map.put(title[1], "N");
			}
			map.put(title[2], i.getProductName());
			map.put(title[3], df.format(i.getBuyBalance()));
			map.put(title[4], i.getCustomeLoginName());
			map.put(title[5], i.getCustomeName());
			map.put(title[6], i.getMobileNo());
			map.put(title[7], DateUtil.getDateLong(i.getButTime()));
			String source="";
			if(i.getSource()!=null){
				if(i.getSource().equals("PC")){
					source="财富派web";
				}else if(i.getSource().equals("Wechat")){
					source="微信";
				}else if(i.getSource().equals("Andriod")){
					source="安卓";
				}else if(i.getSource().equals("IOS")){
					source="IOS";
				}
			}
			map.put(title[8], source);
			String status="";
			if(i.getStatus().equals("0")){
				status="未支付";
			}else if(i.getStatus().equals("1")){
				status="理财中/还款中";
			}else if(i.getStatus().equals("2")){
				status="已结清";
			}else if(i.getStatus().equals("3")){
				status="已过期";
			}else if(i.getStatus().equals("4")){
				status="已撤销";
			}else if(i.getStatus().equals("5")){
				status="未起息";
			}else if(i.getStatus().equals("6")){
				status="退出中";
			}else if(i.getStatus().equals("7")){
				status="流标";
			}
			map.put(title[9], status);
			map.put(title[10], i.getStaffName());
			map.put(title[11], i.getOrgName());
			map.put(title[12], i.getFname());
			if (i.getGenId()!=null){
				UserInfoVO userExtForm = myBatisDaoRead.get("USER_INFO_EXT.getUserExtByUserId", i.getGenId());
				i.setGenName(userExtForm.getRealName());
			}
			map.put(title[13], i.getGenName());
			dataMap.add(map);
		}
		try {
            OutputStream os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            String fileName = "新投资列表";
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + ".xls");
            response.setContentType("application/msexcel");// 定义输出类型
            HSSFWorkbook wb = ExcelUtil.createExcel(title, dataMap, fileName);
            wb.write(os);
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	/**
	 * 根据加盟商名字
	 * 
	 * @param franchiseName
	 * @return
	 */
	public Integer getCrmFranchiseByName(String franchiseName) {
		return this.myBatisDaoRead.get("getFranchiseByfranchiseName", franchiseName);
	}

}
