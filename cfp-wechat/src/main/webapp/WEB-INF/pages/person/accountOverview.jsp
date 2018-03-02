<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="../common/taglibs.jsp" %>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
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
    <meta name="viewport"
          content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="msapplication-tap-highlight" content="no"/>
    <link rel="stylesheet" href="${ctx }/css/reset.css?${version}" type="text/css">
    <link rel="stylesheet" href="${ctx }/css/s_usercenter.css?${version}" type="text/css">
    <script src="${ctx}/js/lib/jquery-1.11.0.min.js"></script>
    <script type="text/javascript">
        var rootPath = '<%=ctx%>';
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
    </script>
    <title>我的资产</title>

</head>
<body class="bodyBj l_NewScroll">

<input type="hidden" value="${userFlag}" id="dis_flag">

<div class="assetsBox">
    <div class="setUp"><img onclick="location.href='${ctx }/person/moreInformation';"
                            src="${ctx }/images/userCenter/uc_icon01.png"/></div>
    <div class="assetsMain">
        <p class="assetsTitle">总资产（元）</p>
        <a href="${ctx }/finance/accountdetailview"><p class="assetsNum"><fmt:formatNumber value="${netAsset}"
                                                                                           pattern="#,##0.00"/></p></a>
    </div>
</div>
<div class="availableAssets">
    <div class="availableAssets_le">
        <p class="assetsLe_text">可用资金（元）</p>
        <p class="assetsRi_num"><fmt:formatNumber value="${cashAccount.availValue2}" pattern="#,##0.##"/></p>
    </div>
    <div class="availableAssets_ri">
        <p class="assetsLe_text">冻结资金（元）</p>
        <p class="assetsRi_num"><fmt:formatNumber value="${cashAccount.frozeValue2}" pattern="#,##0.##"/></p>
    </div>
    <div class="clear_0"></div>
</div>
<div class="ucenterMainbox">
    <dl>
        <dt><a href="${ctx }/person/toAllMyFinanceList"><img src="${ctx }/images/userCenter/uc_icon03.png"/></a></dt>
        <dd>省心计划</dd>
    </dl>
    <dl>
        <dt><a href="${ctx }/finance/toMyCreditRightList"><img src="${ctx }/images/userCenter/uc_icon04.png"/></a></dt>
        <dd>散标出借</dd>
    </dl>
    <dl>
        <dt><a href="${ctx }/finance/toMyCreditRightList?flag=Right">
            <img src="${ctx }/images/userCenter/uc_icon05.png"/></a></dt>
        <dd>债权转让</dd>
    </dl>
    <div class="clear_0"></div>
</div>
<div class="ucenterMainbox">
    <dl>
        <dt><a href="${ctx }/bankcard/to_bankcard_list"><img src="${ctx }/images/userCenter/uc_icon11.png"/></a></dt>
        <dd>银行卡</dd>
    </dl>
    <dl>
        <dt><a href="${ctx }/person/toVoucher"> <img src="${ctx }/images/userCenter/uc_icon07.png"/></a></dt>
        <dd>优惠劵</dd>
    </dl>
    <dl>
        <dt><a href="${ctx }/person/toFundManage?flag=0"><img src="${ctx }/images/userCenter/uc_icon08.png"/></a></dt>
        <dd>资金流水</dd>
    </dl>
    <div class="clear_0"></div>
</div>
<div class="ucenterMainbox">
    <dl>
        <dt><a href="${ctx }/person/toIncome"><img src="${ctx }/images/userCenter/uc_icon09.png"/></a></dt>
        <dd>充值</dd>
    </dl>
    <dl>
        <dt><a href="${ctx }/person/toWithDraw"><img src="${ctx }/images/userCenter/uc_icon10.png"/></a></dt>
        <dd>提现</dd>
    </dl>
    <dl>
        <dt><a onclick="inviteFri();" id="invite"><img src="${ctx }/images/userCenter/uc_icon06.png"/></a></dt>
        <dd>邀请好友</dd>
    </dl>
    <div class="clear_0"></div>
</div>
<div class="ucenterMainbox">
    <dl>
        <dt><a href="${ctx }/finance/toPaymentCalendar"><img src="${ctx }/images/userCenter/uc_icon19.png"/></a></dt>
        <dd>回款日历</dd>
    </dl>
    <div class="clear_0"></div>
</div>
<div class="empty"></div>

<div class="mask">
    <p id="listInfoClose"></p>
    <img src="${ctx}/images/icon_userList.png" alt="">
    <span>未参与用户不获得相应奖励</span>
    <c:if test="${empty invite_code}">
        <i id="listInfoBTN">了解活动详情</i>
    </c:if>
    <c:if test="${not empty invite_code}">
        <i id="listInfoBTN" onclick="location.href='${ctx}/person/distribution?invite_code=${invite_code}'">了解活动详情</i>
    </c:if>
</div>

<script type="text/javascript">
    function inviteFri() {
        var flag = $("#dis_flag").val();
        if (flag == "0") {
            //$(".mask").show();
            location.href = "${ctx }/person/to_myErweima";
        } else {
            location.href = "${ctx }/person/account/myRecommend";
        }
    }

    $("#listInfoClose").on("click", function () {
        $(".mask").hide();
    })

</script>
<c:if test="${not empty __open_account_notice__}">
    <%@include file="/WEB-INF/pages/cg/w_openBomb.jsp" %>
</c:if>
<%@include file="../common/navTag.jsp" %>
</body>
</html>


