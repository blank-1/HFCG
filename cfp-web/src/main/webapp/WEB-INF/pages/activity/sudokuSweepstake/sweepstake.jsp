<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<link href="/images/favicon.ico" rel="shortcut icon">
<link href="/images/favicon.ico" rel="Bookmark">
<meta name="keywords" content="" />
<meta name="description" content="" /> 
<!-- <meta http-equiv="X-UA-Compatible" content="IE=7;IE=9;IE=8;IE=10;IE=11"> -->
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">

<title>财富派</title>
<%@include file="../../common/common_js.jsp"%>
<link rel="stylesheet" type="text/css" href="http://caifupad.com/zt/css/base.css" /><!-- index css -->
<link rel="stylesheet" type="text/css" href="${ctx }/css/sudokuSweepstake/zty.css" />

</head>

<body>
	<%--登陆--%>
<!-- line2 start -->
<%@include file="../../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="0"/>
<!-- navindex end -->
<%@include file="../../login/login.jsp"%>
<div class="detail-zty d001"></div>
<input type="hidden" name="pastUrl" value="/activity/toWebTurntable">
<div <c:if test="${not empty sessionScope.currentUser }">class="detail-zty d002"</c:if> <c:if test="${empty sessionScope.currentUser }">class="detail-zty d005"</c:if> >
    <div class="wrapper">
    <a href="javascript:;" data-id="login" data-mask="mask" class="login"></a>
    <span class="t001"><fmt:formatNumber value="${allBuyBalance }" pattern="#,##0"/><em>元</em></span>
    <span class="t002">${shareNum }<em>次</em></span>
    </div>

</div>
<div class="detail-zty d003"></div>
<div class="detail-zty d004"></div>
<div class="detail-last">
    
    <div class="active-list wrapper">
        <div class="title">活动备注</div>
        <ul>
            <li>1、大转盘活动时间为1月25日10:00-1月31日！ 截止到1月24日平台累计投标额（已放款）决定您的抽奖次数；累计投标额100元(含)-10000元，1次抽奖机会；累计投标额10000元（含）-100000元，2次抽奖机会；累计投标额100000元（含）以上3次抽奖机会。</li>
            <li>2、转盘开启后已投资用户APP或微信分享只可多得一次抽奖机会。</li>
            <li>3、首投用户指1.1-3.15期间第一次有投标行为的用户。</li>
            <li>4、本月活动所有统计金额均以满标放款时间在1月1日0点至3月15日24点之前为准；2重礼活动以最高奖励金额计算，只奖励一次，3重礼活动同理。</li>
            <li>5、所有奖励金额以用户投标实际支付金额计算，不包括使用财富券抵扣金额。</li>
            <li>6、所有现金奖励在活动结束后三个工作日内返至财富派账户中。</li>
            <li>7、本活动奖励和平台其他活动同享。</li>
            <li>8、本活动最终解释权归财富派所有。</li>
        </ul>
    </div>
</div>

<style>
</style>
</body>
</html>
