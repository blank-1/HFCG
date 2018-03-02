<%@ page import="com.xt.cfp.core.constants.LendProductTimeLimitType" %>
<%@ page import="com.xt.cfp.core.constants.LendProductTypeEnum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<%
  pageContext.setAttribute("timeLimitTypeDay", LendProductTimeLimitType.TIMELIMITTYPE_DAY.getValue());
  pageContext.setAttribute("timeLimitTypeMonth", LendProductTimeLimitType.TIMELIMITTYPE_MONTH.getValue());

  pageContext.setAttribute("productTypeRighting", LendProductTypeEnum.RIGHTING.getValue());
  pageContext.setAttribute("productTypeFinancing", LendProductTypeEnum.FINANCING.getValue());
%>
<!DOCTYPE html>
<html>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
  <title>订单支付 - 财富派</title>
  <%@include file="../common/common_js.jsp"%>
  <script type="text/javascript" src="${ctx}/js/financepay.js"></script><!-- pay javascript -->
  <script type="text/javascript" src="${ctx}/js/llbankinfo.js"></script><!-- public javascript -->
  		<link rel="stylesheet" type="text/css" href="${ctx}/css/cxm_index.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/css/dingdan.css" />
</head>
  <% session.removeAttribute("smsCode");%>
<body>
<%@include file="../login/login.jsp"%>
<header>
	<!-- navtopbg start-->
	<%@include file="../common/headLine1.jsp"%>

</header>
<customUI:headLine action="2" />
<div class="clear"></div>
<!-- article start -->
<div class="container clearFloat">
  <h2 class="dd-h2 dd2-h2 pt-10">订单支付</h2>
	<div class="paytitle mt-25">
	<!-- -->
<div class="dd-jr clearFloat">
			<div class="dd-jr-l">
				<h3>${order.lendOrderName}</h3>
				<p>订单号码：${order.orderCode}</p>
			</div>
			<div class="dd-jr-r">
				<h4>支付金额：<b id="buyBalance"><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></b>元</h4>
				<p>创建时间：<fmt:formatDate value="${order.buyTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </p>
			</div>
		</div>
	<!-- -->
	
	
	<ul class="dd-ul clearFloat">
	      <li>
	      	<h5>预期年化收益范围</h5>
	      	<p><fmt:formatNumber value="${lendProduct.profitRate}" pattern="#,##0.00"/>%-<fmt:formatNumber value="${lendProduct.profitRateMax}" pattern="#,##0.00"/>%</p>
	      </li>
	      <li>${lendProduct.productType eq productTypeRighting ? "借款期限" : "省心期"}<br /><i>${lendProduct.timeLimit}</i>
	          ${lendProduct.timeLimitType eq timeLimitTypeMonth ? "个月" : "天"}
	      </li>
	      <li>购买金额<br /><i id="balance"><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></i>元</li>
	      <%-- <li>预期收益<br />
	      	<i><fmt:formatNumber value="${expectedInteresting}" pattern="#,##0.00"/></i>
	      	<c:if test="${not empty awardProfit}">
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
	      </li> --%>
	      <li>投资标的<br />
	      	<i>1-12月标的</i>
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
		          <c:if test="${order.orderState eq '5'}">已支付</c:if>
		          <c:if test="${order.orderState eq '6'}">逾期回款</c:if>
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
	    
	    
	<!-- paycontext start-->
      <!-- paycontext end-->
    <!-- paycontext start-->
  <%--  <div class="paycontext clearFloat">
       <table cellpadding="0" cellspacing="" border="0">
        <tr>
          <td width="79">
            ${lendProduct.productType eq productTypeRighting ? "借款描述" : "产品描述"}
          </td>
          <td width="869">
            ${lendProduct.productType eq productTypeRighting ? loanPublish.desc : lendProduct.productDesc}
          </td>
        </tr>
      </table> 
      <div class="paycontext-main">
      		<c:if test="${not empty loanApplication.awardRate}">
	      		<div class="paycontext-le mb-10">投标奖励</div>
	      		<div class="paycontext-ri mb-10">
	      			<font color="#FE2A4D">
	      				${loanApplication.awardRate}
	      			</font>
	      			<c:if test="${not empty loanApplication.awardPoint }">
	      				奖励金额${loanApplication.awardPoint}时发放
	      			</c:if>
	      		</div>
      		</c:if>
      		<div class="paycontext-le">
				<c:if test="${lendOrder.productType eq '1'}">借款描述</c:if><c:if test="${lendOrder.productType eq '2'}">产品描述</c:if>
			</div>
      		<div class="paycontext-ri">
      			<c:if test="${lendOrder.productType eq '1'}">${loanApplication.desc}</c:if><c:if test="${lendOrder.productType eq '2'}">${lendProduct.productDesc}</c:if>
      		</div>
      </div>

    </div>--%> <!-- paycontext end-->

  </div> <!-- paytitle end -->
  
  <!-- 省心计划利息分配方式（开始） -->
    <h2 class="payType">收益分配方式</h2>
    <div class="shengXinpaybox">
        <!-- <div class="shengXinpay"> -->
            <span class="yixuan" data="1">收益提取至可用余额</span>
            <!-- <span data="0">收益复利投资</span> -->
        <!-- </div> -->
        <div style="clear:both;height:20px;"></div>
        <!-- <p class="shengxinTips"><i style="color:#FF5655;vertical-align: middle;">*</i> 回款利息收益将进入可用余额，资金灵活</p> -->
    </div>
  <!-- 省心计划利息分配方式（结束） -->
  
	<h2 class="payType">支付方式</h2>

  <!-- paypt start -->
  <form action="" id="f1" method="post" class="">
    <input type="hidden" name="payType" id="payType" value="2"/>
    <input type="hidden" name="token" id="token" value="${token}"/>
    <input type="hidden" id="amount" name="amount" value="${order.buyBalance}"/>
    <input type="hidden" name="productType" id="productType" value="0"/>
    <input type="hidden" name="accountPayValue" id="accountPayValue" value="0"/>
    <input type="hidden" name="rechargePayValue" id="rechargePayValue" value="0"/>
    <input type="hidden" name="userVoucherId" id="userVoucherId" value=""/>
    <input type="hidden" name="lendProductPublishId" id="lendProductPublishId" value="${lendProductPublish.lendProductPublishId}"/>
    <input type="hidden" name="lendOrderId" id="lendOrderId" value="${order.lendOrderId}"/>
    <input type = "hidden" name="bkcode" id="bkcode" value="" />
  <div class="paypt">
     
    
     <div class="payType_Tip clearFloat">
        <span class="ptt_Term ptt_Choose_not"  id="accountpay">
        	<input id="cb_account" type="checkbox" class="check" />账户支付<em class="cgray">（可用余额：<i class="cred" id="accm">${userCashBalance}</i>元）</em>
        </span>
        <span class="ptt_money">账户支付：<big id="shijiyue">0.00</big>元</span>
      </div>

     
    <!-- 
       <div class="payType_Tip tip_pay clearFloat">
        <span class="ptt_Term ptt_Choose_not" id="voucherp"  >
        	<input type="checkbox"  class="check"    id="ipt_ticket_pay"/>使用优惠券   
		</span>
        <span class="ptt_money2">
            <i class="pay_num2"></i><big id="ratesp"></big>
        </span>
        <span class="ptt_money">
            <i class="pay_num"></i><big id="vouchersp"></big>
        </span>
      </div>
       -->
    <!-- voucher start (repeat css) -->
     <%--  <div class="voucher clearFloat">
     //todo 处理财富券
      <c:forEach items="${vouchers}" var="voucher" varStatus="var">

        <div class="writes <c:if test="${voucher.isOverly eq '0'}">repeat</c:if>">
          <em class="display-none">${voucher.voucherId}</em>
          <div class="clearFloat ml-30">

            <span><small>￥</small><big>${voucher.voucherValue}</big></span>
            <span class="rights"><br /><i>财富券</i></span>
          </div>

          <p class="tiao">
            <c:if test="${empty voucher.endDate}">长期有效</c:if>
            <c:if test="${not empty voucher.endDate}"><fmt:formatDate value="${voucher.createDate}" pattern="yyyy.MM.dd"/>~<fmt:formatDate value="${voucher.endDate}" pattern="yyyy.MM.dd"/></c:if>
          </p>
          <p>${voucher.voucherRemark}</p>
        </div>
      </c:forEach>
    </div>--%><!-- voucher end -->


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
		     <div class="display-none shower" id="inter-group">
			     <div class="tip clearFloat">
				     <span class="floatLeft">由于各银行支持浏览器限制，建议您使用IE（8.0及以上版本）进行网银支付</span>
		             <a href="javascript:;" class="inter_more">查看更多>></a>
			     </div><!-- bankpayd end -->
				 <div class="bank-group clearFloat">
				 	<%@include file="../common/bankList.jsp" %>
				 </div>
			 </div>
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
			         <!--连连认证支付无卡-->
			         <c:if test="${empty customerCard}">
			             <%@include file="llpayNoCard.jsp"%>
			         </c:if>
			         <!--连连认证支付有卡-->
			         <c:if test="${not empty customerCard}">
			             <%@include file="llpayHasCard.jsp"%>
			         </c:if>
			     </div>
		     </div>
	     </div>
   	  </div>
   	  
   	  
   	  
   	  
		<%-- <p class="clearFloat" id="bankpay">
			<input id="cb_recharge" type="checkbox" disabled class="" />银行卡支付
			<span class="pright display-inherit" id="voucherspbank">银行卡支付：
				<big id="vouchbig"><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00" /></big>元
			</span>
		</p>
		<!-- bankpayd start -->
		<div class="bankpayd bankpay-b0  position-rela">
			<!-- 网银支付 -->
			<h2 class="botyibao clearFloat" id="h21">
				<span class="floatLeft">
				<label><input id="rdi-inter" type="radio" name="bankPayChannel" value="0" /><img
						class="renzheng" src="${ctx}/images/pay/internatePay.jpg" /></label><i>跳转至银行页面，支付限额高</i></span>
			</h2>
			<div class="inter-group">
				<p class="ieshowpay" id="ieshowpay">由于各银行支持浏览器限制，建议您使用IE（8.0及以上版本）进行网银支付</p>
				<%@include file="../common/bankList.jsp"%>
				<p class="inter_more">更多>></p>
			</div>
			<!-- bankpayd end -->

			<div class="clear"></div>
			<div class="divhr"></div>
			<div class="clear"></div>

			<!-- 快捷支付 -->
			<h2 class="clearFloat mt-30" id="h22">
				<span class="floatLeft">
				<label><input id="rdi-quick" type="radio" name="bankPayChannel" value="1" /><img
						class="renzheng" src="${ctx}/images/pay/quickPay.jpg" /></label> <i>一键支付，快捷方便，付款过程安全流畅</i>
				</span>
			</h2>
			<span class="pright floatRight display-none" id="myvoud">支付：
				<big id="bankmyid" class="c_red display-none"><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00" /></big>元
			</span>
			<div class="quick-group">
				<!--连连认证支付无卡-->
				<c:if test="${empty customerCard}">
					<%@include file="llpayNoCard.jsp"%>
				</c:if>
				<!--连连认证支付有卡-->
				<c:if test="${not empty customerCard}">
					<%@include file="llpayHasCard.jsp"%>
				</c:if>
			</div>
		</div> --%>
		<!-- bankpayd end -->
	</div>
	<!-- paypt end -->
    <%-- <div class="input-group mt-30 ml-50">
      <label for="jypassword">
        <span>交易密码：</span>
        <input type="password" maxlength="16" id="jypassword" name="password"  onfocus="if(this.value==defaultValue) {this.value='';this.type='password'}" onblur="if(!value) {value=defaultValue; this.type='text';}" class="ipt-input" />
        <a href="#"  id="ed_ex_psdad" class="ml-20">找回交易密码</a>
      </label>
      <c:if test="${isBidEqualLoginPass eq 'true'}">
        <em class="hui">*交易密码默认与登录密码相同，为保证您的账号安全，请尽快修改！</em>
      </c:if>
      <c:if test="${isBidEqualLoginPass eq 'false'}">
        <em></em>
      </c:if>
      <c:if test="${isBidEqualLoginPass eq 'true'}">
        <span style="display: none;" id="bid_pass">*交易密码默认与登录密码相同，为保证您的账号安全，请尽快修改！</span>
      </c:if>
    </div>
    <div class="btn-group ml-50">
      <button type="button" id="okpay" class="btn btn-error mt-0" style="margin-bottom:50px;">确认支付</button>
      <input type="hidden" id="regitxt" data-val="2"/><!-- 2:不可禁用 1:禁用-->

    </div> --%>
    
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
	<div class="zhezhao5">
		<img class="zheimg" src="${ctx}/images/2013092022533423.gif" />
		<p>支付中...</p>
	</div>
	<script>
	  $(function(){
	    $(".zhezhao5").css("height",document.body.clientHeight);
	    $(".zhezhao5 .zheimg").css("margin-top",document.body.clientHeight/2-100);
	  })
	</script>

	<div class="zhezhao1"></div>
	<!-- 连连支付 支付成功 -->
	<div id="payshowstate" class="masklayer masklback mstate">
		<h2 class="clearFloat"><span>提示</span> <a href="javascript:;" data-id="close" style="display:none;"></a></h2>
        <div class="interp">
            <p>请您在新打开的页面进行支付，支付完成前请不要关闭此窗口</p>
            <div class="btn-inter-group">
                <a href="javascript:;" class="btn btn-error" id="llPaySuccess">支付成功</a>
                <a href="http://help.caifupad.com/guide/common/wangyinzhifu/" id="llPayQuestion"  target="_blank" >支付遇到问题?</a>
            </div>
        </div>
	</div>
	<!-- 网关支付 支付成功 -->
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

	<form id="form_result" action="" method="post">
      <input type="hidden" id="errorMsg" name="errorMsg" value=""/>
      <input type="hidden" name="lendOrderId" value="${order.lendOrderId}"/>
    </form>

	<form id="pay_result" action="${ctx}/payment/getOrderResult" method="post">
	  <input type="hidden" id="type"  name="type" value="pay"/>
	  <input type="hidden" id="rechargeCode"  name="rechargeCode"  value=""/>
	  <input type="hidden" name="lendOrderId" value="${order.lendOrderId}"/>
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
