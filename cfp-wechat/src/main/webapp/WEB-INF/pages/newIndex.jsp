<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="./common/taglibs.jsp"%>
<%
    String ctx = request.getContextPath();
    pageContext.setAttribute("ctx", ctx);
%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />    
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta name="format-detection" content="telephone=no"/>
<meta name="msapplication-tap-highlight" content="no" />
<link rel="stylesheet" href="${ctx}/css/reset.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/index.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/sweetAlert.css?${version}" type="text/css">
<link rel="stylesheet" href="${ctx}/css/swiper.min.css?${version}">
<script src="${ctx}/js/lib/jquery-1.11.0.min.js?${version}"></script>
<script src="${ctx}/js/lib/sweetalert2.js?${version}"></script>
<script src="${ctx}/js/lib/radialIndicator.js?${version}"></script>
<script src="${ctx}/js/lib/swiper.min.js?${version}"></script>
<script src="${ctx}/js/scrolltext.js?${version}"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js" type="text/javascript"></script>
<script type="text/javascript">
var rootPath = '<%=ctx%>';
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
<script type="text/javascript">
var rootPath = '<%=ctx%>';
</script>

<title>首页</title>
</head>
<body style="overflow-y:scroll">
<section class="wrapper">
	<section class="content l_NewScroll">
		<header class="header">
			<div class="swiper-container">
				<div class="swiper-wrapper">
				<c:forEach items="${bannerList }" var="banner">
				<div class="swiper-slide" data-hash="slide1">
						<a href="${banner.httpUrl}"><img src="${banner.imageSrc}"></a>
					</div>
				</c:forEach>
				
				<%-- 	<div class="swiper-slide" data-hash="slide1">
						<a href="${ctx}/gamejs/annualBonus/index.html"><img src="${ctx}/gamejs/annualBonus/banner.png"></a>
					</div>
					<div class="swiper-slide" data-hash="slide2">
						<a href="${ctx}/gamejs/activePageFor1Year/ActivePageFor1Year.html"><img src="${ctx}/gamejs/activePageFor1Year/images/5y.png" alt=""></a>
					</div>
					<div class="swiper-slide" data-hash="slide3">
						<a href="${ctx}/gamejs/sxactivity/ActivePageForSheng.html"><img src="${ctx}/gamejs/sxactivity/share.jpg" alt=""></a>
					</div> --%>
				</div>
				<div class="swiper-pagination"></div>
			</div>
			<div class="title">
				<%-- <c:if test="${empty sessionScope.currentUser}">
					<p>平台累计出借<span><fmt:formatNumber value="${allBuyBalance}"/></span>元</p>
				</c:if>
				<c:if test="${not empty sessionScope.currentUser}">
					<p>您的累计出借金额<span><fmt:formatNumber value="${allBuyBalance}"/></span>元</p>
				</c:if> --%>
				
				<div class="title_bj" style="width:90%;">
					<div class="notice">
							<ul id="jsfoot02" class="noticTipTxt">
								<li><a href="${ctx}/notice/list?sign=notice">${noticeTitle==''?'网站公告信息':noticeTitle }</a></li>
								<li><a href="${ctx}/notice/list?sign=data">平台运营数据</a></li>
							</ul>
					</div>
				</div>
				
			</div>
		</header>
		<a class="l_banner2nd" href="${ctx}/finance/newDiddingList"><img src="${ctx}/images/index_banner2nd01.png" /></a>
		<section id="groom" class="groom">
			<ul class="scroller">
				<p class="l_title_shengxin">优质推荐</p>
				<li dataType="amount" class="l_titleIcon l_titleIcon_shengxin">
					<h2>${financePlanRows.publishName}</h2>
					<div class="record">
						<div class="interest">
							<p>${financePlanRows.profitRate}%-${financePlanRows.profitRateMax}%</p></br>
							预期年化利率 </br></br>
							<c:if test="${financePlanRows.publishBalanceType eq '1'}">
								剩余<span><fmt:formatNumber value="${availableBalance}"/></span>元
							</c:if>
						</div>
						<div class="snatch">
							<dl>
								<dd>
									<div><p><span>${financePlanRows.timeLimit}</span>个月</br>省心期</p></div>
									<div><p><span><fmt:formatNumber value="${financePlanRows.startsAt}"/></span>元</br>起投金额</p></div>
								</dd>
								<dt>
									<input type="hidden" id ="availableBalance"  value="${availableBalance ==0}"  />
									<input type="hidden" id ="publishState"  value="${financePlanRows.publishState == 4}"  />
									<a id="buttonSh" href="${ctx}/finance/toSxDetail?lendProductPublishId=${financePlanRows.lendProductPublishId}">抢购</a>
								</dt>
							</dl>
						</div>
					</div>
					<font>推荐</font>
				</li>
				<p class="l_title_shengxin l_title_shengxin2" id="bidList">精选散标</p>
				<%--<c:forEach items="${loanApplicationList}" var="loan">
					 <li onclick="location.href='${ctx}/finance/bidding?loanApplicationNo=${loan.loanApplicationId}'" dataType="amount" class="l_titleIcon l_titleIcon_company">
						<input type="hidden" value="${loan.ratePercent}" name="ratePercent"/>
						<h2 class="text-overflow">${loan.loanApplicationTitle}</h2>
						<ol class="list">
							<li>
								<div>
									<p>
										<span>${loan.annualRate}</span>%
										<c:if test="${not empty loan.rewardsPercent && loan.rewardsPercent ne 0}">
											<span style="font-size:1rem;">+${loan.rewardsPercent}</span>%
										</c:if>
									</p>年化利率
								</div>
							</li>
							<li>
								<div><p><span>${loan.cycleCount}</span>个月</p>理财期限</div>
							</li>
							<li >
							
							<c:if test="${loan.ratePercent eq '100'}">
										<c:choose>
											<c:when test="${6 == 6}">
												<div class=" backing  schedule"></div>	<!-- 还款中 -->
											</c:when>
											<c:when
												test="${7 == 7 ||8 == 8}">
												<div class=" done schedule"></div>
												<!-- 已结清 -->
											</c:when>
											<c:otherwise>
												<div class=" filled schedule"></div>
												<!-- //已满额 -->
											</c:otherwise>
										</c:choose>
									</c:if> <c:if test="${loan.begin == false}">
										<div class=" preheat  schedule"></div>
										<!-- 预热 -->
									</c:if> <c:if test="${loan.begin == true}">
										<div class="invest schedule"></div>
										<!-- 投资 -->
									</c:if>
									
									
									</li>
						</ol>
					</li>
				</c:forEach>--%>
				<div class="load-more">
					<a href="${ctx}/finance/list?tab=biao">更多标的随心选></a>
				</div>
				
			</ul>
		</section>
	</section>

	<c:if test="${not empty regFlag}">
		<a href="javascript:;" class="l_newer"></a>
	</c:if>
</section>
<c:if test="${not empty regFlag}">
	<div class="l_mask">
		<section class="l_newerWin">
			<p>新手专享</p>
			<p>新手专享标的，立即出借无需等待，年化收益7%-10%</p>
			<p id="close">取消</p>
			<p id="link">立即投资</p>
		</section>
	</div>
</c:if>

<%@include file="./common/navTag.jsp"%>
<script src="${ctx}/js/index.js?${version}"></script>
<script type="text/javascript">
$(".l_newer").click(function(){
	window.location.href="${ctx}/finance/newDiddingList";
})

$("#link").click(function(){
    window.location.href="${ctx}/finance/newDiddingList";
})

$(function(){
    var availableBalance =$("#availableBalance").val();
    var publishState =$("#publishState").val();
    if(availableBalance=='true'){
        $("#buttonSh").css("background","#DCD9D9");
        $("#buttonSh").text("已满额");


    }else if (publishState=='4'){
        $("#buttonSh").css("background","#DCD9D9");
        $("#buttonSh").text("已完成");
    }
 

 
	postTransferHtml();
	/********王亚东请求债权转让首页精英标
	 * 首页精英标
	 * 
	 * **********/
	function postTransferHtml() {
		$.ajax({
			url : rootPath + "/finance/getIndexLoanApplication",
			type : "post",
			success : function(data) {
					$("#bidList").after(
							makeHtmlByNearMake(data));
					console.log(makeHtmlByNearMake(data));
					
					var lis=$(".scroller").find("li[datatype='amount']");
					console.log($(".scroller").find("li[datatype='amount']"));
					for(var i=0;i<lis.length;i++){
						
						if(i>0){
							var div=$(lis[i]).find("ol[class='list']").find("li:last").find("div");
							var radialObj = $(div).radialIndicator({
								   barWidth : 10,
								   barColor : "#FF5E61",
								   barBgColor :"#efefef",
								   displayNumber: false
							}).data('radialIndicator');
							var rate=$(lis[i]).find("input[name='ratePercent']").val();
							radialObj.animate(Math.floor(rate));
						}
					}
			}
		});
	}
	
	
	
	
	/*********王亚东中转站
	 * *第一个数组是省心计划，第二个数组是精英标，第三个数组是 债权转让，第四个数组是新手标**
	 * *************/
	function makeHtmlByNearMake(data) {
		var thtml = "";
		d_rows = data.rows;//列表数据
		  var html = "";
		for (var i = 0; i < d_rows.length; i++) {
			var arrary = [];
				arrary = [];
				var schedule = "";
				
				var stauts = "";
				if (d_rows[i].loanType == 0 || d_rows[i].loanType == 3) {//信贷和企业信贷
					stauts = "l_titleIcon_belive";
				} else if (d_rows[i].loanType == 1 || d_rows[i].loanType == 7) {//1 房贷 个人房产直托
					stauts = "l_titleIcon_apartment";
				} else if (d_rows[i].loanType == 2 || d_rows[i].loanType == 8) {//2 企业车贷
					stauts = "l_titleIcon_car";
				} else if (d_rows[i].loanType == 4) {//2 企业包里
					stauts = "l_titleIcon_baoli";
				} else if (d_rows[i].loanType == 5) {// 基金
					stauts = "l_titleIcon_invest";
				} else if (d_rows[i].loanType == 6) {//企业标
					stauts = "l_titleIcon_company";
				}else if (d_rows[i].loanType == 9) {//现金贷
                   			 stauts = "l_titleIcon_shan";
                }
			 
				var orign = "";
				//定向标设置
				if (d_rows[i].oType == 2 || d_rows[i].oType == 1) {
					orign = '<span>定向</span>';
				} else if (d_rows[i].oType == 3) {
					orign = '<span>新手</span>';
				} else {
					orign = '';
				}
			//	var rewards = "";
				var rewards1 = "<p><span>" + d_rows[i].annualRate + "</span>% ";
				if (null != d_rows[i].rewardsPercent
						&& '0' != d_rows[i].rewardsPercent) {
					//rewards += "<span>加息</span>";
					rewards1 += "<i>+</i><span>" + d_rows[i].rewardsPercent
							+ "</span>%";
				}

				var process = 0;
						if(d_rows[i].ratePercent==100){
							if(d_rows[i].applicationState=='6'){
								schedule = " backing schedule " ;//还款中
							}else if(d_rows[i].applicationState=='7'||d_rows[i].applicationState=='8'){
								schedule = " done schedule " ;//已结清
							}else{
								schedule = " filled schedule " ;//已满额
							}
						}else{
							if(!d_rows[i].begin){
								schedule = "  preheat  schedule  ";//预热
							}else{
								schedule = " invest schedule " ;//投资
							}
						}
						schedule=schedule+"data-val="+d_rows[i].ratePercent;
				html += "  <li datatype='amount' onclick='toNormalDetail("+d_rows[i].loanApplicationId+")' "+" class='l_titleIcon "+stauts+"' >" 
						;
				html += "  <input type='hidden' value='0's name='ratePercent'/>";
				html += "    <h2 class='text-overflow'>"+d_rows[i].loanApplicationTitle+" </h2> ";
			//	console.log(stauts);
				html += "     <ol class='list'>";
				html += "  <li> <div> <p> ";
				html += "   <span>"+d_rows[i].annualRate+"</span>%  ";
				if (null != d_rows[i].rewardsPercent
						&& '0' != d_rows[i].rewardsPercent) {
					//rewards += "<span>加息</span>";
					rewards1 += "<i>+</i><span>" + d_rows[i].rewardsPercent
							+ "</span>%";
					html += " <span style='font-size:1rem;'>+"+d_rows[i].rewardsPercent+"</span>%     </p>年化利率 ";
				}
				var shan1=d_rows[i].cycleCount,shan2="个月";
				if (d_rows[i].loanType == 9) {//现金贷
			                    shan1=shan1*14;
			                    shan2="天";
				}
				html += "   </div</li> <li> <div><p><span>"+shan1+"</span>"+shan2+"</p>理财期限</div>  </li> ";
				html += "   <li > <div class='"+schedule+"'></div>  </li> </ol></li>";
				//thtml += makeConverHtml(arrary, index);
			//	console.log(html);
		}
		return html;
	}

	/*********王亚东中转站结束***************/

	/******省心计划组装html可以做成一个公用类，新手标，债权转让，定向标，省心计划*****/
/* 	function makeConverHtml(data) {
		//省心计划页面开始 data[7]
		var html = "";
			html += "  <li  onclick='toNormalDetail("+data[7]+")' datatype='amount'" + " class='l_titleIcon  " + data[4]
					+ "'";
			html += "    <h2 class='text-overflow'>"+data[3]+"</h2> ";
			html += "     <ol class='list'>";
			html += "  <li> <div> <p> ";
			html += "   <span>"+data[00]+"</span>%  ";
			html += " <span style='font-size:1rem;'>+1</span>%     </p>年化利率 ";
			html += "   </div</li> <li> <div><p><span>"+data[5]+"</span>个月</p>理财期限</div>  </li> ";
			html += "   <li > <div class="invest schedule"></div> </li> </ol></li>";

		return html;
	} */
	

    
})

 function toNormalDetail(id) {
	    var url ="/finance/bidding?loanApplicationNo="+id;
	    window.location.href=rootPath +url;
	}
	
	//公告滚动[开始]
	if(document.getElementById("jsfoot02")){
		var scrollup = new ScrollText("jsfoot02");
		scrollup.LineHeight = 35;        //单排文字滚动的高度
		scrollup.Amount = 1;            //注意:子模块(LineHeight)一定要能整除Amount.
		scrollup.Delay = 20;           //延时
		scrollup.Start();             //文字自动滚动
		scrollup.Direction = "up";   //默认设置为文字向上滚动
	}
	//公告滚动[结束]
	
	
</script>
</body>
</html>


