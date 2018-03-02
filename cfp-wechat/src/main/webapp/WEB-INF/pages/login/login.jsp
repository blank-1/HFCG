<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="0">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<link rel="stylesheet" href="${ctx}/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx}/css/login.css" type="text/css">
<link rel="stylesheet" href="${ctx}/css/sweetAlert.css" type="text/css">
<script data-main="${ctx }/js/login.js?${version}" src="${ctx}/js/lib/require.js"></script>
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
var rootPath = '<%=ctx%>';
</script>
<title>登录</title>
</head>
<html>

<body class="l_NewScroll">
  <header>
      <img src="${ctx}/images/logo.png" alt="" />
  </header>
   <form method="POST" name="frm" class="psw" id="frm">
  <section>
    <div>
        <input class="l_userId input1" name="loginName" data-inputNum="1" id="userID" placeholder="请输入用户名" type="text" maxlength="22" >
        <p class="l_regTip" data-regTip='请输入用户名'></p>
        <i class="l_del"></i>
    </div>
    <div class="l_pswBox">
        <input class="input4" id="passWord" name="loginPass" placeholder="请输入密码" type="password" title="密码" maxlength="16" >
        <p class="l_regTip" data-regTip="请输入密码"></p>
        <p class="l_pswBtn"></p>
        <i class="l_del"></i>
    </div>
    <p>
      <a href="${ctx}/person/forgetLoginPass" class="pjred">忘记密码</a>
    </p>

  </section>
     <input type="hidden" name="loginTer" id="loginTer" value="">
	 <input type="hidden" id="inviteCode" value="${invite_code}">
	 <input type="hidden" name="pastUrl" id="pastUrl" value="${pastUrl}">
</form>
  <button class="l_load pjBred" id="load" >登录</button>
  <p><a class="pjred" href="${ctx}/user/regist/home">快速注册</a></p>
<div class="loading" id="loading">
  <img src="${ctx}/images/loading.gif" alt="">
</div>
</body>

</html>