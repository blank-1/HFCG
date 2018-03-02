<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!doctype html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" /> 
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	<title>省心计划 - 财富派</title>
	<%@include file="../common/common_js.jsp"%>
	<link rel="stylesheet" href="${ctx}/css/page_sheng.css" type="text/css">
	<%-- <script type="text/javascript" src="${ctx}/js/financeDetail.js"></script> --%>
	<style>
		.flbuy span.fsp1{width:100px;}
		.shengXinbtn{width:300px;float:left;}
		.shengXinbtn span{padding:0px 14px;border:1px #EBECEE solid;position:relative;border-radius:3px;background:#EFF1F5;margin-right:10px;font-family:"微软雅黑";font-size:14px;color:#333;cursor:pointer;}
		.shengXinbtn .yixuan{border:1px #FDDCDD solid;background:url(../images/pay/yixuan_smallicon.png) no-repeat top right; box-shadow:0 5px 5px #FDDCDD; /*底边阴影*/}
		.shengxinTip{text-indent: 120px;font-size: 12px;color: #666;}
	</style>
</head>

<body>
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="2"/>
<!-- navindex end -->

<div class="clear"></div>
<!-- masklayer start  -->
<div class="masklayer masklback" id="cjd">
	<h2 class="clearFloat"><span>购买省心计划</span> <a href="javascript:" data-id="close"></a></h2>
             <!-- equity start -->
            <div class="flbuy l_flbuy ">
            	<p class="clearFloat"><span class="fsp1">借款名称：</span>  <span id="title">${financeDetail.productName}</span></p>
                <p class="clearFloat"><span class="fsp1">投标金额：</span>  <span><big  class="c_red"><i id="buymoney">0</i></big>元</span></p>
                <p class="clearFloat"><span class="fsp1">账户余额：</span>  <span><small id="account">${userAccount.availValue2}</small>元</span></p>
                
                <!--收益分配方式（开始）-->
				<div class="shengxinShouyi">
					<span class="fsp1">收益分配方式：</span>
					<!-- <div class="shengXinbtn"> -->
				        <span class="yixuan" data="1">收益提取至可用余额</span>
				        <!-- <span data="0">收益复利投资</span> -->
				    <!-- </div> -->
				    <div style="clear:both;"></div>
				    <!-- <div class="shengxinTip"><i style="color:#FF5655;vertical-align:middle;">*</i> 回款利息收益将进入可用余额，资金灵活</div> -->
				    <div style="clear:both;"></div>
			    </div>
			    <!--收益分配方式（结束）-->
			    
                <form action="" method="post" name="form" id="finance_detail_form" style="margin-top:5px;">
                	<input type="hidden" name="lendProductPublishId" id="lendProductPublishId" value="${financeDetail.lendProductPublishId}" />
                	<input type="hidden" name="amount" id="amount" value="" />
                	<input type="hidden" name="profitReturnConfig" id="profitReturnConfig" value="" /><!-- 省心计划收益分配方式 -->
                	<input type="hidden" name="token" id="token" value="${token }" />
                	<input style="display:none" />
		        	<div class="input-group">
		            	<label for="password">
		            		<span class="fsp1">交易密码：</span>
		                	<input type="password" value="" id="jypassword"  name="password" placeholder="请输入交易密码" class="ipt-input"  onfocus="if(this.value==defaultValue) {this.value='';this.type='password'}" onblur="if(!value) {value=defaultValue; this.type='text';}"/>
		                	<a href="#"  id="ed_ex_psdad" class="ml-20">找回交易密码</a>
		                </label>
		                <em class="ml-90"></em>
		            </div>
		            <!-- 注：这里设置隐藏不显示协议 -->
		        	<div class="input-group" style="display: none;">
		            	<label for="checkBox">
		                	<input type="checkbox" name="checkBox" id="checkBox" checked class="mr-5 ml-90 ipt-check" /><a href="javascript:" class="ahui l_xy_alert"  >《省心计划投资协议》</a> 
		                </label>
		                <em class=" ml-90"></em>
		            </div>
                    <div class="groupbtn clearFloat">
                        <label>
                            <button type="button" class="btn btn-error ml-90 mt-0" id="repay">确认支付</button>
                        </label>
                        <label>
                            <button type="button" class="btn btn-write ml-20 mt-0" id="payrepay">其它方式支付</button>
                        </label>
                    </div>
                </form>
                
            </div><!-- equity start -->
    
</div>
<!-- masklayer end -->

 
<div class="l_headerBkgd">
 	<input type="hidden" name="timeLimitType" id="timeLimitType" value="${financeDetail.timeLimitType}"/>
    <input type="hidden" name="timeLimit" id="timeLimit" value="${financeDetail.timeLimit}"/>
   	<input type="hidden" name="profitRate" id="profitRate" value="${financeDetail.profitRate}"/>
   	<input type="hidden" name="profitRateMax" id="profitRateMax" value="${financeDetail.profitRateMax}"/>
   	<input type="hidden" name="publishBalanceType" id="publishBalanceType" value="${financeDetail.publishBalanceType}"/>
	<div class="l_content">
		<ul class="l_mapLink">
			<li><a class="l_link" href="${ctx}/">财富派首页</a><!--[if lte IE 8]>> <![endif]--></li>
			<li><a class="l_link" href="${ctx}/finance/list">我要理财</a><!--[if lte IE 8]>> <![endif]--></li>
			<li><a class="lastList" href="javascript:;">省心计划详情</a></li>
		</ul>
		<div class="recommendL">
			<p class="l_title"><!--[if lte IE 8]><span class="l_icon"></span> <![endif]-->${financeDetail.publishName}</p>
			<c:if test="${financeDetail.publishBalanceType eq '2'}">
				<ul class="l_midInfo">
					<li class="l_midInfoL">
						<p><span>${financeDetail.profitRate}%</span>-<span>${financeDetail.profitRateMax}%</span></p>
						<p>预期年化收益范围</p>
					</li>
					<li class="l_midInfoM">
						<p>
							<span>${financeDetail.timeLimit}</span>
							<c:if test="${financeDetail.timeLimitType == '1'}">日</c:if>
							<c:if test="${financeDetail.timeLimitType == '2'}">个月</c:if>
						</p>
						<p>省心期</p>
					</li>
					<c:if test="${financeDetail.publishBalanceType == '2'}">
					<li class="l_midInfoR">
						<p>总额度</p>
						<p id="allmoney" data-allmoney="${financeDetail.publishBalance}">${financeDetail.publishBalance}元</p>
						<p>进度：<span id="perline"><i></i></span></p>
					</li>
					</c:if>
				</ul>
			</c:if>
			 <c:if test="${financeDetail.publishBalanceType eq '1'}">
				<ul class="l_midInfo l_midInfo2">
					<li class="l_midInfoL">
						<p><span>${financeDetail.profitRate}%</span>-<span>${financeDetail.profitRateMax}%</span></p>
						<p>预期年化收益范围</p>
					</li>
					<li class="l_midInfoM" style="margin-left: -14px;">
						<p>
							<span>${financeDetail.timeLimit}</span>
							<c:if test="${financeDetail.timeLimitType == '1'}">日</c:if>
							<c:if test="${financeDetail.timeLimitType == '2'}">个月</c:if>
						</p>
						<p style="margin-right:30px;">省心期</p>
					</li>
				</ul>
			</c:if>
			<ul class="l_lastInfo">
				<li class="l_lastInfo1">出借规则：<span>${financeDetail.startsAt}元起投,${financeDetail.upAt}元递增</span></li>
				<li class="l_lastInfo2"><!-- 加入上限：<span>10000元</span> --></li>
				<li class="l_lastInfo3">还款方式：<span>周期还利息,到期还本金</span></li>
				<li class="l_lastInfo4">省心方式：<span>自动投标,回款复投</span></li>
				<li class="l_lastInfo5">投资标的：<span>1-12月热门优先标的</span></li>
			</ul>
		</div>
		<div class="recommendR">
		<c:if test='${(financeDetail.publishBalanceType != 1 && financeDetail.availableBalance <= 0)|| financeDetail.publishState=="3" }'> 
		<b id="biao" class="biao biao7"></b>
		 </c:if> 
		 <!-- 已完结状态 -->
		<c:if test='${ financeDetail.publishState=="4"}'> 
		<b id="biao" class="biao biao8"></b>
		 </c:if> 
		 <!-- 预热状态 -->
		<c:if test='${financeDetail.publishState=="1"}'> 
		<b id="biao" class="biao biao9"></b>
		 </c:if> 
			<c:if test="${empty sessionScope.currentUser}">
				<p class="l_unLogin">账户余额：
					<span id="ye" data-ye="0"></span>
					<a href="javascript:void(0);" onclick="$('#login').slideDown(500);$('.zhezhao1').show();">登录</a>
					<span>查看</span>
				</p>
				<input type="hidden" id="yue_input" value="0">
			</c:if>
			<c:if test="${not empty sessionScope.currentUser}">
				<p class="l_ye">账户余额：
					<span id="ye" data-ye="${userAccount.availValue2}">
						<fmt:formatNumber value="${userAccount.availValue2}" pattern="#,##0.00"/>
						<input type="hidden" id="yue_input" value="${userAccount.availValue2}">
					元</span>
				</p>
			</c:if>
			<c:if test="${financeDetail.publishBalanceType != 1}">
				<p class="l_kyye clearfix" id="kyye" data-kyye="<c:if test="${financeDetail.publishBalanceType == 1}">0</c:if><c:if test="${financeDetail.publishBalanceType != 1}">${financeDetail.availableBalance}</c:if>">剩余可投额度：
						<span id="limited">${financeDetail.availableBalance}</span>
						<span id="jiliang">元</span>
				</p>
			</c:if>
			<input   <c:if test='${(financeDetail.publishBalanceType != 1 && financeDetail.availableBalance <= 0)|| financeDetail.publishState!="2"}'>readonly="readonly" disabled="disabled" </c:if> 
			class="l_moneyInput noNum limitNUM" id="moneyInput" type="text" style="cursor: text;" placeholder="请输入省心投金额"  
			<c:if test='${financeDetail.publishBalanceType == 1 || financeDetail.availableBalance > 0}'>oninput="shengCheck.notNUM(11,$(this).val())" onfocus="shengCheck.focusCkeck()" onblur="shengCheck.blurCheck($(this).val())" </c:if> >
			<p class="l_errTip"></p>
			<span class="l_yuan">元</span>
			<div style="width: 100%;"><i class="l_checkBTN l_checked"  <c:if test='${(financeDetail.publishBalanceType !=1 && financeDetail.availableBalance <= 0)|| financeDetail.publishState!="2"}'>readonly="readonly" disabled="disabled" </c:if> ></i><p class="l_xy" <c:if test='${(financeDetail.publishBalanceType != 1 && financeDetail.availableBalance <= 0)|| financeDetail.publishState!=2}'> disabled="disabled"  </c:if>>《省心计划投资协议》</p></div>
			<c:if test="${not empty sessionScope.currentUser}">
				<c:if test="${userExt.isVerified ne '1'}">
					<button class="l_subBtn <c:if test='${(financeDetail.publishBalanceType != 1 && financeDetail.availableBalance <= 0)|| financeDetail.publishState!=2}'>BTNDis</c:if>"
					 id="unpay"  <c:if test='${(financeDetail.publishBalanceType != 1 && financeDetail.availableBalance <= 0)|| financeDetail.publishState!=2}'> disabled="disabled"  </c:if>  >确定协议并投资</button>
				</c:if>
				<c:if test="${userExt.isVerified eq '1'}">
					<button class="l_subBtn <c:if test='${(financeDetail.publishBalanceType != 1 && financeDetail.availableBalance <= 0)|| financeDetail.publishState!=2}'>BTNDis</c:if>" 
					id=finance_detail_pay  <c:if test='${(financeDetail.publishBalanceType != 1 && financeDetail.availableBalance <= 0)|| financeDetail.publishState!=2}'> disabled="disabled"  </c:if>  >确定协议并投资</button>
				</c:if>
			</c:if>
			<c:if test="${empty sessionScope.currentUser}">
				<button class="l_subBtn  <c:if test='${(financeDetail.publishBalanceType != 1 && financeDetail.availableBalance <= 0)|| financeDetail.publishState!=2}'> BTNDis </c:if>  "
				 id="finance_detail_pay"  data-mask='mask' data-id="login"  <c:if test='${(financeDetail.publishBalanceType != 1 && financeDetail.availableBalance <= 0)|| financeDetail.publishState!=2}'> disabled="disabled"  </c:if>  >确定协议并投资</button>
			</c:if>
			<c:if test="${financeDetail.publishBalanceType != 1}">
			<button id="firstfinan" <c:if test='${(financeDetail.publishBalanceType != 1 && financeDetail.availableBalance <= 0) || financeDetail.publishState!="2"}'> disabled="disabled" </c:if> >最大可投</button>
			</c:if>
			<button id="allbalance" <c:if test='${(financeDetail.publishBalanceType != 1 && financeDetail.availableBalance <= 0)|| financeDetail.publishState!="2"}'>  disabled="disabled" </c:if> >全部余额</button>
			<p class="fTips"><!--[if lte IE 8]><span class="l_icon2">!</span> <![endif]-->市场有风险，投资需谨慎</p>
		</div>

	</div>
</div>

<!-- 内容选项页（开始） -->
<div class="list_tab">
    <h6>
        <span class="current">省心介绍</span>
        <span class="border-right-none">加入记录</span>
    </h6>
    <div class="block">
			<div class="l_indexMid">
				<div class="l_content l_contentMid">
					<div class="l_topC">
						<p class="l_title"><!--[if lte IE 8]><span class="l_icon"></span> <![endif]-->什么是省心计划？</p>			<!-- 改 -->
						<span>“省心计划”是财富派推出的自动投资工具，用户参与该计划可以按照事先确定的预期收益率自动投资财富派平台上的借款标的。通过自动投资所产生的每月回款（含本金和利息）将在省心期内继续自动投资，直至省心期结束。省心期结束后，省心计划将停止自动投标与回款复投。<b>特别说明：省心期结束日期不等于用户收取全部投资本金和收益的日期。用户投资标的到期后收回投资本金和相应的收益。</b></span>
					</div>
					<p class="l_title l_phone" style="margin-top: 10px; margin-bottom: -20px; position: relative; /* text-indent: 22px; */ font-size: 20px; color: #333; text-indent:20px; ">省心计划原理</p>
					<img class="l_pc" src="${ctx}/images/shengxin/pcPageSheng_img02.png" alt="">
					<img class="l_phone" src="${ctx}/images/shengxin/pcPageSheng_img03.png" alt="">
					<div class="l_buttomC">
						<p class="l_title"><!--[if lte IE 8]><span class="l_icon"></span> <![endif]-->常见问题</p>			<!-- 改 -->
						<ul class="l_QandA">
							<li class="l_margB">
								<p class="l_Q">省心计划安全吗？</p>
								<p>省心计划投资标的为平台优质标的，多重安全保障，风险准备金护航，至今平台0逾期。</p>
							</li>
							<li class="l_margB">
								<p class="l_Q">参与省心计划后，哪里可以看到我购买的债权和合同？</p>
								<p>您可以在【账户中心】-我的理财-省心计划中查看您所参与的省心计划所购买的债权以及合同。</p>
							</li>
							<li class="l_margB">
								<p class="l_Q">省心计划可以中途退出吗？</p>
								<p>省心计划开启后，系统将自动为您投资优质标的，为了保证您投资的收益与安全，无法中途暂停省心计划。所以请在购买前选择适合您的省心计划类型 。 </p>
							</li>
							<li>
								<p class="l_Q">为什么省心计划预期年化收益是范围值，而不是具体收益？</p>
								<p>省心计划帮您自动投标，免去资金闲置所造成的损失。财富派平台标的十分火热，为了保证您的资金持续出借，平台将自动帮您的资金出借到最新的标的中，由于多项出借标的预期年化收益不同，所以您的省心计划预期年化收益为范围值。</p>
							</li>
						</ul>
					</div>
				</div>
			</div>
    </div>
    <div class="div">
        <ul class="list-ul">
			<li class="list-ul-first">
				<ul>
					<li>投标人</li>
					<li>投标金额（元）</li>
					<li>投标日期</li>
				</ul>
			</li>
			
			<p id="tbtable" class="bid_information_tab2">

      		</p>
			
		</ul>
		<div class="tcdPageCode mt-20"></div>
    </div>
</div>
<!-- 内容选项页（结束） -->

<%@include file="../common/shengXinProtocol.jsp"%>
<%@include file="../common/serverProtocol.jsp"%>
<script type="text/javascript" src="${ctx}/js/jquery_page.js"></script>
<script type="text/javascript" src="${ctx}/js/sxjh_detail_lend_list.js"></script>
<script>
	$(function(){
		//收益分配方式按钮切换
		$(".shengXinbtn span").click(function(){
			$(this).addClass("yixuan").siblings("span").removeClass("yixuan");
			if($(".yixuan").html().length == 6){
				
				$(".shengxinTip").html("<i style='color:#FF5655;vertical-align: middle;'>*</i> 回款利息收益将会在省心期内自动循环投资，收益更高");
			}else{
				
				$(".shengxinTip").html("<i style='color:#FF5655;vertical-align: middle;'>*</i> 回款利息收益将进入可用余额，资金灵活");
			}
		})
	});
	$(function(){
	    $(".list_tab>h6>span").click(
	        function(){
	            $(this).addClass("current").siblings().removeClass("current");
	            $(".list_tab>div").eq($(".list_tab>h6>span").index(this)).show().siblings("div").hide();
	            bottomB();  
	        }
  
	    );
	});
	var shengCheck = {
		notNUM:function(num,val){
			var leg = val.length;
            if ((/[^0-9.]/g.test(val))||(/^\d+\.[0-9.]{3}/.test(val))) {
                 $(".limitNUM").val($(".limitNUM").val().substring(0,leg-1));
                 return false;
            }
            if(leg>num){
            	$(".limitNUM").val($(".limitNUM").val().substring(0,leg-1));
            	return false;
            }
		},
		errrTips:function(id,tex){
			$("#"+id).addClass("l_errTipInput");
			$(".l_errTip").text(tex).slideDown();
		},
		focusCkeck:function(){
			$(".l_errTip").removeClass("l_errTips").text("").slideUp();
			
		},
		blurCheck:function(val){
			if (val == "") {
				this.errrTips("moneyInput","请输入省心金额");
				return false;
			}else if (val<${financeDetail.startsAt}) {
				this.errrTips("moneyInput","输入大于起投的省心金额");
				return false;
			}else if (val%${financeDetail.upAt} != 0) {
				this.errrTips("moneyInput","请输入正确的省心金额");
				return false;
			}else{
				$(".l_errTipInput").each(function(){
					$(this).removeClass("l_errTipInput");
				});
				return true;
			}
			
		},
		agreementStle:function(){
			$(".l_checkBTN").on("click",function(e){
				e.preventDefault();
				e.stopPropagation();
				if($(this).attr("disabled") == "disabled"){
					return false;
				}
				$(".l_xy").removeClass("l_xyTip");
				$(this).toggleClass("l_checked");
	     	});
	     	$("#finance_detail_pay").on("click",function(){
	      		if (!$(".l_checkBTN").hasClass("l_checked")) {
	      			$(".l_xy").addClass("l_xyTip");
	      			return false;
	      		}
	      		if(${empty sessionScope.currentUser}){
	      			$('#login').slideDown(500);
	      			$('.zhezhao1').show();
	      			return false;
	      		}
	      		if(shengCheck.blurCheck($("#moneyInput").val())){
	      			if($(this).attr("data-id")=="login"){
	      				return false;
	      			}
	      			var b1=moneyf($("#moneyInput"));
	      			if( b1==""){
	      				massage='';
	      				var yue = $("#yue_input").val();
	      				var buy = $("#moneyInput").val();
	      				buy=parseInt(buy);
	      				//计算可用余额是否足够
	      				if((yue-buy)<0){
	      					$("#amount").val(buy);
	      					$("#finance_detail_form").attr("action", rootPath + "/finance/toBuyFinanceByPayAmount").submit();
	      				}else{
	      					$("#buymoney").html($("#moneyInput").val());
	      					$("#cjd").slideDown(500);
	      					$(".zhezhao1").show();
	      				}
	      			}
	      		}
	      	});
		},
		maxMoney:function(){
			if ($(".l_unLogin").is(":visible")) {
				$("#allbalance").attr("disabled","disabled");
			}
			if (!$(".l_kyye").is(":visible")) {
				$("#firstfinan").hide();
			}
			$("#allbalance").on("click",function(){
				$(".l_errTip").text("").slideUp();
				var money1=rmoney($("#ye").attr("data-ye"));
				$("#moneyInput").val(money1);
			})
			$("#firstfinan").on("click",function(){
				/* var money1=rmoney($("#limited").html());//剩余金额
				var limied=rmoney($("#limited").html());//限投
				var qitou=rmoney(${financeDetail.startsAt}+"");//起投金额
				var moneyipt=0;
				var ques=0;//判断是否提示
				if($("#jiliang").html()=="元"){
					moneyipt=Math.min(money1,limied)>=qitou?Math.min(money1,limied):0;
					moneyipt=money1>=qitou?money1:0;
					$("#moneyInput").val(moneyipt,2);
					$("#bay_ipt_money").val(moneyipt,2);
					
				} */
				$(".l_errTip").text("").slideUp();
				$("#moneyInput").val($("#kyye").attr("data-kyye")+".00");
			})
		},
		maskPro:function(){
			$(".l_xy,.l_xy_alert").on("click",function(){
				if($(this).attr("disabled") == "disabled"){
					return false;
				}
				$(".l_mask").show();
				$(".l_proContent").show();
				$("body").css("overflow","hidden");
			});
			$(".l_proContent a").on("click",function(){
				$(".l_mask").hide();
				$(".l_proContent").hide();
				$("body").css("overflow","auto");
			})
		},
		perLine:function(){
			var allmoney = $("#allmoney").attr("data-allmoney"),
				ye = $("#kyye").attr("data-kyye"),
				per = (allmoney - ye )/allmoney*100;
			$("#perline i").css("width",per+"%");
		},
	}
	$(function(){
		shengCheck.agreementStle();
		shengCheck.maxMoney();
		shengCheck.perLine();
		shengCheck.maskPro();
		
		$("#unpay").click(function(){
			$(".l_errTip").addClass("l_errTips").html("为了您的账户安全，请先进行<a data-id='shenfen' data-mask='mask' style='cursor: pointer;text-decoration:underline!important;'>身份验证</a>");
			shengCheck.errrTips("moneyInput");
		});
		
		$("#payrepay").click(function(){
			var buy=$("#moneyInput").val();
			buy=parseInt(buy);
			$("#amount").val(buy);
			$("#finance_detail_form").attr("action", rootPath + "/finance/toBuyFinanceByPayAmount").submit();
		});
		
		$("#repay").click(function(){
			var b1 = passwordf($("#jypassword"), "1");
			if (b1 == "") {
				if ($("#checkBox").is(':checked')) {
					massage = '';
					$("#checkBox").parent().next("em").html(massage);

					$("#amount").val($("#buymoney").html());
					
					// 省心计划收益分配方式
					$("#profitReturnConfig").val($(".yixuan").attr("data"));
					
					//理财
					$.ajax({
						url:rootPath+"/finance/checkBidLoanByAccountBalance",
						type:"post",
						data:{"bidPass":$("#jypassword").val()},
						success:function(data){
							var _data =  eval("("+data+")");
							if(!_data.isSuccess){
								result = false;
								$("#"+_data.id).addClass("ipt-error").parent().siblings("em").html(_data.info);
							}else{
								$("#finance_detail_form").attr("action", rootPath + "/finance/buyFinanceByAccountAmount").submit();
							}
						}
					});

				} else {
					massage = '请勾选购买协议前复选框！';
					$("#checkBox").parent().next("em").html(massage);
				}
			}
		});
	})	
	
	//判断交易密码
	function passwordf(passval1, pa) {

		if (passval1.val() == "")// 只处验证不能为空并且只能为英文或者数字或者下划线组成的２－１５个字符
		{
			if (pa == "0") {
				massage = "";
				passval1.removeClass("ipt-error").parent().siblings("em").html(massage);
			} else {
				massage = "请您输入交易密码！";
				passval1.addClass("ipt-error").parent().siblings("em").html(massage);
			}
		} else if (passval1.val().length < 6 || passval1.val().length > 16 || !(/^[0-9a-zA-Z]+$/.test(passval1.val()))) {
			massage = "交易密码错误";
			passval1.addClass("ipt-error").parent().siblings("em").html(massage);
		}else{
			massage = "";
			passval1.removeClass("ipt-error").parent().siblings("em").html(massage);
		}
		return massage;
	}
	
	function moneyf(mond) {
		var money1 = rmoney($("#limited").html());// 剩余金额
		var limied = rmoney($("#limited").html());// 限投
		var qitou = rmoney(${financeDetail.startsAt}+"");// 起投金额
		var mondmoney = 0;
		var flag = true;
		mondmoney = parseFloat(mond.val());
		
		//检查金额是否符合规则
		if (mond.val() == "" || !/^(([0-9]|([1-9][0-9]{0,9}))((\.[0-9]{0,9})?))$/.test(mondmoney) || (mond.val().length > 10)) {
			massage = "请输入正确的理财金额";
			flag = false;
		} else if (mondmoney < qitou) {
			massage = "请输入大于" + qitou + " 的金额！";
			flag = false;
		} else if (mondmoney < 100 || mondmoney % 100 != 0) {
			massage = "请输入大于100 并且是100 倍数的金额！";
			flag = false;
		}
		//金额与剩余金额、限投金额比较
		var publishBalanceType = $("#publishBalanceType").val();
		if(flag){
			if(publishBalanceType == 2){
				if (mondmoney > limied) {
					massage = "购买金额不能超过限投金额";
					flag = false;
				}
				if (mondmoney > money1) {
					massage = "购买金额不能超过剩余金额";
					flag = false;
				}
			}
			var moneyipt=0;
			if(publishBalanceType == 2){
				moneyipt=moneyipt=Math.min(money1,limied)>=qitou?Math.min(money1,limied):0;
				if((parseFloat(mond.val())-moneyipt)>0){
					massage="购买金额超出最大可投限额";
					flag = false;
				}
			}
		}
		
		if (flag) {
			massage = "";
			$(".l_errTipInput").each(function(){
				$(this).removeClass("l_errTipInput");
			});
		} else{
			shengCheck.errrTips("moneyInput",massage);
		}
		
		return massage;
	}
	if( !('placeholder' in document.createElement('input')) ){

	    $('input[placeholder],textarea[placeholder]').each(function(){
	      var that = $(this),
	      text= that.attr('placeholder');
	      if(that.val()===""){
	        that.val(text).addClass('placeholder');
	      }
	      that.focus(function(){
	        if(that.val()===text){
	          that.val("").removeClass('placeholder');
	        }
	      })
	      .blur(function(){
	        if(that.val()===""){
	          that.val(text).addClass('placeholder');
	        }
	      })
	      .closest('form').submit(function(){
	        if(that.val() === text){
	          that.val('');
	        }
	      });
	    });
	  }
</script>
 
<!-- login start -->
<%@include file="../login/login.jsp"%>
<%@include file="../person/authenticationShenfen.jsp"%>
<!-- login end -->
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->
  

<!-- 找回交易密码-开始 -->
<script src="${ctx}/js/ed_ex_psd.js" type="text/javascript"></script>
<div class="zhezhao6"></div>
<div class="masklayer masklback" id="ed_ex_psd">
		<h2 class="clearFloat"><span>找回交易密码</span><a href="javascript:" data-id="close"></a></h2>
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
				<a href="javascript:" id="ed_ex_psda"><button>确认</button></a>
			</div>
		</div>
</div>
<!-- 找回交易密码-结束 -->


</body>
</html>
