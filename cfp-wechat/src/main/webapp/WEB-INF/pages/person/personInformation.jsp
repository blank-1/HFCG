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
<link rel="stylesheet" href="${ctx}/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx}/css/PersonalInformation.css">
<script data-main="${ctx}/js/about_us.js" src="${ctx}/js/lib/require.js"></script>
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
function toVerified(){
    location.href="${ctx}/person/identityAuthenticationBy?url="+location;
}
</script>
<title>个人信息</title>
</head>
<body class="l_NewScroll">
<section class="info-theme">
	<ul class="info-list">
		<li>
			<p class="definition">用户名</p>
			<p class="right-arrow" onclick="location.href='${ctx }/person/toUpdateLoginName';">${loginName}</p>
		</li>
		<li>
			<p class="definition">姓名</p>
			<c:if test="${userExt.isVerified eq '2'}"><p>${userExt.realName}</p></c:if>
			<c:if test="${userExt.isVerified ne '2'}"><p onclick="toVerified()" class="right-arrow immediately">立即认证</p></c:if>
		</li>
		<li>
			<p class="definition">手机号</p>
			<p class="immediately">${userExt.mobileNo}</p>
		</li>
		<li>
			<p class="definition">身份证</p>
			<c:if test="${userExt.isVerified eq '2'}">
				<p class="immediately">${userExt.encryptIdCardNo}</p>
			</c:if>
			<c:if test="${userExt.isVerified ne '2'}">
				<p onclick="toVerified()" class="right-arrow immediately">立即认证</p>
			</c:if>
		</li>
	</ul>
</section>
<%@include file="/WEB-INF/pages/common/navTag.jsp" %>
</body>
</html>