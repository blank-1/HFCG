<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="../../common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    pageContext.setAttribute("basePath", basePath);
%>

<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache, must-revalidate">
<meta http-equiv="expires" content="0">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<link rel="stylesheet" href="${ctx }/gamecss/gameCss/reset.css" type="text/css">
<!-- Link Swiper's CSS -->
<link rel="stylesheet" href="${ctx }/gamecss/gameCss/swiper.min.css">
<!--  styles.css -->
<link rel="stylesheet" href="${ctx }/gamecss/gameCss/style.css">
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
<title>财富派平台年度账单</title>
</head>
<body>
	<div class="swiper-container">
	    <div class="swiper-wrapper">
	        <div class="swiper-slide">
	            <div class="theme page_1">
	                <div class="animation animated1 ani_1">
	                    <img src="${ctx }/gameimg/gameimg/font1.png" alt="">
	                </div>
	                <div class="animation animated2 ani_2">
	                    <img src="${ctx }/gameimg/gameimg/font2.png" alt="">
	                </div>
	                <ul class="info_content">
	                    <li class="tit_name">用户名</li>
	                    <li>${currentUser.loginName}</li>
	                 
	                    <li class="tit_name">加入时间</li>
	                    <c:if test="${not empty bill.regisTime}">
	                      <li><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"  value="${bill.regisTime}"/></li>
	                    </c:if>
	                   <c:if test="${ empty bill.regisTime}">
	                      <li><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"  value="${currentUser.createTime}"/></li>
	                    </c:if>
	                    <li class="tit_name">首投时间</li>
	                    <li><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${bill.fastInvestTime}"/></li>
	                </ul>
	            </div>
	            	        </div>
	            	        <div class="swiper-slide">
	            <div class="theme page_2">
	                <div class="animation animated3">
	                    <img src="${ctx }/gameimg/gameimg/font3.png" alt="">
	                </div>
	                <div class="animation animated4">
	                    <img src="${ctx }/gameimg/gameimg/font4.png" alt="">
	                </div>
	                <ul class="invest_content">
	                    <li class="invest_name">累计投资</li>
	                    <li id="money">
	                     <c:if test="${not empty bill.investValue}">
	                      <fmt:formatNumber type="number" value="${bill.investValue}" pattern="#,###" maxFractionDigits="0"/>元</li>
	                    </c:if>
	                   <c:if test="${ empty bill.investValue}">
	                      <li>0元</li>
	                    </c:if>
	                   
	                    <li class="invest_name">已获收益</li>
	                    <c:if test="${not empty bill.profitValue}">
	                      <li><fmt:formatNumber type="number" value="${bill.profitValue}" pattern="#,###" maxFractionDigits="2"/>元</li>
	                    </c:if>
	                   <c:if test="${ empty bill.profitValue}">
	                      <li>0元</li>
	                    </c:if>
	                    
	                   
	                    <li class="invest_name">相当于可购买</li>
	                    <li class="Similar"><fmt:formatNumber type="number" value="${bill.profitValue/0.7}" maxFractionDigits="0"/>个鸡蛋</li>
	                    <li class="invest_name">比银行理财多赚</li>
	                    <li><fmt:formatNumber type="number" value="${bill.profitValue*0.66}" pattern="#,###" maxFractionDigits="2"/>元</li>
	                </ul>
	            </div>
	            	        </div>
	            	        <div class="swiper-slide">
	            <div class="theme page_3">
	                <div class="animation animated5">
	                    <img src="${ctx }/gameimg/gameimg/font5.png" alt="">
	                </div>
	                <div class="animation animated6">
	                    <img src="${ctx }/gameimg/gameimg/font6.png" alt="">
	                </div>
	                <div class="Invitation">
	                    邀请好友人数
	                    <c:if test="${not empty bill.inviteNumber}">
	                      <p>${bill.inviteNumber}人</p>
	                    </c:if>
	                  <c:if test="${ empty bill.inviteNumber}">
	                      <p>0人</p>
	                    </c:if>
	                </div>
	            </div>
	            	        </div>
	            	        <div class="swiper-slide">
	            <div class="theme page_4">
	                <div class="animteBox">
	                    <div class="animation animated7">
	                        <img src="${ctx }/gameimg/gameimg/font7.png" alt="">
	                    </div>
	                    <div class="animation animated8">
	                        <img src="${ctx }/gameimg/gameimg/font8.png" alt="">
	                    </div>
	                </div>
	                <ul class="Voucher">
	                    <li>累计获得财富券</li>
						<c:if test="${not empty bill.getVoucher}">
							<li class="money">${bill.getVoucher}元</li>
						</c:if>
						<c:if test="${ empty bill.getVoucher}">
							<li class="money">0元</li>
						</c:if>
						<li>累计使用财富券</li>
						<c:if test="${not empty bill.useVoucher}">
							<li class="money">${bill.useVoucher}元</li>
						</c:if>
						<c:if test="${ empty bill.useVoucher}">
							<li class="money">0元</li>
						</c:if>
					</ul>
	            </div>
	            	        </div>
	            	        <div class="swiper-slide">
	            	           <div class="theme page_5">
	               <div class="animation animated9">
	                   <img src="${ctx }/gameimg/gameimg/font9.png" alt="">
	               </div>
	               <div class="animation animated10">
	                   <img src="${ctx }/gameimg/gameimg/font10.png" alt="">
	               </div>
	               <div class="mark">
	                   <p>热衷标的  </p>
	                   <p class="month">
	                   <c:if test="${ bill.likeBidCycle != 0 &&not empty bill.likeBidCycle }">
	                     ${bill.likeBidCycle}个月标
	                   </c:if>
	                   <c:if test="${ bill.likeBidCycle == 0 ||empty bill.likeBidCycle}">
	                   		  暂无
	                   </c:if>
	                 </p>
	               </div>
	               <div class="animate13">
	                   <img src="${ctx }/gameimg/gameimg/Flower.png" alt="">
	               </div>
	            	           </div>
	            	       </div>
	            	        <div class="swiper-slide">
	            <div class="theme page_6">
	                <div class="animation animated11">
	                    <img src="${ctx }/gameimg/gameimg/font11.png" alt="">
	                </div>
	                <div class="animation animated12">
	                    <img src="${ctx }/gameimg/gameimg/font12.png" alt="">
	                </div>
	                <ul class="info_content" style="border:none;margin-top: 40%;">
	                    <li class="tit_name">已获收益排名</li>
						<c:if test="${not empty bill.profitRanking}">
							<li>${bill.profitRanking}</li>
						</c:if>
						<c:if test="${ empty bill.profitRanking}">
							<li>0</li>
						</c:if>
						<li class="tit_name"> 累计投资排名</li>
						<c:if test="${not empty bill.profitRanking}">
							<li>${bill.profitRanking}</li>
						</c:if>
						<c:if test="${ empty bill.profitRanking}">
							<li>0</li>
						</c:if>
						<li class="tit_name"> 邀请好友人数排名</li>
						<c:if test="${not empty bill.investRanking}">
							<li>${bill.investRanking}</li>
						</c:if>
						<c:if test="${ empty bill.investRanking}">
							<li>0</li>
						</c:if>
					</ul>
	            </div>
	            	        </div>
	        <div class="swiper-slide" onclick="gotonewYear()">
	            <div class="theme page_7" style="width: 100%;height: 100%;">
	                <div class="animation animated13">
	                    <img src="${ctx }/gameimg/gameimg/font13.png" alt="">
	                </div>
	                <div class="animation animated14">
	                    <img src="${ctx }/gameimg/gameimg/font15.png" alt="">
	                </div>
	                <div class="animation animated15">
	                    <img src="${ctx }/gameimg/gameimg/btn.png" alt="">
	                </div>
	            </div>
	        </div>
	    </div>
	    <div class="swiper-pagination"></div>
	</div>
	<i id="audioCtl" class="audioCtl"></i>
	  <audio id="media" src="${ctx }/gameimg/gameimg/main.mp3" autoplay="autoplay" preload="load" loop="loop"></audio>  
	<script src="${ctx }/gamejs/gamejs/lib/jquery-1.11.0.min.js"></script>
	<script src="${ctx }/gamejs/gamejs/swiper.min.js"></script>
	<script src="${ctx }/gamejs/gamejs/AnnualBill.js"></script>
	<script src="${ctx }/gamejs/gamejs/autoPlayAudio1.js"></script>
</body>
<script type="text/javascript">
var rootPath = '<%=ctx%>';
	function gotonewYear(){
		window.location.href=  rootPath+"/gamejs/fuli2/wuliCaifupad.html";
	}
	 
	

</script>
</html>
