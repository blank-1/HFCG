package com.xt.cfp.core.pojo.ext;

import com.alibaba.fastjson.JSONObject;
import com.xt.cfp.core.constants.RateEnum;
import com.xt.cfp.core.constants.RateEnum.RateProductStatusEnum;
import com.xt.cfp.core.pojo.RateProduct;

public class RateProductVO extends RateProduct {

	private Integer totalRateUser;// 发放张数
	private Integer usedRateUser;// 已用张数
	private RateProductConditionVO rateProductConditionVO = new RateProductConditionVO();// 加息券产品表，条件json字符串封装类
	private Integer startAmount;// 起投金额
	private String conditionDesc;// 条件简述
	private String statusStr;// 状态中文

	public Integer getTotalRateUser() {
		return totalRateUser;
	}

	public void setTotalRateUser(Integer totalRateUser) {
		this.totalRateUser = totalRateUser;
	}

	public Integer getUsedRateUser() {
		return usedRateUser;
	}

	public void setUsedRateUser(Integer usedRateUser) {
		this.usedRateUser = usedRateUser;
	}

	public RateProductConditionVO getRateProductConditionVO() {
		return rateProductConditionVO;
	}

	public void setRateProductConditionVO(RateProductConditionVO rateProductConditionVO) {
		this.rateProductConditionVO = rateProductConditionVO;
	}

	public Integer getStartAmount() {
		return startAmount;
	}

	public void setStartAmount(Integer startAmount) {
		this.startAmount = startAmount;
	}

	public String getStatusStr() {
		RateProductStatusEnum rps = RateEnum.RateProductStatusEnum.getRateProductStatusEnumByValue(getStatus());
		if (null != rps) {
			return rps.getDesc();
		}
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getConditionDesc() {

		if (null != rateProductConditionVO) {
			if (rateProductConditionVO.isCondition0_nothing()) {
				StringBuffer desc = new StringBuffer();
				// 使用条件（1.标的期限；）
				if (rateProductConditionVO.isCondition1_term()) {
					desc.append(rateProductConditionVO.getCon1_min()).append("~").append(rateProductConditionVO.getCon1_max()).append("月标；");
				}
				// 使用条件（2.标的类型；）
				if (rateProductConditionVO.isCondition2_type()) {
					if (rateProductConditionVO.isCon2_0()) {
						desc.append("信贷;");
					}
					if (rateProductConditionVO.isCon2_1()) {
						desc.append("房贷;");
					}
					if (rateProductConditionVO.isCon2_2()) {
						desc.append("企业车贷;");
					}
					if (rateProductConditionVO.isCon2_3()) {
						desc.append("企业信贷;");
					}
					if (rateProductConditionVO.isCon2_4()) {
						desc.append("企业保理;");
					}
					if (rateProductConditionVO.isCon2_5()) {
						desc.append("基金;");
					}
					if (rateProductConditionVO.isCon2_6()) {
						desc.append("企业标;");
					}
				}
				// 使用条件（3.起投金额）
				if (rateProductConditionVO.isCondition3_amount()) {
					desc.append(rateProductConditionVO.getCon3_start_amount()).append("元起投；");
				}
				conditionDesc = desc.toString();
			} else {
				conditionDesc = "无条件";
			}
		}

		return conditionDesc;
	}

	public String getCustomizeConditionDesc(boolean con1, boolean con2, boolean con3) {
		if (null != rateProductConditionVO) {
			if (rateProductConditionVO.isCondition0_nothing()) {
				StringBuffer desc = new StringBuffer();
				if (con1) {
					// 使用条件（1.标的期限；）
					if (rateProductConditionVO.isCondition1_term()) {
						desc.append(rateProductConditionVO.getCon1_min()).append("~").append(rateProductConditionVO.getCon1_max()).append("月标；");
					}
				}
				if (con2) {
					// 使用条件（2.标的类型；）
					if (rateProductConditionVO.isCondition2_type()) {
						if (rateProductConditionVO.isCon2_0()) {
							desc.append("信贷;");
						}
						if (rateProductConditionVO.isCon2_1()) {
							desc.append("房贷;");
						}
						if (rateProductConditionVO.isCon2_2()) {
							desc.append("企业车贷;");
						}
						if (rateProductConditionVO.isCon2_3()) {
							desc.append("企业信贷;");
						}
						if (rateProductConditionVO.isCon2_4()) {
							desc.append("企业保理;");
						}
						if (rateProductConditionVO.isCon2_5()) {
							desc.append("基金;");
						}
						if (rateProductConditionVO.isCon2_6()) {
							desc.append("企业标;");
						}
					}
				}
				if (con3) {
					// 使用条件（3.起投金额）
					if (rateProductConditionVO.isCondition3_amount()) {
						desc.append(rateProductConditionVO.getCon3_start_amount()).append("元起投；");
					}
				}
				conditionDesc = desc.toString();
			} else {
				conditionDesc = "无条件";
			}
		}

		return conditionDesc;
	}

	public void setConditionDesc(String conditionDesc) {
		this.conditionDesc = conditionDesc;
	}

	/**
	 * 将json字符串转换为对象
	 * 
	 * @param condition
	 * @return
	 */
	public RateProductConditionVO getRateProductConditionValue(String condition) {
		if (null != condition) {
			JSONObject json = JSONObject.parseObject(condition);

			rateProductConditionVO.setCondition0_nothing(json.getBooleanValue("condition0_nothing"));
			rateProductConditionVO.setCondition1_term(json.getBooleanValue("condition1_term"));
			rateProductConditionVO.setCondition2_type(json.getBooleanValue("condition2_type"));
			rateProductConditionVO.setCondition3_amount(json.getBooleanValue("condition3_amount"));
			rateProductConditionVO.setCon1_min(json.getInteger("con1_min"));
			rateProductConditionVO.setCon1_max(json.getInteger("con1_max"));
			rateProductConditionVO.setCon2_0(json.getBooleanValue("con2_0"));
			rateProductConditionVO.setCon2_1(json.getBooleanValue("con2_1"));
			rateProductConditionVO.setCon2_2(json.getBooleanValue("con2_2"));
			rateProductConditionVO.setCon2_3(json.getBooleanValue("con2_3"));
			rateProductConditionVO.setCon2_4(json.getBooleanValue("con2_4"));
			rateProductConditionVO.setCon2_5(json.getBooleanValue("con2_5"));
			rateProductConditionVO.setCon2_6(json.getBooleanValue("con2_6"));
			rateProductConditionVO.setCon3_start_amount(json.getInteger("con3_start_amount"));
		}

		return rateProductConditionVO;
	}

	/**
	 * 将对象转换为json字符串
	 * 
	 * @param rateProductConditionVO
	 * @return
	 */
	public String setRateProductConditionValue(RateProductConditionVO rateProductConditionVO) {
		return JSONObject.toJSONString(rateProductConditionVO);
	}

}
