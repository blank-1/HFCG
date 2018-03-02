<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />    
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<title>领取成功</title>
<link rel="stylesheet" href="${ctx }/gamecss/reset.css">
<link href="${ctx }/gamecss/PublicStyle.css" rel="stylesheet" type="text/css">
</head>
<body style="background:#ce133b">
<!-- 代码 开始 -->
	<p class="w_success">领取成功！</p>
	<div class="w_Withdrawals">
		<p><span>提现券</span></br>[可抵扣提现手续费]</p>
	</div>
	<div class="w_Celebrate">
		<img src="${ctx }/gameimg/cashCoupon/bkgd_getcard.png" alt="">
	</div>
<!-- 代码 结束 -->
<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
<script src="${ctx }/gamejs/wechat_cashCoupon.js" type="text/javascript"></script>
<script type="text/javascript">
	var rootPath = '<%=ctx%>';	
</script>
</body>
</html>