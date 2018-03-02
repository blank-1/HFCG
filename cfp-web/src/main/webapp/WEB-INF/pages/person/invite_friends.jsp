<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	
	<title>账户中心-个人信息-邀请好友</title>
	<%@include file="../common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx}/js/infor_4.js"></script><!-- public javascript -->
	<script type="text/javascript" src="${ctx}/js/jquery_page.js"></script><!-- public javascript -->
	<script type="text/javascript" src="${ctx}/js/ZeroClipboard.js"></script>
</head>

<body class="body-back-gray">
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
 <input type="hidden" id="titleTab" value="3-4" />
<!-- navindex end -->
<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">个人信息</a>></li>
        <li><span>邀请好友</span></li>
    </ul>
</div>
<!-- person-link end -->

<!-- container start -->
<div class="container clearFloat">
    <!-- pLeft start -->
    <div class="pLeft clearFloat">
    </div>
    <!-- pLeft end -->

    <!-- pRight start -->
    <div class="pRight clearFloat">
        <div class="p-Right-top">
			<p class="yqhy-title">邀请好友</p>
		</div>
        <div class="p-Right-bot yqhy-mt" >
			<div class="pre_zqlist">
				<div class="in_friends_top">
					<div class="in_friends_tople">
						<div class="in_friends_tople_box1">
							<div class="fri_step1"><span class="yoursnumber" >您的邀请码：</span><i>${invitecode}</i></div>
							<div class="clear_10"></div>
							<div class="fri_step1">
								<span>邀请链接：</span>
								<div class="clear_10"></div>
								<input id="fuzhiwenben" type="text" placeholder="http://www.baidu.com" />
								<div class="clear_15"></div>
								<a href="javascript:;" data-mask='mask' data-id="yaoq_ma" id="myText">		
									<button id="btn_submit" >复制链接</button>
								</a>
							</div>
						</div>
					</div>
					<div class="in_friends_topri">
						<p >您的专属推荐二维码</p>
						<p class="friend_ewm"><img src="${ctx}/person/getErWeiMa?keycode=${inviteURL }" id="myQrCode"></img></p>
					</div>
				</div>
				<div class="clear_20"></div>
				<div class="in_friends_bot" id="yqlist">
					
				</div>
				<div class="clear_0"></div>
				
			</div>
		</div>
		<div class="tcdPageCode mt-20"></div>
    </div>
    <!-- pRight start -->
</div>
<!-- container end -->

<div style="clear:both;height:50px;"></div>

<div class="zhezhao"></div>
<div class="zhezhao1"></div>
<!-- masklayer start  -->
<%@include file="../login/login.jsp"%>
<!-- masklayer end -->

<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->
<script type="text/javascript">
$(function(){
	$('#fuzhiwenben').val('${inviteURL }');
	var copyURL = $('#fuzhiwenben').val();
	var clip = new ZeroClipboard.Client();
	clip.setHandCursor(true);
	clip.setText(copyURL);
	clip.glue("btn_submit");
	clip.addEventListener( "complete", function(){
	     alert("邀请码复制成功！");
	 });

    var url = "${ctx}/person/user_qrcode";
    $.post(url,function(data){
        $("#myQrCode").attr("src",data);
    })
});
</script>
</body>
</html>

