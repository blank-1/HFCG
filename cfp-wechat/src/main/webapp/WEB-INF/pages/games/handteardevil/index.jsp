<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>
<!doctype html>
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
	<link rel="stylesheet" href="${ctx }/gamecss/style.css" type="text/css">
	
	<title>手撕鬼子</title>
</head>
<body>
<input type="hidden" name="flag" id="flag" value="0"/>
	<div class="wrapper">
		<div class="title"></div>
			<a class="btn" style="display:block;color:#fff;" href="${ctx }/game/illustrate">开撕</a>
		<div class="logo"></div>
	</div>
	<script src="${ctx }/gamejs/zepto.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
	<script src="${ctx }/js/wechat_xt.js" type="text/javascript"></script>
	<script type="text/javascript">		
		var rootPath = '<%=ctx%>';
	</script>
</body>
</html>