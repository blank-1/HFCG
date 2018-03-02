<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%@include file="../common/common_js.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="0">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<link rel="stylesheet" href="${ctx }/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/s_messageCenter.css?${version}" type="text/css">
<script data-main="${ctx }/js/s_messageCenter.js?${version}" src="${ctx }/js/lib/require.js"></script>
<script type="text/javascript">
//rem自适应字体大小方法
var docEl = document.documentElement,
resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
recalc = function() {
    //设置根字体大小
    docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
};
//绑定浏览器缩放与加载时间
window.addEventListener(resizeEvt, recalc, false);
document.addEventListener('DOMContentLoaded', recalc, false);

//跳转到详情页
function toDetail(id) {
	window.location.href= rootPath+"/notice/detail?noticeId="+id;
}
</script>
<title>消息中心</title>
</head>
<body class="l_NewScroll">
	<div class="s_content">
		<ul class="headNav">
			<li class="${sign=='notice'?'':'cur'}"><a href="javascript:;">平台数据</a></li>
			<li class="${sign=='notice'?'cur':''}"><a href="javascript:;">网站公告</a></li>
		</ul>
		<div class="clear_10"></div>
		<div class="couponList">
			
			<!-- 平台数据[开始] -->
			<div class="page" style="${sign=='notice'?'display:none;':''}">
				<div class="line_01"><b></b><span><img src="${ctx }/images/icon/icon_yqsy.png" />平台运营数据</span><b></b></div>
				<p class="endTime">（实时更新）</p>
				<div class="messageBox boxbj01">
					<p><img src="${ctx }/images/icon/messageIcon_01.png" /></p>
					<p><fmt:formatNumber value="${allBuyBalance}" pattern="#,##0.00"/> <span>元</span></p>
					<p>平台累计出借</p>
				</div>
				<div class="messageBox boxbj02">
					<p><img src="${ctx }/images/icon/messageIcon_02.png" /></p>
					<p><fmt:formatNumber value="${allProfit}" pattern="#,##0.00"/> <span>元</span></p>
					<p>为用户赚取</p>
				</div>
				<div class="messageBox boxbj03">
					<p><img src="${ctx }/images/icon/messageIcon_03.png" /></p>
					<p><span>${days }</span>天</p>
					<p>平台运营时间</p>
				</div>
			</div>
			<!-- 平台数据[结束] -->
			
			<!-- 网站公告[开始] -->
			<div class="page" style="${sign=='notice'?'':'display:none;'}" id="noticeList_div">
				<div style="height: auto;" id="noticeList">
				
				<%-- <dl class="newsList" onclick="window.location.href='${ctx }/notice/detail?noticeId=35'">
					<dd>
						<div class="imgBox">
							<img src="${ctx }/images/icon/news_01.png" alt="" >
						</div>
					</dd>
					<dt>
						<p class="title">
							<span>平台公告</span>
						</p>
						<div class="text-overflow">	
							为了赶上合规的趋势，平台特意降息等发布相关事宜，公司特发布通知，请用户注意查看为了赶上合规的趋势，平台特意降息等发布相关事宜，公司特发布通知，请用户注意查看
						</div>
						<div class="Notice">
							<i>公告</i>
							<i class="timer">4月10日</i>
						</div>
					</dt>
				</dl>  --%>
				
				<!-- <p class="l_lastTip" style="display:none;">向下滑动加载更多</p> -->
				
					<%-- <div class="messageImg">
						<img src="${ctx }/images/messageIcon.png" >
						<p class="imgTip">运营偷懒了，暂无公告</p>
					</div> --%>
				
				</div>
			</div>
			<!-- 网站公告[结束] -->
			
		</div>
	</div>
	
</body>
</html>
