<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content=""/>
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx}/css/reset.css" type="text/css">
<script type="text/javascript">
//rem自适应字体大小方法
var docEl = document.documentElement,
	resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
	recalc = function() {
		//设置根字体大小
		docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
	};
//绑定浏览器缩放与加载时间
window.addEventListener(resizeEvt, recalc, false);
document.addEventListener('DOMContentLoaded', recalc, false);
</script>
<style>
.idCardbox{width:100%; height:auto; text-align:center;}
.idCardbox img{width:15rem; margin-top:2rem;}
.idCardtext{margin-top:5rem; color:#333; font-size:1.4rem; text-align:center; color:#666;}
.idCardbtn{width:94%; height:3rem; line-height:3rem; background:#ff5e61; color:#fff; font-size:1.4rem; border-radius:4px; display:block; margin:1rem auto; position:fixed; left:3%; bottom:4rem;}
</style>
<title>实名认证</title>
</head>

<body class="bodyBj">
    <div class="idCardbox">
        <img src="${ctx}/images/icon_realname.png" />
    </div>
    <p class="idCardtext">您尚未通过身份认证！</p>
    <button class="idCardbtn" onclick="location.href='${ctx}/person/identityAuthenticationBy?url=${url}&userToken=${userToken}&source=${source}';">去认证</button>
</body>
</html>