 // JavaScript Document
 
 $(document).ready(function(e) {
    dataHtml(1,10,true);
	
	//点击查询按钮
	$("#select").click(function(){
		dataHtml(1,10,true)
	});
});

//输出
function dataHtml(page,rows,flag){
	var thtml="";

	if(flag){
	}
	var state = $("#zindex").attr("data-id");
	var couponType = $("#zindex1").attr("data-id");
	var date1=$("#date1").val();//时间一
	var date2=$("#date2").val();//时间二

	$.ajax({
		url:rootPath+"/person/voucherList",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"state":state,
			"couponType":couponType,
			"startDate":date1,
			"endDate":date2
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<d_rows.length;i++){
				var _data = d_rows[i];
				var status = "";

				var couponName='';
				if(_data.voucherCouponType=='1'){
					couponName="财富券";
				}else if(_data.voucherCouponType=='2'){
					couponName="加息券";
				}else if(_data.voucherCouponType=='3'){
					couponName="提现券";
				}
				thtml += '<li><ul class="cfq-border"><li>'+couponName+'</li>' ;
				
				if(_data.voucherCouponType=='2'){
					thtml += '<li class="cfq-hide">'+_data.voucherValue+'%</li>';
				}else{
					thtml += '<li class="cfq-hide">'+_data.amountStr+'</li>';
				}
				
				thtml += '<li class="cfq_li_170 cfq-hide">'+dateTimeFormatter(_data.createDate)+'</li>';
				if(_data.endDate==null){
					thtml+='<li>长期有效</li>';
				}else{
					thtml += '<li>'+getDateStr(new Date(_data.endDate))+'</li>';
				}
				thtml += '<li class="cfq-hide">'+_data.sourceDescStr+'</li>';
				
				
				if(_data.voucherCouponType=='2' ){
					if(_data.status=='0' || _data.status == '1' ){
						thtml+='<li>可用</li>';
					}else{
						if(_data.voucherCouponType=='2'){
							if(_data.amount<1){
								thtml+= '<li  class="help-tip">不可用<p>使用完</p></li>';
							}else  {
								thtml+= '<li  class="help-tip">不可用<p>已过期</p></li>';
							}
						}else{
							thtml+= '<li  class="help-tip">不可用<p>'+_data.detailRemark+'</p></li>';
						}
					}
				}else{
					if(_data.status=='0' ){
						thtml+='<li>可用</li>';
					}else{
						if(_data.voucherCouponType=='2'){
							if(_data.amount<1){
								thtml+= '<li  class="help-tip">不可用<p>使用完</p></li>';
							}else  {
								thtml+= '<li  class="help-tip">不可用<p>已过期</p></li>';
							}
						}else{
							thtml+= '<li  class="help-tip">不可用<p>'+_data.detailRemark+'</p></li>';
						}
					}
				}
				
				
				
				//加息券，使用次数大于1，展示使用剩余次数
				var remark = "";
				if(_data.voucherCouponType=='2' && _data.amount>1){
					remark = _data.voucherRemark==null?",剩余可用"+_data.amount+"次":_data.voucherRemark+",剩余可用"+_data.amount+"次";
				}else{
					remark = _data.voucherRemark==null?"——":_data.voucherRemark;
				}
//				var remark = _data.voucherRemark==null?"——":_data.voucherRemark;
				var remark_ = "" ;
				if(remark.length>15){
					thtml += '<li class="cfq_li_205 help-tipcfq ">'+remark.substring(0,15)+"..."+'<p>'+remark+'</p></li></ul></li>';
				}else{
					thtml += '<li class="cfq_li_205 help-tipcfq ">'+remark+'</li></ul></li>';
				}
				
			}
			$('#pageList').html("");
			$('#pageList').append(thtml);
			bottomB();
			if(d_rows.length>0){
				if(flag){
					$(".tcdPageCode").createPage({
						pageCount:pageCount,
						current:1,
						backFn:function(p){
							//点击分页效果
							dataHtml(parseInt(p),10,false);
						}
					});
				}
			}else{
				$(".tcdPageCode").html("");
			}
			bottomB();
		}
	});
}

