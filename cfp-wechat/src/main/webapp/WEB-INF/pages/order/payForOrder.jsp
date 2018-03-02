<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no" />
<meta name="msapplication-tap-highlight" content="no" />
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx }/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/payment1.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/s_coupon.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/sweetAlert.css?${version}">
<script data-main="${ctx }/js/payment.js?${version}" src="${ctx}/js/lib/require.js"></script>
<style>
.couponList{position:fixed;top: 0;left:0;margin: 0;overflow-y: auto;background: #fff;display: none;height: calc(100% );background: #f2f2f2;}
.w_close {width:100%;display: none;margin: 0 auto;height:3rem;line-height:3rem;font-size:1.2rem;border-radius:0;border:none;background:#ff5e61;color: #fff;position:absolute;bottom:0;left:0;z-index: 99;}
.page{display: inline-block; height:auto; overflow:hidden;padding-bottom: 4rem;}
.page{width:100%;}
.swal2-modal h2 {
	font-size: 1.4rem!important;
	margin: 0;
	padding: 0;
	line-height: 2rem!important;
}
.l_bankcard{
    overflow: hidden;
	display: inline-block!important;
	padding-left: 5%;
}
.l_bankcard span{display: block;
    float: left;
    width:5rem;
    height: 3.5rem;
    line-height: 3.5rem;

}
    .l_bankcard img{
        float: left;
        height: 2.5rem;
        margin: 0.5rem 4%;
    }
</style>
<script type="text/javascript">
//rem自适应字体大小方法
var docEl = document.documentElement,
	resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
	recalc = function() {
		//设置根字体大小
		docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
	};
//绑定浏览器缩放与加载时间
window.addEventListener(resizeEvt, recalc, false);
document.addEventListener('DOMContentLoaded', recalc, false);
</script>
<title>支付</title>
</head>
<% session.removeAttribute("smsCode");%>
<body class="l_NewScroll">

<input type="hidden" value="<fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/>" id="payAccount" />
<input type="hidden" value="1" id="syfp">
<input id="voucherNum" type="hidden" value="${fn:length(vouchers)+fn:length(rateUsers)}"/>

<div class="payment-theme">
	<div class="title">${order.lendOrderName}</div>
	<dl class="amount">
		<dd>
			<p class="leftSide">
				<span id="pay"><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></span>
				<span>支付金额（元）</span>
			</p>
			<c:if test="${empty sxFlag}">
				<p class="rightSide"><!--  省心计划没有预计收益，可删除   -->
					<span>${expectedInteresting}</span>
					<span>预期收益（元）</span>
				</p>
			</c:if>
		</dd>
		<%--<dt>
			<span>6</span>个月标，年化收益为<span>8</span>%＋奖励<span>5</span>％
		</dt>--%>
	</dl>
	<c:if test="${not empty sxFlag}">
		<section class="l_checkRewardWay">
			<span >收益分配方式：&nbsp;&nbsp;收益提至余额</span>
		</section>
	</c:if>
	<ul class="optionList">
		<li class="balance">
			<p>账户支付</p>
			<p>可用余额<span id="AccountBalance"><fmt:formatNumber value="${userCashBalance}" pattern="#,##0.00"/></span>元</p>
		</li>
		<div class="liner"></div>
		<c:if test="${empty sxFlag}">
			<c:if test="${fn:length(vouchers)+fn:length(rateUsers) > 0}">
				<li class="Voucher">
					<p>财富券</p>
					<p><span id="voucherInfo">${fn:length(vouchers)+fn:length(rateUsers)}张</span></p>
				</li>
				<div class="liner"></div>
			</c:if>
		</c:if>

		<section class="l_Show">
			<li class="bankcard" >
				<p class="bankInfo">
					银行卡支付
				</p>
				<p class="checked" id="checked">
					<span id="surplusMoney"><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></span>元
				</p>

			</li>
			<div class="liner"></div>
			<li class="bankcard  l_bankcard" >
				<span>银行卡号</span>
					<img src="${ctx}/images/banklogo/${customerCard.bankCode }_1.png">
					<span>（${customerCard.cardCodeShort}）</span>
					<input type="hidden" class="binding" id="cardNo" value="${customerCard.cardCode}">
			</li>
		</section>
	</ul>
	<p class="Agreement">
		<a href="${ctx}/bankList" class="pay_question">支付及限额></a>
	</p>

	<form method="post"  name="frm" class="frm" id="f1" >
		<input type="hidden" name="cardNo" id="cardNoVal"/>
		<input type="hidden" name="payType" id="payType" value="1"/>
		<input type="hidden" name="accountPayValue" id="accountPayValue" value="0"/>
		<input type="hidden" name="productType" id="productType" value="${productType}"/>
		<input type="hidden" name="token" id="token" value="${token}"/>
		<c:if test="${empty sxFlag}">
			<input type="hidden" id="loanApplicationId" name = "loanApplicationId" value="${loanApplication.loanApplicationId}"/>
		</c:if>
		<c:if test="${not empty sxFlag}">
			<input type="hidden" id="sxFlag" name = "sxFlag" value="sx"/>
			<input type="hidden" name="lendProductPublishId" id="lendProductPublishId" value="${lendProductPublish.lendProductPublishId}"/>
		</c:if>
		<input type="hidden" id="amount" name="amount" value="${order.buyBalance}"/>
		<input type="hidden" name="rechargePayValue" id="rechargePayValue" value="0"/>
		<input type="hidden" name="userVoucherId" id="userVoucherId" value=""/>
		<input type="hidden" name="rateUserIds" id="rateUserIds" value=""/>
		<input type="hidden" name="cardId" id="cardId" value="${null==customerCard?ybBindCard.customerCardId:customerCard.customerCardId}"/>
		<input type="hidden" name="lendOrderId" id="lendOrderId" value="${order.lendOrderId}"/>

		<div class="pwdBox">
			<div class="pwd-container">
				<input type="password" class="pwd-number limitNUM16" id="psw" name="psw" placeholder="请输入密码">
				<span class="eye eye-close" id="eye"></span>
			</div>
			<div class="forget_pwd">
				<%--<p>支付遇到的问题</p>--%>
				<p onclick="location.href='${ctx}/person/resetTransPass'">忘记支付密码></p>
			</div>
		</div>
		<a class="confirm" id="btn2">确定支付</a>
	</form>
	<div style="display: none" >
		<form id="recharge_params" action="" method="post">
			<input type="hidden" name="mchnt_cd" value=""/>
			<input type="hidden" name="mchnt_txn_ssn" value=""/>
			<input type="hidden" name="login_id" value=""/>
			<input type="hidden" name="amt" value=""/>
			<input type="hidden" name="page_notify_url" value=""/>
			<input type="hidden" name="back_notify_url" value=""/>
			<input type="hidden" name="signature" value=""/>
		</form>
	</div>
	<%@include file="../common/payVoucher.jsp"%>

	<div class="loading" id="loading">
		<img src="${ctx}/images/loading.gif" alt="">
	</div>
</div>
</body>
</html>