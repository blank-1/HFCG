<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
    <meta name="keywords" content="" />
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=.5,maximum-scale=1.5,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes" />    
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
    <meta name="format-detection" content="telephone=no"/>
    <meta name="msapplication-tap-highlight" content="no" />
	<title>身份验证</title>
	<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx}/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/ID_proving.css?${version}">
<link rel="stylesheet" href="${ctx}/css/sweetAlert.css?${version}" type="text/css">
<script data-main="${ctx}/js/ID_proving.js?${version}" src="${ctx}/js/lib/require.js?${version}"></script>
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
	<section class="ID-theme">
		<header>
			<img src="${ctx}/images/ID_head.png" alt="">
		</header>
		<div class="ID-container">
			<input type="text" class="ID-number limitNUM18" id="ID-number" placeholder="请输入身份证号码">
		</div>
		<a class="next-btn" id="next-btn">下一步</a>
	</section>
	<script src="${ctx}/js/jquery-1.8.3.min.js"></script>
	 
	
<!-- 隐藏表单【开始】 -->
<form action="${ctx}/person/toInstallNewPassword" id="identityProving_Form" method="post">
	<input type="hidden" id="pass_type" name="pass_type" value="${pass_type}">
	<input type="hidden" id="mobile_no" name="mobile_no" value="${mobile_no}">
	<input type="hidden" id="token" name="token" value="${token}">
	<input type="hidden" id="dtoken" name="dtoken" value="${dtoken}">
</form>
<!-- 隐藏表单【结束】 -->
</body>
</html>
