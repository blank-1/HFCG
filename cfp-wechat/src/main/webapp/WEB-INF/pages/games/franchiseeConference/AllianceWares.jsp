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
	<title>加盟商邀请函</title>	
	<link rel="stylesheet" href="${ctx }/gamecss/reset.css">
	<link rel="stylesheet" href="${ctx }/gamecss/AllianceWares.css">
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
<script src="${ctx }/gamejs/zepto.min.js"></script>
<script src="${ctx }/js/wechat_franchisee.js"></script>
</head>
<body>
	<div class="container">
		<div class="wrapper">
			<section class="page1 pages">
				<div class="animate">
					<div class="font">
					<img src="${ctx }/gameimg/p1_word.png" alt="">
				</div>
				<div class="erweima">
					<img src="${ctx }/gameimg/p1_ScanBack.png" alt="">
					<img class="w_pic_2" style="width:80%;height:80%;" src="${ctx }/gameimg/p1_ScanFinger.png" alt="">
					<span class="w_liner">
						<img src="${ctx }/gameimg/p1_ScanLine.png" alt="">
					</span>
				</div>
				<div class="w_Scanning">
					[&nbsp;&nbsp;<span id="w_txt">点击接收邀请函</span>&nbsp;&nbsp;]
				</div>
				</div>
			</section>
			<section class="page2 pages">
				<div class="animate">
					<div class="w_Topic">
						<img src="${ctx }/gameimg/p2_TitleWord.png" alt="">
					</div>
					<div class="w_content">
						<img src="${ctx }/gameimg/p2_BodyWord.png" alt="">
					</div>
					<div class="w_WeChatBtn">
						<img src="${ctx }/gameimg/p2_WeiXinBTN.png" alt="">
					</div>
				</div>
			</section>
			<section class="page3 pages">
				<div class="animate">
					<div class="w_Topic2">
						<img src="${ctx }/gameimg/p2_TitleWord.png" alt="">
					</div>
					<div class="w_logo">
						<img src="${ctx }/gameimg/p3_LOGO.png" alt="">
					</div>
					<div class="w_Brand">
						<img src="${ctx }/gameimg/p3_word1.png" alt="">
					</div>
					<div class="w_address">
						<img src="${ctx }/gameimg/p3_word2.png" alt="">
					</div>
					<div class="w_Copy">
						<img src="${ctx }/gameimg/p3_word3.png" alt="">
					</div>
				</div>
			</section>
			<section class="page4 pages">
				<div class="animate">
					<div class="w_Topic2">
						<img src="${ctx }/gameimg/p2_TitleWord.png" alt="">
					</div>
					<div class="w_partnership">
						<img src="${ctx }/gameimg/p4_BodyWord.png" alt="">
					</div>
					<div class="w_online">
						<img src="${ctx }/gameimg/p4_TitleWord1.png" alt="">
					</div>
				</div>
			</section>
			<form action="${ctx }/game/franchiseeSuccess" method="post" id="frm">
			<input type="hidden" name="activityNum" value="franchiseeV06">
			<input type="hidden" name="token" value="${token }">
			<section class="page5 pages">
				<div class="w_Frame">
					<div class="w_name">
						<span>姓&nbsp;&nbsp;&nbsp;&nbsp;名</span>
						<input type="text" placeholder="请输入您的名字" class="w_nameVal" id="w_nameVal" name="realName" value="">
					</div>
					<div class="w_phoneNum">
						<span>手机号</span>
						<input type="tel"  placeholder="请输入正确的手机号" class="w_phoneNumVal" id="phoneNumVal" value="" name="phone">
					</div>
					<div class="w_subBnt" id="w_subBnt">
						<p>提交</p>
					</div>
				</div>
				<div class="w_jing">
					<img src="${ctx }/gameimg/p2_TitleWord.png" alt="">
				</div>
				<div class="w_Prompt">
					<img src="${ctx }/gameimg/p5_word.png" alt="">
				</div>
				<div class="w_allLogo">
					<img src="${ctx }/gameimg/p5_logo.png" alt="">
				</div>
			</section>
			</form>
		</div>
	</div>
	
	<div class="arr" id="arr"></div>
	<div class="w_ui_maskBox" id="w_ui_maskBox">
		<div class="w_ui_mask">
			<span id="w_errorMsg">请输入正确的手机号</span>
			<p id="w_sureBtn">确定</p>
		</div>
	</div>

<script src="${ctx }/gamejs/swipepage.js"></script>
<script src="${ctx }/gamejs/activity.js"></script>
<script src="${ctx }/gamejs/AllianceWares.js"></script>
<script type="text/javascript">
		var rootPath = '<%=ctx%>';	
	</script>
</body>
</html>