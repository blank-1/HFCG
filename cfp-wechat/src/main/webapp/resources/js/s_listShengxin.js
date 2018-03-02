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
	var pageNo = 1, pageSize = 10, totalPage, totalPageRight=0, pageNoNew=1;
	var dataText="	 <p class='l_lastTip'>";
	var dataText1="";
	$(function(){
		/** ****数据加载页码等初始化条件 王亚东******** */
	
		//理财查询  chaxunUl
		$(".chujieUl li").on("click",function(){
			$("#lendOrder").html("");
			pageNo=1;
			if($(this).index()=="0"){/**index=1 是理财中，=2是已结清 =0是全部**/
				financeHtml(pageNo,pageSize,"");
			}else if($(this).index()=="1"){
				financeHtml(pageNo,pageSize,"1");
			}else{
				financeHtml(pageNo,pageSize,"3");/**creditorRightsStatus=""是全部，等于1是理财中，等于3是已结清**/
			}
			//调用方法并渲染数据 ，并传递值
			$(this).addClass("liCur").siblings().removeClass("liCur");
		})
		//资金流水
		$(".liushuiUl li").on("click",function(){
			var w_value = [];
			$(this).addClass("liCur").siblings().removeClass("liCur");
			$("#zjlist").html("");
			pageNoNew=1;
			if($(this).index()=="0"){/**0 all **/
				postRiverHtml(pageNoNew,pageSize,0);
			}else if($(this).index()=="1"){/**1 收入 **/
				postRiverHtml(pageNoNew,pageSize,1);
			}else if($(this).index()=="2"){/**2 支出 **/
				postRiverHtml(pageNoNew,pageSize,2);
			}else if($(this).index()=="3"){/** 3 冻结 **/
				postRiverHtml(pageNoNew,pageSize,4);
			}else{ /** 4 解冻 **/
				postRiverHtml(pageNoNew,pageSize,5);
			}
			
		})
		//tab切换
		$(".headNav").on("click","li",function(){
			var num = $(this).index();
			$(".headNav li").eq(num).addClass("listCur").siblings().removeClass("listCur");
			$(".mainList>div").eq($(".headNav>li").index(this)).show().siblings("div").hide();
		})
		//滚动加载
		$(document).on("scroll",function() {
				if(main.scrollLoading("mainBox","l_NewScroll")) {
					if($(".listCur").index()=="0"){//等于0 出借债权
					
						if(pageNo<totalPage){
							pageNo = pageNo+1;
							financeHtml(pageNo,pageSize,"");
						}
						
					}else{//等于1  资金流水
					
						if(pageNoNew<totalPageRight){
							pageNoNew = pageNoNew+1;
							postRiverHtml(pageNoNew,pageSize,"");
						}
						
					}
				}
		})		
		/********初始化理解列表*******/
		financeHtml(1,10,"");
		/********初始化资金流水列表*******/
		postRiverHtml(1,10,"");
		
		/****王亚东出借债权页面渲染开始 ***/
		function financeHtml(page,rows,creditorRightsStatus){
			if('3'==creditorRightsStatus){
				//7
			}
			//组织参数
			$.ajax({
				url:rootPath+"/finance/getSXJHCreditorRightsDetailList", // 查询该 省心计划下的债权列表
				type:"post",
				data: {
					"pageSize": rows,
					"pageNo": page,
 				     "creditorRightsStatus": creditorRightsStatus,
					"lendOrderId":$("#lendOrderId").val()
				},
				success: function (data) {
					var indexs = data.url;//当前索引值
					var d_rows = data.rows;
					totalPage = data.totalPage;
					if(0==d_rows.length){//开始
						if(!$(".mainList").find(".page").eq($(".listCur").index()).find("div").hasClass("noNum")){
							var noData ="<div class='noNum'> ";
							noData+="<img src=' ";
							noData+=rootPath+"/images/userCenter/no.png' />";
							noData+="<p>没有数据</p>";
							noData+="</div>";
							$(".mainList").find(".page").eq($(".listCur").index()).append(noData);//页面渲染
						}
						 return false;
					}else{
//						$(".mainList").find("#lendOrder").html("");//页面渲染
						$(".mainList").find(".page").eq($(".listCur").index()).find(".noNum").remove();
					}
					var thtml="";
					if(totalPage<pageNo){
						if(1!=pageNo){
							$(".chujieUl").append(dataText+"数据已加载完成</p>");
						}else{
							$(".chujieUl").append(dataText+"</p>");
						}
					
					}else{
						$(".chujieUl").find(".l_lastTip").remove();
						for(var i=0;i<d_rows.length;i++){
							var data = d_rows[i];
							var list = data.rightsRepaymentDetailList;
							/***********参数顺序：名字,借款时长，预期收益,购买金额***********/
							
							var num =$(".liCur").index();
							var index="";
							if(1==num){
								index=1;
							}else if(2==num){
								index=3
							}
							if(index!=indexs)
								return "";
							
							thtml+=makeHtml(data.creditorRightsName,
									data.loanApplicationListVO,fmoney(data.expectProfit,2),
									fmoney(data.buyPrice,2),data.rightsState,data.lendCustomerName)
							}
						if(totalPage==pageNo){
							if(1!=pageNo){
								dataText1 ="数据已加载完成</p>";
							}else{
								dataText1 ="";
							}
						}else{
							  dataText1 ="向下滑动加载更多</p>";
						}
						  $("#lendOrder").html("");//页面渲染
					     $("#lendOrder").append(thtml+dataText+dataText1);//页面渲染
					}
					}
			});

		}
		/*****************王亚东出借债权页面渲染结束 *******************/
		
		
		/************王亚东组装页面数据开始**********************/
		function makeHtml(creditorRightsName,loanApplicationListVO,menoy,buyPrice,rightsState,agreementEndDate){
			//获取当前
			var html="";
			html+=" <div class='huikBox'>";
			html+=" <p class='huikBox_p1'>";
			html+=" <span class='huikBox_p1Le'>"+showCreditoRightStatus(rightsState)+"</span>";
			if("3"!=rightsState&&"7"!=rightsState){
				html+="	<span class='huikBox_p1Ri'>距到期："+
				agreementEndDate
				+"天</span>";
			}
			html+=" </p>";
			html+=" <p class='huikBox_p2 text-overflow'>"+creditorRightsName+"</p>";
			html+=" <div class='huikBoxcon'>";
			html+=" <p class='huikBox_p3'>";
			html+=" <span class='huikBox_p3Le'><i>"+loanApplicationListVO.cycleCount+"</i>个月</span>";
			html+=" <span class='huikBox_p3Cen'>"+buyPrice+"</span>";
			html+=" <span class='huikBox_p3Ri'>"+menoy+"</span>";
			html+="  </p>";
			html+=" <p class='huikBox_p3 color_hui'>";
			html+=" <span class='huikBox_p3Le'>理财期限</span>";
			html+=" <span class='huikBox_p3Cen'>在投资金（元）</span>";
			html+=" <span class='huikBox_p3Ri'>预期收益（元）</span>";
			html+=" </p>";
			html+=" </div>";	 
			html+=" </div>";
			return html;
		}
		/************王亚东组装页面数据结束**********************/
		
		/************王亚东组装债权状态**********************/
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
	
	/*********王亚东资金流水请求数据**************/
	function postRiverHtml(page,rows,w_value){
		var thtml ="";
		var group ;
		if(w_value=="0")
			 group =["1","2","3","4","5"];
		else
			 group =[w_value];
		$.ajax({
			url:""+rootPath+"/finance/getSXJHFundDetailList?accId="+$("#accId").val(),
			type:"post",
			data:{"pageSize": rows,
				"pageNo": page,"flowType":group,"searchDate":["t_7", "t_1", "t_6"]},
			success:function(data){
				var _data = data.rows;
				var indexs = data.url;
				if(0==_data.length){//开始
					if(!$(".mainList").find(".page").eq($(".listCur").index()).find("div").hasClass("noNum")){
						var noData ="<div class='noNum'> ";
						noData+="<img src=' ";
						noData+=rootPath+"/images/userCenter/no.png' />";
						noData+="<p>没有数据</p>";
						noData+="</div>";
						$("#zjlist").append(noData);//页面渲染
						 return false;
					}
				}else{
//					$(".mainList").find(".page").eq($(".listCur").index()).remove(".noNum");
					$(".mainList").find(".page").eq($(".listCur").index()).find(".noNum").remove();
//					$(".noNum").remove();
				}//结束
				totalPageRight = data.totalPage;
				if(totalPageRight<pageNoNew){
					if(1!=pageNoNew){
						$("#zjlist").append(dataText+"数据已加载完成</p>");
					}else{
						$("#zjlist").append(dataText+"</p>");
					}
				}else{
					$("#zjlist").find(".l_lastTip").remove();
					for(var i=0;i<_data.length;i++){
						var dataResult = _data[i];
					/** *******王亚东资金流水html组装 顺序 时间、资金流水类型、资金流水类型明细,账户余额************ */
					var num =	$(".liushuiUl>.liCur").index();
						var index ="";
						if(num==1){
							index=1;
						}else if(num==2){
							index=2;
						}else if(num==3){
							index=4;
						}else if(num==4){
							index=5;
						}
						if(indexs!=index)
							return "";
						 thtml += makeRiverHtml(dataResult.changeTime,dataResult.changeType,dataResult.busType,
								dataResult.valueAfter2, getChangeMoney(dataResult));
					}
					
					if(totalPageRight==pageNoNew){
//						if(1!=pageNoNew){
							dataText1 ="数据已加载完成</p>";
//						}else{
//							dataText1 ="</p>";
//						}
					}else{
						  dataText1 ="向下滑动加载更多</p>";
					}
//					$("#zjlist").html("");
					$("#zjlist").append(thtml+dataText+dataText1);
				}
					
			
				 
				
			}
			});
	}
	/*********王亚东资金流水请求数据结束**************/
	
	
	
	
	
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
				html+="  </div>   </div>  <div>" +
						"  <p>"+dateTimeFormatter(changeTime)+"</p>  <p>余额 : <span>"+fmoney(valueAfter2, 2)+"</span></p> </div>";
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
