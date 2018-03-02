<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="../common/taglibs.jsp"%>
<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" /> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	
	<title>账户中心-资金管理-银行卡管理</title>
	<%@include file="../common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx}/js/bankM.js"></script><!-- public javascript -->
	<script type="text/javascript" src="${ctx}/js/infor_5.js"></script><!-- public javascript -->
</head>

<body onload="start()" class="body-back-gray" >
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->
<input type="hidden" id="titleTab" value="2-5" />
<!-- tabp start -->
<%request.setAttribute("tab","4-5");%>
<!-- tabp end -->

<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">资金管理</a>></li>
        <li><span>银行卡管理</span></li>
    </ul>
</div>
<!-- person-link end -->

<!-- container start -->
<div class="container clearFloat">
    <!-- pLeft start -->
    <div class="pLeft clearFloat">
    </div>
    <!-- pLeft end -->

    <!-- pRight start -->
    <div class="pRight clearFloat">
        <div class="p-Right-top">
			<p class="yhkgl-title">银行卡管理</p>
			<div class="clear_10"></div>
		</div>
		<p class="quickPay" style="line-height:5px;">
			<img src="${ctx }/images/hfBank.jpg" style="margin-left:25px;" /> 认证支付
			<small>为保证您的资金安全，我们与恒丰支付合作，您在平台绑定的银行卡将作为"恒丰支付-认证支付"的支付银行卡及平台提现银行卡使用</small>
		</p>
        <div class="p-Right-bot yhkgl-mt0">
			<div class="bank-left">
				<div class="input-group">
					<label>
						<span class="fsp1">开户名：</span>
						<c:if test="${userExt.isVerified ne '1'}">
				          <small class="">＊您未在恒丰开通账户，请先进行<a  data-mask='mask' data-id="hengfengCard"  href="javascript:;">恒丰开户</a>再进行充值操作</small>
				        </c:if>
				        <c:if test="${userExt.isVerified eq '1'}">
				          ${userExt.realName}
				        </c:if>
					</label>
				</div>
				<div class="input-group mt-30">
					<label for="bankid">
						<span class="fsp1 fash-3">银行卡号：</span>
						<input type="text" id="bankid"  size="25" onKeyUp="formatBankNo(this)" onKeyDown="formatBankNo(this)" flag="true" <c:if test="${not empty hisCustomerCard && isSupport eq 'true' }">value="${hisCustomerCard.cardCode}"</c:if>   name="bankid" class="ipt-input" />
						<i id="bankshow">
                   		</i>
					</label>
	                <c:if test="${userExt.isVerified eq '1'}">
				        <em class="hui">*仅支持<font color="#fe2a4d">${userExt.realName}</font>的储蓄卡进行绑卡充值</em>
				    </c:if>
				</div>
				<div class="input-group mt-20">
					<label for="password">
						<span class="fsp1">付款限额：</span>
						<a href="javascript:;"  data-id="p_bank" data-mask="mask" >点击查看</a>
					</label>
				</div>
				<div class="input-group mt-20">
					<label for="moneyp">
						<span class="fsp1 fash-3">绑卡充值：</span>
						<input type="text" id="moneyp" name="moneyp" class="ipt-input" /><i>元</i>
					</label>
					<em class="hui">*充值最小金额不得小于<font color="#fe2a4d">0.01</font>元</em>
				</div>
				<div class="bankphonebtyn mt-30">
				    <%@include file="../common/lianlianProtocol.jsp"%>
					<button type="button" id="recharge" class="btn btn-error">确认协议并充值</button>
					<a href="javascript:;" data-mask="mask" data-id="lianlian" >《恒丰认证支付开通协议》</a>
				</div>
			</div>
			<div class="bank-right">
				<div class="bindcard" id="bindcard">
					<h2>绑卡提醒</h2>
					<p>为使您的投资更加方便快捷，我司的第三方支付平台已全面升级为恒丰支付，您需要在恒丰支付平台重新绑定银行卡。如有问题，请咨询客服400-061-8080</p>
				</div>
			</div>
			<div class="clear_50"></div>
		</div>
    </div>
    <!-- pRight start -->
</div>
<!-- container end -->




<script type="text/javascript">
	<c:if test="${userExt.isVerified ne '1'}">
	$("input.ipt-input[type=text]:visible,button.btn").each(function() {
		$(this).attr("disabled",true);
	});
	</c:if>
</script>

<form id="form_result" action="" method="post">

	<input type="hidden" id="errorMsg" name="errorMsg" value=""/>
</form>
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
<form id="recharge_result" action="${ctx}/payment/getRechargeResult" method="post">
	<input type="hidden" id="type"  name="type" value="recharge"/>
	<input type="hidden" id="rechargeCode"  name="rechargeCode"  value=""/>
</form>
<!-- alert start  -->
<div class="zhezhao1"></div>
<div class="masklayers masklback" id="bankcard_add_alert">
	<h2 class="clearFloat"><span id="bankcard_add_alert_h"></span></h2>
	<div class="shenf_yanz_main">
		<p class="myp"><img id="bankcard_add_alert_img" src="" /><span id="bankcard_add_alert_p"></span></p>
		<div class="input_box_shenf myp2">
			<a href="javascript:closeAlert();" ><button>确认</button></a>
		</div>
	</div>
</div>
<!-- alert end -->

<!-- p_bank start -->
<%@include file="../common/limit.jsp"%>
<!-- p_bank end -->

<%@include file="../common/hengfengCard.jsp"%>
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->

</body>
</html>

