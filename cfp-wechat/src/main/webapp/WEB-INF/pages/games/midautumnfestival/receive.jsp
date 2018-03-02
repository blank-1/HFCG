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
	<div class="w-cash">
		<div class="shareSuccess">
			<p><span><img src="${ctx }/gameimg/s_icon-temp.png" alt=""></span>分享成功</p>
			<p>更多理财项目 关注财富派微信</p>
		</div>
		<div class="towCode">
			<img src="${ctx }/gameimg/qr.png" alt="">
			<p>长按二维码可一键关注我们</p>
		</div>
	</div>
	<script src="${ctx }/gamejs/zepto.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
	<script src="${ctx }/gamejs/zepto.min.js"></script>
	<script src="${ctx }/js/wechat_zq.js" type="text/javascript"></script>
	<script type="text/javascript">
		var rootPath = '<%=ctx%>';
	</script>
</body>
</html>