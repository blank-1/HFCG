<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%@include file="./common/taglibs.jsp"%>

<html>
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" /> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	<title>财富派-计算器</title>
	<%@include file="./common/common_js.jsp"%>
	<script type="text/javascript" src="${ctx }/js/person1.js"></script><!-- person1 javascript -->
</head>

<body>
	<input type="hidden" id="hidem" data-value="545247000" />
		<!-- line2 start -->
<%@include file="./common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine/>
<!-- navindex end -->
<!-- nvaline start -->
<div class="navline mt-0 wrapper clearFloat">
<!--<span> <a href="#">年化收益</a></span>><span><a href="#">借款期限</a></span>><span>信用等级</span> -->
</div>
<!-- nvaline end -->


	<!-- ftitle start -->
<div class="helptitle">
	<h2 class="clearFloat jsq-tab">
    	<ul class="tabul wrapper">
        	<li class=" <c:if test="${tab ne '2'}">action</c:if> zheicon" data-id="tab1">出借计算器</li>
        	<li data-id="tab2" class="hengicon <c:if test="${tab eq '2'}">action</c:if>">借款计算器</li>
        </ul>
    </h2>
</div><!-- ftitle end -->

<!-- finproject start -->
<div class="tab bortab wrapper <c:if test="${tab eq '2'}">display-none</c:if>" id="tab1" >
	
    <div class="bormoney">
    	<span class="firstb">出借金额</span>
    	<span class="second"><input type="text" class="ipt-borrow" onkeyup="value=value.replace(/[^\d.]/g,'')" id="pborrowm" value="<c:if test="${tab eq '1'}">${param.balance}</c:if>"/></span>
    	<span class="third">元</span>
    	<em class="c_red"></em>
    </div>
	<div class="bormoney">
    	<span class="firstb">出借利率</span>
    	<span class="four"><label><input type="radio" <c:if test="${ empty tab||tab eq '2'}">checked</c:if>  <c:if test="${tab eq '1'&& param.annualRate eq '7'}">checked</c:if> name="radio1" value="7"/>7%</label></span>
    	<span class="four"><label><input type="radio" <c:if test="${tab eq '1'&& param.annualRate eq '8'}">checked</c:if> name="radio1" value="8"/>8%</label></span>
    	<span class="four"><label><input type="radio" <c:if test="${tab eq '1'&& param.annualRate eq '9'}">checked</c:if> name="radio1" value="9"/>9%</label></span>
    	<span class="four"><label><input type="radio" <c:if test="${tab eq '1'&& param.annualRate eq '10'}">checked</c:if> name="radio1" value="10"/>10%</label></span>
    	<span class="four"><label><input type="radio" <c:if test="${tab eq '1'&& param.annualRate eq '11'}">checked</c:if> name="radio1" value="11"/>11%</label></span>
    	<span class="four"><label><input type="radio" <c:if test="${tab eq '1'&& param.annualRate eq '12'}">checked</c:if>  name="radio1" value="12"/>12%</label></span>
    </div>
	<div class="bormoney">
    	<span class="firstb">出借期限</span>
    	<span class="four"><label><input type="radio"  name="radio2" <c:if test="${ empty tab||tab eq '2'}">checked</c:if>  <c:if test="${tab eq '1'&& param.months eq '1'}">checked</c:if>  value="1"/>1期</label></span>
    	<span class="four"><label><input type="radio" name="radio2"  <c:if test="${tab eq '1'&& param.months eq '3'}">checked</c:if>  value="3"/>3期</label></span>
    	<span class="four"><label><input type="radio" name="radio2"  <c:if test="${tab eq '1'&& param.months eq '6'}">checked</c:if>  value="6"/>6期</label></span>
    	<span class="four"><label><input type="radio"  name="radio2"  <c:if test="${tab eq '1'&& param.months eq '9'}">checked</c:if>  value="9"/>9期</label></span>
    	<span class="four"><label><input type="radio"  name="radio2"  <c:if test="${tab eq '1'&& param.months eq '12'}">checked</c:if>  value="12"/>12期</label></span>
    </div>
    <div class="bormoney">
    	<span class="firstb">还款方式</span>
    	<span class="four"><label><input type="radio"  name="radio4" <c:if test="${ empty tab||tab eq '2'}">checked</c:if>  <c:if test="${tab eq '1'&& param.method eq '1'}">checked</c:if>  value="1"/>等额本金</label></span>
    	<span class="four"><label><input type="radio" name="radio4" <c:if test="${tab eq '1'&& param.method eq '2'}">checked</c:if>   value="2"/>等额本息</label></span>
    	<span class="four iphone4" style="width:30%"><label><input type="radio" <c:if test="${tab eq '1'&& param.method eq '3'}">checked</c:if> name="radio4"  value="3"/>周期付息到期还本</label></span>
    </div>
	<div class="borcenter mt-30">
    	<button class="btn  jsbtn" type="button" id="jsbtnLend">计算</button>
    </div>
      <!-- bordetail start -->
    <div class="bordetail display-none">
		<p class="tac">计算结果</p>
         <p><span class="bfirst">出借金额</span><span class="blast"><i id="balance1">100.00</i>元</span></p>
         <p><span class="bfirst">获得利息</span><span class="blast"><i id="interest1">100.00</i>元</span></p>
         <p><span class="bfirst">回款总额</span><span class="blast c_red"><i id="all1" class="c_red">100.00</i>元</span></p>
         <p class="tac user_bordetail_title">回款明细</p>
		<table cellpadding="0" cellspacing="0" border="0" class="bortable" id="jsbtnLendTab">
			
         </table>
    </div>
    <!-- bordetail end -->
    
</div>
<!-- finproject end -->

<!-- finproject start -->
<div class="tab bortab wrapper <c:if test="${tab ne '2'}">display-none</c:if>" id="tab2" >
    <div class="bormoney">
    	<span class="firstb">借款金额</span>
    	<span class="second"><input type="text" onkeyup="value=value.replace(/[^\d.]/g,'')" class="ipt-borrow" value="<c:if test="${tab eq '2'}">${param.balance}</c:if>" id="pbloan_money"/></span>
    	<span class="third">元</span>
    	<em class="c_red ml-100"></em>
    </div>
    <div class="bormoney">
    	<span class="firstb">借款利率</span>
    	<span class="four"><label><input type="radio" <c:if test="${ empty tab||tab eq '1'}">checked</c:if>  <c:if test="${tab eq '2'&& param.annualRate eq '7'}">checked</c:if> name="radio6" value="7"/>7%</label></span>
    	<span class="four"><label><input type="radio"  <c:if test="${tab eq '2'&& param.annualRate eq '8'}">checked</c:if> name="radio6" value="8"/>8%</label></span>
    	<span class="four"><label><input type="radio"  <c:if test="${tab eq '2'&& param.annualRate eq '9'}">checked</c:if> name="radio6" value="9"/>9%</label></span>
    	<span class="four"><label><input type="radio"  <c:if test="${tab eq '2'&& param.annualRate eq '10'}">checked</c:if> name="radio6" value="10"/>10%</label></span>
    	<span class="four"><label><input type="radio"  <c:if test="${tab eq '2'&& param.annualRate eq '11'}">checked</c:if> name="radio6" value="11"/>11%</label></span>
    	<span class="four"><label><input type="radio"  <c:if test="${tab eq '2'&& param.annualRate eq '12'}">checked</c:if> name="radio6" value="12"/>12%</label></span>
    </div>
	<div class="bormoney">
    	<span class="firstb">借款期限</span>
    	<span class="four"><label><input type="radio" name="radio3" <c:if test="${ empty tab||tab eq '1'}">checked</c:if>  <c:if test="${tab eq '2'&& param.months eq '1'}">checked</c:if> value="1"/>1期</label></span>
    	<span class="four"><label><input type="radio" name="radio3" <c:if test="${tab eq '2'&& param.months eq '3'}">checked</c:if> value="3"/>3期</label></span>
    	<span class="four"><label><input type="radio" name="radio3" <c:if test="${tab eq '2'&& param.months eq '6'}">checked</c:if> value="6"/>6期</label></span>
    	<span class="four"><label><input type="radio" name="radio3" <c:if test="${tab eq '2'&& param.months eq '9'}">checked</c:if> value="9"/>9期</label></span>
    	<span class="four"><label><input type="radio" name="radio3" <c:if test="${tab eq '2'&& param.months eq '12'}">checked</c:if> value="12"/>12期</label></span>
    </div>
    <div class="bormoney">
    	<span class="firstb">还款方式</span>
    	<span class="four"><label><input type="radio"  name="radio5" <c:if test="${ empty tab||tab eq '1'}">checked</c:if>  <c:if test="${tab eq '2'&& param.method eq '1'}">checked</c:if> value="1"/>等额本金</label></span>
    	<span class="four"><label><input type="radio" name="radio5" <c:if test="${tab eq '2'&& param.method eq '2'}">checked</c:if> value="2"/>等额本息</label></span>
    	<span class="four iphone4" style="width:30%"><label><input type="radio" <c:if test="${tab eq '2'&& param.method eq '3'}">checked</c:if> name="radio5"  value="3"/>周期付息到期还本</label></span>

    </div>
	<div class="borcenter mt-30">
    	<button class="btn btn-error jsbtn" type="button" id="jsbtnLoan">计算</button>
    </div>
    
    <!-- bordetail start -->
    <div class="bordetail display-none">
    	<p class="tac">计算结果</p>
        <p class="clearFloat"><span class="bfirst">借款金额</span><span class="blast"><i id="loan_balance1">100.00</i>元</span></p>
        <p class="clearFloat"><span class="bfirst">借款总成本（利息）</span><span class="blast"><i id="fee1">100.00</i>元</span></p>
        <p class="clearFloat"><span class="bfirst">还款总额</span><span class="blast c_red"><i id="allBalance1" class="c_red">100.00</i>元</span></p>
        <p class="tac">还款明细</p>
        <table cellpadding="0" cellspacing="0" border="0" class="bortable" id="jsbtnLoanTab">
        </table>
    </div><!-- bordetail end -->
</div>
<div class="clear_100"></div>
<!-- finproject end -->

	<script type="text/javascript">
		$(function(){

			<c:if test="${not empty tab && tab eq '1'}">
				$("#jsbtnLend").click();
			</c:if>

			<c:if test="${not empty tab && tab eq '2'}">
				$("#jsbtnLoan").click();
			</c:if>
		});

	</script>
<!-- finproject end -->
<div style="height:120px;clear:both;"></div>
<!-- masklayer start  -->
<%@include file="./login/login.jsp"%>
<!-- masklayer end -->
<!-- footerindex start -->
<%@include file="./common/footLine3.jsp"%>
<!-- fbottom end -->
</body>
</html>

