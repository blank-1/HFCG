// JavaScript Document
//require.js调用的主框架
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"main" : ["main"]
	}
})
require(['jquery',"main"],function($,main) {
	$(function(){
		function dateTimeFormatter(val,type) {//type等于1 只返回年月
			if (val == undefined || val == "")
				return "";
			var date;
			if (val instanceof Date) {
				date = val;
			} else {
				date = new Date(val);
			}
			var y = date.getFullYear();
			var m = date.getMonth() + 1;
			var d = date.getDate();
			var dateStr = y + '/' + (m < 10 ? ('0' + m) : m) + '/'
				+ (d < 10 ? ('0' + d) : d);
			return dateStr;
		}

		/**
		 * *************获取当前时间******************
		 */
		function getNowFormatDate() {
		    var date = new Date();
		    var seperator1 = "/";
		    var seperator2 = ":";
		    var month = date.getMonth() + 1;
		    var strDate = date.getDate();
		    if (month >= 1 && month <= 9) {
		        month = "0" + month;
		    }
		    if (strDate >= 0 && strDate <= 9) {
		        strDate = "0" + strDate;
		    }
		    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
		            + " " + date.getHours() + seperator2 + date.getMinutes()
		            + seperator2 + date.getSeconds();
		    return currentdate;
		}

		/**
		 * *************获取当前时间结束******************
		 */
		
	/**
	 * *************计算相差天数开始******************
	 */
		function DateDiff(sDate1, sDate2) { // sDate1和sDate2是yyyy-MM-dd格式
			var aDate, oDate1, oDate2, iDays;
			aDate = sDate1.split("/");
			oDate1 = new Date(aDate[1] + '/' + aDate[2] + '/' + aDate[0]); // 转换为yyyy-MM-dd格式
			aDate = sDate2.split("/");
			oDate2 = new Date(aDate[1] + '/' + aDate[2] + '/' + aDate[0]);
			iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 / 24); // 把相差的毫秒数转换为天数
			return iDays; // 返回相差天数
		}
		
		function DateDiffExceptDay(sDate1, sDate2) { // sDate1和sDate2是yyyy-MM-dd格式
			var aDate, oDate1, oDate2, iDays;
			oDate1 = new Date(sDate1 ); // 转换为yyyy-MM-dd格式
			oDate2 = new Date(sDate2 );
			iDays = parseInt(Math.abs(oDate1.getTime() - oDate2.getTime()) / 1000 / 60 / 60 / 24); // 把相差的毫秒数转换为天数
			return iDays; // 返回相差天数
		}

		/** *************计算相差天数结束 ****************** */ 
		//计算天数并返回数值
		function returnCompute(details,rightsState,repaymentRecord,date){
			var days ="";
			 if("已生效"==rightsState||"申请转出"==rightsState||"回款中"==rightsState){//只接受还款中的，或者转让中的，已生效的
				 for(var j=0;j<details.length;j++){
						if(7!=creditorRightsStatus){//已转出则不计算距到期时间  getNowFormatDate()
//								console.log(dateTimeFormatter(details[j].repaymentDayPlanned));
							if(dateTimeFormatter(details[j].repaymentDayPlanned)>=dateTimeFormatter(date)){
								days=DateDiff(
										dateTimeFormatter(details[j].repaymentDayPlanned),
										dateTimeFormatter(date));
								break;
							}
						}
					}
			 }else if("提前结清"==rightsState){//需要判断提前结清的当前时间
				//还款时间 
//				 dateTimeFormatter(repaymentRecord.faceDate);
				 var factDate= dateTimeFormatter(repaymentRecord.faceDate).split("/");
//				var factDate=repaymentRecord.faceDate.split("/");
//				if(parseInt(factDate[2])>parseInt(15)){
//					//大于15算下一期
//					 for(var j=0;j<details.length;j++){
//							if(2!=creditorRightsStatus){//已转出则不计算距到期时间
//								if((j+1)<(details.length-1)) //当前时间大于当前期数的时间，则计算下个月的还款时间 减去当前时间 如下个月没有还款则返回0
//									return "0";// 用下个月的
//									days=DateDiff(
//											dateTimeFormatter(details[j+1].repaymentDayPlanned),
//											dateTimeFormatter(getNowFormatDate()));
//									break;
//							}
//						}
//				}else{
//					//小于等于15算本期
//					days=DateDiff(dateTimeFormatter(getNowFormatDate()),
//							dateTimeFormatter(repaymentRecord.faceDate)); //格式化只有月份相减等于0则为相同其数	
//				}
				 days=false;
			 }else{
				 days=false;
			 }
			
			return days;
		}
		//转让中和已转出 /*******状态一共分为四种 第一种是转让中债权状态为8。 第二种是已转入 判断formwhere=2 为已转入 第三种是 可转让 转让的标的是 状态已生效（0） 并且债权大于30天 已转入的状态为2 *********************/
		var pageNoing = 1, pageSizeing = 10,pageNoOut = 1, pageSizeOut = 10,totalPageOut=1,creditorRightsStatus=8, totalPageing=1;//默认转入
		//已转入和可转让
		var pageNoAble = 1, pageSizeAble = 10,totalPageAble=1;//精英标
		getDetailHtml(pageNoing,pageSizeing);
		/*******发起请求**********/
		function getDetailHtml(pageNoing2,pageSizeing2){
			$.ajax({
				url : rootPath + "/finance/getCreditRightListByZhuangRan",
				type : "post",
				async: false,
				data : {
					"pageSize" : pageSizeing2,
					"creditorRightsStatus":creditorRightsStatus,
					"pageNo" : pageNoing2
				},
				success : function(data) {
					var dataText = "	 <p id ='transferId' class='l_lastTip'>";
					var dataText1 = "";
					//var details =data.data.rightsRepaymentDetailList;
					//计算当前期数
					if(8==creditorRightsStatus){//已转入
						totalPageing = data.data.totalPage;
						$("#transferId").remove();
						if (totalPageing == pageNoing) {
							if(0==data.data.rows.length){//开始
								if(!$(".mainList").find(".page").eq(data.data.index).find("div").hasClass("noNum")){
									var noData ="<div class='noNum'> ";
									noData+="<img src=' ";
									noData+=rootPath+"/images/userCenter/no.png' />";
									noData+="<p>没有数据</p>";
									noData+="</div>";
									$(".mainList").find(".page").eq(data.data.index).append(noData);//页面渲染
								}
								return false;
							}else{
								 dataText1 =" 数据已加载完成</p>";
							}// 结尾
							if(1!=pageNoing){
								dataText1 = "数据已加载完成</p>";
							}
							
						} else {
							dataText1 = "向下滑动加载更多</p>";
						}
						pageNoing=pageNoing+1;
						console.log("执行中的pageNoing= "+pageNoing);
					}else if(6==creditorRightsStatus){//可转让： 已生效 并且债权大于30天 
						totalPageAble	= data.data.totalPage;
						dataText = "	 <p id ='AbleId' class='l_lastTip'>";
						if(0==data.data.rows.length){//开始
							if(!$(".mainList").find(".page").eq(data.data.index).find("div").hasClass("noNum")){
								var noData ="<div class='noNum'> ";
								noData+="<img src=' ";
								noData+=rootPath+"/images/userCenter/no.png' />";
								noData+="<p>没有数据</p>";
								noData+="</div>";
								$(".mainList").find(".page").eq(data.data.index).append(noData);//页面渲染
							}
							return false;
						}
							 
						 $("#AbleId").remove();
						 	if (totalPageAble<=pageNoAble) {
						 		if(1!=pageNoAble){
									dataText1 = "数据已加载完成</p>";
								}
							} else {
								dataText1 = "向下滑动加载更多</p>";
							}
						 	
					    pageNoAble=pageNoAble+1;
					}else{//已转出： 2
						totalPageOut= data.data.totalPage;
						if(0==data.data.rows.length){//开始
							if(!$(".mainList").find(".page").eq(data.data.index).find("div").hasClass("noNum")){
								var noData ="<div class='noNum'> ";
								noData+="<img src=' ";
								noData+=rootPath+"/images/userCenter/no.png' />";
								noData+="<p>没有数据</p>";
								noData+="</div>";
								$(".mainList").find(".page").eq(data.data.index).append(noData);//页面渲染
							}
							return false;
						}
						if (totalPageOut<=pageNoOut) {
							if(1!=pageNoOut){
								dataText1 = "数据已加载完成</p>";
							}
						} else {
							dataText1 = "向下滑动加载更多</p>";
						}
						pageNoOut=pageNoOut+1;
						 $("#OutId").remove();
						 dataText = "	 <p id ='OutId' class='l_lastTip'>";
					}
					
					$(".mainList").find(".page").eq(data.data.index).append(
							getMakeHtmlFactory(data) + dataText + dataText1);
				
				}
			});
			
		}
		

		/*******组装html**********/
		function getMakeHtmlFactory(data){
			var rows = data.data.rows;
			data.data.newDate;
			var html ="";
			 for (var int = 0; int < rows.length; int++) {
				 html+= getMakeHtml(rows[int],data.data.newDate);
			 }
			return html;
		}
		
		
		
		
		
		/*******组装html**********/
		function getMakeHtml(right,date){
//			if(right.index!=$(".listCur").index())
//				return "";
			
			var stauts= "huikBox_p2";
//			console.log(right.rightsState);
			if (right.loanType == 0 || right.loanType == 3) {//信贷和企业信贷
				stauts = "huikBox_p2_belive";
			} else if (right.loanType == 1 || right.loanType == 7) {//1 房贷 个人房产直托
				stauts = "huikBox_p2_apartment";
			} else if (right.loanType == 2) {//2 企业车贷
				stauts = "huikBox_p2_car";
			} else if (right.loanType == 4) {//2 企业包里
				stauts = "huikBox_p2_baoli";
			} else if (right.loanType == 5) {// 基金
				stauts = "huikBox_p2_inverst";
			} else if (right.loanType == 6) {//企业标
				stauts = "huikBox_p2_company";
			}
			//先判断状态
			//在判断在当前期数
			var compute = returnCompute(right.details,right.rightsState,right.repaymentRecord,date);
			if(false==compute){
				compute=0;
			}
			var html="<a onclick=\"toDetail('"+right.creditorRightsId+"')\">";
			 html+="<div class='huikBox'>";
			 html+="<p class='huikBox_p1'>";
			 html+="<span class='huikBox_p1Le'>"+right.rightsState+"</span>";
			 if(false==compute){
				 html+="<span class='huikBox_p1Ri'></span>";
			 }else{
				 html+="<span class='huikBox_p1Ri'>下期回款倒计时："+compute+"天</span>";
			 }
			 html+="</p>";
			 var text=" text-overflow";
			 html+="<p class=" +stauts+text+" >"+right.creditorRightsName+"</p>";
			 html+="<div class='huikBoxcon'>";
			 html+="<p class='huikBox_p3'>";
			 html+="<span class='huikBox_p3Le'><i>"+right.details.length+"个月</i></span>";
			 html+="<span class='huikBox_p3Cen'>"+right.buyPrice+"</span>";
			 html+="<span class='huikBox_p3Ri'>"+right.expectProfit+"</span>";
			 html+="</p>";
			 html+="<p class='huikBox_p3 color_hui'>";
			 html+="<span class='huikBox_p3Le'>理财期限</span>";
			 html+="<span class='huikBox_p3Cen'>在投资金（元）</span>";
			 html+="<span class='huikBox_p3Ri'>预期收益（元）</span>";
			 html+="	</p> 	" +
			 		"</div> 	</div>  </a>";
			return html;
		}
	/******加载不同的状态的加载*****/
	function dice(num){
		if(num==0){//已转入
//			pageNoing=pageNoing+1;
			if (pageNoing <= totalPageing) {
				console.log("执行前的pageNoing= "+pageNoing);
				creditorRightsStatus=8;
				getDetailHtml(pageNoing,pageSizeing);//回款中
			}
		}else if(1==num){//可转让
//			pageNoAble=pageNoAble+1;
			if (pageNoAble <= totalPageAble) {
				creditorRightsStatus=6;
				getDetailHtml(pageNoAble,pageSizeAble);//回款中
			}
		}else{//已转出
//			pageNoOut=pageNoOut+1;
			if (pageNoOut <= totalPageOut) {
				creditorRightsStatus=7;
				getDetailHtml(pageNoOut,pageSizeOut);//已转出
			}
		}
	}
		

	 
		//tab切换
		$(".headNav").on("click","li",function(){
			var num = $(this).index();
			if(num==0){//已转入
				pageNoing=1;
			}else if(1==num){//可转让
				pageNoAble=1;
			}else{//已转出
				pageNoOut=1;
			}

			$(".headNav li").eq(num).addClass("listCur").siblings().removeClass("listCur");
			$(".mainList>div").eq($(this).index()).show().empty().siblings("div").hide();
			
			dice(num);
		})
	function onclickOnPage(){
		//tab切换
		$(".headNav").on("click","li",function(){
			$(".headNav").off("click");
			var num = $(this).index();
			if(num==0){//已转入
				pageNoing=1;
			}else if(1==num){//可转让
				pageNoAble=1;
			}else{//已转出
				pageNoOut=1;
			}

			$(".headNav li").eq(num).addClass("listCur").siblings().removeClass("listCur");
			$(".mainList>div").eq($(".headNav>li").index(this)).show().siblings("div").hide();
			dice(num);
		})
		
	}
		//滚动加载
		$(document).on("scroll",function() {
			//$(".headNav").off("click");
			if($(".l_lastTip:visible").text()=="数据已加载完成"){
				
			}
			if(main.scrollLoading("s_content","l_NewScroll")) {
				// 此处写加载列表的方法
		
			var num=$(".listCur").index();
			dice(num);
			}
			
		})
	})
})

function toDetail(id){
    $("#creditorRightsId").val(id);
    $("#queryForm").submit();
}