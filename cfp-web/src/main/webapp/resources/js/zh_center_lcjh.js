
 //多选框效果
	function selectAll(checkbox) {
		$('input[class=c]').prop('checked', $(checkbox).prop('checked'));
		if(!checkbox.checked){
			$('input[class=c]').eq(0).prop('checked',true);
		}
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
  //多选框效果end
  
 $(document).ready(function() {
	financeHtml(4,jhlist);
	
 })

//声明省心计划列表
var jhlist=new Array();
//省心计划查询
function financeHtml(rows,flist){
	//console.log("1213")
	/*声明省心计划列表中各参数
	for(var i=0; i<=parseInt(rows); i++){
		 flist[i]=["信通宝","XTB20150001",3,10,10000.00,500.00,"进行中","2015-5-1","查看","index.do?financeId=1";
		 //1.省心计划名称，2.期数，3.封闭期，4.预期年化收益，5.购买金额(元)，6.预期收益(元)，7.状态，8.购买时间，9.操作
	 
	}*/
		
		 flist[0]=["信通宝","XTB20150001",3,10,10000.00,500.00,"2015-5-1","index.do?financeId=1"];
		 flist[1]=["信通宝","XTB20150001",3,10,10000.00,500.00,"2015-5-1","index.do?financeId=1"];
		 flist[2]=["信通宝","XTB20150001",3,10,10000.00,300.00,"2015-5-1","index.do?financeId=1"];
		 flist[3]=["信通宝","XTB20150001",3,10,10000.00,500.00,"2015-5-1","index.do?financeId=1"];
		 flist[4]=["信通宝","XTB20150001",3,10,10000.00,500.00,"2015-5-1","index.do?financeId=1"];
		 flist[5]=["信通宝","XTB20150001",3,10,10000.00,500.00,"2015-5-1","index.do?financeId=1"];
		
	
	sysH(rows,flist);
	/*
	
	var pamrt= "";  
	pamrt = pamrt+"&prdCat=1" ; 
	$.post('index.do?' +pamrt,{"page":page,"rows":rows },function(result){
			var myrows = result.rows;// 传值条数
			var total = result.total; //总条数
			var thtml = ''; // 页面显示
			
	 },'json'); 
	*/
	
}
function sysH(rows,flist){
	//console.log(rows+"数组长度")
	var thtml="";
	if(rows!=0){
			for(var i=0;i<parseInt(rows);i++){ 
				
				thtml +='<tr>';
				thtml +='<td class="pre_tabbox_le">'+flist[i][0]+'</td>';
				thtml +='<td>'+flist[i][1]+'</td>';
				thtml +='<td>'+flist[i][2]+'个月</td>';
				thtml +='<td class="pre_red">'+flist[i][3]+'%</td>';
				thtml +='<td>'+flist[i][4]+'</td>';
				thtml +='<td>'+flist[i][5]+'</td>';
						if(flist[i][6]==0){
							thtml +='<td>投标</td>';
						}else{
							thtml +='<td>理财</td>';
						}
				thtml +='<td>'+flist[i][6]+'</td>';
				thtml +='<td class="pre_blue"><a href="'+flist[i][7]+'">查看</a></td>';
				thtml +='</tr>';
				
			} 
		
	}else{
				thtml +=' ';
			
		
	}
	$('#pre_tabox').html(thtml);
	
}
function searchHtml(page,rows){
	var thtml="";
	/*
	
	var pamrt= "";  
	pamrt = pamrt+"&prdCat=1" ; 
	$.post('index.do?' +pamrt,{"page":page,"rows":rows },function(result){
			var myrows = result.rows;// 传值条数
			var total = result.total; //总条数
			var thtml = ''; // 页面显示
			
	 },'json'); 
	*/
			
			for(var i=0;i<parseInt(rows);i++){ 
				thtml +='<tr>';
				thtml +='<td class="pre_tabbox_le">'+flist[i][0]+'</td>';
				thtml +='<td>'+flist[i][1]+'</td>';
				thtml +='<td>'+flist[i][2]+'个月</td>';
				thtml +='<td class="pre_red">'+flist[i][3]+'%</td>';
				thtml +='<td>'+flist[i][4]+'</td>';
				thtml +='<td>'+flist[i][5]+'</td>';
						if(flist[i][6]==0){
							thtml +='<td>投标</td>';
						}else{
							thtml +='<td>理财</td>';
						}
				thtml +='<td>'+flist[i][6]+'</td>';
				thtml +='<td class="pre_blue"><a href="'+flist[i][7]+'">查看</a></td>';
				thtml +='</tr>';
			} 
			
			$('.flcontext2').html(thtml);
			
}