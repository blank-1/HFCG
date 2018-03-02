<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
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
<meta name="apple-mobile-web-app-status-bar-style"
	content="black-translucent" />
<meta name="format-detection" content="telephone=no" />
<meta name="msapplication-tap-highlight" content="no" />
<%-- <%@include file="../common/common_js.jsp"%> --%>
<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
<title>推荐二维码</title>
<link rel="stylesheet" href="${ctx}/css/reset.css">
<link rel="stylesheet" href="${ctx}/css/recommend.css">
</head>
<body style="background: url(${ctx}/images/p3_share_img3.jpg) no-repeat;background-size: cover;overflow-y:scroll;">
	<div class="w_wrapper">
		<header class="w_header">
			<img src="${ctx}/images/p3_share_img1.png" alt="">
		</header>
		<article class="w_prompt">
			<span>请好友扫描以下二维码</span>当好友使用您的二维码注册并理财后您可获得相应的现金奖励，同时也可以点击下方“邀请好友”按钮将此链接分享给好友，获得现金!
		</article>
		<c:if test="${not empty inviteNum }">		
			<a href="javascript:void(0);" class="w_peopleNumb">已邀请人数<span id="peopleNumb">${inviteNum}</span>人</a>
		</c:if>
		<div class="w_codeBox">
			<img src="${ctx}/person/getFenXiaoErWeiMa?invite_code=${invitecode}" alt="" class="w_codeimg" s>
			<a href="javascript:void(0);" class="w_invite">邀请好友赚钱</a>
		</div>
	</div>
	<div class="w_shareBox" id="w_shareBox" style="display: none;">
		<img src="${ctx}/images/p4_shareMoney_img4.png" alt="">
	</div>
	<script>
		$(".w_invite").on('touchend',function(){
			$("#w_shareBox").show(10);

		})
		$("#w_shareBox").on("touchend",function(){
			$(this).hide(10)
		})
	</script>
</body>
</html>