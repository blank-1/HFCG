<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<title>财富派</title>
	<link rel="stylesheet" href="${ctx }/gamecss/reset.css">
    <link rel="stylesheet" href="${ctx }/gamecss/Anniversary.css">
</head>
<body style="background: url(${ctx}/gameimg/anniversary/bg.jpg) repeat-y;">
	<div id="wrapper" class="wrapper">
        <div class="scroller">
			<!-- 页面开始 -->
			<div class="pageBox">
				<div class="page1">
					<p class="font">
						<img src="${ctx}/gameimg/anniversary/text_1.png" alt="">
					</p>
					<div class="animate1 ">
						<span class="w_clickAnimate First"></span>
					</div>
				</div>
				<div class="page2">
					<p class="font">
						<img src="${ctx}/gameimg/anniversary/text_2.png" alt="">
					</p>
					<div class="animate2">					
						<span class="w_clickAnimate Second" style="left: 50%;"></span>
						<div class="w_curtains">
							<div class="w_Sunshine">
								
							</div>
						</div>
						<div class="w_timerOut">
							
						</div>
						<ul class="w_sleep">
							<li></li>
							<li></li>
						</ul>
						<ul class="w_Trend1">
							<li></li>
							<li></li>
							<li></li>
						</ul>
					</div>
				</div>
				<div class="page3" style="margin-top: 10rem;">
					<p class="font" style="width:40%;float: right;margin-top: -10rem;">
						<img src="${ctx}/gameimg/anniversary/text_3.png" alt="">
					</p>
					<div class="animate3">
						<span class="w_clickAnimate Third" style="left: 30%;top: 28%;"></span>
						<div class="w_money">
							<img src="${ctx}/gameimg/anniversary/3p_b.png" alt="">
						</div>
						<div class="w_phone">							
						</div>
					</div>
				</div>
				<div class="page4">
					<div class="animate4">
						<span class="w_clickAnimate Fourth" style="left: 50%;top: 30%;"></span>
					</div>
				</div>
				<div class="page5">
					<div class="animate5">
						<div class="w_box">
							<img src="${ctx}/gameimg/anniversary/6p_b_top.png" alt="">
							<div class="w_imBox">
								<img src="${ctx}/gameimg/anniversary/6p_b_bottom.png" alt="">
								<p class="fontqj">
									<img src="${ctx}/gameimg/anniversary/text_5.png" alt="">
								</p>
							</div>
						</div>
					</div>
				</div>
				<div class="page6">
					<ul class="w_list">
						<li></li>
						<li></li>
						<li></li>
						<li></li>
						<li></li>
						<li></li>
						<li></li>
					</ul>
				</div>
				<div class="page7">
					<p class="font" style="margin: 0 auto;">
						<img src="${ctx}/gameimg/anniversary/text_6.png" alt="">
					</p>
					<div class="logo">
						<img src="${ctx}/gameimg/anniversary/7p_logo.png" alt="">
					</div>

				</div>
				<form action="${ctx }/game/milepostValidate" method="post" id="frm">
					<div class="page8">
						<input type="hidden" name="phone" id="phone" value="${phone }">
						<input type="hidden" name="userToken" id="userToken" value="${userToken }">
						<c:if test="${appFlag eq '0'}">
							<input type="tel" class="Receive" id="Receive" placeholder="请输入手机号">
						</c:if>
						<a href="#" class="ReceiveBtn" id="ReceiveBtn">领取感恩红包</a>
						<div class="w_pattern" id="w_pattern">
							手机格式错误
						</div>
					</div>
				</form>
			</div>
        </div>
	</div>
	<!-- 初始化页面开始 -->
	<section class="loading" id="loading">
		<div class="loadBox">
			<div class="mvTxt">Loading
				<span class="mvSq">.</span>
				<span class="mvSq">.</span>
				<span class="mvSq">.</span>
			</div>
			<div class="mvBox">
			 	<div class="mvBtn"></div>
			</div>
		</div>
	</section>
	<div class="arr" id="arr"></div>
	<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
    <script src="${ctx }/gamejs/iscroll.js"></script>
    <script src="${ctx }/gamejs/Anniversary.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
	<script src="${ctx }/gamejs/wechat_anniversary.js"></script>
	<script>
    	var rootPath = '<%=ctx%>';
		$(".First").on("click",function(){
			$(".animate1").addClass("w_come");
			$(this).hide();
		})
		
		$(".Second").on("click",function(){
			$(".w_Trend1").addClass("w_Trend")
			$(this).hide();
		})
		$(".Third").on("click",function(){
			$(".w_money").addClass("w_moneyFall");
			$(this).hide();
		}) 
		$(".Fourth").on("click",function(){
			$(".animate4").addClass("animated4");
			$(this).hide();

		})
	</script>
</body>
</html>