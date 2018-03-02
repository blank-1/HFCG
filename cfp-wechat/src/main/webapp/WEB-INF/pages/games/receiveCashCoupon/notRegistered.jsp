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
<title>未注册</title>
<link rel="stylesheet" href="${ctx }/gamecss/reset.css">
<link href="${ctx }/gamecss/PublicStyle.css" rel="stylesheet" type="text/css">

</head>
<body>
<!-- 代码 开始 -->
<div class="w_NotRegistered">
	<div class="w_RegisteredImg">
		<img src="${ctx }/gameimg/cashCoupon/noreg.png" alt="">
	</div>
	<div class="w_font">
		<p><span>您还没有注册！</span></br>请关注财富派微信公众号</br>完成注册完成后再来领取</p>
	</div>
	<div class="w_attention">
		<div class="w_Frame">
			<div style="margin-right:20%;"><img src="${ctx }/gameimg/cashCoupon/p1_icon2.jpg" alt=""></div>
			<div class="w_Line" id="w_Line">
				<img src="${ctx }/gameimg/cashCoupon/icon_f.png" alt="">
				<span class="w_liner"></span>
			</div>
		</div>
		<p style="color:#9a102d;margin-top:1rem;">长按二维码识别图关注我们</p>
	</div>
	<div class="w_sureBtn">
		分享
	</div>
	<div class="w_uiMask">
		
	</div>
</div>
<!-- 代码 结束 -->
<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
<script src="${ctx }/gamejs/wechat_cashCoupon.js" type="text/javascript"></script>
<script src="${ctx }/gamejs/PublicJS.js"></script>
<script type="text/javascript">
	var rootPath = '<%=ctx%>';	
</script>
</body>
</html>