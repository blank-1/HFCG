<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<title>财富派</title>	
	<link rel="stylesheet" href="${ctx }/gamecss/autumnReset.css">
	<link rel="stylesheet" href="${ctx }/gamecss/autumnStyle.css">
</head>
<body>
<input type="hidden" name="flag" id="flag" value="1"/>
	<div class="container">
		<div class="wrapper">
			<section class="page1 pages">
				<div class="animate">
					<img src="${ctx }/gameimg/zqlogo.png" alt="">
				</div>
			</section>
			<section class="page2 pages">
				<div class="present">
					<p><span>蚂蚁投资恭祝大家中秋快乐</span></br>
<i style="visibility: hidden;">你啊啊</i>蚂蚁投资是汇聚融达推出的互联网金融连锁品牌，致力为加盟商提供整套服务体验，推动利率市场化、解决中小企业和个人融资难的问题，提供安全的管理系统和完善的风控监管体系。
</p>
					<a id="shareBtn" href="#"><img src="${ctx }/gameimg/shadow.png" alt=""></a>
				</div>
			</section>
		</div>
	</div>
	<div class="arr" id="arr"></div>
	<div class="activityMask" id="activityMask">
		<div class="shareImg">
			<img src="${ctx }/gameimg/share_pic-temp.png" alt="">
		</div>
	</div>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
	<script src="${ctx }/js/wechat_zq.js" type="text/javascript"></script>
	<script src="${ctx }/gamejs/zepto.min.js"></script>
	<script src="${ctx }/gamejs/swipepage.js"></script>
	<script src="${ctx }/gamejs/script1.js"></script>
	<script type="text/javascript">		
		var rootPath = '<%=ctx%>';	
		$("#shareBtn").on("click",function(){
		    $("#activityMask").show().on("click",function(){
		        $(this).hide()
		    });
		})
	</script>
</body>
</html>