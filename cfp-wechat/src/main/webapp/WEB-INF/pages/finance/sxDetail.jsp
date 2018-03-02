<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="../common/taglibs.jsp" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="0">
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent"/>
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no"/>
<link rel="stylesheet" href="${ctx }/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/financialProductsInfo.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/sweetAlert.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx }/css/swiper.min.css?${version}">
<script data-main="${ctx }/js/financialProductsInfo.js?${version}" src="${ctx }/js/lib/require.js?${version}"></script>
<script type="text/javascript">
//rem自适应字体大小方法
var docEl = document.documentElement,
    resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
    recalc = function () {
        //设置根字体大小
        docEl.style.fontSize = 10 * (docEl.clientWidth / 320) + 'px';
    };
//绑定浏览器缩放与加载时间
window.addEventListener(resizeEvt, recalc, false);
document.addEventListener('DOMContentLoaded', recalc, false);
var rootPath = '<%=ctx%>';
</script>
<style>
.l_btnFull{background:#e6e6e6;color:#666;}
</style>
<title>详情页</title>
</head>
<body class="l_NewScroll <c:if test="${financeDetail.publishBalanceType eq '1'}">noLimt</c:if>"><!--无限额的页面需要给body加class="noLimt" -->
<input type="hidden" id="sx_startsAt" value="${financeDetail.startsAt}"/>
<input type="hidden" id="sx_upAt" value="${financeDetail.upAt}"/>
<input type="hidden" id="sx_yue" value="${userAccount.availValue2}"/>
<input type="hidden" id="sx_type" value="${financeDetail.publishBalanceType}"/>
<input type="hidden" id="sx_balance" value="${financeDetail.availableBalance}"/>
<input type="hidden" id="isVerified" value="${userExt.isVerified}"/>
<form action="" method="post" name="form" id="finance_detail_form">
    <input type="hidden" name="lendProductPublishId" id="lendProductPublishId" value="${financeDetail.lendProductPublishId}" />
    <input type="hidden" name="amount" id="amount" value="" />
</form>
<h1 class="l_title l_titleIcon_shengxin text-overflow">${financeDetail.publishName}</h1><!-- 名字 -->
<header>
    <ul>
        <li class="l_headerInfo1 pjred">
            <p>
                <i>${financeDetail.profitRate}</i>%
                <c:if test="${financeDetail.profitRate!=financeDetail.profitRateMax}">
                    －<i>${financeDetail.profitRateMax}</i>%
                </c:if>
            </p><!-- 收益区间 -->
            <p>预期年化收益</p>
        </li>
        <li class="l_headerInfo2">
            <p><i class="pjred">${financeDetail.timeLimit}</i> <!-- 期限 -->
                <c:if test="${financeDetail.timeLimitType == '1'}">日</c:if>
                <c:if test="${financeDetail.timeLimitType == '2'}">个月</c:if>
            </p><!-- 收益方式 -->
            <p>省心期</p>
        </li>
        <li class="l_headerInfo3">
            <p>
                <i class="pjred" data-money="${financeDetail.publishBalance>0?financeDetail.publishBalance:0}" id="money">
                    ${financeDetail.publishBalance>0?financeDetail.publishBalance:0}
                </i>元
            </p><!-- 总额度 -->
            <p>总额度</p>
        </li>
    </ul>
</header>
<section class="l_midInfo">
    <p> <!--预热标在此加class="l_incomein" -->
        <font>预热时间：00:10:00</font>
        <span class="l_perLine"><i><b>0%</b></i></span>
        <span class="l_perLineInfo l_Info1" data-hasmoney="${financeDetail.publishBalance-financeDetail.availableBalance}" id="hasmoney">${financeDetail.publishBalance-financeDetail.availableBalance}元</span>
        <span class="l_perLineInfo l_Info2">${financeDetail.availableBalance}元</span>
    </p>
    <ul>
        <li>周期还利息、到期还本金</li>
        <li>自动投标，回款复投</li>
        <li>1-12月热门优先标的</li>
    </ul>
</section>
<p class="l_CheckInfo" onclick="toDesc()">查看详情</p>
<section class="l_inputBox">
    <c:if test="${empty sessionScope.currentUser}">
        <p><span class="pjred" onclick="toLogin()">登录后查看</span></p>
    </c:if>
    <c:if test="${not empty sessionScope.currentUser}">
        <p>账户余额：<span class="pjred"><fmt:formatNumber value="${userAccount.availValue2}" pattern="#,##0.00"/></span>元</p>
    </c:if>
    <!--   <p>预期收益：<span class="pjred" id="shouYi">0.00</span>元</p> -->
    <input id="moneyInput" class="noNum1 limitNUM16" type="tel" placeholder="${financeDetail.startsAt}元起投,${financeDetail.upAt}元递增" name="money"
    <c:if test="${(financeDetail.availableBalance ==0 && financeDetail.publishState == 3)
                    ||(financeDetail.publishState > 3)}">disabled="disabled" style="border: none;" </c:if>>
</section>
<p class="l_propto"><i class="l_checked"></i><span><a href="${ctx}/finance/sxLawDesc?id=${financeDetail.lendProductPublishId}">《省心计划投资协议》</a></span></p>
<button
        <c:if test="${financeDetail.publishBalanceType ne '1'}">
            <c:if test="${financeDetail.publishState < 4&&financeDetail.availableBalance != 0  }">
                id="BTNcheck"  class="l_btn"  >  立即省心
            </c:if>
            <c:if test="${financeDetail.availableBalance ==0 && financeDetail.publishState == 3}">
                class="l_btn  l_btnFull " >    已满额
            </c:if>
            <c:if test="${financeDetail.publishState > 3}">
                class="l_btn  l_btnFull " >  已完成
            </c:if>
        </c:if>
        <c:if test="${financeDetail.publishBalanceType eq '1'}">
            <c:if test="${financeDetail.publishState == 4}">
                class="l_btn  l_btnFull " >  已完成
            </c:if>
            <c:if test="${financeDetail.publishState != 4}">
                id="BTNcheck"  class="l_btn" >  立即省心
            </c:if>
        </c:if>
</button>
</body>
<script type="text/javascript">
    /****去登陆****/
    function toLogin() {
        path = location.href;
        path = path.split("/finance");
        path = "/finance" + path[1];
        location.href = rootPath + "/user/toLogin?pastUrl=" + path;
    }
    /****查看详情****/
    function toDesc() {
        location.href = rootPath + "/finance/sxDesc?lendProductPublishId=" + $("#lendProductPublishId").val();
    }

</script>
</html>

