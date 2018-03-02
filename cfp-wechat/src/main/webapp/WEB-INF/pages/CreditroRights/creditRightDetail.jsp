<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xt.cfp.core.constants.Constants"%>
<%@include file="../common/taglibs.jsp"%>
<%
	String ctx = request.getContextPath();
	pageContext.setAttribute("ctx", ctx);
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
	pageContext.setAttribute("basePath", basePath);
	pageContext.setAttribute("picPath", Constants.picPath);
%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style"
	content="black-translucent" />
<meta name="format-detection" content="telephone=no" />
<meta name="msapplication-tap-highlight" content="no" />
<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
<link rel="stylesheet" href="${ctx }/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/register.css" type="text/css">

<title>理财详情</title>
</head>

<body style="background-color: #ebeef6; margin: 0; padding: 0;">
	<div class="topmoneyinfo">
		<ul class="leftlist">
			<img id="moneyicon" src="${ctx }/images/iconTou.png" />
			<span
				style="display: block; line-height: 1.8rem; font-size: 1.4rem; clear: both; color: #fff;"
				id="creditorRightsName"></span>
			<li>年化收益：<span id="annualRate"></span></li>
			<li>借款时长：<span id="cycleCount"></span></li>
			<li>还款方式：<span id="repayMentMethod"></span></li>
			<c:if test="${not empty rateOrder }">
				<i class="l_quan">您已使用${rateOrder.rateValue }%加息券</i>
			</c:if>
			<c:if test="${not empty activityOrder }">
				<i class="l_1year">一周年庆加息${activityOrder.rateValue }%</i>
			</c:if>
		</ul>
		<ul class="rightlist">
			<li id="trunOut">投资金额</li>
			<li id="" style="color: #f4536e; font-size: 2rem;"><span id="buyPrice"></span>元</li>
			<br>
			<li id='trunPerson'   style="display:none">转让人</li>
			<li id='trunPerson1'  style='color: #f4536e; font-size: 2rem;'display=none"><span id="trunP" ></span> </li>
			<br/>
			<li>已获收益：</li>
			<li style="color: #f4536e; font-size: 2rem;"><span id="expectProfit"></span>元</li>
		</ul>
	</div>
	<div id="dataAll" class="dataAll">
		<ul class="dataLeft">
			<li>回款期</li>
			<li>回款日期</li>
			<li>应回本金（元）</li>
			<li>应回利息（元）</li>
			<li>罚息（元）</li>
			<li>应缴费用（元）</li>
			<li>应回款总额（元）</li>
			<li>已回款总额（元）</li>
			<li>状态</li>
		</ul>
		<div class="dataRight" id="dataRight"></div>
	</div>
	<a id="moneyinfo2" class="moneyinfo2" href="#"><img
		id="moneyinfoimg" class="moneyinfoimg"
		src="${ctx }/images/moneyinfo.png" /></a>
	<div id="moneyinfo3" class="moneyinfo3">
		<p>此标已撤销，还有其他高收益产品</p>
		<a class="moneybtn">去看看</a>
	</div>
</body>
<script type="text/javascript">
	var rootPath = '<%=ctx%>';
	var creditorRightsId = '${creditorRightsId}';
	var creditorRightsApplyId = '${creditorRightsApplyId}';
	loanDetail();
	function loanDetail() {
		$.post(rootPath + "/finance/getCreditRightDetail",{"creditorRightsId" : creditorRightsId
			,"creditorRightsApplyId":creditorRightsApplyId},
						function(data) {
							if (data.result == 'success') {
								var detail = data.data.creditorRightsDetail;
								var list = data.data.repaymentList;
								var lendCustomerName = data.data.lendCustomerName;
								if (detail.rightsState == '已生效') {
									$("#moneyicon").attr("src",rootPath + "/images/iconTou.png");
								} else if (detail.rightsState == '还款中') {
									$("#moneyicon").attr("src",rootPath + "/images/iconTou.png");
								} else if (detail.rightsState == '已结清') {
									$("#moneyicon").attr("src",rootPath + "/images/iconQing.png");
								} else if (detail.rightsState == '提前结清') {
									$("#moneyicon").attr("src",rootPath+ "/images/icon_tiqian.png");
								}
								if (detail.fromWhere == '1') {//标
									$("#creditorRightsName").html(detail.creditorRightsName);//标题

									if(detail.awardPoint=='2'){
										if(detail.awardRate!=''&&detail.awardRate!=null){
											$("#annualRate").html(detail.annualRate+"+"+detail.awardRate+"%");//年化收益
										}
									}else{
										
									$("#annualRate").html(detail.annualRate);//年化收益
									}
								 
									$("#cycleCount").html(list.length+"个月");//借款时长;
									$("#repayMentMethod").html(detail.repayMentMethod);//还款方式
									$("#buyPrice").html(detail.buyPrice);//投资金额
								} else {//转让
									if(detail.awardPoint=='2'){
										if(detail.awardRate!=''&&detail.awardRate!=null&&detail.awardRate!='0'){
											$("#annualRate").html(detail.annualRate+"+"+detail.awardRate+"%");//年化收益
										}
									}else{
										
									$("#annualRate").html(detail.annualRate);//年化收益
									}
									$("#trunOut").html(""); 
								  	$("#trunOut").html("转入金额");  
								  	$(".rightlist").css("padding","1rem 0 2rem 6%");
								  	$(".rightlist li").css("line-height","2rem");
									$("#creditorRightsName").html(detail.creditorRightsName);//标题
								//	$("#annualRate").html(detail.annualRate);//年化收益
									$("#cycleCount").html(list.length+"个月");//借款时长
									$("#repayMentMethod").html(detail.repayMentMethod);//还款方式
									$("#buyPrice").html(detail.buyPrice);//投资金额
									//alert("==-="+list[list.length].supportProfit);
								//	alert(list.length+"-=-=-=");
							 	$("#expectProfit").html(detail.expectProfit);//预期收益
									$("#trunP").html(data.data.lendCustomerName);
									$("#trunPerson").show();
									$("#trunPerson1").show();
									}
								var trunCountMoney = 0;
							 	$("#expectProfit").html(detail.expectProfit);//已获收益s
								if (detail.fromWhere == '1') {//标
									for (var i = 0; i < list.length; i++) {
										var html_ul = "<ul>" + "<li>"
												+ list[i].sectionCode + "</li><li>"+ list[i].repaymentDayPlanned+ "</li>" + "<li>"
												+ list[i].shouldCapital2 + "</li>"+ "<li>" + list[i].shouldInterest2+  "</li>" + "<li>"
												+ list[i].defaultInterest + "</li>"+ "<li>" + list[i].shouldFee+ "</li>" + "<li>"
												+ list[i].allBackMoney + "</li>"+ "<li>" + list[i].factMoney+ "</li>" + "<li>"
												+ list[i].rightsDetailState+ "</li>" + "</ul>";
												$("#dataRight").append(html_ul);
									}

								}else {
									for (var i = 0; i < list.length; i++) {
										var html_ul = "<ul>" + "<li>"
												+ list[i].sectionCode + "</li><li>"+ list[i].repaymentDayPlanned+ "</li>" + "<li>"
												+ list[i].shouldCapital2 + "</li>"+ "<li>" + list[i].shouldInterest2+  "</li>" + "<li>"
												+ list[i].defaultInterest + "</li>"+ "<li>" + list[i].shouldFee+ "</li>" + "<li>"
												+ list[i].allBackMoney + "</li>"+ "<li>" + list[i].factMoney+ "</li>" + "<li>"
												+ list[i].rightsDetailState+ "</li>" + "</ul>";
												$("#dataRight").append(html_ul);
									}
								}
							} else {
								alert(data.errMsg);
							}
						}, 'json');
	}
	
 </script>
</html>

