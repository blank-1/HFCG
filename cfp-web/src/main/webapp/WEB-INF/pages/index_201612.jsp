<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="./common/taglibs.jsp"%>
<%@include file="./common/common_js.jsp"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="utf-8" />
		<meta name="keywords" content="财富派,投资理财,p2p投资,p2p理财,p2p网贷平台" />
		<meta name="description" content="财富派 - 国内领先的P2P网贷平台，为企业和个人投资者提供专业的P2P理财服务。并通过严格的风控审核，让您的投资理财变得更安全、简单、高效。财富派，为您个人理财保驾护航。" />
		<meta http-equiv="X-UA-Compatible" content="IE=7;IE=9;IE=8;IE=10;IE=11">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
		<title>财富派 - 国内专业的P2P网贷投资理财服务平台</title>
		
		<link rel="stylesheet" type="text/css" href="${ctx}/css/index.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/css/index_new/style.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/css/cxm_index.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/css/indexNew.css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/css/index_new/media.css" />
		
		<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
		<script type="text/javascript" src="${ctx}/js/index_new/jquery.SuperSlide.js"></script>
		<script type="text/javascript" src="${ctx}/js/index_new/index.js"></script>
		<script type="text/javascript" src="${ctx}/js/index_new/index_person.js"></script>
		<script type="text/javascript" src="${ctx}/js/index_new/index2.js"></script>
		<style>
			.header .dlogo a img{ vertical-align:middle;margin-top:21px;}
		</style>
	</head>
<body>

<!-- 下面导入登录弹出层 -->
<%@include file="./login/login.jsp"%>

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
			<li class="linknone"><a href="http://help.caifupad.com" class="a_333">帮助中心</a></li>
			<li class="linknone"><a href="/zt/yindao/yindao.html" class="a_333">新手引导</a></li>
		</ul>
		<ul class="uSevers">
			<li>客服热线：400-061-8080</li>
			<li class="linknone">工作日：09:00－18:00 &nbsp;&nbsp;&nbsp; 周 六：09:00-17:00</li>
		</ul>
	</div>
</div>
<!-- line2 start -->

<!-- 导航菜单（开始） -->
<customUI:headLine action="1"/>
<!-- 导航菜单（结束） -->

<div class="clear"></div>

<!-- index_focus start -->
<div class="index_focus">
	<a href="javascript:;" class="index_focus_pre" title="上一张">上一张</a>
	<a href="javascript:;" class="index_focus_next" title="下一张">下一张</a>
	<script src="/cfp_data/js/3.js"></script>
</div>
<!-- index_focus end -->

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
					<li class="lbuttom"><a href="${ctx}/user/regist/home" class="btn btn-error">免费注册</a></li>
					<li class="laccount">已有账号，<a href="javascript:void(0)" class="thloginBtn" data-mask='mask' data-id="login" ><i></i>立即登录</a></li>
					<li class="lsum NotLogin">
						<span class="ssumleft">平台累计出借<br /><big class="timer"   data-to="${allBuyBalance}" data-speed="1500"><fmt:formatNumber value="${allBuyBalance}" pattern="#,##0.00"/></big>元</span>
						<span class="ssumright">为用户赚取<br /><big class="timer" data-to="${allProfit}" data-speed="1500"><fmt:formatNumber value="${allProfit}" pattern="#,##0.00"/></big>元</span>
					</li>
				</c:if>
			</ul>
        </div>
        <!-- deye end -->
    </div>
</div>
<!-- sRollNotice end -->

<!-- dBodyIndex start -->
<div class="dBodyIndex">
	<div class="container clearFloat">
    	<div class="indexNewbox clearFloat">
        	<div class="dsafe clearFloat">
            	<span class="sone">
                    <a href="/zt/safeguard/HighYield.html" targvet="_blank">
                        <i>
                            <img src="${ctx}/images/newIcon/index-icon4.png" class="index-pic1"/>
                            <img src="${ctx}/images/newIcon/index-icon1.png" class="index-pic2"/>
                        </i>
                        <code>财富派VS银行理财<small>合理高收益完胜银行理财</small></code>
                    </a>
                </span>
            	<span class="ssecond">
                    <a href="/zt/safeguard/Security.html" target="_blank">
                        <i>
                            <img src="${ctx}/images/newIcon/index-icon4.png" class="index-pic1"/>
                            <img src="${ctx}/images/newIcon/index-icon2.png" class="index-pic2"/>
                        </i>
                        <code>八重安保<small>全方位保障资金安全</small></code>
                    </a>
                </span>
            	<span class="sthird">
                    <a href="/zt/safeguard/reserve.html" target="_blank">
                        <i>
                            <img src="${ctx}/images/newIcon/index-icon4.png" class="index-pic1"/>
                            <img src="${ctx}/images/newIcon/index-icon3.png" class="index-pic2"/>
                        </i>
                        <code>运营披露<small>风险准备金信息披露</small></code>
                    </a>
                </span>
            </div>
            <div class="QRcode">
                <div class="QRcodele">
                    <img src="${ctx}/images/newIcon/icon_01.jpg" />
                </div>
                <div class="QRcoderi">
                    <p class="qrcodeText">扫描<i>左侧二维码</i></p>
                    <p class="qrcodeText2">下载财富派APP</p>
                    <p class="qrcodeText3">
                        <span style="float:left;"><i><img src="${ctx}/images/newIcon/iosIcon.jpg" /></i>IOS</span>
                        <span style="float:right;"><i><img src="${ctx}/images/newIcon/androidIcon.jpg" /></i>Android</span>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- dBodyIndex end -->

<!-- 新手标（开始） -->
<div class="newHandMain">
    <div class="container newHand clearFloat">
        <div class="newHandle">
            <img src="${ctx}/images/newIcon/star.png" class="starImg" />
            <p class="newHandlep">新手专享<span>新手加息，无需抢标</span></p>
            <p><a href="${ctx}/finance/list?flag=new">查看更多</a></p>
        </div>
        <div class="newHandri">
            <div class="newHandriTitle"><i class="new"><img src="${ctx}/images/newIcon/icon_02.jpg" /></i>${loanSpecialApplication.loanApplicationTitle}</div>
            <div class="newHandbox">
                <div class="newHandB1">
                	<c:if test="${not empty loanSpecialApplication.rewardsPercent && loanSpecialApplication.rewardsPercent ne 0}">
						<p>${loanSpecialApplication.annualRate}<i>%</i>+<font>${loanSpecialApplication.rewardsPercent}</font><i>%</i><i><img src="${ctx}/images/newIcon/icon_05.jpg" style="margin-left:16px;" /></i></p>
					</c:if>
					<c:if test="${empty loanSpecialApplication.rewardsPercent || loanSpecialApplication.rewardsPercent eq 0}">
						<li class="sanBiaoUlli2">${loanSpecialApplication.annualRate}％</li>
						<p>${loanSpecialApplication.annualRate}<i>%</i></p>
					</c:if>
                
                    <p class="pText">预期年化收益</p>
                </div>
                <div class="newHandB2">
                    <p>${loanSpecialApplication.cycleCount}<i>个月</i></p>
                    <p class="pText">借款期限</p>
                </div>
                <div class="newHandB3">
                    <p><fmt:formatNumber value="${loanSpecialApplication.confirmBalance}" pattern="#,#00.00"/></p>
                    <p class="pText">借款金额（元）</p>
                </div>
                <div class="newHandB4 ph-post1-wrapper">
                   	<!-- 已满标  -->
	                <c:if test="${loanSpecialApplication.ratePercent eq '100'}">
						<a target="_blank" href="${ctx}/finance/bidding?loanApplicationNo=${loanSpecialApplication.loanApplicationId}" class="ljcjBtn" 
							style="background: #D9D9D9;color: #fff;border: 1px #D9D9D9 solid;">
	                		已满标
	                	</a>	
					</c:if>
	                
	                <!-- 预热中  -->
               		<c:if test="${loanSpecialApplication.ratePercent ne '100'}">
						<c:if test="${loanSpecialApplication.begin == false}">
							<a target="_blank" href="${ctx}/finance/bidding?loanApplicationNo=${loanSpecialApplication.loanApplicationId}" class="ljcjBtn" 
								style="background:#fd934f;color:#fff;border:1px #fd934f solid;">
								预热中
							</a>
						</c:if>
					</c:if> 
	                
	                <!-- 立即出借  -->
               		<c:if test="${loanSpecialApplication.ratePercent ne '100'}">
						<c:if test="${loanSpecialApplication.begin == true}">
							<a target="_blank" href="${ctx}/finance/bidding?loanApplicationNo=${loanSpecialApplication.loanApplicationId}" class="ljcjBtn button" data-title="立即出借">
								<span id="justPay" percent-data=${loanSpecialApplication.ratePercent }><span>立即出借</span></span>
							</a>
						</c:if>
					</c:if>
					
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 新手标（结束） -->

<!-- 省心计划（开始） -->
<div class="newshengxMain">
    <div class="shengxin container clearFloat">
        <div class="shengxinTitle">
            <div class="shengxJh">省心计划<span>优先投资，循环出借</span></div>
            <div class="shengXmore"><a href="${ctx}/finance/list?tab=zhe">查看更多>></a></div>
        </div>
        <div style="clear:both;"></div>
        <div class="shengxinBox clearFloat">
            <div class="shengxinLe">
                <div class="sxTexttitle"><i><img src="${ctx}/images/newIcon/icon_03.jpg" /></i>${shengxinLe.publishName }</div>
                <div class="shengxinboxs">
                    <div class="shengXinB1" style="width:40%;float:left;text-align:center;margin:15px 0;">
                        <p class="shengXt">${shengxinLe.profitRate }<i>%</i>-${shengxinLe.profitRateMax }<i>%</i></p>
                        <p class="shengXtext">预期年化收益</p>
                    </div>
                    <div class="shengXinB2" style="width:30%;float:left;text-align:center;margin:15px 0;">
                        <p class="shengXt2">${shengxinLe.timeLimit }<i>个月</i></p>
                        <p class="shengXtext">省心期</p>
                    </div>
                    <div class="shengXinB3 ph-post1-wrapper" style="width:30%;float:left;">
                    
                        <c:if test="${shengxinLe.publishState == '1'.charAt(0)}">
                        	<a href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${shengxinLe.lendProductPublishId}" 
                        		style="background:#fd934f;color:#fff;border:1px #fd934f solid;">
                        		预热中
                        	</a>
                        </c:if>
                        <c:if test="${shengxinLe.publishState == '2'.charAt(0)}">
                        	<a href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${shengxinLe.lendProductPublishId}" class="button" data-title="立即省心">
                        		<c:if test="${shengxinLe.publishBalanceType eq '2'.charAt(0)}">
                        			<span id="justPay" percent-data=${shengxinLe.soldBalance*100/shengxinLe.publishBalance gt 100?100:shengxinLe.soldBalance*100/shengxinLe.publishBalance }><span>立即省心</span></span>
                        		</c:if>
                        		<c:if test="${shengxinLe.publishBalanceType eq '2'.charAt(0)}">
                        			<span id="justPay" percent-data=0><span>立即省心</span></span>
                        		</c:if>
                        	</a>
                        </c:if>
                        <c:if test="${shengxinLe.publishState == '4'.charAt(0)}">
                        	<a href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${shengxinLe.lendProductPublishId}" >
                        		已完成
                        	</a>
                        </c:if>
                        <c:if test="${shengxinLe.publishState == '3'.charAt(0)}">
                        	<a href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${shengxinLe.lendProductPublishId}" 
                        		style="background: #D9D9D9;color: #fff;border:1px #D9D9D9 solid;">
                        		已满额
                        	</a>
                        </c:if>
                        
                    </div>
                </div>
            </div>
            <div class="shengxinRi">
                <div class="sxTexttitle"><i><img src="${ctx}/images/newIcon/icon_03.jpg" /></i>${shengxinRi.publishName }</div>
                <div class="shengxinboxs">
                    <div class="shengXinB1" style="width:40%;float:left;text-align:center;margin:15px 0;">
                        <p class="shengXt">${shengxinRi.profitRate }<i>%</i>-${shengxinRi.profitRateMax }<i>%</i></p>
                        <p class="shengXtext">预期年化收益</p>
                    </div>
                    <div class="shengXinB2" style="width:30%;float:left;text-align:center;margin:15px 0;">
                        <p class="shengXt2">${shengxinRi.timeLimit }<i>个月</i></p>
                        <p class="shengXtext">省心期</p>
                    </div>
                    <div class="shengXinB3 ph-post1-wrapper" style="width:30%;float:left;">
                    
                		<c:if test="${shengxinRi.publishState == '1'.charAt(0)}">
                        	<a href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${shengxinRi.lendProductPublishId}" 
                        		style="background:#fd934f;color:#fff;border:1px #fd934f solid;">
                        		预热中
                        	</a>
                        </c:if>
                        <c:if test="${shengxinRi.publishState == '2'.charAt(0)}">
                        	<a href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${shengxinRi.lendProductPublishId}" class="button" data-title="立即省心">
                        		<c:if test="${shengxinRi.publishBalanceType eq '2'.charAt(0)}">
                        			<span id="justPay" percent-data=${shengxinRi.soldBalance*100/shengxinRi.publishBalance gt 100?100:shengxinRi.soldBalance*100/shengxinRi.publishBalance}><span>立即省心</span></span>
                        		</c:if>
                        		<c:if test="${shengxinRi.publishBalanceType ne '2'.charAt(0)}">
                        			<span id="justPay" percent-data=0><span>立即省心</span></span>
                        		</c:if>
                        	</a>
                        </c:if>
                        <c:if test="${shengxinRi.publishState == '4'.charAt(0)}">
                        	<a href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${shengxinRi.lendProductPublishId}" >
                        		已完成
                        	</a>
                        </c:if>
                        <c:if test="${shengxinRi.publishState == '3'.charAt(0)}">
                        	<a href="${ctx}/finance/toFinanceDetail?lendProductPublishId=${shengxinRi.lendProductPublishId}" 
                        		style="background: #D9D9D9;color: #fff;border:1px #D9D9D9 solid;">
                        		已满额
                        	</a>
                        </c:if>
                        
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 省心计划（结束） -->

<!-- 散标（开始） -->
<div class="sanBiaoMain">
    <div class="sanBiao container clearFloat">
        <div class="shengxinTitle">
            <div class="shengxJh">精选散标<span>优质债权，期限灵活</span></div>
            <div class="sanBiaomore"><a href="${ctx}/finance/list?tab=heng">查看更多>></a></div>
        </div>
        <div style="clear:both;"></div>
        <div class="sanBiaolist clearFloat">
            <ul class="sanBiaoUltitle">
                <li class="sanBiaoUlli">借款标题</li>
                <li class="sanBiaoUlli2">年化利率</li>
                <li class="sanBiaoUlli3">借款期限</li>
                <li class="sanBiaoUlli4">借款金额（元）</li>
                <li class="sanBiaoUlli5"></li>
            </ul>
            
            <c:forEach items="${loanApplicationList}" var="loan" varStatus="u_no">
            	<ul class="sanBiaoUllist" <c:if test="${u_no.index==4 }">style="border-bottom:none;"</c:if> >
	                <li class="sanBiaoUlli"><i>
						<c:if test="${loan.loanType eq '9'}">
							<img src="${ctx}/images/icon_xjd.png" /></i>
						</c:if>
						<c:if test="${loan.loanType ne '9'}">
						<img src="${ctx}/images/newIcon/icon_04.jpg" /></i>
						</c:if>

	                	<a target="_blank" href="${ctx}/finance/bidding?loanApplicationNo=${loan.loanApplicationId}">${loan.loanApplicationTitle}</a>
	                </li>
                	<c:if test="${not empty loan.rewardsPercent && loan.rewardsPercent ne 0}">
						<li class="sanBiaoUlli2">${loan.annualRate}％＋<i>${loan.rewardsPercent}%</i><font><img src="${ctx}/images/newIcon/icon_06.jpg" /></font></li>
					</c:if>
					<c:if test="${empty loan.rewardsPercent || loan.rewardsPercent eq 0}">
						<li class="sanBiaoUlli2">${loan.annualRate}％</li>
					</c:if>
	                <li class="sanBiaoUlli3"><i> ${loan.loanType eq '9'?"14":loan.cycleCount}</i>
							${loan.loanType eq '9'?"天":"个月"}
					</li>
	                <li class="sanBiaoUlli4"><fmt:formatNumber value="${loan.confirmBalance}" pattern="#,#00.00"/></li>
	                <li class="sanBiaoUlli5 ph-post1-wrapper">
	                
	                <!-- 已满标  -->
	                <c:if test="${loan.ratePercent eq '100'}">
						<a target="_blank" href="${ctx}/finance/bidding?loanApplicationNo=${loan.loanApplicationId}" class="ljcjBtn" 
							style="background: #D9D9D9;color: #fff;border: 1px #D9D9D9 solid;">
	                		已满标
	                	</a>	
					</c:if>
	                
	                <!-- 预热中  -->
               		<c:if test="${loan.ratePercent ne '100'}">
						<c:if test="${loan.begin == false}">
							<a target="_blank" href="${ctx}/finance/bidding?loanApplicationNo=${loan.loanApplicationId}" class="ljcjBtn" 
								style="background:#fd934f;color:#fff;border:1px #fd934f solid;">
								预热中
							</a>
						</c:if>
					</c:if>
	                
	                <!-- 立即出借  -->
               		<c:if test="${loan.ratePercent ne '100'}">
						<c:if test="${loan.begin == true}">
							<a target="_blank" href="${ctx}/finance/bidding?loanApplicationNo=${loan.loanApplicationId}" class="ljcjBtn button" data-title="立即出借">
								<span id="justPay" percent-data=${loan.ratePercent }><span>立即出借</span></span>
							</a>
						</c:if>
					</c:if>
	                	
					</li>
            	</ul>
            </c:forEach>
            
        </div>
    </div>
</div>
<!-- 散标（结束） -->

<!-- 公告（开始） -->
<div class="caifunewsMain clearFloat">
	<script src="/cfp_data/js/1.js"></script>
</div>
<!-- 公告（结束） -->

<!-- 页尾（开始） -->
<script src="/cfp_data/js/25.js"></script>
<!-- 页尾（结束） -->

<script type="text/javascript">
$(document).ready(function(){

    $(".index_focus").hover(function(){
        $(this).find(".index_focus_pre,.index_focus_next").stop(true, true).fadeTo("show", 1)
    },function(){
        $(this).find(".index_focus_pre,.index_focus_next").fadeOut()
    });
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
    
    //重新设置首页登录框位置
    $("#login").css("top", document.body.clientHeight/2-(parseInt($(".masklayer").height()))*0.5);
    
    //立即出借按钮，鼠标划过事件
    $(".sanBiaoUllist,.newHandB4,.shengxinLe,.shengxinRi").hover(function(){
    	var per = $(this).find("#justPay").attr("percent-data");
    	$(this).find("#justPay").css("width",per+"%");
    },function(){
    	$(this).find("#justPay").css("width","0%");
    });

});
</script>
<!-- 下面导入右侧工具菜单 -->
<%@include file="common/sidebar.jsp"%>
</body>
</html>

