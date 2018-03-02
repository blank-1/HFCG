<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
<link rel="stylesheet" href="${ctx }/css/reset.css">
<link rel="stylesheet" href="${ctx }/css/register.css">
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
.l_closeBtn{
	display: none;
    width: 100%;
    height: 3rem;
    background: rgba(0,83,120,0.4);
    position: fixed;
    z-index: 9999999;
    right: 0;
    bottom: 0;
    margin: 0;
    text-align: center;
}
.l_closeBtn span{
    color: #fff;
    font-size: 1.4rem;
    width: 4rem;
    height: 2rem;
    line-height: 2rem;
    text-align: center;
    display: block;
    border: solid 1px #fff;
    border-radius: 0.2rem;
    margin: 0.5rem auto;
}
.l_closeBtn1{
    display: none;
    width: 4rem;
    position: fixed;
    z-index: 9999999;
    right: 30%;
    bottom: 0;
    margin: 0;
}
.l_closeBtn1 span{
    color: #fff;
    font-size: 1.4rem;
    width: 4rem;
    height: 2rem;
    line-height: 2rem;
    text-align: center;
    display: block;
    border: solid 1px #fff;
    border-radius: 0.2rem;
    margin: 0.5rem auto;
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
<input type="hidden" value="${loanPublish.publishRule}" id="publishRule" />
<input type="hidden" value="${loanApplicationNo}" id="loanApplicationNo">
<input type="hidden" id="mCount" name="mCount" value="${mCount}"/>
<input type="hidden" id="currentUser" name="currentUser" value="${empty sessionScope.currentUser}"/>
<input type="hidden" id="applicationBegin" name="loanApplicationBegin" value="${loanApplicationListVO.begin}"/>
<input type="hidden" id="applicationState" name="applicationState" value="${loanApplicationListVO.applicationState}"/>
<!--/顶部错误提示栏开始/-->
	<span id="alart"></span>
    <!--/顶部错误提示栏结束/-->
<!--\头图信息开始\-->
	<div class="topinfo">
    	<ul>
    	<c:if test="${loanApplicationListVO.loanType eq '0'}"><p class="l_titleIconCompany" style="color:white;">${loanApplicationListVO.loanApplicationTitle}</p></c:if>
    	<c:if test="${loanApplicationListVO.loanType eq '1'||loanApplicationListVO.loanType eq '7'}"><p class="l_titleIconHouse" style="color:white;">${loanApplicationListVO.loanApplicationTitle}</p></c:if>
    	<c:if test="${loanApplicationListVO.loanType eq '2'}"><p class="l_titleIconCar" style="color:white;">${loanApplicationListVO.loanApplicationTitle}</p></c:if>

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
			<c:if test="${loanApplicationListVO.loanType ne '8'}">
        <li>抵押信息：
	        <c:if test="${loanApplicationListVO.loanType eq '0'}">无抵押</c:if>
	          <c:if test="${loanApplicationListVO.loanType eq '1'|| loanApplicationListVO.loanType eq '7'}">抵押房</c:if>
	          <c:if test="${loanApplicationListVO.loanType eq '2'}">抵押车</c:if>
	        </li>
			</c:if>
        <a id="btn3" href="javaScript:showProtocol();">出借咨询与服务协议></a>
        </ul>
        <p class="rinfo">年化收益率<br><span><a>${loanApplicationListVO.annualRate}</a>%
        <c:if test="${loanApplicationListVO.rewardsPercent !=null && loanApplicationListVO.rewardsPercent gt 0 && loanApplicationListVO.awardPoint != null && loanApplicationListVO.awardPoint != ''}">
        <i style="font-style: normal;font-size: 2rem;">+${loanApplicationListVO.rewardsPercent}%</i>
        </c:if>

        </span></p>
    </div>
<!--\头图信息结束\-->
<div class="middle">
<!--\环形进度条开始\-->

	<div class="circle"  <c:if test="${loanPublish.publishRule ne '0'}">style="left:0;display:none;"</c:if>>
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
<p>借款期限&nbsp;<span><a>${loanApplicationListVO.cycleCount}</a>个月</span></p>
<p>借款金额(元)<br><span id="loanAmount"><fmt:formatNumber value="${loanApplicationListVO.confirmBalance}" pattern="#,#00.00#"/></span></p>
<p>出借限额(元)<br><span id="limited"><fmt:formatNumber value="${loanApplicationListVO.maxBuyBalanceNow}" pattern="#,##0.00"/></span></p>
</div>
<div style="clear:both;"></div>
<!--\ 借款信息结束\-->
</div>
<!--\ 投资输入框开始\-->
<p style="font-size:1.4rem; color:#9b9ea6;padding: 0 2%;margin:1% 2%;"">预期收益：<span id="sy" style="color:#fe2a4d;">0.00</span>元</p>
<p style="font-size:1rem; color:rgb(200,200,200);padding: 0 2%;margin:1% 2%;">(市场有风险，投资需谨慎)</p>
<input type="hidden" id="qitou" name="qitou" value="${loanApplicationListVO.startAmount}"/>
<form  id="lendForm" action="" method="post" style="margin:1rem 2% 0 2%;">
<input type="hidden" id="token" name="token" value="${token}"/>
<input type="hidden" id="oType" name="" value="${oType}"/>
<input type="hidden" id="loanApplicationId" name = "loanApplicationId" value="${loanApplicationListVO.loanApplicationId}"/>

	<input type="tel" name="amount" id="money" onclick="callAndroid()"
	oninput="expectedProfit()" disabled autocomplete="off"
	maxlength="10"
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
	onblur="if(value==''){this.style.color='#afafaf';value='起投金额(${loanApplicationListVO.startAmount}元)'};
	return sy();"
	<c:if test="${oType==3}">  disabled='disabled'</c:if>
	/>


	<input type="button" name="btn2"
		<c:if test="${loanPublish.publishRule eq '0'}">
			<c:choose>
				<c:when test="${oType==3}">value="限新手用户"</c:when>
				<c:when test="${oType==2&&isTargetUser==false}">value="限定向用户"</c:when>
				<c:otherwise>value="立即投资"</c:otherwise>
			</c:choose>
		</c:if>
		<c:if test="${loanPublish.publishRule ne '0'}">
			<c:if test="${oType!='3'}"> onclick="toSxList(111)" </c:if>
			<c:if test="${loanPublish.publishRule eq '1'}">
			<c:if test="${oType !='3'}"> value="我要优先" </c:if>
			<c:if test="${oType =='3'}"> value="限新手用户" </c:if>

			</c:if>
			<c:if test="${loanPublish.publishRule eq '2'}">
			<c:if test="${oType !='3'}"> value="我要参与" </c:if>
			<c:if test="${oType =='3'}"> value="限新手用户" </c:if>
			</c:if>

		</c:if>
		<c:if test="${not empty sessionScope.currentUser}">
			<c:if test="${loanPublish.publishRule ne '2'}">
				<c:if test="${userExt.isVerified ne '2'}">id="verified"</c:if>
				<c:if test="${userExt.isVerified eq '2'}">
					<c:if test="${oType!='3'}">  id="btn2"</c:if>
				</c:if>
			</c:if>
		</c:if>
		<c:if test="${empty sessionScope.currentUser}">id='login'</c:if>
		<c:if test="${userExt.isVerified ne '2'}">
			<c:if test="${oType !='3'}"> class='tzbtn' </c:if>
			<c:if test="${oType =='3'}"> class='tzbtn l_disabled' </c:if>
		</c:if>
		<c:if test="${userExt.isVerified eq '2'}">
			<c:if test="${loanPublish.publishRule ne '0'}">

				<c:if test="${oType !='3'}"> class='tzbtn' </c:if>
			<c:if test="${oType =='3'}"> class='tzbtn l_disabled' </c:if>

			</c:if>
			<c:if test="${loanPublish.publishRule eq '0'}">
				<c:if test="${oType !='3'}"> class='tzbtn' </c:if>
			<c:if test="${oType =='3'}"> class='tzbtn l_disabled' </c:if>   </c:if>
		</c:if>
		<c:if test="${loanApplicationListVO.applicationState ne '3' || loanApplicationListVO.begin eq'false'}">
		style="background-color:#a9a9a9;color:#777575;border-color:#a9a9a9;height:4rem;border-radius:4px;" disabled="true"
		</c:if> />
</form>
<input  id="isVerified" type="hidden" value="${userExt.isVerified }" />

<iframe id="test" name="test" style="display:none;"></iframe>

    <ul class="w_listDetails" style="margin-top:6%;">
	    <li id="aa" onclick="l_financinBox()"><p>借款详情<span>查看详情</span></p></li>
	    <li <c:if test="${customerPicNum>0}">onclick="w_maskBox()"</c:if>><p>项目证明<span>共${customerPicNum}张图</span></p></li>
	    <li onclick="w_tenderBox()"><p>投标记录<span>共${totalNum}人</span></p></li>
	    <li onclick="payList()"><p>还款列表<span>查看详情</span></p></li>
	</ul>

<!--\ 产品信息切换页结束\-->
	<!--\ 借款详情开始\-->
		<div class="l_financinBox" id="l_financinBox">
			<div class="l_minHeight">
		        <p class="l_infoTitle">标的信息</p>
		        <ul>
		            <li>借款用途：<span><customUI:dictionaryTable constantTypeCode="loanUseage" desc="true" key="${loanApplicationListVO.loanUseage}"/></span></li>
		            <li>用途描述：<span>${loanApplicationListVO.useageDesc}</span></li>
		        </ul>
		        <p class="l_infoTitle">借款人基本信息</p>
		        <ul>
		            <li>性别：<span><customUI:dictionaryTable constantTypeCode="sex" desc="true"  key="${basicSnapshot.sex}"/></span></li>
		            <li>婚姻状况：
		            	<span>
							<customUI:dictionaryTable constantTypeCode="isMarried" desc="true" key="${basicSnapshot.isMarried}"/>
	        				<c:if test="${not empty basicSnapshot.isMarried&&not empty basicSnapshot.childStatus }">/</c:if>
	        				<customUI:dictionaryTable  desc="true"  constantTypeCode="childStatus"  key="${basicSnapshot.childStatus}"/>
						</span>
		            </li>
		            <li>最高学历：<span><customUI:dictionaryTable  desc="true"  constantTypeCode="education"  key="${basicSnapshot.education}"/></span></li>
		            <li>信用卡额度：<span><fmt:formatNumber value="${basicSnapshot.maxCreditValue}" pattern="#,##0.00"/>元</span></li>
		            <li>月均收入：<span><fmt:formatNumber value="${basicSnapshot.monthlyIncome}" pattern="#,##0.00"/>元</span></li>
		            <li>现居地：<span>${adress.provinceStr}${adress.cityStr}${adress.districtStr}${adress.detail}</span></li>
		        </ul>
		        <p class="l_infoTitle">认证报告</p>

		        <ul class="l_hasReport">
		        <h1>第三方合作机构实地考察，财富派严格复核</h1>
		            <c:forEach items="${authInfo}" var="auth" varStatus="stat">
	            		<li><span><customUI:dictionaryTable constantTypeCode="authReport" desc="true" key="${auth}"/></span></li>
	        		</c:forEach>
		        </ul>

		        <c:if test="${loanApplicationListVO.loanType ne '0'}">
		        	<p class="l_infoTitle">抵押信息</p>
		            <ul>
		                <li>抵押物类型：<span>
		               		<c:if test="${house.mortgageType eq '1'}">一抵</c:if>
		          			<c:if test="${house.mortgageType eq '2'}">二抵</c:if>
		          		</span></li>
		          		<li>总评估价：<span><fmt:formatNumber value="${loanPublish.assessValue}" pattern="#,##0.00"/> 万元</span></li>
		                <li>房屋地址：<span>${houseAdress.provinceStr}${houseAdress.cityStr}${houseAdress.districtStr}${houseAdress.detail}</span></li>
		                <li>市值：<span><fmt:formatNumber value="${loanPublish.marketValue}" pattern="#,##0.00"/> 万元</span></li>
		                <li>房屋面积：<span><fmt:formatNumber value="${loanPublish.hourseSize}" pattern="#,##0.00"/>平方米</span></li>
		                <li>抵押说明：<span>${loanPublish.hourseDesc}</span></li>
		            </ul>
	            </c:if>
			</div>
		        <div>
		           <img src="${ctx }/images/logo3.png" alt="">
		        </div>

	    </div>
	<!--\ 借款详情结束\-->

	<!--\ 项目证明开始\-->
	<div class="w_maskBox" id="w_maskBox">
	    <div class="swiper-container" style="width: 100%;height: 100%">
	        <div class="swiper-wrapper">
	        	<c:forEach items="${customerUploadSnapshots}" var="customersnapshot">
	        		<div class="swiper-slide"><img src="${picPath}${customersnapshot.attachment.url}" alt="${customersnapshot.attachment.fileName}"></div>
	        	</c:forEach>
	        </div>
	        <div class="swiper-page">
	            <p><span class="w_Numbers">1</span>/<span class="w_altogether"></span></p>
	        </div>
	    </div>
	</div>
	<!--\ 项目证明结束\-->

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
        <div class="offBtn" id="offBtn">关闭</div>
</div>



<script type="text/javascript">
var second=${second};
var rule=$("#publishRule").val();
$(function(){
	<c:if test="${loanApplicationListVO.begin eq'false'&&loanApplicationListVO.applicationState eq '3'}">
		timer(parseInt(${secondBetwween}));
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
	    $(".agreementss").show();
	    $("body").css("overflow","hidden");
        $("body").css("position","fixed");
	}
	function offProtocol(){
	    $("#masks").hide();
	    $("body").css({"overflow":"auto","position":"relative"});
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

    function payList(){
    	location.href=rootPath + "/finance/detail?loanApplicationNo="+$("#loanApplicationNo").val()+"&goType=6";
    }
</script>
<script src="${ctx }/js/FinancingProductinfo.js" type="text/javascript"></script>
<div class="masks" id="masks">
	<%@include file="../agreement/person0.jsp"%>
</div>
</body>
</html>