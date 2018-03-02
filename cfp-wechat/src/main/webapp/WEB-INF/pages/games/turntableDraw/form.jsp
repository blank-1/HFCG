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
<title>填写手机号</title>
<link href="${ctx }/gamecss/LuckDraw.css" rel="stylesheet" type="text/css">
</head>
<body class="w_body">
	<div>
		<div class="w_caption">
			<img src="${ctx }/gameimg/images/p0_banner.png" alt="">
		</div>
		<form action="" method="post" id="frm">
		<input type="hidden" name="token" value="<%=session.getAttribute("token") %>">
		<div class="w_phoneTitleBox">
			<p class="w_phoneTitle"><img src="${ctx }/gameimg/images/p0_word.png" alt=""></p>
			<p class="w_inputBxo">
				<input type="tel" class="w_tel" id="w_tel" name="phone" placeholder="请输入手机号" maxlength="11">
			</p>
			<span id="w_submit" class="w_submit">
				<img src="${ctx }/gameimg/images/p0_btn.png" alt="">
			</span>
			<div class="w_pattern" id="w_pattern">
				手机格式错误
			</div>
		</div>
		</form>
	</div>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
<script src="${ctx }/gamejs/wechat_turntable.js"></script>

<script>
	var rootPath = '<%=ctx%>';
	
	$(function(){
		var meg = '${meg}';
		if(meg != ""){
			$("#w_pattern").fadeIn(500,function(){
	        	$(this).fadeOut(1200)
	        }).html(meg); 
		}
	});
	
	//判断手机号是否正确
	$("#w_submit").on("click",function(){
	  var phoneVal=$("#w_tel").val();
	      if(phoneVal==""){
	        $("#w_pattern").fadeIn(500,function(){
	        	$(this).fadeOut(1200)
	        }).html("输入内容不能为空"); 
	      }else if(!(/^1[3|4|5|8|7][0-9]\d{4,8}$/.test(phoneVal))){
	          $("#w_pattern").fadeIn(500,function(){
	          	$(this).fadeOut(1200)
	          }).html("请输入正确的手机号")
	      }else if(phoneVal.length<11){
	        $("#w_pattern").fadeIn(500,function(){
	        	$(this).fadeOut(1200)
	        }).html("不是完整的11位手机号")
	      }else{
	      	   var phone = $("#w_tel").val(); 
	           window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd06957f9d3daac89&redirect_uri=http%3a%2f%2fm.caifupad.com%2fgame%2froulette&response_type=code&scope=snsapi_base&state="+phone+"#wechat_redirect";
	      }
	})
</script>
</body>
</html>