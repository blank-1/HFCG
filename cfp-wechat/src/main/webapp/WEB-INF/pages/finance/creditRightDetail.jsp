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
<link rel="stylesheet" href="${ctx }/css/reset.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/ProductsInfo.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/sweetAlert.css" type="text/css">
<link rel="stylesheet" href="${ctx }/css/swiper.min.css">
<script src="${ctx }/js/jquery-1.8.3.min.js"></script>
<script data-main="${ctx }/js/ProductsInfo.js" src="${ctx }/js/lib/require.js"></script>
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
<title>详情页</title>
</head>
<body class="l_NewScroll l_addM"><!--加息券的页面需要给body加class="l_addM"******定向标页面加class="dingxiang" -->
  <h1 class="l_title text-overflow" id="creditorRightsName"><!-- 名字 --></h1>
  <header>
    <ul>
      <li class="l_headerInfo1 pjred">
        <p><i class=" l_tipIcon" id="annualRate"> </i></p>
        <font id="repayMentMethod"><!--  还款方式：周期还息，到期还本--></font>
        	<!-- 使用加息卷开始 -->
        	
        	<c:if test="${not empty rateOrder && not empty activityOrder }">
			  <font class="l_zName">已使用加息券${rateOrder.rateValue }%,周年庆平台奖励加息${activityOrder.rateValue }%</font>
			</c:if>
			<c:if test="${not empty activityOrder && empty rateOrder}">
			  <font class="l_zName">周年庆平台奖励加息${activityOrder.rateValue }%</font>
			</c:if>
			<c:if test="${ empty activityOrder && not empty rateOrder}">
			  <font class="l_zName">已使用加息券${rateOrder.rateValue }%</font>
			</c:if>
       	<!-- 使用加息卷结束 -->
        <p>预期年化收益</p>

      </li>
      <li class="l_headerInfo2">
        <p><i class="pjred" id="cycleCount"><!-- 借款时长 --></i></p>
        <p>借款时长</p>
      </li>
      <li class="l_headerInfo3">
        <p><i class="pjred" id="buyPrice"><!-- 投资金额 --></i>元</p>
        <p>出借金额</p>
      </li>
      <li class="l_headerInfo4">
        <p><i class="pjred" id="expectProfit"> </i>元</p>
        <p>已获收益</p>
      </li>
    </ul>
    </header>
    <section class="l_midInfo">
      <ul id="l_midInfo">
        <li>
          <p>回款日期</p>
          <p>应回本息</p>
          <p>状态</p>
        </li>

      </ul>
    </section>
<div class="loading" id="loading">
    <img src="${ctx}/images/loading.gif" alt="">
</div>
</body>
<script type="text/javascript">

    $("#loading").show();
    setTimeout(function(){
        $("#loading").hide();
    },1500);

	var creditorRightsId = '${creditorRightsId}';
	loanDetail();
	function loanDetail() {
		$.post(rootPath + "/finance/getCreditRightDetail?creditorRightsId="+creditorRightsId,
						function(data) {
							if (data.result == 'success') {
								var detail = data.data.creditorRightsDetail;
								var list = data.data.repaymentList;
								var lendCustomerName = data.data.lendCustomerName;
								var cssType="";
								if (detail.rightsState == '已生效') {
									cssType="l_hasSheng";
								} else if (detail.rightsState == '还款中') {
									cssType="l_backing";
								} else if (detail.rightsState == '已结清') {
									cssType="l_hasDone";
								} else if (detail.rightsState == '提前结清'){
									cssType="l_hasDoneper";
								}else if(detail.rightsState == '已冻结'){
									cssType="l_freeze";
								}else if(detail.rightsState == '转让中'){
									cssType="l_zhuanIng";
								}else if(detail.rightsState == '已转出'){
									cssType="l_hasZhuan";
								}else if(detail.rightsState == '申请转出'){
									cssType="l_shenqingZhuan";
								}
								$("#creditorRightsName").html(detail.creditorRightsName);//标题
								if(detail.awardRate!=''&&detail.awardRate!=null&&detail.awardRate!='0'&&parseFloat(detail.awardRate)>0){
									$("#annualRate").html(detail.annualRate+"+"+detail.awardRate+"%");//年化收益
								}else{
									$("#annualRate").html(detail.annualRate);//年化收益
								}
									$("#cycleCount").html(detail.cycleCount);//借款时长
									$("#repayMentMethod").html(detail.repayMentMethod);//还款方式
									$("#buyPrice").html(detail.buyPrice);//投资金额
								 	$("#expectProfit").html(detail.expectProfit);//预期收益
								for (var i = 0; i < list.length; i++) {
								
									if(list[i].rightsDetailState=="已还清"){
										
										if(i+1<list.length){
											if(list[i+1].rightsDetailState=="提前还款"){
												var money=0;
												var shouldCapital2=list[i].shouldCapital2;
												var shouldInterest2=list[i].shouldInterest2;
												var shouldFee=list[i].shouldFee;
												shouldCapital2=clearComma(shouldCapital2);
												shouldInterest2=clearComma(shouldInterest2);
												shouldFee=clearComma(shouldFee);
												money=parseFloat(shouldCapital2)+parseFloat(shouldInterest2)-parseFloat(shouldFee);
												for (var j = i+1; j < list.length; j++) {
													var shouldCapital=clearComma(list[j].shouldCapital2);
													money+=parseFloat(shouldCapital);
												}
												list[i].factMoney=money.toFixed(2);
											}
										}
									}
									if(list[i].rightsDetailState=="提前还款"){
										list[i].factMoney="---";
										list[i].shouldFee="---";
									}
								
									
								  	$("#annualRate").addClass(cssType); 
											var html="  <li>"+
										         	" <p>"+list[i].repaymentDayPlanned+"</p>"+
										          	"<p>"+list[i].allBackMoney+"</p>"+
										          	"<p>"+list[i].rightsDetailState+"</p>"+
										        	"</li>";
									$("#l_midInfo").append(html);
								}
							} else {
								alert(data.errMsg);
							}
						}, 'json');
	}
	
	function clearComma(str){
		str=str+"";
		str=str.replace(/,/g, "");
		return str;
	}
</script>
</html>


