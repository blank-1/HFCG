<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>

<!DOCTYPE HTML>
<html>
<head>
	<meta name="keywords" content="" />
	<meta name="description" content="" /> 
	<title>财富派-找回密码</title>
    <%@include file="../common/common_js.jsp"%>
    <script type="text/javascript" src="${ctx }/js/register.js"></script><!-- action javascript -->
</head>
<% session.removeAttribute("zh_mobile");%>
<body class="body-back-gray">
<%request.setAttribute("pastUrl", "/"); %>
<header>
<!-- navtopbg start-->
	<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- navtopbg end-->
<!-- navindex start -->
<customUI:headLine/>
<!-- navindex end -->
<nav class="clearFloat">
</nav>
</header>
<!-- person-link start -->
<div class="person-link">
    <div class="container clearFloat">
        <div class="re-password"><img src="${ctx}/images/pw/pw_04.jpg" /></div>
    </div>
</div>
<!-- person-link end -->
<%@include file="../login/login.jsp"%>

<!-- container start -->
<div class="container clearFloat">
	<div class="re-password-link">
		<ul>
			<li><a href="/">财富派首页</a>></li>
			<li><a href="javascript:;" id="crumbs_1">找回密码</a>></li>
			<li><span id="crumbs_2">完成</span></li>
		</ul>
	</div>
	<div class="clear_90"></div>
	<div class="re-password-main">
		<p>密码修改成功，请妥善保管您的账户密码！</p>
		<div class="clear_80"></div>
		<div class="re-password-btn">
			<button type="button"  id="submit-register"  data-mask='mask' data-id="login">立即登录</button>
		</div>
	</div>
</div>
<!-- container end -->

<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->
</body>
</html>
