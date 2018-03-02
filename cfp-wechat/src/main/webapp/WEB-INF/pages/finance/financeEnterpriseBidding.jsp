<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content=""/>
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />    
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<%@include file="../common/common_js.jsp"%>
<link rel="stylesheet" href="${ctx }/css/style.css">
<link rel="stylesheet" href="${ctx }/css/styles.css">
<link rel="stylesheet" href="${ctx }/css/besideStyle.css">
<link rel="stylesheet" href="${ctx }/css/swiper.min.css">
<script src="${ctx }/js/swiper.min.js"></script>
<title>详情页</title>
<style>
*,*:before,*:after{
    -webkit-box-sizing:border-box;
}
/*银行协议*/
.masks{
	width: 100%;
	height: 100%;
	background: rgba(0,0,0,.5);
	position: fixed;
	top: 0;
	left: 0;
	display: none;
}
.swiper-container{
    width: 100%;
    height: 100%;
}
.swiper-slide{
    overflow-y:scroll;
    text-align: center;
    font-size: 18px;
    background: #fff;
    background: #000;
    position: relative;
}
.swiper-page{
  width: 100%;
  height: 3rem;
  text-align: center;
  position: absolute;
  bottom: 0;
  left: 0;
  background: rgba(0,83,120,0.4);
  z-index: 9999;
  color: #fff;
  text-indent: -6rem;
  font-size: 1.4rem;
}
.swiper-page p{
	margin:1rem 0 0 0;
}
.swiper-slide img{
  width: 100%;
  vertical-align:middle;
  position: absolute;
  left: 50%;
  margin-left: -50%;
  top:10%;
}
.w_listDetails li span{
	color: #afafaf;
	float: right;
	padding-right: 3rem;
	position: relative;
	
	background: url(../images/disable.png) no-repeat 80%;
	background-size: 10%;
}
.l_hasReport li{
	background: url(../images/has2.png) no-repeat right ;
	background-size: 6%;
	font-size: 1rem;
	width: 49%;
	float:left;
}
</style>
</head>
<c:choose>
	<c:when test="${oType==2||oType==1}">
		<body id="backgd" style="padding:0;" class="DirectionalBid">
	</c:when>
	<c:when test="${empty oType }">
		<body id="backgd" style="padding:0;" class="">
	</c:when>
	<c:when test="${oType==3}">
		<body id="backgd" style="padding:0;" class="DirectionalBidNew">
	</c:when>
</c:choose>
<input type="hidden" id="applicationBegin" name="loanApplicationBegin" value="${loanApplicationListVO.begin}"/>	
<input type="hidden" id="applicationState" name="applicationState" value="${loanApplicationListVO.applicationState}"/>	
<input type="hidden" value="${loanApplicationNo}" id="loanApplicationNo">
<input type="hidden" value="${loanPublish.publishRule}" id="publishRule" />
<input type="hidden" id="currentUser" name="currentUser" value="${empty sessionScope.currentUser}"/>	
<input type="hidden" id="oType" name="" value="${oType}"/>	
<script src="${ctx }/js/FinancingProductinfo.js" type="text/javascript"></script>
<script src="${ctx }/js/yxMobileSlider.js" type="text/javascript"></script>  
<div class="masks" id="masks" style="z-index: 9999;">
	<%@include file="../agreement/company3.jsp"%>      
</div>


<!--弹窗结束--> 
<!--/顶部错误提示栏开始/-->
	<span id="alart"></span>
    <!--/顶部错误提示栏结束/-->
<!--\头图信息开始\-->
	<div class="topinfo">
    	<ul>
        <c:if test="${loanApplicationListVO.loanType eq '6'}"><p class="l_icon_pledge" style="color:white;">${loanApplicationListVO.loanApplicationTitle}</p></c:if>
        <c:if test="${loanApplicationListVO.loanType eq '3'}"><p class="l_titleIconCompany" style="color:white;">${loanApplicationListVO.loanApplicationTitle}</p></c:if>
    	<c:if test="${loanApplicationListVO.loanType eq '4'}"><p class="l_titleIconFactoring" style="color:white;">${loanApplicationListVO.loanApplicationTitle}</p></c:if>
    	<c:if test="${loanApplicationListVO.loanType eq '2'}"><p class="l_titleIconCar" style="color:white;">${loanApplicationListVO.loanApplicationTitle}</p></c:if>
        <c:if test="${loanApplicationListVO.loanType eq '5'}"><p class="l_titleIconFund" style="color:white;">${loanApplicationListVO.loanApplicationTitle}</p></c:if>
        <li>还款方式：
          <c:if test="${loanApplicationListVO.repayMethod eq '1'}">
            <c:forEach items="${repayMentMethod}" var="method">
              <c:if test="${method.value eq loanApplicationListVO.repayMentMethod}">${method.desc}</c:if>
            </c:forEach>
          </c:if>
          
          <c:if test="${loanApplicationListVO.repayMethod ne '1'}">
            <customUI:dictionaryTable constantTypeCode="repaymentMode" desc="true" key="${loanApplicationListVO.repayMethod}"/>
          </c:if>
        </li>
        <li>项目类型：
        	<c:if test="${loanApplicationListVO.loanType eq '5'}">基金</c:if>
	        <c:if test="${loanApplicationListVO.loanType eq '4'}">保理项目</c:if>
          	<c:if test="${loanApplicationListVO.loanType eq '3'}">信用贷</c:if>
          	<c:if test="${loanApplicationListVO.loanType eq '2'}">抵押车</c:if>
          	<c:if test="${loanApplicationListVO.loanType eq '6'}">企业标</c:if>
	        </li>
        <li>还款来源：
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
	        <c:choose>
	        	<c:when test="${loanApplicationListVO.loanType eq '5'}">
	        		<a id="btn4"  onclick="showProtocols()">《投资管理协议》</a>
        			<a id="btn3" style="display: block;" onclick="showProtocol()">《资产转让交易协议》</a>
	        	</c:when>
	        	<c:otherwise>
	        		<a id="btn3" href="javaScript:showProtocolss();">《借款及服务协议》范本></a>
	        	</c:otherwise>
	        </c:choose>
        </ul>
        <p class="rinfo">年化收益率<br><span><a>${loanApplicationListVO.annualRate}</a>%
        <c:if test="${loanApplicationListVO.rewardsPercent != null && loanApplicationListVO.rewardsPercent gt 0 && loanApplicationListVO.awardPoint != null && loanApplicationListVO.awardPoint != ''}">
			<i style="font-style: normal;font-size: 2rem;">+${loanApplicationListVO.rewardsPercent}%</i>
		</c:if> 
        </span></p>
          
    </div>
<!--\头图信息结束\-->
<div class="middle">
<!--\环形进度条开始\-->
	
	<div class="circle" <c:if test="${loanPublish.publishRule ne '0'}">style="left:0;display:none;"</c:if>>
		<div class="pie_left"><div class="left"></div></div>
		<div class="pie_right"><div class="right"></div></div>
		<c:choose>
			<c:when test="${loanApplicationListVO.begin eq'false'}">
				<div class="mask"><p><span id="yure">预热</span></p></div>
			</c:when>
			<c:when test="${loanApplicationListVO.applicationState eq '9'||loanApplicationListVO.applicationState eq 'A'}">
				<div class="mask"><p><span>取消</span></p></div>
			</c:when>
			<c:when test="${loanApplicationListVO.applicationState eq '3'}">
				<div class="mask"><p>剩余金额<br><span class="xsize" id="mony2"><fmt:formatNumber value="${loanApplicationListVO.remain}" pattern="#,##0.00"/></span></p></div>
			</c:when>
			<c:otherwise>
				<div class="mask"><p><span id="manbiao">满标</span></p></div>
			</c:otherwise>
		</c:choose>
    </div>
      
    <c:if test="${loanPublish.publishRule eq '1'}">
    	<img id="img_sheng1" src="${ctx }/images/icon_sheng1.png" alt="" style="width: 40%;margin-top: 2rem;"/>
    	<p id="timer_p" style="position:absolute;bottom:1rem;left:5%;margin:0;color:#0199dc;text-align:center;display:none;">预计结束时间：
    	<span id="timer_s">60秒</span></p>
    </c:if>
    <c:if test="${loanPublish.publishRule eq '2'}">
    	<img src="${ctx }/images/icon_sheng2.png" alt="" style="width: 40%;margin-top: 2rem;"/>
    </c:if>
<!--\ 借款信息开始\-->
<div class="brwinfo">
<c:choose>
	<c:when test="${loanApplicationListVO.loanType eq '5'}">
		<p>封闭期&nbsp;<span><a>${loanApplicationListVO.cycleCount}</a>个月</span></p>
		<p>发行总额(元)<br><span id="loanAmount"><fmt:formatNumber value="${loanApplicationListVO.confirmBalance}" pattern="#,#00.00#"/></span></p>
		<p style="display: none;">出借限额(元)<br><span id="limited"><fmt:formatNumber value="${loanApplicationListVO.maxBuyBalanceNow}" pattern="#,##0.00"/></span></p>
	</c:when>
	<c:otherwise>
		<p>借款期限&nbsp;<span><a>${loanApplicationListVO.cycleCount}</a>个月</span></p>
		<p>借款金额(元)<br><span id="loanAmount"><fmt:formatNumber value="${loanApplicationListVO.confirmBalance}" pattern="#,#00.00#"/></span></p>
		<p>出借限额(元)<br><span id="limited"><fmt:formatNumber value="${loanApplicationListVO.maxBuyBalanceNow}" pattern="#,##0.00"/></span></p>
	</c:otherwise>
</c:choose>

</div>
<div style="clear:both;"></div> 
<!--\ 借款信息结束\-->
</div>
<!--\ 投资输入框开始\-->
<p style="font-size:1.4rem; color:#9b9ea6;padding: 0 2%;margin:1% 2%;">预期收益：<span id="sy" style="color:#fe2a4d;">0.00</span>元</p>
<p style="font-size:1rem; color:rgb(200,200,200);padding: 0 2%;margin:1% 2%;">(市场有风险，投资需谨慎)</p> 
<input type="hidden" id="qitou" name="qitou" value="${loanApplicationListVO.startAmount}"/>	
<form  id="lendForm" action="" method="post" style="margin:1rem 2% 0 2%;">
<input type="hidden" id="token" name="token" value="${token}"/>	
<input type="hidden" id="loanApplicationId" name = "loanApplicationId" value="${loanApplicationListVO.loanApplicationId}"/>
<input type="hidden" id="mCount" name="mCount" value="${mCount}"/>		
	<input type="tel" name="amount" id="money" onclick="callAndroid()"
	oninput="expectedProfit()" disabled autocomplete="off" maxlength="10"
	<c:if test="${loanApplicationListVO.applicationState ne '3' || loanApplicationListVO.begin eq'false'}"> disabled="disabled"</c:if>
	<c:if test="${loanPublish.publishRule eq '2'}">
		value="点击右侧,了解更多" 
	</c:if>
	<c:if test="${loanPublish.publishRule ne '2'}">
		value="起投金额(${loanApplicationListVO.startAmount}元)" 
	</c:if>
	class="yzm"
	style="color:#afafaf;height:4rem;width:66%;border-radius:4px;"
	onfocus="if(value=='起投金额(${loanApplicationListVO.startAmount}元)'){this.style.color='#4b4b4b';value=''}"
	onblur="if(value==''){this.style.color='#afafaf';value='起投金额(${loanApplicationListVO.startAmount}元)'};return sy();" />
	
	<input type="button" name="btn2" 
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
		<c:if test="${not empty sessionScope.currentUser}">
			<c:if test="${loanPublish.publishRule ne '2'}">
				<c:if test="${userExt.isVerified ne '1'}">id="verified"</c:if>
				<c:if test="${userExt.isVerified eq '1'}">
					<c:if test="${oType!='3'}">  id="btn2"</c:if>
				</c:if>
			</c:if>
		</c:if>
		<c:if test="${empty sessionScope.currentUser}">id='login'</c:if>
		<c:if test="${userExt.isVerified ne '1'}">class='tzbtn'</c:if> 
		<c:if test="${userExt.isVerified eq '1'}">
			<c:if test="${loanPublish.publishRule ne '0'}">class='tzbtn'</c:if>
			<c:if test="${loanPublish.publishRule eq '0'}">class='tzbtn l_disabled'</c:if>
		</c:if> 
		<c:if test="${loanApplicationListVO.applicationState ne '3' || loanApplicationListVO.begin eq'false'}"> 
		style="background-color:#a9a9a9;color:#777575;border-color:#a9a9a9;height:4rem;border-radius:4px;" disabled="true"
		</c:if> />
</form>


<iframe id="test" name="test" style="display:none;"></iframe>
	
	<ul class="w_listDetails" style="margin-top:6%;">
	    <li id="aa" onclick="l_financinBox()"><p>借款详情<span>查看详情</span></p></li>
	    <li <c:if test="${customerPicNum>0}">onclick="w_maskBox()"</c:if>><p>项目证明<span>共${customerPicNum}张图</span></p></li>
	    <li <c:if test="${enterprisePicNum>0}">onclick="we_maskBox()"</c:if>><p>企业证明<span>共${enterprisePicNum}张图</span></p></li>
	    <li onclick="w_tenderBox()"><p>投标记录<span>共${totalNum}人</span></p></li>
	    <li onclick="payList()"><p>还款列表<span>查看详情</span></p></li>
	</ul>
	
	<!--\ 借款详情开始\-->
	<div class="l_financinBox" id="l_financinBox">
		<div class="l_minHeight">
	        <p class="l_infoTitle">标的信息</p>
	        <ul>
	            <li>借款用途：
	            	<span>
	            		<customUI:dictionaryTable constantTypeCode="enterpriseLoanUseage" desc="true" key="${loanApplicationListVO.loanUseage}"/>
	            	</span>
	            </li>
	            <li>用途描述：<span>${loanApplicationListVO.useageDesc}</span></li>
	        </ul>
	        <p class="l_infoTitle">公司信息</p>
	        <ul>
	            <li>企业名称:<span>${enterpriseInfo.jmEnterpriseName}</span></li>
	            <li>组织机构代码:<span>${enterpriseInfo.jmOrganizationCode}</span></li>
	            <li>经营年限:<span>${enterpriseInfo.operatingPeriod}年</span></li>
	            <li>注册资金：<span><fmt:formatNumber value="${enterpriseInfo.registeredCapital}" pattern="#,##0.00"/></span></li>
	            <li>企业信息：<span>${enterpriseInfo.information}</span></li>
	        </ul>
	        <c:if test="${loanApplicationListVO.loanType eq '2'}">
	        	<p class="l_infoTitle">抵押信息</p>
	        	<div class="l_CarInfo1" >
	        		<ul style="width: 30%;background: #ddf5ff;text-align:center;">
	                    <li>序号</li>
	                    <li>类型</li>
	                    <li>汽车品牌</li>
	                    <li>汽车型号</li>
	                    <li>市场价格（万）</li>
	                    <li>车架号</li>
	                    <li>变更信息</li>
	                </ul>
		        	<div class="l_CarInfo1_Box" id="carInfo"></div>
	        	</div>
	        </c:if>
        </div>
        <div>
           <img src="${ctx }/images/logo3.png" alt=""> 
        </div>
    </div>
    <!--\ 借款详情结束\-->
    
    <!--\ 项目证明开始\-->
    <div class="w_maskBox" id="w_maskBox">
	    <div class="swiper-container tone" style="width: 100%;height: 100%">
	        <div class="swiper-wrapper">
	        	<c:forEach items="${customerUploadSnapshots}" var="customersnapshot">
	        		<div class="swiper-slide one"><img src="${picPath}${customersnapshot.attachment.url}" alt="${customersnapshot.attachment.fileName}"></div>
	        	</c:forEach>
	        </div>
	        <div class="swiper-page">
	            <p><span class="w_Numbers">1</span>/<span class="w_altogether oneAll"></span></p>
	        </div>
	    </div>
	</div>
	<!--\ 项目证明结束\-->
	
	<!--\ 企业证明开始\-->
    <div class="w_maskBox" id="we_maskBox">
	    <div class="swiper-container ttwo" style="width: 100%;height: 100%">
	        <div class="swiper-wrapper">
	        	<c:forEach items="${enterpriseInfoSnapshots}" var="customersnapshot">
	        		<div class="swiper-slide two"><img src="${picPath}${customersnapshot.attachment.url}" alt="${customersnapshot.attachment.fileName}"></div>
	        	</c:forEach>
	        </div>
	        <div class="swiper-page">
	            <p><span class="w_Numbers">1</span>/<span class="w_altogether twoAll"></span></p>
	        </div>
	    </div>
	</div>
	<!--\ 企业证明结束\-->
	
	<!--\ 投标记录开始\-->
    <table class="gridtable l_gridtable" style="display: none;top:0;left: 0;z-index: 999999;" cellspacing="0" id="l_gridtable">
            <tr>
              <th style="width: 25%; text-align: center;">用户</th>
              <th style="width: 25%; text-align: center;">金额</th>
              <th style="width: 50%; text-align: center;">日期</th>
              
            </tr>
        </table>
	<div class="w_tenderBox" id="w_tenderBox" onScroll="toubiaoMsg()">
	    <table class="gridtable " cellspacing="0" id="touzi_list">
	    	<tr>
	          <th>用户</th>
	          <th>金额</th>
	          <th>日期</th>
	        </tr>
	        
	    </table>
	</div>
	<!--\ 投标记录结束\-->
	
<div class="maskBox" id="maskBox"></div>
<div class="dialog" id="dialog">
		<!-- 保理项目信息 -->
        <div class="l_PjInfo" id="blPjInfo">
            <p>项目信息</p>
            <ul>
                <li>融资方:<span>${financeParty.companyNameStr}</span></li>
                <li>融资金额:<span><fmt:formatNumber value="${factoringSnapshot.financingAmount}" pattern="#,##0.00"/>元</span></li>
                <li>授信金额:<span><fmt:formatNumber value="${loanApplicationListVO.loanBalance}" pattern="#,##0.00"/>元</span></li>
                <li>借款用途:<span><customUI:dictionaryTable constantTypeCode="enterpriseLoanUseage" desc="true"
                                                     key="${loanApplicationListVO.loanUseage}"/></span></li>
                <li>还款来源:<span>${factoringSnapshot.sourceOfRepaymentStr}</span></li>
                <li>年化利率:<span>${loanApplicationListVO.annualRate}%</span></li>
                <li>借款用途描述:<span>${loanApplicationListVO.useageDesc}</span></li>
            </ul>
        </div>
        <!-- 车贷信贷项目信息 -->
        <div class="l_PjInfo" id="PjInfo">
            <p>项目信息</p>
            <ul>
                <li>借款用途：<span><customUI:dictionaryTable constantTypeCode="enterpriseLoanUseage" desc="true"
                                                    key="${loanApplicationListVO.loanUseage}"/></span></li>
                <li>项目地区：<span>${address}</span></li>
                <li>内部评级：<span><customUI:dictionaryTable constantTypeCode="internalRating" desc="true"
                                                   key="${enterpriseCarLoanSnapshot.internalRating}"/></span></li>
                <li>用途描述：<span>${loanApplicationListVO.useageDesc}</span></li>
                <li>项目描述：<span>${enterpriseCarLoanSnapshot.projectDescription}</span></li>
                <li>风控信息：<span>${enterpriseCarLoanSnapshot.riskControlInformation}</span></li>
               
            </ul>
        </div>
        <div class="l_RiskControlScheme icon19" id="CompanyInformation" >
            <p>公司信息</p>
            <ul>
                <li style="margin-bottom: 4%;">企业名称:<span style="font-size: 1.2rem;">${enterpriseInfo.jmEnterpriseName} </span></li>
                <li style="margin-bottom: 4%;">组织机构代码:<span style="font-size: 1.2rem;">${enterpriseInfo.jmOrganizationCode}</span></li>
                <li style="margin-bottom: 4%;">经营年限:<span style="font-size: 1.2rem;">${enterpriseInfo.operatingPeriod}年</span></li>
                <li style="margin-bottom: 4%;">注册资金:<span style="font-size: 1.2rem;"><fmt:formatNumber value="${enterpriseInfo.registeredCapital}" pattern="#,##0.00"/></span></li>
                <li style="margin-bottom: 4%;">企业信息:<span style="font-size: 1.2rem;">${enterpriseInfo.information}</span></li>
            </ul>
        </div>   
        <div class="l_RiskControlScheme" id="RiskControlScheme">
            <p>风控信息</p>
            <ul>
                <li style="margin-bottom: 4%;">应收账款说明:<br><span style="font-size: 1.2rem;">${factoringSnapshot.accountReceivableDescription}</span></li>
                <li style="margin-bottom: 4%;">款项风险评价:<br><span style="font-size: 1.2rem;">${factoringSnapshot.moneyRiskAssessment}</span></li>
                <li style="margin-bottom: 4%;">项目综合评价:<br><span style="font-size: 1.2rem;">${factoringSnapshot.projectComprehensiveEvaluati}</span></li>
            </ul>
        </div>
        <div class="l_GuaranteeScheme" id="GuaranteeScheme">
            <p>项目保障方案</p>
            <ul style="height:16rem;overflow: scroll; ">
                 <li style="margin-bottom: 4%;font-size: 2rem;">360度实地尽调 - 大数据思维保障项目质量<br><span style="font-size: 1.2rem;">${factoringSnapshot.fieldAdjustmentValue}</span></li>
                 <li style="margin-bottom: 4%;font-size: 2rem;">还款保证金 - 构建风险缓释空间<br><span style="font-size: 1.2rem;">${factoringSnapshot.repaymentGuaranteeValue}</span></li> 
                 <li style="margin-bottom: 4%;font-size: 2rem;">法律援助基金 - 平台资金支持护航维权启动<br><span style="font-size: 1.2rem;">${factoringSnapshot.aidFundValue}</span></li>
            </ul>
        </div>
        <div class="l_FactoringCompany" id="FactoringCompany">
            <p>保理公司</p>
            <ul>
                <li style="margin-bottom: 4%;">企业名称:<span style="font-size: 1.2rem;">${enterpriseInfo.jmEnterpriseName} </span></li>
                <li style="margin-bottom: 4%;">组织机构代码:<span style="font-size: 1.2rem;">${enterpriseInfo.jmOrganizationCode}</span></li>
                <li style="margin-bottom: 4%;">经营年限:<span style="font-size: 1.2rem;">${enterpriseInfo.operatingPeriod}年</span></li>
                <li style="margin-bottom: 4%;">注册资金:<span style="font-size: 1.2rem;"><fmt:formatNumber value="${enterpriseInfo.registeredCapital}" pattern="#,##0.00"/></span></li>
                <li style="margin-bottom: 4%;">企业信息:<span style="font-size: 1.2rem;">${enterpriseInfo.information}</span></li>
            </ul>
        </div>
        
    	<div class="offBtn" id="offBtn">  关闭 </div>
</div>
       
<script type="text/javascript">
var second=${second};
var rule=$("#publishRule").val();
$(function(){
	<c:if test="${loanApplicationListVO.begin eq'false'&&loanApplicationListVO.applicationState eq '3'}">
		timer(parseInt(${secondBetwween}));
  	</c:if>
  	<c:if test="${loanApplicationListVO.loanType eq '2'}">
        searchCardHtml(1, 200);
    </c:if>
});
	var loanApplicationId=${loanApplicationListVO.loanApplicationId};
	//调用不同倒计时方法
	var intDiff = parseInt(${secondBetwween});//倒计时总秒数量
	//年月日倒计时
	function timer(intDiff){

	  var interval = window.setInterval(function(){
	          var day=0,
	              hour=0,
	              minute=0,
	              second=0;//时间默认值
	      if(intDiff > 0){
	        day = Math.floor(intDiff / (60 * 60 * 24));
	        hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
	        minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
	        second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
	      }
	    if(intDiff < 0){
	    	window.location.reload();
	    	return;
	    }

	    if (minute <= 9) minute = '0' + minute;
	      if (second <= 9) second = '0' + second;
	     $("#yure").html('预热中<br>剩余时间'+day+"天"+hour+":"+minute+':'+second);
	     //$("#manbiao").html('满标<br>剩余时间'+day+"天"+hour+":"+minute+':'+second); 
	      intDiff--;
	    }, 1000);
	}
	//车贷列表查询
    function searchCardHtml(page, rows) {
        var thtml = "";   
        $.ajax({
            url: rootPath + "/finance/getCardList",
            type: "post",
            data: {
                "pageSize": rows,
                "pageNo": page,
                "loanApplicationId": ${loanApplicationListVO.loanApplicationId}
            },
            success: function (data) {
                var d_rows = data.rows;
                var pageCount = data.totalPage;
                for (var i = 0; i < d_rows.length; i++) {
                    var data = d_rows[i];
                    var change = data.changeDesc == null ? "" : data.changeDesc;
                    thtml += '<ul>';
                    thtml += '<li>' + ((page - 1) * 5 + i + 1) + '</li>';
                    thtml += '<li>' + data.arrived + '抵</li>';
                    thtml += '<li>' + data.automobileBrand + '</li>';
                    thtml += '<li>' + data.carModel + '</li>';
                    thtml += '<li>' + fmoney(data.marketPrice, 2) + '</li>';
                    thtml += '<li>' + data.frameNumber + '</li><li>' + change + '</li>'
                    thtml += '</ul>';
                }
                $('#carInfo').html(thtml);
            }
        });

    }
    $(".slider").yxMobileSlider({width:300,height:230,during:3000});
    $(".slider1").yxMobileSlider1({width:320,height:260,during:3000});
  //弹出协议
  $("#aa").on('click',function(e){
           var target=e.target;
            while(target.nodeType!=1){
                target=target.parentNode;
            }
             var ele=e.target.tagName.toLowerCase();
             if(ele!='input' && ele!='textarea' && ele!='select'){
                e.preventDefault();
             }

        })
    function showProtocol(){
        $("#masks").show();
        $("#agreementss").show();
        $("#agreements").hide();
        $("#agreementsss").hide();
        $("body").css("overflow","hidden");
        $("body").css("position","fixed");
    }
    function offProtocol(){
        $("#masks").hide();
        $("body").css({"overflow":"auto","position":"relative"});
    }
    function showProtocols(){
        $("#masks").show();
        $("#agreements").show();
        $("#agreementss").hide();
        $("#agreementsss").hide();
        $("body").css("overflow","hidden");
        $("body").css("position","fixed");
    }
    function showProtocolss(){
        $("#masks").show();
        $("#agreements").hide();
        $("#agreementss").hide();
        $("#agreementsss").show();
        $("body").css("overflow","hidden");
        $("body").css("position","fixed");
        
    }
    
    
    function w_tenderBox(){
    	location.href=rootPath + "/finance/detail?loanApplicationNo="+$("#loanApplicationNo").val()+"&goType=4";
        
    }
    function l_financinBox(){
    	location.href=rootPath + "/finance/detail?loanApplicationNo="+$("#loanApplicationNo").val()+"&goType=1";
    }
    function w_maskBox(){
    	location.href=rootPath + "/finance/detail?loanApplicationNo="+$("#loanApplicationNo").val()+"&goType=2";
    }
    function we_maskBox(){
    	location.href=rootPath + "/finance/detail?loanApplicationNo="+$("#loanApplicationNo").val()+"&goType=3";
    }
    function payList(){
    	location.href=rootPath + "/finance/detail?loanApplicationNo="+$("#loanApplicationNo").val()+"&goType=6";
    }
</script>

   
</body>
</html>