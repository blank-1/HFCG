<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>


<!DOCTYPE HTML>
<html>
<head>
	<meta name="keywords" content="" />
	<meta name="description" content="" /> 
	<title>财富派-找回密码</title>
    <%@include file="../common/common_js.jsp"%>
    <script type="text/javascript" src="${ctx }/js/re_password.js"></script><!-- action javascript -->
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
</header>

<!-- person-link start -->
<div class="person-link">
    <div class="container clearFloat">
        <div class="re-password"><img src="${ctx}/images/pw/pw_01.jpg" /></div>
    </div>
</div>
<!-- person-link end -->
<!-- container start -->
<%@include file="../login/login.jsp"%>
<div class="container clearFloat">
	<div class="re-password-link">
		<ul>
			<li><a href="/">财富派首页</a>></li>
			<li><a href="javascript:void(0);" id="crumbs_1">找回密码</a>></li>
			<li><span id="crumbs_2">验证身份</span></li>
		</ul>
	</div>
	<div class="clear_50"></div>
	<div class="re_Step1_main">
		<form action="${ctx }/user/rePasswordTwo" class="form" method="post">
			<input type="hidden" name="token" value="${token }">
			<div class="re_Step1_mainle">
				<div class="input-group">
					<label for="phone">
						<span>手机号：</span>
						<input type="text" class="ipt-input phone-input" value="" id="phone" maxlength="11" name="phone" placeholder="请输入注册时的手机号" autocomplete="off"  />
					</label>
					<em class="new-emtip"></em>
				</div>
				<div class="input-group clearFloat">
					<label for="valid" class="floatLeft">
						<span class="floatLeft linheight new-mt-7">验证码：</span>
						<input type="text" value="" class="ipt-input widthvalid valid-input" onKeyUp="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" id="valid" name="valid" maxlength="6" placeholder="请输入验证码" autocomplete="off"  />
					</label>
					<button type="button" id="getvalid" class="btn-blank mt-0 floatLeft btn-hqvalid">获取验证码</button>
					<div class="clear"></div>
					<em class="new-emtip"></em>
				</div>
				<p class="new-yzm-no"><a href="http://help.caifupad.com/guide/common/reg/" target="_black">收不到验证码？</a></p>
			</div>
			<div class="re_Step1_mainri">
				<p>人工找回密码：</p>
				<p>如遇手机丢失等特殊情况无法找回密码 ，请将您的身份证信息发送至客服邮箱：myservice@mayitz.com，或拨打客服电话：400-061-8080。我们会尽快帮您找回密码。</p>
			</div>
			<div style="clear:both;"></div>
			<div class="btn-group mt-50">
					<button type="button" id="submit-register" class="btn btn-error widbtn new-btn">下一步</button>
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