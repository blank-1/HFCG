<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
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
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx }/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/s_listShengxin.css" type="text/css">
<style type="text/css">
 	.noNum{display: block;}
 	.page{display:inline-block;}
	/* .noNum img{width:13rem; margin-top:3rem;}
	.noNum p{color:#999; font-size:1.2rem; margin-top:2rem;} */
</style>
<script data-main="${ctx}/js/s_listShengxin.js" src="${ctx}/js/lib/require.js"></script>
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
var rootPath = '<%=ctx%>';
</script>
<title>省心计划</title>

</head>
<body class="l_NewScroll">
	<div class="mainBox">
		<div class="shengXbox">
			<p class="shengXp1">理财中金额（元）</p>
			<p class="shengXp2"><fmt:formatNumber value="${financeAccountValue }" pattern="#,##0.00"/></p>
			<p class="shengXp3">（在投资金+预期收益）</p>
		</div>
		<!-- 账户ID -->
          <input type="hidden" id="accId" value="${accId }"/>
        <!-- 理财计划，出借订单ID -->
          <input type="hidden" id="lendOrderId" value="${lendOrderId }"/>
		<div class="shengXmain">
			<div class="sXmainbox">
				<div class="lineHeight">
					<span class="sXmainboxLe color_333">在投预期收益（元）</span>
					<span class="sXmainboxCen color_333">已获收益（元）</span>
					<span class="sXmainboxRi color_333">预期年化收益范围</span>
				</div>
				<div class="lineHeight">
					<span class="sXmainboxLe color_red"><fmt:formatNumber value="${expectProfit }" pattern="#,##0.00"/></span>
					<span class="sXmainboxCen color_red"><fmt:formatNumber value="${currentProfit }" pattern="#,##0.00"/></span>
					<span class="sXmainboxRi color_666">${profitRate }-${profitRateMax }%</span>
				</div>
			</div>
			<div class="sXmainbox">
				<div class="lineHeight">
					<span class="sXmainboxLe color_333">购买金额（元）</span>
					<span class="sXmainboxCen color_333">待出借金额（元）</span>
					<span class="sXmainboxRi color_333">省心期（月）</span>
				</div>
				<div class="lineHeight">
					<span class="sXmainboxLe color_666"><fmt:formatNumber value="${buyBalance }" pattern="#,##0.00"/></span>
					<span class="sXmainboxCen color_666"><fmt:formatNumber value="${availValue }" pattern="#,##0.00"/></span>
					<span class="sXmainboxRi color_666">${timeLimit }</span>
				</div>
			</div>
			<div class="sXmainbox">
				<p>
					<span class="sXmainboxLeft">还款方式</span>
					<span class="sXmainboxRight">省心期结束且债权到期后还本付息</span>
				</p>
			</div>
			<div class="sXmainbox">
				<p>
					<span class="sXmainboxLeft">收益分配方式</span> <span
						class="sXmainboxRight"> <c:if
							test="${loep.profitReturnConfig eq '0' }">
							收益复利投资
						</c:if> <c:if test="${loep.profitReturnConfig eq '1' }">
							收益提取至可用余额
						</c:if>
					</span>
				</p>
			</div>
			<div class="sXmainbox">
				<p>
					<span class="sXmainboxLeft">购买日期</span>
					<span class="sXmainboxRight"><fmt:formatDate value="${buyTime}" pattern="yyyy-MM-dd" type="date"/></span>
				</p>
			</div>
			<div class="sXmainbox">
				<p>
					<span class="sXmainboxLeft">省心计划到期日期</span>
					<span class="sXmainboxRight"><fmt:formatDate value="${agreementEndDate}" pattern="yyyy-MM-dd" type="date"/></span>
				</p>
			</div>
		</div>
		<div class="clear_0"></div>
		<div class="s_content">
			<ul class="headNav">
				<li class="listCur"><a  class="chutab" href="javascript:;">出借明细</a></li>
				<li   id="chaxunUl"><a class="liutab" href="javascript:;">资金流水</a></li>
			</ul>
			
			<div class="mainList l_borrowList">
				<div class="page"  >
					<ul class="chujieUl">
						<li class="liCur">全部</li>
						<li>理财中</li>
						<li>已结清</li>
						<div class="clear_0"></div>
					</ul>
					
					<!-- <ul class="chaxunUl">
						<li class="liCur">全部</li>
						<li>近7天</li>
						<li>近1月</li>
						<li>近半年</li>
					</ul> -->
					<div id="lendOrder" class="clear_0"></div>
					
				 
					 
					 
					 
					 
			 	
				<!-- 	<p class="l_lastTip">已是全部数据</p> -->
				</div>
				<div class="page" style="display:none;">
					<ul class="liushuiUl">
						<li class="liCur">全部</li>
						<li>收入</li>
						<li>支出</li>
						<li>冻结</li>
						<li>解冻</li>
						<div class="clear_0"></div>
					</ul>
					
					 
					<ul id="zjlist" class="regular pay_page0">
		            </ul>
				<!-- 	<p class="l_lastTip">向下滑动加载更多</p> -->
				</div>
			</div>
		</div>
	</div>
</body>
</html>
