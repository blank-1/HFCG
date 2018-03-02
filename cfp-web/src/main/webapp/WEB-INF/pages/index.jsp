<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%@include file="./common/taglibs.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="财富派,投资理财,p2p投资,p2p理财,p2p网贷平台" />
	<meta name="description" content="财富派 - 国内领先的P2P网贷平台，为企业和个人投资者提供专业的P2P理财服务。并通过严格的风控审核，让您的投资理财变得更安全、简单、高效。财富派，为您个人理财保驾护航。" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
	<title>财富派 - 国内专业的P2P网贷投资理财服务平台</title>
	<%@include file="./common/common_js.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/index.css" /><!-- index css -->
	<script type="text/javascript" src="${ctx}/js/index_new/index.js"></script>
	<script type="text/javascript" src="${ctx}/js/index_new/jquery.SuperSlide.js"></script>
	<style type="text/css">
		.seoHidden{display: block;height: 0;width: 0;overflow: hidden;}
		
	</style>
	<style>
	.iJie1 {
		display: inline-block;
		float: left;
		height: 17px;
		width: 20px;
		background: url(../images/index_new/dxb.png) no-repeat;
		margin-top: 40px;
		margin-right: 5px;}
	</style>
</head>

<body class="position-rela">
<h1 class="seoHidden">财富派</h1>
<!-- line2 start -->

<div class="linktop">
	<div class="container clearFloat">
		<ul class="uWelcome clearFloat">
			<li class="lFirst linknone">欢迎来到财富派</li>
			<c:if test="${not empty sessionScope.currentUser }">
				<%--账户中心链接--%>
				<li><a href="${ctx}/person/account/overview">${sessionScope.currentUser.loginName}</a></li>
				<li><a href="${ctx}/user/logout">退出</a></li>
			</c:if>
			<c:if test="${empty sessionScope.currentUser }">
				<li><a href="javascript:" data-mask='mask' data-id="login">登录</a></li>
				<li><a href="${ctx}/user/regist/home">注册</a></li>
			</c:if>
			<li class="linknone"><a href="http://help.caifupad.com/" class="a_333">帮助中心</a></li>
			<li class="linknone"><a href="https://www.caifupad.com/zt/yindao/yindao.html" class="a_333">新手引导</a></li>
		</ul>
		<ul class="uSevers">
			<li>客服热线：400-061-8080</li>
			<li class="linknone">工作日：09:00－18:00 &nbsp;&nbsp;&nbsp; 周 六：09:00-17:00</li>
		</ul>
	</div>
</div>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="1"/>
<!-- navindex end -->
<%@include file="./login/login.jsp"%>

<div class="clear"></div>
<!-- index_focus start -->
<div class="index_focus">

	<a href="javascript:;" class="index_focus_pre" title="上一张">上一张</a>
	<a href="javascript:;" class="index_focus_next" title="下一张">下一张</a>
	<script src="/cfp_data/js/3.js"></script>
</div>
<!-- index_focus end -->
<div class="clear"></div>
<!-- sRollNotice start -->
<div class="sRollNotice">
	<div class="dposiabso container">
		<!-- deye start -->
		<div class="dEye transformAll">
			<ul class="uEye">
					<li class="lfirst">推荐产品</li>
				<c:forEach items="${loanApplicationList}" var="loan" step="1" begin="0" end="0">
					<li class="ltitle">
						<a target="_blank" class="" style="color:#fff" href="${ctx}/finance/bidding?loanApplicationNo=${loan.loanApplicationId}">

								${loan.loanApplicationTitle}</a>
					</li>
					<li class="lyear clearFloat"><span class="syear">预期年化收益</span><span class="srat " ><code class="timer" data-id="cont-number"  id="cont-number" data-to="${loan.annualRate}" data-speed="1500">${loan.annualRate}</code><small>%<c:if test="${not empty loan.rewardsPercent && loan.rewardsPercent ne 0}">
						+${loan.rewardsPercent}%奖
					</c:if></small></span></li>
				</c:forEach>
				<c:if test="${isLogined}">
					<li class="laccound la1 clearFloat">
						<span>${sessionScope.currentUser.loginName},欢迎回来</span>
						<a href="${ctx}/user/logout">退出</a>
					</li>
					<li class="laccound la2 clearFloat">
						<span>账户余额：<fmt:formatNumber value="${cashAccount.value2}" pattern="#,##0.00"/>元</span>
						<a href="${ctx}/person/toIncome">充值</a>
					</li>
					<li class="lsum">
						<span class="ssumleft">累计出借<br /><big class="timer"   data-to="${allBuyBalance}" data-speed="1500"><fmt:formatNumber value="${allBuyBalance}" pattern="#,##0.00"/></big>元</span>
						<span class="ssumright">累计收益<br /><big class="timer" data-to="${allProfit}" data-speed="1500"><fmt:formatNumber value="${allProfit}" pattern="#,##0.00"/></big>元</span>
					</li>

				</c:if>
				<c:if test="${!isLogined}">
					<li class="lbuttom"><!-- <em class="leftxie"></em> --><a href="${ctx}/user/regist/home" class="btn btn-error">免费注册</a></li>
					<li class="laccount">已有账号，<a href="javascript:void(0)" class="thloginBtn" data-mask='mask' data-id="login" ><i></i>立即登录</a></li>
					<li class="lsum NotLogin">
						<span class="ssumleft">平台累计出借<br /><big class="timer"   data-to="${allBuyBalance}" data-speed="1500"><fmt:formatNumber value="${allBuyBalance}" pattern="#,##0.00"/></big>元</span>
						<span class="ssumright">为用户赚取<br /><big class="timer" data-to="${allProfit}" data-speed="1500"><fmt:formatNumber value="${allProfit}" pattern="#,##0.00"/></big>元</span>
					</li>
				</c:if>
			</ul>
		</div>
		<!-- deye end -->
		<div class="dagreen display-none">
			<i></i>
			<ul>
				<li class=""><a href="#">· iPhone6S找到主人！来围观10月活动获奖</a></li>
				<li class=""><a href="#">· iPhone6S找到主人！来围观10月活动获奖</a></li>
				<li class=""><a href="#">· iPhone6S找到主人！来围观10月活动获奖</a></li>
			</ul>
		</div>
	</div>
</div>
<!-- sRollNotice end -->

<!-- dBodyIndex start -->
<div class="dBodyIndex">
	<div class="container clearFloat">

		<!-- dLeft start -->
		<div class="dLeft clearFloat">
			<em class=""></em>
			<div class="dsafe clearFloat">
			
				<span class="sone">
                    <a href="https://www.caifupad.com/zt/safeguard/HighYield.html" targvet="_blank">
                        <i>
                            <img src="${ctx}/images/index_new/index-icon4.png" class="index-pic1"/>
                            <img src="${ctx}/images/index_new/index-icon1.png" class="index-pic2"/>
                        </i>
                        <code>财富派VS银行理财<small>合理高收益完胜银行理财</small></code>
                    </a>
                </span>
            	<span class="ssecond">
                    <a href="https://www.caifupad.com/zt/safeguard/Security.html" target="_blank">
                        <i>
                            <img src="${ctx}/images/index_new/index-icon4.png" class="index-pic1"/>
                            <img src="${ctx}/images/index_new/index-icon2.png" class="index-pic2"/>
                        </i>
                        <code>八重安保<small>全方位保障投资安全</small></code>
                    </a>
                </span>
            	<span class="sthird">
                    <a href="https://www.caifupad.com/zt/safeguard/reserve.html" target="_blank">
                        <i>
                            <img src="${ctx}/images/index_new/index-icon4.png" class="index-pic1"/>
                            <img src="${ctx}/images/index_new/index-icon3.png" class="index-pic2"/>
                        </i>
                        <code>运营披露<small>风险准备金信息披露</small></code>
                    </a>
                </span>
			</div>
			<a onclick="subgo()" style="cursor: pointer;">
				<div class="Novice"></div>
            </a>
			
			<!-- dFinancePlan start -->
			<c:if test="${sxjhTotal != 0}">
            <div class="dFinancePlan clearFloat sindex_Shengxin">
            	<div class="sindexLeft">
                    <h3>省心计划</h3>
                    <span><i>————</i>￥<i>————</i></span>
                    <p>优先投资，循环出借</p>
                    <a href="${ctx}/finance/list?tab=zhe" class="sxjhmore">查看更多</a>
                </div>
                <div class="sindexRight">
                	
                	<c:forEach items="${lendProductPublishVOList}" var="sxjh">
	                	<!-- 省心展示单区-开始 -->
	                    <div class="sindexRight_box">
	                        <div class="rightBox1">
	                            <span class="rightBox1_le cut_str"><i><img src="${ctx}/images/index_new/sXicon.jpg" /></i>${sxjh.publishName }</span>
	                            <span class="rightBox1_ri">省心期 <i>${sxjh.timeLimit }</i>个月</span>
	                        </div>
	                        <div class="rightBox2">
	                            <span class="rightBox2_le">预期年化收益<font>${sxjh.profitRate }％-${sxjh.profitRateMax }％</font></span>
	                            <span class="rightBox2_ri">
	                            <!-- 如果剩余金额大于0可正常购买 -->
	                            <c:if test="${sxjh.publishState=='1'.charAt(0)}">
	                            	<a href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${sxjh.lendProductPublishId}" class="sxBtn">预热中</a>	
	                            </c:if>
	                            <c:if test="${sxjh.publishState == '2'.charAt(0)}">
	                            	<a href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${sxjh.lendProductPublishId}" class="sxBtn">立即省心</a>	
	                            </c:if>
	                            <c:if test="${sxjh.publishState == '4'.charAt(0)}">
	                            	<a href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${sxjh.lendProductPublishId}" class="sxBtn">已完成</a>	
	                            </c:if>
	                            <!-- 如果剩余金额等于0已满额 -->
	                            <c:if test="${sxjh.publishState =='3'.charAt(0)}">
	                            	<a  href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${sxjh.lendProductPublishId}" class="sxBtn" style="background: #f5f5f5 none repeat scroll 0 0;border: medium none;color: #999;">已满额</a>	
	                            </c:if>
	                            </span>
	                        </div>
	                        <div class="clear"></div>
	                        <div class="rightBox3">
	                            <div class="sIndex_line">
	                            	<!-- 进度条展示区开始 -->
	                            	<c:if test="${sxjh.publishBalanceType =='2'.charAt(0)}">
	                            		<div class="anmation2" style="display:none; width:100%;" data-value="${sxjh.soldBalance*100/sxjh.publishBalance }" >
		                                     <div class="tiao2" style="width:${sxjh.soldBalance*100/sxjh.publishBalance }%;">
		                                        <div class="dian2"></div>
		                                        <i class="numvalS" style="display: none;">${sxjh.soldBalance*100/sxjh.publishBalance }%</i>
		                                    </div>
	                                	</div>
	                            	</c:if>
	                                <!-- 进度条展示区结束 -->
	                            </div>
	                        </div>
	                    </div>
                    	<!-- 省心展示单区-结束 -->
                	</c:forEach>
                	
                	<!-- 如果只有一条省心产品，则用图片补位 -->
                    <c:if test="${sxjhTotal==1}">
                    	<div class="sindexRight_box">
                        	<img src="${ctx}/images/index_new/sxBanner.jpg" style="max-width:100%;margin-top:-10px;" />
                    	</div>
                    </c:if>
                    
                    <!-- <div class="sindexRight_box">
                        <img src="${ctx}/images/index_new/sxBanner.jpg" style="max-width: 100%;" />
                    </div> -->
                    
                    <!--  
                    <div class="sindexRight_box">
                        <div class="rightBox1">
                            <span class="rightBox1_le"><i><img src="${ctx}/images/index_new/sXicon.jpg" /></i>省心计划a－001</span>
                            <span class="rightBox1_ri">省心期 1 个月</span>
                        </div>
                        <div class="rightBox2">
                            <span class="rightBox2_le">预期年化收益<font>7％－11%</font></span>
                            <span class="rightBox2_ri"><button class="btnM">已满额</button></span>
                        </div>
                        <div class="clear"></div>
                        <div class="rightBox3">
                            <div class="sIndex_line">
                                <div class="anmation2" style="display:none; width:100%;" data-value="50" >
                                     <div class="tiao2" style="width:50%;">
                                        <div class="dian2"></div>
                                        <i class="numvalS" style="display: none;">70%</i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    -->
                    
                </div>
            </div>
            </c:if>
            <!-- dFinancePlan end -->

			<!-- dImmeLend start -->
			<div class="dFinancePlan clearFloat lendimm" style="margin-top:0;">
			
			
				<h2 class="clearFloat"><span><i class="dFinancePlan_i"></i>散标理财<small>期限灵活，100元起投</small></span><a href="${ctx}/finance/list?tab=heng">更多>></a></h2>
				<ul class="uFinTitle clearFloat">
					<li class="lone">借款标题</li>
					<li class="ltwo">年化利率</li>
					<li class="lthree">借款期限</li>
					<li class="lfore">借款金额(元)</li>
					<li class="lfifle">&nbsp;</li>
				</ul>
				<c:forEach items="${loanApplicationList}" var="loan">
					
				
					<ul class="uFinList clearFloat">
						<li class="lone">
								<c:if test="${loan.oType=='1'||loan.oType=='2'}">
					 
						<i style="margin-top: 40px;margin-right: 5px;display: 
						inline-block;float: left;height: 17px;width: 20px;
						background: url(${ctx}/images/index_new/dxb.png) ;"></i>
						</c:if>
							<c:if test="${loan.oType=='3'}">
						<i style="margin-top: 40px;margin-right: 5px;display: 
						inline-block;float: left;height: 17px;width: 20px;
						background: url(${ctx}/images/index_new/newuser_icon.png) no-repeat;"></i>
						</c:if>
						<c:if test="${empty  loan.oType||loan.oType=='0'}">
						<i class="iJie"></i>
						</c:if>
						
						
						<a target="_blank" href="${ctx}/finance/bidding?loanApplicationNo=${loan.loanApplicationId}">
							<c:if test="${not empty loan.rewardsPercent && loan.rewardsPercent ne 0}">
								<%--<i>奖${loan.rewardsPercent}%</i>--%>
							</c:if>
								${loan.loanApplicationTitle}</a></li>
						<c:if test="${not empty loan.rewardsPercent && loan.rewardsPercent ne 0}">
							<li class="ltwo"><em class="eyearm"></em><i>${loan.annualRate}</i>%<small>+${loan.rewardsPercent}%</small></li>
						</c:if>
						<c:if test="${empty loan.rewardsPercent || loan.rewardsPercent eq 0}">
							<li class="ltwo">${loan.annualRate}%</li>
						</c:if>
						<li class="lthree">${loan.cycleCount}<small>个月</small></li>
						<li class="lfore"><fmt:formatNumber value="${loan.confirmBalance}" pattern="#,#00.00"/></li>

						<c:if test="${loan.ratePercent eq '100'}">
							<li class="lfifle"><a target="_blank" href="${ctx}/finance/bidding?loanApplicationNo=${loan.loanApplicationId}" class="btn awriteBtn" >已满标</a></li>
						</c:if>
						<c:if test="${loan.ratePercent ne '100'}">
							<li class="lfifle"><a target="_blank" href="${ctx}/finance/bidding?loanApplicationNo=${loan.loanApplicationId}" class="btn aredBtn" >
								<c:if test="${loan.begin == false}">
									预热中
								</c:if>
								<c:if test="${loan.begin == true}">
									立即出借
								</c:if>
							</a></li>
						</c:if>
						<li   class="anmation" style="display: none; <c:if test="${loan.remain < 1}" >width:100%;</c:if>" data-value="${loan.ratePercent}" >
							<div class="tiao" style="width: ${loan.ratePercent}%;">
							<div class="dian"></div>
							<i class="numval" style="display: none;">${loan.ratePercent}%</i>
							</div>
							<c:if test="${loan.remain > 0}">
								<i class="rigval rigval_cl" style="left: ${1-loan.ratePercent}%; display: none;" >剩余可投<fmt:formatNumber value="${loan.remain }" pattern="#,#00.00"/></i>
							</c:if>
						</li>
					</ul>
				</c:forEach>
			</div>
			<!-- dFinancePlan end -->
		</div>
		<!-- dLeft end -->
		<!-- dRight start -->
		<script src="/cfp_data/js/1.js"></script>


	</div>
</div>
<!-- dBodyIndex end -->
<!-- masklayer start  -->
<%--<div class="masklayer videogroup" id="vediogroup" style="width:1000px;height:500px; background:#000; top:8%;">

	<object class="objectgroup" classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" width="1000"
			height="500" codebase="http://fpdownload.adobe.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0">
		<i class="close" data-id="close">×</i>
		<param name="movie" value="vedio.swf">
		<param name="play" value="true">
		<param name="loop" value="true">
		<param name="quality" value="high">
		<param name="allowFullScreen" value="true">
		<embed src="../images/vedio/vedio.swf" width="1000" height="500" play="true" loop="true" quality="high" pluginspage="http://www.adobe.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash">
		</embed>
	</object>
</div>--%>
<!-- masklayer end -->
<!--飘窗登录前start-->
<%--<div class="bay-window" id="bay-window">
	<c:if test="${isLogined}">
		<div class="bay-wd-main">
			 <div class="bay-wd-main-le">
				<span class="span-left"><img src="${ctx}/images/pc-icon1.jpg" />您的累计出借金额 <font class="bay-wd-main-blue"><fmt:formatNumber value="${allBuyBalance}" pattern="#,##0.00"/></font><i>元</i></span>
				<span class="span-right"><img src="${ctx}/images/pc-icon2.jpg" />为您赚的收益 <font  class="bay-wd-main-red"><fmt:formatNumber value="${allProfit}" pattern="#,##0.00"/></font><i>元</i></span>
			 </div>
			 <div class="bay-wd-main-ri">
				<a href="${ctx}/finance/list" class="btn btn-error bay-login">立即出借</a>
				<a href="${ctx}/person/toIncome" class="bay-register ml-20">充值</a>
			 </div>
		</div>
	</c:if>
	<c:if test="${!isLogined}">
		<div class="bay-wd-main">
			 <div class="bay-wd-main-le">
				<span class="span-left"><img src="${ctx}/images/pc-icon3.png" />平台累计出借金额 <font class="bay-wd-main-blue"><fmt:formatNumber value="${allBuyBalance}" pattern="#,##0.00"/></font><i>元</i></span>
				<span class="span-right"><img src="${ctx}/images/pc-icon4.jpg" />为出借人赚的收益 <font  class="bay-wd-main-red"><fmt:formatNumber value="${allProfit}" pattern="#,##0.00"/></font><i>元</i></span>
			 </div>
			 <div class="bay-wd-main-ri">
				<a href="javascript:" data-mask='mask' data-id="login" class="btn btn-error bay-login">登录</a>
				<a href="${ctx}/user/regist/home" class="bay-register ml-20">快速注册</a>
			 </div>
		</div>
	</c:if>
</div>--%>
<!--飘窗end-->
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
<script>
//当滚动条的位置处于距顶部100像素以下时，跳转链接出现，否则消失
/*$(function () {
	$(window).scroll(function(){
		if ($(window).scrollTop()>100){
			$(".bay-window").fadeIn(500);
		}else{
			$(".bay-window").fadeOut(500);
		}
	});
});*/
//返回顶部

</script>

<script type="text/javascript">
	$(document).ready(function(){

		//大banner特效左右滚动控制显示或隐藏
		$(".index_focus").hover(function(){
			$(this).find(".index_focus_pre,.index_focus_next").stop(true, true).fadeTo("show", 1)
		},function(){
			$(this).find(".index_focus_pre,.index_focus_next").fadeOut()
		});
		//大banner特效
		$(".index_focus").slide({
			titCell: ".slide_nav a ",
			mainCell: ".bd ul",
			delayTime: 500,
			interTime: 3500,
			prevCell:".index_focus_pre",
			nextCell:".index_focus_next",
			effect: "fold",
			autoPlay: true,
			trigger: "click",
			startFun:function(i){
				$(".index_focus_info").eq(i).find("h3").css("display","block").fadeTo(1000,1);
				$(".index_focus_info").eq(i).find(".text").css("display","block").fadeTo(1000,1);
			}
		});
		//小banner特效
	/*	$('.ck-slide').ckSlide({
			autoPlay: true,//默认为不自动播放，需要时请以此设置
			dir: 'x',//默认效果淡隐淡出，x为水平移动，y 为垂直滚动
			interval:3000//默认间隔2000毫秒
		});*/
		
		//首页省心计划模块进度条
	    $('.sindexRight_box').mouseenter(function(){   
	        var boxAnmation = $(this).find(".anmation2"); 
	        var boxValue = boxAnmation.attr("data-value") ;
	        boxAnmation.show();       
	        boxAnmation.find('.tiao2').animate({
	            "width":boxValue+"%" 
	        },800,function(){
	            boxAnmation.find('.numvalS').fadeIn(300);
	        });
	        
	    }); 

	    $('.sindexRight_box').mouseleave(function(){ 
	        var boxAnmation = $(this).find(".anmation2"); 
	        boxAnmation.find('.tiao2').animate({
	            "width":"0%"    
	        });
	        boxAnmation.find('.numvalS').fadeOut();
	        boxAnmation.hide();       
	    });

	});
	
		function subgo(){
		  window.location.href="${ctx}/finance/list?flag=new&cache="+Math.random();
	}
</script>

<script type="text/javascript" src="${ctx}/js/index_new/index2.js"></script>
<!-- <script type="text/javascript" src="${ctx}/js/jquery.jslides.js"></script>action javascript -->
<%@include file="common/footLine1.jsp"%>
<%@include file="common/sidebar.jsp"%>
</body>
</html>
