// JavaScript Document
//require.js调用的主框架
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"main" : ["main"],
		"swal":["lib/sweetalert2"]
	}
})
require(['jquery',"main","swal"],function($,main,swal){
	var pageNo = 1, pageSize = 10, totalPage;//全部
	var pageNoIng = 1, pageSizeIng = 10, totalPageIng;//理财中
	var pageNoEnd = 1, pageSizeEnd = 10, totalPageEnd;//授权结束
	var pageNoFinal = 1, pageSizeFinal = 10, totalPageFinal;//已结清
	var dataText="	 <p class='l_lastTip'>";
	var dataText1="";
	$(function(){
		/** ****数据加载页码等初始化条件 王亚东******** */
	
		//理财查询  chaxunUl
		$(".liushuiUl li").on("click",function(){
			$(this).addClass("liCur").siblings().removeClass("liCur");
			$("#lendOrder").html("");
			if($(this).index()=="0"){/**index=1 是理财中，=2是授权结束 =0是全部**/
				pageNo=1;
				financeHtml(pageNo,pageSize,"");
			}else if($(this).index()=="1"){
				pageNo=1;
				financeHtml(pageNo,pageSize,0);//理财中
			}else if($(this).index()=="2"){//授权结束
				pageNo=1;
				financeHtml(pageNo,pageSize,1);
			}else{//已结清
				financeHtml(pageNo,pageSize,2);/**creditorRightsStatus=""是全部，等于1是理财中，等于3是已结清**/
			}
			//调用方法并渲染数据 ，并传递值
		
		})
		//tab切换
//		$(".headNav").on("click","li",function(){
//			var num = $(this).index();
//			$(".headNav li").eq(num).addClass("listCur").siblings().removeClass("listCur");
//			$(".mainList>div").eq($(".headNav>li").index(this)).show().siblings("div").hide();
//		})
		//滚动加载
		$(document).on("scroll",function() {
				if(main.scrollLoading("mainBox","l_NewScroll")) {
					var num=$(".liCur").index();

				if(num=="0"){/**index=1 是理财中，=2是授权结束 =0是全部**/
					financeHtml(pageNo,pageSize,"");
				}else if(num=="1"){
					financeHtml(pageNo,pageSize,0);//理财中
				}else if(num=="2"){//授权结束
					financeHtml(pageNo,pageSize,1);
				}else{//已结清
					financeHtml(pageNo,pageSize,2);/**creditorRightsStatus=""是全部，等于1是理财中，等于3是已结清**/
				}
			 
						
				}
		})		
		/********初始化理解列表*******/
		financeHtml(1,10,"");
		/********初始化资金流水列表*******/
		
		/****王亚东出借债权页面渲染开始 ***/
		function financeHtml(page,rows,creditorRightsStatus){
			//组织参数
			$.ajax({
				url:rootPath+"/finance/getAllMyFinanceList", // 查询该 省心计划下的债权列表
				type:"post",
				data: {
					"pageSize": rows,
					"queryState":creditorRightsStatus,
					"pageNo": page 
				},
				success: function (data) {
					var index = data.url;
					var d_rows = data.rows;
					totalPage = data.totalPage;
					if(totalPage==pageNo){
						if(0==d_rows.length){
							if(!$("div").hasClass("noNum")){
								var noData ="<div class='noNum'> ";
								noData+="<img src=' ";
								noData+=rootPath+"/images/userCenter/no.png' />";
								noData+="<p>没有数据</p>";
								noData+="</div>";
								$("#lendOrder").before(noData);//页面渲染
							}
							  return false;
						}else{
							if(1!=pageNo){
								 dataText1 =" 数据已加载完成</p>";
							}else{
								dataText1 =" </p>";  
							}
							
						}
					}else{
						  dataText1 ="向下滑动加载更多</p>";
					}
//					$(".noNum").empty();
					$(".noNum").remove();
					$(".l_lastTip").remove();
					var thtml="";
					if(""==creditorRightsStatus){
						dataText="	 <p class='l_lastTip' id='l_lastTip'>";
					}else if(0==creditorRightsStatus){
						dataText="<p class='l_lastTip' id='l_lastTip0'>";
					}else if(1==creditorRightsStatus){
						dataText="	 <p class='l_lastTip' id='l_lastTip1'>";
					}else if(2==creditorRightsStatus){
						dataText="	 <p class='l_lastTip' id='l_lastTip2'>";
					}
 					if(data.totalPage<pageNo){
 						if(1!=pageNo){
 							$(".l_lastTip").append(dataText+"数据已加载完成</p>");
						}else{
							$(".l_lastTip").append(dataText+"</p>");
						}
 					}else{
						for(var i=0;i<d_rows.length;i++){
							var data = d_rows[i];
							var list = data.rightsRepaymentDetailList;
							thtml+=makeHtml(data,index)
						}
						
							$("#lendOrder").append(thtml+dataText+dataText1);//页面渲染
//					}
					}
                    pageNo=pageNo+1;
				}
			});

		 	}
		/*****************王亚东出借债权页面渲染结束 *******************/
		
		
		/************王亚东组装页面数据开始**********************/
		function makeHtml(data,index){
			var num=$(".liCur").index();
			var indexs="";
			if(num==1){
				indexs=0;
			}else if(num==2){
				indexs=1;
			}else if(num==3){
				indexs=2;
			}
			console.log("当前位置indexs="+indexs);
			console.log("选择的位置index="+num);
			if(indexs!=index)
            return "";			
			var html="";
			html+=" <a onclick=\"toDetail('"+data.lendOrderId+"')\">";
			html+=" <div class='huikBox'>";
			html+=" <p class='huikBox_p1'>";
			html+=" <span class='huikBox_p1Le'>"+showCreditoRightStatus(data.orderState)+"</span>";
			if(null==data.agreementEndDate||""==data.agreementEndDate){
				html+="	<span class='huikBox_p1Ri'> "
	 		}else{
	 			if(2==data.orderState||6==data.orderState){
	 				html+="	<span class='huikBox_p1Ri'>最后到期时间："+	dateTimeFormatter1(data.agreementEndDate)
	 				+"</span>";
	 			}else{
	 				console.log(data.newDate);
	 				html+="	<span class='huikBox_p1Ri'>最后到期时间："+	DateDiff(
							dateTimeFormatter(data.agreementEndDate),
							dateTimeFormatter(data.newDate))+"天</span>";
	 			}
	 			
	 		}
			
		 		
			
			
			
			html+=" </p>";
			html+=" <p class='l_titleIcon_shengxin text-overflow'>"+data.publishName+"</p>";
			html+=" <div class='huikBoxcon'>";
			html+=" <p class='huikBox_p3'>";
			html+=" <span class='huikBox_p3Le'><i>"+data.timeLimit+"</i>个月</span>";
			html+=" <span class='huikBox_p3Cen'>"+data.buyBalance+"</span>";
			html+=" <span class='huikBox_p3Ri'>"+data.currentProfit2+"</span>";
			html+="  </p>";
			html+=" <p class='huikBox_p3 color_hui'>";
			html+=" <span class='huikBox_p3Le'>理财期限</span>";
			html+=" <span class='huikBox_p3Cen'>在投资金（元）</span>";
			html+=" <span class='huikBox_p3Ri'>已获收益（元）</span>";
			html+=" </p>";
			html+=" </div>";	 
			html+=" </div>";
			html+="</a>";
			return html;
		}
		/************王亚东组装页面数据结束**********************/

		/************王亚东组装债权状态**********************/
		function showCreditoRightStatus(v) {
			switch (v) {
				case '0':
					return "未支付";
					break
				case '1':
					return "理财中";
					break
				case '2':
					return "已结清";
					break
				case '3':
					return "已过期";
					break
				case '4':
					return "已撤销";
					break
				case '5':
					return "已支付";
					break
				case '6':
					return "授权期结束";
					break
				case '7':
					return "";
					break
			}
			//
		}
		/************王亚东组装债权状态结束**********************/
	/**
	 * *************计算相差天数开始王亚东******************
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
		/** *************计算相差天数结束 王亚东****************** */
		/**
		 * *************王亚东格式化时间开始******************
		 */
		function dateTimeFormatter(val) {

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
		
		function dateTimeFormatter1(val) {

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
			var dateStr = y + '-' + (m < 10 ? ('0' + m) : m) + '-'
					+ (d < 10 ? ('0' + d) : d);
			return dateStr;
		}
		/**
		 * *************王亚东格式化时间解暑 ******************
		 */ 
		
		/**
		 * *************王亚东获取当前时间******************
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
		 * *************王亚东获取当前时间结束******************
		 */
	})
	
	/*********王亚东资金流水html组装 顺序 时间、资金流水类型、资金流水类型明细,账户余额*************/
	function makeRiverHtml(changeTime,changeType,busType,valueAfter2,actionMoney){
		var image ="";// 
		if(changeType=="1"){//收入
			image ="/cfp-wechat/images/income.png";
		}else if(changeType =="2"||changeType =="3"){//2支出"  PAY_FROZEN("3","冻结资金支出"),
			image ="/cfp-wechat/images/expenses.png";
		}else if(changeType =="4"){//冻结
			image ="/cfp-wechat/images/Frozen.png";
		}else{//UNFREEZE("5","解冻"),
			image ="/cfp-wechat/images/Thaw.png";
		}
			var html="";
				html+="<li>  <dl>";
				html+="  <dd class='income'>";
				html+="  <img src='"+image+"'>";
				html+="</dd> <dt><div>";
				html+="  <p>"+busType+"</p> <p>"+getChangeType(changeType)+" : <span>"+actionMoney+"</span></p>";
				html+="  </div>   </div>    <p>"+dateTimeFormatter(changeTime)+"</p>  <p>余额 : <span>"+fmoney(valueAfter2, 2)+"</span></p>";
				html+=" </div>  </dt> </dl>  </li>";
		 return html;
	}
	/*********王亚东资金流水html组装结束**************/
	/*********王亚东资金流水操作的资金*************/	
	function getChangeMoney(dataResult){
		if(dataResult.changeType ==2 || dataResult.changeType == 3){
			return "-"+fmoney(dataResult.changeValue2, 2);
		}else if(dataResult.changeType ==1){
			return "+"+fmoney(dataResult.changeValue2, 2);
		}else if(dataResult.changeType == 4 || dataResult.changeType == 5){
			return fmoney(dataResult.changeValue2, 2);
		}else{
			return '0.00' ;
		}
	}
	/*********王亚东资金流水操作的资金结束*************/	
	
	function getChangeType(type){
		if(type ==2 || type == 3){
			return '支出';
		}else if(type == 1){
			return '收入';
		}else if(type == 4){
			return '冻结';
		}else if(type == 5){
			return '解冻';
		}
		return '' ;
	}
})

function toDetail(id){
    $("#lendOrderId").val(id);
    $("#queryForm").submit();
}