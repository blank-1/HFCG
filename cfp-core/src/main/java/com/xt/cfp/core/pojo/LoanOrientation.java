package com.xt.cfp.core.pojo;

public class LoanOrientation {
    private Long loanApplicationId;//定向标ID

    private Long oType;//定向类型 	1为定向密码  2为定向用户

    private String oPass;//定向密码

    private Long oUserid;//定向用户ID

    private String oCode;//定向标的编号
    
    private String oPassVo;//明文定向密码
    
    private String userName ;//用户名
    private String logName;//登录名
    private String phone;//手机号
    
    

  

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getoPassVo() {
		return oPassVo;
	}

	public void setoPassVo(String oPassVo) {
		this.oPassVo = oPassVo;
	}

	public String getoCode() {
		return oCode;
	}

	public void setoCode(String oCode) {
		this.oCode = oCode;
	}

	public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public Long getoType() {
        return oType;
    }

    public void setoType(Long oType) {
        this.oType = oType;
    }

    public String getoPass() {
        return oPass;
    }

    public void setoPass(String oPass) {
        this.oPass = oPass == null ? null : oPass.trim();
    }

    public Long getoUserid() {
        return oUserid;
    }

    public void setoUserid(Long oUserid) {
        this.oUserid = oUserid;
    }
 

    
}