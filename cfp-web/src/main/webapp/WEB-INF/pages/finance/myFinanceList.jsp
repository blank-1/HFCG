<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="../common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
	<meta name="keywords" content="" />
	<meta name="description" content="" /> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	
	<title>账户中心-省心计划</title>
	<%@include file="../common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx }/js/lcjh.js"></script>
	<script type="text/javascript" src="${ctx }/js/jquery_page.js"></script><!-- jquery_page javascript 分页 -->
	<style>
	/*--- 省心计划---*/
	.w_PeacePlan{width: 100%;border-bottom: 1px solid #e6e6e6;padding: 10px 0;}
	.w_PeacePlan li{display: inline-block;width: 32%;text-align: center;border-right: 1px solid #e6e6e6;}
	.totalAssets{background: url(../images/personal/SaveWorry.png) no-repeat 55px 8px;}
	.mountMoney{color: #999;} 
	.mountMoney i{font-size: 20px;color: #ff5e63;padding: 0 5px;}
	.investmentIncome{background: url(../images/personal/SaveWorry.png) no-repeat 60px -20px;}
	.haveBeenObtained{background: url(../images/personal/SaveWorry.png) no-repeat 80px -45px;}
	.w_Amount-money,.w_plan-project{width: 100%;border-bottom: 1px solid #e6e6e6;padding: 10px 0;}
	.w_Amount-money p,.w_plan-project p{display: inline-block;width: 38%;}
	.w_Amount-money p i,.w_plan-project p i{font-size: 16px;padding: 0 5px;}
	.AmountFfinancial{text-indent: 90px;background: url(../images/personal/SaveWorry.png) no-repeat 55px -97px;}
	.AmountTotal{background: url(../images/personal/SaveWorry.png) no-repeat 55px -72px;}
	.AmountLent{text-indent: 45px;background: url(../images/personal/SaveWorry.png) no-repeat 10px -122px;}
	.TotalParticipation{background: url(../images/personal/SaveWorry.png) no-repeat 10px -150px;}
	
	.p-Right-bot ul li ul li {
	    float: left;
	    width: 120px;
	    height: 60px;
	    line-height: 60px;
	    text-align: center;
	    font-size: 12px;
	    color: #666;
	}
	.p-Right-bot ul li ul li.p-Right-ulli-wrap {
	    line-height: 50px !important;
	}
	.lcjh-state {
		border:none;
	}
	</style>
</head>

<body class="body-back-gray">
	<input type="hidden" id="hidem" data-value="545247000" />
		<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
	<customUI:headLine action="3"/>
<!-- navindex end -->
 <input type="hidden" id="titleTab" value="1-1" />
<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a  href="javascript:;">我的理财</a>></li>
        <li><span>省心计划</span></li>
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
        <div class="p-Right-top">
			<p class="lcjh-title">省心计划</p>
			
			<!-- 新加统计区域（开始） -->
			<div class="lcjh-state">
				<ul class="w_PeacePlan">
					<li>
						<p class="totalAssets">省心账户总资产</p>
						<p class="mountMoney"><i><fmt:formatNumber value="${totalValue }" pattern="#,##0.00"/></i>元</p>
					</li>
					<li>
						<p class="investmentIncome">在投预期收益</p>
						<p class="mountMoney"><i><fmt:formatNumber value="${waitInterest }" pattern="#,##0.00"/></i>元</p>
					</li>
					<li style="border:none;">
						<p class="haveBeenObtained">已获收益</p>
						<p class="mountMoney"><i><fmt:formatNumber value="${currentProfit }" pattern="#,##0.00"/></i>元</p>
					</li>
				</ul>
				<div class="w_Amount-money">
					<p class="AmountFfinancial">理财中金额<i><fmt:formatNumber value="${financingValue }" pattern="#,##0.00"/></i><i style="color: #999;">元</i></p>
					<p class="AmountLent">待出借金额<i><fmt:formatNumber value="${forLendBalance }" pattern="#,##0.00"/></i><i style="color: #999;">元</i></p>
				</div>
				<div class="w_plan-project">
					<p class="AmountFfinancial AmountTotal">理财中计划总数<i>${financingOrderSize }</i><i style="color: #999;">项</i></p>
					<p class="AmountLent TotalParticipation">累计参与计划总数 <i>${allOrderSize }</i><i style="color: #999;">项</i></p>
				</div>
			</div>
			<!-- 新加统计区域（结束） -->
			
			<div class="lcjh-state">
				<div class="th-sorts clearFloat">
					<span class="a-head">省心计划状态</span>
					<a href="javascript:;" class="d a-head" value="">全部</a>	
					<a href="javascript:;" class="d" value="0">理财中</a>
					<a href="javascript:;" class="d" value="1">授权期结束</a>
					<a href="javascript:;" class="d" value="2">已结清</a>
				</div>
				<div class="th-sorts clearFloat"  id="all1">
					<span class="a-head">计划类型</span>
					<a href="javascript:;" class="c a-head" value="">全部</a>
					<c:forEach items="${timeLimitList }" var="timeLimit">
						<a href="javascript:;" class="c" value="${timeLimit }" >省心计划-${timeLimit }个月</a>	
					</c:forEach>
				</div>
			</div>
		</div>
        <div class="p-Right-bot">
			<ul id="myFinance">
				
			</ul>
		</div>
		<div class="tcdPageCode mt-20"></div>
    </div>
    <!-- pRight start -->
</div>
<!-- container end -->
		
<div class="zhezhao"></div>
<div class="zhezhao1"></div>
<!-- masklayer start  -->
<div class="masklayer" id="login">
	<div class="logind">
	<h2><span>登录财富派</span> <a href="javascript:" class="logina" data-id="close"></a></h2>
             <!-- equity start -->
            <div class="login">
                <form action="" method="post" name="form" class="">
                    <div class="input-group">
                        <label for="username">
                            <input type="text" value="" id="unlogin" maxlength="20" name="username" placeholder="用户名/手机号" class="ipt-input" />
                        </label>
                        <em class="hui"></em>
                    </div>
                    <div class="input-group clearFloat">
                        <label for="password">
                            <input type="password" value="" id="pwlogin" maxlength="16" name="password" placeholder="密码" class="ipt-input" />
                        </label>
                        <em class="passwordem floatLeft"></em><a class="write floatRight passworda mr-10"  href="re_password1.html">忘记密码？</a>
                    </div>
                    <div class="btn-group">
                        <button type="button" id="submit-login" class="btn btn-error mt-0">登录</button>
                        <a class="write floatRight passworda mr-10" href="register.html">账号</a>
                    </div>
                </form>
                
            </div><!-- equity start -->
</div>
</div><!-- masklayer end -->
	<%@include file="../login/login.jsp"%>
<!-- masklayer end -->
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->

</body>
</html>