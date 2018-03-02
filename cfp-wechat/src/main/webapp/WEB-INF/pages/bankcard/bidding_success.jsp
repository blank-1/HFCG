<%@ page language="java"  pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<title>银行卡</title>
</head>
<body>
<ul class="l_SusInfo">
	<img src="${ctx}/images/s_icon-temp.png" alt="">
	<li>绑定成功！</li>
	<li>您现在可以：</li> 
</ul>
<button class="l_susBtn pjbtn2" style="border-radius:4px;  " onclick="window.location.href='#';">去理财</button>
<p class="l_arrow" style="font-size:1.4rem;width:3.2rem;margin:6% auto;color: #7ad6ff;letter-spacing: 0.2rem; " onclick="window.location.href='#';">提现</p>  
</body>
</html>