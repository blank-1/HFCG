<%@ page language="java"  pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />    
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx}/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/sweetAlert.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/s_bindCard.css?${version}" type="text/css">
<script data-main="${ctx}/js/s_bindCard.js?${version}" src="${ctx}/js/lib/require.js?${version}"></script>
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
<title>绑定银行卡</title>
<style>
.bkNochecked{top:.9rem;}
.bkProtocol span a {color: #8ab1f7;margin-left:0rem;}
</style>
</head>

<body class="bodyBj">
<div class="clear_15"></div>
<div class="bindCardmain">
	<div class="bindCardBox">
		<div class="bindCardtext">开户名：${userExt.realName}</div>
	</div>
	<div class="bindCardBox">
		<div class="bindCardtext">身份证号：${userExt.idCard}</div>
	</div>
	<div class="bindCardBox">
		<div class="bindCardtext">
			<span>银行卡号：</span>
			<input type="tel" id="cardId" placeholder="请输入银行卡号" maxlength="19"/>

		</div>
		<p class="l_bindCardtext"></p>
	</div>
</div>
<p style="width:90%;margin: 0 auto;margin-top: .5rem;color: #666;">* 绑定银行卡需要跳转到第三方支付平台并支付0.01元，绑卡成功后0.01元将充值到您的账户余额中。</p>
<p class="bindcardTip bkProtocol" style="position:relative;">
	<span class="bkNochecked checked"></span>
	<span style="margin-left:2rem;"><a href="${ctx}/bankcard/bindCardProtocol">《连连支付认证支付开通协议》</a></span>
	<span class="bCtipRi" id="cardHelp"><font><img src="${ctx}/images/userCenter/helpIcon.png" /></font>绑卡遇到的问题</span>
</p>
<div class="bkProtocol"></div>
<button class="bindCardBtn" id="bindCardBtn">确认协议并绑卡</button>

<div class="Maskbox"></div>
<div class="cardHelp">
	<div class="helpMain">
		<p class="helpTitle">绑卡提醒</p>
		<p class="helpInfo">为使您的投资更加方便快捷，我司的第三方支付平台已全面升级为连连支付，您需要在连连支付平台重新绑定银行卡。如有问题，请咨询客服<a href="tel:400-061-8080">400-061-8080</a></p>
	</div>
	<button class="closeBtn">确定</button>
</div>

<form action="${llWapPayUrl }" id="llRechargeForm" method="post">
	<input type="hidden" name="req_data" id="req_data" value='' />
</form>

<div class="loading" id="loading">
	<img src="${ctx}/images/loading.gif" alt="">
</div>

</body>
</html>