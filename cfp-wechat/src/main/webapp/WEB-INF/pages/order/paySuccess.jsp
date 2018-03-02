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
<link rel="stylesheet" href="${ctx }/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/PaymentSuccess.css">
<script data-main="${ctx }/js/PaymentSuccess.js" src="${ctx }/js/lib/require.js"></script>
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
<title>购买成功</title>
</head>
<body class="l_NewScroll">
<section class="pay_theme">
	<header class="head">
		<img src="${ctx }/images/payTitle.jpg" alt="">
	</header>
	<div class="details">
		<ul class="details_list">
			<li>
				<p>标的名称</p>
				<p class="text-overflow">${title}</p>
			</li>
			<div class="liner"></div>
			<li>
				<p>期限</p>
				<p>
					<span class="month">${count}</span>
					<c:if test="${loanType eq '9'}">天</c:if>
					<c:if test="${loanType ne '9'}">个月</c:if>
				</p>
			</li>
			<div class="liner"></div>
			<li>
				<p>订单金额</p>
				<p><span><fmt:formatNumber value="${amount}" pattern="#,##0.00"/></span>元</p>
			</li>
			<div class="liner"></div>
			<c:if test="${account>0}">
				<li>
					<p>财富券</p>
					<p><span>${account}</span>元</p>
				</li>
				<div class="liner"></div>
			</c:if>
			<c:if test="${rate>0}">
				<li>
					<p>加息券</p>
					<p><span>${rate}</span>％</p>
				</li>
			</c:if>
		</ul>
	</div>
	<a href="${ctx }/finance/list" class="Continue">继续理财</a>
	<a href="${ctx }/person/account/overview" class="check">查看资产></a>
</section>
</body>
</html>