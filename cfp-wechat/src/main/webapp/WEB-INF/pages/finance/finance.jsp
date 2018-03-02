<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="../common/taglibs.jsp" %>
<%@include file="../common/common_js.jsp"%>
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
    <link rel="stylesheet" href="${ctx }/css/financialProductsInfo.css?${version}" type="text/css">
    <link rel="stylesheet" href="${ctx }/css/sweetAlert.css?${version}" type="text/css">
    <link rel="stylesheet" href="${ctx }/css/swiper.min.css?${version}">
    <script data-main="${ctx }/js/biao.js?${version}" src="${ctx }/js/lib/require.js?${version}"></script>
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
    </script>
    <style>
        .l_disabled {background-color:#ccc;border:solid 1px #ccc;color: #afafaf;
        }
        .swal2-modal h2 {
            font-size: 1.4rem !important;
            margin: 0;
            padding: 0;
            line-height: 2rem !important;
        }
    </style>
    <title>详情页</title>
</head>
<body class="l_NewScroll l_sanbiao
    <c:if test="${not empty oType && oType ne '0'&& oType ne '3'}">dingxiang</c:if>
    <c:if test="${creditor_type eq 'zqzr'}">l_zhuanrang</c:if>
">
<!--无限额的页面需要给body加class="noLimt" 定向标页面加class="dingxiang" 散标页面加class="l_sanbiao" 转让标加class="l_zhuanrang" -->

<input type="hidden" id="goType" value="${goType}">
<input type="hidden" id="loanApplicationId" value="${loanApplicationListVO.loanApplicationId}"/>

<input type="hidden" id="applicationBegin" value="${loanApplicationListVO.begin}"/>
<input type="hidden" id="applicationState" value="${loanApplicationListVO.applicationState}"/>
<input type="hidden" id="loanApplicationNo" value="${loanApplicationNo}" >
<input type="hidden" id="publishRule" value="${loanPublish.publishRule}" />
<input type="hidden" id="currentUser" value="${empty sessionScope.currentUser}"/>
<input type="hidden" id="oType" value="${oType}"/>

<input type="hidden" id="nextRepaymentDay" value="${nextRepaymentDay}">

<input type="hidden" id="qitou" name="qitou" value="${loanApplicationListVO.startAmount}"/>

<c:if test="${loanApplicationListVO.begin eq'false'&&loanApplicationListVO.applicationState eq '3'}">
    <input type="hidden" id="timerFlag" value="1"/>
    <input type="hidden" id="secondBetwween" value="${secondBetwween}"/>
</c:if>
<input type="hidden" id="sxTimer" value="${second}"/>

<input type="hidden" id="creditor_type" value="${creditor_type}"/>
<input type="hidden" id="residualTime" value="${residualTime}"/>

<h1 class="l_title text-overflow
    <c:if test="${loanApplicationListVO.loanType eq '8'}">title_car</c:if>
    <c:if test="${loanApplicationListVO.loanType eq '0'}">l_titleIcon_belive</c:if>
    <c:if test="${loanApplicationListVO.loanType eq '7'}">l_titleIcon_fczt</c:if>
    <c:if test="${loanApplicationListVO.loanType eq '1'}">l_titleIcon_apartment</c:if>
    <c:if test="${loanApplicationListVO.loanType eq '2'}">l_titleIcon_car</c:if>
    <c:if test="${loanApplicationListVO.loanType eq '3'}">l_titleIcon_company</c:if>
    <c:if test="${loanApplicationListVO.loanType eq '4'}">l_titleIcon_baoli</c:if>
    <c:if test="${loanApplicationListVO.loanType eq '5'}">l_titleIcon_invest</c:if>
    <c:if test="${loanApplicationListVO.loanType eq '6'}">l_titleIcon_qyb</c:if>
    <c:if test="${loanApplicationListVO.loanType eq '9'}">l_titleIcon_xjd</c:if>
">
 ${loanApplicationListVO.loanApplicationTitle}
</h1>
<header>
    <ul>
        <li class="l_headerInfo1 pjred">
            <p>
                <i>${loanApplicationListVO.annualRate}</i>%
                <c:if test="${loanApplicationListVO.rewardsPercent != null && loanApplicationListVO.rewardsPercent gt 0 && loanApplicationListVO.awardPoint != null && loanApplicationListVO.awardPoint != ''}">
                    +<i>${loanApplicationListVO.rewardsPercent}</i>%
                </c:if>
            </p>
            <p>年化收益</p>
        </li>
        <li class="l_headerInfo2">
            <p>
                <i class="pjred">
                    <c:if test="${creditor_type ne 'zqzr'}">
                        <c:if test="${loanApplicationListVO.loanType eq '9'}">${loanApplicationListVO.cycleCount*14}</c:if>
                        <c:if test="${loanApplicationListVO.loanType ne '9'}">${loanApplicationListVO.cycleCount}</c:if>
                    </c:if>
                    <c:if test="${creditor_type eq 'zqzr'}">${surpMonth}</c:if>
                </i>
                <c:if test="${loanApplicationListVO.loanType ne '9'}">个月</c:if>
                <c:if test="${loanApplicationListVO.loanType eq '9'}">天</c:if>

            </p>
            <p>
                <c:if test="${creditor_type eq 'zqzr'}">剩余期限</c:if>
                <c:if test="${creditor_type eq 'sb'}">
                    <c:choose>
                        <c:when test="${loanApplicationListVO.loanType eq '5'}">封闭期</c:when>
                        <c:otherwise>借款期限</c:otherwise>
                    </c:choose>
                </c:if>
            </p>
        </li>
        <li class="l_headerInfo3">
            <p><i class="pjred" data-money="${loanApplicationListVO.confirmBalance}" id="loanAmount"><fmt:formatNumber value="${loanApplicationListVO.confirmBalance}" pattern="#,##0.00#"/></i>元</p>
            <p>
                <c:if test="${creditor_type eq 'zqzr'}">转让价格</c:if>
                <c:if test="${creditor_type eq 'sb'}">
                    <c:choose>
                        <c:when test="${loanApplicationListVO.loanType eq '5'}">发行总额</c:when>
                        <c:otherwise>借款金额</c:otherwise>
                    </c:choose>
                </c:if>
            </p>
        </li>
        <li class="l_headerInfo4">
            <p>
                <c:if test="${creditor_type ne 'zqzr'}">
                    <i class="pjred" data-money="${loanApplicationListVO.maxBuyBalanceNow}" id="limited">
                        <fmt:formatNumber value="${loanApplicationListVO.maxBuyBalanceNow}" pattern="#,##0.00#"/>
                    </i>
                </c:if>
                <c:if test="${creditor_type eq 'zqzr'}">
                    <i class="pjred" data-money="${shouldCapital}" id="limited">
                        <fmt:formatNumber value="${shouldCapital}" pattern="#,##0.00#"/>
                    </i>
                </c:if>
                元
            </p>
            <p><c:if test="${creditor_type eq 'zqzr'}">剩余本金</c:if><c:if test="${creditor_type ne 'zqzr'}">出借限额</c:if></p>
        </li>
    </ul>
</header>
<section class="l_midInfo
    <c:if test="${loanApplicationListVO.loanType eq '0'
                ||loanApplicationListVO.loanType eq '1'
                ||loanApplicationListVO.loanType eq '7'
                ||loanApplicationListVO.loanType eq '8'}">l_person</c:if>

    <c:if test="${loanApplicationListVO.loanType eq '2'
                ||loanApplicationListVO.loanType eq '3'
                ||loanApplicationListVO.loanType eq '4'
                ||loanApplicationListVO.loanType eq '5'
                ||loanApplicationListVO.loanType eq '6'}">l_company</c:if>
">
    <c:if test="${creditor_type eq 'zqzr'}">
        <p class="timeDown">
            <font id="yure"></font>
        </p>
    </c:if>
    <p
        <c:if test="${creditor_type ne 'zqzr'}">
            <c:if test="${loanPublish.publishRule eq '0' && loanApplicationListVO.begin eq'false'}">class="l_incomein"</c:if>
        </c:if>
        <c:if test="${creditor_type eq 'zqzr'}">class="l_incomein"</c:if>
    >
        <c:if test="${creditor_type ne 'zqzr'}"><font id="yure"></font></c:if>
        <span class="l_perLine"><i><b>0%</b></i></span>
        <c:if test="${creditor_type ne 'zqzr'}">
            <span class="l_perLineInfo l_Info1" data-hasmoney="${loanApplicationListVO.confirmBalance-loanApplicationListVO.remain}" id="hasmoney">
                <fmt:formatNumber value="${loanApplicationListVO.confirmBalance-loanApplicationListVO.remain}" pattern="#,##0.00"/>元
            </span>
        </c:if>
        <c:if test="${creditor_type eq 'zqzr'}">
            <span class="l_perLineInfo l_Info1" data-hasmoney="${shouldCapital-lendRightsBalance}" id="hasmoney">
                <fmt:formatNumber value="${shouldCapital-lendRightsBalance}" pattern="#,##0.00"/>元
            </span>
        </c:if>
        <span class="l_perLineInfo l_Info2" id="mony2">
            <c:if test="${creditor_type ne 'zqzr'}"><fmt:formatNumber value="${loanApplicationListVO.remain}"
                                                                      pattern="#,##0.00"/></c:if>
            <c:if test="${creditor_type eq 'zqzr'}"><fmt:formatNumber value="${lendRightsBalance}" pattern="#,##0.00"/></c:if>
        </span>
    </p>

    <ul>
        <li>
            <c:if test="${loanApplicationListVO.repayMethod eq '1'}">
                <c:forEach items="${repayMentMethod}" var="method">
                    <c:if test="${method.value eq loanApplicationListVO.repayMentMethod}">${method.desc}</c:if>
                </c:forEach>
            </c:if>

            <c:if test="${loanApplicationListVO.repayMethod ne '1'}">
                <customUI:dictionaryTable constantTypeCode="repaymentMode" desc="true"
                                          key="${loanApplicationListVO.repayMethod}"/>
            </c:if>
        </li>
        <c:if test="${loanApplicationListVO.loanType ne '8' &&  loanApplicationListVO.loanType ne '9'}">
            <li>
                <c:if test="${loanApplicationListVO.loanType eq '0'}">无抵押</c:if>
                <c:if test="${loanApplicationListVO.loanType eq '1'|| loanApplicationListVO.loanType eq '7'}">抵押房</c:if>
                <c:if test="${loanApplicationListVO.loanType eq '2'}">抵押车</c:if>
                <c:if test="${loanApplicationListVO.loanType eq '3'}">信用贷</c:if>
                <c:if test="${loanApplicationListVO.loanType eq '4'}">保理项目</c:if>
                <c:if test="${loanApplicationListVO.loanType eq '5'}">基金</c:if>
                <c:if test="${loanApplicationListVO.loanType eq '6'}">企业标</c:if>
            </li>
            <li>
                <c:if test="${loanApplicationListVO.loanType ne '4'}"><!-- 如果不为保理 -->
                <c:if test="${loanApplicationListVO.loanType eq '3' || loanApplicationListVO.loanType eq '6'}"><!-- 如果为企业信贷 -->
                经营收入
                </c:if>
                <c:if test="${loanApplicationListVO.loanType ne '3' && loanApplicationListVO.loanType ne '6'}"><!-- 如果不为企业信贷 -->
                    ${enterpriseInfo.enterpriseName}
                </c:if>
                </c:if>
                <c:if test="${loanApplicationListVO.loanType eq '4'}">${factoringSnapshot.sourceOfRepayment}</c:if>
            </li>
        </c:if>

        <c:if test="${creditor_type eq 'zqzr'}">
            <li>${lendCustomerName}</li>
        </c:if>
    </ul>
</section>

<p class="l_CheckInfo">查看详情</p>

<section class="l_inputBox">
    <c:if test="${empty sessionScope.currentUser}">
        <p><span class="pjred" onclick="toLogin()">登录后查看</span></p>
    </c:if>
    <c:if test="${not empty sessionScope.currentUser}">
        <p>账户余额：<span class="pjred"><fmt:formatNumber value="${cashAccount.availValue2}" pattern="#,##0.00"/></span>元
        </p>
    </c:if>
    <p>预期收益：<span class="pjred" id="shouYi">0.00</span>元</p>

    <c:if test="${creditor_type eq 'zqzr'}">
        <input id="moneyInput" class="noNum1 limitNUM16" type="tel"
               placeholder="起投资金${loanApplicationListVO.startAmount}元" name="money">
    </c:if>
    <c:if test="${creditor_type ne 'zqzr'}">
        <c:if test="${loanPublish.publishRule eq '2'}">
            <input id="moneyInput" class="noNum1 limitNUM16" type="tel" placeholder="仅限省心用户" disabled="disabled"
                   name="money">
        </c:if>
        <c:if test="${loanPublish.publishRule ne '2'}">
            <input id="moneyInput" class="noNum1 limitNUM16" type="tel" name="money"
                   <c:if test="${oType=='2'&&isTargetUser==false}">disabled="disabled" placeholder="仅限定向用户"</c:if>
                   <c:if test="${oType!='2'}">placeholder="起投资金${loanApplicationListVO.startAmount}元"</c:if>
                   <c:if test="${loanApplicationListVO.applicationState eq '7'||loanApplicationListVO.applicationState eq '8'
            ||loanApplicationListVO.applicationState eq '4'||loanApplicationListVO.applicationState eq '5'
            ||loanApplicationListVO.applicationState eq '6'}">disabled="disabled" style="border: none;"</c:if>>
        </c:if>
    </c:if>

    <c:if test="${not empty oType && oType eq '1'}">
        <c:if test="${not empty sessionScope.currentUser}">
            <div class="l_inputPassword"><!--定向密码在此 -->
                <p>此标的为定向标，请输入定向密码</p>
                <input id="inputPassword" type="text" placeholder="请输入6位密码" name="input" maxlength="6"
                       <c:if test="${loanApplicationListVO.applicationState eq '7'||loanApplicationListVO.applicationState eq '8'
                    ||loanApplicationListVO.applicationState eq '4'||loanApplicationListVO.applicationState eq '5'
                    ||loanApplicationListVO.applicationState eq '6'}">disabled="disabled" style="border: none;" </c:if>>
            </div>
        </c:if>
    </c:if>
</section>

<p class="l_propto">
    <i class="l_checked"></i>
    <span>
        <c:choose>
            <c:when test="${loanApplicationListVO.loanType eq '8'}">
                <a href="${ctx }/finance/carPersonProtocol">《借款及服务协议》范本</a>
            </c:when>
            <c:when test="${loanApplicationListVO.loanType eq '9'}">
                <a href="${ctx }/finance/cashLoanProtocol">《借款及服务协议》范本</a>
            </c:when>
            <c:otherwise>
                <a href="${ctx }/finance/creditorProtocol">《借款及服务协议》范本</a>
            </c:otherwise>
        </c:choose>
    </span>
</p>

<input type="button"
       <c:if test="${creditor_type eq 'zqzr'}">value="立即投资"</c:if>
        <c:if test="${creditor_type ne 'zqzr'}">
            <c:choose>
                <c:when test="${loanApplicationListVO.applicationState eq '7'||loanApplicationListVO.applicationState eq '8'}">
                    value="已结清"
                </c:when>
                <c:when test="${loanApplicationListVO.applicationState eq '4'||loanApplicationListVO.applicationState eq '5'}">
                    value="已满标"
                </c:when>
                <c:when test="${loanApplicationListVO.applicationState eq '6'}">
                    value="还款中"
                </c:when>
                <c:otherwise>
                    <c:if test="${loanPublish.publishRule eq '0'}">
                        <c:choose>
                            <c:when test="${oType==3}">value="限新手用户"</c:when>
                            <c:when test="${oType==2&&isTargetUser==false}">value="限定向用户"</c:when>
                            <c:otherwise>value="立即投资"</c:otherwise>
                        </c:choose>
                    </c:if>
                    <c:if test="${loanPublish.publishRule ne '0'}">
                        <c:if test="${oType!=3}">onclick="toSxList()" </c:if>
                        <c:if test="${oType == 3}">value="限新手用户" </c:if>
                        <c:if test="${loanPublish.publishRule eq '1'}">value="我要优先"</c:if>
                        <c:if test="${loanPublish.publishRule eq '2'}">value="我要参与"</c:if>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </c:if>

        <c:if test="${not empty sessionScope.currentUser}">
            <c:if test="${creditor_type eq 'zqzr'}">id="btn2" class='l_btn'</c:if>
            <c:if test="${creditor_type ne 'zqzr'}">
                <c:if test="${loanPublish.publishRule ne '2'}">
                    <c:if test="${userExt.isVerified ne '2'}">id="verified"</c:if>
                    <c:if test="${userExt.isVerified eq '2'}">
                        <c:if test="${oType!='3'}">
                            <c:if test="${oType=='2'&&isTargetUser==true}">id="btn2"</c:if>
                            <c:if test="${oType!='2'}">id="btn2"</c:if>
                        </c:if>
                    </c:if>
                </c:if>
            </c:if>
        </c:if>
       <c:if test="${not empty mCount && mCount==0}">id="btn2"</c:if>
        <c:if test="${empty sessionScope.currentUser}">
            onclick="toLogin()"
            <c:if test="${creditor_type eq 'zqzr'}">class='l_btn'</c:if>
        </c:if>

        <c:if test="${creditor_type ne 'zqzr'}">
            <c:if test="${oType !='3'}">
                <c:if test="${oType!='2'}">class='l_btn'</c:if>
                <c:if test="${oType=='2'&&isTargetUser==true}">class='l_btn'</c:if>
                <c:if test="${oType=='2'&&isTargetUser==false}">class='l_btn l_disabled'</c:if>
            </c:if>
            <c:if test="${oType =='3'}">
                <c:if test="${not empty mCount && mCount>0}">class='l_btn l_disabled'</c:if>
                <c:if test="${empty mCount || mCount==0}">class='l_btn'</c:if>
            </c:if>
            <c:if test="${loanApplicationListVO.applicationState ne '3' || loanApplicationListVO.begin eq'false'}">
                style="background-color:#a9a9a9;color:#777575;border-color:#a9a9a9;height:4rem;border-radius:4px;" disabled="true"
            </c:if>
        </c:if>

/>

<div class="loading" id="loading" style="display: block;">
    <img src="${ctx}/images/loading.gif" alt="">
</div>
<form id="lendForm" action="" method="post">
    <input type="hidden" name="token" value="${token}"/>
    <input type="hidden" name="oType" value="${oType}"/>
    <input type="hidden" name="loanApplicationId" value="${loanApplicationListVO.loanApplicationId}"/>
    <input type="hidden" id="mCount" name="amount" value="${mCount}"/>
    <input type="hidden" id="targetPass" name="targetPass" value=""/>

    <input type="hidden" id="creditorRightsId" name="creditorRightsId" value="${creditorRightsId}"/>
    <input type="hidden" id="creditorRightsApplyId" name="creditorRightsApplyId" value="${creditorRightsApplyId}"/>
</form>
<script type="text/javascript">
    /****去登陆****/
    function toLogin() {
        path = location.href;
        path = path.split("/finance");
        path = "/finance" + path[1];
        location.href = rootPath + "/user/toLogin?pastUrl=" + path;
    }

    function toSxList() {
        var rule = $("#publishRule").val();
        var second = $("#sxTimer").val();
        if (rule == '0' && second == 0) {
            return false;
        }
        location.href = rootPath + "/finance/list?tab=shengxin"
    }
</script>
</body>
</html>

