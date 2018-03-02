<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<title>财富派</title>
	<%@include file="../common/common_js.jsp"%>
	<link rel="stylesheet" href="${ctx }/css/sharePage.css">
	<style type="text/css">
	body{
		-webkit-touch-callout: default;
		-webkit-user-select: none;
		-khtml-user-select: none;
		-moz-user-select: none;
		-ms-user-select: none;
		user-select: none;
	}
	</style>
</head>
<body>
	<div class="container">
		<div class="wrapper">
			<div>
				<img src="${ctx }/images/share-1.png" alt="">
			</div>
			<div>
				<img src="${ctx }/images/share-2.png" alt="">
			</div>
			<div>
				<img src="${ctx }/images/share-3.png" alt="">
			</div>
			<div>
				<img src="${ctx }/images/share-4.png" alt="">
			</div>
			<div>
				<img src="${ctx }/images/share-5.png" alt="">
			</div>
			<div>
				<img src="${ctx }/images/share-6.png" alt="">
			</div>
			<div>
				<img src="${ctx }/images/share-7.png" alt="">
			</div>
			<div class="w_attention">
				<p>更多理财项目</br>关注财富派微信公众号</p>
				<div class="w_Frame">
					<div style="margin-right:20%;"><img src="${ctx }/images/shareicon1.png" alt=""></div>
					<div class="w_Line" id="w_Line">
						<img src="${ctx }/images/shareicon2.png" alt="">
						<span class="w_liner"></span>
					</div>
				</div>
				<p style="color:#9693dd;margin-top:1rem;">长按二维码识别图关注我们</p>
			</div>
		</div>
	</div>
	<input type="hidden" name="shareCode" value="${invite_code}" id="shareCode">
	<div class="w_registerBtn">
		<a href="${ctx }/user/regist/share?invite_code=${invite_code}">立即注册</a>
	</div>
</body>
</html>