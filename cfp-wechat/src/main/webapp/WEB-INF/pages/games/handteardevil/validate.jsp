<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>

<!doctype html>
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
	<title>请输入手机号</title>
	<link rel="stylesheet" href="${ctx }/gamecss/reset.css">
	<link rel="stylesheet" href="${ctx }/gamecss/style.css">
</head>
<body>
<input type="hidden" name="flag" id="flag" value="0"/>
	<form action="${ctx }/game/validate" id="frm" method="post">
    <img src="${ctx }/gameimg/banner.png" style=" width:100%;"/>
	<p class="w-promptFont">还差一步，留下手机号，即可领取成功！</p>
	<input type="text" class="w-phoneNum" id="w-phoneNum" name="phone" placeholder="请输入手机号">
	<span class="w-errorMsg" id="errorMsg"></span>
	<a class="w-collar" id="w-collar" href="#">领&nbsp;&nbsp;券</a>
</form>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
	<script src="${ctx }/js/wechat_xt.js" type="text/javascript"></script>
	<script src="${ctx }/gamejs/zepto.min.js"></script>
	<script src="${ctx }/gamejs/validate.js"></script>
	<script type="text/javascript">		
		var rootPath = '<%=ctx%>';	
	</script>
</body>
</html>