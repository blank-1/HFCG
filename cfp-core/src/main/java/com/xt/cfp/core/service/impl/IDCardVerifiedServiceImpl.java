package com.xt.cfp.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xt.cfp.core.constants.IDCardCheckEnum;
import com.xt.cfp.core.dao.MyBatisDao;
import com.xt.cfp.core.pojo.IDCardVerified;
import com.xt.cfp.core.pojo.UserInfoExt;
import com.xt.cfp.core.service.IDCardVerifiedService;
import com.xt.cfp.core.service.UserInfoExtService;
import com.xt.cfp.core.util.DateUtil;
import com.xt.cfp.core.util.JsonView;
import com.xt.cfp.core.util.StringUtils;

/**
 * Created by ren yulin on 15-7-18.
 */
@Service
public class IDCardVerifiedServiceImpl implements IDCardVerifiedService {
	@Autowired
    private UserInfoExtService userInfoExtService;
    @Autowired
    private MyBatisDao myBatisDao;
    @Override
    public void insert(IDCardVerified idCardVerified) {
        myBatisDao.insert("IDCARD_VERIFIED.insert",idCardVerified);
    }

    @Override
    public void insert(String realName, String cardCode,char result) {
        IDCardVerified idCardVerified = new IDCardVerified();
        idCardVerified.setRealName(realName);
        idCardVerified.setCardCode(cardCode);
        idCardVerified.setVerifiedTime(new Date());
        idCardVerified.setVerifiedResult(result);
        insert(idCardVerified);

    }

    @Override
    public IDCardVerified findByNameIdCode(String realName, String idCode) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("realName",realName);
        map.put("cardCode",idCode);
        List<IDCardVerified> list = myBatisDao.getList("IDCARD_VERIFIED.findByNameIdCode", map);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    
    @Override
    public IDCardVerified findByIdCode( String idCode) {
        List<IDCardVerified> list = myBatisDao.getList("IDCARD_VERIFIED.findByIdCode",idCode);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    
	@Override
	public void updateIDCardVerified(IDCardVerified newVerified) {
		myBatisDao.update("IDCARD_VERIFIED.updateByPrimaryKeySelective", newVerified);
	}

	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public String updateUserVerified(long userId, String idCard, String realName) {
		IDCardVerified verified = findByIdCode( idCard);
		if (null != verified) {
			//这里的逻辑意思是:不管之前是否有同身份证号认证失败的记录,只要是从后台过来的认证请求,都按成功处理
			if (IDCardCheckEnum.SUCCESS.value2Char() != verified.getVerifiedResult()) {
				IDCardVerified newVerified = new IDCardVerified();
				newVerified.setVerifiedResult(IDCardCheckEnum.SUCCESS.value2Char());
				newVerified.setVerifiedId(verified.getVerifiedId());
				newVerified.setVerifiedTime(new Date());
				updateIDCardVerified(newVerified);
			}
		} else {
			insert(realName, idCard, IDCardCheckEnum.SUCCESS.value2Char());
		}
		
		UserInfoExt userInfoExt = new UserInfoExt();
		userInfoExt.setUserId(userId);
		userInfoExt.setIsVerified(IDCardCheckEnum.SUCCESS.getValue());
		userInfoExt.setRealName(realName);
		userInfoExt.setBirthday(DateUtil.parseStrToDate(StringUtils.getBirthdayByIdCode(idCard), "yyyy-MM-dd"));
		userInfoExt.setSex(StringUtils.getSexByByIdCode(idCard));
		userInfoExt.setIdCard(idCard);
		userInfoExtService.updateUserInfoExt(userInfoExt);

		return JsonView.JsonViewFactory.create().success(true).info("用户认证通过！").toJson();
	}
}
