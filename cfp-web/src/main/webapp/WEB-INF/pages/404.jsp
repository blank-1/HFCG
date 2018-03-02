<!DOCTYPE html>
<html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="./common/taglibs.jsp"%>
<%@include file="./common/common_js.jsp"%>
<%@include file="./login/login.jsp"%>
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>404 - 财富派</title>
</head>

<body>
<header>
<!-- line2 start -->
<%@include file="./common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine/>
<!-- navindex end -->

</header>
<article>
	<div class="404_main">
		<div class="main_top">
			<p>页面不存在</p>
		</div>
		<div class="main_center"></div>
		<div class="main_bot">
			<p class="main_bot_big">
				不要着急,让我们去<a href="${ctx}/" style="font-size: 30px;">首页</a>看看吧
			</p>
			<p>客服电话：400-061-8080</p>
			<p>联系邮箱：myservice@mayitz.com</p>
		</div>
	</div>
</article>
<!-- footer start -->
<%@include file="./common/footLine3.jsp"%>
<!-- footer end -->
</body>
</html>