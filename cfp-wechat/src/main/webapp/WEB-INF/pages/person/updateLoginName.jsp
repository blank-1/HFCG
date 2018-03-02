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
<title>修改登录名</title>
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx}/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/Authentication_info_edit.css?${version}">
<link rel="stylesheet" href="${ctx}/css/sweetAlert.css?${version}">
<script data-main="${ctx}/js/update_login_name.js?${version}" src="${ctx}/js/lib/require.js?${version}"></script>
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
<style>
.swal2-modal h2{line-height: 1.4!important;}
</style>
</head>
<body class="l_NewScroll">
<section class="edit-theme">
	<section class="name-box">
		<input type="text" class="name" id="name" value="" placeholder="请输入登录名">
		<p class="remove-btn" id="remove-btn">
			<img src="${ctx}/images/remove.png" alt="">
		</p>
	</section>
	<section class="complete" id="complete">保存</section>
</section>
<div class="loading" id="loading">
	<img src="${ctx}/images/loading.gif" alt="">
</div>
<%@include file="/WEB-INF/pages/common/navTag.jsp" %>
</body>
</html>
