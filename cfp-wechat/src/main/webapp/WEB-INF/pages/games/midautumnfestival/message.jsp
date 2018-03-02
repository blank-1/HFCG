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
	<div class="w-all">
		<div class="w-chatBox">
			<ul class="w-list">
				<li>
					<dl>
						<dt><img src="${ctx }/gameimg/hander.png" alt=""></dt>
						<dd>
							<p>有点儿伤心！</p>
						</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt><img src="${ctx }/gameimg/hander.png" alt=""></dt>
						<dd style="width:75%;">
							<p>一个小小的动作，伤害却那么大。</p>
						</dd>
					</dl>
				</li>
				<li>
					<dl>
						<dt><img src="${ctx }/gameimg/hander.png" alt=""></dt>
						<dd>
							<p>其实我只想对你说：<a href="${ctx }/game/activity" style="color:blue;">点击查看</a></p>
						</dd>
					</dl>
				</li>
			</ul>
		</div>
		<div class="w-input">
			<img src="${ctx }/gameimg/text.png" alt="">
		</div>
	</div>
	<script src="${ctx }/gamejs/zepto.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
	<script src="${ctx }/gamejs/zepto.min.js"></script>
	<script src="${ctx }/js/wechat_zq.js" type="text/javascript"></script>
	<script>
		var rootPath = '<%=ctx%>';
		var timer=0;
		var time=setInterval(function(){
				timer++;
				if(timer==1){
					$(".w-list li").eq(0).show(200)
				}
				if(timer==2){
					$(".w-list li").eq(1).show(200)
				}
				if(timer==3){
					$(".w-list li").eq(2).show(200)
				}
		},1500)
	</script>
</body>
</html>