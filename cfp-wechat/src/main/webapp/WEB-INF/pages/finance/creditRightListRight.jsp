<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%@include file="../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
    pageContext.setAttribute("picPath", Constants.picPath);
%>
<!doctype html>
<html style="font-size: 9.46875px;">
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
<link rel="stylesheet" href="${ctx }/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/s_listCredits.css" type="text/css">
<script data-main="${ctx }/js/s_listCreditsRight.js?${version}" src="${ctx }/js/lib/require.js"></script>
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
var rootPath = '<%=ctx%>';
</script>
<style type="text/css">

.headNav li {
    width: 33.33%;
    padding: 1rem 0;
    text-align: center;
    color: #ff5e61;
    font-size: 1.4rem;
}
	.noNum{text-align:center; display:block;position: absolute;width: 100%;height: 14rem;top: 50%;left: 0;margin-top: -7rem;}
.noNum img{width: 10rem; margin-top:0; }
.noNum p{color:#999; font-size:1.2rem; margin-top:2rem;}
</style>
<title>债权转让</title>
</head>
<body class="l_NewScroll">
	<div class="s_content">
		<ul class="headNav">
		<!-- 	<li class="listCur"><a href="javascript:;">转让中</a></li> -->
			<li class="listCur"><a href="javascript:;">已转入</a></li>
			<li><a href="javascript:;">可转让</a></li>
			<li><a href="javascript:;">已转出</a></li>
		</ul>
		<div class="mainList">
			<div class="page">
			
			</div>
			<div class="page">
			
			</div>
			<div class="page">
			
			</div>
		</div>
	</div>

	<form action="${ctx}/finance/creditRightDetail" id="queryForm" name="queryForm" method="post">
		<input type="hidden" name="creditorRightsId" id="creditorRightsId" value="">
	</form>
</body>
</html>
