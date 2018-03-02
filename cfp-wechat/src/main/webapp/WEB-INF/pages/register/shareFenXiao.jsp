<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
%>
<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<title>活动详情</title>
	<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript">
	 var rootPath = '<%=ctx%>';
	</script>
	<style type="text/css">
	body{
		-webkit-touch-callout: default;
		-webkit-user-select: none;
		-khtml-user-select: none;
		-moz-user-select: none;
		-ms-user-select: none;
		user-select: none;
	}
	</style>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
	<script src="${ctx}/js/wechat_dis.js"></script>
	<link rel="stylesheet" href="${ctx}/css/reset2.css">
	<link rel="stylesheet" href="${ctx}/css/share.css">
</head>
<body>
	<input type="hidden" value="${invite_code}" id="incode">
	<div class="w_wrapper">
		<ul class="w_list">
			<li>
				<img src="${ctx}/images/p0_share_img1.jpg" alt=""></li><li>
				<img src="${ctx}/images/p0_share_img2.jpg" alt="">
			</li><li>
				<img src="${ctx}/images/p0_share_img03.png" alt="">
			</li>
		</ul>
		<div class="w_box">
			<img src="${ctx}/person/getFenXiaoErWeiMa?invite_code=${invite_code}" alt="">
			<p>
				<c:if test="${sameCode eq '2'}">
					长按二维码 一起赚钱
				</c:if>
				<c:if test="${sameCode ne '2'}">
					扫我二维码 一起赚钱
				</c:if>
			</p>
			<c:if test="${not empty islogin}">
				<c:if test="${sameCode eq '1'}">
					<a href="javascript:void(0);" class="promptlyBtn">
						<img src="${ctx}/images/p0_share_icon01.png" alt="">
					</a>
				</c:if>
				<c:if test="${sameCode eq '2'}">
					<div class="w_box3">
					      <a href="javascript:void(0);" class="promptlyBtn">
					        <img src="${ctx}/images/p0_share_icon03.png" alt="">
					      </a>
					      <a href="${ctx}/person/account/overview">
					        <img src="${ctx}/images/p0_share_icon02.png" alt="">
					      </a>
				    </div>
				</c:if>
			</c:if>
			<c:if test="${empty islogin}">
				<div class="w_box3">
				      <a href="javascript:void(0);" class="promptlyBtn">
				        <img src="${ctx}/images/p0_share_icon03.png" alt="">
				      </a>
				      <a href="${ctx}/user/toLogin?invite_code=${invite_code}">
				        <img src="${ctx}/images/p0_share_icon02.png" alt="">
				      </a>
			    </div>
			</c:if>
		</div>
	</div>
	<div class="w_share" id="w_share">
		<img src="${ctx}/images/p4_shareMoney_img4.png" alt="">
	</div>
	<script>
		$(".promptlyBtn").on('click',function(){
			$('body').css({
				"overflow":"hidden"
			})
			$("#w_share").show().on("click",function(){
				$(this).hide();
				$('body').css({
					"overflow":"auto"
				})
			});
		})
	</script>
</body>
</html>