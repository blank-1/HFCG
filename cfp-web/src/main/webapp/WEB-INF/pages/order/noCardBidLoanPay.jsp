<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta name="keywords" content="" />
		<meta name="description" content="" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
		<title>订单支付 - 财富派</title>
		<%@include file="../common/common_js.jsp"%>
		<script type="text/javascript" src="${ctx}/js/pay.js"></script><!-- pay javascript -->
		<script type="text/javascript" src="${ctx}/js/llbankinfo.js"></script><!-- public javascript -->
		<link rel="stylesheet" type="text/css" href="${ctx}/css/cxm_index.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/css/dingdan.css" />
	</head>
 
<body>
<% session.removeAttribute("smsCode");%>
<header>
  <!-- navtopbg start-->
  <!-- line2 start -->
  <%@include file="../common/headLine1.jsp"%>
  <%@include file="../login/login.jsp"%>
  <!-- line2 start -->
  
  <script type="text/javascript" >
	$(function(){
		var ifIep=$("#ieshowpay");
		var Sys = {};
		var ua = navigator.userAgent.toLowerCase();
		var s;
		(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
		(s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
		(s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
		(s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
		(s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
		
		//以下进行测试
		if (Sys.ie ){
			if(parseInt(Sys.ie)>=8){
				
				ifIep.hide();
			}else{
				
				ifIep.show();
			}
				
		}else if (Sys.firefox){
			ifIep.show();
		}else if (Sys.chrome){
			ifIep.show();
		}else if (Sys.opera){
			ifIep.show();
		}else if (Sys.safari){
			ifIep.show();
		}else{
			ifIep.show();
		}
})
</script>

</header>
<customUI:headLine action="2" />
<div class="clear"></div>
<!-- article start -->
<div class="container clearFloat">
	<h2 class="dd-h2 dd2-h2 pt-10">订单支付</h2>
	<div class="paytitle mt-25">
		<div class="dd-jr clearFloat">
			<div class="dd-jr-l">
				<h3>${order.lendOrderName}</h3>
				<p>订单号码：${order.orderCode}</p>
			</div>
			<div class="dd-jr-r">
				<h4>支付金额：<b id="buyBalance"><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></b>元</h4>
				<p>创建时间：<fmt:formatDate value="${order.buyTime}" pattern="yyyy-MM-dd hh:mm:ss"/> </p>
			</div>
		</div>
    
	    <ul class="dd-ul clearFloat">
	      <li>
	      	<h5>预期年化收益</h5>
	      	<p><fmt:formatNumber value="${order.profitRate}" pattern="#,##0.00"/>%
		      	<c:if test="${not empty loanApplication.rewardsPercent && loanApplication.rewardsPercent gt 0}">
		      		<c:if test="${isRights}"><!-- 债权转让需要判断奖励 -->
		      			<c:if test="${loanApplication.awardPoint != null && loanApplication.awardPoint != 1}">
		      				+<span>${loanApplication.rewardsPercent}%</span>
		      			</c:if>
		      		</c:if>
		      		<c:if test="${!isRights}">
		      			+<span>${loanApplication.rewardsPercent}%</span>
		      		</c:if>
		      	</c:if>
	      	</p>
	      </li>
	      <li>借款期限<br /><i>${order.timeLimit}</i>
	          <c:if test="${order.timeLimitType eq '1'}">天</c:if>
	          <c:if test="${order.timeLimitType eq '2'}">个月</c:if>
	      </li>
	      <li>购买金额<br /><i id="balance"><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></i>元</li>
	      <li>预期收益<br />
	      	<i><fmt:formatNumber value="${expectedInteresting}" pattern="#,##0.00"/></i>
	      	<c:if test="${not empty awardProfit && awardProfit gt 0}">
	      		<c:if test="${isRights}"><!-- 债权转让需要判断奖励 -->
	      			<c:if test="${loanApplication.awardPoint != null && loanApplication.awardPoint != 1}">
	      				<i style="color: #FE2A4D;">+${awardProfit}</i>
	      			</c:if>
	      		</c:if>
	      		<c:if test="${!isRights}">
	      			<i style="color: #FE2A4D;">+${awardProfit}</i>
	      		</c:if>
	      	</c:if>
	      	元 
	      </li>
	      <li style="border-right:none;">订单状态<br />
		      <p><strong>
		        <c:if test="${order.productType eq '1'}">
		          <c:if test="${order.orderState eq '0'}">未支付</c:if>
		          <c:if test="${order.orderState eq '1'}">还款中</c:if>
		          <c:if test="${order.orderState eq '2'}">已结清</c:if>
		          <c:if test="${order.orderState eq '3'}">已过期</c:if>
		          <c:if test="${order.orderState eq '4'}">已撤销</c:if>
		          <c:if test="${order.orderState eq '5'}">已支付</c:if>
		          <c:if test="${order.orderState eq '7'}">流标</c:if>
		        </c:if>
		        <c:if test="${order.productType eq '2'}">
		          <c:if test="${order.orderState eq '0'}">未支付</c:if>
		          <c:if test="${order.orderState eq '1'}">理财中</c:if>
		          <c:if test="${order.orderState eq '2'}">已结清</c:if>
		          <c:if test="${order.orderState eq '3'}">已过期</c:if>
		          <c:if test="${order.orderState eq '4'}">已撤销</c:if>
		          <c:if test="${order.orderState eq '5'}">匹配中</c:if>
		          <c:if test="${order.orderState eq '6'}">退出</c:if>
		        </c:if>
		        <c:if test="${order.productType eq '3'}">
			          <c:if test="${order.orderState eq '0'}">待支付</c:if>
			          <c:if test="${order.orderState eq '1'}">还款中</c:if>
			          <c:if test="${order.orderState eq '2'}">已结清</c:if>
			          <c:if test="${order.orderState eq '3'}">已过期</c:if>
			          <c:if test="${order.orderState eq '4'}">已撤销</c:if>
			          <c:if test="${order.orderState eq '5'}">已支付</c:if>
			          <c:if test="${order.orderState eq '6'}">退出中</c:if>
		        </c:if>
		      </strong></p>
	      </li>
	    </ul>
	    
	    <dl class="dd-dl clearFloat">
	    	<c:if test="${order.productType ne '3'}">
		    	<c:if test="${not empty loanApplication.awardPoint}">
		      		<dt>投标奖励：</dt>
		      		<dd>
		        	<p>
			        	<c:if test="${not empty loanApplication.rewardsPercent}">
			      			<font style="color: #FE2A4D;">${loanApplication.rewardsPercent}%</font>
			      		</c:if>
			      		奖励金额${loanApplication.awardPoint}时发放
			        </p>
			        </dd>
		      	</c:if>
		        
	        </c:if>
	    </dl>
	    
	    <dl class="dd-dl clearFloat">
	        <dt><c:if test="${order.productType eq '1' || order.productType eq '3'}">借款描述</c:if><c:if test="${order.productType eq '2'}">产品描述</c:if>：</dt>
	        <dd><p><c:if test="${order.productType eq '1' || order.productType eq '3'}">${loanApplication.desc}</c:if><c:if test="${order.productType eq '2'}">${lendProduct.productDesc}</c:if> </p>
	        </dd>
	    </dl>
    </div>
  
	<h2 class="payType">支付方式</h2>
	<form action="" id="f1" method="post" class="">
    <input type="hidden" name="payType" id="payType" value="2"/>
    <input type="hidden" name="token" id="token" value="${token}"/>
    <input type="hidden" id="loanApplicationId" name = "loanApplicationId" value="${loanApplication.loanApplicationId}"/>
    <input type="hidden" id="amount" name="amount" value="${order.buyBalance}"/>
    <input type="hidden" name="productType" id="productType" value="${order.productType}"/>
    <input type="hidden" name="accountPayValue" id="accountPayValue" value="0"/>
    <input type="hidden" name="rechargePayValue" id="rechargePayValue" value="0"/>
    <input type="hidden" name="userVoucherId" id="userVoucherId" value=""/>
    <input type="hidden" name="rateUserIds" id="rateUserIds" value=""/>
    <input type="hidden" name="lendOrderId" id="lendOrderId" value="${order.lendOrderId}"/>
    <input type = "hidden" name="bkcode" id="bkcode" value="" />
    <div class="paypt">
      <div class="payType_Tip clearFloat">
        <span class="ptt_Term ptt_Choose_not"  id="accountpay">
        	<input id="cb_account" type="checkbox" class="check" />账户支付<em class="cgray">（可用余额：<i class="cred" id="accm">${userCashBalance}</i>元）</em>
        </span>
        <span class="ptt_money">账户支付：<big id="shijiyue">0.00</big>元</span>
      </div>
      
      <div class="payType_Tip tip_pay clearFloat">
        <span class="ptt_Term ptt_Choose_not" id="voucherp">
        	<input type="checkbox" class="check" id="ipt_ticket_pay"/>使用优惠券  <c:if test="${fn:length(vouchers) > 0}"><em class="ticket_sum"></em></c:if>
		</span>
        <span class="ptt_money2">
            <i class="pay_num2"></i><big id="ratesp"></big>
        </span>
        <span class="ptt_money">
            <i class="pay_num"></i><big id="vouchersp"></big>
        </span>
      </div>
      
	<!-- voucher start (repeat css) -->
	<div class="ticket-group chooseOne display-none clearFloat cfq_ticket cfp_group" id="ticket-group">
		<c:forEach items="${vouchers}" var="voucher" varStatus="status">
			<div class="single_ticket writes clearFloat cfq <c:if test="${voucher.isOverly eq '0'}">repeat</c:if>">
				<em class="display-none voucherId">${voucher.voucherId}</em>
				<span class="ticket_m"><em>￥</em><i class="ticket_s_m">${voucher.voucherValue}</i></span>
				<div class="explain">
					<big>${voucher.voucherRemark}</big>
					<p>
						<c:if test="${empty voucher.endDate}">长期有效</c:if>
						<c:if test="${not empty voucher.endDate}"><fmt:formatDate value="${voucher.createDate}" pattern="yyyy.MM.dd"/>~<fmt:formatDate value="${voucher.endDate}" pattern="yyyy.MM.dd"/></c:if>
					</p>
				</div>
				<em class="yx_icon"><img src="../images/quan/yx_icon.png" /></em>
			</div>
			<c:if test="${(status.index + 1)%4==0}">
				<div style="clear: both;"></div>
			</c:if>
		</c:forEach>
	</div>
	<!-- voucher end -->
    
    <!-- 加息券 start -->
    <div class="ticket-group chooseOne display-none clearFloat cfq_ticket jxq_group" id="ticket-group-jxq">
        <c:forEach items="${rateUsers}" var="rateUser" varStatus="status">
	        <div class="single_ticket writes clearFloat jxq_bj <c:if test="${rateUser.isOverlay eq '0'}">overly</c:if>">
	            <span class="ticket_m">
	                <em class="display-none rateUserId">${rateUser.rateUserId}</em>
	                <font class="x_ticket_t"><em class="jxqVal" data-ticket="${rateUser.rateValue}">${rateUser.rateValue}</em><i class="ticket_s_m">%</i></font>
	                <font class="x_ticket_b">加息券</font>
	            </span>
	            <div class="explain">
	                <p><span class="explain_p_le">起投金额：</span><span class="explain_p_ri">${rateUser.startAmount}元</span></p>
	                <p><span class="explain_p_le">使用条件：</span><span class="explain_p_ri">${rateUser.conditionStr}</span></p>
	                <p><span class="explain_p_le">有效期至：</span><span class="explain_p_ri"><fmt:formatDate value="${rateUser.endDate}" pattern="yyyy-MM-dd"/></span></p>
	            </div>
	            <em class="yx_icon"><img src="../images/quan/yx_icon.png" /></em>
	        </div>
			<c:if test="${(status.index + 1)%4==0}">
				<div style="clear: both;"></div>
			</c:if>
		</c:forEach>
    </div>
    <!-- 加息券 end -->
      
     <div class="payType_Tip clearFloat" id="bankpay">
        <span class="ptt_Term ptt_Choose" id="card_pay_span">
        	<input id="cb_recharge"  type="checkbox" disabled  class="check"  />银行卡支付
        </span>
       	<span class="ptt_money" id="voucherspbank" >银行卡支付：<big id="vouchbig"><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></big>元</span>
     </div>
     
     <div class="bank_pay_term chooseOne clearFloat">
	     <!-- 网银支付 -->
	     <div class="bpt-01">
		     <h2 class="s_hide" id="h21">
		     	<input id="rdi-inter" type="radio" name="bankPayChannel" value="0" />
		     	<img class="renzheng" src="${ctx}/images/pay/order_icon_03.jpg" />
		     </h2>
		    <%-- <div class="display-none shower" id="inter-group">
			     <div class="tip clearFloat">
				     <span class="floatLeft">由于各银行支持浏览器限制，建议您使用IE（8.0及以上版本）进行网银支付</span>
		             <a href="javascript:;" class="inter_more">查看更多>></a>
			     </div><!-- bankpayd end -->
				 <div class="bank-group clearFloat">
				 	<%@include file="../common/bankList.jsp" %>
				 </div>
			 </div>--%>
	     </div>
	
		 <!-- 快捷支付 -->
		 <div class="bpt-02">
		     <h2 class="s_show" id="h22">
		     	<input id="rdi-quick" type="radio" name="bankPayChannel" checked="checked" class="vadio" value="1"/>
		     	<img class="renzheng" src="${ctx}/images/pay/order_icon_06.jpg" />
		     </h2>
		     <div class="shower" id="quick-group">
			     <span class="pright floatRight display-none" id="myvoud">支付：<big id="bankmyid" class="c_red display-none"><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></big>元</span>
			     <div class="quick-group">
			         <%--<!--连连认证支付无卡-->
			         <c:if test="${empty customerCard}">
			             <%@include file="llpayNoCard.jsp"%>
			         </c:if>
			         <!--连连认证支付有卡-->
			         <c:if test="${not empty customerCard}">
			             <%@include file="llpayHasCard.jsp"%>
			         </c:if>--%>
						 <p class="quickPay clearFloat">
							<span class="floatLeft">
								 <img src="${ctx}/images/hfBank.jpg" />一键支付
							</span>
							<span class="bank banksy mt-0 floatLeft">
								<img class="ml-20" src="${ctx}/images/banklogo/${customerCard.bankCode}.png"/>
								<input type="hidden" name="cardId" id="cardId" value="${customerCard.customerCardId}"/>
								<input type="hidden" name="cardNo" id="cardNo" value="${customerCard.cardCode}"/>
								<i>尾号：<big>${customerCard.cardCodeShort}</big></i>
								<%--<a href="javascript:;" data-id="p_bank" data-mask="mask">查看限额</a>--%>
							</span>
						 </p>
			     </div>
		     </div>
	     </div>
   	  </div>
	</div><!-- paypt end -->
	<div style="height:20px;clear:both;"></div>
    <div class="input-group">
      <label for="jypassword">
        <span>交易密码：</span>
        <input type="password" maxlength="16" id="jypassword" name="password"  onfocus="if(this.value==defaultValue) {this.value='';this.type='password'}" onblur="if(!value) {value=defaultValue; this.type='text';}" class="ipt-input" />
        <a href="#"  id="ed_ex_psdad" class="ml-20">找回交易密码</a>
      </label>
      <c:if test="${isBidEqualLoginPass eq 'true'}">
        <em class="hui" style="margin-left:75px;">*交易密码默认与登录密码相同，为保证您的账号安全，请尽快修改！</em>
      </c:if>
      <c:if test="${isBidEqualLoginPass eq 'false'}">
        <em class="pay-em"></em>
      </c:if>
      <c:if test="${isBidEqualLoginPass eq 'true'}">
        <span style="display: none;" id="bid_pass">*交易密码默认与登录密码相同，为保证您的账号安全，请尽快修改！</span>
      </c:if>
    </div>
    <div class="btn-group ml-75">
      <button type="button" id="okpay" class="btn btn-error mt-0" style="margin-bottom:50px;  width: 158px; font-family: 'MicroSoft YaHei';background: #f66;">确认支付</button>
      <input type="hidden" id="regitxt" data-val="2"/><!-- 2:不可禁用 1:禁用-->
    </div>
  </form>

</div><!-- article end -->

    <div class="masklayer masklback heimask" id="payshowstate">
        <h2 class="clearFloat"><span>提示</span> <a href="javascript:;" data-id="close" style="display:none;"></a></h2>

        <div class="interp">
            <p>请您在新打开的页面进行支付，支付完成前请不要关闭此窗口</p>
            <div class="btn-inter-group">
                <a href="javascript:;" class="btn btn-error" id="llPaySuccess">支付成功</a>
                <a href="http://help.caifupad.com/guide/common/wangyinzhifu/" id="llPayQuestion"  target="_blank" >支付遇到问题?</a>
            </div>
        </div>
    </div>
  <!-- article end -->
<div class="zhezhao5">
  <img class="zheimg" src="${ctx}/images/2013092022533423.gif"/>
  <p>支付中...</p>
</div>
<!-- masklayer start  -->
<div class="masklayer masklback heimask" id="interdvi1">
	<h2 class="clearFloat"><span>提示</span> <a href="javascript:;" data-id="close" style="display:none;"></a></h2>

	<!-- phonevacs end -->
    <div class="interp">
    	<p>请您在新打开的网上银行页面进行支付，支付完成前请不要关闭此窗口</p>
        <div class="btn-inter-group">
        	<a href="javascript:;"  class="btn btn-error payresult" >支付成功</a>
        	<a href="http://help.caifupad.com/guide/common/wangyinzhifu/" class="payresult" target="_blank">支付遇到问题?</a>
        </div>
    </div>
</div>
<!-- masklayer end -->
    <form id="form_result" action="" method="post">
      <input type="hidden" id="errorMsg" name="errorMsg" value=""/>
      <input type="hidden" name="lendOrderId" value="${order.lendOrderId}"/>
    </form>

	<form id="pay_result" action="${ctx}/payment/getOrderResult" method="post">
	  <input type="hidden" id="type"  name="type" value="pay"/>
	  <input type="hidden" id="rechargeCode"  name="rechargeCode"  value=""/>
	  <input type="hidden" name="lendOrderId" value="${order.lendOrderId}"/>
	</form>

	<form id="hf_cz_form" class="form" method="post" target="_blank">
		<input type="hidden" id="mchnt_cd2" name="mchnt_cd" value=""/>
		<input type="hidden" id="mchnt_txn_ssn2" name="mchnt_txn_ssn" value=""/>
		<input type="hidden" id="login_id" name="login_id" value="${userExtInfo.mobileNo}"/>
		<input type="hidden" id="amt" name="amt" value=""/>
		<input type="hidden" id="page_notify_url2" name="page_notify_url" value=""/>
		<input type="hidden" id="back_notify_url2" name="back_notify_url" value=""/>
		<input type="hidden" id="signature2" name="signature" value=""/>

	</form>

<!-- p_bank start -->
<%@include file="../common/limit.jsp"%>
<!-- p_bank end -->

<!-- footer start -->
<%@include file="../common/footLine1.jsp"%>
<!-- footer end -->

<!-- 找回交易密码-开始 -->
<script src="${ctx}/js/ed_ex_psd.js" type="text/javascript"></script>
<div class="zhezhao6"></div>
<div class="masklayer masklback" id="ed_ex_psd">
		<h2 class="clearFloat"><span>找回交易密码</span><a href="javascript:;" data-id="close"></a></h2>
		<div class="xiugai_phone_main" id="xigai1">
        	<img src="${ctx}/images/ed_ex_psd1.jpg" class="mt-30 mb-30"/>
			<form action="" class="form" method="post">
				<div class="input_box_phone">
					<label>
						<span>绑定的手机号码</span>
						<i class="tal ex-plone">${sessionScope.currentUser.encryptMobileNo}</i>
					</label>
					<em></em>
				</div>
				<div class="input_box_phone">
					<label>
						<span>手机验证码</span>
						<input type="text" id="ex_valid" value="" autocomplete="off" maxlength="6" placeholder="请输入验证码" style=""/>
						<button type="button" id="ex_get_valid" class="huoqu_yanzm" >获取验证码</button>
					</label>
					<em></em>
				</div>
				<div class="input_box_phone ipt_box_phone">
					<button type="button" id="next_sub1">下一步</button>
				</div>
			</form>
		</div>
		<div class="xiugai_phone_main display-none" id="phone_d2">
        	<img src="${ctx}/images/ed_ex_psd2.jpg" class="mt-30 mb-30"  />
			<form action="" class="form" method="post">
					<div class="input_box_phone input_box ipt-box-ex">
						<label>
							<span>输入新交易密码</span>
							<input type="password" class="width200" id="ed_ex_psd1" value="" maxlength="16" style="width:160px;" autocomplete="off" value="" onFocus="if(value==defaultValue){value=''}" onBlur="if(!value){value=defaultValue}" onKeyUp="ed_ex_Check(this.value)" />
								
								<div type="button" id="rejc_ex_psd" class="Tcolor floatLeft">无</div>
						</label>
						<em class="hui fontsize12">交易密码为6 -16 位字符，支持字母及数字,字母区分大小写</em>
					</div>
					<div class="input_box_phone">
						<label>
							<span>再次输入新交易密码</span>
							<input type="password" class="width200" value="" onFocus="if(value==defaultValue){value=''}" onBlur="if(!value){value=defaultValue}" id="ed_ex_psd2" value="" maxlength="16" autocomplete="off" />
						</label>
						<em></em>
					</div>
					<div class="input_box_phone ipt_box_phone">
						<button type="button" id="next_sub2">下一步</button>
					</div>
			</form>
		</div>
		<div class="xiugai_phone_main display-none" id="phone_d3">
				
        	<img src="${ctx}/images/ed_ex_psd3.jpg" class="mt-30 mb-30" />
			<p class="mt-50"><img src="${ctx}/images/img/true.jpg" /><span>交易密码重置成功！</span></p>
				
			<div class="input_box_phone ipt_box_phone" style="margin-top:85px;">
				<a href="javascript:;" id="ed_ex_psda"><button>确认</button></a>
			</div>
		</div>
</div>
<!-- 找回交易密码-结束 -->
<%@include file="../common/lianlianProtocol.jsp"%>
<!-- masklayer start  -->
<div class="masklayer masklback heimask" id="interdvi">
	<h2 class="clearFloat"><span>提示</span> <a href="javascript:;" data-id="close"></a></h2>

	<!-- phonevacs end -->
    <div class="interp">
    	<p>请您在新打开的网上银行页面进行充值，充值完成前请不要关闭此窗口</p>
        <div class="btn-inter-group">
        	<a href="javascript:;" class="btn btn-error payresult">支付成功</a>
        	<a href="http://help.caifupad.com/guide/common/wangyinzhifu/" class="payresult"  target="_blank" >支付遇到问题?</a>
        </div>
    </div>
</div>
<!-- masklayer end -->

</body>
</html>
