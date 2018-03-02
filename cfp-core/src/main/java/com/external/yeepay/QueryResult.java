package com.external.yeepay;

public class QueryResult {
	
	private String r0_Cmd;       	//订单查询请求，固定值QueryOrdDetail
	private String r1_Code;		 	//查询结果,1正常，50订单不存在
	private String r2_TrxId;	 	//易宝支付交易流水号
	private String r3_Amt;		 	//支付金额
	private String r4_Cur;		 	//交易币种，固定值CNY
	private String r5_Pid;	 		//商品名称
	private String r6_Order;		//商户订单号
	private String r8_MP;			//商户扩展信息
	private String rw_RefundRequest;//退款请求号
	private String rx_CreateTime;	//订单创建时间
	private String ry_FinishTime;	//订单成功时间
	private String rz_RefundAmount;	//退款请求金额
	private String rb_PayStatus;	//支付状态 ̬INIT未支付     CANCELED已取消    SUCCESS已支付
	private String rc_RefundCount;	//已退款次数
	private String rd_RefundAmt;	//已退款金额
	private String hmac;			//签名数据
	
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
	public String getR0_Cmd() {
		return r0_Cmd;
	}
	public void setR0_Cmd(String cmd) {
		r0_Cmd = cmd;
	}
	public String getR1_Code() {
		return r1_Code;
	}
	public void setR1_Code(String code) {
		r1_Code = code;
	}
	public String getR2_TrxId() {
		return r2_TrxId;
	}
	public void setR2_TrxId(String trxId) {
		r2_TrxId = trxId;
	}
	public String getR3_Amt() {
		return r3_Amt;
	}
	public void setR3_Amt(String amt) {
		r3_Amt = amt;
	}
	public String getR4_Cur() {
		return r4_Cur;
	}
	public void setR4_Cur(String cur) {
		r4_Cur = cur;
	}
	public String getR5_Pid() {
		return r5_Pid;
	}
	public void setR5_Pid(String pid) {
		r5_Pid = pid;
	}
	public String getR6_Order() {
		return r6_Order;
	}
	public void setR6_Order(String order) {
		r6_Order = order;
	}
	public String getR8_MP() {
		return r8_MP;
	}
	public void setR8_MP(String r8_mp) {
		r8_MP = r8_mp;
	}
	public String getRb_PayStatus() {
		return rb_PayStatus;
	}
	public void setRb_PayStatus(String rb_PayStatus) {
		this.rb_PayStatus = rb_PayStatus;
	}
	public String getRc_RefundCount() {
		return rc_RefundCount;
	}
	public void setRc_RefundCount(String rc_RefundCount) {
		this.rc_RefundCount = rc_RefundCount;
	}
	public String getRd_RefundAmt() {
		return rd_RefundAmt;
	}
	public void setRd_RefundAmt(String rd_RefundAmt) {
		this.rd_RefundAmt = rd_RefundAmt;
	}
	public String getRw_RefundRequest() {
		return rw_RefundRequest;
	}
	public void setRw_RefundRequest(String rw_RefundRequest) {
		this.rw_RefundRequest = rw_RefundRequest;
	}
	public String getRx_CreateTime() {
		return rx_CreateTime;
	}
	public void setRx_CreateTime(String rx_CreateTime) {
		this.rx_CreateTime = rx_CreateTime;
	}
	public String getRy_FinishTime() {
		return ry_FinishTime;
	}
	public void setRy_FinishTime(String ry_FinishTime) {
		this.ry_FinishTime = ry_FinishTime;
	}
	public String getRz_RefundAmount() {
		return rz_RefundAmount;
	}
	public void setRz_RefundAmount(String rz_RefundAmount) {
		this.rz_RefundAmount = rz_RefundAmount;
	}
}
