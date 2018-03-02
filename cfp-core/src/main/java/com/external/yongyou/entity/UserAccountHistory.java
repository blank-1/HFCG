package com.external.yongyou.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserAccountHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//自增id主键
	public BigDecimal autoid ;
	//身份证号
	public String uid ;
	//姓名
	public String uname ;
	//手机
	public String mphone ;
	//交易日期
	public Date ddate ;
	//类型（收入/支出）
	public String ctype ;
	//交易金额
	public BigDecimal je ;
	//摘要信息
	public String cmemo ;
	//结算方式（银行/第三方支付等）
	public String jsfs ;
	//结算方式对应的账户信息
	public String jszh ;
	//费用类型：收入、手续费或其他
	public String fylx ;
	//默认值 ：  -1
	public BigDecimal u8id ;
	
	public String billcode ;
	
	public String ztid ;
	
	public String u8jsfs ;
	
	public String u8jscode ;
	
	public String u8dfcode ;
	
	public BigDecimal u8itype ;
	
	public BigDecimal lsid ;

	/**
	 * select传值
	*/
	public BigDecimal userid ;
	public String changetype ; 
	public String fromtype ; 
	public String paychannel ; 
	
	
	public BigDecimal getAutoid() {
		return autoid;
	}

	public void setAutoid(BigDecimal autoid) {
		this.autoid = autoid;
	}

	public String getUid() {
		this.setUid(uid);
		return uid;
	}

	public void setUid(String uid) {
//		this.uid = (uid!=null && !uid.equals("") ? uid : "undefined");
		this.uid = uid ;
	}

	public String getUname() {
		this.setUname(uname);
		return uname;
	}

	public void setUname(String uname) {
//		this.uname = ( uname!=null  && !uname.equals("") ? uname : "未知用户");
		this.uname = uname ;
	}

	public String getMphone() {
		return mphone;
	}

	public void setMphone(String mphone) {
		this.mphone = mphone;
	}

	public Date getDdate() {
		return ddate;
	}

	public void setDdate(Date ddate) {
		this.ddate = ddate;
	}

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	public BigDecimal getJe() {
		return je;
	}

	public void setJe(BigDecimal je) {
		this.je = je;
	}

	public String getCmemo() {
		return cmemo;
	}

	public void setCmemo(String cmemo) {
		this.cmemo = cmemo;
	}

	public String getJsfs() {
		return jsfs;
	}

	public void setJsfs(String jsfs) {
		this.jsfs = jsfs;
	}

	public String getJszh() {
		return jszh;
	}

	public void setJszh(String jszh) {
		this.jszh = jszh;
	}

	public String getFylx() {
		return fylx;
	}

	public void setFylx(String fylx) {
		this.fylx = fylx;
	}

	public BigDecimal getU8id() {
		this.setU8id(u8id);
		return u8id;
	}

	public void setU8id(BigDecimal u8id) {
		this.u8id = (u8id!=null?u8id:new BigDecimal(-1));
	}

	public String getBillcode() {
		return billcode;
	}

	public void setBillcode(String billcode) {
		this.billcode = billcode;
	}

	public String getZtid() {
		return ztid;
	}

	public void setZtid(String ztid) {
		this.ztid = ztid;
	}

	public String getU8jsfs() {
		return u8jsfs;
	}

	public void setU8jsfs(String u8jsfs) {
		this.u8jsfs = u8jsfs;
	}

	public String getU8jscode() {
		return u8jscode;
	}

	public void setU8jscode(String u8jscode) {
		this.u8jscode = u8jscode;
	}

	public String getU8dfcode() {
		return u8dfcode;
	}

	public void setU8dfcode(String u8dfcode) {
		this.u8dfcode = u8dfcode;
	}

	public BigDecimal getU8itype() {
		return u8itype;
	}

	public void setU8itype(BigDecimal u8itype) {
		this.u8itype = u8itype;
	}

	public BigDecimal getUserid() {
		return userid;
	}

	public void setUserid(BigDecimal userid) {
		this.userid = userid;
	}

	public String getChangetype() {
		return changetype;
	}

	public void setChangetype(String changetype) {
		this.changetype = changetype;
	}

	public String getFromtype() {
		return fromtype;
	}

	public void setFromtype(String fromtype) {
		this.fromtype = fromtype;
	}

	public String getPaychannel() {
		return paychannel;
	}

	public void setPaychannel(String paychannel) {
		this.paychannel = paychannel;
	}

	public BigDecimal getLsid() {
		return lsid;
	}

	public void setLsid(BigDecimal lsid) {
		this.lsid = lsid;
	}
	
}
