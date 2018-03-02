
//订单查询
$(document).ready(function() {
	searchHtml(5,10);
 })
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
			 
		thtml +='<div class="pre_zqlist_chaxun">';
		thtml +='<table class="pre_tabbox_chaxun" style="width:1000px;height:50px;">';
		thtml +='<tr class="pre_tabboxtitle_chaxun" ><td class="pre_tabbox1_200">日期</td><td class="pre_tabbox1_200">订单号</td><td class="pre_tabbox1_200">购买债权/产品</td><td class="pre_tabbox1_100">订单金额(元)</td><td class="pre_tabbox1_80">债权/理财</td><td class="pre_tabbox1_80">订单状态</td><td class="pre_tabbox1_140">操作</td></tr>';
		thtml +='</table>';
		thtml +='<table class="pre_tabbox2_chaxun">';
		for(var j=0;j<parseInt(rows);j++){
			thtml +='<tr><td class="pre_tabbox_200">2015-05-26 12:35:20</td><td class="pre_tabbox_200">DDH2014325928392</td><td class="pre_tabbox_200">消费借款-3个月-10%(年化率)</td><td class="pre_tabbox_100">10,000,00</td><td class="pre_tabbox_80">投标中</td><td class="pre_tabbox_80">未支付</td><td class="pre_tabbox_140"><span class="pre_red"><img src="../../images/chaxun_card.jpg" /><a href="">支付</a></span><span class="pre_blue"><a href="">查看</a></span></td></tr>';
		}
		thtml +='</table>';
		thtml +='<div style="height:50px;clear:both;"></div><div class="wrapper"><div class="tcdPageCode"  style="text-align:center;"></div></div>';
		thtml +='</div>';
		
		$("#ddlist").html(thtml);
			
}
//订单查询 ddlist end