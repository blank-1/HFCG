<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>

<!DOCTYPE HTML>
<html>
<head>
	<meta name="keywords" content="" />
	<meta name="description" content="" /> 
	<title>财富派-找回密码</title>
	<%@include file="../common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx }/js/re_password3.js"></script><!-- action javascript -->
</head>

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
        <div class="re-password"><img src="${ctx}/images/pw/pw_03.jpg" /></div>
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
			<li><span id="crumbs_2">验证密码</span></li>
		</ul>
	</div>
	<div class="clear_50"></div>
	<div class="re_Step1_main">
		<form action="${ctx }/user/rePasswordFour" class="form" method="post">
			<input type="hidden" name="token" value="${token }">
			<div class="re_Step1_mainle">
				<div class="input-group clearFloat">
						<label for="" class="floatLeft">
							<span>设置密码：</span>
							<input class="ipt-input widpass new-pasinput" style="" type="password" maxlength="16" name="password" id="password" value="" placeholder="请输入密码" onKeyUp="CheckIntensity(this.value)" />
						</label>
						<div type="button" id="rejc" class="Tcolor floatLeft mt-0">无</div>
						<div class="clear"></div>
						<em class="new-emtips"></em>
				</div>
				<div class="input-group">
					<label for="idcard">
						<span>确认密码：</span>
						<input type="password" class="ipt-input phone-input" value="" id="password2" maxlength="16" name="idcard" placeholder="请输入密码" autocomplete="off"  />
					</label>
					<em class="new-emtips">${message }</em>
				</div>
			</div>
			<div class="re_Step1_mainri">
				<p>人工找回密码：</p>
				<p>如遇手机丢失等特殊情况无法找回密码 ，请将您的身份证信息发送至客服邮箱：myservice@mayitz.com，或拨打客服电话：400-061-8080。我们会尽快帮您找回密码。</p>
			</div>
			<div style="clear:both;"></div>
			<div class="btn-group mt-50">
					<button type="button" id="submit-findpwd" class="btn btn-error widbtn new-btn">下一步</button>
			</div>
			<div class="clear_50"></div>
		</form>
	</div>
</div>
<!-- container end -->

<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->
</body>
</html>