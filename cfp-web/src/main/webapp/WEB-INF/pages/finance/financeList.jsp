<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="keywords" content="" />
    <meta name="description" content="" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">

	<title>我要理财－出借列表</title>
	<%@include file="../common/common_js.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/s_shengxin.css" />
	<script type="text/javascript" src="${ctx}/js/login.js"></script><!-- login javascript -->
	<script type="text/javascript" src="${ctx}/js/financeList.js"></script><!-- financeList javascript -->
    <script type="text/javascript" src="${ctx}/js/jquery_page.js"></script><!-- jquery_page javascript 分页 -->
    <script type="text/javascript" src="${ctx}/js/fDData.js"></script><!-- jquery_page javascript 分页 -->

<style>
	.finance_zw_img{height:170px;line-height:20px;text-align:center;background:#F9FAFB;margin-bottom:30px;}
	.finance_zw_img img{margin-top:30px;}
	.finance_zw_img h2{font-size:16px;color:#999;line-height:22px;}
</style>
<script>
	function setWinName1(){
	  window.name="page1";
	}
	function setWinName2(){
	  window.name="page2";
	}
	function setWinName3(){
	  window.name="page3";
	}
	function resetWN(){
	  window.name = "";
	}
</script>

</head>
<%//默认显示投标tab
	String tab = (String)request.getParameter("tab");
	if(tab==null||tab==""){
		request.setAttribute("tab","heng");
	}
%>
<body id="financelist" class="">
<input type="hidden" id="hidem" data-value="${userAccount.availValue2 }" />
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="2"/>
<!-- navindex end -->
<%-- <!-- ftitle start -->
<div class="ftitle">
	<h2 class="clearFloat">
    	<ul class="tabul wrapper">
        	<li class="${tab=='zhe'?'action':''} zheicon" data-id="tab1">省心计划<i></i></li>
        	<li data-id="tab2" class="${tab=='heng'?'action':''} hengicon">出借列表<i></i></li>
        	<li class="${tab=='rithts'?'action':''}" data-id="tab3">债权转让<i></i></li>
        </ul>
    </h2>
</div><!-- ftitle end -->
 --%>
<!-- th-pro-title start -->
<div class="th-pro-title wylc">
    <ul class="pro-group container clearFloat">
        <li data-id="finance-project" class="pro-li1 ${tab=='zhe'?'th-action':''}">省心计划</li>
        <li data-id="finance-list" class="pro-li2  ${tab=='heng'?'th-action':''} ">出借列表</li>
        <li data-id="turning-creditrights" class=" pro-li3 ${tab=='rithts'?'th-action':''} "  >债权转让<i></i></li>
    </ul>
</div>
<!-- th-pro-title end -->


<!-- container start finance-list -->
<div class="container clearFloat tab ${tab!='zhe'?'display-none':''} " id="finance-project">
    <!-- th-nav start -->
    <ul class="th-nav clearFloat">
        <li><a href="${ctx}/" class="upp">财富派首页</a>></li>
        <li><a href="#" class="upp">我要理财</a>></li>
        <li><a href="javascript:;" class="current">省心计划</a></li>
    </ul>
    <div class="s_shengxin_banner">

    </div>
    <!-- th-nav end -->
    <!-- th-fince start -->
    <%-- <div class="th-fince clearFloat">
        <!-- th-choose start -->
        <div class="th-choose-list">
            <div class="finance-ul-group clearFloat">
                <span class="span-head">出借列表</span>
                <input type="hidden" id="financeLendList" name="conditions" />
                <ul class="lend-list clearFloat">
                    <li class="li-context action"  data-id="0">全部</li>
                    <li class="li-context"  data-id="3" >到期付本息</li>
                    <li class="li-context"  data-id="2">到期付本周期付息</li>
                </ul>
            </div>
            <div class="finance-ul-group finance-midden clearFloat">
                <span class="span-head">借款期限</span>
                  <input type="hidden" id="financeduringList" name="conditions" />
                <ul class="loan-list clearFloat">
                    <li class="li-context action" data-id="0">全部</li>
                    <li class="li-context"  data-id="1">6个月以下</li>
                    <li class="li-context"  data-id="2">6-12个月</li>
                    <li class="li-context"  data-id="3">12个月以上</li>
                </ul>
            </div>
            <div class="finance-ul-group finance-last clearFloat">
                <input type="hidden" id="financeGuessList" name="conditions" />
                <span class="span-head income-title">预期收益</span>
                <ul class="income-list clearFloat">
                    <li class="li-context action">全部</li>
                     <c:forEach items="${lendRateTypes}" var="lendRateType" >
                        <li data-id="${lendRateType}" class="li-context">${lendRateType}%</li>
                     </c:forEach>
                </ul>
            </div>
        </div>
        <!-- th-choose end -->

        <!-- th-fince-img start -->
        <div class="th-fince-img col-md-hidden"><img src="../images/finance_list/list_03.png" alt=""></div>
        <!-- th-fince-img end -->
    </div> --%>
    <!-- th-fince END -->
    <div class="clear"></div>
     <!-- th-sort start -->
<!-- 	<div class="th-sort clearFloat">
		<a href="javascript:;" class="a-head">排序：</a>
		<a href="javascript:;" class="a-midden a-midden-up">默认排序</a>
		  <a href="javascript:;" class="a-midden a-sort annRateOrder"  id="annRateOrder" data-id="2" >预期收益</a>
        <a href="javascript:;" class="a-midden a-sort durationOrder"   id="durationOrder" data-id="2">借款期限</a>
		<a href="javascript:;" class="a-midden">信用等级</a>
	</div> -->
	<!-- <div class="th-sort clearFloat finance">
		<a href="javascript:;" class="a-head">排序：</a>
        <a href="javascript:;" class="a-midden " onclick="defaultSortOrder_finance(this);" data-id="0">默认排序</a>
        <a href="javascript:;" class="a-midden a-sort annRateOrder "  id="annRateOrderFinance" data-id="" >预期收益</a>
     	<a href="javascript:;" class="a-midden a-sort durationOrder"   id="durationOrderFinance" data-id="">借款期限</a>
	</div> -->
	<!-- th-sort end -->
    <!-- th-finance-list-group start -->
    <div class="s_shengxin_main" id="financeProject">
    </div>
    <!-- th-finance-list-group end  -->
    <div style="clear:both"></div>
   <div class="tcdPageCode mt-20" id="pageListFinance">
   </div>
	<div class="clear_50"></div>
    <!-- th-finance-list-group end  -->
    <div class="wrapper" id="pageList">
	    <div class="tcdPageCode mt-20" id=""></div>
    </div>
</div>
<!-- container end -->


<!-- finance-project start-->
<%-- <div class="container  tab ${tab=='heng'?'display-none':''}" " id="finance-project">
      <!-- tab start -->
    <div class="tab finproject " id="tab1" >
    	<!-- fdheng start -->
    	<div class="fdheng">
        	<!-- fdif start -->
        	<div class="fdif wrapper clearFloat">
            	<span class="firstsp fdsp">付息类型</span>
                <!-- select start -->
		        <dl class="select" class="zindex" id="Ftype">
		                            <dt data-id="0">全部</dt>
		                            <dd>
		                               <ul>
		                               	   <li data-id="0"><a href="javascript:">全部</a></li>
		                               	   <c:forEach items="${interestReturnTypes }" var="temp">
			                               	   <li data-id="${temp }">
			                               	   <a href="javascript:">
			                               	   <c:if test="${temp eq '1'}">周期付息</c:if>
			                               	   <c:if test="${temp eq '2'}">到期付息</c:if>
			                               	   </a>
			                               	   </li>
		                               	   </c:forEach>
		                               </ul>
		                            </dd>
		        </dl><!-- select end -->
            	<span class="fdsp">封闭期</span>

								<!-- select start -->
		                        <dl class="select" class="zindex" id="cloperiod">
		                            <dt data-id="0">全部</dt>
		                            <dd>
		                               <ul>
		                               	   <li data-id="0"><a href="javascript:">全部</a></li>
		                               	   <c:forEach items="${closingTypes }" var="temp">
		                               	   	<li data-id="${temp }"><a href="javascript:">${temp }个月</a></li>
		                               	   </c:forEach>
		                               </ul>
		                            </dd>
		                        </dl><!-- select end -->
            	<span class="fdsp">预期收益</span>

								<!-- select start -->
		                        <dl class="select mr-5" id="expectpay">
		                            <dt data-id="0">全部</dt>
		                            <dd>
		                               <ul>
		                               	   <li data-id="0"><a href="javascript:">全部</a></li>
		                               	   <c:forEach items="${rateTypes }" var="temp">
		                               	   	<li data-id="${temp }"><a href="javascript:">${temp }%</a></li>
		                               	   </c:forEach>
		                               </ul>
		                            </dd>
		                        </dl><!-- select end -->
                  <button type="button" id="selectbtn" class="btn btn-blue mt-0 widselect" class="selectbtn">查询</button>
            </div><!-- fdif end -->
            <!-- pai start -->
            <div class="pai wrapper clearFloat">
            	<span class="firstsp tal">排序：</span>
                <div class="uldiv">
                    <a href="javascript:" data-value="0" data-id="yqsort" class="xiala">预期收益</a>
                </div>
                <div class="uldiv">
               	 <a href="javascript:" data-value="0" data-id="fbsort" class="xiala xiala2">封闭期</a>
                </div>

            </div><!-- pai end -->
        </div><!-- fdheng start -->

        <div class="wrapper" id="financeProject"></div>
    </div><!-- tab end -->
</div> --%>
<!-- finance-project start-->

<!-- container start finance-list -->
<div class="container clearFloat tab ${tab!='heng'?'display-none':''}" id="finance-list">
    <!-- th-nav start -->
    <ul class="th-nav clearFloat">
        <li><a href="${ctx}/" class="upp">财富派首页</a>></li>
        <li><a href="#" class="upp">我要理财</a>></li>
        <li><a href="javascript:;" class="current">出借列表</a></li>
       <!--  <li><a href="javascript:;" class="current">房贷</a></li> -->
    </ul>
    <!-- th-nav end -->

    <!-- th-fince start -->
    <div class="th-fince clearFloat">
        <!-- th-choose start -->
        <div class="th-choose-list">
            <div class="finance-ul-group clearFloat">
                <span class="span-head">借款类型</span>
                <input type="hidden" id="loanType-lend" name="conditions"   />
                <ul class="lend-list clearFloat">
                    <li class="li-context action" data-id="-1" >全部</li>
                   <!--  <li class="li-context">信贷</li>
                    <li class="li-context">房贷</li>
                    <li class="li-context">企业车贷</li>
                    <li class="li-context">企业信贷</li>
                    <li class="li-context">企业保理</li>
                    <li class="li-context">基金</li> -->
				    <%-- <c:forEach items="${loanTypes}" var="loanType" >
                        <li class="li-context" data-id="${loanType.value}">${loanType.desc}</li>
                    </c:forEach> --%>
					<li class="li-context" data-id="0">信贷</li>
					<li class="li-context" data-id="1">房贷</li>
					<li class="li-context" data-id="3">企业信贷</li>
                    <li class="li-context" data-id="8">车贷</li>
                    <li class="li-context" data-id="9">闪盈计划</li>
                </ul>
            </div>
            <div class="finance-ul-group finance-midden clearFloat">
                <span class="span-head">借款期限</span>
                <input type="hidden" id="durationType-lend"  name="conditions"  value="" data-id="" />
                <ul class="loan-list clearFloat">
                    <li class="li-context action" data-id="-1">全部</li>
                    <li class="li-context" data-id="0" >6个月以下</li>
                    <li class="li-context" data-id="1" >6-12个月</li>
                    <li class="li-context" data-id="2" >12个月以上</li>
                </ul>
            </div>
            <div class="finance-ul-group finance-last clearFloat">
                <span class="span-head income-title">预期收益</span>
                <input type="hidden" id="loanRateType-lend" value=""  name="conditions"  data-id="" />
                <ul class="income-list clearFloat">
                     <li data-id="-1" class="li-context action" >全部</li>
                     <c:forEach items="${loanRateTypes}" var="loanRateType" >
                        <li data-id="${loanRateType}" class="li-context">${loanRateType}%</li>
                     </c:forEach>
                    <!-- <li class="li-context">8%</li>
                    <li class="li-context action">全部</li>
                    <li class="li-context">9%</li>
                    <li class="li-context">10%</li>
                    <li class="li-context">11%</li>
                    <li class="li-context">12%</li> -->
                </ul>
            </div>
        </div>
        <!-- th-choose end -->

        <!-- th-fince-img start -->
			<div class="th-fince-img col-md-hidden"  >
				<%-- 	<img src="${ctx }/images/Veteran.jpg" alt=""> --%>
		  		<div class="new-fince-img">
					<img src="${ctx}/images/old_banner.jpg" alt=""> <img
						src="${ctx}/images/new_icon.png" alt="" class="font"> <img
						src="${ctx}/images/Angle.png" alt="" class="Angle">
				</div>
				<div class="old-fince-img">
					<img src="${ctx}/images/new_banner.jpg" alt=""> <img
						src="${ctx}/images/old_icon.png" alt="" class="font"> <img
						src="${ctx}/images/Angle.png" alt="" class="Angle">
				</div>
			</div>
			<!-- th-fince-img end -->
    </div>
    <!-- th-fince END -->
    <div class="clear"></div>
    <!-- th-sort start -->
    <div class="th-sort clearFloat loanlist">
        <a href="javascript:;" class="a-head">排序：</a>
        <a href="javascript:;" class="a-midden " onclick="defaultSortOrder(this);" data-id="0">默认排序</a>
        <a href="javascript:;" class="a-midden a-sort annRateOrder"  id="annRateOrder" data-id="2" >预期收益</a>
        <a href="javascript:;" class="a-midden a-sort durationOrder"   id="durationOrder" data-id="2">借款期限</a>
        <!-- <a href="javascript:;" class="a-midden">信用等级</a> -->
    </div>
    <!-- th-sort end -->

    <!-- th-finance-list-group start -->
    <div class="th-finance-list-group flcontext2" id="loanlist-flcontext2" >
        <ul class="finance-title clearFloat">
            <li class="li-01">项目名称</li>
            <li class="li-02">年化利率</li>
            <li class="li-03">借款期限</li>
            <li class="li-04">借款金额</li>
            <li class="li-05">借款进度</li>
            <li class="li-06">&nbsp;</li>
        </ul>
        <ul class="finance-list clearFloat">
        </ul>
    </div>
    <!-- <div class="flcontext2 wrapper"></div> -->
    <!-- th-finance-list-group end  -->
    <div class="wrapper" id="pageList">
	    <div class="tcdPageCode mt-20" id="lend_page"></div>
    </div>
</div>
<!-- container end -->


<!-- container start turning-creditrights -->
<div class="container clearFloat tab ${tab!='rithts'?'display-none':''}" id="turning-creditrights">
    <!-- th-nav start -->
    <ul class="th-nav clearFloat">
        <li><a href="${ctx}/" class="upp">财富派首页</a>></li>
        <li><a href="#" class="upp">我要理财</a>></li>
        <li><a href="javascript:;" class="current">债权转让</a></li>
       <!--  <li><a href="javascript:;" class="current">房贷</a></li> -->
    </ul>
    <!-- th-nav end -->

    <!-- th-fince start -->
    <div class="th-fince clearFloat">
        <!-- th-choose start -->
        <div class="th-choose-list">
            <div class="finance-ul-group clearFloat">
                <span class="span-head">借款类型</span>
                <input type="hidden" id="rightsType" name="conditions" data-id="-1" />
                <ul class="lend-list clearFloat">
                    <li class="li-context action" data-id="-1" >全部</li>
                   <%--  <c:forEach items="${loanTypes}" var="loanType" >
                        <li class="li-context" data-id="${loanType.value}">${loanType.desc}</li>
                    </c:forEach> --%>
                    <li class="li-context" data-id="0">信贷</li>
					<li class="li-context" data-id="1">房贷</li>
                    <li class="li-context" data-id="3">企业信贷</li>
                    <li class="li-context" data-id="8">车贷</li>
                </ul>
            </div>
            <div class="finance-ul-group finance-midden clearFloat">
                <span class="span-head">借款期限</span>
                <input type="hidden" id="rightsDurationType" name="conditions" data-id="-1" />
                <ul class="loan-list clearFloat">
                    <li class="li-context action" data-id="-1" >全部</li>
                    <li class="li-context" data-id="0" >6个月以下</li>
                    <li class="li-context" data-id="1" >6-12个月</li>
                    <li class="li-context" data-id="2" >12个月以上</li>
                </ul>
            </div>
            <div class="finance-ul-group finance-last clearFloat">
                <span class="span-head income-title">预期收益</span>
                <input type="hidden" id="rightsRateType" name="conditions" data-id="-1" />
                <ul class="income-list clearFloat">
                    <li class="li-context action">全部</li>
                    <c:forEach items="${loanRateTypes}" var="loanRateType" >
                        <li data-id="${loanRateType}" class="li-context" >${loanRateType}%</li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <!-- th-choose end -->

        <!-- th-fince-img start -->
        <div class="th-fince-img col-md-hidden"><img src="${ctx }/images/finance_list/list_03.png" alt=""></div>
        <!-- th-fince-img end -->
    </div>
    <!-- th-fince END -->
    <div class="clear"></div>
    <!-- th-sort start -->
    <div class="th-sort clearFloat creditor">
        <a href="javascript:;" class="a-head">排序：</a>
        <a href="javascript:;" class="a-midden " onclick="defaultSortOrder_creditor(this);" data-id="0">默认排序</a>
        <a href="javascript:;" class="a-midden a-sort annRateOrder"  id="annRateOrder" data-id="2" >预期收益</a>
        <a href="javascript:;" class="a-midden a-sort durationOrder"   id="durationOrder" data-id="2">借款期限</a>
        <!-- <a href="javascript:;" class="a-midden">信用等级</a> -->
    </div>
    <!-- th-sort end -->

     <!-- th-finance-list-group start -->
   <div class="th-finance-list-group" id="rights-flcontext2">
<%--        <ul class="finance-title clearFloat">
            <li class="li-01" style="text-align:left;">
            <span style="display:inline-block; margin-left:50px;">借款标题</span></li>
            <li class="li-08">年化利率</li>
            <li class="li-03">剩余期限</li>
            <li class="li-04">剩余本金</li>
            <li class="li-04">转出价格</li>
            <li class="li-10">进度</li>
        </ul>
        <ul class="finance-list clearFloat">
            <li class="li-01"><a href="" title="房屋装修借款">房屋装修借款</a></li>
            <li class="li-08"><em>12</em>%</li>
            <li class="li-03"><em>1</em>个月</li>
            <li class="li-04"><em>1000</em>元</li>
            <li class="li-04"><em>1000</em>元</li>
            <li class="li-10">
                <div class="pro-group">
                    <div class="pro-schedule" style="width:60%"></div>
                </div>
                <span class="rate">60%</span>
            </li>
            <li class="li-06"><a href="javascript:;" class="btn btn-write">立即加入</a></li>
        </ul>--%>
    </div>
    <!-- th-finance-list-group end  -->
    <div class="wrapper" id="rightsList">

    </div>
</div>
<!-- container end -->



<!-- tab start -->
    <%-- <div class="tab finproject display-none" id="tab3">

    	<!-- fdheng start -->
    	<div class="fdheng fdheng2">
        	<!-- fdif start -->
        	<div class="fdif wrapper clearFloat">
            	<span class="firstsp fdsp">借款类型</span>

								<!-- select start -->
		                        <dl class="select" class="zindex">
		                            <dt id="rightsType"  data-id="-1">全部</dt>
		                            <dd>
		                               <ul>
										   <li data-id="-1"><a href="javascript:">全部</a></li>
                                           <c:forEach items="${loanTypes}" var="loanType" >
                                               <li data-id="${loanType.value}"><a href="javascript:">${loanType.desc}</a></li>
                                           </c:forEach>
		                               </ul>
		                            </dd>
		                        </dl><!-- select end -->
            	<span class="fdsp">借款期限</span>

								<!-- select start -->
		                        <dl class="select" class="zindex">
		                            <dt id="rightsDurationType" data-id="-1">全部</dt>
		                            <dd>
		                               <ul>
										   <li data-id="-1"><a href="javascript:">全部</a></li>
                                           <c:forEach items="${durationTypes}" var="durationType" >
                                               <li data-id="${durationType}"><a href="javascript:">${durationType}个月</a></li>
                                           </c:forEach>
		                               </ul>
		                            </dd>
		                        </dl><!-- select end -->
            	<span class="fdsp">预期收益</span>

								<!-- select start -->
		                        <dl class="select mr-5">
		                            <dt id="rightsRateType" data-id="-1">全部</dt>
		                            <dd>
		                               <ul>
										   <li data-id="-1"><a href="javascript:">全部</a></li>
                                           <c:forEach items="${loanRateTypes}" var="loanRateType" >
                                               <li data-id="${loanRateType}"><a href="javascript:">${loanRateType}%</a></li>
                                           </c:forEach>
		                               </ul>
		                            </dd>
		                        </dl><!-- select end -->
                  <button id="rightLoanQuery" type="button" class="btn btn-blue mt-0">查询</button>
            </div><!-- fdif end -->
            <!-- pai start -->
            <div class="pai wrapper clearFloat">
            	<span class="firstsp tal">排序：</span>
                <div class="uldiv">
                    <a href="javascript:" onclick="reOrder(this)" id="annRateOrder" class="xiala">预期收益</a>
                </div>
                <div class="uldiv">
               	 <a href="javascript:" onclick="reOrder(this)"  id="durationOrder" class="xiala xiala2">借款期限</a>
                </div>
                <div class="uldiv">
               	 <a href="javascript:" onclick="reOrder(this)"  id="creditRankOrder" class="xiala">信用等级</a>
                </div>
            </div><!-- pai end -->
        </div><!-- fdheng start -->
        <div class="fltitle mt-10">
            <ul class="dltitleul wrapper">
                <li class="sp1 tac">借款标题</li>
                <li class="sp2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年化利率</li>
                <li class="sp3">剩余期限</li>
                <li class="sp4">剩余金额</li>
                <li class="sp5">信用等级</li>
            </ul>
        </div>

        <div class="wrapper" id="rightsList">

        </div>
    </div> --%><!-- tab end -->


    <!-- tab start -->
    <%-- <div class="tab finproject ${tab=='zhe'?'display-none':''}" id="tab2">

    	<!-- fdheng start -->
    	<div class="fdheng fdheng2">
        	<!-- fdif start -->
        	<div class="fdif wrapper clearFloat">
            	<span class="firstsp fdsp">借款类型</span>

								<!-- select start -->
		                        <dl class="select" class="zindex">
		                            <dt id="loanType"  data-id="-1">全部</dt>
		                            <dd>
		                               <ul>
										   <li data-id="-1"><a href="javascript:">全部</a></li>
                                           <c:forEach items="${loanTypes}" var="loanType" >
                                               <li data-id="${loanType.value}"><a href="javascript:">${loanType.desc}</a></li>
                                           </c:forEach>
		                               </ul>
		                            </dd>
		                        </dl><!-- select end -->
            	<span class="fdsp">借款期限</span>

								<!-- select start -->
		                        <dl class="select" class="zindex">
		                            <dt id="durationType" data-id="-1">全部</dt>
		                            <dd>
		                               <ul>
										   <li data-id="-1"><a href="javascript:">全部</a></li>
                                           <c:forEach items="${durationTypes}" var="durationType" >
                                               <li data-id="${durationType}"><a href="javascript:">${durationType}个月</a></li>
                                           </c:forEach>
		                               </ul>
		                            </dd>
		                        </dl><!-- select end -->
            	<span class="fdsp">预期收益</span>

								<!-- select start -->
		                        <dl class="select mr-5">
		                            <dt id="loanRateType" data-id="-1">全部</dt>
		                            <dd>
		                               <ul>
										   <li data-id="-1"><a href="javascript:">全部</a></li>
                                           <c:forEach items="${loanRateTypes}" var="loanRateType" >
                                               <li data-id="${loanRateType}"><a href="javascript:">${loanRateType}%</a></li>
                                           </c:forEach>
		                               </ul>
		                            </dd>
		                        </dl><!-- select end -->
                  <button id="loanQuery" type="button" class="btn btn-blue mt-0">查询</button>
            </div><!-- fdif end -->
            <!-- pai start -->
            <div class="pai wrapper clearFloat">
            	<span class="firstsp tal">排序：</span>
                <div class="uldiv">
                    <a href="javascript:" onclick="reOrder(this)" id="annRateOrder" class="xiala">预期收益</a>
                </div>
                <div class="uldiv">
               	 <a href="javascript:" onclick="reOrder(this)"  id="durationOrder" class="xiala xiala2">借款期限</a>
                </div>
                <div class="uldiv">
               	 <a href="javascript:" onclick="reOrder(this)"  id="creditRankOrder" class="xiala">信用等级</a>
                </div>
            </div><!-- pai end -->
        </div><!-- fdheng start -->
        <div class="fltitle mt-10">

            <ul class="dltitleul wrapper">
                <li class="sp1 tac">借款标题</li>
                <li class="sp2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;年化利率</li>
                <li class="sp3">借款期限</li>
                <li class="sp4">借款金额</li>
                <li class="sp5">信用等级</li>
                <li class="sp6">借款进度</li>
            </ul>
        </div>
        <!-- flcontext2 start -->
        <div class="flcontext2 wrapper">


        </div><!-- flcontext2 end -->


        <div class="wrapper" id="pageList">

        </div>
    </div> --%><!-- tab end -->


<!-- masklayer start  -->
<div class="masklayer masklback" id="licai">
	<h2 class="clearFloat"><span>购买理财</span> <a href="javascript:" data-id="close"></a></h2>
             <!-- equity start -->
            <div class="flbuy">
            	<p class="clearFloat"><span class="fsp1">借款名称：</span>  <span class="fsp2" id="title"></span></p>
                <p class="clearFloat"><span class="fsp1">购买金额：</span>  <span class="fsp2"><big  class="c_red"><i id="buymoney">0</i></big>元</span></p>
                <p class="clearFloat"><span class="fsp1">预期收益：</span>  <span class="fsp2"><small id="expected" class="c_red">0</small>元</span></p>
                <p class="clearFloat"><span class="fsp1">账户余额：</span>  <span class="fsp2"><small id="account"></small>元</span></p>
                <form action="" method="post" name="form" id="finance_list_form" class=" mt-20">
                	<input type="hidden" name="lendProductPublishId" id="lendProductPublishId" value="" />
                	<input type="hidden" name="amount" id="amount" value="" />
                	<input type="hidden" name="token" id="token" value="" />
                	<input style="display:none" />
		        	<div class="input-group">
		            	<label for="jypassword">
		            		<span class="fsp1">交易密码：</span>
		                	<input type="password" value="" id="jypassword"  name="password" placeholder="请输入交易密码" class="ipt-input"  onfocus="if(this.value==defaultValue) {this.value='';this.type='password'}" onblur="if(!value) {value=defaultValue; this.type='text';}"/>
		                	<a href="#"  id="ed_ex_psdad" class="ml-20">找回交易密码</a>
		                </label>
		                <em class="ml-90"></em>
		            </div>
		        	<div class="input-group">
		            	<label for="checkBox">
		                	<input type="checkbox" name="checkBox" id="checkBox" checked class="mr-5 ml-90 ipt-check" /><a href="javascript:" class="ahui">《信通宝理财产品购买协议》</a>  <a class="ahui" href="javascript:">《用户委托理财产品协议》</a>
		                </label>
		                <em class=" ml-90"></em>
		            </div>
                    <div class="groupbtn clearFloat">
                        <label>
                            <button type="button" class="btn btn-error ml-90 mt-0" id="repay">确认支付</button>
                        </label>
                        <label>
                            <button type="button" class="btn btn-write ml-20 mt-0" id="payrepay">使用其它方式支付</button>
                        </label>
                    </div>
                </form>
                <input type="hidden" id="flagCustomer" value="${flagCustomer}" />
            </div><!-- equity start -->

</div><!-- masklayer end -->

<%@include file="../login/login.jsp"%>
<%@include file="../person/authenticationShenfen.jsp"%>
<!-- 返回顶部 -->
<%--<div class="Return_top">
	<div id="back-to-top">
		<dl>
			<!-- <dd><a href="javascript:;"><img src="${ctx}/images/Return_top1.jpg" /></a></dd> -->
			<dd id="top"><a href=""><img src="${ctx}/images/Return_top3.jpg" /></a></dd>
		</dl>
	</div>
</div>--%>
<!-- 返回顶部 end-->
</body>
<script>
//返回顶部
/*$(function () {
	$(window).scroll(function(){
		if ($(window).scrollTop()>100){
			$("#top").fadeIn(500);
		}
		else{
			$("#top").fadeOut(500);
		}
	});
	//当点击跳转链接后，回到页面顶部位置
	$("#top").click(function(){
	$('body,html').animate({scrollTop:0},1000);
	return false;
	});
});*/
</script>
<script>
var jsonProduct = eval('${jsonProduct}');

/* var falg = true;

$(".col-md-hidden").on("click",function(){
	 var flag = $("#flagCustomer").val();
	 if(flag!=null&&flag!=""&&flag=="new"){
		   $(this).find("img").attr("src","${ctx}/images/Novice.jpg");
		   window.location.href="${ctx}/finance/list";
	 }else{
	       $(this).find("img").attr("src","${ctx}/images/Veteran.jpg");
		   window.location.href="${ctx}/finance/list?flag=new&cache="+Math.random();
	 }

}) */

var flag = $("#flagCustomer").val();
	if (flag != null && flag != "" && flag == "new") {
		$(".new-fince-img").animate({
			"opacity" : 0,
			"z-index" : "-1"
		}, 300)
		$(".old-fince-img").animate({
			"opacity" : 1,
			"z-index" : "1"
		}, 300)
	} else {
		$(".old-fince-img").animate({
			"opacity" : 0,
			"z-index" : "-1"
		}, 300)
		$(".new-fince-img").animate({
			"opacity" : 1,
			"z-index" : "1"
		}, 300)

	}

  	$(".old-fince-img").on(
			"click",
			function() {
				$(this).animate({
					"opacity" : 0,
					"z-index" : "-1"
				}, 300)
				window.location.href = "${ctx}/finance/list"
						;
				/* $(".new-fince-img").animate({
					"opacity" : 1,
					"z-index" : "1"
				}, 300) */
			})
	$(".new-fince-img").on("click", function() {
		$(this).animate({
			"opacity" : 0,
			"z-index" : "-1"
		}, 300)
		window.location.href = "${ctx}/finance/list?flag=new&cache="
			+ Math.random();
	/* 	$(".old-fince-img").animate({
			"opacity" : 1,
			"z-index" : "1"
		}, 300) */
	});
	$(function(){
    var Wname = window.name;
    if (Wname == "page1") {
    	$(".th-pro-title.wylc li").eq(0).click();//省心计划
        resetWN();
    }else if (Wname == "page2") {
    	$(".th-pro-title.wylc li").eq(1).click();//出借列表
      	resetWN();
    }else if(Wname == "page3"){
    	$(".th-pro-title.wylc li").eq(2).click();//债权转让
      	resetWN();
    }
  });
</script>



<!-- 找回交易密码-开始 -->
<script src="${ctx}/js/ed_ex_psd.js" type="text/javascript"></script>
<div class="zhezhao6"></div>
<div class="masklayer masklback" id="ed_ex_psd">
		<h2 class="clearFloat"><span>找回交易密码</span><a href="javascript:;" data-id="close"></a></h2>
		<div class="xiugai_phone_main" id="xigai1">
        	<img src="${ctx}/images/ed_ex_psd1.jpg" class="mt-30 mb-30"/>
			<form action="" class="form" method="post">
				<div class="input_box_phone">
					<label>
						<span>绑定的手机号码</span>
						<i class="tal ex-plone">${sessionScope.currentUser.encryptMobileNo}</i>
					</label>
					<em></em>
				</div>
				<div class="input_box_phone">
					<label>
						<span>手机验证码</span>
						<input type="text" id="ex_valid" value="" autocomplete="off" maxlength="6" placeholder="请输入验证码" style=""/>
						<button type="button" id="ex_get_valid" class="huoqu_yanzm" >获取验证码</button>
					</label>
					<em></em>
				</div>
				<div class="input_box_phone ipt_box_phone">
					<button type="button" id="next_sub1">下一步</button>
				</div>
			</form>
		</div>
		<div class="xiugai_phone_main display-none" id="phone_d2">
        	<img src="${ctx}/images/ed_ex_psd2.jpg" class="mt-30 mb-30"  />
			<form action="" class="form" method="post">
					<div class="input_box_phone input_box ipt-box-ex">
						<label>
							<span>输入新交易密码</span>
							<input type="password" class="width200" id="ed_ex_psd1" value="" maxlength="16" style="width:160px;" autocomplete="off" value="" onFocus="if(value==defaultValue){value=''}" onBlur="if(!value){value=defaultValue}" onKeyUp="ed_ex_Check(this.value)" />

								<div type="button" id="rejc_ex_psd" class="Tcolor floatLeft">无</div>
						</label>
						<em class="hui fontsize12">交易密码为6 -16 位字符，支持字母及数字,字母区分大小写</em>
					</div>
					<div class="input_box_phone">
						<label>
							<span>再次输入新交易密码</span>
							<input type="password" class="width200" value="" onFocus="if(value==defaultValue){value=''}" onBlur="if(!value){value=defaultValue}" id="ed_ex_psd2" value="" maxlength="16" autocomplete="off" />
						</label>
						<em></em>
					</div>
					<div class="input_box_phone ipt_box_phone">
						<button type="button" id="next_sub2">下一步</button>
					</div>
			</form>
		</div>
		<div class="xiugai_phone_main display-none" id="phone_d3">

        	<img src="${ctx}/images/ed_ex_psd3.jpg" class="mt-30 mb-30" />
			<p class="mt-50"><img src="${ctx}/images/img/true.jpg" /><span>交易密码重置成功！</span></p>

			<div class="input_box_phone ipt_box_phone" style="margin-top:85px;">
				<a href="javascript:;" id="ed_ex_psda"><button>确认</button></a>
			</div>
		</div>
</div>
<!-- 找回交易密码-结束 -->

<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->
</html>
