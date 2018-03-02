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
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<title>成功</title>
	<link rel="stylesheet" href="${ctx }/gamecss/reset.css">
	<link rel="stylesheet" href="${ctx }/gamecss/AllianceWares.css">
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
<script src="${ctx }/gamejs/zepto.min.js"></script>
<script src="${ctx }/js/wechat_franchisee.js"></script>
</head>
<body>
	<div class="l_container">
		<div class="l_wrapper">
			<div class="l_successtip">
				<p>提交成功！</p>
				<p>感谢您对蚂蚁投资的支持！</p>
			</div>
			<div class="l_attention">
				<p>更多详情</br>关注蚂蚁投资微信公众号</p>
				<div class="l_Frame">
					<div style="margin-right:6%;"><img src="${ctx }/gameimg/p6_erweima.png" alt=""></div>
					<div class="l_Line" id="l_Line"><img src="${ctx }/gameimg/p6_icon2.jpg" alt="">
					<span class="w_Lineimg"><img src="${ctx }/gameimg/p6_scanline.png" alt=""></span></div>
				</div>
				<p style="color:#9693dd;margin-top:1rem;">长按二维码识别图关注我们</p>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var rootPath = '<%=ctx%>';	
	</script>
</body>
</html>