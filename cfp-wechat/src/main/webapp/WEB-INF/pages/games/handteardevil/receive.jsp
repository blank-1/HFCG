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
	<link rel="stylesheet" href="${ctx }/gamecss/reset.css">
	<link rel="stylesheet" href="${ctx }/gamecss/style.css">
</head>
<body style="overflow:visible;">
<input type="hidden" name="flag" id="flag" value="0"/>
	<div class="w-cash">
		<img src="${ctx }/gameimg/voucher.png" alt="">
	</div>
	<p class="w-operation"><span>长按二维码</span><br>
关注财富派<br>
注册并实名认证即可生效!</p>
	<div class="w-companyInfo">
		<dl>
			<dd>
				<h2><img src="${ctx }/gameimg/logo2.png" alt=""></h2>
				<p style="margin:1rem 0 0 1rem;font-size:1.6rem;">财富派</p>
				<p>微信号：caifupad</p>
				<p>北京汇聚融达网络科技有限公司</p>
				<p>长按识别图中二维码关注我们</p>
			</dd>
			<dt><img src="${ctx }/gameimg/img2.png" alt=""></dt>
		</dl>
	</div>
	
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
	<script src="${ctx }/js/wechat_xt.js" type="text/javascript"></script>
	<script src="${ctx }/gamejs/zepto.min.js"></script>
	<script src="${ctx }/gamejs/validate.js"></script>
	<script type="text/javascript">		
		var rootPath = '<%=ctx%>';	
	</script>
</body>
</html>