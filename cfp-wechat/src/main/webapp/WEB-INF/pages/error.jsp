<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="./common/taglibs.jsp"%>
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
<link rel="stylesheet" href="${ctx}/css/reset2.css">
<link rel="stylesheet" href="${ctx }/css/register2.css" type="text/css">
<%@include file="./common/common_js.jsp"%>
<title>错误提示</title>
</head>

<body style="margin:0;padding:0; background-color:#ebeef6;">
<div class="errorbox">
	<img src="${ctx }/images/FaceIcon.png" />
	<c:if test="${errorCode != null||errorMsg  != null}">
		<%--<p class="errorp1">错误代码：<span>${errorCode }</span></p>--%>
	    <p class="errorp2"><%--错误原因：--%><span>${errorMsg }</span></p>
    </c:if>
    <c:if test="${errorCode == null&&errorMsg  == null}">
		<p class="errorp1">现在网络有点问题，请您稍后在尝试重新连接，或者刷新页面</p>
	</c:if>
    <a class="errorbtn" href="javascript:void(history.go(-1))">确定</a>
    <div class="errorhelp">
    	<p>客服电话：400-061-8080</p>
        <p>E-mail：myservice@mayitz.com</p>
    </div>
    
</div>
    
</body>
</html>
