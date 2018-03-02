<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
		<link rel="stylesheet" href="${ctx }/css/reset.css" type="text/css">
		<link rel="stylesheet" href="${ctx }/css/register.css" type="text/css">
		<title>账号绑定</title>
	</head>
	<body id="backgd">
		<img src="${ctx }/images/icon_recharge3.png" alt=""
			style="width: 100%; margin: 10% 0">
		<h1 style="color: #777778; margin: 10% auto 4%; text-align: center;">银行正在处理中，请耐心等待
		</h1>
		<h6 style="color: #777778; margin: 4% 0 0 0;text-align: center; ">您稍后可以到理财记录页</h6>
		<h6 style="color: #777778; margin: 1% 0 9% 0;text-align: center; ">查看投资详情</h6>
		<a href="${ctx }/person/account/overview"
			style="border: solid 1px #8c97ba; color: #8c97ba; font-size: 1.6rem; text-align: center; width: 60%; margin: 0 auto; display: block; line-height: 3rem; margin-top: 24%;">确定</a>
	</body>
</html>