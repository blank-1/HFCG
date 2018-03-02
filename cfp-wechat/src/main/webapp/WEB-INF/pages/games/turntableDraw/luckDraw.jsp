<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<title>抽奖啦</title>
<link href="${ctx }/gamecss/LuckDraw.css" rel="stylesheet" type="text/css">

</head>
<body style="background:#db1640 url(${ctx}/gameimg/images/p1_bkgd.png) no-repeat;background-size: 100% auto;z-index:9999;">
	<input type="hidden" name="useNum" value="${useNum}">
   	<input type="hidden" name="totalNum" value="${totalNum}">
   	<input type="hidden" name="mobileExist" value="${mobileExist}">
   	<input type="hidden" name="phone" value="${phone}" id="phone">
   	<input type="hidden" name="token" value="<%=session.getAttribute("token") %>">
<!-- 代码 开始 -->
	<div>
		<div class="banner">
			<div class="turnplate" style="">
				<canvas class="item" id="wheelcanvas" width="422px" height="422px" lineHeight="20px"></canvas>
				<img class="pointer" src="${ctx }/gameimg/images/p1_point.png"/>
				<div class="w_over" id="w_over" style="width: 100%;height: 2rem;position: absolute;top: 50%;background: #e43c49;z-index: 999;opacity: .8;text-align: center;line-height: 2rem;border-radius: 5px;font-size: 1rem;color: #fff;margin-top: -1rem;display: none;">
					
				</div>
			</div>
			<div class="w_shadow"></div>
			<div class="w_bottonSha"></div>
		</div>
		<div class="w_chance">
			<p>您有<span id="w_Original">${useNum}</span>次机会，分享可再抽<span id="w_Surplus">${totalNum }</span>次</p>
		</div>
		<div class="w_Example">
			<marquee width=100%> 
				<a>恭喜<span class="w_party">156****6171</span>&nbsp;&nbsp;<span class="w_money">20</span>元财富券</a>
				<a>恭喜<span class="w_party">153****3680</span>&nbsp;&nbsp;<span class="w_money">50</span>元财富券</a>
				<a>恭喜<span class="w_party">139****5628</span>&nbsp;&nbsp;<span class="w_money">100</span>元财富券</a>
				<a>恭喜<span class="w_party">187****7792</span>&nbsp;&nbsp;<span class="w_money">20</span>元财富券</a>
				<a>恭喜<span class="w_party">182****6632</span>&nbsp;&nbsp;<span class="w_money">20</span>元财富券</a>
				<a>恭喜<span class="w_party">130****1314</span>&nbsp;&nbsp;<span class="w_money">50</span>元财富券</a>
				<a>恭喜<span class="w_party">139****5569</span>&nbsp;&nbsp;<span class="w_money">50</span>元财富券</a>
				<a>恭喜<span class="w_party">186****1121</span>&nbsp;&nbsp;<span class="w_money">20</span>元财富券</a>
				<a>恭喜<span class="w_party">172****1926</span>&nbsp;&nbsp;<span class="w_money">10</span>元财富券</a>
				<a>恭喜<span class="w_party">153****7983</span>&nbsp;&nbsp;<span class="w_money">100</span>元财富券</a>
				<a>恭喜<span class="w_party">152****6764</span>&nbsp;&nbsp;<span class="w_money">10</span>元财富券</a>
				<a>恭喜<span class="w_party">133****1536</span>&nbsp;&nbsp;<span class="w_money">20</span>元财富券</a>
				<a>恭喜<span class="w_party">131****3649</span>&nbsp;&nbsp;<span class="w_money">50</span>元财富券</a>
			</marquee>
		</div>
		<div class="w_illustration">
			<div class="w_batten">
				<img src="${ctx }/gameimg/images/p1_word_top.png" alt="">
			</div>
			<div class="w_content">
				<p>
					1.共价值10万元财富券，先到先得</br>
					2.每位用户可抽奖1次</br>
					3.分享到朋友圈可多抽奖1次</br>
					4.未注册用户中奖后完成注册即可到账</br>
					5.活动解释权归财富派所有
				</p>
			</div>
			<div class="w_footer">
				<img src="${ctx }/gameimg/images/p1_word_btm.png" alt="">
			</div>
		</div>
	</div>
<div class="w_ui_bg" id="w_ui_bg">
	<div class="w_ui_mask">
		<div class="w_resultBox">
			<p>恭喜您！抽中</p>
			<div class="w_result">
				<span id="w_fruit" class="w_fruit">20</span>元</br>财富券
			</div>
			<p id="w_prompt">
				财富券已放至账户<span id="w_userNum" style="color: #ff8400;">${phone}</span></br>
				首次分享可以多抽我一次哦
			</p>
		</div>
		<div class="w_sureBtn">
			<p id="w_know">我知道了</p>
			<p id="w_Share">立即分享</p>
		</div>
		<div class="w_enroll" id="w_enroll">
			<a href="${ctx }/game/regist/share">注册领奖</a>
		</div>
		<div class="w_attention">
			<p style="color: #4b4b4b;">
				关注并登录财富派公众号</br>
				即可使用财富券
			</p>
			<div class="w_Frame">

				<div style="margin-right:20%;"><img src="${ctx }/gameimg/images/p1_icon2.jpg" alt=""></div>
				<div class="w_Line" id="w_Line">
					<img src="${ctx }/gameimg/images/p1_icon3.png" alt="">
					<span class="w_liner"></span>
				</div>
			</div>
			<p style="color:#a2a2a2;margin-top:1rem;">长按二维码识别图关注我们</p>
		</div>
		<span class="w_closeBtn" id="w_closeBtn">
			<img src="${ctx }/gameimg/images/p1_close.png" alt="">
		</span>			
	</div>
	<div class="w_opportunity" id="w_opportunity">
		<dl>
			<dt><img src="${ctx }/gameimg/images/p1_icon.png" alt=""></dt>
			<dd>您的次数已用完，机会留给朋友吧！</dd>
		</dl>
		<p id="w_however" class="w_however">那好吧</p>
	</div>
</div>
<div class="w_layout" id="w_layout">
	邀请好友一起抽我吧！</br>
	好事情要和大家一起分享哟！
</div>
<!-- 代码 结束 -->
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>

<script type="text/javascript" src="${ctx }/js/jquery-1.8.3.min.js"></script>
<script src="${ctx }/gamejs/awardRotate.js"></script>
<script src="${ctx }/gamejs/jQueryRotate.js"></script>
<script src="${ctx }/gamejs/wechat_turntable.js"></script>
<script>
	var rootPath = '<%=ctx%>';
	$("#w_closeBtn,#w_submit").on("click",function(){
		$("#w_regPhoneBox").fadeOut(500)
	})
	var pointer=${mobileExist};
	if(pointer){
		$(".w_enroll").hide();
		$(".w_sureBtn").show();
	}else{
		$(".w_enroll").show();
		$(".w_sureBtn").hide();
	}
</script>
</body>
</html>