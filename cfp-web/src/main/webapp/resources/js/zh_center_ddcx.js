//订单查询
$(document).ready(function() {
	searchHtml(1, 10, true);
});

function ddlbcx(){
	$("#orderCode1").val($("#orderCode").val());
	$("#orderState1").attr("data-id",$("#orderState").attr("data-id"));
	$("#startDate1").val($("#startDate").val());
	$("#endDate1").val($("#endDate").val());
	searchHtml(1, 10,true);
}

function searchHtml(page, rows, flag) {
	if(flag){
		$("#lendorder_list_page").html("");
		$("#lendorder_list_page").html('<div class="tcdPageCode"  style="text-align:center;"></div>');
	}
	var thtml = "";
	
	var orderCode = $("#orderCode1").val();
	var orderState = $("#orderState1").attr("data-id");
	var startDate = $("#startDate1").val();
	var endDate = $("#endDate1").val();
	
	$.ajax({
		url:rootPath+"/person/lendorder_list",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"orderCode" : orderCode,
			"orderState" : orderState,
			"startDate" : startDate,
			"endDate" : endDate
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;

			
			
			for (var j = 0; j < d_rows.length; j++) {
				var data = d_rows[j];
				thtml += '<li><ul><li class="pre_tabbox_150 ddcx-hide">' + dateTimeFormatter(data.buyTime) +'</li>';
				thtml += '<li class="pre_tabbox_150 ddcx-hide">' + data.orderCode + '</li>';
				thtml += '<li  class="pre_tabbox_200"><p class="" style="width:200px;white-space:nowrap;" title='+data.lendOrderName+'>' + ((data.lendOrderName != null && data.lendOrderName.length>18)? (data.lendOrderName.substring(0,18)+"..."):(data.lendOrderName))+ '</p></li>';
				thtml += '<li class="pre_tabbox_80">' + data.buyBalance + '</li><li class="pre_tabbox_70 ddcx-hide ddcx-zqlc-hide">' + parseProductType(data.productType) + '</li>';
				thtml += '<li class="pre_tabbox_80">' + parseOrderState(data.productType,data.orderState) + '</li>';
				if(data.orderState=='0'){//未支付 显示支付和查看按钮   否则只显示查看
					thtml += '<li class="pre_tabbox_100"><span class="pre_red"><a  onclick="toPay('+data.lendOrderId+','+data.productType+')" href="javascript:;">支付</a></span><span class="pre_blue"><a href="javascript:;" onclick="toOrderDetail('+data.lendOrderId+')">查看</a></span></li></ul></li>';
				}else{							
					thtml += '<li class="pre_tabbox_100"><span class="pre_blue"><a href="javascript:;" onclick="toOrderDetail('+data.lendOrderId+')">查看</a></span></li></ul></li>';
				}
			}
			
			$(".flcontexts").html(thtml);
			bottomB();
			if(flag){
				$(".tcdPageCode").createPage({
					pageCount:pageCount,
					current:1,
					backFn:function(p){
						//点击分页效果
						searchHtml(parseInt(p),10,false);
					}
				});
			}
			
			
		}
	});
}

function toPay(id,type){

	var payUrl = "";
	var validateUrl ="";
	if (type == '1') {
		//投标类产品
		payUrl = rootPath + "/finance/toPayForLender?lendOrderId=" + id;
		validateUrl = rootPath+"/finance/checkBidLoanByPay?lendOrderId="+id;
	}

	if (type == '2') {
		//省心计划产品
		payUrl = rootPath + "/finance/toPayFinanceOrder?lendOrderId=" + id;
		validateUrl = rootPath+"/finance/checkForToBuyFinanaceByPayAmount?lendOrderId="+id;
	}
	
	if (type == '3') {
		//债权转让
		payUrl = rootPath + "/finance/toPayRightsOrder?lendOrderId=" + id;
		validateUrl = rootPath+"/finance/checkCreditorRightsByPay?lendOrderId="+id;
	}

	$.ajax({//校验交易密码
		 url:validateUrl,
		 type:"post",
		 error : function(data) {
			 var data = data.responseText;
			 var _data = eval('('+data+')');
			 $("#tip").html(_data.errorMsg);
			 $("#alertDialog").click();
		 },
		 success:function(data){
			 var _data =  eval("("+data+")");
			 if(!_data.isSuccess){
				//失败，弹框提示
				 $("#tip").html(_data.info);
				 $("#alertDialog").click();
			 }else{
				window.location.href = payUrl;
			 }
		 }
	 });
}

function toOrderDetail(lendOrderId) {
	window.open(rootPath + '/person/to_lendorder_detail?lendOrderId=' + lendOrderId);
}

function parseProductType(productType){
	if(productType == '2'){
		return '理财中';
	}else if(productType == '1' || productType == '3'){
		return '投标中';
	}
}

function parseOrderState(productType,orderState){
	if(productType=='1' || productType == '3'){//出借
		if(orderState == '0'){
			return '未支付';
		}else if(orderState == '1'){
			return '还款中';
		}else if(orderState == '2'){
			return '已结清';
		}else if(orderState == '3'){
			return '已过期';
		}else if(orderState == '4'){
			return '已撤销';
		}else if(orderState == '5'){
			return '已支付';
		}else if(orderState == '6'){
			return '退出中';
		}else if(orderState == '7'){
			return '流标';
		}else {
			return '';
		}
	}else if(productType == '2'){//理财
		if(orderState == '0'){
			return '未支付';
		}else if(orderState == '1'){
			return '理财中';
		}else if(orderState == '2'){
			return '已结清';
		}else if(orderState == '3'){
			return '已过期';
		}else if(orderState == '4'){
			return '已撤销';
		}else if(orderState == '5'){
			return '已支付';
		}else if(orderState == '6'){
			return '授权期到期';
		}else {
			return '';
		}
	}else{
		return '';
	}
	
}