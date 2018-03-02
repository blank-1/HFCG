//账户中心-我的理财-省心计划-出借债权-资金流水tab
$(function(){
	
	/**
	 * 新版条件按钮控制
	 * */
	$("a.zjlstime, a.zjlstype").click(function(){
		if(!$(this).hasClass("a-head")){
			$(this).addClass("a-head").siblings("a").removeClass("a-head");
			searchHtml2(null, null,true);
		}
	});
	
});

//订单查询
$(document).ready(function() {
	searchHtml2(1,10,true);
})
 
function searchHtml2(page,rows,flag){
		var thtml='<ul class="zjgl-ul"><li><ul class="fund_listtop"><li class="fund_li100 fund-hide">日期</li><li class="fund_li80">流水类型</li><li class="fund_li100 fund-hide">费用类型</li><li class="fund_li110 ">交易金额(元)</li><li class="fund_li110">冻结/解冻(元)</li><li class="fund_li110 fund-hide">账户余额(元)</li><li class="fund_li200 fund-hide">备注</li></ul></li></ul>';
		if(flag){
			$(".tcdPageCode2").html("");
		}

		var w_value = [];//定义一个数组 
		var d_value = [];
		
		
		$(".zjlstype.a-head").each(function(){
			var value = $(this).attr("value");
			if(value==null||value=='')  {
				$(this).siblings(".zjlstype").each(function(){
					w_value.push($(this).attr("value"));
					if($(this).attr("value") == '2'){
		        		w_value.push(3);
		        	}
				});
			}else{
				w_value.push(value);
			}
		});
		$(".zjlstime.a-head").each(function(){
			var value = $(this).attr("value");
			if(value==null||value=='')  {
				$(this).siblings(".zjlstime").each(function(){
					d_value.push($(this).attr("value"));
				});
			}else{
				d_value.push(value);
			}
		});
		
		$.ajax({
			url:""+rootPath+"/finance/getSXJHFundDetailList?accId="+$("#accId").val(),
			type:"post",
			data:{"pageNo":page,"pageSize":rows,"flowType":w_value,"searchDate":d_value},
			success:function(data){
				var _data = data.rows;
				var pageCount2 = data.totalPage;
				thtml +='<ul class="fund_listmain">';
				for(var i=0;i<_data.length;i++){
					var dataResult = _data[i];
					
						thtml +='<li class="fund_listmain_li">'	
						thtml +='<ul class="zjgl-ulli-ul">'+
										'<li class="fund_listmain_li100 fund-hide">'+dateTimeFormatter(dataResult.changeTime)+'</li>'+
										'<li class="fund_listmain_li80">'+getChangeType(dataResult.changeType)+'</li>'+
										'<li class="fund_listmain_li100 fund-hide">'+dataResult.busType+'</li>'+
										'<li class="fund_listmain_li110"><b class="color_red ">'+getChangeMoney1(dataResult)+'</b></li>'+
										'<li class="fund_listmain_li110"><b class="color_red">'+getChangeMoney2(dataResult)+'</b></li>'+
										'<li class="fund_listmain_li110"><b class="color_black fund-hide">'+fmoney(dataResult.valueAfter2, 2)+'</b></li>'+
										'<li class="fund_listmain_li200 fund-hide" title="'+dataResult.desc+'">'+(dataResult.desc.length>30?dataResult.desc.substring(0,30):dataResult.desc)+'</li></ul>'	
						thtml +='</li>';
				} 
				thtml +='</ul>';
				$("#zjlist").html(thtml);
				bottomB();
				if(flag){
					 $(".tcdPageCode2").createPage2({
					        pageCount2:pageCount2,
					        current2:1,
					        backFn2:function(p){
					            //console.log(p);
							//点击分页效果
					            searchHtml2(parseInt(p),10,false);
					        }
					    });
				}
				}
		});
		
}

	
//订单查询 zjlist end

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

function getChangeMoney1(dataResult){
	if(dataResult.changeType ==2 || dataResult.changeType == 3){
		return "-"+fmoney(dataResult.changeValue2, 2);
	}else if(dataResult.changeType ==1){
		return "+"+fmoney(dataResult.changeValue2, 2);
	}else{
		return '0.00' ;
	}
}
	
function getChangeMoney2(dataResult){
	if(dataResult.changeType == 4 || dataResult.changeType == 5){
		return fmoney(dataResult.changeValue2, 2);
	}else{
		return '0.00' ;
	}
}

