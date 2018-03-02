<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="0">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx }/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/Authentication_info_edit.css">
<link rel="stylesheet" href="${ctx }/css/sweetAlert.css" type="text/css">
<script data-main="${ctx }/js/Authentication_info_edit.js?${version}" src="${ctx }/js/lib/require.js"></script>
<script type="text/javascript">
//rem自适应字体大小方法
var docEl = document.documentElement,
resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
recalc = function() {
    //设置根字体大小S
    docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
};
//绑定浏览器缩放与加载时间
//
window.addEventListener(resizeEvt, recalc, false);
document.addEventListener('DOMContentLoaded', recalc, false);
</script>
<title>手机号</title>
</head>
<body class="l_NewScroll">
<section class="edit-theme">
	<section class="name-box">
		<input type="number" class="name limitNUM11" id="name" value="${mobile_no}" <c:if test="${not empty mobile_no}">readonly="readonly" placeholder="${mobile_no}"</c:if> <c:if test="${empty mobile_no}">placeholder="请输入手机号"</c:if> >
	</section>
	<section class="Verification">
		<input type="text" class="Veri-in limitNUM6" id="Veri-in" placeholder="请输入验证码">
		<button class="have-code" id="checkCodeB">获取验证码</button>
	</section>
	<section class="complete" id="complete">下一步</section>
</section>

<!-- 隐藏表单【开始】 -->
<form action="${ctx}/person/toIdentityProving" id="forgetPswFinal_Form" method="post">
	<input type="hidden" id="pass_type" name="pass_type" value="${pass_type}">
	<input type="hidden" id="mobile_no" name="mobile_no" value="${mobile}">
	<input type="hidden" id="mobile_dis" name="mobile_dis" value="${mobile_no}">
	<input type="hidden" id="token" name="token" value="${token}">
	<input type="hidden" id="dtoken" name="dtoken">
</form>
<!-- 隐藏表单【结束】 -->

<div class="loading" id="loading" style="display: none;">
	<img src="${ctx}/images/loading.gif" alt="">
</div>
<%@include file="/WEB-INF/pages/common/navTag.jsp" %>
</body>
</html>


