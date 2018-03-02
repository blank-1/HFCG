<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../common/taglibs.jsp"%>

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
	<%@include file="../../common/common_js.jsp"%>
	<link rel="stylesheet" href="${ctx }/css/sharePage.css">	
</head>
<body style="background: #ebeef6;">
	<div class="w-cash">
		<div class="shareSuccess">
			<p><span><img src="${ctx }/images/s_icon-temp.png" 

alt=""></span>注册成功</p>
			<p>更多理财项目 关注财富派微信</p>
		</div>
		<div class="towCode">
			<img src="${ctx }/images/qr.png" alt="">
			<p>长按二维码可一键关注我们</p>
		</div>
		<a style="width:90%;height:3rem;color:#fff;background:#fe2a4d;line-height:3rem;font-size:1.2rem;text-align:center;margin:1rem auto;display:block;" href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd06957f9d3daac89&redirect_uri=http%3a%2f%2fm.caifupad.com%2fgame%2ftoRoulette&response_type=code&scope=snsapi_base&state=1#wechat_redirect">确定</a>
	</div>
</body>
</html>