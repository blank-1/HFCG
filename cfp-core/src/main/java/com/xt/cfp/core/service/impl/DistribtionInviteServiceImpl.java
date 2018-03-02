package com.xt.cfp.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xt.cfp.core.constants.DisActivityEnums;
import com.xt.cfp.core.constants.UserType;
import com.xt.cfp.core.constants.DisActivityEnums.DistributionInviteEnum;
import com.xt.cfp.core.constants.DisActivityEnums.WhiteTabsSourceEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.AdminInfo;
import com.xt.cfp.core.pojo.DistributionInvite;
import com.xt.cfp.core.pojo.InviteWhiteTabs;
import com.xt.cfp.core.pojo.UserInfo;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.pojo.WhiteTabs;
import com.xt.cfp.core.pojo.ext.Distributor;
import com.xt.cfp.core.pojo.ext.DistributorVO;
import com.xt.cfp.core.service.DistributionInviteService;
import com.xt.cfp.core.service.InviteWhiteTabsService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.service.UserInfoService;
import com.xt.cfp.core.service.WhiteTabsService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.Pagination;
import com.xt.cfp.core.util.StringUtils;

@Service
public class DistribtionInviteServiceImpl implements DistributionInviteService {

	private  Logger logger = Logger.getLogger(DistribtionInviteServiceImpl.class);
	@Autowired
	private MyBatisDao myBatisDao;
	@Autowired
	private UserInfoExtService userInfoExtService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private WhiteTabsService whiteTabsService ;
	@Autowired
	private InviteWhiteTabsService inviteWhiteTabsService ;

	/**
	 * 设置邀请关系
	 * 
	 * @author wangyadong
	 * @param currentUserId
	 * @return
	 */
	@Override
	public String settingDistribtionRelation(Long recUserId, Long userId) {
		DistributionInvite distribtionInvite = new DistributionInvite();
		// 根据父级id 查询邀请关系
		List<DistributionInvite> distribtionInviteList = myBatisDao.getList("selectByPrimaryKeyForUserId", recUserId);
		//首先插入一条邀请人和被邀请人的关系记录
		if (recUserId == null){
			logger.info("邀请人ID为空，不插入邀请关系");
			return null;
		}
		UserInfoExt uie = myBatisDao.get(
				"USER_INFO_EXT.selectByPrimaryKey", recUserId);
		if (uie != null) {
			distribtionInvite.setDisLevel("1");
			distribtionInvite.setUserPid(recUserId);
			distribtionInvite.setUserId(userId);
		}
		insert(distribtionInvite);
		logger.info("插入当前注册用户的邀请人和注册用户的邀请关系");
		//当邀请人关系存在邀请关系表数据时，查询出邀请人的1、2级关系数据，再此基础上加1，结果就是对应新邀请人的关系等级
		if (distribtionInviteList != null) {
			for(DistributionInvite invite : distribtionInviteList){
				if(!StringUtils.isNull(invite.getDisLevel()) && Integer.valueOf(invite.getDisLevel()) < 3){
					String newLevel = String.valueOf(Integer.valueOf(invite.getDisLevel()) + 1);
					distribtionInvite = new DistributionInvite(); 
					distribtionInvite.setDisLevel(newLevel);// 2-3
					distribtionInvite.setUserPid(invite.getUserPid());
					distribtionInvite.setUserId(userId);
					insert(distribtionInvite);
					logger.info("插入当前注册用户邀请人的邀请关系人和注册用户的邀请关系，邀请关系级别：" + newLevel);
				}
			}
			// 新增用户为二级，相对于邀请人而言是一级，邀请人为根
//			String disLevel = distribtionInviteFirst.getDisLevel();
//
//			DistributionInvite distribtionInviteSecond = new DistributionInvite();
//			if (disLevel != null && !"".equals(disLevel)) {
//				if ("2".equals(disLevel)) {// 二级
//					distribtionInvite.setDisLevel("1");// 2-3
//					distribtionInvite.setUserPid(distribtionInviteFirst.getUserId());
//					distribtionInvite.setUserId(userId);
//					
//					// 2-1
//					insert(distribtionInvite);
//					distribtionInviteSecond.setDisLevel("2");
//					distribtionInviteSecond.setUserPid(distribtionInviteFirst.getUserPid());
//					distribtionInviteSecond.setUserId(userId);
//					insert(distribtionInviteSecond);
//					//存储1-3 
//					UserInfoExt uie2 =myBatisDao.get("USER_INFO_EXT.selectByPrimaryKey", distribtionInviteFirst.getUserPid());	
//					distribtionInviteSecond.setDisLevel("3");
//					distribtionInviteSecond.setUserPid(uie2.getRecUserId());
//					distribtionInviteSecond.setUserId(userId);
//					insert(distribtionInviteSecond);
//					
//					
//				}
//				if ("1".equals(disLevel)) {// 一级
//					distribtionInvite.setDisLevel("2");
//					distribtionInvite.setUserPid(distribtionInviteFirst.getUserId());
//					distribtionInvite.setUserId(userId);
//					insert(distribtionInvite);
//				}
//				if ("3".equals(disLevel)) {// 一级
//					distribtionInvite.setDisLevel("1");// 3-1 3-2
//					distribtionInvite.setUserPid(distribtionInviteFirst.getUserId());
//					distribtionInvite.setUserId(userId);
//					insert(distribtionInvite);
//					
//					distribtionInviteSecond.setDisLevel("2");
//					distribtionInviteSecond.setUserPid(distribtionInviteFirst.getUserPid());
//					distribtionInviteSecond.setUserId(userId);
//					insert(distribtionInviteSecond);
//					
//					UserInfoExt uie3 =myBatisDao.get("USER_INFO_EXT.selectByPrimaryKey", distribtionInviteFirst.getUserPid());	
//					distribtionInviteSecond.setDisLevel("3");
//					distribtionInviteSecond.setUserPid(uie3.getRecUserId());
//					distribtionInviteSecond.setUserId(userId);
//					insert(distribtionInviteSecond);
//					

//				}
//			}

		} 
		//如果邀请人是【参与佣金白名单（WhiteTabs）】内，那么被邀请人也要增加进参与佣金白名单
		int count = whiteTabsService.countUserId(recUserId);
		if(count > 0){
			WhiteTabs whitetab = new WhiteTabs();
			whitetab.setSource(WhiteTabsSourceEnum.SOURCE_INVITE.getValue());
			whitetab.setUserId(userId);
			whiteTabsService.insert(whitetab);
		}
		
		//如果邀请人在【邀请白名单InviteWhiteTabs】内，那么被邀请人也要增加进参与佣金白名单和邀请白名单
		int count1 = inviteWhiteTabsService.countUserId(recUserId);
		if(count1 > 0){
			int count2 = whiteTabsService.countUserId(userId);
			if(count2<1){
				WhiteTabs whitetab = new WhiteTabs();
				whitetab.setSource(WhiteTabsSourceEnum.SOURCE_INVITE.getValue());
				whitetab.setUserId(userId);
				whiteTabsService.insert(whitetab);
			}
			InviteWhiteTabs inviteWhitetab = new InviteWhiteTabs();
			inviteWhitetab.setSource(WhiteTabsSourceEnum.SOURCE_INVITE.getValue());
			inviteWhitetab.setUserId(userId);
			inviteWhitetab.setType(DisActivityEnums.InviteWhiteTabsTypeEnum.TYPE_IMPORT.getValue());
			inviteWhiteTabsService.insert(inviteWhitetab);
		}
		
		return null;
	}

	@Override
	public Pagination<Distributor> findAllByPage(int pageNo, int pageSize, Map<String, Object> params) {
		Pagination<Distributor> re = new Pagination<Distributor>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        int totalCount = this.myBatisDao.count("findAllDistributionByPage", params);
        List<DistributionInvite> friends = this.myBatisDao.getListForPaging("findAllDistributionByPage", params, pageNo, pageSize);
        List<Distributor>  distributors=new ArrayList<Distributor>();
        for (DistributionInvite friend : friends) {
        	Distributor distributor=new Distributor();
        	distributor.setDistributionInvite(friend);
        	UserInfo ui=userInfoService.getUserByUserId(friend.getUserId());
        	try {
				distributor.setCreateTime(DateUtil.getPlusTime(ui.getCreateTime()));
			} catch (Exception e) {
				e.printStackTrace();
			}
        	String name=ui.getLoginName();
        	if(!distributor.getDistributionInvite().getDisLevel().equals("1")){
        		if(!StringUtils.isNull(name)){
        			name=name.substring(0, 2)+"****"+name.substring(name.length()-2,name.length());
        		}
        	}
        	distributor.setName(name);
        	distributors.add(distributor);
		}
        re.setTotal(totalCount);
        re.setRows(distributors);
        return re;
	}

	public List<UserInfoExt> trunMainByUserInfo(List<UserInfoExt> mainByUserInfo) {
		try {
			if (mainByUserInfo != null && mainByUserInfo.size() > 0) {
				Map<String, List<UserInfoExt>> setUserInfoExtByMap = setUserInfoExtByMap(mainByUserInfo, "");
				if (setUserInfoExtByMap != null && setUserInfoExtByMap.get("1") != null) {// 不等于空说明可以
																							// 继续插入
					insertUserInFoDataBase(setUserInfoExtByMap.get("1"), setUserInfoExtByMap.get("2"), setUserInfoExtByMap.get("3"));
				}// ‘等于null就是查到头了
				if (setUserInfoExtByMap.get("2") == null)
					return null;
				return trunMainByUserInfo(setUserInfoExtByMap.get("2"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 执行数据 初始化 数据
	 * 
	 * @author wangyadong
	 */
	public List<UserInfoExt> mainByUserInfo() {
		List<UserInfoExt> user = myBatisDao.getList("getUserExtForIsNull", "");// 根节点
		return user;
	}

	/**
	 * 循环数据分批次插入质数据库 只执行循环和插入 查询方法在上一层中执行
	 * 
	 * @author wangyadong
	 * @param setUserInfoExt2
	 * @param setUserInfoExt3
	 * @param setUserInfoExt4
	 */
	public void insertUserInFoDataBase(List<UserInfoExt> setUserInfoExt2, List<UserInfoExt> setUserInfoExt3, List<UserInfoExt> setUserInfoExt4) {
		// 一级数据 0-1
		DistributionInvite di1 = new DistributionInvite();
		for (int i = 0; i < setUserInfoExt2.size(); i++) {
			Long recUserId = setUserInfoExt2.get(i).getRecUserId();
			Long userId = setUserInfoExt2.get(i).getUserId();
			String level = "1";
			di1.setDisLevel(level);
			// di1.setInviteDate(new Date());
			di1.setUserId(userId);
			di1.setUserPid(recUserId);
			insert(di1);
		}
		DistributionInvite di2 = new DistributionInvite();
		// 二级数据 1-2
		for (int i = 0; i < setUserInfoExt3.size(); i++) {
			Long recUserId = setUserInfoExt3.get(i).getRecUserId();
			Long userId = setUserInfoExt3.get(i).getUserId();
			String level = "2";
			di2.setDisLevel(level);
			// di2.setInviteDate(new Date());
			di2.setUserId(userId);
			di2.setUserPid(recUserId);
			insert(di2);
		}
		DistributionInvite di3 = new DistributionInvite();

		DistributionInvite d4 = new DistributionInvite();
		// 三级数据1-3
		for (int i = 0; i < setUserInfoExt4.size(); i++) {
			Long recUserId = setUserInfoExt3.get(i).getRecUserId();
			Long userId = setUserInfoExt3.get(i).getUserId();
			String level = "3";
			di3.setDisLevel(level);
			// di3.setInviteDate(new Date());
			di3.setUserId(userId);
			UserInfoExt userInfo = myBatisDao.get("USER_INFO_EXT.selectByPrimaryKey", recUserId);
			di3.setUserPid(userInfo.getRecUserId());
			insert(di3);
			// 1-3
			// 查询出的他父亲的父级 也就是他的recId 的父级
			// UserInfoExt userInfo =
			// myBatisDao.get("USER_INFO_EXT.selectByPrimaryKeyByRec",
			// recUserId);
			// d4.setUserId(userId);
			// d4.setUserPid(userInfo.getRecUserId());//
			// setUserInfoExt3.get(i).getSex();
			// d4.setDisLevel("");
			// d4.setInviteDate(new Date());
			// 存储本身的加爷爷级别的数据
			// d4.setUserPid(userInfo.getUserId());
			// d4.set
			// setUserInfoExt3.get(i).setAvatar(userInfo.getUserId());
			// 分别插入
		}
	}

	/**
	 * 查询数据
	 * 
	 * @author wangyadong
	 * @param user
	 * @return
	 */
	public List<UserInfoExt> setUserInfoExt(List<UserInfoExt> user, String level) {
		List<UserInfoExt> userInfoList = new ArrayList<UserInfoExt>();
		for (int i = 0; i < user.size(); i++) {
			// 一级数据
			UserInfoExt userInfoExt = user.get(i);
			Long userId = userInfoExt.getUserId();
			List<UserInfoExt> userInfo = myBatisDao.getList("USER_INFO_EXT.selectByPrimaryKeyByRec", userId);
			// userInfo.setSex(level);
			if (userInfo != null && userInfo.size() > 0)
				userInfoList.addAll(userInfo);
		}
		return userInfoList;
	}

	/**
	 * @author wangyadong 返回三级map
	 * @param user
	 * @return
	 */
	public Map<String, List<UserInfoExt>> setUserInfoExtByMap(List<UserInfoExt> user, String level) {
		Map<String, List<UserInfoExt>> map = new HashMap<String, List<UserInfoExt>>();
		List<UserInfoExt> setUserInfoExt1 = setUserInfoExt(user, null);
		if (setUserInfoExt1 != null)
			map.put("1", setUserInfoExt1);
		List<UserInfoExt> setUserInfoExt2 = setUserInfoExt(setUserInfoExt1, null);
		if (setUserInfoExt2 != null)
			map.put("2", setUserInfoExt2);
		List<UserInfoExt> setUserInfoExt3 = setUserInfoExt(setUserInfoExt2, null);
		if (setUserInfoExt3 != null)
			map.put("3", setUserInfoExt3);
		return map;
	}

	/**
	 * @author wangyadong 新 增方法
	 */
	public void insert(DistributionInvite di) {
		//
		myBatisDao.insert("DISTRIBUTION_INVITE.insertSelective", di);

	}

	@Override
	public Integer countUserCustomerByUserId(Long userId) {
		return myBatisDao.get("DISTRIBUTION_INVITE.countUserCustomerByUserId", userId);
	}

	/**
	 * 根据被邀请人ID，查询分销关系列表
	 * @param userId 被邀请人ID
	 * @return 分销关系列表
	 */
	@Override
	public List<DistributionInvite> getDistributionInviteByUserId(Long userId, boolean isWhiteTabs) {
		if(isWhiteTabs){
			return myBatisDao.getList("DISTRIBUTION_INVITE.getDistributionInviteInWhiteTabsByUserId", userId);
		}
		return myBatisDao.getList("DISTRIBUTION_INVITE.getDistributionInviteByUserId", userId);
	}
	
	/**
	 * 工具主方法
	 * @author wangyadong
	 */
		@Override
		public void settingRelationByInvative() {
			List<UserInfoExt> mainByUserInfo = mainByUserInfo();//返回
			for (int i = 0; i < mainByUserInfo.size(); i++) {
				trunMainByUserInfoSign(mainByUserInfo.get(i),"");
			}
		}
		
		
		
		private void trunMainByUserInfoSign(UserInfoExt userInfoExt, String string) {
		// TODO Auto-generated method stub
			DistributionInvite  di1 = null;
				//查询yi级
				List<UserInfoExt>  userInfo2 = myBatisDao.getList("USER_INFO_EXT.selectByPrimaryKeyByRec", userInfoExt.getUserId());
//			 if(!"".equals(string)){
//				 settingDistribtionRelation(userInfoExt.getRecUserId(),userInfoExt.getUserId());
//			 }
				if(userInfo2!=null&&userInfo2.size()>0){
				
					di1=	new DistributionInvite();
					for (int j = 0; j < userInfo2.size(); j++) {
						di1.setDisLevel("1");
						di1.setUserPid(userInfo2.get(j).getRecUserId());
						di1.setUserId(userInfo2.get(j).getUserId());
						insert(di1);
						//er级
						List<UserInfoExt>  userInfo3 = myBatisDao.getList("USER_INFO_EXT.selectByPrimaryKeyByRec", userInfo2.get(j).getUserId());
						for (int k = 0; k < userInfo3.size(); k++) {
							di1=	new DistributionInvite();
							di1.setDisLevel("2");
							di1.setUserPid(userInfoExt.getUserId());
							di1.setUserId(userInfo3.get(k).getUserId());
							insert(di1);
//							di1.setDisLevel("1");
//							di1.setUserPid(userInfo2.get(j).getUserId());
//							di1.setUserId(userInfo3.get(k).getUserId());
//							insert(di1);
							//san级
							List<UserInfoExt>  userInfo4 = myBatisDao.getList("USER_INFO_EXT.selectByPrimaryKeyByRec", userInfo3.get(k).getUserId());
							for (int l = 0; l< userInfo4.size(); l++) {
								di1=	new DistributionInvite();
								di1.setDisLevel("3");
//								 UserInfoExt userInfo = myBatisDao.get("USER_INFO_EXT.selectByPrimaryKey", userInfo4.get(l).getRecUserId());
								di1.setUserPid(userInfoExt.getUserId());
								di1.setUserId(userInfo4.get(l).getUserId());
								insert(di1);
								
//								di1.setDisLevel("2");
//								di1.setUserPid(userInfo2.get(j).getUserId());
//								di1.setUserId(userInfo4.get(l).getUserId());
//								insert(di1);
//								
//								di1.setDisLevel("1");
//								di1.setUserPid(userInfo3.get(k).getUserId());
//								di1.setUserId(userInfo4.get(l).getUserId());
//								insert(di1);
								
//								trunMainByUserInfoSign(userInfo4.get(l),"12");
							}
							
							
							}
						}
						} 
				}

	@Override
	public Pagination<DistributorVO> findAllDistributionCustomerByPage(int pageNo, int pageSize, Map<String, Object> params) {
		Pagination<DistributorVO> re = new Pagination<DistributorVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        int totalCount = this.myBatisDao.count("findAllDistributionCustomerByPage", params);
        List<DistributorVO> customers = this.myBatisDao.getListForPaging("findAllDistributionCustomerByPage", params, pageNo, pageSize);
        for (DistributorVO distributorVO : customers) {
			if(!StringUtils.isNull(distributorVO.getIdCard())){
				String card=distributorVO.getIdCard();
				card=card.substring(0,4)+"**********"+card.substring(card.length()-4);
				distributorVO.setIdCard(card);
			}
		}
        re.setTotal(totalCount);
        re.setRows(customers);
        return re;
	}

	@Override
	public DistributorVO findDistributionCustomerDetailByUserId(Long userId) {
		DistributorVO vo=myBatisDao.get("findDistributionCustomerDetailByUserId", userId);
		if(StringUtils.isNull(vo.getStatus())){
			vo.setStatus("-1");
		}
		if(!StringUtils.isNull(vo.getIdCard())){
			String card=vo.getIdCard();
			card=card.substring(0,4)+"**********"+card.substring(card.length()-4);
			vo.setIdCard(card);
		}
		if(!StringUtils.isNull(vo.getStatus())){
			if(vo.getStatus().equals("0")){
				vo.setStatus("正常");
			}else if(vo.getStatus().equals("1")){
				vo.setStatus("冻结");
			}else if(vo.getStatus().equals("2")){
				vo.setStatus("禁用");
			}
		}
		vo.setBirthStr(DateUtil.getDateLong(vo.getBirthDay()));
		return vo;
	}

	@Override
	public Pagination<DistributorVO> findUserCustomersByPage(int pageNo,int pageSize, Map<String, Object> params) {
		Pagination<DistributorVO> re = new Pagination<DistributorVO>();
        re.setCurrentPage(pageNo);
        re.setPageSize(pageSize);

        int totalCount = this.myBatisDao.count("findUserCustomersByPage", params);
        List<DistributorVO> customers = this.myBatisDao.getListForPaging("findUserCustomersByPage", params, pageNo, pageSize);
        
        re.setTotal(totalCount);
        re.setRows(customers);
        return re;
	}



		@Override
		public DistributionInvite addDistribtionInviten(DistributionInvite distribtionInvite) {
			// TODO Auto-generated method stub
			return null;
		}





		@Override
		public DistributionInvite getAdminById(Long distribtionInviteId) {
			// TODO Auto-generated method stub
			return null;
		}





		@Override
		public AdminInfo editAdmin(DistributionInvite distribtionInvite) {
			// TODO Auto-generated method stub
			return null;
		}





		@Override
		public void deleteDistribtionInvite(Long distribtionInviteId) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public List<Long> getUserIdByUserPids(List<Long> userPidList) {
			Map<String,List<Long>> paramMap = new HashMap<>();
			paramMap.put("userPidList", userPidList);
			return myBatisDao.getList("DISTRIBUTION_INVITE.getUserIdByUserPids",paramMap);
		}

		@Override
		public Map<Long, Object> findUserAllInfoByMobiles(
				List importWhiteTabsMobiles) {
			Map<Long, Object>  rsMap = new HashMap<>();
			List<UserInfo> userList = userInfoService.getUserInfoByMobileNosAndType(importWhiteTabsMobiles, UserType.COMMON.getValue());
			for(UserInfo ui : userList){
				rsMap.put(ui.getUserId(), ui);
			}
			return rsMap;
		}
		
}
