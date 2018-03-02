<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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
<script src="${ctx}/js/iscroll.js"></script>
<link rel="stylesheet" href="${ctx}/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/Invite.css?${version}" type="text/css">
<script data-main="${ctx}/js/Invite.js?${version}" src="${ctx}/js/lib/require.js?${version}"></script>
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
<title>邀请好友</title>
</head>
<body class="l_NewScroll">
<p class="l_title pjred">我邀请注册成功的好友</p>
<ul class="l_list">
	<li>
		<span>用户名</span>
		<span>注册时间</span>
		<span>平台奖励</span>
	</li>
	<c:forEach items="${users}" var="user">
		<li>
			<span>${user.loginName}</span>
			<span><fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd"/></span>
			<span>--</span>
		</li>
	</c:forEach>
</ul>
<img src="${ctx}/images/invite_img01.png" alt="" class="l_noData" <c:if test="${empty users || fn:length(users)==0}">style="display: block;"</c:if>>
</body>
</html>