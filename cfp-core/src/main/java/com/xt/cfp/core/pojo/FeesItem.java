package com.xt.cfp.core.pojo;

import com.xt.cfp.core.constants.FeesItemTypeEnum;
import com.xt.cfp.core.constants.FeesPointEnum;
import com.xt.cfp.core.constants.RadiceTypeEnum;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 费用项目
 */
public class FeesItem {
	
    protected long feesItemId;//费用项目ID
    protected char itemKind;//费用分类(债权/产品)
    protected String itemType;//费用类别
    protected String itemName;//项目名称
    protected float feesRate;//收费比例
    protected String itemDesc;//费用描述
    protected int radicesType;//基数
    protected String radiceName;//自定义基数名称
    protected String radiceLogic;//自定义基数逻辑
    protected char itemState;//费用项目状态
    protected Date createTime;//录入时间
    protected String radiceTypeStr;

    public String getRadiceTypeStr() {
        return radiceTypeStr;
    }

    public void setRadiceTypeStr(String radiceTypeStr) {
        this.radiceTypeStr = radiceTypeStr;
    }

    /**
     * 辅助字段
     */
    protected String constantName;//处理列表基数显示的属性
    protected int parentConstant;//处理费用和子费用类别的属性
    protected char lendOrProductState;//处理发布或者产品状态的属性
    protected String itemStateAll;//费用项目状态为全部时，做的查询
    
    private String showTypeContent;
    
    /**
     * 基数类型：1为本金，2为当期利息，3为全部利息，4为当期本金+利息，5为全部本金+利息，6为当前收益，7为全部收益, 8为当期本金
     */
    public static final int RADICESTYPE_PRINCIPAL = 1;
    public static final int RADICESTYPE_CURRENTINTEREST = 2;
    public static final int RADICESTYPE_ALLINTEREST = 3;
    public static final int RADICESTYPE_CURRENTPI = 4;
    public static final int RADICESTYPE_ALLPI = 5;
    public static final int RADICESTYPE_SUMPROFIT = 6;
    public static final int RADICESTYPE_ALLPROFIT = 7;
    public static final int RADICESTYPE_CURRENTPRINCIPAL = 8;

    public String getRadiceTypeStr(int radicesType) {
        return RadiceTypeEnum.getDescByValue(String.valueOf(radicesType));
    }

    /**
     * 费用类别，1为咨询费，2为审核费，3为服务费，4为提前还款，5为逾期，6为罚息，7为平台费，8为溢出费
     */
    public static final String ITEMTYPE_CONSULT = "1";
    public static final String ITEMTYPE_AUDITING = "2";
    public static final String ITEMTYPE_SERVICE = "3";
    public static final String ITEMTYPE_EARLYREPAYMENT = "4";
    public static final String ITEMTYPE_DELAYREPAYMENT = "5";
    public static final String ITEMTYPE_DEFAULTINTREST = "6";
    public static final String ITEMTYPE_WORKFLOW = "7";
    public static final String ITEMTYPE_OVERFLOW = "8";
    
    public String getItemTypeStr(String itemType) {
        return FeesItemTypeEnum.getDescByValue(itemType);
    }
    
    /**
     * 收取时机：1为放款收取，2为还款周期收取，3为到期收取，4为提前还款，5为提前退出，6为逾期，7首日逾期，8周期返息,9溢出
     */
    public static final char FEESCYCLE_ATMAKELOAN = '1';
    public static final char FEESCYCLE_ATCYCLE = '2';
    public static final char FEESCYCLE_ATCOMPLETE = '3';
    public static final char FEESCYCLE_ATEARLYREPAYMENT = '4';
    public static final char FEESCYCLE_ATEARLYBEQUIT = '5';
    public static final char FEESCYCLE_ATDELAY = '6';
    public static final char FEESCYCLE_ATDELAY_FIRSTDAY = '7';
    public static final char FEESCYCLE_CYCLE_BACK_INTEREST = '8';
    public static final char FEESCYCLE_ATOVERFLOW = '9';
    
    public String getChargeCycleStr(char chargeCycle) {
        return FeesPointEnum.getDescByValue(String.valueOf(chargeCycle));
    }

    /**
     * 费用分类：1为借款，2为出借
     */
    public static final char ITEMKIND_LOAN = '1';
    public static final char ITEMKIND_LEND = '2';

    /**
     * 费用父类型，1为费用，2为违约
     */
    public static final char PARENTITEMTYPE_FEES = '1';
    public static final char PARENTITEMTYPE_BREACH = '2';

    /**
     * 状态：0为启用，1为禁用
     */
    public static final char ITEMSTATE_ENABLED = '0';
    public static final char ITEMSTATE_DISABLED = '1';

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getItemStateAll() {
        return itemStateAll;
    }

    public void setItemStateAll(String itemStateAll) {
        this.itemStateAll = itemStateAll;
    }
    
    public long getFeesItemId() {
        return feesItemId;
    }

    public void setFeesItemId(long feesItemId) {
        this.feesItemId = feesItemId;
    }

    public char getItemKind() {
        return itemKind;
    }

    public void setItemKind(char itemKind) {
        this.itemKind = itemKind;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getFeesRate() {
        return feesRate;
    }

    public void setFeesRate(float feesRate) {
        this.feesRate = feesRate;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public int getRadicesType() {
        return radicesType;
    }

    public void setRadicesType(int radicesType) {
        this.radicesType = radicesType;
        this.radiceTypeStr = RadiceTypeEnum.getDescByValue(String.valueOf(radicesType));
    }

    public String getRadiceName() {
        return radiceName;
    }

    public void setRadiceName(String radiceName) {
        this.radiceName = radiceName;
    }

    public String getRadiceLogic() {
        return radiceLogic;
    }

    public void setRadiceLogic(String radiceLogic) {
        this.radiceLogic = radiceLogic;
    }

    public char getItemState() {
        return itemState;
    }

    public void setItemState(char itemState) {
        this.itemState = itemState;
    }

    public String getConstantName() {
        return constantName;
    }

    public void setConstantName(String constantName) {
        this.constantName = constantName;
    }

    public int getParentConstant() {
        return parentConstant;
    }

    public void setParentConstant(int parentConstant) {
        this.parentConstant = parentConstant;
    }

    public char getLendOrProductState() {
        return lendOrProductState;
    }

    public void setLendOrProductState(char lendOrProductState) {
        this.lendOrProductState = lendOrProductState;
    }

	public String getShowTypeContent() {
		StringBuffer sb=new StringBuffer();
		String num=deleteZero(feesRate);
		sb.append(num).append("%(");
		switch(radicesType){
			case 1:
				sb.append("本金");
				break;
			case 2:
				sb.append("当期利息");
				break;
			case 3:
				sb.append("全部利息");
				break;
			case 4:
				sb.append("当期本金+利息");
				break;
			case 5:
				sb.append("全部本金+利息");
				break;
			case 6:
				sb.append("当前收益");
				break;
			case 7:
				sb.append("全部收益");
				break;
			case 8:
				sb.append("当期本金");
				break;
		}
		sb.append(")");
		return sb.toString();
	}

	public void setShowTypeContent(String showTypeContent) {
		this.showTypeContent = showTypeContent;
	}
	
	private String deleteZero(Number number){
		if(new BigDecimal(number+"").compareTo(new BigDecimal("0"))==0){
			return "0";
		}
		String num=new BigDecimal(number+"").stripTrailingZeros().toPlainString();
		return num;
	}
	
}
