<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="../common/taglibs.jsp"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta name="keywords" content="" />
	<meta name="description" content="" /> 
	  <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	<title>我的省心计划 - 财富派</title>
	<%@include file="../common/common_js.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx }/css/s_shengxin.css" />
	<script type="text/javascript" src="${ctx }/js/jquery_page.js"></script><!-- jquery_page javascript 分页 -->
	<script type="text/javascript" src="${ctx }/js/jquery_page2.js"></script>
	
	<!-- 出借明细tab 数据加载 -->
	<script type="text/javascript" src="${ctx }/js/sxjh_lend_detail.js"></script>
	
	<!-- 资金流水tab 数据加载 -->
    <script type="text/javascript" src="${ctx }/js/sxjh_fund_detail.js"></script>
	
<script type="text/javascript">

//页面tab切换
$(function(){
    $(".list_tab>h6>span").click(
        function(){
            $(this).addClass("current").siblings().removeClass("current");
            $(".list_tab>div").eq($(".list_tab>h6>span").index(this)).show().siblings("div").hide();
            bottomB();
        }
    );
});

</script>
	
</head>
<body>
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- navindex start -->
	<customUI:headLine action="3"/>
<!-- navindex end -->

<input type="hidden" id="titleTab" value="1-1" />

<div class="clear"></div>
<!-- article start -->

<!--crumbs start-->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a  href="javascript:;">我的理财</a>></li>
        <li><a  href="javascript:;">省心计划</a>></li>
        <li><span>出借债权</span></li>
    </ul>
</div>
<!--crumbs end-->


<!-- container start -->
<div class="container clearFloat">
    <!-- pLeft start -->
    <div class="pLeft clearFloat">
    </div>
    <!-- pLeft end -->
    
    <!-- pRight top start -->
    <div class="pRight_sxCjzq">
        <div class="p-Right-sxCjzq-top">
            <p class="cjzq-Title">省心计划-出借债权
                <span>
                    <a href="downloadFinanceAgreement?lendOrderId=${lendOrderId }">《省心计划投资协议》<img src="${ctx}/images/personal/xiaZicon.png" /></a>
                </span>
            </p>
            <div class="sxCjzq_main">
            	
            	<!-- 账户ID -->
            	<input type="hidden" id="accId" value="${accId }"/>
            	<!-- 理财计划，出借订单ID -->
            	<input type="hidden" id="lendOrderId" value="${lendOrderId }"/>
            	
                <div class="sxCjzq_mainLeft">
                    <p class="biaoTi1">理财中金额（元）</p>
                    <p>
                        <span>—————</span>
                        <img src="../images/personal/sx_icon.png" style="vertical-align:middle;"/>
                        <span>—————</span>
                    </p>
                    <p class="biaoTi3"><fmt:formatNumber value="${financeAccountValue }" pattern="#,##0.00"/><font>元</font></p>
                    <p class="biaoTi2">（在投资金+在投预期收益）</p>
                </div>
                <div class="sxCjzq_mainRight">
                    <ul>
                        <li class="li-1">
                            
                                <span>预期年化收益率范围<font>${profitRate }-${profitRateMax }%</font></span>
                                <span>省心期（月）<font>${timeLimit }</font></span>
                            
                        </li>
                        <li class="li-5">
                            <span>
                                <p class="paycolor">还款方式</p>
                                <p>省心期结束且债权到期后还本付息</p>
                            </span>
                            <span>
                                <p class="paycolor">收益分配方式</p>
                                <c:if test="${loep.profitReturnConfig eq '0' }"><p>收益复利投资</p></c:if>
                                <c:if test="${loep.profitReturnConfig eq '1' }"><p>收益提取至可用余额</p></c:if>
                            </span>
                        </li>
                        <li class="li-2">
                            <span>在投资金预期收益（元）<font><fmt:formatNumber value="${expectProfit }" pattern="#,##0.00"/></font></span>
                            <span>已获收益（元）<font><fmt:formatNumber value="${currentProfit }" pattern="#,##0.00"/></font></span>
                        </li>
                        <li class="li-3">
                            <span>购买金额（元）<font><fmt:formatNumber value="${buyBalance }" pattern="#,##0.00"/></font></span>
                            <span>待出借金额（元）<font><fmt:formatNumber value="${availValue }" pattern="#,##0.00"/></font></span>
                        </li>
                        <li class="li-4">
                            <span>购买日期 <font><fmt:formatDate value="${buyTime}" pattern="yyyy-MM-dd" type="date"/></font></span>
                            <span>省心计划到期日期 <font><fmt:formatDate value="${agreementEndDate}" pattern="yyyy-MM-dd" type="date"/></font></span>
                        </li>
                    </ul>
                </div>
                <div style="clear:both;"></div>
            </div>
        </div>
    </div>
    <!-- pRight top end -->
    
    <!-- pRight down start -->
    <div class="list_tab listTabsx">
        <h6>
            <span class="current">出借明细</span>
            <span class="border-right-none">资金流水</span>
        </h6>
        <!-- 出借明细 开始 -->
        <div class="block" style="">
            <div class="p-Right-top Right-topSx" >
                <div class="pre_zt pre_ztSx">
	                <div class="th-sorts clearFloat">
						<span class="a-head">债权状态</span>
						<a href="javascript:;" name="zt" class="c a-head" value="">全部</a>
						<a href="javascript:;" name="zt" class="c" value="1">回款中</a>
						<a href="javascript:;" name="zt" class="c" value="2">回款逾期</a>
						<a href="javascript:;" name="zt" class="c" value="3">已结清</a>
					</div>
					<div class="th-sorts clearFloat">
						<span class="a-head">排&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;序</span>
						<a class="a-head zqsort mr" href="javascript:;" >默认</a>
						<a href="javascript:;" data-value="0" id="backDate"  class="zqsort a-midden ">回款日期</a>
						<a href="javascript:;" data-value="0" id="buyDate"  class="zqsort a-midden ">投标时间</a>
					</div>
                </div>
            </div>
            <div class="pre_zqlist">
                <ul class="pre_ul50">
                    <li class="pre_ul_170">债权名称</li>
                    <li class="pre_ul_80 cjzq_list_hide">借款人</li>
                    <li>出借金额(元)</li>
                    <li class="cjzq_list_hide2">待收回款(元)</li>
                    <li class="cjzq_list_hide">已回金额(元)</li>
                    <li>最近回款日</li>
                    <li class="cjzq_list_hide">债权状态</li>
                    <li class="cjzq_list_hide2">投标时间</li>
                    <li class="pre_ul_50 cjzq_list_hide">明细</li>
                </ul>
                <ul class="pre_ulbig" id="pre_ul_big">
                </ul>
                <div style="height:0px;clear:both;"></div>
            </div>
            <div class="tcdPageCode mt-20"></div>
        </div>
        <!-- 出借明细 结束 -->
        
        <!-- 资金流水 开始 -->
        <div class="div" style="border:none;">
            <div class="p-Right-top Right-topSx" >
                <div class="pre_zt pre_ztSx">
                    
	                <div class="th-sorts clearFloat">
						<span class="a-head">流水类型</span>
						<a class="a-head zjlstype" href="javascript:;">全部</a>
						<a href="javascript:;" value="1" class="zjlstype">收入</a>
						<a href="javascript:;" value="2"  class="zjlstype">支出</a>
						<a href="javascript:;" value="4" class="zjlstype">冻结</a>
						<a href="javascript:;" value="5" class="zjlstype">解冻</a>
					</div>
					<div class="th-sorts clearFloat">
						<span class="a-head">查询日期</span>
						<a class="a-head zjlstime" href="javascript:;">全部</a>
						<a href="javascript:;" value="t_7" class="zjlstime">近7天</a>
						<a href="javascript:;" value="t_1" class="zjlstime">近1月</a>
						<a href="javascript:;" value="t_6" class="zjlstime">近半年</a>
					</div>
				
                </div>
            </div>
            <div class="pre_zqlist mt-5">
                    <div id="zjlist">
                    </div>
            </div>
            <div class="tcdPageCode2 mt-20"></div>
        </div>
        <!-- 资金流水 结束 -->
        
    </div>
    <!-- pRight down end -->
    <div style="height:20px;clear:both;"></div>
</div>
<!-- container end -->


<!--fiance protocol-->
  <%@include file="../common/financeProtocol.jsp"%>
    <%@include file="../common/serverProtocol.jsp"%>
  <!-- fiance protocol end  -->

<div style="height:10px;clear:both;"></div>
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->
<script>
	
</script>
</body>
</html>