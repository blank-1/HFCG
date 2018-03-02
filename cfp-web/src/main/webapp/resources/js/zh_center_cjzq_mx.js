
//点击向上按钮展开
$(function(){
	$(document).on("click",".pre_ulsmall li",function(){
		if($(this).parents("ul.pre_ulsmall").find("li:last i").hasClass("up")){
			$(this).parent().parent("li").next().slideDown(500,function(){bottomB();}).siblings(".div_show").slideUp(500,function(){bottomB();});
			$(this).parents("ul.pre_ulsmall").find("li:last i").removeClass("up").addClass("down");

		}else{
			$(this).parent().parent("li").next().slideUp(500,function(){bottomB();}).siblings(".div_show").slideUp(500,function(){bottomB();});
			$(this).parents("ul.pre_ulsmall").find("li:last i").removeClass("down").addClass("up");
		}

	});
	$("a.c").click(function(){
		$(this).addClass('a-head').siblings().removeClass("a-head");
		init();
	})
	
	//排序
	$(".th-sorts  a.zqsort").click(function(){
		if($(this).hasClass("mr")){
			$(this).addClass("a-head");
			$(this).siblings('a.zqsort').removeClass("a-midden-up").removeClass("a-midden-down").attr("data-value",0);
		}else{
			$(this).siblings("a.mr").removeClass("a-head");
			var _this=$(this);
			var fin_up=_this.hasClass("a-midden-up");
			var fin_down=_this.hasClass("a-midden-down");
			if(fin_down){
				_this.removeClass("a-midden-up").removeClass("a-midden-down");
				_this.attr("data-value",0);
			}else if(!fin_up){
				_this.removeClass("a-midden-down").addClass("a-midden-up");
				_this.attr("data-value",1);
			}else if(!fin_down){
				_this.removeClass("a-midden-up").addClass("a-midden-down");
				_this.attr("data-value",2);
			}
			
		}
		searchHtml(1, 10);
	});
})

	
//多选框效果
	function selectAll(checkbox) {
		$('input[class=c]').prop('checked', $(checkbox).prop('checked'));
		if(!checkbox.checked){
			$('input[class=c]').eq(0).prop('checked',true);
		}
		init();
	};
	function selectAlls(checkbox) {
		$('input[class=d]').prop('checked', $(checkbox).prop('checked'));
		if(!checkbox.checked){
			$('input[class=d]').eq(0).prop('checked',true);
		}
	};
	$(function(){
		function checkp(xuan1){
			var falg=0;
			xuan1.each(function(){
				if($(this).is(":checked")){
					falg+=1;
				}
			});
			if(falg>0) return true; else return false;
		}
		$('.xuan .c').click(function(){
			if(!$(this).is(":checked")){
				$(".xuan").find("input[type=checkbox]").eq(0).prop("checked",false);
			}
			if(!checkp($('.xuan .c'))){
				$(this).prop("checked",true);
			}
		});
		$('.xuan2 .d').click(function(){
			if(!$(this).is(":checked")){
				$(".xuan2").find("input[type=checkbox]").eq(0).prop("checked",false);
			}
			if(!checkp($('.xuan2 .d'))){
				$(this).prop("checked",true);
			}
		});
	})
	//排序的箭头
	$(function(){

		$(".xiala").click(function(){
			var lengthF=$(this).css("background-image").length;
			if($(this).css("background-image").substring(lengthF-7,lengthF-5).indexOf("3")=="-1"){
				if($(this).hasClass("xiala6")){
					
					$(this).removeClass("xiala6").removeClass("xiala7");
					$(this).attr("data-value",0);
					
				}else{
					
					$(this).addClass("xiala7").removeClass("xiala6");
					$(this).attr("data-value",1);
				}
			}else{
					$(this).addClass("xiala6").removeClass("xiala7");
					$(this).attr("data-value",2);
			}

			financeHtml(1,10,jhlist,ylist);
		});
	})
	//排序的箭头end	
	function init(){
		financeHtml(1,10,jhlist,ylist);
	}
	$(document).ready(function() {
		//console.log($("input[name=zt][checked=checked]").length)
		init();
	})
//声明出借债权明细列表
var jhlist=new Array();
//出借列表详细
var ylist=new Array();

//排序方法
function orderBy(v){
	if(v=='1')
		return "A";
	if(v=='2')
		return "D";
	if(v=='0')
		return null;
}
//查询出借债权明细
function financeHtml(page,rows,flist,ylist){
	flist = new Array();
	ylist=new Array();
	$("#myCreditRight").html("");
	$("#myCreditRight").html('<div class="tcdPageCode"></div>');
	//组织参数
	var creditorRightsStatus = "";
	$("a[name=zt]").each(function(){
		if($(this).hasClass("a-head"))
			creditorRightsStatus+=$(this).attr("value")+"-"
	});
	creditorRightsStatus = creditorRightsStatus.substring(0,creditorRightsStatus.lastIndexOf("-"));

	var backDate =  $("#backDate").attr("data-value");
	var buyDate =  $("#buyDate").attr("data-value");


	$.ajax({
		url:rootPath+"/finance/getCreditRightList",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"creditorRightsStatus": creditorRightsStatus,
			"backDate": orderBy(backDate),
			"buyDate":  orderBy(buyDate),
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;

			for(var i=0;i<d_rows.length;i++){
				var data = d_rows[i];
				var list = data.rightsRepaymentDetailList;
				flist[i]=[
					data.creditorRightsName,
					data.loanRealName,
					fmoney(data.buyPrice,2),
					fmoney(data.waitTotalpayMent,2),
					fmoney(data.factBalance,2),
					getDateStr(new Date(data.currentPayDate)),
					showCreditoRightStatus(data.rightsState),
					showTurnStatus(data.turnState),
					getDateStr(new Date(data.buyDate)),
					"<img src='../images/up.jpg' />",
					"index.do?financeId=1",
					data.loanApplicationListVO,
					fmoney(data.expectProfit,2),
					data.creditorRightsId,
					data.awardPoint,
					data.awardRate,
					data.rightsBtn,
					data.fromWhere,
					data.creditorRightsApplyId,
					data.rateList
				];
				ylist[i] = new Array();
                //提前还款 当期标记
                var tag = 0;
                //后期累计回款本金
                var ljAmount = 0;
				for(var j=0;j<list.length;j++){
                    if(list[j].rightsDetailState == '4'){
                        ljAmount = ljAmount + list[j].shouldCapital2;
                    }else{
                        tag = j;
                    }
                }
                for (var j = 0; j < list.length; j++) {
					var detail = list[j];
					var capital = fmoney(detail.shouldCapital2,2);
					var interest = fmoney(detail.shouldInterest2,2);
					var dInterest = fmoney(detail.defaultInterest,2);
					var fee = fmoney(detail.shouldFee,2);
                    if(detail.rightsDetailState == '4'){
                        fee = "---";
                    }
					var allBackMoney =  fmoney(detail.shouldCapital2+detail.shouldInterest2+detail.defaultInterest,2);
					var factMoney = fmoney(detail.factBalance+detail.depalFine,2);
                    if(detail.rightsDetailState == '4'){
                        factMoney = "---";
                    }else{
                        if(tag == j){
                            factMoney = fmoney(detail.factBalance + ljAmount + detail.depalFine, 2);
                        }
                    }
					ylist[i][j] = [detail.sectionCode,getDateStr(new Date(detail.repaymentDayPlanned)),capital,interest,dInterest,fee,allBackMoney,factMoney,repaymentDetailStatus(detail.rightsDetailState),"index.do?financeId=1"];
				}

			}

			//页面渲染
			render(flist,ylist);

			if(d_rows.length>0){
				$(".tcdPageCode").createPage({
					pageCount:pageCount,
					current:1,
					backFn:function(p){
						//点击分页效果
						searchHtml(parseInt(p),rows,flist,ylist);
					}
				});
			}else{
				$(".tcdPageCode").createPage({
					pageCount:pageCount,
					current:1,
					backFn:function(p){
						//点击分页效果
						searchHtml(parseInt(p),rows,flist,ylist);
					}
				});
			}
			bottomB();
		}
	});

}

function repaymentDetailStatus(v){
	switch (v) {
		case '0':
			return "未还款";
			break
		case '1':
			return "部分还款";
			break
		case '2':
			return "已还清";
			break
		case '3':
			return "逾期";
			break
	        case '4':
	            return "提前结清";
			break
		case '5':
			return "已转出";
			break
		case '6':
			return "平台垫付利息";
			break
		case '8':
			return "转让中";
			break
	}

}

function paymentMethod(v){
	switch (v) {
		case '0':
			return "等额本息";
			break
		case '1':
			return "等额本金";
			break
	}
}

function showTurnStatus(v) {
	switch (v) {
		case '0':
			return "未申请";
			break
		case '1':
			return "申请转出";
			break
		case '2':
			return "已转让";
			break
	}
}

function showCreditoRightStatus(v) {
	switch (v) {
		case '0':
			return "已生效";
			break
		case '1':
			return "还款中";
			break
		case '2':
			return "已转出";
			break
		case '3':
			return "已结清";
			break
		case '4':
			return "已删除";
			break
		case '5':
			return "申请转出";
			break
		case '6':
			return "已转出(平台垫付)";
			break
		case '7':
			return "提前结清";
			break
		case '8':
			return "转让中";
			break
	}
}
function searchHtml(page,rows,flist,ylist){
	flist = new Array();
	ylist=new Array();
	//组织参数
//	var creditorRightsStatus = "";
//	$("input[name=zt]").each(function(){
//		if(this.checked)
//			creditorRightsStatus+=$(this).val()+"-"
//	});
//	creditorRightsStatus = creditorRightsStatus.substring(0,creditorRightsStatus.lastIndexOf("-"));
	//组织参数
	var creditorRightsStatus = "";
	$("a[name=zt]").each(function(){
		if($(this).hasClass("a-head"))
			creditorRightsStatus+=$(this).attr("value")+"-"
	});
	creditorRightsStatus = creditorRightsStatus.substring(0,creditorRightsStatus.lastIndexOf("-"));

	var backDate =  $("#backDate").attr("data-value");
	var buyDate =  $("#buyDate").attr("data-value");

	$.ajax({
		url:rootPath+"/finance/getCreditRightList",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"creditorRightsStatus": creditorRightsStatus,
			"backDate": orderBy(backDate),
			"buyDate":  orderBy(buyDate),
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;

			for(var i=0;i<d_rows.length;i++){
				var data = d_rows[i];
				var list = data.rightsRepaymentDetailList;
				flist[i]=[
					data.creditorRightsName,
					data.loanRealName,
					fmoney(data.buyPrice,2),
					fmoney(data.waitTotalpayMent,2),
					fmoney(data.factBalance,2),
					getDateStr(new Date(data.currentPayDate)),
					showCreditoRightStatus(data.rightsState),
					showTurnStatus(data.turnState),
					getDateStr(new Date(data.buyDate)),
					"<img src='../images/up.jpg' />",
					"index.do?financeId=1",
					data.loanApplicationListVO,
					fmoney(data.expectProfit,2),
					data.creditorRightsId,
					data.awardPoint,
					data.awardRate,
					data.rightsBtn,
					data.fromWhere,
					data.creditorRightsApplyId,
					data.rateList
				];
				ylist[i] = new Array();
				  //提前还款 当期标记
                var tag = 0;
                //后期累计回款本金
                var ljAmount = 0;
				for(var j=0;j<list.length;j++){
                    if(list[j].rightsDetailState == '4'){
                        ljAmount = ljAmount + list[j].shouldCapital2;
                    }else{
                        tag = j;
                    }
                }
                for (var j = 0; j < list.length; j++) {
					var detail = list[j];
					var capital = fmoney(detail.shouldCapital2,2);
					var interest = fmoney(detail.shouldInterest2,2);
					var dInterest = fmoney(detail.defaultInterest,2);
					var fee = fmoney(detail.shouldFee,2);
                    if(detail.rightsDetailState == '4'){
                        fee = "---";
                    }
					var allBackMoney =  fmoney(detail.shouldCapital2+detail.shouldInterest2+detail.defaultInterest,2);
					var factMoney = fmoney(detail.factBalance+detail.depalFine,2);
                    if(detail.rightsDetailState == '4'){
                        factMoney = "---";
                    }else{
                        if(tag == j){
                            factMoney = fmoney(detail.factBalance + ljAmount + detail.depalFine, 2);
                        }
                    }
					ylist[i][j] = [detail.sectionCode,getDateStr(new Date(detail.repaymentDayPlanned)),capital,interest,dInterest,fee,allBackMoney,factMoney,repaymentDetailStatus(detail.rightsDetailState),"index.do?financeId=1"];
				}


			}

			//页面渲染
			render(flist,ylist);
			bottomB();
		}
	});

}
//出借债权明细 pre_ul_big end


function render(flist,ylist){
	var thtml="";
	for(var i=0;i<flist.length;i++){
		var award="";
		var awardRate="";
		var rateValue="";
		var rateValue2="";
			if(flist[i][14]!=undefined&&flist[i][14]!=""&&flist[i][14]!=null){
				award="<i class='borlisdeta2'>注</i>投标奖励金额于"+flist[i][14]+"时，发放至您的账户，请注意查收。";
			}
			if(flist[i][15]!=undefined&&flist[i][15]!=""&&flist[i][15]!=null&&parseFloat(flist[i][15])>0){
				awardRate=flist[i][15]+"<i class='borlisdeta'>注</i>";
			}
		if (flist[i][19] != undefined && flist[i][19] != null && flist[i][19].length>0) {
			var rateOrderList = flist[i][19] ;
			for(var x=0;x<rateOrderList.length;x++){
				var rorder = rateOrderList[x];
				if(rorder.rateType == '1'){
					//加息券
					rateValue2 = "<i class='borlisdeta2'>券</i>您已使用" + rorder.rateValue
					+ "%加息券";
				}
				if(rateOrderList[x].rateType == '2'){
					//活动奖励
					rateValue = "<i class='borlisdeta2'>庆</i>一周年庆加息" + rorder.rateValue
					+ "%";
				}
			}
		}
		thtml +='<ul class="pre_ulbig">';
		if(i % 2==0)
			thtml +='<li>';
		else
			thtml +='<li class="pre_ulbig_hui">';
		thtml +='<ul class="pre_ulsmall">';
		thtml +='<li class="pre_ul_lismall_170 ">'+flist[i][0]+'</li>';
		thtml +='<li class="pre_ul_lismall_80 cjzq_list_hide" title="'+flist[i][1]+'">'+flist[i][1].substring(0,6)+'</li>';
		thtml +='<li>'+flist[i][2]+'</li>';
		thtml +='<li class="cjzq_list_hide2">'+flist[i][3]+'</li>';
		thtml +='<li class="cjzq_list_hide" >'+flist[i][4]+'</li>';
		thtml +='<li class="cjzq_list_hide3">'+flist[i][5]+'</li>';
		thtml +='<li class="cjzq_list_hide" >'+flist[i][6]+'</li>';
		thtml +='<li class="zqzr_list_hide">'+flist[i][8]+'</li>';
		thtml +='<li class="pre_ul_lismall_50 " ><i class="up"></i></li>';
		thtml +='</ul>';
		thtml +='</li>';

		thtml +='<div class="div_show display-none" >';
		thtml+='<div class="div_show_top">';
		thtml+='<dl class="dl_box1">';
		thtml+='<dd>借款标题：'+flist[i][11].loanApplicationTitle+'</dd>';
		thtml+='<dd>出借金额：'+flist[i][2]+'元</dd></dl>'+
			   '<dl class="dl_box2">';
		if(flist[i][11].loanType!='9'){
			thtml+=    '<dd>借款时长：'+flist[i][11].cycleCount+'个月</dd>'+
            '<dd>预期收益：'+flist[i][12]+'元</dd>'+
            '</dl>'+
            '<dl class="dl_box3">'+
            '<dd>年化利率：'+flist[i][11].annualRate+'%';
		}else {
            thtml+=   '<dd>借款时长：'+'14天</dd>'+
            '<dd>预期收益：'+flist[i][12]+'元</dd>'+
            '</dl>'+
            '<dl class="dl_box3">'+
            '<dd>年化利率：'+flist[i][11].annualRate+'%';
		}

    	if (flist[i][17] == 1 || (flist[i][17] == 2 && flist[i][14] != '放款')) {// 债权转让奖励判断
			thtml += awardRate;
		}
		thtml+='</dd><dd>还款方式：'+flist[i][11].repayMentMethod+'</dd>'+
			    '</dl>'+
			    '<dl class="dl_box4">';
		    		thtml+=	'<dd><img src="../images/lc_xq_05.jpg">'+
		    			'<a target="_blank" href="' + rootPath + "/finance/downloadAgreement?creditorRightsId=" + flist[i][13] + '">协议下载</a>'+
		    	    '</dd>';
			    	if(flist[i][6] == "已生效" && flist[i][16]==true){
			    		thtml+='<dd><img src="../images/cj_mx_04.jpg">'+
				    	'<a target="_blank" href="' + rootPath + "/finance/turnCreditRightBidding?creditorRightsId=" + flist[i][13] + '"  style="color:#f66;">债权转让</a>'+
				    	'</dd>'
			    	}
			    thtml+='</dl>'+
		  '</div>';
	    if (flist[i][17] == 1 || (flist[i][17] == 2 && flist[i][14] != '放款')) {// 债权转让奖励注判断
			thtml += '<p class="pborlde">' + award + '</p>';
		}	    
	    if (flist[i][19] != undefined && flist[i][19] != null && flist[i][19].length>0 && rateValue2!="" ) {// 出借债权加息或活动奖励标记
	    	thtml += '<p class="pborlde">' + rateValue2 + '</p>';
	    }	    
	    if (flist[i][19] != undefined && flist[i][19] != null && flist[i][19].length>0 && rateValue!="" ) {// 出借债权加息或活动奖励标记
	    	thtml += '<p class="pborlde">' + rateValue + '</p>';
	    }	    
		thtml +='<div class="div_show_bot">';
		thtml +='<ul class="div_show_bot_ultop"><li class="cjzq_list_hide" >回款期</li><li>回款日期</li><li>应回本金(元)</li><li>应回利息(元)</li><li class="cjzq_list_hide" >罚息(元)</li><li class="div_show_bot_ul2_120 cjzq_list_hide">应回款总额(元)</li><li class="div_show_bot_ul2_120 cjzq_list_hide3 ">已回款总额(元)</li><li class="cjzq_list_hide">应缴费用(元)</li> <li class="div_show_bot_ul2_60">状态</li></ul>';
		thtml +='<ul class="div_show_bot_ul">';

		for(var j=0; j<ylist[i].length; j++){
			thtml +='<li>';
			thtml +='<ul class="div_show_bot_ul2">';
			thtml +='<li class="cjzq_list_hide">'+ylist[i][j][0]+'</li>';
			thtml +='<li>'+ylist[i][j][1]+'</li>';
			thtml +='<li>'+ylist[i][j][2]+'</li>';
			thtml +='<li>'+ylist[i][j][3]+'</li>';
			thtml +='<li class="cjzq_list_hide">'+ylist[i][j][4]+'</li>';
			thtml +='<li class="div_show_bot_ul2_120 cjzq_list_hide">'+ylist[i][j][6]+'</li>';
			thtml +='<li class="div_show_bot_ul2_120 cjzq_list_hide3">'+ylist[i][j][7]+'</li>';
			thtml +='<li  class="cjzq_list_hide">'+ylist[i][j][5]+'</li>';
			thtml +='<li class="div_show_bot_ul2_60">'+ylist[i][j][8]+'</li>';
			thtml +='</ul>';
			thtml +='</li>';
		}
		thtml +='</ul>';
		thtml +='</div>';
		if(flist[i][17]==2 && flist[i][18] != null){			
			thtml +='<p><a href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+flist[i][18]+'">查看借款详情</a></p>';
		}else{
			thtml +='<p><a href="'+rootPath+'/finance/bidding?loanApplicationNo='+flist[i][11].loanApplicationId+'">查看借款详情</a></p>';
		}
		thtml +='</div>';
		thtml +='</div>';
		thtml +='</ul>';
	}
	$("#pre_ul_big").html(thtml);
}
