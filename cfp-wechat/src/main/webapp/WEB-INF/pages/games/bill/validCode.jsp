<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/taglibs.jsp"%>
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
<link rel="stylesheet" href="${ctx }/gamecss/gameCss/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/gamecss/gameCss/telcheck.css" type="text/css">
<link rel="stylesheet" href="${ctx }/gamecss/gameCss/sweetAlert.css" type="text/css">
<script src="${ctx }/gamejs/gamejs/lib/jquery-1.11.0.min.js"></script>
<script src="${ctx }/gamejs/gamejs/telcheck.js"></script>
<script type="text/javascript">
var rootPath = '<%=ctx%>';
</script>
<script src="${ctx }/gamejs/gamejs/lib/sweetalert2.js"></script>
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
<title>财富派年度账单</title>
</head>
<body class="l_NewScroll">
<section>
<img src="${ctx }/gameimg/gameimg/index_img01.png" alt="">
<input id="telNum" type="tel" maxlength="11" placeholder="请输入手机号码">
<p><input id="checkNum" type="tel" maxlength="6" placeholder="请输入验证码"> <button>获取验证码</button></p>
<button id="subBtn">提交</button>
</section>

</body>
</html>
