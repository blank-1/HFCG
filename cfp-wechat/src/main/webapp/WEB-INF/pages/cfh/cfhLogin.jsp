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
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<%@include file="../common/common_js.jsp"%>

<title>登录</title>
<script src="${ctx }/js/cfhLogin.js" type="text/javascript"></script> 
</head>
<html>
<body>
	<!--/顶部错误提示栏开始/-->
	<span id="alart"></span>
    <!--/顶部错误提示栏结束/-->
	<img style="width:100%;" src="${ctx}/images/p2_login_img3.jpg" alt="">
    <form method="POST" name="frm" class="psw" id="frm">
    	<!--手机号开始-->
		<input type="text" value="手机号/用户名" name="loginName" id="telnum"  maxlength="20" size="20" style="color:#afafaf;width: 82%;margin:8% 0 8% 5%; " onfocus="if(value=='手机号/用户名'){this.style.color='#4b4b4b';value=''}" onblur="if(value==''){this.style.color='#afafaf';value='手机号/用户名'}" />
		<!--手机号结束-->
		<!--密码输入框开始-->
		<div class="box" style="width: 100%;">
        <span id=box><input type="password" placeholder="输入登录密码" name="loginPass"  id="psw" value=""  maxlength="16" min="6" size="16"style="color:#afafaf;width: 82%;margin-left:5%; " onblur="" /></span>
		<span id='clicks' class="click1" style="left:82%;"></span>
        </div>
		<!--密码输入框结束-->
		
		<%-- <div id ="building"> 
			<a href="${ctx}/person/forgetLoginPass" class="fgt" style="display: block;width: 95%;text-align: right;padding-right:5%;">忘记密码？</a>
		</div> --%>
		<input type="button" name="btn" class="btnall" onclick="vali();" style="background-color:#3bc2ff; color:white; border:0;width: 40%; height: 4rem; font-size: 1.8rem; line-height: 4rem; color:#fff; margin: 16% 5%; float: left; text-align:center;padding:0;" id="login" value="登录" />
	</form>
	<input type="button" class="btn3" onclick="regist();" style="width: 40%; height: 4rem; font-size: 1.8rem; line-height: 4rem; color:#fff; margin: 16% 5%; float: left; text-align:center;padding:0;" value="快速注册" />

	<form method="post" action="" id="toVerifiedForm">
		<input type="hidden" name="hToken" id="hToken" value="${hToken}">
		<input type="hidden" name="type" id="type" value="verified">
	</form>
	<form method="post" action="${ctx}/cfhRelation/regist" id="registForm">
		<input type="hidden" name="hToken" id="hToken" value="${hToken}">
	</form>
</body>
</html>