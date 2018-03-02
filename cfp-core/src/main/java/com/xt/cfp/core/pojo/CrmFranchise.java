package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 加盟商表
 *  * 名称                  空值       类型            
------------------- -------- ------------- 
FRANCHISE_ID        NOT NULL NUMBER(11)    
ADDRESS_ID                   NUMBER(11)    
FRANCHISE_STATE              CHAR(1)       
FRANCHISE_CODE               VARCHAR2(200) 
FRANCHISE_NAME               VARCHAR2(200) 
FRANCHISE_AGREECODE          VARCHAR2(100) 
STAR_LEVEL                   VARCHAR2(10)  
SIGN_DATE                    DATE          
OPEN_DATE                    DATE          
CREATE_DATE                  DATE          
CONCAT_PERSON                VARCHAR2(50)  
CONCAT_INFORMATION           VARCHAR2(50)  
LAST_UPDATE_DATE             DATE    
 * @author wangyadong
 *
 */
public class CrmFranchise {
    private Long franchiseId;//加盟商ID
    private Long addressId;//地址ID
    private String franchiseState;//加盟商状态
    private String franchiseCode;//加盟商编号v
    private String franchiseName;//加盟商名字v franchiseName

    private String franchiseAgreecode;//加盟商协议编号
    private String address;//加盟商协议编号
    private String starLevel;//星级
    private String signDate;//签约日期：
    private String openDate;//开业日期：
    private Date createDate;//创建时间

    private String concatPerson;//联系人
    private String concatInformation;//联系方式
    private Date lastUpdateDate;//最后修改时间
    private String  detailAddress;//详细地址
    
    
    private String city;
    private String pronvince;
    private String district;
    
     
    
    public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPronvince() {
		return pronvince;
	}

	public void setPronvince(String pronvince) {
		this.pronvince = pronvince;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	private Long userId;
    
    
    

   
  
    public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

//	private BigDecimal OnSaleGoal = BigDecimal.ZERO; // 折标金额
//    private BigDecimal allManagerMoney = BigDecimal.ZERO;//总投资额
//    private BigDecimal increaseByCurrentManagerMonth = BigDecimal.ZERO;//本月新增投资
//    private BigDecimal increaseByCurrentMonth ;// 本月新增客户
//    private BigDecimal allCustomer ;//总客户数 OnSaleGoal
//    
	private String onSaleGoal; // 折标金额
    private String allManagerMoney ;//总投资额
    private String increaseByCurrentManagerMonth;//本月新增投资
    private int increaseByCurrentMonth ;// 本月新增客户
    private int allCustomer ;//总客户数
    
    
    
    

     
	 

	public String getOnSaleGoal() {
		return onSaleGoal;
	}

	public void setOnSaleGoal(String onSaleGoal) {
		this.onSaleGoal = onSaleGoal;
	}

	public String getAllManagerMoney() {
		return allManagerMoney;
	}

	public void setAllManagerMoney(String allManagerMoney) {
		this.allManagerMoney = allManagerMoney;
	}

	public String getIncreaseByCurrentManagerMonth() {
		return increaseByCurrentManagerMonth;
	}

	public void setIncreaseByCurrentManagerMonth(String increaseByCurrentManagerMonth) {
		this.increaseByCurrentManagerMonth = increaseByCurrentManagerMonth;
	}

	public int getIncreaseByCurrentMonth() {
		return increaseByCurrentMonth;
	}

	public void setIncreaseByCurrentMonth(int increaseByCurrentMonth) {
		this.increaseByCurrentMonth = increaseByCurrentMonth;
	}

	public int getAllCustomer() {
		return allCustomer;
	}

	public void setAllCustomer(int allCustomer) {
		this.allCustomer = allCustomer;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public Long getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(Long franchiseId) {
        this.franchiseId = franchiseId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getFranchiseState() {
        return franchiseState;
    }

    public void setFranchiseState(String franchiseState) {
        this.franchiseState = franchiseState == null ? null : franchiseState.trim();
    }

    public String getFranchiseCode() {
        return franchiseCode;
    }

    public void setFranchiseCode(String franchiseCode) {
        this.franchiseCode = franchiseCode == null ? null : franchiseCode.trim();
    }

    public String getFranchiseName() {
        return franchiseName;
    }

    public void setFranchiseName(String franchiseName) {
        this.franchiseName = franchiseName == null ? null : franchiseName.trim();
    }

    public String getFranchiseAgreecode() {
        return franchiseAgreecode;
    }

    public void setFranchiseAgreecode(String franchiseAgreecode) {
        this.franchiseAgreecode = franchiseAgreecode == null ? null : franchiseAgreecode.trim();
    }

    public String getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(String starLevel) {
        this.starLevel = starLevel == null ? null : starLevel.trim();
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getConcatPerson() {
        return concatPerson;
    }

    public void setConcatPerson(String concatPerson) {
        this.concatPerson = concatPerson == null ? null : concatPerson.trim();
    }

    public String getConcatInformation() {
        return concatInformation;
    }

    public void setConcatInformation(String concatInformation) {
        this.concatInformation = concatInformation == null ? null : concatInformation.trim();
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}