<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8" />
    <meta name="keywords" content="" />
    <meta name="description" content="" />
    <meta http-equiv="X-UA-Compatible" content="IE=7;IE=9;IE=8;IE=10;IE=11">
	<meta name="renderer" content="webkit|ie-comp|ie-stand">
    <title>订单详情 - 财富派</title>
    <script type="text/javascript" src="${ctx}/js/pay.js"></script><!-- pay javascript -->
    <link rel="stylesheet" type="text/css" href="${ctx}/css/index.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/style.css" />
</head>

<body>
<header>
    <nav class="logobor">
        <!-- navbg start -->
        <div class="wrapper clearFloat">
            <div class="logo floatLeft">
                <a href="${ctx}/" class=""><img src="${ctx}/img/finance_03.jpg" /></a>
            </div>
            <span class="floatLeft regissp">订单详情</span>
        </div><!-- navbg end -->
    </nav>

</header>
<div class="clear"></div>
<!-- article start -->
<div class="pay wrapper clearFloat">

    <!-- paytitle start -->
    <div class="paytitle mt-25">
        <!-- -->
        <div class="clearFloat">
            <div class="payleft">
                <img src="${ctx}/img/pay_07.jpg" class="floatLeft" />
                <div class="floatLeft">

                    <h2>${order.lendOrderName}</h2>
                    <span>订单号码：${order.orderCode}</span>
                </div>
            </div>
            <div class="payright">
                <p>支付金额：<big><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></big>元</p>
                <span>创建时间：<fmt:formatDate value="${order.buyTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </span>
            </div>
        </div>

        <!-- paycontext start-->
        <div class="paycontext clearFloat">
            <span class="first">预期年化收益<br /><i><fmt:formatNumber value="${order.profitRate}" pattern="#,##0.00"/></i>%</span>
            <span>借款期限<br /><i>${order.timeLimit}</i>
                <c:if test="${order.timeLimitType eq '1'}">天</c:if>
                <c:if test="${order.timeLimitType eq '2'}">个月</c:if>
                </span>
            <span>购买金额<br /><i id="balance"><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></i>元</span>
            <span>预期收益<br /><i><fmt:formatNumber value="${expectedInteresting}" pattern="#,##0.00"/></i>元 </span>
            <span class="last">订单状态<br /><i class="c_red">
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
	                <c:if test="${order.orderState eq '5'}">匹配中</c:if>
	                <c:if test="${order.orderState eq '6'}">退出</c:if>
				</c:if>

            </i></span>
        </div> <!-- paycontext end-->
        <!-- paycontext start-->
        <div class="paycontext clearFloat">
            <table cellpadding="0" cellspacing="" border="0">
                <c:if test="${order.productType eq '1'}">
                    <tr>
                        <td width="79">借款描述</td>
                        <td width="869">${loanApplication.applicationDesc}</td>
                    </tr>
                </c:if>
                <c:if test="${order.productType eq '2'}">
                    <tr>
                        <td width="79">产品描述</td>
                        <td width="869">${lendProduct.productDesc}</td>
                    </tr>
                </c:if>


            </table>

        </div> <!-- paycontext end-->

    </div> <!-- paytitle end -->
    <h2 class="mt-0 clearFloat"><span>支付方式</span><small class="floatRight fs_twelve mr-25">支付时间：<fmt:formatDate value="${order.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/></small> </h2>

    <!-- paypt start -->
    <div class="paypt">
        <c:forEach items="${paymentOrderDetail}" var="detail">
            <p class="clearFloat">
                <c:if test="${detail.amountType eq '0'}"><span class="pright display-inherit">余额支付：<big ><fmt:formatNumber value="${detail.amount}" pattern="#,##0.00"/></big>元</span></c:if>
                <c:if test="${detail.amountType eq '2'}"><span class="pright display-inherit">财富券支付：<big ><fmt:formatNumber value="${detail.amount}" pattern="#,##0.00"/></big>元</span></c:if>
                <c:if test="${detail.amountType eq '1'}"><span class="pright display-inherit">银行卡支付：<big ><fmt:formatNumber value="${detail.amount}" pattern="#,##0.00"/></big>元</span></c:if>
            </p>
        </c:forEach>
    </div> <!-- paypt end -->


</div><!-- article end -->

</body>
</html>
