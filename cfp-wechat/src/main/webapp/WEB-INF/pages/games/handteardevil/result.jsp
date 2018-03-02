<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes" />    
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
	<meta name="format-detection" content="telephone=no"/>
	<meta name="msapplication-tap-highlight" content="no" />
	<link rel="stylesheet" href="${ctx }/gamecss/style.css" type="text/css">
	<script src="${ctx }/js/wechat_xt.js" type="text/javascript"></script>
	<title>手撕鬼子</title>
</head>
<body style="overflow:auto;">

<div class="result">
    	<img class="l-model" src="${ctx }/gameimg/FrameTop.png" />
        <div class="l-model2">
        <p>您手撕了<span id="resultNum">0</span>个鬼子,<span style="color:#000;" id="w_fen"></span></p>
		<input type="hidden" name="flag" id="flag" value="1"/>
		<div class="designation" id="designation">
			<p class="octuple" id="octuple"></p>
		</div>
        <p class="l-resultinfo">分享成功可领10元现金奖励！</p>
        <div class="btnBox">
			<a id="share">分享领券</a>
			<a href="${ctx }/game/illustrate">再撕一次</a>
		</div>
        <p class="l-resultinfo2">
        	1.现金奖将在9月29日之前发放至财富派账户中。<br>
			2.获奖的新用户请于活动期间内完成注册，以便奖励发放（限前1000名参与游戏的用户）。<br>
            3.本活动最终解释权归财富派所有。
         </p>
        </div>
        <img class="l-mode3" src="${ctx }/gameimg/FrameBottom.png" />
        </div>
	<!--<div class="result">
		<div class="model">
			<p>您手撕了<span id="resultNum">0</span>个鬼子,鬼子落荒而逃</br>获得称号：</p>
		</div>
		<div class="designation">
			<p class="octuple" id="octuple"></p>
		</div>
		<div class="fontAera">
			<p>
				1、每位分享成功用户可获得10元现金奖励。</br>
				2、现金奖励在X月X日发放至财富派账户中。</br>
				3、获奖的新用户请于活动期间内完成注册，以便奖励发放。</br>
				4、本活动最终解释权归财富派所有。
			</p>
		</div>
		<div class="btnBox">
			<a id="share">分享领券</a>
			<a href="${ctx }/game/illustrate">再撕一次</a>
		</div>
	</div>-->
	<dis class="maskBox">
		<p>
			英雄！你辣么厉害！</br>
			你朋友知道吗？
		</p>
	</dis>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
	<script src="${ctx }/js/wechat_xt.js" type="text/javascript"></script>
	<script src="${ctx}/gamejs/zepto.min.js"></script>
	<script src="${ctx}/gamejs/result.js"></script>
	
	<script type="text/javascript">		
		var rootPath = '<%=ctx%>';
		
		
	</script>
</body>
</html>