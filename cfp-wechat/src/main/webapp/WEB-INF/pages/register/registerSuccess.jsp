<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>

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
	<title>注册成功</title>
	<%@include file="../common/common_js.jsp"%>
	<link rel="stylesheet" href="${ctx }/css/sharePage.css">	
</head>
<body style="background: #f5f9fa;">
	<div class="w-cash">
		<div class="shareSuccess">
			<p><span><img src="${ctx }/images/s_icon-temp.png" 

alt=""></span>注册成功</p>
			<p>更多理财项目 关注财富派微信</p>
		</div>
		<input type="hidden" name="shareCode" value="${invite_code}" id="shareCode">
		<div class="towCode">
			<img src="${ctx }/images/qr.png" alt="">
			<!-- <p>长按二维码可一键关注我们</p> -->
		</div>
	</div>
</body>
</html>