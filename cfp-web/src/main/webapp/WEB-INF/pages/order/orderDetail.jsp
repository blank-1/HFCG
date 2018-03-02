<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8" />
    <meta name="keywords" content="" />
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
    <title>订单详情 - 财富派</title>
    <%@include file="../common/common_js.jsp"%>
    <script type="text/javascript" src="${ctx}/js/pay.js"></script><!-- pay javascript -->
    <script type="text/javascript" src="${ctx}/js/script/jquery-1.7.2.min.js"></script>
    <link href="${ctx}/css/base.css" rel="stylesheet" type="text/css">
	<link href="${ctx}/css/dingdan.css" rel="stylesheet" type="text/css">
</head>
<body>
<header>
    <%@include file="../common/headLine1.jsp"%>
</header>

<div class="container">
	<h2 class="dd-h2">订单支付</h2>
    <div class="dd-jr clearFloat">
    	<div class="dd-jr-l">
            <h3>${order.lendOrderName}</h3>
            <p>订单号码：${order.orderCode}</p>
        </div>
        <div class="dd-jr-r">
        	<h4>支付金额：<b><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></b>元</h4>
            <p>创建时间：<fmt:formatDate value="${order.buyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
        </div>
    </div>
    <ul class="dd-ul clearFloat">
    	<c:if test="${order.productType eq '2'}">
    		<li>
	        	<h5>预期年化收益范围</h5>
	            <p>${lendProduct.profitRate }%-${lendProduct.profitRateMax }%</p>
        	</li>
    	</c:if>
    	<c:if test="${order.productType ne '2'}">
    		<li>
	        	<h5>预期年化收益</h5>
	            <p><fmt:formatNumber value="${order.profitRate}" pattern="#,##0.00"/>%
	            <span>
	           		<c:if test="${not empty loanApplication.rewardsPercent && loanApplication.rewardsPercent gt 0}">
			      		<i style="color: #FE2A4D;">+${loanApplication.rewardsPercent}%</i>
			      	</c:if>
	            </span>
	            </p>
        	</li>
    	</c:if>
    	
    	<c:if test="${order.productType eq '2'}">
    		<li>
	        	<h5>省心期</h5>
	            <p><em>${order.timeLimit}</em>
	            	<c:if test="${order.timeLimitType eq '1'}">天</c:if>
	                <c:if test="${order.timeLimitType eq '2'}">个月</c:if>
	            </p>
        	</li>
    	</c:if>
    	<c:if test="${order.productType ne '2'}">
	    	<li>
	        	<h5>借款期限</h5>
	            <p><em>${order.timeLimit}</em>
	            	<c:if test="${order.timeLimitType eq '1'}">天</c:if>
	                <c:if test="${order.timeLimitType eq '2'}">个月</c:if>
	            </p>
	        </li>
    	</c:if>
        
        
        <li>
        	<h5>购买金额</h5>
            <p><em><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></em>元</p>
        </li>
        
        <c:if test="${order.productType eq '2'}">
        	<li>
	        	<h5>投资标的</h5>
	            <p>${dueTimeScope }月标的</p>
        	</li>
        </c:if>
        <c:if test="${order.productType ne '2'}">
        	<li>
	        	<h5>预期收益</h5>
	            <p><em><fmt:formatNumber value="${expectedInteresting}" pattern="#,##0.00"/>
	            	<c:if test="${not empty awardProfit && awardProfit gt 0}">
			      		<i style="color: #FE2A4D;">+${awardProfit}</i>
			      	</c:if>
	            </em>元</p>
        	</li>
        </c:if>
        
        <li style="border-right:none;">
        	<h5><i>订单状态</i></h5>
            <p><strong>
            	<c:if test="${order.productType eq '1'}">
	                <c:if test="${order.orderState eq '0'}">未支付</c:if>
	                <c:if test="${order.orderState eq '1'}">还款中</c:if>
	                <c:if test="${order.orderState eq '2'}">已结清</c:if>
	                <c:if test="${order.orderState eq '3'}">已过期</c:if>
	                <c:if test="${order.orderState eq '4'}">已撤销</c:if>
	                <c:if test="${order.orderState eq '5'}">已支付</c:if>
	                <c:if test="${order.orderState eq '7'}">流标</c:if>
				</c:if>
				<c:if test="${order.productType eq '2'}">
					<c:if test="${order.orderState eq '0'}">未支付</c:if>
	                <c:if test="${order.orderState eq '1'}">理财中</c:if>
	                <c:if test="${order.orderState eq '2'}">已结清</c:if>
	                <c:if test="${order.orderState eq '3'}">已过期</c:if>
	                <c:if test="${order.orderState eq '4'}">已撤销</c:if>
	                <c:if test="${order.orderState eq '5'}">已支付</c:if>
	                <c:if test="${order.orderState eq '6'}">授权期结束</c:if>
				</c:if>
				<c:if test="${order.productType eq '3'}">
			          <c:if test="${order.orderState eq '0'}">待支付</c:if>
			          <c:if test="${order.orderState eq '1'}">还款中</c:if>
			          <c:if test="${order.orderState eq '2'}">已结清</c:if>
			          <c:if test="${order.orderState eq '3'}">已过期</c:if>
			          <c:if test="${order.orderState eq '4'}">已撤销</c:if>
			          <c:if test="${order.orderState eq '5'}">已支付</c:if>
			          <c:if test="${order.orderState eq '6'}">退出中</c:if>
		        </c:if>
            </strong></p>
        </li>
    </ul>

	<c:if test="${not empty loanApplication.rewardsPercent && loanApplication.rewardsPercent gt 0}">
	    <c:if test="${not empty loanApplication.awardPoint }">
			<dl class="dd-dl clearFloat">
		    	<dt>奖励说明 ：</dt>
		        <dd>
		        	<font style="color: #FE2A4D;">${loanApplication.rewardsPercent}%</font>
		        	奖励金额${loanApplication.awardPoint}时发放
		        </dd>
	    	</dl>
	   	</c:if>
	</c:if>
		
    <dl class="dd-dl clearFloat">
    	<dt><c:if test="${order.productType ne '2'}">借款描述：</c:if><c:if test="${order.productType eq '2'}">产品描述：</c:if></dt>
        <dd><p>
        	<c:if test="${order.productType ne '2'}">${loanApplication.desc}</c:if><c:if test="${order.productType eq '2'}">${lendProduct.productDesc}</c:if>
        </p></dd>
    </dl>
    <c:if test="${not empty rateOrder || not empty activityOrder}">
    <dl class="dd-dl clearFloat">
    	<dt>备注：</dt>
    	<c:if test="${not empty rateOrder }">
    	<dd><p>
        	您已使用${rateOrder.rateValue }%加息券<c:if test="${not empty activityOrder }">，一周年庆加息${activityOrder.rateValue }% </c:if>
        </p></dd>
    	</c:if>
    </dl>
    </c:if>
    
    <!-- 省心计划收益方式（开始） -->
    <c:if test="${order.productType eq '2'}">
	    <div class="dd-tit" style="margin:20px 0">
	        <span>收益分配方式</span>
	    </div>
	    <div class="shengXinpaybox">
	        <div class="shengXinpay">
	        	<c:if test="${order.profitReturnConfig eq '0'}">收益复利投资</c:if>
	            <c:if test="${order.profitReturnConfig eq '1'}">收益提取至可用余额</c:if>
	            <c:if test="${empty order.profitReturnConfig }">收益提取至可用余额</c:if>
	        
	            <%-- <span <c:if test="${order.profitReturnConfig eq '0'}">class="yixuan"</c:if>>收益复利投资</span> --%>
	            <%-- <span <c:if test="${order.profitReturnConfig eq '1'}">class="yixuan"</c:if>>收益提取至可用余额</span> --%>
	        </div>
	        <div style="clear:both;height:20px;"></div>
	        <!-- <p class="shengxinTips"><i style="color:#FF5655;vertical-align: middle;">*</i> 回款利息收益将会在省心期内自动循环投资，收益更高</p> -->
	    </div>
    </c:if>
    <!-- 省心计划收益方式（结束） -->
    
    <div class="dd-btn">
    	<c:if test="${empty isLookProduct}">
	    	<c:if test="${order.productType eq '1'}"><a href="${ctx}/finance/bidding?loanApplicationNo=${loanApplication.loanApplicationId}" class="btn btn-error mt-20 floatRight width198 mr-25">产品详情</a></c:if>
	        <c:if test="${order.productType eq '2'}"><a href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${order.lendProductPublishId}" class="btn btn-error mt-20 floatRight width198 mr-25">产品详情</a></c:if>
	        <c:if test="${order.productType eq '3'}"> 
	        	<c:if test="${order.orderState ne '4'}">
	       			 <a href="${ctx}/finance/creditRightBidding?creditorRightsApplyId=${creditorRightsApplyId}" class="btn btn-error mt-20 floatRight width198 mr-25">产品详情</a>
	        	</c:if>
	        </c:if>
        </c:if>
        <c:if test="${not empty isLookProduct && isLookProduct}">
	    	<c:if test="${order.productType eq '1'}"><a href="${ctx}/finance/bidding?loanApplicationNo=${loanApplication.loanApplicationId}" class="btn btn-error mt-20 floatRight width198 mr-25">产品详情</a></c:if>
	        <c:if test="${order.productType eq '2'}"><a href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${order.lendProductPublishId}" class="btn btn-error mt-20 floatRight width198 mr-25">产品详情</a></c:if>
	        <c:if test="${order.productType eq '3'}"> 
	        	<c:if test="${order.orderState ne '4'}">
	       			 <a href="${ctx}/finance/creditRightBidding?creditorRightsApplyId=${creditorRightsApplyId}" class="btn btn-error mt-20 floatRight width198 mr-25">产品详情</a>
	        	</c:if>
	        </c:if>
        </c:if>
    </div>
    <div style="clear:both;"></div>
    <div class="dd-tit">
    	<span>支付方式</span>
        <em>支付时间：<fmt:formatDate value="${order.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/></em>
    </div>
    <div class="dd-prese">
	    <c:forEach items="${paymentOrderDetail}" var="detail">
	            <c:if test="${detail.amountType eq '0'}"><p class="pright display-inherit">余额支付：<i><fmt:formatNumber value="${detail.amount}" pattern="#,##0.00"/></i>元</p></c:if>
	            <c:if test="${detail.amountType eq '2'}"><p class="pright display-inherit">财富券支付：<i><fmt:formatNumber value="${detail.amount}" pattern="#,##0.00"/></i>元</p></c:if>
	            <c:if test="${detail.amountType eq '1'}"><p class="pright display-inherit">银行卡支付：<i><fmt:formatNumber value="${detail.amount}" pattern="#,##0.00"/></i>元</p></c:if>
	    </c:forEach>
    </div>
</div>

<!-- footer start -->
<%@include file="../common/footLine3.jsp"%>
<!-- footer end -->
</body>
</html>
