<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />    
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<title>活动结束啦！</title>
<link href="${ctx }/gamecss/LuckDraw.css" rel="stylesheet" type="text/css">
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
</head>
<body class="actBody">
	<div class="w_actOver">
		<div class="w_overPic">
			<img src="${ctx }/gameimg/images/activeOver.png" alt="">
			<p>活动已结束</br>下次再来吧~</p>
		</div>
		<div class="w_Code">
			<img src="${ctx }/gameimg/images/p1_icon2.jpg" alt="">
		</div>
	</div>
</body>
</html>