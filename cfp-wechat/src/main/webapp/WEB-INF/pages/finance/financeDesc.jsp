<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="../common/taglibs.jsp" %>
<%@include file="../common/common_js.jsp" %>
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
<link rel="stylesheet" href="${ctx}/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/financialProductsInfo_borrowInfo.css?${version}" type="text/css">
<script data-main="${ctx}/js/financialProductsInfo_borrowInfo.js?${version}" src="${ctx}/js/lib/require.js"></script>
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
</script>
<title>详情页</title>
</head>
<body class="l_NewScroll
            <c:if test="${loanApplicationListVO.loanType eq '2'
                ||loanApplicationListVO.loanType eq '3'
                ||loanApplicationListVO.loanType eq '4'
                ||loanApplicationListVO.loanType eq '5'
                ||loanApplicationListVO.loanType eq '6'}">l_company</c:if>
"><!-- 企业标在此加class="l_company" -->

<input type="hidden" id="loanApplicationId" value="${loanApplicationListVO.loanApplicationId}">
<input type="hidden" id="creditorRightsId" value="${creditorRightsId}"/>
<input type="hidden" value="" id="reqUrl">
<input type="hidden" value="${goType}" id="goType">

<header>
    <p class="l_focus">借款详情</p>
    <p>项目证明</p>
    <p>企业证明</p>
    <p>投标记录</p>
    <p>还款列表</p>
</header>
<section class="l_borrowInfo">
    <ul>
        <h1>借款描述</h1>
        <span>${loanApplicationListVO.desc}</span>
    </ul>
    <ul>
        <h1>标的信息</h1>
        <p>借款用途：
            <span>
                <c:if test="${loanApplicationListVO.loanType eq '0' || loanApplicationListVO.loanType eq '1'|| loanApplicationListVO.loanType eq '7'|| loanApplicationListVO.loanType eq '8'|| loanApplicationListVO.loanType eq '9'}">
                    <customUI:dictionaryTable constantTypeCode="loanUseage" desc="true"
                                              key="${loanApplicationListVO.loanUseage}"/>
                </c:if>
                <c:if test="${loanApplicationListVO.loanType ne '0' && loanApplicationListVO.loanType ne '1' && loanApplicationListVO.loanType ne '7' && loanApplicationListVO.loanType eq '8'&& loanApplicationListVO.loanType ne '9'}">
                    <customUI:dictionaryTable constantTypeCode="enterpriseLoanUseage" desc="true"
                                              key="${loanApplicationListVO.loanUseage}"/>
                </c:if>
            </span>
        </p>
        <p>用途描述：<span>${loanApplicationListVO.useageDesc}</span></p>
    </ul>
    <c:if test="${loanApplicationListVO.loanType ne '9'}">
        <ul>
            <h1>
                <c:if test="${loanApplicationListVO.loanType eq '0' || loanApplicationListVO.loanType eq '1'|| loanApplicationListVO.loanType eq '7' || loanApplicationListVO.loanType eq '8'}">借款人基本信息</c:if>
                <c:if test="${loanApplicationListVO.loanType ne '0' && loanApplicationListVO.loanType ne '1' && loanApplicationListVO.loanType ne '7' && loanApplicationListVO.loanType ne '8'}">公司信息</c:if>
            </h1>
            <c:if test="${loanApplicationListVO.loanType eq '0' || loanApplicationListVO.loanType eq '1'|| loanApplicationListVO.loanType eq '7'|| loanApplicationListVO.loanType eq '8'}">
                <table cellspacing="0">
                    <tr>
                        <td>性别</td>
                        <td><customUI:dictionaryTable constantTypeCode="sex" desc="true" key="${basicSnapshot.sex}"/></td>
                    </tr>
                    <c:if test="${loanApplicationListVO.loanType ne '8'}">
                        <tr>
                            <td>婚姻状况</td>
                            <td>
                                <customUI:dictionaryTable constantTypeCode="isMarried" desc="true"
                                                          key="${basicSnapshot.isMarried}"/>
                                <c:if test="${not empty basicSnapshot.isMarried&&not empty basicSnapshot.childStatus }">/</c:if>
                                <customUI:dictionaryTable desc="true" constantTypeCode="childStatus"
                                                          key="${basicSnapshot.childStatus}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>最高学历</td>
                            <td><customUI:dictionaryTable desc="true" constantTypeCode="education"
                                                          key="${basicSnapshot.education}"/></td>
                        </tr>
                    </c:if>

                    <tr>
                        <td>月均收入</td>
                        <td><fmt:formatNumber value="${basicSnapshot.monthlyIncome}" pattern="#,##0.00"/>元</td>
                    </tr>
                        <%--<tr>
                            <td>年收入额</td>
                            <td>500.000.00元</td>
                        </tr>--%>
                    <c:if test="${loanApplicationListVO.loanType ne '8'}">
                        <tr>
                            <td>信用卡额度</td>
                            <td><fmt:formatNumber value="${basicSnapshot.maxCreditValue}" pattern="#,##0.00"/>元</td>
                        </tr>
                    </c:if>
                    <tr>
                        <td>现居住地址</td>
                        <td>${adress.provinceStr}${adress.cityStr}${adress.districtStr}${adress.detail}</td>
                    </tr>
                </table>
            </c:if>
            <c:if test="${loanApplicationListVO.loanType ne '0' && loanApplicationListVO.loanType ne '1' && loanApplicationListVO.loanType ne '7'&& loanApplicationListVO.loanType ne '8'}">
                <table cellspacing="0">
                    <tr>
                        <td>企业名称</td>
                        <td>${enterpriseInfo.jmEnterpriseName}</td>
                    </tr>
                    <tr>
                        <td>组织机构代码</td>
                        <td>${enterpriseInfo.jmOrganizationCode}</td>
                    </tr>
                    <tr>
                        <td>经营年限</td>
                        <td>${enterpriseInfo.operatingPeriod}年</td>
                    </tr>
                    <tr>
                        <td>注册资金</td>
                        <td><fmt:formatNumber value="${enterpriseInfo.registeredCapital}" pattern="#,##0.00"/></td>
                    </tr>
                    <tr>
                        <td>企业信息</td>
                        <td>${enterpriseInfo.information}</td>
                    </tr>
                </table>
            </c:if>
        </ul>
    </c:if>
    <ul class="l_shenheInfo
        <c:if test="${loanApplicationListVO.loanType ne '0' && loanApplicationListVO.loanType ne '1' && loanApplicationListVO.loanType ne '7'&& loanApplicationListVO.loanType ne '8'&& loanApplicationListVO.loanType ne '9'}">l_CheckInfoC</c:if>
        <c:if test="${loanApplicationListVO.loanType eq '0'||loanApplicationListVO.loanType eq '9'}">l_CheckInfoP</c:if>
        <c:if test="${loanApplicationListVO.loanType eq '8'}">l_CheckInfoTC</c:if>
        <c:if test="${loanApplicationListVO.loanType eq '1'||loanApplicationListVO.loanType eq '7'}">l_CheckInfoH</c:if>
    ">
        <h1>审核信息</h1>
        <li class="sfz">身份证</li>
        <li class="hkb">户口本</li>
        <li class="fcz">房产证</li>
        <li class="dhhs">电话核实</li>
        <li class="sdkc">实地考察</li>
        <li class="fcgj">房产估价</li>
        <li class="zxbg">征信报告</li>
        <li class="jykc">经营考察</li>
        <li class="xtyz">系统验证</li>
        <li class="sxdc">失信调查</li>
        <li class="bzcs">保证措施</li>
        <li class="cwbb">财务报表</li>
        <li class="frxx">法人信息</li>
        <li class="fwrz">房屋认证</li>
        <li class="gzkls">工资流水</li>
        <li class="gzls">工资流水</li>
        <li class="gzzm">工作证明</li>
        <li class="gszc">公司章程</li>
        <li class="gdjy">股东决议</li>
        <li class="gldc">关联调查</li>
        <li class="gtzm">国土证明</li>
        <li class="jbzz">基本证照</li>
        <li class="jhz">结婚证</li>
        <li class="jygm">经营规模</li>
        <li class="pfxt">评分系统</li>
        <li class="qsdc">亲属调查</li>
        <li class="srzm">收入证明</li>
        <li class="sjyz">数据验证</li>
        <li class="hyqj">行业前景</li>
        <!-- 个人信用车贷开始 sfz， dhhs，sdkc ,cljy,clgz-->
        <li class="sfyz">身份验证</li>
        <li class="cljy">车辆检验</li>
        <li class="clgz">车辆估值</li>
    </ul>

    <c:if test="${loanApplicationListVO.loanType eq '0' || loanApplicationListVO.loanType eq '1' || loanApplicationListVO.loanType eq '7'|| loanApplicationListVO.loanType eq '8'|| loanApplicationListVO.loanType eq '9'}">
        <ul class="l_rzbg">
            <h1>认证报告</h1>

            <c:forEach items="${authInfo}" var="auth" varStatus="stat">
                <c:if test="${ loanApplicationListVO.loanType ne '8'}">
                <li><customUI:dictionaryTable constantTypeCode="authReport" desc="true" key="${auth}"/></li>
                </c:if>
                <c:if test="${ loanApplicationListVO.loanType eq '8'}">
                    <li><customUI:dictionaryTable constantTypeCode="authReportForPeopelCar" desc="true" key="${auth}"/></li>
                </c:if>
            </c:forEach>
        </ul>
        <c:if test="${loanApplicationListVO.loanType ne '9'}">
        <c:if test="${loanApplicationListVO.loanType ne '0'}">
            <ul>

                <c:if test="${loanApplicationListVO.loanType ne '8'}">
                    <h1>抵押信息</h1>
                <table cellspacing="0">
                    <tr>
                        <td>抵押物类型</td>
                        <td>
                            <c:if test="${house.mortgageType eq '1'}">一抵</c:if>
                            <c:if test="${house.mortgageType eq '2'}">二抵</c:if>
                        </td>
                    </tr>

                        <tr>
                            <td>房屋地址</td>
                            <td>${houseAdress.provinceStr}${houseAdress.cityStr}${houseAdress.districtStr}${houseAdress.detail}</td>
                        </tr>
                        <tr>
                            <td>房屋面积</td>
                            <td><fmt:formatNumber value="${loanPublish.hourseSize}" pattern="#,##0.00"/>平方米</td>
                        </tr>
                        <tr>
                            <td>总评估值</td>
                            <td><fmt:formatNumber value="${loanPublish.assessValue}" pattern="#,##0.00"/> 万元</td>
                        </tr>
                        <tr>
                            <td>市值</td>
                            <td><fmt:formatNumber value="${loanPublish.marketValue}" pattern="#,##0.00"/> 万元</td>
                        </tr>
                        <%--<tr>
                            <td>抵押物说明</td>
                            <td>抵押物为宣武区一套244.68㎡房产。房屋为精装修，经评估公司评估。房屋为二抵，综合抵押率控制在评估价格7成以内。</td>
                        </tr>--%>
                </table>
                </c:if>
                <c:if test="${loanApplicationListVO.loanType eq '8'}">

                    <h1>车辆信息</h1>
                    <table cellspacing="0">
                        <tbody>
                     <%--   <tr>
                            <td>产品金额</td>
                            <td><fmt:formatNumber value="${basicInfoForPeopleAndCar.carMoney}" pattern="#,##0.00"/> 万元</td>
                        </tr>
                        <tr>
                            <td>市场价格</td>
                            <td><fmt:formatNumber value="${basicInfoForPeopleAndCar.originalPrice}" pattern="#,##0.00"/> 万元</td>
                        </tr>--%>

                        <tr>
                            <td>市场评估价格</td>
                            <td><fmt:formatNumber value="${basicInfoForPeopleAndCar.appraisal}" pattern="#,##0.00"/> 万元</td>
                        </tr>
                        <tr>
                            <td>型号</td>
                            <td>${basicInfoForPeopleAndCar.carModel}</td>
                        </tr>
                        <tr>
                            <td>行驶里程</td>
                            <td><fmt:formatNumber value="${basicInfoForPeopleAndCar.mileage}" pattern="#,##0.00"/> 公里</td>
                        </tr>
                        <tr>
                            <td>购买时间</td>
                            <td><fmt:formatDate value="${basicInfoForPeopleAndCar.buyTime}" pattern="yyyy-MM-dd" type="date"/> </td>
                        </tr>

                        </tbody></table>
                </c:if>
            </ul>
        </c:if>
        </c:if>
    </c:if>

    <c:if test="${loanApplicationListVO.loanType eq '2'}">
        <ul id="carInfo">
            <h1>抵押信息</h1>
        </ul>
    </c:if>

    <ul>
        <h1>风控步骤</h1>
        <p>
            <c:if test="${loanApplicationListVO.loanType ne '0' && loanApplicationListVO.loanType ne '1' && loanApplicationListVO.loanType ne '7'&& loanApplicationListVO.loanType ne '8'}">${enterpriseCarLoanSnapshot.riskControlInformation}</c:if>
            <c:if test="${loanApplicationListVO.loanType eq '0' || loanApplicationListVO.loanType eq '1' || loanApplicationListVO.loanType eq '7'|| loanApplicationListVO.loanType eq '8'|| loanApplicationListVO.loanType eq '9'}">${loanApplicationListVO.riskControlInformation}</c:if>
        </p>
    </ul>

    <img class="l_logo" src="${ctx}/images/logo02.png" alt=""/>
</section>

<section class="l_photoBox" id="l_swiperBox">
    <ul class="l_sections">
        <c:forEach items="${customerUploadSnapshots}" var="customersnapshot">
            <li style="background: url(${picPath}${customersnapshot.attachment.url}) 50% 50% / contain no-repeat"></li>
        </c:forEach>
    </ul>
</section>

<section class="l_photoBox2" id="l_swiperBox2">
    <ul class="l_sections2">
        <c:forEach items="${enterpriseInfoSnapshots}" var="customersnapshot">
            <li style="background: url(${picPath}${customersnapshot.attachment.url}) 50% 50% / contain no-repeat"></li>
        </c:forEach>
    </ul>
</section>

<section class="l_borrowList">
    <ul id="touzi_list"></ul>
    <p class="l_lastTip">向下滑动加载更多</p>
</section>

<section class="l_repayList">
    <p>
        <span>已还本息：<i><fmt:formatNumber value="${hasPaidBalance}" pattern="#,##0.00"/></i>元；待还本息：<i><fmt:formatNumber
                value="${waitPaidBalance}" pattern="#,##0.00"/></i>元</span>
        <span>待还本息可能会存在误差，实际金额以到账金额为准！</span>
    </p>
    <p><span>期数</span><span>合约还款日期</span><span>应还本息</span><span>还款状态</span></p>
    <ul>
        <c:forEach items="${showRepaypaymentList }" var="repay">
            <li
                    <c:if test="${goType eq 'zqzr' && stateMap[repay.planState] eq '已还清'}">style="display: none;"</c:if> >
                <span>${repay.sectionCode}</span>
                <span>
                    <c:if test="${isRepaying}"><fmt:formatDate value="${repay.repaymentDay}" pattern="yyyy-MM-dd"
                                                               type="date"/></c:if>
                    <c:if test="${!isRepaying}">————</c:if>
                </span>
                <span><i><fmt:formatNumber value="${repay.shouldBalance2 }" pattern="#,##0.00"/></i>元</span>
                <span>
                    <c:if test="${isRepaying}">${stateMap[repay.planState] }</c:if>
                    <c:if test="${!isRepaying}">未生效</c:if>
                </span>
            </li>
        </c:forEach>
    </ul>
</section>
</body>
</html>

