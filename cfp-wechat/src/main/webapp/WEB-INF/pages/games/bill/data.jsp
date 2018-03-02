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
<link rel="stylesheet" href="${ctx }/gamecss/gameCss/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/gamecss/gameCss/data.css" type="text/css">
<script src="${ctx }/gamejs/gamejs/lib/jquery-1.11.0.min.js"></script>
<script src="${ctx }/gamejs/gamejs/autoPlayAudio1.js"></script>
<script src="${ctx }/gamejs/gamejs/data.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
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
<title>财富派年度账单</title>
</head>
<body class="l_NewScroll">
<section>
  <i id="audioCtl" class="audioCtl"></i>
  <img src="${ctx }/gameimg/gameimg/data_img01.png" alt="">
  <p><img src="${ctx }/gameimg/gameimg/data_img03.png" class="animate1" alt=""><img src="${ctx }/gameimg/gameimg/data_img04.png" class="animate1" alt=""></p>
  <img src="${ctx }/gameimg/gameimg/data_img02.png" alt="">
 <%--  <button id="gotobill"><a href="${ctx }/game/yearBill">登录个人账单</a></button> 
 <a href="${ctx }/gamejs/fuli2/wuliCaifupad.html ">
 --%>
  <button id="gotobill" onclick="gotobill()">登录个人账单</button> 
  <h1 id="newYear" onclick="gotonewYear()">新年活动  </h1>
  <audio id="media" src="${ctx }/gameimg/gameimg/main.mp3" autoplay="autoplay" preload="load" loop="loop"></audio>
</section>

</body>
<script type="text/javascript">



	function gotobill(){
		window.location.href= rootPath+"/game/yearBill";
	}
	 
	function gotonewYear(){
		window.location.href=  rootPath+"/gamejs/fuli2/wuliCaifupad.html";
	}
	 
	

</script>
</html>

