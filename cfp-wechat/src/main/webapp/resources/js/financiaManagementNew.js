pushData(1);

$(".regular").on("touchmove touchstart",function (e) {
    e.stopPropagation();
})
function pushData(ind) {
	$(".page" + ind).on("scroll",function() {
						var scrollTop = $(this).find("ul").outerHeight(true),
                            selfH = $(this).scrollTop()+ $(window).height();
							if (scrollTop <= selfH) {
								//分页数据


								if (pageNo <= totalPage) {
									postLoanHtml(pageNo, pageSize);
								}
							}
						}
					 )}

 
/** ****数据加载页码等初始化条件省心计划 王亚东******** */
var pageNo = 1, pageSize = 10, totalPage;//省心计划
 
 

 

/*********王亚东请求精英标计划***************/
function postLoanHtml(pageNoLoan, pageSizeLoan) {
	$.ajax({
		url : rootPath + "/finance/loanSpecialList",
		type : "post",
		data : {
			"pageSize" : pageSizeLoan,
			"pageNo" : pageNoLoan
		},
		success : function(data) {
			var dataText = "	 <p id ='jybId' class='l_lastTip'>";
			var dataText1 = "";
            totalPage = data.totalPage;//总页数
			if (totalPage == pageNo) {
				dataText1 = "数据已加载完成</p>";
			} else {
				dataText1 = "向下滑动加载更多</p>";
			}
			$("#jybId").remove();
			$(".pay_page1").append(
					makeHtmlByNearMake(data, 1) + dataText + dataText1);
			AnimationLoading($(".page1"));
            pageNo = pageNo + 1
		}
	});
}
/*********王亚东请求精英标结束***************/

 

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
		 
			arrary = [];
			var schedule = "";
			
			var stauts = "";
			if (d_rows[i].loanType == 0 || d_rows[i].loanType == 3) {//信贷和企业信贷
				stauts = "l_titleIcon_belive";
			} else if (d_rows[i].loanType == 1 || d_rows[i].loanType == 7) {//1 房贷 个人房产直托
				stauts = "l_titleIcon_apartment";
			} else if (d_rows[i].loanType == 2) {//2 企业车贷
				stauts = "l_titleIcon_car";
			} else if (d_rows[i].loanType == 4) {//2 企业包里
				stauts = "l_titleIcon_baoli";
			} else if (d_rows[i].loanType == 5) {// 基金
				stauts = "l_titleIcon_invest";
			} else if (d_rows[i].loanType == 6) {//企业标
				stauts = "l_titleIcon_company";
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
	return thtml;
}

/*********王亚东中转站结束***************/

/******省心计划组装html可以做成一个公用类，新手标，债权转让，定向标，省心计划*****/
function makeConverHtml(data, index) {
	//省心计划页面开始
	if(data[0]>=100)
		data[0]=0;
	var html = "";
        //精英标
		html += "  <li datatype='amount'" + " class='l_titleIcon  " + data[4]
				+ "'";
		html += "  <dl><dd class='head'><div class='title'>" + data[3]
				+ "</div>" + data[2] + "</dd>";
		html += "  <dt class='foot' onclick='toNormalDetail("+data[7]+")'> <div class='chunk expect'>";
		html += data[5];
		html += "  <div class='chunk Month'><p><span>" + data[6]
				+ "</span>个月</p>     <p>理财期限</p>  </div>";
		html += "  <div class='Speed'>";
		html += "  <div class='" + data[1] + "' data-val='" + data[0]
				+ "'></div> ";
		html += "   </div> </dt></dl> </li>";
	return html;
}
function toNormalDetail(id) {
    var url ="bidding?loanApplicationNo="+id;
    window.location.href=url;
}


$(function() {

	//请求数据
	postLoanHtml(pageNo, pageSize)

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