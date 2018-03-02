<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>

<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes" />    
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
	<meta name="format-detection" content="telephone=no"/>
	<meta name="msapplication-tap-highlight" content="no" />
	<title>财富派</title>
	<link rel="stylesheet" href="${ctx }/gamecss/autumnReset.css">
	<link rel="stylesheet" href="${ctx }/gamecss/autumnStyle.css">
</head>
<body>
<input type="hidden" name="flag" id="flag" value="0"/>
	<div class="w-wrapper">
		<div class="w-kong"></div>
		<div class="w-handerBox">
			<div class="w-hander">
				<img src="${ctx }/gameimg/hander.png" alt="">
			</div>
			<p><span>蚂蚁投资CEO</span></br>北京市</p>
		</div>
		<div class="w-btnbox">
			<a href="${ctx }/game/message">
				<dl>
					<dt><img src="${ctx }/gameimg/hangup.png" alt=""></dt>
					<dd>拒绝</dd>
				</dl>
			</a>
			<a href="${ctx }/game/converse">
				<dl>
					<dt id="handUp"><img src="${ctx }/gameimg/connect.png" alt=""></dt>
					<dd>接听</dd>
				</dl>
			</a>
		</div>
	</div>
	<script src="${ctx }/gamejs/zepto.min.js"></script>
	<audio id="audios" src="${ctx }/gameimg/marimba.mp3" autoplay></audio>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
	<script src="${ctx }/js/wechat_zq.js" type="text/javascript"></script>
	<script type="text/javascript">		
		var rootPath = '<%=ctx%>';
		var audio = document.getElementById('audios');
		audio.play();
		var time=0;
		var timer=setInterval(function(){
			time+=1;
			if(time==2){
				audio.play();
				time=0;
			}
		},1000)
	</script>
</body>
</html>