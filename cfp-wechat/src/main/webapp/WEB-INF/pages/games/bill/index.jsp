<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
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
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<link rel="stylesheet" href="${ctx }/gamecss/gameCss/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/gamecss/gameCss/index.css" type="text/css">
<script type="text/javascript">
var rootPath = '<%=ctx%>';
</script>
<script src="${ctx }/gamejs/gamejs/lib/jquery-1.11.0.min.js"></script>
<script src="${ctx }/gamejs/gamejs/index.js"></script>
<script src="${ctx }/gamejs/gamejs/autoPlayAudio1.js"></script>
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
  <i id="audioCtl" class="audioCtl"></i>
  <img src="${ctx }/gameimg/gameimg/index_img01.png" alt="">
  <img src="${ctx }/gameimg/gameimg/index_img02.png" alt="">
  <p>${lastYear}，你的小目标实现了吗？</p>
  <p>${currentYear}，一起财富，一起幸福。</p>
  <img id="btn" src="${ctx }/gameimg/gameimg/index_img03.png" alt="">
  <audio id="media" src="${ctx }/gameimg/gameimg/main.mp3" autoplay="autoplay" preload="load" loop="loop"></audio>
</section>

</body>
</html>

