<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../common/common.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>订单详情</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  	<br/><br/>
  	<span style="margin-left: 20px;margin-top: 30px;font-size: large;font-weight: bold;">订单号：${lendOrder.orderCode }</span>
  	<span style="margin-left: 200px;font-size: large;font-weight: bold;">订单状态：
  		<!-- 1投资类 -->
  		<c:if test="${lendOrder.productType == 1 }">
  			<c:if test="${lendOrder.orderState ==0 }">未支付</c:if>
  			<c:if test="${lendOrder.orderState ==1 }">还款中</c:if>
  			<c:if test="${lendOrder.orderState ==2 }">已结清</c:if>
  			<c:if test="${lendOrder.orderState ==3 }">已过期</c:if>
  			<c:if test="${lendOrder.orderState ==4 }">已撤销</c:if>
  			<c:if test="${lendOrder.orderState ==5 }">已支付</c:if>
  			<c:if test="${lendOrder.orderState ==7 }">流标</c:if>
  		</c:if>
  		
  		<!-- 2省心计划 -->
  		<c:if test="${lendOrder.productType == 2 }">
  			<c:if test="${lendOrder.orderState ==0 }">未支付</c:if>
  			<c:if test="${lendOrder.orderState ==1 }">理财中</c:if>
  			<c:if test="${lendOrder.orderState ==2 }">已结清</c:if>
  			<c:if test="${lendOrder.orderState ==3 }">已过期</c:if>
  			<c:if test="${lendOrder.orderState ==4 }">已撤销</c:if>
  			<c:if test="${lendOrder.orderState ==5 }">已支付</c:if>
  			<c:if test="${lendOrder.orderState ==6 }">授权期结束</c:if>
  		</c:if>
  	</span>
  	<hr/>
  	<br/>
  	
  	<!-- 1投资类 -->
  	<c:if test="${lendOrder.productType == 1 }">
  		<span style="margin-left: 20px;font-weight: bold;">借款ID：${detailVO_2AZQ.loanApplicationId }</span>
  		<span style="margin-left: 200px;font-weight: bold;">借款标题：${detailVO_2AZQ.loanApplicationName }</span>
  	</c:if>
  	
  	<!-- 2省心计划 -->
  	<c:if test="${lendOrder.productType == 2 }">
  	  	<span style="margin-left: 20px;font-weight: bold;">省心计划标题：${detailVO_2BLC.publishName }</span>
  		<span style="margin-left: 200px;font-weight: bold;">省心计划期数：${detailVO_2ALC.timeLimit }${detailVO_2ALC.timeLimitType==2?'月':'天' }</span>
  	</c:if>

  	<div style="margin-left: 10px;margin-bottom: 50px;">
  		<table border="1" width="1200px;" style="font-size: 12px;">
  			<tr>
  				<td colspan="9">
  				债权信息
  				</td>
  			</tr>
  			<tr style="background-color: #6699CC;text-align: center;">
  				<td>起息日期</td>
  				<td>到期日期</td>
  				<td>预期年化利率<c:if test="${lendOrder.productType == 2 }">范围</c:if></td>
  				<td>时长</td>
  				<td>封闭期</td>
  				<td>起投金额</td>
  				<td>本期发行</td>
  				<td>付息方式</td>
  				<td>保障说明</td>
  			</tr>
  			<tr style="text-align: center;">
  				<td><fmt:formatDate value="${lendOrder.agreementStartDate }" pattern="yyyy-MM-dd"/></td>
  				<td><fmt:formatDate value="${lendOrder.agreementEndDate }" pattern="yyyy-MM-dd"/></td>
  				<td>${lendOrder.profitRate }<c:if test="${lendOrder.productType == 2 }">-${detailVO_2ALC.profitRateMax }</c:if>%</td>
  				<td>${lendOrder.timeLimit }${lendOrder.timeLimitType==2?'月':'日' }</td>
  				<td>${lendOrder.closingDate }${lendOrder.closingType==2?'月':'日' }</td>
  				
  				<!-- 1投资类 -->
  				<c:if test="${lendOrder.productType == 1 }">
	  				<td>${detailVO_2BZQ.startsAt }元</td>
	  				<td>${detailVO_2BZQ.confirmBalance }元</td>
	  				<td>${detailVO_2BZQ.toInterestPoint }</td>
	  				<td>${detailVO_2BZQ.guaranteeType }</td>
  				</c:if>
  				
  				<!-- 2省心计划 -->
  				<c:if test="${lendOrder.productType == 2 }">
	  				<td>${detailVO_2BLC.startsAt }元 </td>
	  				<td>${detailVO_2BLC.publishBalance }元</td>
	  				<td>${detailVO_2BLC.toInterestPoint }</td>
	  				<td>${detailVO_2BLC.guaranteeType }</td>
  				</c:if>
  				
  			</tr>
  		</table>
  	</div>
  	
  	<span style="margin-left: 20px;font-weight: bold;">购买人用户名：${detailVO_3A.realName }</span>
  	<span style="margin-left: 200px;font-weight: bold;">购买时间：${detailVO_3A.createTime }</span>
  	<span style="margin-left: 200px;font-weight: bold;">出借金额：${detailVO_3A.amount }元</span>
  	<div style="margin-left: 10px;">
  		<table border="1" width="1200px;" style="font-size: 12px;">
  			<tr>
  				<td colspan="9">
  				支付明细
  				</td>
  			</tr>
  			<tr style="background-color: #6699CC;text-align: center;">
  				<td>支付时间</td>
  				<td>支付单号</td>
  				<td>支付方式</td>
  				<td>支付平台</td>
  				<td>支付金额</td>
  				<td>支付结果</td>
  			</tr>
  			<c:forEach items="${detailVO_3B }" var="vo">
	  			<tr style="text-align: center;">
	  				<td>${vo.createTime }</td>
	  				<td>${vo.rechargeCode }</td>
	  				<td>${vo.amountType }</td>
	  				<td>${vo.channelName }</td>
	  				<td>${vo.amount }元</td>
	  				<td>${vo.status }</td>
	  			</tr>
  			</c:forEach>
  		</table>
  	</div>
  	
	<!-- 1投资类 -->
	<c:if test="${lendOrder.productType == 1 }">
		<a href=""></a>
	</c:if>
	
	<!-- 2省心计划 -->
	<c:if test="${lendOrder.productType == 2 }">
		<a style="margin-left: 20px;margin-top: 100px;" href="${ctx}/jsp/lendList/tofinancialPlanDetail?lendOrderId=${lendOrder.lendOrderId}">查看省心计划详情</a>
	</c:if>
  	
  </body>
</html>
