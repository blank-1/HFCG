<%@ page language="java"  pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />    
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<title>选择支行信息</title>
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx}/css/reset.css?${version}" type="text/css">
	<link rel="stylesheet" href="${ctx}/css/sweetAlert.css?${version}">
<link rel="stylesheet" href="${ctx}/css/set_information.css?${version}">
<script data-main="${ctx}/js/set_information.js?${version}" src="${ctx}/js/lib/require.js?${version}"></script>
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
</head>
<body class="l_NewScroll">
<section class="set-theme">

	<input type="hidden" id="w_seachPid" name="w_seachPid" value="${w_seachPid }">
	<input type="hidden" id="w_seachCid" name="w_seachCid" value="${w_seachCid }">
	<input type="hidden" id="w_seachPidStr" name="w_seachPidStr" value="${w_seachPidStr }">
	<input type="hidden" id="w_seachCidStr" name="w_seachCidStr" value="${w_seachCidStr }">

	<section class="available">
		<p>可提现金额（元）</p>
		<p>${cashAccount.availValue2}</p>
	</section>
	<section class="Bank-card">
		<div class="head">${bankName}</div>
		<dl class="card-number">
			<dd><img src="${ctx}/images/banklogo/${customerCard.bankCode}_1.png" alt=""></dd>
			<dt>${encryptCardNo}</dt>
		</dl>
	</section>
	<ul class="select">
		<li class="region-box" id="region-box">
			<span>开户地区</span>
			<p id="address">请选择开户省市</p>
			<p></p>
		</li>
		<div class="liner"></div>
		<li class="Branch-box">
			<div class="search-contaier">
				<span>开户支行</span>
				<input type="text" readonly="readonly" placeholder="请输入支行关键字" class="search-txt" id="search-txt">
				<p id="search_btn"></p>
			</div>
			<ul class="Branch-list" id="Branch-list"></ul>
		</li>
	</ul>
	<a class="sure">确定</a>

	<div class="select-mask" id="select-mask">
		<div class="Selected" id="Selected">
			<span class="city cur">选择省</span>
			<span class="area">选择市区</span>
		</div>
		<div class="cityBox" id="cityBox">
			<ul class="citylisting listing" id="citylisting">
			</ul>
			<ul class="areaListing listing" id="areaListing">
			</ul>
		</div>
	</div>

	<form action="${ctx}/person/choiceBranch" id="branchForm" name="branchForm" method="post">
		<input id="prcptcd" name="prcptcd" type="hidden">
		<input id="brabank_name" name="brabank_name" type="hidden">
		<input id="brabank_city" name="brabank_city" type="hidden">
	</form>
</section>
</body>
</html>
