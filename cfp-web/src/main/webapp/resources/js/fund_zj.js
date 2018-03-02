
//多选框效果
	function selectAllw(checkbox) {
		$('input[class=w]').prop('checked', $(checkbox).prop('checked'));
		if(!checkbox.checked){
			$('input[class=w]').eq(0).prop('checked',true);       
		}
		searchHtml(null,null,true);
	};
	function selectAlls(checkbox) {
		$('input[class=d]').prop('checked', $(checkbox).prop('checked'));
		if(!checkbox.checked){
			$('input[class=d]').eq(0).prop('checked',true);
		}
		searchHtml(null,null,true);
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
		$('.xuan .w').click(function(){
			if(!$(this).is(":checked")){
				$(".xuan").find("input[type=checkbox]").eq(0).prop("checked",false);
			}
			if(!checkp($('.xuan .w'))){
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
		
		/**
		 * 新版条件按钮控制
		 * */
		$("a.zjlstime, a.zjlstype").click(function(){
			if(!$(this).hasClass("a-head")){
				$(this).addClass("a-head").siblings("a").removeClass("a-head");
				searchHtml(null, null,true);
			}
		});
		
	});
	
function searchLike(flag){	
	searchHtml(null,null,true);
} 


//订单查询
$(document).ready(function() {
	searchHtml(1,10,true);
 })
function searchHtml(page,rows,flag){
		var thtml='<ul class="zjgl-ul"><li><ul class="fund_listtop"><li class="fund_li100 fund-hide">日期</li><li class="fund_li80">流水类型</li><li class="fund_li100 fund-hide">费用类型</li><li class="fund_li110 ">交易金额(元)</li><li class="fund_li110">冻结/解冻(元)</li><li class="fund_li110 fund-hide">账户余额(元)</li><li class="fund_li200 fund-hide">备注</li></ul></li></ul>';
		if(flag){
			$(".tcdPageCode").html("");
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
			url:""+rootPath+"/person/fundManageList?accId="+$("#accId").val(),
			type:"post",
			data:{"pageNo":page,"pageSize":rows,"flowType":w_value,"searchDate":d_value},
			success:function(data){
				var _data = data.rows;
				var pageCount = data.totalPage;
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
					 $(".tcdPageCode").createPage({
					        pageCount:pageCount,
					        current:1,
					        backFn:function(p){
					            //console.log(p);
							//点击分页效果
					            searchHtml(parseInt(p),10,false);
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

