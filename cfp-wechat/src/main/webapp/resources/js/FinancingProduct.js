var pageNo = 1, pageSize = 10, totalPage, totalPageRight,totalPageNew; 
var flag = $("#flagCustomer").val();
$(document).ready(function() {
		

	if (tab == 'heng') {
		$('.lefttab').removeClass('borderh');
		$('.righttab').addClass('borderh');
		$('.lcbbox').css('display', 'block');
		$('.jybbox').css('display', 'none');
		$('.lcb').css('font-weight', 'bolder');
		$('.jyb').css('font-weight', 'normal');
		$('.xsjh').css('font-weight', 'normal');
		$(".xsjh").removeClass("xsjhtab");
		$(".xsjh").css("color","#afafaf");
		$(".content").css("display","none");
		searchRightHtml(1, 10);
	} else if (tab == 'shengxin') {
     	$(".lefttab").css("border-bottom","none");
     	$(".righttab").css("border-bottom","none");
     	$(".jyb").css("font-weight","normal");
     	$(".jyb").css("color","#afafaf");
     	$(".newbidbox").css("display","block");
		
		searchHtmlNewBidPlan(1,10);
	} else {
		$('.jybbox').css('display', 'block');
		$('.lcbbox').css('display', 'none');
		$('.lcb').css('font-weight', 'normal');
		$('.jyb').css('font-weight', 'bolder');
		$('.xsjh').css('font-weight', 'normal');
		$(".lcb").removeClass("lefttab");
		$(".lcb").removeClass("borderh");
		$(".xsjh").removeClass("xsjhtab");
		$(".lcb").css("color","#afafaf");
		$(".xsjh").css("color","#afafaf");
		$(".content").css("display","none");
		searchHtml(1, 10);
	}

	if (flag != null && flag != "" && flag == "new") {
		$("#jybbox").css("display", "none");
	}else{
		$("#newbidbox").css("display", "none");
	} 	
			

	/*is_weixn();*/

	
});

function a() {
	window.location.href = rootPath + "/finance/list?tab=heng";

};
function b() {
	window.location.href = rootPath + "/finance/list";

};

function c() {//省心计划tab
	//perAnimate();
	window.location.href = rootPath + "/finance/list?tab=shengxin";
	

};



// 出借列表查询
function searchHtml(page, rows) {
	if (flag != null && flag != "" && flag == "new") {
		return false;
	}

	var thtml = "";
	$
			.ajax({
				url : rootPath + "/finance/loanList",
				type : "post",
				data : {
					"pageSize" : rows,
					"pageNo" : page
				},
				success : function(data) {
					var d_rows = data.rows;
					var pageCount = data.totalPage;
					totalPage = data.totalPage;
					pageNo = data.currentPage;
					var total = data.total;
					for (var i = 0; i < d_rows.length; i++) {
						var data = d_rows[i];
						// 奖励
						var reward = data.rewardsPercent;
						if (reward != null && reward != '0') {// 有奖励的
							reward = '<acronym>' + data.annualRate
									+ '</acronym>%+<acronym>' + reward
									+ '</acronym>%';
							thtml += '<div class="project reward ';
						} else {// 无奖励的
							reward = '<acronym>' + data.annualRate
									+ '</acronym>%';
							thtml += '<div class="project ';
						}

						// 【定向标】如果为定向用户标，添加定向图标样式
						if (data.oType == 2 || data.oType == 1) {
							thtml += 'DirectionalBid">';
						} else if (data.oType == 3) {
							thtml += 'DirectionalBidNew">';
						} else {
							thtml += '">';
						}
						if (data.loanType == 0 || data.loanType == 3) {
							thtml += '<p class="title l_titleIconCompany">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 1 || data.loanType == 7) {
							thtml += '<p class="title l_titleIconHouse">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 2) {
							thtml += '<p class="title l_titleIconCar">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 4) {
							thtml += '<p class="title l_titleIconFactoring">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 5) {
							thtml += '<p class="title l_titleIconFund">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 6) {
							thtml += '<p class="title l_icon_pledge">'
									+ data.loanApplicationTitle + '</p>';
						}

						thtml += '<div class="titinf"><span class="titinf2">借款金额&nbsp;'
								+ data.confirmBalance.toFixed(2)
								+ '元</span></div>'
								+ '<div class="lbox"><span>年化利率</span><p>'
								+ reward
								+ '</p></div>'
								+ '<div class="mbox"><span>借款期限</span><p><acronym>'
								+ data.cycleCount
								+ '</acronym>个月</p></div>'
								+ '<div class="rbox"><span>起投金额</span><p><acronym>100</acronym>元</p></div>';
						if (data.ratePercent == 100) {
							thtml += '<a class="pjbtn pjbtn2" href="'
									+ rootPath
									+ '/finance/bidding?loanApplicationNo='
									+ data.loanApplicationId + '">已满标</a>';
						} else {
							if (!data.begin) {
								thtml += '<a class="pjbtn pjbtn1" href="'
										+ rootPath
										+ '/finance/bidding?loanApplicationNo='
										+ data.loanApplicationId + '">预热中</a>';
							} else {
								thtml += '<a class="pjbtn" href="' + rootPath
										+ '/finance/bidding?loanApplicationNo='
										+ data.loanApplicationId + '">投资</a>';
							}
						}
						thtml += '</div>';
					}
					$(".l_lastTips").remove();
					$('.jybbox').append(thtml);
					var tips = $("<p>").addClass("l_lastTips").text(
							"向上滑动加载更多数据").appendTo($("form:visible"));
					if (total < 10 && total > 0) {
						var tips = $("<p>").addClass("l_lastTips").text(
								"这已经是全部数据了").appendTo($("form:visible"));
					}
					$('#financePlanbox').css('display','none');
					if (total == 0) {
						var listNoData = $("<img>").attr({
							"src" : "/cfp-wechat/images/icon_noData.png",
							"style" : "width:50%;margin:40% 0 8% 25%;"
						}).appendTo($("form:visible")), listNoDataInfo = $(
								"<p>")
								.text("暂时无数据，请稍后再来~")
								.attr("style",
										"width:100%;font-size:1.6rem;text-align:center;color:#afafaf;line-height:2rem;")
								.appendTo($("form:visible"));
					}
				}
			});
};

// 债权列表查询 wangyadong add
function searchRightHtml(page, rows) {
	if(flag!=null&&flag!=""&&flag=="new"){
		return false ;
	}
	var thtml = "";
	$
			.ajax({
				url : rootPath + "/finance/creditorRightsList",
				type : "post",
				data : {
					"pageSize" : rows,
					"pageNo" : page
				},
				success : function(data) {
					var d_rows = data.rows;
					var pageCount = data.totalPage;
					totalPageRight = data.totalPage;
					pageNo = data.currentPage;
					var totalRight = data.total;
					for (var i = 0; i < d_rows.length; i++) {
						var data = d_rows[i];
						// 奖励
						var reward = data.rewardsPercent;
					
						if (reward != null && data.awardPoint =='2') {// 有奖励的dddd 1= 2f
							reward = '<acronym>' + data.annualRate + '</acronym>%+<acronym>'
							+ reward + '</acronym>%';
					        thtml += '<div class="project reward ">';
						/*	reward = '<acronym>' + data.annualRate + '</acronym>%+<acronym>'
							+ reward + '</acronym>%';
							thtml += '<div class="project reward ';*/
						} else {// 无奖励的
							reward = '<acronym>' + data.annualRate + '</acronym>%';
							thtml += '<div class="project" >';
						}
						if (data.loanType == 0 || data.loanType == 3) {
							thtml += '<p class="title l_titleIconCompany">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 1 || data.loanType == 7) {
							thtml += '<p class="title l_titleIconHouse">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 2) {
							thtml += '<p class="title l_titleIconCar">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 4) {
							thtml += '<p class="title l_titleIconFactoring">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 5) {
							thtml += '<p class="title l_titleIconFund">'
									+ data.loanApplicationTitle + '</p>';
						}else if (data.loanType == 6) {
							thtml += '<p class="title l_icon_pledge">'
								+ data.loanApplicationTitle + '</p>';
						}
						thtml += '<div class="titinf"><span class="titinf2">剩余本金&nbsp;'
								+ data.whenWorth.toFixed(2)
								+ '元</span></div>'
								+ '<div class="lbox"><span>年化收益率</span><p>'
								+ reward
								+ '</p></div>'
								+ '<div class="mbox"><span>剩余期限</span><p><acronym>'
								+ data.cycleCount
								+ '</acronym>个月</p></div>'
								+ '<div class="rbox"><span>起投金额</span><p><acronym>100</acronym>元</p></div>';
						thtml += '<a class="pjbtn" href="'
								+ rootPath
								+ '/finance/creditRightBidding?creditorRightsApplyId='
								+ data.creditorRightsApplyId + '">投资</a>';
						thtml += '</div>';
					}
					$(".l_lastTips").remove(); 
				 
					$('.lcbbox').append(thtml);
					
//					var tips1 =	$('.lcbbox').find('.l_lastTips').text("向上滑动加载更多数据").appendTo($("form:visible"));
				var tips1 = $("<p>").addClass("l_lastTips").text("向上滑动加载更多数据").appendTo($("form:visible"));
				if (totalRight<10) {
						$(".l_lastTips").remove(); 
					}
				$('#financePlanbox').css('display','none');
				if (totalRight == 0){
					var listNoData = $("<img>").attr({"src":"/cfp-wechat/images/icon_noData.png","style":"width:50%;margin:40% 0 8% 25%;"}).appendTo($("form:visible")),
						listNoDataInfo = $("<p>").text("现在还没有转让债权~").attr("style","width:100%;font-size:1.6rem;text-align:center;color:#afafaf;line-height:2rem;").appendTo($("form:visible"));	
					}
				}
			});
};





 
$(".l_NewScroll").on("scroll load",function(){
		var MainBoxH = $("form:visible").outerHeight() ,
		scrollTop = $(window).scrollTop() + $(window).height(),
		bodyHeight = $("section").height(),
		lastBox = $("form:visible>p").last(),
		firstBox = $("form:visible>div").first(),
		lastBoxDis =Math.floor(lastBox.offset().top) + lastBox.height()-1; 
//		console.log(lastBoxDis);
//		console.log(bodyHeight);
	//判断当页面滑动到最底部
	if ( lastBoxDis <= bodyHeight) {
		
			if (totalPage >= pageNo && $("#jybbox").is(":visible")) {
				pageNo += 1;
				searchHtml(pageNo, 10);	
			}else if(totalPageRight>=pageNo && $("#lcbbox").is(":visible")){ 
				pageNo += 1;
			 searchRightHtml(pageNo,10);
			}else if(totalPage>=pageNo && $("#newbidbox").is(":visible")){
				pageNo += 1;
				searchHtmlSpecial(pageNo,10);
			}else if(totalPage>=pageNo && $("#financePlanbox").is(":visible")){
				
				pageNo += 1;
				searchHtmlNewBidPlan(pageNo,10);
			}else{
			//	$(".l_lastTips").removeClass(); 
				$(".l_lastTips").html("这已经是全部数据了");
			}
			
	}
})
 



// 新手标列表查询
function searchHtmlSpecial(page, rows) {
	if(flag==null||flag==""||flag!="new"){
		return false ;
	}
	var thtml = "";
	$.ajax({
				url : rootPath + "/finance/loanSpecialList",
				type : "post",
				data : {
					"pageSize" : rows,
					"pageNo" : page
				},
				success : function(data) {
					var d_rows = data.rows;
					var pageCount = data.totalPage;
					totalPage = data.totalPage;
					pageNo = data.currentPage;
					var total = data.total;
					for (var i = 0; i < d_rows.length; i++) {
						var data = d_rows[i];
						// 奖励
						var reward = data.rewardsPercent;
						if (reward != null && reward != '0') {// 有奖励的
							reward = '<acronym>' + data.annualRate + '</acronym>%+<acronym>'
									+ reward + '</acronym>%';
							thtml += '<div class="project reward ';
						} else {// 无奖励的
							reward = '<acronym>' + data.annualRate + '</acronym>%';
							thtml += '<div class="project ';
						}

						// 【定向标】如果为定向用户标，添加定向图标样式
						if (data.oType == 2 || data.oType == 1) {
							thtml += 'DirectionalBid">';
						} else if(data.oType == 3){
							thtml += 'DirectionalBidNew">';
						}else {
							thtml += '">';
						}
						if (data.loanType == 0 || data.loanType == 3) {
							thtml += '<p class="title l_titleIconCompany">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 1 || data.loanType == 7) {
							thtml += '<p class="title l_titleIconHouse">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 2) {
							thtml += '<p class="title l_titleIconCar">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 4) {
							thtml += '<p class="title l_titleIconFactoring">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 5) {
							thtml += '<p class="title l_titleIconFund">'
									+ data.loanApplicationTitle + '</p>';
						} else if (data.loanType == 6) {
							thtml += '<p class="title l_icon_pledge">'
								+ data.loanApplicationTitle + '</p>';
						}
						
						thtml += '<div class="titinf"><span class="titinf2">借款金额&nbsp;'
								+ data.confirmBalance.toFixed(2)
								+ '元</span></div>'
								+ '<div class="lbox"><span>年化利率</span><p>'
								+ reward
								+ '</p></div>'
								+ '<div class="mbox"><span>借款期限</span><p><acronym>'
								+ data.cycleCount
								+ '</acronym>个月</p></div>'
								+ '<div class="rbox"><span>起投金额</span><p><acronym>100</acronym>元</p></div>';
						if (data.ratePercent == 100) {
							thtml += '<a class="pjbtn pjbtn2" href="'
									+ rootPath
									+ '/finance/bidding?loanApplicationNo='
									+ data.loanApplicationId + '">已满标</a>';
						} else {
							if (!data.begin) {
								thtml += '<a class="pjbtn pjbtn1" href="'
										+ rootPath
										+ '/finance/bidding?loanApplicationNo='
										+ data.loanApplicationId + '">预热中</a>';
							} else {
								thtml += '<a class="pjbtn" href="' + rootPath
										+ '/finance/bidding?loanApplicationNo='
										+ data.loanApplicationId + '">投资</a>';
							}
						}
						thtml += '</div>';
					}
					$('#financePlanbox').css('display','none');
					$(".l_lastTips").remove(); 
					$("#newbidbox").append(thtml);
//				 	alert("ddd"+	$("#newbidbox").children(".DirectionalBidNew").length);
//					$("#newbidbox").children(".DirectionalBidNew").length;
					if (total == $("#newbidbox").children(".DirectionalBidNew").length ) {
						 $("<p>").addClass("l_lastTips").text("这已经是全部数据了").appendTo($("form:visible"));
					}
					if($("#newbidbox").children(".DirectionalBidNew").length<total){
						 $("<p>").addClass("l_lastTips").text("向上滑动加载更多数据").appendTo($("form:visible"));
					}
					if(pageNo==1&&d_rows.length==0){
						$(".l_lastTips").hide();
					}
					
				 
					if (total == 0){
					var listNoData = $("<img>").attr({"src":"/cfp-wechat/images/icon_noData.png","style":"width:50%;margin:40% 0 8% 25%;"}).appendTo($("form:visible")),
						listNoDataInfo = $("<p>").text("暂时无数据，请稍后再来~").attr("style","width:100%;font-size:1.6rem;text-align:center;color:#afafaf;line-height:2rem;").appendTo($("form:visible"));	
					}
				}
			});
};

if(flag!=null&&flag!=""&&flag=="new"){
	  $(".l_bottomTap").hide();
	  $(".tab").hide();
	  $(".l_NewScroll").css({
		  "height":"100%"
	  })
	    $("#jybbox").css("display","none");
	  searchHtmlSpecial(1,10);
	} 


//var slistMain = $(".slistMain").children().length;
//if(parseInt(slistMain)<=parseInt('0')){
//	("s_lastTips").text("暂无数据，请稍后回来...")
//}



//省心计划列表查询
function searchHtmlNewBidPlan(page, rows) {

	 $.ajax({
				url : rootPath + "/finance/lendList",
				type : "post",
				data : {
					"pageSize" : rows,
					"pageNo" : page
				},
				success : function(data) {
					commonNewBid(data);
				}
			});
};
/**
 * 组装省心计划
 * @author wangyadong
 * @returns
 */
var total;
function commonNewBid(data){
	var thtml = "";
	var d_rows = data.rows;
	var pageCount = data.totalPage;
	totalPage = data.totalPage;
	//pageNo = data.currentPage;
	total = data.total;
	var arr =[""];
	var iWidth =0;
	for (var i = 0; i < d_rows.length; i++) {		
		var data = d_rows[i],
		
		    s = Math.round(parseInt(data.soldBalance)*100/parseInt(data.publishBalance));
		  //  s =	parseInt(data.soldBalance)*100/parseInt(data.publishBalance)
		// 奖励  data
		thtml +='<div class="peacePlanbox">'; 
			if(data.recommend=='1'){
				//推荐
				thtml+='<img src="'+rootPath+'/images/tuijian_icon.png" class="tuiJian"/>';
			}
		thtml+='<div class="listMain"> ';
		thtml+='<h2 class="box_title">'//标题h2开始
		thtml+=data.publishName;//标题
		thtml+='</h2>';	//标题h2结束
		thtml+='<div class="peacePlanmain">';
		thtml+='<div class="mainBoxleft">';
		thtml+='<p>';////收益率 区间 p 开始
		thtml+= data.profitRate+'<i>%</i>-'+data.profitRateMax+'<i>%</i>';//收益率 区间
		thtml+='</p>';//收益率 区间 p 结束
		thtml+='<p class="smainText">预期年化收益</p>';
		thtml+='</div>';
		thtml+='<div class="mainBoxcen">';
		thtml+='<p>';//时长p 开始
		thtml+='<i>'+data.timeLimit+'</i>个月';//时长
		thtml+='</p>';//时长p 结束
		thtml+='<p class="smainText">省心期</p>';
		thtml+='</div>';
		thtml+='<div class="mainBoxright">';
		thtml+='<button  onclick="changeUrl('+data.lendProductPublishId+')"'; //data.lendProductPublishId
		  if (((data.publishBalanceType == '2' && s >= 100) || data.publishState != '2')
				&& data.publishState == '3') {
			thtml += '  class="fullBtn">已满额</button>';
			// thtml += "<div class='s_shengxinButton_hui'><a
			// href='javascript:;' ><button
			// onclick='window.location=(\""+rootPath+"/finance/toFinanceDetail?lendProductPublishId="+pId+"\")'
			// >已满额</button></a></div></div>";
		} else if (data.publishState == '4') {
			thtml += ' class="fullBtn">已完成</button>';
			// thtml += "<div class='s_shengxinButton_hui'><a
			// href='javascript:;' ><button
			// onclick='window.location=(\""+rootPath+"/finance/toFinanceDetail?lendProductPublishId="+pId+"\")'
			// >已完成</button></a></div></div>";
		} else if (data.publishState == '2') {
			thtml += '>立即省心 </button>';
			// thtml += "<div class='s_shengxinButton'><a href='javascript:;'
			// ><button
			// onclick='window.location=(\""+rootPath+"/finance/toFinanceDetail?lendProductPublishId="+pId+"\")'
			// >立即加入</button></a></div></div>";
		} else if (data.publishState == '1') {
			thtml += '>预热中</button>';
			// thtml += "<div class='s_shengxinButton'><a href='javascript:;'
			// ><button
			// onclick='window.location=(\""+rootPath+"/finance/toFinanceDetail?lendProductPublishId="+pId+"\")'
			// >预热中</button></a></div></div>";
		}
		thtml+='</div>';
		thtml+='</div>';
		thtml+='<p>';
		thtml+='<span class="s_perLine" ';  // 进度
		if (data.publishBalanceType != '2') {
			//显示进度条
			thtml+='style="display:none"';
	    }
		iWidth+=i;
		thtml+=' > <i data-width="'+s+'"><b>%</b></i></span>';  // 进度
		thtml+='<div style="clear:both;"> </div>';	
		thtml+='</p>';
		thtml+='</div>';
		thtml+='</div>';
	}
	$(".s_lastTips").remove(); 
	$(".jybbox").css('display','none');
	$(".slistMain").append(thtml);
//	var tips = $("<p>").addClass("s_lastTips").text("向上滑动加载更多数据").appendTo($("form:visible"));
	$(".s_lastTips").remove(); 
	if ( total >0 ) {
	 $("<p>").addClass("s_lastTips").text("向上滑动加载更多数据").appendTo($("form:visible"));
	}
	if ( total ==$(".slistMain").children().length ) {
		 $(".s_lastTips").css("display","none");
		  $("<p>").addClass("s_lastTips").text("已加载全部数据").appendTo($("form:visible"));
		
	}
	
	if (total == 0){
	var listNoData = $("<img>").attr({"src":"/cfp-wechat/images/icon_noData.png","style":"width:50%;margin:40% 0 8% 25%;"}).appendTo($("form:visible")),
		listNoDataInfo = $("<p>").text("暂时无数据，请稍后再来~").attr("style","width:100%;font-size:1.6rem;text-align:center;color:#afafaf;line-height:2rem;").appendTo($("form:visible"));	
	}
//	perAnimate(50);
	$(".s_perLine:visible i").each(function(){
		$(this).width($(this).attr("data-width")+"%");
		$(this).find("b").text($(this).attr("data-width")+"%");
	})
}

function perAnimate(arr){
//  var sper = 90; ["1","2","3","4"]
//	for(int i =0;i<)
  $(".s_perLine>i").css("width",arr+"%");
  $(".s_perLine>i>b").html(arr+"%");
}

function changeUrl(urlId){
	var url ="toSxDetail?lendProductPublishId="+urlId;
	console.log(url);
	window.location.href=url;
//	$("#financePlanbox").sumbit();
	 
}


/*function is_weixn(){  
    var ua = navigator.userAgent.toLowerCase();  
    if(ua.match(/MicroMessenger/i)=="micromessenger") {  
        return true;  
    } else {  
    	$(".xsjh").hide();
    	$(".tab p").width(40+"%");
        return false; 
        
    }  
}  
*/

















