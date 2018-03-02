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
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=3.0,user-scalable=no">
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
		<div class="w-handerBox">
			<div class="w-hander">
				<img src="${ctx }/gameimg/hander.png" alt="">
			</div>
			<p><span>蚂蚁投资CEO</span></br>北京市</p>
		</div>
		<div class="w-tmer">
			<p></br><span id="minu">00:</span><span id="w-Countdown">00</span></p>
		</div>
		<div class="option">
			<img src="${ctx }/gameimg/option.png" alt="">
		</div>
		<div class="w-hangup">
			<a href="${ctx }/game/activity">
				<dl>
					<dt><img src="${ctx }/gameimg/hangup.png" alt=""></dt>
					<dd>挂断</dd>
				</dl>
			</a>
		</div>
	</div>
	<audio preload="preload" id="audio" src="${ctx }/gameimg/20150921_120316.mp3" autoplay></audio>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
	<script src="${ctx }/gamejs/zepto.min.js"></script>
	<script src="${ctx }/js/wechat_zq.js" type="text/javascript"></script>
	<script src="${ctx }/gamejs/zepto.min.js"></script>
	<script>

		var rootPath = '<%=ctx%>';	

		var timer=null,
			time=0,
			audio=document.getElementById("audio");

		var timer=setInterval(function(){
				time+=1;
				if(time<10){
					$("#w-Countdown").html('0'+time)
				}else{
					$("#w-Countdown").html(time)
				}
				if(time>=60){
					$("#minu").html("01:");
					time=0;
					$("#w-Countdown").html('0'+time)
				}

				if(audio.ended){
					location.href=rootPath+"/game/activity"
				}			
		}, 1000)
	</script>
</body>
</html>
