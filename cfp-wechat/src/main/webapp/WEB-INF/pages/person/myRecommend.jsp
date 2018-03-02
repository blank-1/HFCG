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
<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
<script src="${ctx}/js/wechat_dis.js"></script>
<title>我的推荐</title>
<script type="text/javascript">
	var rootPath = '<%=ctx%>';
</script>
<link rel="stylesheet" href="${ctx}/css/MyInvite.css">
<link rel="stylesheet" href="${ctx}/css/resetIndex.css">
</head>
<body>
	<input type="hidden" value="${incode}" id="incode">
	<header>
		<ul>
			<li onclick="location.href='${ctx}/person/commision?userLevel=0';">
				<p>推广收益（元）</p>
				<p id="InviteReward"><fmt:formatNumber value="${salesDistributionProfit}"/></p>
			</li>
			<li onclick="location.href='${ctx}/person/inviteFriends';">
				<p>成功邀请人数</p>
				<p><span>${inviteNum}</span>人</p>
			</li>
			<li>
				<p>待收佣金（元）</p>
				<p><fmt:formatNumber value="${salesDistributionsholdProfit}"/></p>
			</li>
		</ul>
	</header>
	<p class="l_itips">快速推荐好友，获得更多收益</p>
	<p id="MyInviteCode">我的邀请码：<span>${incode}</span> <button id="shareBTN">分享给好友</button></p>
	<p class="l_share"><img id="l_ercode" src="${ctx}/person/getFenXiaoErWeiMa?invite_code=${incode}" alt="我的二维码"><br>（点击查看大图）<br>扫描二维码，推荐好友 </p>
	<p class="l_itips2">如何赚取佣金？<a href="${ctx}/person/distribution?invite_code=${incode}">活动规则></a></p>
	<img class="l_bottomImg" src="${ctx}/images/MyInvite_img01.jpg" alt="如何赚取佣金？">
	<p class="l_TipsMask"><img src="${ctx}/images/p4_shareMoney_img4.png" alt=""></p>
	
<script type="text/javascript" src="${ctx}/js/mainIndex.js"></script>

<script type="text/javascript">
$("#l_ercode").on("touchend",function(e){
		e.preventDefault();
		e.stopPropagation();
		$(".l_share").hasClass("big")?$(".l_share").removeClass("big").addClass("trans"):$(".l_share").removeClass("trans").addClass("big");
	})
	$("#shareBTN").on("touchend",function(e){
		e.preventDefault();
		e.stopPropagation();
		$(".l_TipsMask").show();
		$("body").addClass("fixed");
	})
	$(".l_TipsMask").on("touchend",function(e){
		e.preventDefault();
		e.stopPropagation();
		$(this).hide();
		$("body").removeClass("fixed");
	})
</script>
</body>
</html>