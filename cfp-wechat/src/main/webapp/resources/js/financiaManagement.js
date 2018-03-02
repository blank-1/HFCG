pushData(0);
var moveW = $(".w_page").width();
$(".regular").on("touchmove touchstart",function (e) {
	e.stopPropagation();
})
$("#w_vouList").on(
		"click",
		"li",
		function() {
			var thisIndex = $(this).index(), moveW = $(".w_page").width();
			pushData(thisIndex)
			if (thisIndex == 0) {
				window.location.href= rootPath + "/finance/list?tab=shengxin";
			}
			if (thisIndex == 1) {
				window.location.href= rootPath + "/finance/list?tab=biao";
			}
			if (thisIndex == 2) {
				window.location.href= rootPath + "/finance/list?tab=zhuang";
			}
		})

function pushData(ind) {

	var tab =$("#tab").val();
	if("shengxin"==tab){//省心计划
		ind=0; //0
	}else if(""==tab){// 散标
		ind=1; //1
	}else if("zhuang"==tab){//转让
		ind=2; //2
	}
	$(".page" + ind).on("scroll",function(event) {
						var scrollTop = $(this).find("ul").outerHeight(true),
							selfH = $(this).scrollTop()+ $(window).height()-($(".w_header").outerHeight(true)+$(".w_footer").outerHeight(true)+$(".l_lastTip").outerHeight(true))+3;
						if (scrollTop<= selfH) {
							//分页数据
							if (ind == "0") {//省心计划
								if (pageNo <= totalPage) {
									postConverHtml(pageNo, pageSize);
								}
							} else if (ind == "1") {//金英标
								if (pageNoLoan <= totalPageLoan) {
									postLoanHtml(pageNoLoan, pageSizeLoan);
								}
							} else {//债权转让
								 
								if (pageNoTransfer <= totalPageTransfer) {
									postTransferHtml(pageNoTransfer,
											pageSizeTransfer);
								}

							}

						}
					})
}
//console.log("初始化");



/** ****数据加载页码等初始化条件省心计划 王亚东******** */
var pageNo = 1, pageSize = 10, totalPage, totalPageRight, totalPageNew;//省心计划
/** ****数据加载页码等初始化条件精英标 王亚东******** */
var pageNoLoan = 1, pageSizeLoan = 10, totalPageLoan;//精英标
/** ****数据加载页码等初始化条件债权转让 王亚东******** */
var pageNoTransfer = 1, pageSizeTransfer = 10, totalPageTransfer;//精英标
var list = [];

/*********王亚东请求省心计划***************/
function postConverHtml(page, pageSize) {
    if(pageNo>1){
        $(".page0").css({
            "overflow":"hidden"
        })
    }else{
        $(".page0").css({
            "overflow":"auto"
        })
    }
	$(".loading_mask").show();
	$.ajax({
		url : rootPath + "/finance/lendList",
		type : "post",
		data : {
			"pageSize" : pageSize,
			"pageNo" : page
		},
		success : function(data) {
            var dataText = "	 <p id ='sxId' class='l_lastTip'>";
            var dataText1 = "";
            totalPage = data.totalPage;//总页数
            if (totalPage == pageNo) {
                dataText1 = "数据已加载完成</p>";
            } else {
                dataText1 = "向下滑动加载更多</p>";
            }
            $("#sxId").remove();
            $(".pay_page0").append(
                makeHtmlByNearMake(data, 0) + dataText + dataText1);
            AnimationLoading($(".page" + $(".w_active").parent("li").index()));
            pageNo = pageNo + 1;
			setTimeout(function () {
                $(".loading_mask").hide();
                $(".page0").css({
                    "overflow":"auto"
                })
            },1200);
		}
	});
}
/*********王亚东请求省心计划结束***************/

/*********王亚东请求精英标计划***************/
function postLoanHtml(pageNoLoan2, pageSizeLoan2) {
	if(pageNoLoan>1){
        $(".page1").css({
            "overflow":"hidden"
        })
	}else{
        $(".page1").css({
            "overflow":"auto"
        })
	}
    $(".loading_mask").show();
	$.ajax({
		url : rootPath + "/finance/loanList",
		type : "post",
		data : {
			"pageSize" : pageSizeLoan2,
			"pageNo" : pageNoLoan2
		},
		success : function(data) {
            var dataText = "	 <p id ='jybId' class='l_lastTip'>";
            var dataText1 = "";
            totalPageLoan = data.totalPage;//总页数
            if (totalPageLoan == pageNoLoan2) {
                dataText1 = "数据已加载完成</p>";
            } else {
                dataText1 = "向下滑动加载更多</p>";
            }
            totalPageLoan = data.totalPage;
            $("#jybId").remove();
            $(".pay_page1").append(
                makeHtmlByNearMake(data, 1) + dataText + dataText1);
            AnimationLoading($(".page" + $(".w_active").parent("li").index()));
            pageNoLoan = pageNoLoan + 1;
			setTimeout(function () {
                $(".loading_mask").hide();
                $(".page1").css({
                    "overflow":"auto"
                })
            },1200)
		}
	});
}
/*********王亚东请求精英标结束***************/

/*********王亚东请求债权转让*****
 * var pageNoTransfer = 1, pageSizeTransfer = 10, totalPageTransfer;//精英标
 * 
 * **********/
function postTransferHtml(pageNoLoan, pageSizeLoan) {
    if(pageNoTransfer>1){
        $(".page2").css({
            "overflow":"hidden"
        })
    }else{
        $(".page2").css({
            "overflow":"auto"
        })
    }
    $(".loading_mask").show();
	$.ajax({
		url : rootPath + "/finance/creditorRightsList",
		type : "post",
		data : {
			"pageSize" : pageSizeTransfer,
			"pageNo" : pageNoTransfer
		},
		success : function(data) {
            var dataText = "	 <p id ='transferId' class='l_lastTip'>";
            var dataText1 = "";
            totalPageTransfer = data.totalPage;
            if (totalPageTransfer == pageNoTransfer) {
                dataText1 = "数据已加载完成</p>";
            } else {
                dataText1 = "向下滑动加载更多</p>";
            }
            if(0==data.rows.length){
                var noData ="<div class='noNum' style='display: block;'> ";
                noData+="<img src=' ";
                noData+=rootPath+"/images/l_no-data.png' />";
                noData+="<p>不好意思，目前没有"+$('.w_active').text()+"的标的了。去出借列表购买其他的标的吧</p>";
                noData+="</div>";
                $(".pay_page2").append(noData);
            }else{
                $("#transferId").remove();
                $(".pay_page2").append(
                    makeHtmlByNearMake(data, 2) + dataText + dataText1);
                AnimationLoading($(".page" + $(".w_active").parent("li").index()))
                pageNoTransfer=pageNoTransfer+1;
            }
			setTimeout(function () {
                $(".loading_mask").hide();
                $(".page2").css({
                    "overflow":"auto"
                })
            },1200)

			
		}
	});
}
/*********王亚东请求债权转让结束***************/

/*********王亚东中转站
 * *第一个数组是省心计划，第二个数组是精英标，第三个数组是 债权转让，第四个数组是新手标**
 * *************/
function makeHtmlByNearMake(data, index) {
	var thtml = "";
	d_rows = data.rows;//列表数据
	totalPage = data.totalPage;//总页数
	total = data.total;
	var iWidth = 0;
	for (var i = 0; i < d_rows.length; i++) {
		var arrary = [];
		if ("0" == index) {//省心计划
			var schedule = "";
			var banlance = Math.round(parseInt(d_rows[i].soldBalance) * 100
					/ parseInt(d_rows[i].publishBalance));
			if (((d_rows[i].publishBalanceType == '2' && banlance >= 100) || d_rows[i].publishState != '2')
					&& d_rows[i].publishState == '3') {//已满额
				schedule = " filled schedule " + i;
			} else if (d_rows[i].publishState == '4') {//已完成
				schedule = "  hasdone schedule " + i;
			} else if (d_rows[i].publishState == '2') {//立刻投资
				schedule = " invest schedule " + i;
			} else {
				schedule = " preheat schedule " + i;//预热中
			}
			var recomend = "";
			if (d_rows[i].recommend == '1')//true 为推荐
				recomend = "<span>推荐</span>";
			arrary.push(banlance);//进度
			arrary.push(schedule);//省心计划状态
			arrary.push(recomend);//是否推荐
			arrary.push(d_rows[i].publishName);//名称
			arrary.push("  <p><span>" + d_rows[i].profitRate
					+ "</span>%<i>-</i><span>" + d_rows[i].profitRateMax
					+ "</span>%</p>   <p>预期年化收益</p> </div>");//年化收益区间1
			arrary.push(d_rows[i].timeLimit);//理财期限
            arrary.push(d_rows[i].lendProductPublishId);//理财期限
			thtml += makeConverHtml(arrary, 0);
		} else if ("1" == index||"2"==index) {//精英标 债权转让
			arrary = [];
			var schedule = "";
			
			var stauts = "";
			if (d_rows[i].loanType == 0 || d_rows[i].loanType == 3) {//信贷和企业信贷
				stauts = "l_titleIcon_belive";
			} else if (d_rows[i].loanType == 1 || d_rows[i].loanType == 7) {//1 房贷 个人房产直托
				stauts = "l_titleIcon_apartment";
			} else if (d_rows[i].loanType == 2 ||d_rows[i].loanType == 8 ) {//2 企业车贷和个人车贷
				stauts = "l_titleIcon_car";
			} else if (d_rows[i].loanType == 4) {//2 企业包里
				stauts = "l_titleIcon_baoli";
			} else if (d_rows[i].loanType == 5) {// 基金
				stauts = "l_titleIcon_invest";
			} else if (d_rows[i].loanType == 6) {//企业标
				stauts = "l_titleIcon_company";
			} else if (d_rows[i].loanType == 9) {//现金贷
                stauts = "l_titleIcon_xianjin";
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
			var rewards = "";
			var rewards1 = "<p><span>" + d_rows[i].annualRate + "</span>% ";
			if (null != d_rows[i].rewardsPercent
					&& '0' != d_rows[i].rewardsPercent) {
				rewards += "<span>加息</span>";
				rewards1 += "<i>+</i><span>" + d_rows[i].rewardsPercent
						+ "</span>%";
			}

			var process = 0;
			
			if("2"==index){
				var ratePercent = ( d_rows[i].totalAmountOfLoan /  d_rows[i].applyPrice) * 100 + "";
				process = ratePercent.indexOf(".") != -1 ? ratePercent.substring(0, ratePercent.indexOf(".") + 3) : ratePercent;
				schedule = " invest schedule " ;//已满额
				if("100"==process){//满标
					schedule = " filled schedule " ;//已满额
				}
			}else{
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
							schedule = "  preheat  schedule " ;//预热
						}else{
							schedule = " invest schedule " ;//投资
						}
					}
				
				
			}
			arrary.push(process);//进度
			arrary.push(schedule);//是否满标 是否预热中 立即投资则没有这两种状态
			arrary.push(rewards + orign);//奖励 没有奖励则显示正常收益率 和定向标展示
			arrary.push(d_rows[i].loanApplicationTitle);//名字
			arrary.push(stauts);//标的类型 stauts
			arrary.push(rewards1 + " <p>预期年化收益</p></div>");////借款期限
			arrary.push(d_rows[i].cycleCount);//名字
            if("1" == index){
                arrary.push(d_rows[i].loanApplicationId);
            }else{
                arrary.push(d_rows[i].creditorRightsApplyId);
            }
			thtml += makeConverHtml(arrary, index);
		}

	}
	return thtml;
}

/*********王亚东中转站结束***************/

/******省心计划组装html可以做成一个公用类，新手标，债权转让，定向标，省心计划*****/
function makeConverHtml(data, index) {
	//省心计划页面开始
	var html = "";
	if(data[0]>=100)
		data[0]=0;
	if (0 == index) {
		html += "  <li datatype='amount' class='l_titleIcon l_titleIcon_shengxin'>";
		html += "  <dl><dd class='head'><div class='title'>" + data[3]
				+ "</div>" + data[2] + "</dd>";
		html += "  <dt class='foot' onclick='toSxDetail("+data[6]+")'> <div class='chunk expect'>";
		html += data[4];
		html += "  <div class='chunk Month'><p><span>" + data[5]
				+ "</span>个月</p>     <p>理财期限</p>  </div>";
		html += "  <div class='Speed'>";
		html += "  <div class='" + data[1] + "' data-val='" + data[0]
				+ "'></div> ";
		html += "   </div> </dt></dl> </li>";
	} else if (1 == index) {//精英标
		html += "  <li datatype='amount'" + " class='l_titleIcon  " + data[4]
				+ "'";
		html += "  <dl><dd class='head'><div class='title'>" + data[3]
				+ "</div>" + data[2] + "</dd>";
		html += "  <dt class='foot' onclick='toNormalDetail("+data[7]+")'> <div class='chunk expect'>";
		html += data[5];
		var shan1=data[6],shan2="个月";
		if(data[4]=="l_titleIcon_xianjin"){
	            shan1=shan1*14;
	            shan2="天";
		}
		html += "  <div class='chunk Month'><p><span>" + shan1
				+ "</span>"+shan2+"</p>     <p>理财期限</p>  </div>";
		html += "  <div class='Speed'>";
		html += "  <div class='" + data[1] + "' data-val='" + data[0]
				+ "'></div> ";
		html += "   </div> </dt></dl> </li>";
	} else {//债权转让
		html += "  <li datatype='amount' class='l_titleIcon "+data[4]+"'>";
		html += "  <dl><dd class='head'><div class='title'>" + data[3]
				+ "</div>" + data[2] + "</dd>";
		html += "  <dt class='foot' onclick='toCreditorRightsDetail("+data[7]+")'> " +
				"	<div class='chunk expect'>"+ data[5];
		html += "  <div class='chunk Month'><p><span>" + data[6]
				+ "</span>个月</p>     <p>理财期限</p>  </div>";
		html += "  <div class='Speed'>";
		html += "  <div class='" + data[1] + "' data-val='" + data[0]
				+ "'></div> ";
		html += "   </div> </dt></dl> </li>";
	}

	return html;
}
/*****省心计划组装html可以做成一个公用类，新手标，债权转让，定向标，省心计划结束***/

function toSxDetail(id) {
    var url ="toSxDetail?lendProductPublishId="+id;
    window.location.href=url;
}

function toNormalDetail(id) {
    var url ="bidding?loanApplicationNo="+id;
    window.location.href=url;
}

function toCreditorRightsDetail(id) {
    var url ="creditRightBidding?creditorRightsApplyId="+id;
    window.location.href=url;
}

$(function() {
	//请求数据
	var tab =$("#tab").val();
	if("shengxin"==tab){//省心计划
		postConverHtml(pageNo, pageSize);
		$(".orderList").animate({
			"margin-left" : 0 * moveW + "px"
		}, 300)
		$("#li1").addClass("w_active").siblings().removeClass("w_active");
	}else if(""==tab){// 散标
		postLoanHtml(pageNoLoan, pageSizeLoan);//精英标
		$(".orderList").animate({
			"margin-left" : -1 * moveW + "px"
		}, 300)
		$("#li2").addClass("w_active").siblings().removeClass("w_active");
	}else if("zhuang"==tab){//转让
		postTransferHtml(pageNoTransfer, pageSizeTransfer);
		$(".orderList").animate({
			"margin-left" : -2 * moveW + "px"
		}, 300)
		$("#li3").addClass("w_active").siblings().removeClass("w_active");
	}
		
	

})

function AnimationLoading(pages) {
    var lis = pages.find("li");
    for (var i = 0; i < lis.length; i++) {
        var div = $(lis[i]).find("div.schedule"), divVal = div.attr("data-val");
        if(divVal==null||divVal==undefined){
            divVal=0;
        }
        div.find("canvas").remove();
        var radialObj = $(div).radialIndicator({
            barWidth : 10,
            barColor : "#FF5E61",
            barBgColor : "#efefef",
            displayNumber : false
        }).data('radialIndicator');
        radialObj.animate(Math.floor(divVal));
    }
}
