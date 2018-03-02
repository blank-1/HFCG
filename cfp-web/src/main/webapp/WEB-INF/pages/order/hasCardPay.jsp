<!DOCTYPE html>
<html>
<%@ page import="com.xt.cfp.core.constants.LendProductTimeLimitType" %>
<%@ page import="com.xt.cfp.core.constants.LendProductTypeEnum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<%@include file="../login/login.jsp"%>
<%
  pageContext.setAttribute("timeLimitTypeDay", LendProductTimeLimitType.TIMELIMITTYPE_DAY.getValue());
  pageContext.setAttribute("timeLimitTypeMonth", LendProductTimeLimitType.TIMELIMITTYPE_MONTH.getValue());

  pageContext.setAttribute("productTypeRighting", LendProductTypeEnum.RIGHTING.getValue());
  pageContext.setAttribute("productTypeFinancing", LendProductTypeEnum.FINANCING.getValue());
%>
<head>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
  <meta name="description" content="" />
  <title>订单支付 - 财富派</title>
  <%@include file="../common/common_js.jsp"%>
  <script type="text/javascript" src="${ctx}/js/pay.js"></script><!-- pay javascript -->
  <script type="text/javascript" src="${ctx}/js/financeList.js"></script><!-- financeList javascript -->
</head>
<% session.removeAttribute("smsCode");%>
<body>
<header>
  <!-- navtopbg start-->
  <%@include file="../common/headLine1.jsp"%>
  <!-- navtopbg end-->
  <nav class="logobor">
    <!-- navbg start -->
    <div class="wrapper clearFloat">
      <div class="logo floatLeft">
        <a href="${ctx}/" class=""><img src="${ctx}/images/finance_03.jpg" /></a>
      </div>
      <span class="floatLeft regissp">订单支付</span>
    </div><!-- navbg end -->
  </nav>

</header>
<div class="clear"></div>
<!-- article start -->
<div class="pay wrapper clearFloat">
  <h2>订单详情</h2>
  <!-- paytitle start -->
  <div class="paytitle">
    <!-- -->
    <div class="clearFloat">
      <div class="payleft">
        <img src="${ctx}/images/pay_07.jpg" class="floatLeft" />
        <div class="floatLeft">

          <h2>${lendOrder.lendOrderName}</h2>
          <span>订单号码：${lendOrder.orderCode}</span>
        </div>
      </div>
      <div class="payright">
        <p>支付金额：<big><fmt:formatNumber value="${lendOrder.buyBalance}" pattern="#,##0.00"/></big>元</p>
        <span>创建时间：<fmt:formatDate value="${lendOrder.buyTime }" pattern="yyyy-MM-dd HH:mm:ss" /></span>
      </div>
    </div><!-- -->

    <!-- paycontext start-->
    <div class="paycontext clearFloat">
      <span class="first">预期年化收益<br />
      <i>${lendProduct.profitRate}</i>%
      	<c:if test="${not empty loanApplication.awardRate}">
      		<i style="color: #FE2A4D;">+${loanApplication.awardRate}<img src="../images/borrow_05.png"></i>
      	</c:if>
      </span>
      <span>${lendProduct.productType eq productTypeRighting ? "借款期限" : "产品期限"}<br /><i>${lendProduct.timeLimit}</i>${lendProduct.timeLimitType eq timeLimitTypeMouth ? "个月" : "天"}</span>
      <span>购买金额<br /><i id="balance"><fmt:formatNumber value="${lendOrder.buyBalance}" pattern="#,##0.00"/></i>元</span>
      <span>预期收益<br />
      	<i><fmt:formatNumber value="${interesting}" pattern="#,##0.00"/></i>
      	<c:if test="${not empty awardProfit}">
      		<i style="color: #FE2A4D;">+${awardProfit}</i>
      	</c:if>
      	元
       </span>
      <span class="last">订单状态<br /><i>
        <c:if test="${lendOrder.productType eq '1'}">
          <c:if test="${lendOrder.orderState eq '0'}">未支付</c:if>
          <c:if test="${lendOrder.orderState eq '1'}">还款中</c:if>
          <c:if test="${lendOrder.orderState eq '2'}">已结清</c:if>
          <c:if test="${lendOrder.orderState eq '3'}">已过期</c:if>
          <c:if test="${lendOrder.orderState eq '4'}">已撤销</c:if>
          <c:if test="${lendOrder.orderState eq '5'}">已支付</c:if>
          <c:if test="${lendOrder.orderState eq '7'}">流标</c:if>
        </c:if>
        <c:if test="${lendOrder.productType eq '2'}">
          <c:if test="${lendOrder.orderState eq '0'}">未支付</c:if>
          <c:if test="${lendOrder.orderState eq '1'}">理财中</c:if>
          <c:if test="${lendOrder.orderState eq '2'}">已结清</c:if>
          <c:if test="${lendOrder.orderState eq '3'}">已过期</c:if>
          <c:if test="${lendOrder.orderState eq '4'}">已撤销</c:if>
          <c:if test="${lendOrder.orderState eq '5'}">匹配中</c:if>
          <c:if test="${lendOrder.orderState eq '6'}">退出</c:if>
        </c:if>

      </i></span>
    </div> <!-- paycontext end-->
    <!-- paycontext start-->
    <div class="paycontext clearFloat">
      <%-- <table cellpadding="0" cellspacing="" border="0">
        <tr>
          <td width="79">
            ${lendProduct.productType eq productTypeRighting ? "借款描述" : "产品描述"}
          </td>
          <td width="869">
            ${lendProduct.productType eq productTypeRighting ? loanPublish.desc : lendProduct.productDesc}
          </td>
        </tr>
      </table> --%>
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
				<c:if test="${order.productType eq '1'}">借款描述</c:if><c:if test="${order.productType eq '2'}">产品描述</c:if>
			</div>
      		<div class="paycontext-ri">
      			<c:if test="${order.productType eq '1'}">${loanApplication.desc}</c:if><c:if test="${order.productType eq '2'}">${lendProduct.productDesc}</c:if>
      		</div>
      </div>

    </div> <!-- paycontext end-->

  </div> <!-- paytitle end -->
  <h2>支付方式 </h2>

  <!-- paypt start -->
  <form action="" id="f1" method="post" class="mt-30">
    <input type="hidden" name="payType" id="payType" value="1"/>
    <input type="hidden" name="productType" id="productType" value="0"/>
    <input type="hidden" name="lendProductPublishId" id="lendProductPublishId" value="${lendProductPublish.lendProductPublishId}"/>
    <input type="hidden" id="amount" name="amount" value="${lendOrder.buyBalance}"/>
    <input type="hidden" name="token" id="token" value="${token}"/>
    <input type="hidden" name="accountPayValue" id="accountPayValue" value="0"/>
    <input type="hidden" name="rechargePayValue" id="rechargePayValue" value="0"/>
    <input type="hidden" name="userVoucherId" id="userVoucherId" value=""/>
    <input type="hidden" name="cardId" id="cardId" value="${customerCard.customerCardId}"/>
    <input type="hidden" name="lendOrderId" id="lendOrderId" value="${lendOrder.lendOrderId}"/>
  <div class="paypt">
    <p class="clearFloat" id="accountpay">
      <input id="cb_account" type="checkbox" class="" />账户支付<span class="yue">（可用余额：<big id="accm">${userCashBalance}</big>元）</span>

      <span class="pright">余额支付：<big id="shijiyue">20.00</big>元</span>
    </p>

    <p class="clearFloat" id="voucherp">
      <input type="checkbox" ${fn:length(vouchers) > 0 ? "" : "disabled"} class="" />使用财富券<c:if test="${fn:length(vouchers) > 0}"><span class="yue">(${fn:length(vouchers)}张可用财富券)</span></c:if>

      <span class="pright">财富券支付：<big id="vouchersp">0.00</big>元</span>
    </p>
    <!-- voucher start (repeat css) -->
    <div class="voucher clearFloat">
      <%//todo 处理财富券%>
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
    </div><!-- voucher end -->


    <p class="clearFloat" id="bankpay">
      <input type="checkbox" disabled  class="" id="cb_recharge" />银行卡支付

      <span class="pright" id="voucherspbank">银行卡支付：<big id="vouchbig"><fmt:formatNumber value="${lendOrder.buyBalance}" pattern="#,##0.00"/></big>元</span>
    </p>
    <!-- bankpayd start -->
    <div class="bankpayd bankpay-b0">
      <h2 class="clearFloat">
        <span class="floatLeft"><img class="renzheng" src="${ctx}/images/pay_06.png" /><i>一键支付</i></span>
        <span class="bank floatLeft">
            	<img src="${ctx}/images/banklogo/${customerCard.bankCode}.png" />
                <i>尾号：<big>${customerCard.cardCodeShort}</big></i>
                <a href="javascript:" class="ahui" data-id="p_bank" data-mask="mask">查看限额</a>
        </span>
        <span class="pright floatRight">支付：<big id="bankmyid" class="c_red"><fmt:formatNumber value="${lendOrder.buyBalance}" pattern="#,##0.00"/></big>元</span>
        <div class="clear"></div>
        <div class="banked">

          <div class="input-group">
            <label for="phone">
              <span class="fsp1">银行预留手机号：</span>
              ${customerCard.mobile}
              <input type="hidden" value="${customerCard.mobile}" id="phone" />
            </label>
            <em ></em>
          </div>
          <div class="input-group">
            <label for="valid" class="floatLeft">
              <span class="floatLeft linheight">验证码：</span>
              <input type="text" value=""  id="valid" name="valid" maxlength="6" class="ipt-input widthvalid payipt2" />
            </label>
            <button type="button" id="getvalid" class="btn btn-blue mt-0 ml-0 floatLeft">获取验证码</button>
            <div class="clearFloat"></div>
            <em></em>
          </div>
        </div>
    </h2></div><!-- bankpayd end -->
  </div> <!-- paypt end -->
    <div class="input-group mt-30 ml-50">
      <label for="jypassword">
        <span>交易密码：</span>
        <input type="password" id="jypassword"  onfocus="if(this.value==defaultValue) {this.value='';this.type='password'}" onblur="if(!value) {value=defaultValue; this.type='text';}" name="password" class="ipt-input" />
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
      <button type="button" id="okpayWithCard" class="btn btn-error mt-0" style="margin-bottom:50px;">确认支付</button>
    </div>
    <input type="hidden" id="regitxt" data-val="2"/><!-- 2:不可禁用 1:禁用-->

  </form>
</div><!-- article end -->
<div class="zhezhao5">
  <img class="zheimg" src="${ctx}/images/2013092022533423.gif"/>
  <p>支付中...</p>
</div>
<script>
  $(function(){

    $(".zhezhao5").css("height",document.body.clientHeight);
    $(".zhezhao5 .zheimg").css("margin-top",document.body.clientHeight/2-100);
  })
</script>

<form id="form_result" action="" method="post">

  <input type="hidden" id="errorMsg" name="errorMsg" value=""/>
  <input type="hidden" name="lendOrderId" value="${lendOrder.lendOrderId}"/>
</form>

<!-- p_bank start -->
<%@include file="../common/limit.jsp"%>
<!-- p_bank end -->

<!-- footer start -->
<%@include file="../common/footLine3.jsp"%>
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



</body>
</html>

