<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%@include file="../common/common_js.jsp"%>
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
<link rel="stylesheet" href="${ctx }/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/noticeDetails.css?${version}">
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
<title>公告详情</title>
</head>
<body>
	<section class="container">
		<div class="details">
			<p><img src="${ctx }/images/icon/left.jpg" class="left"/></p>
			<p class="title">${wechatNotice.noticeTitle }</p>
			<p class="title2">
				<span class="gonggao"><img src="${ctx }/images/icon/news_09.png" />[公告]</span>
				<span class="cfppt">财富派平台</span>
				<span class="time"><fmt:formatDate value="${wechatNotice.publishTime }" pattern="yyyy-MM-dd"/> </span>
			</p>
			<div class="text">${wechatNotice.noticeContent }</div>
			<p><img src="${ctx }/images/icon/right.jpg" class="right"/></p>
			<div style="clear:both;"></div>
		</div>
	</section>
</body>
</html>

