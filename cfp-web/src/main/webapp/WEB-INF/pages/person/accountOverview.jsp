<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="utf-8" />
	<meta http-equiv=”X-UA-Compatible” content=”IE=edge” >
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	
	<title>账户中心</title>
	<%@include file="../common/common_js.jsp"%>
</head>

<body class="body-back-gray">
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->


<!-- tabp start -->
<%-- <%request.setAttribute("tab","1");%>
<%@include file="accountCommon.jsp"%> --%>
 <input type="hidden" id="titleTab" value="0-0" />
<!-- tabp end -->

<div class="clear_0"></div>

<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><span>账户总览</span></li>
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
        <!-- finance-plan start -->
        <div class="finance-plan person-3 clearFloat">
            <div class="fin-tit">
                <p class="pfirst"><span>净资产</span><small>=账户余额+散标待回+省心账户+负债</small></p>
                <p class="p-second"><fmt:formatNumber value="${netAsset}" pattern="#,##0.00"/> <small>元</small></p>
            </div>
            <ul class="sum-g">
                <li class="li-01 clearFloat">
                    <div class="sum-left">
                        <span class="head">账户余额</span>
                        <span class="deep"><fmt:formatNumber value="${cashAccount.value2}" pattern="#,##0.##"/></span>
                        <span class="light">元</span>
                    </div>
                    <div class="sum-right">
                        <a href="${ctx}/person/toWithDraw" class="btn btn-link">提现</a>
                        <a href="${ctx}/person/toIncome" class="btn btn-error">充值</a>
                    </div>
                </li>
                <li class="li-03 clearFloat">
                    <div class="sum-left">
                        <span class="head">可用余额</span>
                        <span class="deep"><fmt:formatNumber value="${cashAccount.availValue2}" pattern="#,##0.##"/></span>
                        <span class="light">元</span>
                    </div>
                   <div class="sum-right">
                        <a href="${ctx}/finance/list?tab=heng" class="btn btn-link">立即出借</a>
                    </div>
                </li>
                <li class="li-02 clearFloat">
                    <div class="sum-left  user-border">
                        <span class="head">冻结余额</span>
                        <span class="deep"><fmt:formatNumber value="${cashAccount.frozeValue2}" pattern="#,##0.##"/></span>
                        <span class="light">元</span>
                    </div>
                </li>
                <li class="li-04 clearFloat">
                    <div class="sum-left ">
                        <p>
	                        <span class="head">财富券</span>
	                        <span class="deep"><fmt:formatNumber value="${voucherValue}" pattern="#,##0"/></span>
	                        <span class="light">元</span>
                        </p>
                        <p>
                            <span style="padding-left: 30px;">加息券</span>
                            <span class="deep"><fmt:formatNumber value="${rateNumber}" pattern="#,##0"/></span>
                            <span class="light">张</span>
                        </p>
                    </div>
                    <div class="sum-right user-lineh">
                        <a href="http://help.caifupad.com/guide/caifuquan/" class="btn btn-link" target="_blank">如何获取</a>
                        <a href="${ctx}/person/toVoucher" class="btn btn-error">查看</a>
                    </div>
                </li>
            </ul>
        </div>
        <!-- finance-plan end -->

        <!-- addBorrow start -->
        <div class="addBorrow person-3 clearFloat">
            <div class="fin-tit">
                <p class="pfirst"><span>累计出借</span><small>=散标累计+省心累计</small></p>
                <p class="p-second"><fmt:formatNumber value="${totalLendAmount}" pattern="#,##0.00"/> <small>元</small></p>
            </div>
            <ul class="sum-g">
                <li class="li-02 clearFloat">
                    <div class="sum-left">
                        <span class="head">散标待回本金</span>
                        <span class="deep"><fmt:formatNumber value="${capitalRecive}" pattern="#,##0.00"/></span>
                        <span class="light">元</span>
                    </div>
                </li>
                <li class="li-03 clearFloat">
                    <div class="sum-left">
                        <span class="head">散标待回利息</span>
                        <span class="deep"><fmt:formatNumber value="${interestRecive}" pattern="#,##0.00"/></span>
                        <span class="light">元</span>
                    </div>
                </li>
            </ul>
        </div>
        <!-- addBorrow end -->

        <!-- obtain start -->
        <div class="obtain person-3 clearFloat">
            
            <div class="fin-tit">
                <p class="pfirst"><span>已获收益</span><small>=已获利息+已获奖励</small></p>
                <p class="p-second"><fmt:formatNumber value="${allProfit}" pattern="#,##0.00"/> <small>元</small></p>
            </div>
            <ul class="sum-g">
                <li class="li-02 clearFloat">
                    <div class="sum-left">
                        <span class="head">已获利息</span>
                        <span class="deep"><fmt:formatNumber value="${totalProfit}" pattern="#,##0.00"/></span>
                        <span class="light">元</span>
                    </div>
                </li>
                <li class="li-03 clearFloat">
                    <div class="sum-left">
                        <span class="head">已获奖励</span>
                        <span class="deep"><fmt:formatNumber value="${totalAward}" pattern="#,##0.00"/></span>
                        <span class="light">元</span>
                    </div>
                </li>
            </ul>
        </div>
        <!-- obtain end -->

        <!-- p-finance-plan start -->
        <%-- <div class="p-finance-plan clearFloat">
            <div class="p-fin-plan-l clearFloat">
                <p class="pfirst">
                    <span>省心计划</span>
                    <small>高收益,安全保障</small>
                </p>
                <p class="plast">
                    <!-- <span>封闭期：<em>3~6</em>个月</span>
                     -->
                    <span>总资产：</span>
                    <small><fmt:formatNumber value="${financeTotalValue }" pattern="#,##0.00"/>元</small>
                    <span>参与计划总数：</span>
                    <small><fmt:formatNumber value="${financeTotalCounts }" pattern="#,##0"/>项</small>
                    <br />
                    <span>已获利息：</span>
                    <small><fmt:formatNumber value="${financeTotalInterest }" pattern="#,##0.00"/>元</small>
                    <span>在投回款利息：</span>
                    <small><fmt:formatNumber value="${financeWaitInterest }" pattern="#,##0.00"/>元</small>
                </p>
            </div>
            <div class="p-fin-plan-r"><a href="${ctx}/finance/list?tab=zhe" class="btn btn-error">立即加入</a></div>
        </div> --%>
        
        <!-- p-finance-plan start -->
        <div class="p-finance-plan clearFloat">
            <div class="fin-tit">
                <p class="pfirst"><span>省心账户</span><a href="${ctx }/finance/list?tab=zhe"></a></p>
            </div>
            <ul class="sum-g">
                <li class="li-01 clearFloat">
                    <div class="sum-left">
                        <span class="head">资产&nbsp;&nbsp;</span>
                        <span class="deep"><fmt:formatNumber value="${financeTotalValue }" pattern="#,##0.00"/></span>
                        <span class="light">元</span>
                    </div>
                </li>
                <li class="li-02 clearFloat">
                    <div class="sum-left">
                        <span class="head">理财中计划总数&nbsp;&nbsp;</span>
                        <span class="deep"><fmt:formatNumber value="${financeTotalCounts }" pattern="#,##0"/></span>
                        <span class="light">项</span>
                    </div>
                </li>
                 <li class="li-03 clearFloat">
                    <div class="sum-left">
                        <span class="head">已获收益&nbsp;&nbsp;</span>
                        <span class="deep"><fmt:formatNumber value="${financeTotalInterest }" pattern="#,##0.00"/></span>
                        <span class="light">元</span>
                    </div>
                </li>
                 <li class="li-04 clearFloat">
                    <div class="sum-left">
                        <span class="head">省心待回利息&nbsp;&nbsp;</span>
                        <span class="deep"><fmt:formatNumber value="${financeWaitInterest }" pattern="#,##0.00"/></span>
                        <span class="light">元</span>
                    </div>
                </li>
            </ul>
           <!--  <div class="p-fin-plan-l clearFloat">
               <p class="pfirst">
                   <span>理财计划</span>
                   <small>高收益,安全保障</small>
               </p>
               <p class="plast">
                   <span>封闭期：<em>3~6</em>个月</span>
                   <small>预期年化收益：<em>12+1%</em></small>
               </p>
           </div>
           <div class="p-fin-plan-r"><a href="#" class="btn btn-error">立即加入</a></div> -->
        </div>
        <!-- p-finance-plan end -->
        <!-- p-finance-plan end -->
        <div class="clear"></div>
        <div class="recent_transt clearFloat">
            <h2 class="bigtitle clearFloat">
                <span class="context">近期交易</span>
                <a href="${ctx}/person/to_lendorder_list" class="look_more">更多交易项目>></a>
            </h2>
            <table cellpadding="0" cellspacing="0" border="0">
                <tr class="tp_title">
                    <td width="40%">标题</td>
                    <td width="20%">交易金额</td>
                    <td width="20%">交易时间</td>
                    <td class="last" width="20%">操作</td>
                </tr>
                
                 <c:forEach items="${lendOrderRecent}" var="order">
		            <tr class="tp_list">
		                <td><a href="javascript:;">${order.lendOrderName}</a></td>
		                <td><fmt:formatNumber value="${order.buyBalance}" pattern="#,##0.00"/></td>
		                <td style="line-height:25px;"><fmt:formatDate value="${order.buyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		                <c:if test="${order.productType ne '2' }">
			                <td class="last"><a href="${ctx}/lendOrder/showOrderDetail?lendOrderId=${order.lendOrderId}" target="_blank" class="btn">查看详情</a></td>
		                </c:if>
		                <c:if test="${order.productType eq '2' }">
			                <td class="last"><a href="${ctx}/finance/getAllMyFinanceListDetail?lendOrderId=${order.lendOrderId}" target="_blank" class="btn">查看详情</a></td>
		                </c:if>
		            </tr>
		        </c:forEach>
        
            </table>
        </div>
    </div>
    <!-- pRight start -->
</div>
<!-- container end -->


<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->
<style type="text/css">
  ${demo.css}
</style>
</body>
</html>
