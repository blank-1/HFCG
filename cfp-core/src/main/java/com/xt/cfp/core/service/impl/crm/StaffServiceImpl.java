package com.xt.cfp.core.service.impl.crm;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.constants.DisActivityEnums.InviteWhiteTabsTypeEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.*;
import com.xt.cfp.core.pojo.ext.UserInfoVO;
import com.xt.cfp.core.pojo.ext.crm.StaffTop;
import com.xt.cfp.core.pojo.ext.crm.StaffVO;
import com.xt.cfp.core.pojo.ext.crm.TransforStaff;
import com.xt.cfp.core.service.InviteWhiteTabsService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.WhiteTabsService;
import com.xt.cfp.core.service.crm.CrmCustomerBusHisService;
import com.xt.cfp.core.service.crm.CrmCustomerBusService;
import com.xt.cfp.core.service.crm.OrganizationService;
import com.xt.cfp.core.service.crm.StaffService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.MD5Util;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class StaffServiceImpl implements StaffService {

	private static Logger logger = Logger.getLogger(StaffServiceImpl.class);
	
	private static String[] filterOrg={"001001002","001001003002001","001001003003001"};
	
	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private MyBatisDao myBatisDaoRead;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private UserInfoExtService userInfoExtService;
	@Autowired
	private InviteWhiteTabsService inviteWhiteTabsService;
	@Autowired
	private WhiteTabsService whiteTabsService;
    @Autowired
    private CrmCustomerBusService crmCustomerBusService;
	@Autowired
	private CrmCustomerBusHisService crmCustomerBusHisService;
	
	@Override
	public Pagination<StaffVO> findStaffPaging(int pageNo, int pageSize,
			Long orgId, Long franId, String staffName,String staff_moblie,String orgCode) {
		Pagination<StaffVO> re = new Pagination<StaffVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);
        
        Map<String, Object> params = new HashMap<String, Object>();
        if(!StringUtils.isNull(staffName)){
        	params.put("staffName", staffName);
        }
        if(franId!=null){
        	params.put("franId", franId);
        }
        if(orgId!=null){
        	String ids=organizationService.findAllChildsBySelfForSQLSelect(orgId,"o");
        	if(ids!=null&&ids.length()>0){
        		params.put("ids", ids);
        	}
        }
        params.put("orgCode", orgCode);
        if(!StringUtils.isNull(staff_moblie)){
        	params.put("moblie", staff_moblie);
        }
        int totalCount = this.myBatisDaoRead.count("selectStaffList", params);
        List<StaffVO> uah = this.myBatisDaoRead.getListForPaging("selectStaffList", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
	}
	
	@Override
	public Pagination<StaffVO> findOrgStaffPaging(int pageNo, int pageSize,Long orgId) {
		Pagination<StaffVO> re = new Pagination<StaffVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        if(orgId!=null){
        	params.put("orgId", orgId);
        }

        int totalCount = this.myBatisDaoRead.count("selectStaffByOrgId", params);
        List<StaffVO> uah = this.myBatisDaoRead.getListForPaging("selectStaffByOrgId", params, pageNo, pageSize);

        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
	}

	@Override
	public StaffVO findById(Long staffId) {
		StaffVO staff=null;
		if(staffId!=null){
			staff=myBatisDaoRead.get("selectStaffById", staffId);
		}
		return staff;
	}

	@Override
	public CrmFranEmp findCrmFramEmpByMobileNo(String mobile) {
		//判断参数是否为null
		if (org.apache.commons.lang.StringUtils.isEmpty(mobile))
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("mobile", "null");

		CrmFranEmp staff=myBatisDaoRead.get("CRM_FRAN_EMP.findCrmFramEmpByMobileNo", mobile);
		return staff;
	}

	@Transactional
	@Override
	public String updateStatus(Long staffId, String status) {
		String result="操作成功";
		if(staffId==null){
			result="员工id为空";
			return result;
		}
		CrmFranEmp staff=myBatisDao.get("CRM_FRAN_EMP.selectByPrimaryKey", staffId);
		if(staff!=null){
			try {
				staff.setStatus(status);
				myBatisDao.update("CRM_FRAN_EMP.updateByPrimaryKey", staff);
			} catch (Exception e) {
				e.printStackTrace();
				result="更新失败";
			}
		}else{
			result="员工id异常";
		}
		return result;
	}

	@Override
	public CrmOrg findByStaffId(Long staffId) {
		
		return organizationService.findByStaffId(staffId);
	}

	@Transactional
	@Override
	public String saveStaff(Long orgId, Long staffId, String staffName,
			String mobile,  Long roleId) {
		String result="操作成功";
		if(roleId==null||roleId==-1){
			result="请为员工选择角色";
			return result;
		}
		if(orgId==null){
			//没有组织机构的都挂在根节点下
			CrmOrg org=organizationService.findAllMainOrgs().get(0).getOrg();
			orgId=org.getOrgId();
		}
		//关联用户和加盟商
		Long francId=null;
		francId=organizationService.findFranIdByOId(orgId);
		if(francId==null){
			francId=-1l;
		}
		try {
			CrmFranEmp emp=null;
			if(staffId!=null&&staffId!=-1){//更新
				emp=myBatisDao.get("CRM_FRAN_EMP.selectByPrimaryKey", staffId);
				if(!emp.getMobile().equals(mobile)){
					List<CrmFranEmp> e=myBatisDao.getList("CRM_FRAN_EMP.selectByPhone", mobile);
					if(e!=null&&e.size()>0){
						result="已注册手机号";
						return result;
					}
				}
				if(emp.getOrgId()!=null&&emp.getOrgId().compareTo(orgId)!=0){
					emp.setJoinTime(new Date());
				}
				//更新组织机构
				emp.setOrgId(orgId);
				emp.setMobile(mobile);
				//验证用户是否平台注册用户
				UserInfo userInfo =userInfoService.getUserByMobileNo(mobile);
				if(userInfo!=null){
					emp.setUserId(userInfo.getUserId());
					emp.setMobile(userInfo.getMobileNo());
					UserInfoVO uvo=userInfoService.getUserExtByUserId(userInfo.getUserId());
					if(uvo!=null&&!StringUtils.isNull(uvo.getRealName())){
						emp.setName(uvo.getRealName());
					}else{
						emp.setName("");
					}
					if(!emp.getName().equals(staffName)){
						result="用户名和平台注册姓名不匹配";
						return result;
					}
					Map<String, Object> param = new HashMap<String, Object>();
		            param.put("userId", userInfo.getUserId());
		            param.put("type", 0);
		            if(emp.getCode()==null||emp.getCode().equals("")){
		            	InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
						if(invite!=null){
							emp.setCode(invite.getInvitationCode());
						}
		            }
				}
				emp.setFranchiseId(francId);
				myBatisDao.update("CRM_FRAN_EMP.updateByPrimaryKeySelective", emp);
			}else{//插入
				if(!StringUtils.isNull(mobile)){
					List<CrmFranEmp> e=myBatisDao.getList("CRM_FRAN_EMP.selectByPhone", mobile);
					if(e!=null&&e.size()>0){
						result="已注册手机号";
						return result;
					}
				}
				//判断是否是注册用户
				UserInfo userInfo =userInfoService.getUserByMobileNo(mobile);
				emp=new CrmFranEmp();
				emp.setMobile(mobile);
				if(userInfo!=null){
					emp.setUserId(userInfo.getUserId());
					emp.setMobile(userInfo.getMobileNo());
					UserInfoVO uvo=userInfoService.getUserExtByUserId(userInfo.getUserId());
					if(uvo!=null&&uvo.getRealName()!=null){
						if(uvo.getIsVerified().equals("0")){
							result="未实名认证用户";
							return result;
						}
						emp.setName(uvo.getRealName());
					}else{
						emp.setName("");
					}
					if(!emp.getName().equals(staffName)){
						result="用户名和平台注册姓名不匹配";
						return result;
					}
					Map<String, Object> param = new HashMap<String, Object>();
		            param.put("userId", userInfo.getUserId());
		            param.put("type", 0);
					InvitationCode invite = userInfoExtService.getInviteCodeByUserId(param);
					if(invite!=null){
						emp.setCode(invite.getInvitationCode());
					}
				}else{
					result="平台未注册用户";
					return result;
				}

				//更新关系
				CrmCustomerBus customer=crmCustomerBusService.selectByCondition(userInfo.getUserId(),null);
				if(null!=customer){
					result="已存在一条该员工的关系记录";
					return result;
				}
				customer=new CrmCustomerBus();
				customer.setCreateTime(new Date());
				customer.setUserId(userInfo.getUserId());
				customer.setpId(userInfo.getUserId());
				crmCustomerBusService.insertCrmCustomerBus(customer);

				emp.setStatus("1");
				emp.setOrgId(orgId);
				emp.setPassword(MD5Util.MD5Encode("111111", "UTF-8"));
				emp.setJoinTime(new Date());
				emp.setFranchiseId(francId);
				myBatisDao.insert("CRM_FRAN_EMP.insert", emp);
				//将用户加入白名单中
				updateWhiteTab(orgId, userInfo);
			}
			//更新角色
			updateEmpRole(staffId, roleId, emp);
		} catch (SystemException e) {
			e.printStackTrace();
			result="更新员工失败";
		}
		return result;
	}

	private void updateEmpRole(Long staffId, Long roleId, CrmFranEmp emp) {
		CrmStaffRole role=myBatisDao.get("CRM_STAFF_ROLE.selectByStaffId", staffId);
		if(role==null){
            role=new CrmStaffRole();
            role.setRoleId(roleId);
            role.setStaffId(emp.getStaffId());
            myBatisDao.insert("CRM_STAFF_ROLE.insert", role);
        }else{
            role.setRoleId(roleId);
            myBatisDao.update("CRM_STAFF_ROLE.updateByPrimaryKeySelective", role);
        }
	}

	private void updateWhiteTab(Long orgId, UserInfo userInfo) {
		WhiteTabs wt=whiteTabsService.findByUserId(userInfo.getUserId());
		if(wt!=null){//将销售从白名单剔除
            whiteTabsService.delete(wt.getUserId());
        }
		List<CrmOrg> orgs=getFilterOrg();
		if(orgs.size()>0){
            for (CrmOrg o : orgs) {
                if(o.getOrgId().compareTo(orgId)==0){
                    InviteWhiteTabs iwt=inviteWhiteTabsService.findById(userInfo.getUserId());
                    if(iwt==null){//加入销售名单
                        iwt=new InviteWhiteTabs();
                        iwt.setSource("2");
                        iwt.setType(InviteWhiteTabsTypeEnum.TYPE_INVITE.getValue());
                        iwt.setUserId(userInfo.getUserId());
                        inviteWhiteTabsService.insert(iwt);
                    }
                    break;
                }
            }
        }
	}

	private List<CrmOrg> getFilterOrg(){
		List<CrmOrg> list = new ArrayList<CrmOrg>();
		for (String code : filterOrg) {
			List<CrmOrg> orgs=organizationService.findByCode(code);
			if(orgs!=null&&orgs.size()>0){
				list.addAll(orgs);
			}
		}
		return list;
	}
	
	@Override
	public CrmFranEmp findStaffById(Long staffId) {
		return myBatisDao.get("CRM_FRAN_EMP.selectByPrimaryKey", staffId);
	}

	@Override
	public CrmFranEmp getCrmFranEmpByStaffId(Long staffId) {
		return myBatisDaoRead.get("CRM_FRAN_EMP.selectByPrimaryKey",staffId);
	}

	@Transactional
	@Override
	public CrmFranEmp updateStaff(CrmFranEmp crmFranEmp) {
		if(crmFranEmp==null)
			throw new SystemException(ValidationErrorCode.ERROR_NULL).set("crmFranEmp", "null");

		//判断id属性是否为空
		if (crmFranEmp.getStaffId() == null)
			throw new SystemException(ValidationErrorCode.ERROR_REQUIRED_FIELD).set("staffId", "null");
		//修改
		myBatisDao.update("CRM_FRAN_EMP.updateByPrimaryKeySelective",crmFranEmp);

		return crmFranEmp;
	}

	@Override
	public List<StaffVO> findStaffByOId(Long oId) {
		List<StaffVO> emps=myBatisDaoRead.getList("CRM_FRAN_EMP.selectStaffByOrgId", oId);
		return emps;
	}
    /**
     * //查询加盟商ID，如果为超级管理员则不属于任何加盟商 ,因为一个人员只能属于一个加盟商
     */
	@Override
	public Long findCrmFranById(Long userId) {
		// TODO Auto-generated method stubselectByPrimaryKeyBycrmOrgFranchiseCount
		
		return myBatisDaoRead.get("selectByPrimaryKeyBycrmOrgFranchiseCount", userId);
	}


	/**
	 *@param staffId
     * @return
     *@date 2016年1月8日 下午15:06:16 
     *@auhthor wangyadong
	 */
	@Override
	public Long getCrmFranEmpOrgIdByStaffId(Long staffId) {
		// TODO Auto-generated method stub
		return myBatisDaoRead.get("getCrmFranEmpOrgIdByStaffId", staffId);
	}

	@Override
	public List<CrmFranEmp> findOrgStaffByOId(Long oId) {
		CrmOrg org=organizationService.findOrgById(oId);
		Map map=new HashMap();
		map.put("orgCode", org.getCode());
		List<CrmFranEmp> emps=myBatisDaoRead.getList("CRM_FRAN_EMP.selectOrgStaffByOrgCode", map);
		return emps;
	}

	@Override
	public Pagination<StaffTop> findTopStaffPaging(int pageNo, int pageSize, String time,String timeZone, Long franId, String order,String orgCode,Long userId) {
		Pagination<StaffTop> re = new Pagination<StaffTop>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        Map<String, Object> params = new HashMap<String, Object>();
        String[] times=null;
        if(!StringUtils.isNull(timeZone)&&timeZone.equals("-1")){
        	if(StringUtils.isNull(time)){
            	times=DateUtil.calcWeekOfDay(DateUtil.getDateLong(new Date()));
            }else{
            	times=DateUtil.calcWeekOfDay(time);
            }
        }else if(!StringUtils.isNull(timeZone)){
        	switch (timeZone) {
			case "0":
				times=DateUtil.calcMonthOfDay(new Date());
				break;
			case "1":
				times=DateUtil.calcQuarterOfDay(new Date());
				break;
			case "2":
				times=DateUtil.calcYearOfDay(new Date());
				break;
			}
        	 
        }else{
        	times=DateUtil.calcWeekOfDay(DateUtil.getDateLong(new Date()));
        }
        params.put("startTime", times[0]);
        params.put("endTime", times[1]);
        if(franId!=null){
        	params.put("franId",franId);
        }
        if(!StringUtils.isNull(order)){
        	if(order.equals("1")){
        		params.put("order", "dis");
        	}
        }
        params.put("orgCode", orgCode);
        params.put("userId", userId);
        int totalCount = this.myBatisDaoRead.count("selectStaffTop", params);
        List<StaffTop> uah = this.myBatisDaoRead.getListForPaging("selectStaffTop", params, pageNo, pageSize);
        DecimalFormat df = new DecimalFormat("0.00"); 
        Map map=new HashMap();
        map.put("startTime", times[0]);
        map.put("endTime", times[1]);
        for (StaffTop staffTop : uah) {
        	staffTop.setTime(times[0]+"----"+times[1]);
        	BigDecimal account=new BigDecimal(df.format(staffTop.getAccount()));
        	BigDecimal disAccount=new BigDecimal(df.format(staffTop.getDisAccount()));
        	staffTop.setAccount(account);
        	staffTop.setDisAccount(disAccount);
		}
        re.setTotal(totalCount);
        re.setRows(uah);

        return re;
	}

	@Override
	public List<CrmFranCustomer> findStaffCustomers(Long staffUid,Long customerId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pCustomerId", staffUid);
		params.put("customerId", customerId);
		List<CrmFranCustomer> list=myBatisDaoRead.getList("CRM_FRAN_CUSTOMER.selectStaffCustomer", params);
		return list;
	}
	
	@Transactional
	@Override
	public String saveCustomer(CrmFranCustomer customer) {
		myBatisDao.insert("CRM_FRAN_CUSTOMER.insert", customer);
		return null;
	}


	@Transactional
	@Override
	public String updateCustomer(CrmFranCustomer customer) {
		myBatisDao.update("CRM_FRAN_CUSTOMER.updateByPrimaryKeySelective", customer);
		return null;
	}

	@Override
	public String saveCustomerAfterRegister(Long staffUid, Long customerId) {
		String result="success";
		if(staffUid==null||customerId==null){
			result="员工id和客户id不能为空";
			return result;
		}
		CrmFranEmp emp=findByUserId(staffUid);
		if(emp==null){
			result="crm中该员工不存在";
			return result;
		}
		List<CrmFranCustomer> list=findStaffCustomers(staffUid,customerId);
		if(list==null||list.size()==0){
			CrmFranCustomer customer=new CrmFranCustomer();
			customer.setCreateDate(new Date());
			customer.setCustomerId(customerId);
			customer.setpCustomerId(staffUid);
			saveCustomer(customer);
		}
		return result;
	}

	@Override
	public CrmFranEmp findByUserId(Long userId) {
		return myBatisDaoRead.get("CRM_FRAN_EMP.selectByUserId", userId);
	}

	@Override
	public List<UserInfoExt> findStaffInUEAndHaveRecId() {
		return myBatisDaoRead.getList("CRM_FRAN_EMP.selectStaffInUserExt");
	}

	@Override
	public List<UserInfoExt> findStaffCustomer(Long recId) {
		return myBatisDaoRead.getList("CRM_FRAN_EMP.selectStaffCustomer",recId);
	}
	
	@Transactional
	@Override
	public String addCustomer(Long staffId, String mobile, String type) {
		String result="操作成功";
		UserInfo user=userInfoService.getUserByMobileNo(mobile);
		if(user==null){
			result="平台中无此客户";
			return result;
		}
		CrmFranEmp emp=findByUserId(user.getUserId());
		CrmFranEmp staff=findStaffById(staffId);
		if("add".equals(type)){
			if(null!=emp){
				result="员工不能被补录";
				return result;
			}
		}else{
			if(null==emp){
				result="迁移的员工不存在";
				return result;
			}else{
				if(!staff.getStatus().equals("2")){
					result="没有被禁用的员工不能迁移";
					return result;
				}
			}
		}
        CrmCustomerBus customer=null;
		if("add".equals(type)){//补录
			customer=crmCustomerBusService.selectByCondition(user.getUserId(),null);
			if(null!=customer){
				result="此客户已存在邀请关系";
				return result;
			}

			UserInfoExt ue=userInfoExtService.getUserInfoExtById(user.getUserId());
			if(ue.getIsVerified().equals("0")){
				result="此客户没有进行实名认证";
				return result;
			}

            customer=new CrmCustomerBus();
            customer.setCreateTime(new Date());
            customer.setpId(staff.getUserId());
            customer.setUserId(user.getUserId());
            crmCustomerBusService.insertCrmCustomerBus(customer);
			ue.setRecUserId(staff.getUserId());
			userInfoExtService.updateUserInfoExt(ue);
		}else{//迁移
            emp=findCrmFramEmpByMobileNo(mobile);
			if(emp==null){
				result="迁移的员工不存在";
				return result;
			}
            customer=crmCustomerBusService.selectByCondition(staff.getUserId(),emp.getUserId());
			if(null!=customer){
				result="变迁的员工关系已存在";
				return result;
			}

			Map map=new HashMap();
            customer=crmCustomerBusService.selectByCondition(staff.getUserId(),null);
            if(null==customer){
                result="非法数据操作";
                return result;
            }

            List<CrmCustomerBus> customers=crmCustomerBusService.selectByPId(staff.getUserId()); //2级变1级
            if(null!=customers&&customers.size()>0){
                for (CrmCustomerBus cb : customers) {
                    cb.setpId(emp.getUserId());
                    cb.setUpdateTime(new Date());
                    crmCustomerBusService.updateCrmCustomerBus(cb);
                    map.clear();
                    map.put("userId",cb.getUserId());
                    map.put("timeFlag","yes");
                    updateBusHis(emp.getUserId(), map, cb,1);
                    List<CrmCustomerBus> customers2=crmCustomerBusService.selectByPId(cb.getUserId());//2级
                    if(null!=customers2&&customers2.size()>0){
                        for (CrmCustomerBus cb2 : customers2) {
                            map.clear();
                            map.put("userId",cb2.getUserId());
                            map.put("timeFlag","yes");
                            updateBusHis(emp.getUserId(), map, cb2,2);
                            List<CrmCustomerBus> customers3=crmCustomerBusService.selectByPId(cb2.getUserId());//3级
                            if(null!=customers3&&customers3.size()>0){
                                for (CrmCustomerBus cb3 : customers3) {
                                    map.clear();
                                    map.put("userId",cb3.getUserId());
                                    map.put("timeFlag","yes");
                                    updateBusHis(emp.getUserId(), map, cb3,3);
                                }
                            }
                        }
                    }
                }
            }
            customer.setpId(emp.getUserId());
            customer.setUpdateTime(new Date());
            crmCustomerBusService.updateCrmCustomerBus(customer);

            staff.setStatus("4");
            updateStaff(staff);
            map.clear();
            map.put("userId",staff.getUserId());
            map.put("timeFlag","yes");
            updateBusHis(emp.getUserId(), map, customer,1);
		}
		return result;
	}

    private void updateBusHis(Long pid, Map map, CrmCustomerBus cb,Integer level) {
        List<CrmCustomerBusHis> list=crmCustomerBusHisService.selectByCondition(map);
        if(null!=list&&list.size()>0){
            CrmCustomerBusHis crmCustomerBusHis=list.get(0);
            crmCustomerBusHis.setEndTime(new Date());
            crmCustomerBusHisService.updateOrInsertHis(crmCustomerBusHis);
        }
        CrmCustomerBusHis crmCustomerBusHis=new CrmCustomerBusHis();
        crmCustomerBusHis.setpId(pid);
        crmCustomerBusHis.setStartTime(new Date());
        crmCustomerBusHis.setUserId(cb.getUserId());
        crmCustomerBusHis.setLevel(level);
        crmCustomerBusHisService.updateOrInsertHis(crmCustomerBusHis);
    }

    @Override
	public List<TransforStaff> findtransforStaff(String m) {
		List<TransforStaff> staffs=null;
		String mobile=null;
		if(!StringUtils.isNull(m)){
			mobile=m;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", mobile);
		staffs=myBatisDaoRead.getList("CRM_FRAN_EMP.selectTransforStaff",params);
		return staffs;
	}

	@Override
	public BigDecimal countUserOrderAmount(Map map) {
		return myBatisDaoRead.get("countUserOrderAmount", map);
	}

	@Override
	public BigDecimal countUserOrderDisAmount(Map map) {
		return myBatisDaoRead.get("countUserOrderDisAmount", map);
	}

	/**
	 * 根据客户ID，查询加盟商客户表条数
	 * @param customerId 客户ID
	 * @return 条数
	 */
	@Override
	public Integer getCrmFranCustomerCountByCustomerId(Long customerId) {
		return myBatisDao.get("CRM_FRAN_EMP.getCrmFranCustomerCountByCustomerId", customerId);
	}

}
