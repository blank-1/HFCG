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
<link rel="stylesheet" href="${ctx }/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/register.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/sweetAlert.css?${version}" type="text/css">
<script data-main="${ctx }/js/register.js?${version}" src="${ctx }/js/lib/require.js"></script>
<script type="text/javascript">
var rootPath = '<%=ctx%>';
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
    <style>.swal2-modal h2{line-height:1!important}</style>
<title>注册</title>
</head>
<body class="l_NewScroll">
 
  <header>
      <img src="${ctx }/images/img_registerBanner.png" alt="">
  </header>
  <section>
  <form  id="frm" action="${ctx}/user/regist/register" method="post" >
    <div>
        <input class="l_userId" name="loginName" id="userName" placeholder="输入用户名 (字母与数字组合)" type="text" maxlength="22" >
        <p class="l_regTip" data-regTip='请输入4~20位字符,支持汉字,字母,数字及"-","_"组合'></p>
        <i class="l_del"></i>
    </div>
    <div>
       <input class="l_userId" name="mobileNo" id="telNumber" placeholder="输入手机号码" type="tel" maxlength="11">
        <p class="l_regTip" data-regTip="该手机号将用于手机号登录和找回密码"></p>
        <i class="l_del"></i>
    </div>

    <div class="l_checkCode">
        <input class="l_userId" type="tel"  name="validCode" maxlength="6" placeholder="输入短信验证码" id="checkCode">
        <p class="l_regTip" data-regTip="请输入短信验证码"></p>
        <i class="l_del"></i>
        <button id="checkCodeB"  name="btn" class="pjred">获取验证码</button>
    </div>
    <div class="l_pswBox">
        <input id="passWord" name="loginPass" placeholder="设置登录密码" type="password" title="密码" maxlength="16">
        <p class="l_regTip" data-regTip="请输入6~16位字符,字母加数字组合,字母区分大小写"></p>
        <p class="l_pswBtn"></p>
        <i class="l_del"></i>
    </div>
    <div><input class="l_userId  l_code" value="${invite_code}" name="inviteCode" type="tel" id="code" placeholder="邀请码 (非必选)" maxlength="8" ><i class="l_del"></i></div>

    <div class="l_checkBox">
        <span   class="l_uncheck l_checked"></span><span class="mainText">同意&nbsp;<a class="pjred" href="${ctx}/user/regist/registeraGreement">《注册协议》</a></span>
    </div>
    <button class="l_load pjBred" id="load">注册</button>
    <p>已有账号？<a class="pjred" href="${ctx }/user/toLogin">请登录</a></p>
    </form>
  </section>
  <div class="loading" id="loading">
      <img src="${ctx}/images/loading.gif" alt="">
  </div>
</body>
</html>