
 $(document).ready(function() {
	financeHtml(jhlist);
 })

//声明出借明细列表
var jhlist=new Array();
//查询出借明细
function financeHtml(flist){
		$.ajax({
		url:""+rootPath+"/finance/findCreditorRightsByDetailList?lendOrderId="+$("#lendOrderId").val(),
		type:"post",
		data:{},
		success:function(data){
			var _data = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<_data.length;i++){
				var dataResult = _data[i];
				flist[i] = [
							dataResult.loanApplicationCode,
							dataResult.loanApplicationName,
							dataResult.dueTime,
							dataResult.annualRate,
							dataResult.buyBalance,
							dataResult.expectProfit,
							dataResult.factBalance,
							dataResult.newWaitTotalpayMent,
							dataResult.rightsState,
							dataResult.dueTimeType
							];
				}
			sysH(_data.length,flist,pageCount);
			}
	});

		
	
}
function sysH(rows,flist,pageCount){
	//console.log(rows+"数组长度")
	var thtml='<li class="page_mian_li_title"><ul><li class="loanlist_li5 display_none">序号</li><li class="loanlist_li20">借款标题</li><li class="loanlist_li5 display_none">借款时长</li><li class="loanlist_li10 display_none">借款利率</li><li class="loanlist_li10">出借金额（元）</li><li class="loanlist_li15 display_none">预期收益（元）</li><li class="loanlist_li15">已回款金额（元）</li><li class="loanlist_li15">待回款金额（元）</li><li class="loanlist_li5 display_none">状态</li></ul></li>';
	if(rows!=0){
			for(var i=0;i<parseInt(rows);i++){
				 thtml +='<li class="page_mian_li_main"><ul><li class="loanlist_li5 display_none">'+(i+1)+'</li>';
				 thtml +='<li class="loanlist_li20">'+(flist[i][1] != null && flist[i][1].length>10?flist[i][1].substring(0,10):flist[i][1])+'</li>';
				 thtml +='<li class="loanlist_li5 display_none">'+flist[i][2]+getDueTimeType(flist[i][9])+'</li>';
				 thtml +='<li class="loanlist_li10 display_none">'+flist[i][3]+'%</li>';
				 thtml +='<li class="loanlist_li10">'+flist[i][4]+'</li>';
				 thtml +='<li class="loanlist_li15 display_none">'+flist[i][5]+'</li>';
				 thtml +='<li class="loanlist_li15">'+flist[i][6]+'</li>';
				 thtml +='<li class="loanlist_li15">'+flist[i][7]+'</li>';
				 thtml +='<li class="loanlist_li5 display_none">';
				 thtml += getCreditorRightsState(flist[i][8]) ;
				 
				 
				 thtml +='</li></ul></li>';
			} 
	}else{
				thtml +=' ';		
	}
			$('#pre_tabox2').html(thtml);
			$(".tcdPageCode").createPage({
		        pageCount:pageCount,
		        current:1,
		        backFn:function(p){
		            //console.log(p);
				//点击分页效果
				searchHtml(parseInt(p),10);	
		        }
		    });
			
			bottomB();
}
function searchHtml(page,rows){
	
	
	var thtml='<li class="page_mian_li_title"><ul><li class="loanlist_li5 display_none">序号</li><li class="loanlist_li20">结款标题</li><li class="loanlist_li5 display_none">结款时长</li><li class="loanlist_li10 display_none">结款利率</li><li class="loanlist_li10">出借金额（元）</li><li class="loanlist_li15 display_none">预期收益（元）</li><li class="loanlist_li15">已回款金额（元）</li><li class="loanlist_li15">待回款金额（元）</li><li class="loanlist_li5 display_none">状态</li></ul></li>';
	$.ajax({
		url:""+rootPath+"/finance/findCreditorRightsByDetailList?lendOrderId="+$("#lendOrderId").val(),
		type:"post",
		data:{"pageNo":page,"pageSize":rows},
		success:function(data){
			var _data = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<_data.length;i++){
				var dataResult = _data[i];
				
				 thtml +='<li class="page_mian_li_main"><ul><li class="loanlist_li5 display_none">'+(i+1)+'</li>';
				 thtml +='<li class="loanlist_li20">'+(dataResult.loanApplicationName != null &&dataResult.loanApplicationName.length>10?dataResult.loanApplicationName.substring(0,10):dataResult.loanApplicationName)+'</li>';
				 thtml +='<li class="loanlist_li5 display_none">'+dataResult.dueTime+getDueTimeType(dataResult.dueTimeType)+'</li>';
				 thtml +='<li class="loanlist_li10 display_none">'+dataResult.annualRate+'%</li>';
				 thtml +='<li class="loanlist_li10">'+dataResult.buyBalance+'</li>';
				 thtml +='<li class="loanlist_li15 display_none">'+dataResult.expectProfit+'</li>';
				 thtml +='<li class="loanlist_li15">'+dataResult.factBalance+'</li>';
				 thtml +='<li class="loanlist_li15">'+dataResult.newWaitTotalpayMent+'</li>';
				 thtml +='<li class="loanlist_li5 display_none">';
				 html += getCreditorRightsState(dataResult.rightsState) ;
				 thtml +='</li></ul></li>';
			
			} 

			$('#pre_tabox2').html(thtml);
			bottomB();
			}
	});
			
			
}

function getCreditorRightsState(state){
    
    if(state==0){
		return '已生效';
	}else if(state==1){
		return '还款中';
	}else if(state==2){
		return '已转出';
	}else if(state==3){
		return '已结清';
	}else if(state==4){
		return '已转出';
	}else if(state==5){
		return '申请转出';
	}else if(state==6){
		return '已转出(平台垫付)';
	}else if(state==7){
		return '提前结清';
	}else if(state==8){
		return '转让中';
	}else if(state==9){
		return '已锁定';
	}
}

function getDueTimeType(type){
	if(type == '1'){
		return "天";
	}else if(type=="2"){
		return "个月";
	}
}