	//声明省心计划列表
	var jhlist=new Array();
	$(document).ready(function() { 	
		financeHtml(jhlist);
	});
	
	$(function(){
		
		//[查询条件]省心计划状态
		$("a.d").click(function(){
			if(!$(this).hasClass("a-head")){
				$(this).addClass("a-head").siblings("a.d").removeClass("a-head");
				financeHtml(jhlist);
			}
		});
		
		//[查询条件]计划类型
		$("a.c").click(function(){
		 	if(!$(this).hasClass("a-head")){
		 		$(this).addClass("a-head").siblings("a.c").removeClass("a-head");
		 		financeHtml(jhlist);
		 	}
		});
		
	});

//省心计划初次查询
function financeHtml(flist){
	var d_value = $("a.d.a-head").attr("value");//态查询条件
	var c_value = $("a.c.a-head").attr("value");//态查询条件
	
	$.ajax({
		url:""+rootPath+"/finance/getAllMyFinanceList",
		type:"post",
		data:{
			"queryState":d_value,
			"queryType":c_value
		},
		success:function(data){
			var _data = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<_data.length;i++){
				var dataResult = _data[i];
				flist[i] = [
							dataResult.publishName,//省心计划名称
							dataResult.timeLimit,//省心期
							dataResult.dueTimeScope,//投在标的
							dataResult.buyBalance,//购买金额(元)
							dataResult.currentProfit2,//已获收益(元)
							dataResult.orderState,//状态
							dataResult.buyTime,//购买时间
							''+rootPath+'/finance/getAllMyFinanceListDetail?lendOrderId='+dataResult.lendOrderId];
				}
			sysH(_data.length,flist,pageCount);
			}
	});
}

//初次渲染
function sysH(rows,flist,pageCount){
	$("#myFinance").html("");
	$(".tcdPageCode").html("");
	var thtml='<li class="p-Right-ulli-bg"><ul><li class="p-Right-bot-ulli-95">省心计划名称</li><li class="p-Right-bot-ulli-60">省心期</li><li class="p-Right-bot-ulli-95">投资标的</li><li>购买金额(元)</li><li class="p-Right-ulli-hide">已获利息(元)</li><li class="p-Right-ulli-hide">状态</li><li class="p-Right-ulli-hide">购买时间</li><li class="p-Right-ulli-hide p-Right-bot-ulli-80">操作</li></ul></li>';
	if(rows!=0){
	
			for(var i=0;i<parseInt(rows);i++){
				thtml += '<li><ul class="p-Right-ulli-borb"><li class="p-Right-bot-ulli-95"  title='+flist[i][0]+'>'+((flist[i][0].length>6)? (flist[i][0].substring(0,6)+"..."):(flist[i][0]))+'</li>';
				thtml += '<li class="p-Right-bot-ulli-60"><b>'+flist[i][1]+'</b>个月</li>';
				thtml += '<li class="p-Right-bot-ulli-95"><b>'+flist[i][2]+'</b>月标的</li>';
				thtml += '<li><b>'+fmoney(flist[i][3],2)+'</b></li>';
				thtml += '<li class="p-Right-ulli-hide"><b>'+fmoney(flist[i][4],2)+'</b></li>';
				
				if(flist[i][5] == '0'){
					thtml +='<li class="p-Right-ulli-hide">未支付</li>';
				}else if(flist[i][5] == '1'){
					thtml +='<li class="p-Right-ulli-hide">理财中</li>';
				}else if(flist[i][5] == '2'){
					thtml +='<li class="p-Right-ulli-hide">已结清</li>';
				}else if(flist[i][5] == '3'){
					thtml +='<li class="p-Right-ulli-hide">已过期</li>';
				}else if(flist[i][5] == '4'){
					thtml +='<li class="p-Right-ulli-hide">已撤销</li>';
				}else if(flist[i][5] == '5'){
					thtml +='<li class="p-Right-ulli-hide">已支付</li>';
				}else if(flist[i][5] == '6'){
					thtml +='<li class="p-Right-ulli-hide">授权期结束</li>';
				}else{
					thtml +='<li class="p-Right-ulli-hide"></li>';
				}
				thtml +='<li class="p-Right-ulli-hide p-Right-ulli-wrap">'+dateTimeFormatter(flist[i][6])+'</li>';
				thtml +='<li class="p-Right-ulli-hide p-Right-bot-ulli-80"><a href="'+flist[i][7]+'" >查看</a></li></ul></li>';
			} 
	}else{
				thtml +=' ';	
	}
			$("#myFinance").html(thtml);
			$(".tcdPageCode").createPage({
		        pageCount:pageCount,
		        current:1,
		        backFn:function(p){
		            //console.log(p);
		            //console.log("加载")
				//点击分页效果
				searchHtml(parseInt(p),10);	
		        }
		    });
			bottomB();
	
}

//分页查询和渲染
function searchHtml(page,rows){
	var thtml='<li class="p-Right-ulli-bg"><ul><li class="p-Right-bot-ulli-95">省心计划名称</li><li class="p-Right-bot-ulli-60">省心期</li><li class="p-Right-bot-ulli-95">投资标的</li><li>购买金额(元)</li><li class="p-Right-ulli-hide">已获收益(元)</li><li class="p-Right-ulli-hide">状态</li><li class="p-Right-ulli-hide">购买时间</li><li class="p-Right-ulli-hide p-Right-bot-ulli-80">操作</li></ul></li>';

	var d_value = $("a.d.a-head").attr("value");//态查询条件
	var c_value = $("a.c.a-head").attr("value");//态查询条件
	
	$.ajax({
		url:""+rootPath+"/finance/getAllMyFinanceList",
		type:"post",
		data:{
			"pageNo":page,
			"pageSize":rows,
			"queryState":d_value,
			"queryType":c_value
		},
		success:function(data){
			var _data = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<_data.length;i++){
				var dataResult = _data[i];
				
				thtml += '<li><ul class="p-Right-ulli-borb"><li class="p-Right-bot-ulli-95"  title='+dataResult.publishName+'>'+((dataResult.publishName.length>6)? (dataResult.publishName.substring(0,6)+"..."):(dataResult.publishName))+'</li>';
				thtml += '<li class="p-Right-bot-ulli-60"><b>'+dataResult.timeLimit+'</b>个月</li>';
				thtml += '<li class="p-Right-bot-ulli-95"><b>'+dataResult.dueTimeScope+'</b>月标的</li>';
				thtml += '<li><b>'+fmoney(dataResult.buyBalance,2)+'</b></li>';
				thtml += '<li class="p-Right-ulli-hide"><b>'+fmoney(dataResult.currentProfit2,2)+'</b></li>';
				
				if(dataResult.orderState == '0'){
					thtml +='<li class="p-Right-ulli-hide">未支付</li>';
				}else if(dataResult.orderState == '1'){
					thtml +='<li class="p-Right-ulli-hide">理财中</li>';
				}else if(dataResult.orderState == '2'){
					thtml +='<li class="p-Right-ulli-hide">已结清</li>';
				}else if(dataResult.orderState == '3'){
					thtml +='<li class="p-Right-ulli-hide">已过期</li>';
				}else if(dataResult.orderState == '4'){
					thtml +='<li class="p-Right-ulli-hide">已撤销</li>';
				}else if(dataResult.orderState == '5'){
					thtml +='<li class="p-Right-ulli-hide">已支付</li>';
				}else if(dataResult.orderState == '6'){
					thtml +='<li class="p-Right-ulli-hide">授权期结束</li>';
				}else{
					thtml +='<li class="p-Right-ulli-hide"></li>';
				}
				thtml +='<li class="p-Right-ulli-hide p-Right-ulli-wrap">'+dateTimeFormatter(dataResult.buyTime)+'</li>';
				thtml +='<li class="p-Right-ulli-hide p-Right-bot-ulli-80"><a href="'+rootPath+'/finance/getAllMyFinanceListDetail?lendOrderId='+dataResult.lendOrderId+'" >查看</a></li></ul></li>';
				
			} 
			$('#myFinance').html(thtml);
			bottomB();
			}
	});
			
			
			
}
